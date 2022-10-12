package com.agrogames.islandsofwar.engine.impl.navigator;

import com.agrogames.islandsofwar.engine.abs.common.Cell;

enum Direction {
    Top, Right, Bottom, Left;

    /**
     * goal != current
     * @return if goal==current, BOTTOM
     */
    public static Direction primary(Cell current, Cell goal) {
        if (module(goal.x - current.x) > module(goal.y - current.y)) {
            if (goal.x > current.x) {
                return Direction.Right;
            } else {
                return Direction.Left;
            }
        } else {
            if (goal.y > current.y) {
                return Direction.Top;
            } else {
                return Direction.Bottom;
            }
        }
    }

    /**
     * goal != current
     * @return if goal==current, BOTTOM
     */
    public static Direction secondary(Cell current, Cell goal) {
        if (module(goal.x - current.x) < module(goal.y - current.y)) {
            if (goal.x > current.x) {
                return Direction.Right;
            } else {
                return Direction.Left;
            }
        } else {
            if (goal.y > current.y) {
                return Direction.Top;
            } else {
                return Direction.Bottom;
            }
        }
    }

    /**
     * goal != current
     * @return if goal==current, BOTTOM
     */
    public static Direction tertiary(Cell current, Cell goal) {
        if (module(goal.x - current.x) < module(goal.y - current.y)) {
            if (goal.x <= current.x) {
                return Direction.Right;
            } else {
                return Direction.Left;
            }
        } else {
            if (goal.y <= current.y) {
                return Direction.Top;
            } else {
                return Direction.Bottom;
            }
        }
    }

    /**
     * goal != current
     * @return if goal==current, BOTTOM
     */
    public static Direction quaternary(Cell current, Cell goal) {
        if (module(goal.x - current.x) > module(goal.y - current.y)) {
            if (goal.x <= current.x) {
                return Direction.Right;
            } else {
                return Direction.Left;
            }
        } else {
            if (goal.y <= current.y) {
                return Direction.Top;
            } else {
                return Direction.Bottom;
            }
        }
    }

    private static int module(int a) {
        return a >= 0 ? a : -a;
    }
}
