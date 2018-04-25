package org.manuel.puzzle.model

class Hexagon(val row: Int, val position: Int, var filled :Boolean = false) {

    val preFilled = filled
    var oneSide: Hexagon? = null
    var TwoSide: Hexagon? = null
    var ThreeSide: Hexagon? = null
    var FourSide: Hexagon? = null
    var FiveSide: Hexagon? = null
    var SixSide: Hexagon? = null


    override fun toString(): String {
        return "Hexagon(row=${row}, position=$position)"
    }


}