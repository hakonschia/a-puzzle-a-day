package com.example.apuzzleaday

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Board(
    board: List<List<BoardPiece>>,
    placedPieces: List<PlacedPiece>,
    unplacedPieces: List<Piece>,
    availablePlacesForSelectedPiece: List<PlacedPiece>,
    onSelectPiece: (Piece) -> Unit,
    onSelectPosition: (Pair<Int, Int>) -> Unit,
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        val boxSize = maxWidth / 7
        var availablePositionOffset by remember(availablePlacesForSelectedPiece) { mutableStateOf(0) }

        Column {
            Row {
                Button(
                    onClick = {
                        availablePositionOffset = (availablePositionOffset - 1).coerceAtLeast(0)
                    }
                ) {
                    Text("Previous")
                }

                Button(
                    onClick = {
                        availablePositionOffset = (availablePositionOffset + 1).coerceAtMost(availablePlacesForSelectedPiece.lastIndex)
                    }
                ) {
                    Text("Next")
                }
            }

            Column {
                board.forEachIndexed { outerIndex, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        row.forEachIndexed { innerIndex, piece ->
                            val position = outerIndex to innerIndex
                            val pieceAtPosition = placedPieces.firstOrNull { it.actualCoordinates.contains(position) }?.piece
                            val selectedPiece = availablePlacesForSelectedPiece.getOrNull(availablePositionOffset)?.takeIf {
                                it.actualCoordinates.contains(position)
                            }

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .clickable {
                                        onSelectPosition(position)
                                    }
                                    .size(boxSize)
                                    .background(
                                        when {
                                            selectedPiece != null -> selectedPiece.piece.color
                                            pieceAtPosition != null -> pieceAtPosition.color
                                            piece is BoardPiece.OffGrid -> Color.Black
                                            else -> Color.Gray.copy(alpha = 0.5f)
                                        }
                                    )
                                    .border(
                                        width = 1.dp,
                                        color = if (selectedPiece != null) Color.Green else Color.White
                                    )
                            ) {
                                when (piece) {
                                    is BoardPiece.Day -> {
                                        Text(
                                            text = piece.day.toString()
                                        )
                                    }

                                    is BoardPiece.Month -> {
                                        Text(
                                            text = piece.shortName
                                        )
                                    }

                                    is BoardPiece.OffGrid -> {
                                    }
                                }
                            }
                        }
                    }
                }
            }

            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
            ) {
                unplacedPieces.forEach { unplacedPiece ->
                    Piece(
                        piece = unplacedPiece,
                        boxSize = boxSize,
                        modifier = Modifier
                            .clickable {
                                onSelectPiece(unplacedPiece)
                            }
                    )
                }
            }
        }
    }
}

@Composable
private fun Piece(
    piece: Piece,
    boxSize: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        for (i in 0 until piece.width) {
            Row {
                for (j in 0 until piece.height) {
                    val position = i to j
                    val isAtPosition = piece.coordinates.contains(position)

                    Box(
                        modifier = Modifier
                            .size(boxSize)
                            .background(
                                if (isAtPosition) {
                                    piece.color
                                } else {
                                    Color.Transparent
                                }
                            )
                    )
                }
            }
        }
    }
}
