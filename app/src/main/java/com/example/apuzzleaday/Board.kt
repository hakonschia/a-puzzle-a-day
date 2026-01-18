package com.example.apuzzleaday

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
    modifier: Modifier = Modifier
) {
    BoxWithConstraints(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .fillMaxWidth()
    ) {
        val boxSize = maxWidth / 7

        Column {
            Column {
                board.forEachIndexed { outerIndex, row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        row.forEachIndexed { innerIndex, piece ->
                            val position = outerIndex to innerIndex
                            val pieceAtPosition = placedPieces.firstOrNull { it.actualCoordinates.contains(position) }?.piece

                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .size(boxSize)
                                    .background(
                                        when {
                                            piece is BoardPiece.OffGrid -> Color.Black
                                            pieceAtPosition != null -> pieceAtPosition.color
                                            else -> Color.Gray.copy(alpha = 0.5f)
                                        }
                                    )
                                    .border(1.dp, Color.White)
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
                        boxSize = boxSize
                    )
                }
            }
        }
    }
}

@Composable
private fun Piece(
    piece: Piece,
    boxSize: Dp
) {
    Column {
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
