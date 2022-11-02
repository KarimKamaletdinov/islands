package com.agrogames.islandsofwar.engine.impl.bullet;

import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;

import java.util.ArrayList;
import java.util.List;

public class BulletAdder implements com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder {
    private final List<IBullet> bullets = new ArrayList<>();
    @Override
    public void addBullet(IBullet bullet) {
        bullets.add(bullet);
    }

    public List<IBullet> getBullets(){
        return bullets;
    }
}
