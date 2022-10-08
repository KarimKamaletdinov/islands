package com.agrogames.islandsofwar.engine.impl.game;

import com.agrogames.islandsofwar.engine.abs.game.Bullet;

import java.util.ArrayList;
import java.util.List;

public class BulletAdder implements com.agrogames.islandsofwar.engine.abs.game.BulletAdder {
    private final List<Bullet> bullets = new ArrayList<>();
    @Override
    public void AddBullet(Bullet bullet) {
        bullets.add(bullet);
    }

    public List<Bullet> getBullets(){
        return bullets;
    }
}
