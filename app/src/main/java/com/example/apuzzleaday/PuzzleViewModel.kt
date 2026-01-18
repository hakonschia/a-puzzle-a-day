package com.example.apuzzleaday

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

sealed interface BoardPiece {
    sealed interface Month : BoardPiece {
        val shortName: String

        data class January(override val shortName: String = "Jan") : Month
        data class February(override val shortName: String = "Feb") : Month
        data class March(override val shortName: String = "Mar") : Month
        data class April(override val shortName: String = "Apr") : Month
        data class May(override val shortName: String = "May") : Month
        data class June(override val shortName: String = "Jun") : Month
        data class July(override val shortName: String = "Jul") : Month
        data class August(override val shortName: String = "Aug") : Month
        data class September(override val shortName: String = "Sep") : Month
        data class October(override val shortName: String = "Oct") : Month
        data class November(override val shortName: String = "Nov") : Month
        data class December(override val shortName: String = "Dec") : Month
    }

    data class Day(
        val day: Int
    ) : BoardPiece

    data object OffGrid : BoardPiece
}

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

val board = run {
    var dayCounter = 1

    //@formatter:off
    listOf(
        listOf(BoardPiece.Month.January(), BoardPiece.Month.February(), BoardPiece.Month.March(), BoardPiece.Month.April(), BoardPiece.Month.May(), BoardPiece.Month.June(), BoardPiece.OffGrid),
        listOf(BoardPiece.Month.July(), BoardPiece.Month.August(), BoardPiece.Month.September(), BoardPiece.Month.October(), BoardPiece.Month.November(), BoardPiece.Month.December(), BoardPiece.OffGrid),
        listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
        listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
        listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
        listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
        listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.OffGrid, BoardPiece.OffGrid, BoardPiece.OffGrid, BoardPiece.OffGrid)
    )
    //@formatter:on
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

class PuzzleViewModel : ViewModel() {

    private val _placedPieces = MutableStateFlow<List<PlacedPiece>>(emptyList())
    val placedPieces = _placedPieces.asStateFlow()

    val unplacedPieces = placedPieces
        .map { placedPieces ->
            // Note that finding the unplaced pieces like this assumes the color of the piece is the same as in the original
            // "pieces" list, depending on what the UI wants to show it might be tempting to change the color with piece.copy(color = ...)
            // but that would break this logic
            // Might be more safe if the pieces have some sort of ID, dunno
            pieces - placedPieces.map { it.piece }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = pieces
        )

    private val _availablePlacesForSelectedPiece = MutableStateFlow<List<PlacedPiece>>(emptyList())
    val availablePlacesForSelectedPiece = _availablePlacesForSelectedPiece.asStateFlow()

    fun restartGame() {
        _placedPieces.value = emptyList()
        _availablePlacesForSelectedPiece.value = emptyList()
    }

    fun selectPosition(position: Pair<Int, Int>) {
        val availablePlacesForSelectedPiece = availablePlacesForSelectedPiece.value
        val isPositionPossibleToTake = availablePlacesForSelectedPiece.map { it.actualCoordinates }.any { it.contains(position) }

        if (isPositionPossibleToTake) {
            _placedPieces.value += PlacedPiece(
                // It doesn't matter which item in availablePlacesForSelectedPiece we take here since every piece is the same
                // but there will always be one piece in the list if we get into this branch so just take the first
                piece = availablePlacesForSelectedPiece.first().piece,
                startPosition = position
            )

            _availablePlacesForSelectedPiece.value = emptyList()
        }
    }

    fun selectPiece(piece: Piece) {
        _availablePlacesForSelectedPiece.value = buildList {
            board.indices.forEach { outerIndex ->
                board[outerIndex].indices.forEach { innerIndex ->
                    val position = outerIndex to innerIndex
                    val pieceWhenPlacedAtPosition = PlacedPiece(piece, position)

                    val isPositionTaken = placedPieces.value.any { alreadyPlacedPiece ->
                        alreadyPlacedPiece.actualCoordinates.any { pieceWhenPlacedAtPosition.actualCoordinates.contains(it) }
                    }

                    val isPositionOutOfBounds = pieceWhenPlacedAtPosition.actualCoordinates.any { (x, y) ->
                        try {
                            board[x][y] is BoardPiece.OffGrid
                        } catch (_: IndexOutOfBoundsException) {
                            true
                        }
                    }

                    if (!isPositionTaken && !isPositionOutOfBounds) {
                        add(
                            PlacedPiece(
                                piece = piece,
                                startPosition = position
                            )
                        )
                    }
                }
            }
        }
    }
}
