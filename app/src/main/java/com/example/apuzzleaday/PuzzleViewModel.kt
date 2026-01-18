package com.example.apuzzleaday

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

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
)

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

class PuzzleViewModel : ViewModel() {
    private var dayCounter = 1

    private val _board = MutableStateFlow(
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
    )
    val board = _board.asStateFlow()

    private val _placedPieces = MutableStateFlow<List<PlacedPiece>>(
        listOf(
            PlacedPiece(pieces[1], 1 to 1),
            PlacedPiece(pieces[3], 3 to 2)
        )
    )
    val placedPieces = _placedPieces.asStateFlow()

}
