package com.agrogames.islandsofwar.engine.impl.bullet;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletAdder implements com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder {
    private final List<Bullet> bullets = new ArrayList<>();
    @Override
    public void addBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public List<Bullet> getBullets(){
        return bullets;
    }
}
