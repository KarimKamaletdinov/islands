package com.agrogames.islandsofwar.engine.impl.graphics;

import com.agrogames.islandsofwar.engine.abs.graphics.GraphicsAdder;
import com.agrogames.islandsofwar.engine.abs.graphics.IGraphicsObject;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.IUnitAdder;

public class GraphicsObject implements IGraphicsObject {
    private final Point location;
    private final float rotation;
    private final String texture;
    private final String spawnSound;
    private final IGraphicsObject next;
    private final float lifeTime;
    private float timeSinceCreated;
    private boolean remove;

    public GraphicsObject(String texture, Point location, float rotation, String spawnSound, IGraphicsObject next, float lifeTime) {
        this.location = location;
        this.rotation = rotation;
        this.texture = texture;
        this.spawnSound = spawnSound;
        this.next = next;
        this.lifeTime = lifeTime;
    }

    public GraphicsObject(String texture, Point location, float rotation, float lifeTime, String spawnSound) {
        this(texture, location, rotation, spawnSound, null, lifeTime);
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
    public String getSpawnSound() {
        return spawnSound;
    }

    @Override
    public String getTexture() {
        return texture;
    }

    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, IUnitAdder unitAdder, GraphicsAdder graphicsAdder, float deltaTime) {
        timeSinceCreated += deltaTime;

        if(timeSinceCreated >= lifeTime && !remove){
            remove = true;
            if(next != null){
                graphicsAdder.addGraphics(next);
            }
        }
    }
}
