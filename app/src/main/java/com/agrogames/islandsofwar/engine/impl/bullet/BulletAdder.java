package com.agrogames.islandsofwar.engine.impl.bullet;

import com.agrogames.islandsofwar.engine.abs.bullet.IBullet;
import com.agrogames.islandsofwar.sounds.abs.SoundPlayer;

import java.util.ArrayList;
import java.util.List;

public class BulletAdder implements com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder {
    private final List<IBullet> bullets = new ArrayList<>();
    private final SoundPlayer soundPlayer;

    public BulletAdder(SoundPlayer soundPlayer) {
        this.soundPlayer = soundPlayer;
    }

    @Override
    public void addBullet(IBullet bullet) {
        bullets.add(bullet);
        if(bullet.getCreationSound() != null){
            soundPlayer.playSound(bullet.getCreationSound());
        }
    }

    public List<IBullet> getBullets(){
        return bullets;
    }
}
