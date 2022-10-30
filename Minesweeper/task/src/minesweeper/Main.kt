package minesweeper

import kotlin.random.Random
import kotlin.random.nextInt

fun main() {
    val game = MinesweeperGame()
    game.play()
}

class MinesweeperGame() {
    var gameBoard:MinesweeperBoard = MinesweeperBoard(0)


    fun initiateGame() {
        println("How many mines do you want on the field?")
        val num = readln().toInt()
        gameBoard = MinesweeperBoard(num)
    }

    fun play() {
        initiateGame()
        var won = false
        var ended = false
        while(!ended) {
            gameBoard.displayBoard()
            println("Set/unset mines marks or claim a cell as free:")
            val (inX, inY, action) = readln().split(" ")
            val x = inY.toInt() - 1
            val y = inX.toInt() - 1
            println(action == "mine")
            if(action == "mine") {
                won = gameBoard.markSpot(x, y)
                ended = won
            } else if(action == "free") {
                ended = !gameBoard.explore(x, y)
                if(gameBoard.calculateWin()) {
                    won = true
                    ended = true
                }
            }
        }
        gameBoard.displayBoard()
        if(!won) {
            println("You stepped on a mine and failed!")
        } else {
            println("Congratulations! You found all the mines!")
        }
    }
}


class MinesweeperBoard(val numMines: Int, val X_SIZE: Int = 9, val Y_SIZE: Int = 9) {
    class Tile(val x: Int, val y: Int, var mined: Boolean = false) {
        val MINE_TILE: Char = 'x'
        val UNEXPLORED_TILE: Char = '.'
        val MARKED_TILE: Char = '*'
        val SAFE_TILE: Char = '/'

        var display: Char = UNEXPLORED_TILE
        var numMines: Int = 0
        var marked: Boolean = false
            set(value) {
                if(!explored) {
                    field = value
                    display = if(field) { MARKED_TILE } else { UNEXPLORED_TILE }
                }
            }
        var explored: Boolean = false
            set(value) {
                field = value
                if (value) {
                    marked = false
                    if (mined) {
                        display = MINE_TILE
                    } else if (numMines > 0) {
                        display = numMines.digitToChar()
                    } else {
                        display = SAFE_TILE
                    }
                }
            }

    }

    private var board: MutableList<MutableList<Tile>> = MutableList(X_SIZE) { MutableList(Y_SIZE) { Tile(0, 0) } }
    var mineCoordinates: MutableList<MutableList<Int>> = MutableList(0) {
        MutableList(2) { 0 }
    }
    var markedCoordinates: MutableList<MutableList<Int>> = MutableList(0) {
        MutableList(2) { 0 }
    }

    init {
        board
        for (xi in 0 until X_SIZE) {
            for (yi in 0 until Y_SIZE) {
                board[xi][yi] = Tile(xi, yi)
            }
        }

        var i = 0
        while (i < numMines) {
            val randX = Random.nextInt(0 until X_SIZE)
            val randY = Random.nextInt(0 until Y_SIZE)

            if (isSafe(randX, randY)) {
                placeMine(randX, randY)
                i++
            }
        }

        calcNumMinesAll()
    }

    fun isSafe(x: Int, y: Int): Boolean {
        return !board[x][y].mined
    }

    fun isMined(x: Int, y: Int): Boolean {
        return board[x][y].mined
    }

    fun isExplored(x: Int, y: Int): Boolean {
        return board[x][y].explored
    }

    private fun placeMine(x: Int, y: Int) {
        board[x][y].mined = true
        mineCoordinates.add(mutableListOf(x, y))
    }

    fun displayBoard() {
        println("\n |123456789|")
        println("-|----------|")
        var i = 1
        for (mList in board) {
            print("$i|")
            for (tile in mList) {
                print(tile.display)
            }
            i++
            println("|")
        }
        println("-|---------|")
    }

    fun markSpot(x: Int, y: Int): Boolean {
        if (isValidCoordinate(x, y)) {
            if (!board[x][y].marked) {
                board[x][y].marked = true
                markedCoordinates.add(mutableListOf(x, y))
            } else {
                board[x][y].marked = false
                markedCoordinates.remove(mutableListOf(x, y))
            }
        }

        return calculateWin()
    }

    /**
     * return false if mine is found
     */
    fun explore(x: Int, y: Int): Boolean {
        if (isValidCoordinate(x, y)) {
            if (isMined(x, y)) {
                for(tile in mineCoordinates) {
                    board[tile[0]][tile[1]].explored = true
                }
                return false
            }
            autoExplore(x, y)
        }
        return true
    }

    private fun autoExplore(x: Int, y: Int) {
        if (isValidCoordinate(x, y)) {
            if (!isExplored(x, y)) {
                board[x][y].explored = true
                if(board[x][y].marked) {
                    markedCoordinates.remove(mutableListOf(x,y))
                }

                if (board[x][y].numMines == 0) {
                    autoExplore(x - 1, y - 1)
                    autoExplore(x - 1, y)
                    autoExplore(x - 1, y + 1)
                    autoExplore(x, y - 1)
                    autoExplore(x, y + 1)
                    autoExplore(x + 1, y - 1)
                    autoExplore(x + 1, y)
                    autoExplore(x + 1, y + 1)
                }
            }
        }
    }

    fun isValidCoordinate(x: Int, y: Int): Boolean {
        return x in 0 until X_SIZE && y in 0 until Y_SIZE
    }

    fun calculateWin(): Boolean {
        if (markedCoordinates.containsAll(mineCoordinates) && mineCoordinates.containsAll(markedCoordinates)) {
            return true
        }
        for (mList in board) {
            for (tile in mList) {
                if (!tile.explored && !tile.mined) {
                    return false
                }
            }
        }

        return true
    }


    private fun calcNumMinesAll() {
        for (xi in 0 until X_SIZE) {
            for (yi in 0 until Y_SIZE) {
                calcNumMines(xi, yi)
            }
        }
    }

    private fun calcNumMines(x: Int, y: Int) {
        var numMines: Int = 0
        if (isSafe(x, y)) {
            if (x > 0) {
                if (y > 0) {
                    if (isMined(x - 1, y - 1)) {
                        numMines++
                    }
                }
                if (isMined(x - 1, y)) {
                    numMines++
                }
                if (y + 1 < Y_SIZE) {
                    if (isMined(x - 1, y + 1)) {
                        numMines++
                    }
                }
            }
            if (y > 0) {
                if (isMined(x, y - 1)) {
                    numMines++
                }
            }
            if (y + 1 < Y_SIZE) {
                if (isMined(x, y + 1)) {
                    numMines++
                }
            }
            if (x + 1 < X_SIZE) {
                if (y > 0) {
                    if (isMined(x + 1, y - 1)) {
                        numMines++
                    }
                }
                if (isMined(x + 1, y)) {
                    numMines++
                }
                if (y + 1 < Y_SIZE) {
                    if (isMined(x + 1, y + 1)) {
                        numMines++
                    }
                }
            }
        }

        board[x][y].numMines = numMines
    }

}





