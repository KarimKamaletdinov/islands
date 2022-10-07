package com.agrogames.islandsofwar.render.abs;

import com.agrogames.islandsofwar.engine.abs.game.GameObject;

public interface Presenter {
    GameObject[] getProtectors();
    GameObject[] getAttackers();
}
