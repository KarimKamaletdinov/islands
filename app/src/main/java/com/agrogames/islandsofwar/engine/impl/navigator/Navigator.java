package com.agrogames.islandsofwar.engine.impl.navigator;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.agrogames.islandsofwar.engine.abs.common.Cell;
import com.agrogames.islandsofwar.engine.abs.common.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Navigator {
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static Cell[] buildRoute(Point start, Cell finish, Cell[] map) {
        return buildSimpleRoute(new Cell(start), finish, Arrays.asList(map)).toArray(new Cell[0]);
    }

    private static List<Cell> buildSimpleRoute(Cell start, Cell finish, List<Cell> map){
        List<RouteCell> simpleRoute = new ArrayList<>();
        RouteCell location = new RouteCell(new Cell(start.x, start.y));
        simpleRoute.add(location);
        if(location.cell.equals(finish)) return new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            if(i < 0){
                return new ArrayList<>();
            }
            location = moveOptimally(location, finish, map, simpleRoute);
            if(location == null){
                i-=2;
                if(simpleRoute.size() > 1) {
                    simpleRoute.remove(simpleRoute.size() - 1);
                    location = simpleRoute.get(simpleRoute.size() - 1);
                }
            } else{
                simpleRoute.add(location);
                if(location.cell.equals(finish)) break;
            }
        }

        List<Cell> result = new ArrayList<>();
        for (RouteCell rc : simpleRoute){
            result.add(rc.cell);
        }
        return result;
    }

    private static RouteCell moveOptimally(RouteCell cell, Cell goal, List<Cell> map,
                                           List<RouteCell> route){
        RouteCell c = tryMove(cell, Direction.primary(cell.cell, goal), map, route);
        if(c == null) c = tryMove(cell, Direction.secondary(cell.cell, goal), map, route);
        if(c == null) c = tryMove(cell, Direction.tertiary(cell.cell, goal), map, route);
        if(c == null) c = tryMove(cell, Direction.quaternary(cell.cell, goal), map, route);
        return c;
    }

    private static RouteCell tryMove(RouteCell cell, Direction direction, List<Cell> map,
                                     List<RouteCell> route){
        if(cell.triedDirections.contains(direction))
            return null;
        Cell moved = move(cell.cell, direction);
        if(map.contains(moved)){
            return null;
        }
        RouteCell rc = new RouteCell(moved);
        if(route.contains(rc))
            return null;
        cell.triedDirections.add(direction);
        return rc;
    }

    private static Cell move(Cell cell, Direction direction){
        switch (direction){
            case Top:
                return new Cell(cell.x, cell.y + 1);
            case Right:
                return new Cell(cell.x + 1, cell.y);
            case Bottom:
                return new Cell(cell.x, cell.y - 1);
            case Left:
                return new Cell(cell.x - 1, cell.y);
            default:
                Log.e("IOW", "Unknown direction: " + direction);
                return new Cell(cell.x, cell.y);
        }
    }
}

