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
    private float fromLastReload = 0;
    private float rotation;
    private Unit owner;

    public Weapon(Point relativeLocation, float reload, WeaponType type, float longRange) {
        this.relativeLocation = relativeLocation;
        this.reload = reload;
        this.type = type;
        this.longRange = longRange;
    }

    @Override
    public Point getLocation() {
        Point rr = relativeLocation.rotate(owner.getRotation());
        return new Point(owner.getLocation().x + rr.x, owner.getLocation().y + rr.y);
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, float deltaTime) {
        for (Unit enemy: provider.getEnemies()){
            rotate(enemy);
            break;
        }

        fromLastReload += deltaTime;
        if(fromLastReload < reload) return;
        fromLastReload = 0;

        Point location = getLocation();
        Stream<Unit> enemies = Arrays.stream(provider.getEnemies());
        Unit[] near = enemies.filter(this::isNear).filter(e -> isAvailable(e, provider.getAll()))
                .sorted(Comparator.comparingInt(e -> (int) getDist(e.getLocation(), location))).toArray(Unit[]::new);
        for (Unit enemy: near){
            rotate(enemy);
            shoot(bulletAdder, enemy);
            return;
        }
    }

    private void shoot(BulletAdder bulletAdder, Unit enemy) {
        Point enemyLocation = enemy.getLocation();
        Bullet bullet = BulletFactory.Create(this);
        bullet.setGoal(enemyLocation);
        bulletAdder.AddBullet(bullet);
    }

    private void rotate(Unit enemy) {
        Point location = getLocation();
        Point enemyLocation = enemy.getLocation();
        rotation = getAngle(enemyLocation, location);
    }

    private boolean isAvailable(Unit enemy, MapObject[] all){
        Point location = getLocation();
        Point enemyLocation = enemy.getLocation();
        float r = getAngle(enemyLocation, location);
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
                        Cell c = new Cell(new Point(x, y));
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
