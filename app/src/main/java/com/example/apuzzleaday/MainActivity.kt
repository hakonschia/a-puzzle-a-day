package com.example.apuzzleaday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apuzzleaday.ui.theme.APuzzleADayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            APuzzleADayTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val viewModel = viewModel<PuzzleViewModel>()

                    Board(
                        board = board,
                        placedPieces = viewModel.placedPieces.collectAsStateWithLifecycle().value,
                        unplacedPieces = viewModel.unplacedPieces.collectAsStateWithLifecycle().value,
                        availablePlacesForSelectedPiece = viewModel.availablePlacesForSelectedPiece.collectAsStateWithLifecycle().value,
                        onSelectPiece = viewModel::selectPiece,
                        onSelectPosition = viewModel::selectPosition,
                        onRestartGame = viewModel::restartGame,
                        modifier = Modifier
                            .padding(innerPadding)
                    )
                }
            }
        }
    }
}
