package edu.nd.pmcburne.hwapp.one

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime

class GameViewModel: ViewModel() {
    // 1️⃣ Selected date
    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    // 2️⃣ Selected gender
    private val _selectedGender = MutableStateFlow("Men")
    val selectedGender: StateFlow<String> = _selectedGender

    private val _loading = MutableStateFlow(false)

    val loading: StateFlow<Boolean> = _loading

    // 3️⃣ Combined filtered entries
    private val _entries = MutableStateFlow<List<Game>>(emptyList())

    val entries: StateFlow<List<Game>> = _entries

    // 4️⃣ Update functions
    fun setDate(date: LocalDate) {
        _selectedDate.value = date
    }

    fun setGender(gender: String) {
        _selectedGender.value = gender
    }

    private fun fetchApiData() {
        viewModelScope.launch {
            _loading.value = true
            try {
                Log.d("app", "starting API")
                val games = fetchGames(
                    selectedDate.value,
                    selectedGender.value
                )
                Log.d("app", "received API")
            } catch (e: Exception) {
                Log.e("app", "Failed to fetch games", e)
            }
            _loading.value = false
        }
    }

    fun chooseGames(context: Context) {
        val dao = DatabaseProvider.getDatabase(context).gameDao()
        val date = _selectedDate.value
        val gender = _selectedGender.value

        viewModelScope.launch {

            // --- 1️⃣ Fetch cached DB immediately ---
            launch {
                try {
                    val cached = dao.getGames(date, gender).first() // suspend
                    _entries.value = cached.map { it.toGame() }
                } catch (e: Exception) {
                    Log.e("app", "Failed to fetch from DB", e)
                }
            }

            // --- 2️⃣ Fetch API in background ---
            launch {
                _loading.value = true
                try {
                    val apiGames = fetchGames(date, gender) // suspend API call
                    // update DB
                    dao.updateGames(apiGames.map { it.toEntity(date, gender) })
                    // update entries
                    _entries.value = apiGames
                } catch (e: Exception) {
                    Log.e("app", "Failed to fetch from API", e)
                } finally {
                    _loading.value = false
                }
            }
        }
    }
}