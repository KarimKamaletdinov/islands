package com.agrogames.islandsofwar.engine.impl.unit;

import com.agrogames.islandsofwar.common.M;
import com.agrogames.islandsofwar.engine.abs.another.AnotherAdder;
import com.agrogames.islandsofwar.engine.abs.bullet.Bullet;
import com.agrogames.islandsofwar.engine.abs.bullet.BulletAdder;
import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;
import com.agrogames.islandsofwar.engine.abs.map.MapProvider;
import com.agrogames.islandsofwar.engine.abs.unit.UnitAdder;
import com.agrogames.islandsofwar.engine.abs.weapon.Weapon;
import com.agrogames.islandsofwar.factories.BulletFactory;
import com.agrogames.islandsofwar.map.abs.MapParams;
import com.agrogames.islandsofwar.types.UnitType;

import java.util.Stack;

public class Plane extends Unit{
    private Cell goal;
    private Direction direction;
    private Stack<Bullet> bombs;
    private Float timeFromLastBomb = null;

    public Plane(UnitType type, int health, float speed, float rotationSpeed) {
        super(type, new Cell(-1, -1), new Weapon[0], health, speed, rotationSpeed);
    }

    @Override
    public Cell[] getTerritory() {
        Cell l = new Cell(location);
        return new Cell[]{
                l,
                new Cell(l.x, l.y + 1),
                new Cell(l.x, l.y - 1),

                new Cell(l.x + 1, l.y),
                new Cell(l.x + 2, l.y + 1),
                new Cell(l.x + 2, l.y - 1),

                new Cell(l.x - 1, l.y),
                new Cell(l.x - 1, l.y + 1),
                new Cell(l.x - 1, l.y - 1),
        };
    }

    @Override
    public boolean isMoving() {
        return true;
    }

    @Override
    public int getHeight() {
        return 4;
    }
    @Override
    public void update(MapProvider provider, BulletAdder bulletAdder, UnitAdder unitAdder, AnotherAdder anotherAdder, float deltaTime) {
        if(goal == null) return;
        if(timeFromLastBomb != null) {
            bomb(bulletAdder);
            timeFromLastBomb += deltaTime;
        }

        if(direction == Direction.Right)
            location.x += speed * deltaTime;
        else if(direction == Direction.Left)
            location.x -= speed * deltaTime;
        else if(direction == Direction.Up)
            location.y += speed * deltaTime;
        else if(direction == Direction.Down)
            location.y -= speed * deltaTime;

        Cell l = new Cell(location);
        if(l.equals(goal) && timeFromLastBomb == null) timeFromLastBomb = 0.4f;
        if(l.x > MapParams.Width + 5 || l.x < -5 || l.y > MapParams.Height + 5 || l.y < -5){
            health.current = 0;
        }
    }

    private void bomb(BulletAdder bulletAdder){
        if(bombs == null) {
            bombs = new Stack<>();
            for(Bullet b : BulletFactory.createBombs(this)){
                bombs.push(b);
            }
        }
        if(timeFromLastBomb >= 0.4f && !bombs.isEmpty()){
            timeFromLastBomb -= 0.4f;
            Bullet b = bombs.pop();
            switch (direction){
                case Up:
                    b.setGoal(new Point(location.x, location.y + 0.5f));
                    break;
                case Right:
                    b.setGoal(new Point(location.x + 0.5f, location.y));
                    break;
                case Down:
                    b.setGoal(new Point(location.x, location.y - 0.5f));
                    break;
                case Left:
                    b.setGoal(new Point(location.x - 0.5f, location.y));
                    break;
            }
            bulletAdder.addBullet(b);
        }
    }

    @Override
    public void setGoal(Cell goal) {
        if(this.goal != null) return;
        int gx = goal.x;
        int gy = goal.y;
        int cdx = gx - MapParams.Width / 2;
        int cdy = gy - MapParams.Height / 2;
        if(M.module(cdx) > M.module(cdy)){
            if(cdx > 0){
                gx = MapParams.Width;
                direction = Direction.Left;
                rotation = (float) Math.PI;
            } else {
                direction = Direction.Right;
                rotation = 0;
                gx = 1;
            }
        } else {
            if(cdy > 0){
                gy = MapParams.Height;
                direction = Direction.Down;
                rotation = (float) Math.PI / 2f * 3f;
            } else {
                direction = Direction.Up;
                rotation = (float) Math.PI / 2f;
                gy = 1;
            }
        }
        location = new Point(new Cell(gx, gy));
        this.goal = goal;
    }

    private enum Direction{
        Up, Right, Down, Left
    }
}
