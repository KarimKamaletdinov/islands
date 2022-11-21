package com.agrogames.islandsofwar.islands.abs;

public interface IslandProvider {
    Island[] getMy();
    Island[] getAttackable();
    Island getMyById(int id);
    Island getAttackableById(int id);
}
