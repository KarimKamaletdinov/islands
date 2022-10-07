package com.agrogames.islandsofwar.rendermanager;

import com.agrogames.islandsofwar.graphics.abstractions.DrawTextureService;
import com.agrogames.islandsofwar.graphics.abstractions.RenderManager;
import com.agrogames.islandsofwar.graphics.abstractions.TextureBitmap;

public class Manager implements RenderManager {
    @Override
    public void Render(DrawTextureService service) {
        service.DrawTexture(5, 5, TextureBitmap.Car, 5, 5, 0);
        service.DrawTexture(10, 5, TextureBitmap.Car, 5, 5, 2);
    }
}
