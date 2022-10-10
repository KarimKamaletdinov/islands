package com.agrogames.islandsofwar.render.impl;

import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.unit.Unit;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.graphics.abs.TextureBitmap;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.render.abs.Presenter;

public class Renderer implements com.agrogames.islandsofwar.render.abs.Renderer {
    private final Presenter presenter;

    public Renderer(Presenter presenter){
        this.presenter = presenter;
    }

    @Override
    public void render(TextureDrawer drawer) {
        drawer.DrawTexture(7.5f, 5, TextureBitmap.Background, 15, 10, 0);
        for (Unit unit: presenter.getAttackers()){
            GameObjectRenderer.render(unit, drawer);
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }
        for (Unit unit: presenter.getProtectors()){
            GameObjectRenderer.render(unit, drawer);
            for(Weapon weapon: unit.getWeapons()){
                GameObjectRenderer.render(weapon, drawer);
            }
        }
        for (Bullet bullet: presenter.getAttackersBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }
        for (Bullet bullet: presenter.getProtectorsBullets()){
            GameObjectRenderer.render(bullet, drawer);
        }
    }
}
