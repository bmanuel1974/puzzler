package org.manuel.puzzle.model

import java.util.*

class Puzzle(val centerRows: Int, val rowsFromCenter: Int, val preFilled: List<String>? = null) {

    var hexagonList: MutableList<MutableList<Hexagon>> = mutableListOf()
    var centerRow = rowsFromCenter
    val random = Random()

    init {

        for (i in rowsFromCenter downTo 0) {
            val rows = (centerRows -1) - i
            val rowList = arrayListOf<Hexagon>()
            for (row in 0..rows) {
                val filled = preFilled != null && preFilled.contains("$i$row")
                rowList.add(Hexagon(hexagonList.size, row, filled))
            }

            hexagonList.add(rowList)

        }
        for (i in 1..rowsFromCenter) {
            val rows = (centerRows -1) - i
            val rowList = arrayListOf<Hexagon>()
            for (row in 0..rows) {
                val filled = preFilled != null && preFilled.contains("$i$row")
                rowList.add(Hexagon(hexagonList.size, row, filled))
            }

            hexagonList.add(rowList)
        }
    }

    fun moveToPosition(direction: Int, currentHexagon: Hexagon): Hexagon? {
        //this one is already full

        //Can't move up on first row
        if (isFirstRow(currentHexagon) && isMovingUp(direction)) {
            return null
        }

        //can't move down if we are currently on the last row
        if (isLastRow(currentHexagon) && isMovingDown(direction)) {
            return null
        }

        //can't move left if we are currently on the first element
        if (currentHexagon.position == 0 && isMovingLeft(direction)) {
            return null
        }

        //can't move left if we are currently on the first element
        if (isLastElementInRow(currentHexagon) && isMovingRight(direction)) {
            return null
        }


        try {
            if (currentHexagon.row <= centerRow && isMovingUp(direction)) {
                if (direction == 1 && !this.hexagonList[currentHexagon.row - 1][currentHexagon.position - 1].filled) {
                    return this.hexagonList[currentHexagon.row - 1][currentHexagon.position - 1]
                }
                if (direction == 2 && !this.hexagonList[currentHexagon.row - 1][currentHexagon.position].filled) {
                    return this.hexagonList[currentHexagon.row - 1][currentHexagon.position]
                }
            }

            if (currentHexagon.row < centerRow && isMovingDown(direction)) {
                if (direction == 4 && !this.hexagonList[currentHexagon.row + 1][currentHexagon.position + 1].filled) {
                    return this.hexagonList[currentHexagon.row + 1][currentHexagon.position + 1]
                }
                if (direction == 5 && !this.hexagonList[currentHexagon.row + 1][currentHexagon.position].filled) {
                    return this.hexagonList[currentHexagon.row + 1][currentHexagon.position]
                }
            }

            if (currentHexagon.row >= centerRow && isMovingDown(direction)) {
                if (direction == 4 && !this.hexagonList[currentHexagon.row + 1][currentHexagon.position].filled) {
                    return this.hexagonList[currentHexagon.row + 1][currentHexagon.position]
                }
                if (direction == 5 && !this.hexagonList[currentHexagon.row + 1][currentHexagon.position - 1].filled) {
                    return this.hexagonList[currentHexagon.row + 1][currentHexagon.position - 1]
                }
            }

            if (currentHexagon.row > centerRow && isMovingUp(direction)) {
                if (direction == 1 && !this.hexagonList[currentHexagon.row - 1][currentHexagon.position].filled) {
                    return this.hexagonList[currentHexagon.row - 1][currentHexagon.position]
                }
                if (direction == 2 && !this.hexagonList[currentHexagon.row - 1][currentHexagon.position + 1].filled) {
                    return this.hexagonList[currentHexagon.row - 1][currentHexagon.position + 1]
                }
            }

            if (isMovingLeft(direction)) {
                if (!hexagonList[currentHexagon.row][currentHexagon.position - 1].filled) {
                    return hexagonList[currentHexagon.row][currentHexagon.position - 1]
                }
            }
            if (isMovingRight(direction)) {
                if (!hexagonList[currentHexagon.row][currentHexagon.position + 1].filled) {
                    return hexagonList[currentHexagon.row][currentHexagon.position + 1]
                }
            }
        } catch (e: Exception) {

        }



        return null

    }

    private fun isLastElementInRow(currentHexagon: Hexagon): Boolean {
        return this.hexagonList[currentHexagon.position].size == currentHexagon.position
    }

    fun isFirstRow(currentHexagon: Hexagon) = currentHexagon.row == 0


    fun isMovingUp(direction: Int): Boolean {
        return when (direction) {
            1 -> true
            2 -> true
            else -> false
        }
    }

    fun isMovingDown(direction: Int): Boolean {
        return when (direction) {
            4 -> true
            5 -> true
            else -> false
        }
    }

    fun isMovingLeft(direction: Int): Boolean {
        return direction == 6
    }

    fun isMovingRight(direction: Int): Boolean {
        return direction == 3
    }

    fun isLastRow(currentHexagon: Hexagon): Boolean {
        return (currentHexagon.row == hexagonList.size)
    }

    fun isSolved(): Boolean {
        var solved = true
        this.hexagonList.map {
            it.map { if (!it.filled) solved = false }
        }

        return solved
    }

    fun reset() {

        hexagonList.forEach {
            it.forEach {
                if (this.preFilled != null && preFilled.contains("${it.row}${it.position}")) {

                } else {
                    it.filled = false
                }
            }
        }

    }

    fun solve() {
        var rowStart: Int = randNum(0, this.hexagonList.size)
        var row = this.hexagonList[rowStart]
        var startPosition = randNum(0, row.size)
        var currentHexagon: Hexagon = row[startPosition]
        currentHexagon.filled = true
        var moves: MutableList<String> = arrayListOf()

        var directionsTried = mutableMapOf(1 to false, 2 to false, 3 to false, 4 to false, 5 to false, 6 to false)
        moves.add("R${rowStart + 1}P$startPosition")
        while (true) {

            if (!triedAllDirections(directionsTried)) {
                val direction = randomDirectionNotTried(directionsTried)

                val moveTo = this.moveToPosition(direction, currentHexagon)
                if (moveTo != null) {
                    moves.add("R${moveTo.row}P${moveTo.position}-D$direction")
                    moveTo.filled = true
                    directionsTried = mutableMapOf(1 to false, 2 to false, 3 to false, 4 to false, 5 to false, 6 to false)
                    currentHexagon = moveTo
                }

            }

            if (this.isSolved()) {
                println("solved!")
                println(moves)
            }

            if (!this.isSolved() && triedAllDirections(directionsTried) || this.isSolved()) {

                this.reset()
                directionsTried = mutableMapOf(1 to false, 2 to false, 3 to false, 4 to false, 5 to false, 6 to false)


                rowStart = randNum(0, this.hexagonList.size)
                row = this.hexagonList[rowStart]
                startPosition = randNum(0, row.size)
                currentHexagon = row[startPosition]
                currentHexagon.filled = true
                moves.clear()
                moves.add("R${rowStart}P${startPosition}")
            }
        }


    }

    fun randomDirectionNotTried(directions: MutableMap<Int, Boolean>): Int {
        val tryInt = randNum(1, 7)
        if (directions.getOrDefault(tryInt, false)) {
            return randomDirectionNotTried(directions)
        }

        directions[tryInt] = true
        return tryInt
    }

    fun triedAllDirections(directions: Map<Int, Boolean>): Boolean {
        return directions.filter { it.value == true }.size == 6
    }

    fun randNum(from: Int, to: Int): Int {
        return random.nextInt(to - from) + from
    }
}

fun main(args: Array<String>) {
    val puzzle = Puzzle(6, 3, listOf("30", "31", "32", "34", "35"))
    puzzle.hexagonList.map { println(it) }
    puzzle.solve()

}



