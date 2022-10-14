package com.agrogames.islandsofwar.engine.impl.weapon;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.map.MapObject;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.weapon.WeaponType;
import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.impl.bullet.BulletFactory;

import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Stream;

class Weapon implements com.agrogames.islandsofwar.engine.abs.weapon.Weapon {
    private final Point relativeLocation;
    private final float reload;
    private final WeaponType type;
    private final float longRange;
    private final float rotationSpeed;
    private float fromLastReload = 0;
    private float relativeRotation;
    private Float goalRotation;
    private Unit owner;

    public Weapon(Point relativeLocation, float reload, WeaponType type, float longRange, float rotationSpeed) {
        this.relativeLocation = relativeLocation;
        this.reload = reload;
        this.type = type;
        this.longRange = longRange;
        this.rotationSpeed = rotationSpeed;
    }

    @Override
    public Point getLocation() {
        Point rr = relativeLocation.rotate(owner.getRotation());
        return new Point(owner.getLocation().x + rr.x, owner.getLocation().y + rr.y);
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
    public void update(MapProvider provider, BulletAdder bulletAdder, float deltaTime) {
        Point location = getLocation();
        Stream<Unit> enemies = Arrays.stream(provider.getEnemies());
        Unit[] near = enemies.filter(this::isNear).filter(e -> isAvailable(e, provider.getAll()))
                .sorted(Comparator.comparingInt(e -> (int) getDist(e.getLocation(), location))).toArray(Unit[]::new);

        for (Unit enemy: near){
            setRotation(enemy);
            break;
        }

        rotate(deltaTime);

        fromLastReload += deltaTime;
        if(fromLastReload < reload) return;
        fromLastReload = 0;

        for (Unit enemy: near){
            setRotation(enemy);
            shoot(bulletAdder, enemy);
            return;
        }
    }

    private void shoot(BulletAdder bulletAdder, Unit enemy) {
        //if (getRotation() != goalRotation) return;
        Point enemyLocation = enemy.getLocation();
        Bullet bullet = BulletFactory.Create(this);
        bullet.setGoal(enemyLocation);
        bulletAdder.AddBullet(bullet);
    }

    private void setRotation(Unit enemy) {
        Point location = getLocation();
        Point enemyLocation = enemy.getLocation();
        goalRotation = getAngle(enemyLocation, location);
        if(goalRotation > Math.PI * 2f){
            goalRotation-= (float) (Math.PI * 2f);
        }
        if(goalRotation < 0){
            goalRotation+= (float)Math.PI * 2f;
        }
    }

    private void rotate(float deltaTime){
        if(goalRotation == null) return;
        float r = getRotation();
        if(r == goalRotation) return;
        if(r > Math.PI * 2f){
            r-=Math.PI * 2f;
        }
        if(r < 0){
            r+=Math.PI * 2f;
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

    private boolean isAvailable(Unit enemy, MapObject[] all){
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
                if(mapObject != owner && mapObject != enemy){
                    for (Cell cell: mapObject.GetTerritory()){
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

    private boolean isNear(Unit enemy){
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
    public WeaponType getType() {
        return type;
    }

    @Override
    public void setOwner(Unit owner) {
        this.owner = owner;
    }

    @Override
    public Unit getOwner() {
        return owner;
    }

    @Override
    public float getLongRange() {
        return longRange;
    }
}
