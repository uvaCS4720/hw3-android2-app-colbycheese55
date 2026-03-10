package edu.nd.pmcburne.hwapp.one

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.time.LocalDate
import java.time.LocalTime

class GameViewModel: ViewModel() {
    // 1️⃣ Selected date
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // 2️⃣ Selected gender
    private val _selectedGender = MutableStateFlow("Men")
    val selectedGender: StateFlow<String> = _selectedGender

    // 3️⃣ Combined filtered entries
    private val _entries = MutableStateFlow(
        listOf(
            Game(
                homeName = "homies",
                awayName = "awayies",
                homeScore = 10,
                awayScore = 15,
                gameState = "4th quarter",
                startTime = LocalTime.now(),
                timeLeft = 100
            ),
            Game(
                homeName = "homies",
                awayName = "awayies",
                homeScore = 0,
                awayScore = 0,
                gameState = "future",
                startTime = LocalTime.now(),
                timeLeft = 100
            ),
            Game(
                homeName = "homies",
                awayName = "awayies",
                homeScore = 10,
                awayScore = 15,
                gameState = "finished",
                startTime = LocalTime.now(),
                timeLeft = 100
            )
        )
    )

    val entries: StateFlow<List<Game>> = _entries

    // 4️⃣ Update functions
    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setGender(gender: String) {
        _selectedGender.value = gender
    }
}