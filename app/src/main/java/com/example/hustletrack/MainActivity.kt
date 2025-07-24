package com.example.hustletrack

import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.hustletrack.ui.theme.HustleTrackTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HustleTrackTheme {
                Surface(color = MaterialTheme.colors.background) {
                    val viewModel: TimerViewModel = viewModel(
                        factory = ViewModelFactory(application)
                    )

                    val elapsedTime by viewModel.elapsedTime.collectAsState()
                    val isRunning by viewModel.isRunning.collectAsState()

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = formatElapsedTime(elapsedTime),
                            style = MaterialTheme.typography.h4,
                            modifier = Modifier.padding(bottom = 32.dp)
                        )

                        Row {
                            Button(
                                onClick = {
                                    if (isRunning) {
                                        viewModel.stopAndSave()
                                    } else {
                                        viewModel.startTimer()
                                    }
                                },
                                modifier = Modifier.padding(end = 8.dp)
                            ) {
                                Text(if (isRunning) "Stop" else "Start")
                            }

                            Button(
                                onClick = { viewModel.resetTimer() },
                                enabled = !isRunning
                            ) {
                                Text("Reset")
                            }
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                            onClick = { viewModel.showHistory = true }
                        ) {
                            Text("View History")
                        }

                        if (viewModel.showHistory) {
                            HistoryScreen(viewModel)
                        }
                    }
                }
            }
        }
    }

    private fun formatElapsedTime(millis: Long): String {
        val seconds = millis / 1000 % 60
        val minutes = millis / 1000 / 60
        return "%02d:%02d".format(minutes, seconds)
    }
}

class ViewModelFactory(private val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TimerViewModel(app) as T
    }
}

@Composable
fun TimerScreen(viewModel: TimerViewModel, padding: PaddingValues) {
    // existing timer layout here
}