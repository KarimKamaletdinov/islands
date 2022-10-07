package com.agrogames.islandsofwar.rendermanager;

import com.agrogames.islandsofwar.engine.abs.Engine;
import com.agrogames.islandsofwar.engine.abs.game.GameObject;
import com.agrogames.islandsofwar.engine.impl.unit.UnitFactory;
import com.agrogames.islandsofwar.graphics.abs.TextureDrawer;
import com.agrogames.islandsofwar.graphics.abs.RenderManager;
import com.agrogames.islandsofwar.render.impl.Renderer;

public class Manager implements RenderManager {
    private final Engine engine;
    private final Renderer renderer;

    public Manager() {
        this.engine = new com.agrogames.islandsofwar.engine.impl.Engine(new GameObject[]{
                UnitFactory.Tank(1, 1)
        }, new GameObject[]{});
        this.renderer = new Renderer(new Presenter(this.engine));
    }

    @Override
    public void Render(TextureDrawer drawer) {
        engine.update(1);
        renderer.render(drawer);
    }
}
