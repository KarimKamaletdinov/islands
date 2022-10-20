package com.agrogames.islandsofwar.engine.impl.navigator;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.map.abs.MapParams;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BigShipNavigator {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static List<Cell> buildRoute(Cell start, Cell finish){
        if(start.x != 1 && start.y != 1
                && start.x != MapParams.Width && start.y != MapParams.Height ||
                finish.x != 1 && finish.y != 1
                        && finish.x != MapParams.Width && finish.y != MapParams.Height)
            return new ArrayList<>();
        List<Direction> availableDirections = new ArrayList<>();
        availableDirections.add(Direction.Top);
        availableDirections.add(Direction.Right);
        availableDirections.add(Direction.Bottom);
        availableDirections.add(Direction.Left);

        if(start.y == MapParams.Height) {
            availableDirections.remove(Direction.Top);
            if(start.x > 1 && start.x < MapParams.Width){
                availableDirections.remove(Direction.Bottom);
            }
        }
        if(start.x == MapParams.Width) {
            availableDirections.remove(Direction.Right);
            if(start.y > 1 && start.y < MapParams.Height){
                availableDirections.remove(Direction.Left);
            }
        }
        if(start.y == 1) {
            availableDirections.remove(Direction.Bottom);
            if(start.x > 1 && start.x < MapParams.Width){
                availableDirections.remove(Direction.Top);
            }
        }
        if(start.x == 1) {
            availableDirections.remove(Direction.Left);
            if(start.y > 1 && start.y < MapParams.Height){
                availableDirections.remove(Direction.Right);
            }
        }

        List<List<Cell>> availableRoutes = new ArrayList<>();
        for (Direction direction:  availableDirections) {
            List<Cell> route = new ArrayList<>();
            Cell c = new Cell(start.x, start.y);
            route.add(c);
            int attempt = 0;
            while (!c.equals(finish) && attempt < 500){
                if(direction == Direction.Top){
                    c = new Cell(c.x, MapParams.Height);
                    if(c.x == finish.x) c = new Cell(c.x, finish.y);
                    route.add(c);
                    if(c.x == 1)
                        direction = Direction.Right;
                    else
                        direction = Direction.Left;
                } else if(direction == Direction.Bottom){
                    c = new Cell(c.x, 1);
                    if(c.x == finish.x)
                        c = new Cell(c.x, finish.y);
                    route.add(c);
                    if(c.x == 1)
                        direction = Direction.Right;
                    else
                        direction = Direction.Left;
                } else if(direction == Direction.Right){
                    c = new Cell(MapParams.Width, c.y);
                    if(c.y == finish.y)
                        c = new Cell(finish.x, c.y);
                    route.add(c);
                    if(c.y == 1)
                        direction = Direction.Top;
                    else
                        direction = Direction.Bottom;
                } else {
                    c = new Cell(1, c.y);
                    if(c.y == finish.y) c = new Cell(finish.x, c.y);
                    route.add(c);
                    if(c.y == 1)
                        direction = Direction.Top;
                    else
                        direction = Direction.Bottom;
                }
                attempt++;
            }
            availableRoutes.add(route);
        }

        availableRoutes.sort(Comparator.comparingInt(List::size));
        return availableRoutes.get(0);
    }
}
