package org.manuel.puzzle.model

class Hexagon(val row: Int, val position: Int, var filled :Boolean = false) {
    override fun toString(): String {
        return "Hexagon(row=${row}, position=$position)"
    }


}