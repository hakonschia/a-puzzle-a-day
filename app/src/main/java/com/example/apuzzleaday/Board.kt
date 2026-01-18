package com.example.apuzzleaday

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Board(
    board: List<List<BoardPiece>>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        board.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                row.forEach { piece ->
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f)
                            .background(if (piece is BoardPiece.OffGrid) Color.Black else Color.Gray.copy(alpha = 0.5f))
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
}
