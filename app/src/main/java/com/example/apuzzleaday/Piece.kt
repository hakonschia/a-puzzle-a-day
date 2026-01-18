package com.example.apuzzleaday

import androidx.compose.ui.graphics.Color


data class Piece(
    // TODO this isn't really coordinates its the size, but its not really a size either so idk what to call this
    val coordinates: List<Pair<Int, Int>>,
    val color: Color
) {
    val width = coordinates.maxOf { it.first } + 1
    val height = coordinates.maxOf { it.second } + 1
}

data class PlacedPiece(
    val piece: Piece,
    val startPosition: Pair<Int, Int>
) {
    val actualCoordinates: List<Pair<Int, Int>> = piece.coordinates.map { (x, y) ->
        x + startPosition.first to y + startPosition.second
    }
}

val pieces = listOf(
    /**
     * . . .
     * .
     * .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 2 to 0), color = Color.Red.copy(alpha = 0.25f)),

    /**
     * . . .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2), color = Color.Green.copy(alpha = 0.5f)),

    /**
     * . . .
     *     . .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 1, 0 to 2, 1 to 2, 1 to 3), color = Color.Blue.copy(alpha = 0.5f)),

    /**
     * . . . .
     *   .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 1), color = Color.Yellow.copy(alpha = 0.5f)),

    /**
     * .   .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 2, 1 to 0, 1 to 1, 1 to 2), color = Color.Magenta.copy(alpha = 0.5f)),

    /**
     * .
     * . . .
     *     .
     */
    Piece(listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2, 2 to 2), color = Color.Cyan.copy(alpha = 0.5f)),

    /**
     * .
     * . . . .
     */
    Piece(listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2, 1 to 3), color = Color.Cyan.copy(alpha = 0.35f)),

    /**
     * . .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1, 1 to 2), Color.Cyan.copy(alpha = 0.2f))
)
