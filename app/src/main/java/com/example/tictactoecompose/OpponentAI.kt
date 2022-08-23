package com.example.tictactoecompose


class OpponentAI(tiles: ArrayList<Tile>) {
    var bestMove : Int

    private class Move {
        var row: Int = -1
        var col: Int = -1
    }

    init {
        val board = createBoard(tiles)
        bestMove = findBestMove(board)
    }

    fun getWinner(tiles: ArrayList<Tile>): Winner {
        val board = createBoard(tiles)
        val evaluation = evaluate(board)
        if(!hasAvailableMoves(board) && evaluation != 10) return Winner.Draw

        return when(evaluation) {
            10 -> Winner.Player
            -10 -> Winner.AI
            else -> Winner.Unknown
        }
    }

    private fun createBoard(tiles: ArrayList<Tile>): ArrayList<ArrayList<Tile>> {
        val board = ArrayList<ArrayList<Tile>>()
        board.add(ArrayList())
        board.add(ArrayList())
        board.add(ArrayList())
        for(i in 0..2) {
            board[0].add(tiles[i])
        }
        for(i in 3..5) {
            board[1].add(tiles[i])
        }
        for(i in 6..8) {
            board[2].add(tiles[i])
        }
        return board
    }

    private fun evaluate(b: ArrayList<ArrayList<Tile>>) : Int {
        // Checking for Rows for X or O victory.
        for(row in 0..2) {
            if (b[row][0] == b[row][1] && b[row][1] == b[row][2]) {
                if (b[row][0].isPlayer)
                    return +10
                else if (b[row][0].isOpponent)
                    return -10
            }
        }

        // Checking for Columns for X or O victory.
        for(col in 0..2) {
            if (b[0][col] == b[1][col] && b[1][col] == b[2][col]) {
                if (b[0][col].isPlayer)
                    return +10
                else if (b[0][col].isOpponent)
                    return -10
            }
        }

        // Checking for Diagonals for X or O victory.
        if (b[0][0] == b[1][1] && b[1][1] == b[2][2]) {
            if (b[0][0].isPlayer)
                return +10
            else if (b[0][0].isOpponent)
                return -10
        }

        if (b[0][2] == b[1][1] && b[1][1] == b[2][0]) {
            if (b[0][2].isPlayer)
                return +10
            else if (b[0][2].isOpponent)
                return -10
        }

        // Else if none of them have won then return 0
        return 0
    }

    private fun hasAvailableMoves(board: ArrayList<ArrayList<Tile>>): Boolean {
        for (i in 0..2) for (j in 0..2) if (!board[i][j].isOpponent && !board[i][j].isPlayer) return true
        return false
    }

    private fun minimax(board: ArrayList<ArrayList<Tile>>, depth: Int, isMax: Boolean): Int {
        val score = evaluate(board)

        // If Maximizer has won the game
        // return his/her evaluated score
        if (score == 10) return score

        // If Minimizer has won the game
        // return his/her evaluated score
        if (score == -10) return score

        // If there are no more moves and
        // no winner then it is a tie
        if (hasAvailableMoves(board)) return 0

        // If this maximizer's move
        return if (isMax) {
            var best = -1000

            // Traverse all cells
            for (i in 0..2) {
                for (j in 0..2) {
                    // Check if cell is empty
                    if (!board[i][j].isOpponent && !board[i][j].isPlayer) {
                        // Make the move
                        board[i][j] = board[i][j].copy(isPlayer = true)

                        // Call minimax recursively and choose
                        // the maximum value
                        best = Math.max(best, minimax(board, depth + 1, !isMax))

                        // Undo the move
                        board[i][j] = board[i][j].copy(isPlayer = false)
                    }
                }
            }
            best
        } else {
            var best = 1000

            // Traverse all cells
            for (i in 0..2) {
                for (j in 0..2) {
                    // Check if cell is empty
                    if (!board[i][j].isOpponent && !board[i][j].isPlayer) {
                        // Make the move
                        board[i][j] = board[i][j].copy(isOpponent = true)

                        // Call minimax recursively and choose
                        // the minimum value
                        best = Math.min(best, minimax(board, depth + 1, !isMax))

                        // Undo the move
                        board[i][j] = board[i][j].copy(isOpponent = false)
                    }
                }
            }
            best
        }
    }

    // This will return the best possible
    // move for the player
    private fun findBestMove(board: ArrayList<ArrayList<Tile>>): Int {
        var bestVal = -1000
        val bestMove = Move()
        bestMove.row = -1
        bestMove.col = -1

        // Traverse all cells, evaluate minimax function
        // for all empty cells. And return the cell
        // with optimal value.
        for (i in 0..2) {
            for (j in 0..2) {
                // Check if cell is empty
                if (!board[i][j].isOpponent && !board[i][j].isPlayer) {
                    // Make the move
                    board[i][j] = board[i][j].copy(isPlayer = true)

                    // compute evaluation function for this
                    // move.
                    val moveVal = minimax(board, 0, false)

                    // Undo the move
                    board[i][j] = board[i][j].copy(isPlayer = false)

                    // If the value of the current move is
                    // more than the best value, then update
                    // best/
                    if (moveVal > bestVal) {
                        bestMove.row = i
                        bestMove.col = j
                        bestVal = moveVal
                    }
                }
            }
        }
        return when {
            bestMove.row == 0 && bestMove.col == 0 -> 0
            bestMove.row == 0 && bestMove.col == 1 -> 1
            bestMove.row == 0 && bestMove.col == 2 -> 2
            bestMove.row == 1 && bestMove.col == 0 -> 3
            bestMove.row == 1 && bestMove.col == 1 -> 4
            bestMove.row == 1 && bestMove.col == 2 -> 5
            bestMove.row == 2 && bestMove.col == 0 -> 6
            bestMove.row == 2 && bestMove.col == 1 -> 7
            bestMove.row == 2 && bestMove.col == 2 -> 8
            else -> -1
        }
    }

}