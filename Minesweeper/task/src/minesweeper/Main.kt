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

    fun viewBoard() {
        gameBoard.oldDisplayBoard()
    }

    fun calculateWin(): Boolean {
        if(gameBoard.oldBoard == gameBoard.visibleBoard)  {
            println("Congratulations! You found all the mines!")
            return true
        } else {
            return false
        }
    }

    fun markSpot(x: Int, y: Int): Boolean {
        if(x - 1 >= 0
            && x - 1 < gameBoard.X_SIZE
            && y - 1 >= 0
            && y - 1 < gameBoard.Y_SIZE) {
            gameBoard.markMineInVisible(x - 1, y - 1)
            viewBoard()
        }

        return calculateWin()
    }

    fun play() {
        initiateGame()
        viewBoard()
        var won = false
        while(!won) {
            println("Set/delete mines marks (x and y coordinates):")
            val (inX, inY) = readln().split(" ").map{it.toInt()}
            won = markSpot(inX, inY)
        }
    }
}


class MinesweeperBoard(val numMines: Int, val X_SIZE: Int = 9, val Y_SIZE: Int = 9) {
    class Tile(val x: Int, val y: Int, var mined: Boolean = false) {
        val MINE_TILE: Char = 'x'
        val SAFE_TILE: Char = '.'
        val MARKED_TILE: Char = '*'
        val EXPLORED_TILE: Char = '/'

        var display: Char = SAFE_TILE
        var numMines: Int = 0
        var marked: Boolean = false
            set(value) {
                if(!explored) {
                    field = value
                    if (value) {
                        display = MARKED_TILE
                    }
                }
            }
        var explored: Boolean = false
            set(value) {
                field = value
                if(value) {
                    marked = false
                    if (mined) {
                        display = MINE_TILE
                    } else if (numMines > 0) {
                        display = numMines.digitToChar()
                    } else {
                        display = EXPLORED_TILE
                    }
                }
            }

    }

    private var board: MutableList<MutableList<Tile>> = MutableList(X_SIZE){ MutableList(Y_SIZE){Tile(0,0)} }
    var mineLocations: MutableList<MutableList<Int>> = MutableList(0) {
        MutableList(2){0}
    }

    init {
        board
        for(xi in 0..X_SIZE) {
            for(yi in 0..Y_SIZE) {
                board[xi][yi] = Tile(xi, yi)
            }
        }

        var i = 0
        while(i < numMines) {
            val randX = Random.nextInt(0 until X_SIZE)
            val randY = Random.nextInt(0 until Y_SIZE)

            if(isSafe(randX, randY)) {
                placeMine(randX, randY)
                i++
            }
        }

        calcNumMinesAll()
    }

    fun isSafe(x: Int, y: Int): Boolean {
        return board[x][y].mined
    }

    private fun placeMine(x: Int, y: Int) {
        board[x][y].mined = true
        mineLocations.add(mutableListOf(x, y))
    }

    fun displayBoard() {
        println("\n |123456789|")
        println("-|----------|")
        var i = 1
        for(mList in board) {
            print("$i|")
            for(tile in mList) {
                print(tile.display)
            }
            i++
            println("|")
        }
        println("-|---------|")
    }


    private fun calcNumMinesAll() {
        for(xi in 0 until X_SIZE) {
            for(yi in 0 until Y_SIZE) {
                calcNumMines(xi, yi)
            }
        }
    }

    private fun calcNumMines(x: Int, y: Int) {
        var numMines: Int = 0
        if (oldIsSafe(x, y)) {
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
            if(x + 1 < X_SIZE) {
                if(y > 0) {
                    if(isMined(x + 1, y - 1)) {
                        numMines++
                    }
                }
                if(isMined(x + 1, y)) {
                    numMines++
                }
                if(y + 1 < Y_SIZE) {
                    if(isMined(x + 1, y + 1)) {
                        numMines++
                    }
                }
            }

            if(numMines > 0) {
                board[x][y].numMines = numMines
            }

        }
    }






























    val MINE_TILE: Char = 'x'
    val SAFE_TILE: Char = '.'
    val MARKED_TILE: Char = '*'





    var oldBoard: MutableList<MutableList<Char>> = MutableList(9) {
            MutableList(9) {SAFE_TILE}
    }

    var visibleBoard: MutableList<MutableList<Char>> = oldBoard

    fun oldDisplayBoard() {
        println("\n |123456789|")
        println("-|----------|")
        var i = 1
        for(mList in visibleBoard) {
            print("$i|")
            for(tile in mList) {
                print(tile)
            }
            i++
            println("|")
        }
        println("-|---------|")
    }

    private fun oldPlaceMine(x: Int, y: Int) {
        oldBoard[x][y] = MINE_TILE
        mineLocations.add(mutableListOf(x, y))
    }

    fun isMined(x: Int, y: Int): Boolean {
        return oldBoard[x][y] == MINE_TILE
    }

    fun oldIsSafe(x: Int, y: Int): Boolean {
        return oldBoard[x][y] != MINE_TILE
    }



    fun oldcreateBoard(n: Int) {
        oldBoard = MutableList(Y_SIZE) {
            MutableList(X_SIZE) {SAFE_TILE}
        }


        var i = 0
        while (i < n) {
            val randX = Random.nextInt(0 until X_SIZE)
            val randY = Random.nextInt(0 until Y_SIZE)

            if(oldIsSafe(randX, randY)) {
                oldPlaceMine(randX, randY)
                i++
            }
        }

        oldcalcNumMinesAll()

        visibleBoard = oldBoard

        removeMinesFromVisible()
    }

    private fun removeMinesFromVisible() {
        for(mine in mineLocations) {
            visibleBoard[mine[0]][mine[1]] = SAFE_TILE
        }
    }

    fun markMineInVisible(x: Int, y: Int) {
        when {
            visibleBoard[x][y] == SAFE_TILE -> {
                visibleBoard[x][y] = MARKED_TILE
            }
            visibleBoard[x][y] == MARKED_TILE -> {
                visibleBoard[x][y] = SAFE_TILE
            }
            else -> {
                println("There is a number number here!")
            }
        }
    }



    private fun oldcalcNumMinesAll() {
        for(xi in 0 until X_SIZE) {
            for(yi in 0 until Y_SIZE) {
                oldcalcNumMines(xi, yi)
            }
        }
    }

    private fun oldcalcNumMines(x: Int, y: Int) {
        var numMines: Int = 0
        if (oldIsSafe(x, y)) {
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
            if(x + 1 < X_SIZE) {
                if(y > 0) {
                    if(isMined(x + 1, y - 1)) {
                        numMines++
                    }
                }
                if(isMined(x + 1, y)) {
                    numMines++
                }
                if(y + 1 < Y_SIZE) {
                    if(isMined(x + 1, y + 1)) {
                        numMines++
                    }
                }
            }

            if(numMines > 0) {
                oldBoard[x][y] = numMines.digitToChar()
            }

        }
    }
}