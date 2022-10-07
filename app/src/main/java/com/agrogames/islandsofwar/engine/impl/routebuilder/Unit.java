package com.agrogames.islandsofwar.engine.impl.routebuilder;

import com.agrogames.islandsofwar.engine.abs.common.Point;

public interface Unit {
    Point getLocation();
    Point getGoal();
    void setLocation(Point location);
    int getSpeed();
}
