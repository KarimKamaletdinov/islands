package com.agrogames.islandsofwar.engine.impl.weapon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.IUnit;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.engine.abs.weapon.IWeapon;
import com.agrogames.islandsofwar.engine.impl.bullet.Bullet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Weapon implements IWeapon {
    private final Point relativeLocation;
    private final float reload;
    private final String texture;
    private final float longRange;
    private final float rotationSpeed;
    private final int damage;
    private final float speed;
    private final int flightHeight;
    private final int targetHeight;
    private final Point[] bulletsStarts;
    private final boolean bang;
    private final String bulletTexture;

    private float fromLastReload = 0;
    private float relativeRotation;
    private Float goalRotation;
    private IUnit owner;

    public Weapon(Point relativeLocation, float reload, String texture, float longRange,
                  float rotationSpeed, Point[] bulletsStarts, String bulletTexture, int damage, float speed, int flightHeight, int targetHeight, boolean bang) {
        this.relativeLocation = relativeLocation;
        this.reload = reload;
        this.texture = texture;
        this.longRange = longRange;
        this.rotationSpeed = rotationSpeed;
        this.bulletTexture = bulletTexture;
        this.damage = damage * bulletsStarts.length;
        this.speed = speed;
        this.flightHeight = flightHeight;
        this.targetHeight = targetHeight;
        this.bulletsStarts = bulletsStarts;
        this.bang = bang;
    }

    @Override
    public Point getLocation() {
        Point rr = relativeLocation.rotate(owner.getRotation());
        return new Point(owner.getLocation().x + rr.x, owner.getLocation().y + rr.y);
    }

    public Point[] getBulletStarts(){
        List<Point> result = new ArrayList<>();
        Point l = getLocation();
        float rotation = getRotation();
        for(Point s : bulletsStarts){
            Point rr = s.rotate(rotation);
            result.add(new Point(l.x + rr.x, l.y + rr.y));
        }
        return result.toArray(new Point[0]);
    }

    @Override
    public float getRotation() {
        float r = owner.getRotation() + relativeRotation;
        if(r > Math.PI * 2f){
            r-=Math.PI * 2f;
        }
        if(r < 0){
            r+=Math.PI * 2f;
        }
        return r;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, AnotherAdder anotherAdder, float deltaTime) {
        Point location = getLocation();
        Stream<IUnit> enemies = Arrays.stream(provider.getEnemies());
        IUnit[] near = enemies.filter(this::isNear).filter(e -> isAvailable(e, provider.getAll()))
                .sorted(Comparator.comparingInt(e -> (int) getDist(e.getLocation(), location))).toArray(IUnit[]::new);

        for (IUnit enemy: near){
            setRotation(enemy);
            break;
        }

        rotate(deltaTime);

        fromLastReload += deltaTime;
        if(fromLastReload < reload) return;

        for (IUnit enemy: near){
            setRotation(enemy);
            shoot(bulletAdder, enemy);
            return;
        }
    }

    private void shoot(BulletAdder bulletAdder, IUnit enemy) {
        if (!M.nearlyEquals(getRotation(), goalRotation)) return;
        fromLastReload = 0;
        Point enemyLocation = enemy.getTerritory().length == 0
                ? enemy.getLocation()
                : new Point(enemy.getTerritory()[0]);

        for(Point start : this.getBulletStarts()) {
            IBullet bullet = new Bullet(this.bulletTexture, start, this.speed, this.damage,
                    this.longRange, this.flightHeight, this.targetHeight, this.owner, this.bang);
            bullet.setGoal(enemyLocation);
            bulletAdder.addBullet(bullet);
        }
    }

    private void setRotation(IUnit enemy) {
        Point location = getLocation();
        Point enemyLocation = enemy.getLocation();
        goalRotation = getAngle(enemyLocation, location);
        if(goalRotation > Math.PI * 2f){
            goalRotation -= (float) (Math.PI * 2f);
        }
        if(goalRotation < 0){
            goalRotation += (float)Math.PI * 2f;
        }
    }

    private void rotate(float deltaTime){
        if(goalRotation == null) return;
        float r = getRotation();
        if(r > Math.PI * 2f){
            r-=Math.PI * 2f;
        }
        if(r < 0){
            r+=Math.PI * 2f;
        }
        if(r == goalRotation) return;
        if(r - goalRotation > Math.PI){
            r -= Math.PI * 2f;
        } else if(goalRotation - r > Math.PI){
            r += Math.PI * 2f;
        }
        if(r < goalRotation){
            r += Math.PI * deltaTime * rotationSpeed;
            if(r > goalRotation){
                r = goalRotation;
            }
        }
        else{
            r -= Math.PI * deltaTime * rotationSpeed;
            if(r < goalRotation){
                r = goalRotation;
            }
        }
        relativeRotation = r - owner.getRotation();
    }

    private boolean isAvailable(IUnit enemy, MapObject[] all){
        if(enemy.getMinDamage() > damage || enemy.getHeight() != targetHeight) return false;
        Point location = getLocation();
        Point enemyLocation = enemy.getLocation();
        float r = getAngle(enemyLocation, location);
        if(r > Math.PI * 2f){
            r-=Math.PI * 2f;
        }
        if(r < 0){
            r+=Math.PI * 2f;
        }
        float d = getDist(enemyLocation, location);

        float l = 0;
        float x = location.x;
        float y = location.y;

        while (l < d){
            l += 0.5f;
            x += Math.cos(r) * 0.5;
            y += Math.sin(r) * 0.5;

            for (MapObject mapObject: all) {
                if(mapObject != owner && mapObject != enemy && mapObject.getHeight() == flightHeight){
                    for (Cell cell: mapObject.getTerritory()){
                        Cell c = new Cell((int) x + 1, (int) y + 1);
                        if(cell.equals(c)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private float getAngle(Point p1, Point p2){
        float r = (float) Math.atan(
                ((double) p1.y - (double) p2.y) /
                        ((double) p1.x - (double) p2.x));
        if (p1.x  < p2.x){
            r += Math.PI;
        }
        return r;
    }

    private boolean isNear(IUnit enemy){
        Point el = enemy.getLocation();
        Point l = getLocation();
        float dist = getDist(el, l);
        return dist <= longRange;
    }

    private float getDist(Point p1, Point p2) {
        float dx = p1.x - p2.x;
        float dy = p1.y - p2.y;
        return (float) Math.sqrt(dx * dx + dy * dy);
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public void setOwner(IUnit owner) {
        this.owner = owner;
    }

    @Override
    public Point getRelativeLocation() {
        return relativeLocation;
    }

    @Override
    public float getLongRange() {
        return longRange;
    }

    @Override
    public int getDamage() {
        return damage;
    }
}
