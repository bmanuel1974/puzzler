package org.manuel.puzzle.model

//https://www.draw.io/#G1r0UGF2Xpmzd63b8t_vPR4Pe6KBGCfrNB
class Puzzle(val centerRows: Int, val rowsFromCenter: Int, val preFilled: List<String>? = null) {

    var hexagonList: MutableList<MutableList<Hexagon>> = mutableListOf()
    var centerRow = rowsFromCenter
    var maxState = CurrentState(mutableListOf())

    init {

        for (i in rowsFromCenter downTo 0) {
            val rows = (centerRows -1) - i
            val rowList = arrayListOf<Hexagon>()
            for (row in 0..rows) {
                val filled = preFilled != null && preFilled.contains("${hexagonList.size}$row")
                rowList.add(Hexagon(hexagonList.size, row, filled))
            }

            hexagonList.add(rowList)

        }
        for (i in 1..rowsFromCenter) {
            val rows = (centerRows -1) - i
            val rowList = arrayListOf<Hexagon>()
            for (row in 0..rows) {
                val filled = preFilled != null && preFilled.contains("${hexagonList.size}$row")
                rowList.add(Hexagon(hexagonList.size, row, filled))
            }

            hexagonList.add(rowList)
        }

        for((listIndex, list) in hexagonList.withIndex()) {
               for((rowIndex, row) in list.withIndex())  {
                  setSides(listIndex, rowIndex)
                   setTopAndBottom(listIndex, rowIndex)
               }
        }

        println(hexagonList)

    }

    fun setTopAndBottom(row: Int, position: Int) {
        val current = hexagonList[row][position]
       if (row < centerRow) {
          val oneSide = try {hexagonList[row-1][position -1]} catch (e: Exception)  {null}
          val twoSide = try {hexagonList[row-1][position]} catch (e: Exception)  {null}
          val fourSide = try {hexagonList[row+1][position+1]} catch (e: Exception)  {null}
          val fiveSide = try {hexagonList[row+1][position]} catch (e: Exception)  {null}
           current.also {
               it.oneSide = oneSide?.apply { FourSide = current}
               it.TwoSide = twoSide?.apply { FiveSide = current }
               it.FourSide = fourSide?.apply { this.oneSide = current}
               it.FiveSide = fiveSide?.apply {TwoSide = current}
           }

       } else if (row == centerRow) {
              val oneSide = try {hexagonList[row-1][position -1]} catch (e: Exception)  {null}
              val twoSide = try {hexagonList[row-1][position]} catch (e: Exception)  {null}
              val fourSide = try {hexagonList[row+1][position]} catch (e: Exception)  {null}
              val fiveSide = try {hexagonList[row+1][position-1]} catch (e: Exception)  {null}
               current.also {
                   it.oneSide = oneSide?.apply { FourSide = current}
                   it.TwoSide = twoSide?.apply { FiveSide = current }
                   it.FourSide = fourSide?.apply { this.oneSide = current}
                   it.FiveSide = fiveSide?.apply {TwoSide = current}
               }

       } else {
              val oneSide = try {hexagonList[row-1][position]} catch (e: Exception)  {null}
              val twoSide = try {hexagonList[row-1][position+1]} catch (e: Exception)  {null}
              val fourSide = try {hexagonList[row+1][position]} catch (e: Exception)  {null}
              val fiveSide = try {hexagonList[row+1][position-1]} catch (e: Exception)  {null}
               current.also {
                   it.oneSide = oneSide?.apply { FourSide = current}
                   it.TwoSide = twoSide?.apply { FiveSide = current }
                   it.FourSide = fourSide?.apply { this.oneSide = current}
                   it.FiveSide = fiveSide?.apply {TwoSide = current}
               }
       }

    }

    fun setSides(row: Int, position: Int) {
        val current = try {this.hexagonList[row][position]} catch (e: Exception) {null}
        val leftOf = try {this.hexagonList[row][position-1]} catch (e: Exception) {null}
        val rightOf = try {this.hexagonList[row][position+1]} catch (e: Exception) {null}

        current?.also {
           it.SixSide = leftOf
           it.ThreeSide = rightOf
        }

        leftOf?.also {
           it.ThreeSide = current
        }

        rightOf?.also {
           it.SixSide = current
        }
    }

    fun solve() {
        this.hexagonList.forEach { list ->
           list.forEach { row ->
               if (!row.preFilled) {
                   tryAllDirections(row, CurrentState(mutableListOf("${row.row}${row.position}")))
               }
           }
        }
    }

    fun tryAllDirections(row: Hexagon, state: CurrentState) {
        for (direction in 1..6) {
            val list = mutableListOf<String>()
            list.addAll(state.moves)

            val newState = CurrentState(list)
            var current = followDirection(row, direction, newState)
            var prev: Hexagon? = null
            while ( current != null) {
               prev = current
               current = followDirection(current, direction, newState)
            }

            if (prev != null) {
                tryAllDirections(prev, newState)
            }

            this.maxState
            if (newState.moves.size > this.maxState.moves.size) {
               this.maxState = newState
            }

        }
    }

    fun followDirection(current: Hexagon, direction: Int, state: CurrentState): Hexagon? {
        var move: Hexagon? = current

          when (direction) {
            1 -> {move = current.oneSide}
            2 -> {move = current.TwoSide}
            3 -> {move = current.ThreeSide}
            4 -> {move = current.FourSide}
            5 -> {move = current.FiveSide}
            6 -> {move = current.SixSide}
        }

        if (move != null && move != current
                && !state.moves.contains("${move.row}${move.position}")
                && !move.preFilled) {

            state.moves.add("${move.row}${move.position}")
            return move
        }
        return null
    }
}

fun main(args: Array<String>) {
//    val puzzle = Puzzle(9, 4)
    val puzzle = Puzzle(9, 4, listOf("01", "02", "03", "14", "31", "70", "71", "82"))
    puzzle.hexagonList.map { println(it) }
    puzzle.solve()

    println(puzzle.maxState.moves)

}




