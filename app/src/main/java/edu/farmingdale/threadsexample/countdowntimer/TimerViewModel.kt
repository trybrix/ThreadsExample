package edu.farmingdale.threadsexample.countdowntimer

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import edu.farmingdale.threadsexample.R
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class TimerViewModel(private val context: Context) : ViewModel() {
    private var timerJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    // Values selected in time picker
    var selectedHour by mutableIntStateOf(0)
        private set
    var selectedMinute by mutableIntStateOf(0)
        private set
    var selectedSecond by mutableIntStateOf(0)
        private set

    // Total milliseconds when timer starts
    var totalMillis by mutableLongStateOf(0L)
        private set

    // Time that remains
    var remainingMillis by mutableLongStateOf(0L)
        private set

    // Timer's running status
    var isRunning by mutableStateOf(false)
        private set

    fun selectTime(hour: Int, min: Int, sec: Int) {
        selectedHour = hour
        selectedMinute = min
        selectedSecond = sec
    }

    fun startTimer() {
        // Convert hours, minutes, and seconds to milliseconds
        totalMillis = (selectedHour * 60 * 60 + selectedMinute * 60 + selectedSecond) * 1000L

        // Start coroutine that makes the timer count down
        if (totalMillis > 0) {
            isRunning = true
            remainingMillis = totalMillis

            timerJob = viewModelScope.launch {
                while (remainingMillis > 0) {
                    delay(1000)
                    remainingMillis -= 1000
                }

                isRunning = false
                playChime() // Play chime when the timer ends
            }
        }
    }

    private fun playChime() {
        mediaPlayer = MediaPlayer.create(context, R.raw.timer_end_chime) // Ensure file is in res/raw
        mediaPlayer?.start()
    }

    fun resetTimer() {
        if (isRunning) {
            timerJob?.cancel()
        }
        isRunning = false
        remainingMillis = 0
        totalMillis = 0
        selectedHour = 0
        selectedMinute = 0
        selectedSecond = 0
    }

    fun cancelTimer() {
        if (isRunning) {
            timerJob?.cancel()
            isRunning = false
            remainingMillis = 0
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        mediaPlayer?.release() // Release MediaPlayer resources
        mediaPlayer = null  // Allows chime to not play or play = null
    }
}