package com.agrogames.islandsofwar.engine.impl.another;

import com.agrogames.islandsofwar.engine.abs.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.abs.another.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;

public class GraphicsObject implements IGraphicsObject {
    private final Point location;
    private final float rotation;
    private final String texture;
    private final IGraphicsObject next;
    private final float lifeTime;
    private float timeSinceCreated;
    private boolean remove;

    public GraphicsObject(String texture, Point location, float rotation, IGraphicsObject next, float lifeTime) {
        this.location = location;
        this.rotation = rotation;
        this.texture = texture;
        this.next = next;
        this.lifeTime = lifeTime;
    }

    public GraphicsObject(String texture, Point location, float rotation, float lifeTime) {
        this(texture, location, rotation, null, lifeTime);
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
    public String getTexture() {
        return texture;
    }

    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, AnotherAdder anotherAdder, float deltaTime) {
        timeSinceCreated += deltaTime;

        if(timeSinceCreated >= lifeTime && !remove){
            remove = true;
            if(next != null){
                anotherAdder.addAnother(next);
            }
        }
    }
}
