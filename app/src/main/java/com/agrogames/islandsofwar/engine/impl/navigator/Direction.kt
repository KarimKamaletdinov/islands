package com.agrogames.islandsofwar.engine.impl.navigator

import com.agrogames.islandsofwar.engine.abs.common.Cell

enum class Direction {
    Top, Right, Bottom, Left;

    companion object {
        /**
         * goal != current
         * @return if goal==current, BOTTOM
         */
        fun primary(current: Cell?, goal: Cell?): Direction {
            return if (module(goal!!.x - current!!.x) > module(goal.y - current.y)) {
                if (goal.x > current.x) {
                    Right
                } else {
                    Left
                }
            } else {
                if (goal.y > current.y) {
                    Top
                } else {
                    Bottom
                }
            }
        }

        /**
         * goal != current
         * @return if goal==current, BOTTOM
         */
        fun secondary(current: Cell?, goal: Cell?): Direction {
            return if (module(goal!!.x - current!!.x) < module(goal.y - current.y)) {
                if (goal.x > current.x) {
                    Right
                } else {
                    Left
                }
            } else {
                if (goal.y > current.y) {
                    Top
                } else {
                    Bottom
                }
            }
        }

        /**
         * goal != current
         * @return if goal==current, BOTTOM
         */
        fun tertiary(current: Cell?, goal: Cell?): Direction {
            return if (module(goal!!.x - current!!.x) < module(goal.y - current.y)) {
                if (goal.x <= current.x) {
                    Right
                } else {
                    Left
                }
            } else {
                if (goal.y <= current.y) {
                    Top
                } else {
                    Bottom
                }
            }
        }

        /**
         * goal != current
         * @return if goal==current, BOTTOM
         */
        fun quaternary(current: Cell?, goal: Cell?): Direction {
            return if (module(goal!!.x - current!!.x) > module(goal.y - current.y)) {
                if (goal.x <= current.x) {
                    Right
                } else {
                    Left
                }
            } else {
                if (goal.y <= current.y) {
                    Top
                } else {
                    Bottom
                }
            }
        }

        private fun module(a: Int): Int {
            return if (a >= 0) a else -a
        }
    }
}