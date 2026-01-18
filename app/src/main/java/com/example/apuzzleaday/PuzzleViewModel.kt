package com.example.apuzzleaday

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

class PuzzleViewModel : ViewModel() {
    private var dayCounter = 1

    private val _board = MutableStateFlow(
        listOf(
            listOf(BoardPiece.Month.January(), BoardPiece.Month.February(), BoardPiece.Month.March(), BoardPiece.Month.April(), BoardPiece.Month.May(), BoardPiece.Month.June(), BoardPiece.OffGrid),
            listOf(BoardPiece.Month.July(), BoardPiece.Month.August(), BoardPiece.Month.September(), BoardPiece.Month.October(), BoardPiece.Month.November(), BoardPiece.Month.December(), BoardPiece.OffGrid),
            listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
            listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
            listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
            listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++)),
            listOf(BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.Day(dayCounter++), BoardPiece.OffGrid, BoardPiece.OffGrid, BoardPiece.OffGrid, BoardPiece.OffGrid)
        )
    )

    val board = _board.asStateFlow()
}
