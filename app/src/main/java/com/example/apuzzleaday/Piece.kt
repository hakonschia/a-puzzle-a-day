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
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 2 to 0), color = Color.Red.copy(alpha = 0.25f)).generateAllPermutationsForPiece(),

    /**
     * . . .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 0, 1 to 1, 1 to 2), color = Color.Green.copy(alpha = 0.5f)).generateAllPermutationsForPiece(),

    /**
     * . . .
     *     . .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 1 to 2, 1 to 3), color = Color.Blue.copy(alpha = 0.5f)).generateAllPermutationsForPiece(),

    /**
     * . . . .
     *   .
     */
    Piece(listOf(0 to 0, 0 to 1, 0 to 2, 0 to 3, 1 to 1), color = Color.Yellow.copy(alpha = 0.5f)).generateAllPermutationsForPiece(),

    /**
     * .   .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 2, 1 to 0, 1 to 1, 1 to 2), color = Color.Magenta.copy(alpha = 0.5f)).generateAllPermutationsForPiece(),

    /**
     * .
     * . . .
     *     .
     */
    Piece(listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2, 2 to 2), color = Color.Cyan.copy(alpha = 0.5f)).generateAllPermutationsForPiece(),

    /**
     * .
     * . . . .
     */
    Piece(listOf(0 to 0, 1 to 0, 1 to 1, 1 to 2, 1 to 3), color = Color.Cyan.copy(alpha = 0.35f)).generateAllPermutationsForPiece(),

    /**
     * . .
     * . . .
     */
    Piece(listOf(0 to 0, 0 to 1, 1 to 0, 1 to 1, 1 to 2), Color.Cyan.copy(alpha = 0.2f)).generateAllPermutationsForPiece()
).flatten()

private fun Piece.generateAllPermutationsForPiece(): Set<Piece> {
    // A Boolean matrix where true means the piece is at the coordinate and false means it's not
    val originalMatrix = List(height) { outerIndex ->
        List(width) { innerIndex ->
            coordinates.contains(innerIndex to outerIndex)
        }
    }

    // Depending on the piece this will create duplicates, which is why we put them in a Set and not a List :)
    return setOf(
        originalMatrix.toPiece(color),
        originalMatrix.flipVertically().toPiece(color),
        originalMatrix.flipHorizontally().toPiece(color),
        originalMatrix.flipVerticallyAndHorizontally().toPiece(color),
        originalMatrix.transpose().toPiece(color),
        originalMatrix.transpose().flipVertically().toPiece(color),
        originalMatrix.transpose().flipHorizontally().toPiece(color),
        originalMatrix.transpose().flipVerticallyAndHorizontally().toPiece(color)
    )
}

private fun List<List<Boolean>>.toPiece(color: Color): Piece {
    return Piece(
        color = color,
        coordinates = buildList {
            this@toPiece.forEachIndexed { outerIndex, list ->
                list.forEachIndexed { innerIndex, b ->
                    if (b) {
                        add(outerIndex to innerIndex)
                    }
                }
            }
        }
    )
}

private fun List<List<Boolean>>.flipVertically(): List<List<Boolean>> {
    /**
     * .
     * .
     * . .
     *   .
     * ->
     *   .
     * . .
     * .
     * .
     */
    return reversed()
}

private fun List<List<Boolean>>.flipHorizontally(): List<List<Boolean>> {
    /**
     * .
     * .
     * . .
     *   .
     * ->
     *   .
     *   .
     * . .
     * .
     */
    return map { it.reversed() }
}

private fun List<List<Boolean>>.flipVerticallyAndHorizontally(): List<List<Boolean>> {
    /**
     * .
     * .
     * . .
     *   .
     * ->
     * .
     * . .
     *   .
     *   .
     */
    return flipVertically().flipHorizontally()
}

private fun List<List<Boolean>>.transpose(): List<List<Boolean>> {
    /**
     * .
     * .
     * . .
     *   .
     * ->
     * . . .
     *     . .
     */

    // If the matrix is n*m, n might != m, to simplify this we just create a n*n matrix (this code assumes the outer list is
    // always larger, which we probably shouldn't do, but whatever)

    val newMatrix = List(size) {
        List(size) { false }.toMutableList()
    }

    // A transposition just flips the value of the indices, ie. new[innerIndex][outerIndex] = old[outerIndex][innerIndex]
    indices.forEach { outerIndex ->
        get(outerIndex).forEachIndexed { innerIndex, value ->
            newMatrix[innerIndex][outerIndex] = value
        }
    }

    // Remove redundant rows where everything is false
    return newMatrix.filter { it.any { value -> value } }
}
