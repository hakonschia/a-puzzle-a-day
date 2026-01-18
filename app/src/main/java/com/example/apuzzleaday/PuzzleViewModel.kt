package com.example.apuzzleaday

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

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
