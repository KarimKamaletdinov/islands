package com.agrogames.islandsofwar.engine.impl.another;

import com.agrogames.islandsofwar.engine.abs.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;
import com.agrogames.islandsofwar.types.AnotherObjectType;
import com.agrogames.islandsofwar.types.BulletType;

public class AnotherObject implements com.agrogames.islandsofwar.engine.abs.another.AnotherObject {
    private final Point location;
    private final float rotation;
    private final AnotherObjectType type;
    private final com.agrogames.islandsofwar.engine.abs.another.AnotherObject next;
    private final float lifeTime;
    private float timeSinceCreated;
    private boolean remove;

    public AnotherObject(Point location, float rotation, AnotherObjectType type, com.agrogames.islandsofwar.engine.abs.another.AnotherObject next, float lifeTime) {
        this.location = location;
        this.rotation = rotation;
        this.type = type;
        this.next = next;
        this.lifeTime = lifeTime;
    }

    public AnotherObject(Point location, float rotation, AnotherObjectType type, float lifeTime) {
        this(location, rotation, type, null, lifeTime);
    }

    @Override
    public Point getLocation() {
        return location;
    }

    @Override
    public float getRotation() {
        return rotation;
    }

    @Override
    public boolean shouldBeRemoved() {
        return remove;
    }

    @Override
    public AnotherObjectType getType() {
        return type;
    }

    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, AnotherAdder anotherAdder, float deltaTime) {
        timeSinceCreated += deltaTime;

        if(timeSinceCreated >= lifeTime && !remove){
            remove = true;
            if(next != null){
                anotherAdder.addAnother(next);
            }
        }
    }
}
