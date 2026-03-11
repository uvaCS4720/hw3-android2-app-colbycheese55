package edu.nd.pmcburne.hwapp.one

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import okhttp3.Request
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.concurrent.TimeUnit

private val client = OkHttpClient().newBuilder()
    .callTimeout(10, TimeUnit.SECONDS)
    .connectTimeout(10, TimeUnit.SECONDS)
    .readTimeout(10, TimeUnit.SECONDS)
    .writeTimeout(10, TimeUnit.SECONDS)
    .build()

suspend fun fetchGames(date: LocalDate, gender: String): List<Game> {
    val genderStr = when (gender) {
        "Women" -> "basketball-women"
        "Men" -> "basketball-men"
        else -> "ERROR"
    }
    val formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")
    val dateStr = date.format(formatter)
    val url = "https://ncaa-api.henrygd.me/scoreboard/$genderStr/d1/$dateStr"
    Log.d("app", "url: $url")

//    https://ncaa-api.henrygd.me/scoreboard/basketball-men/2026/3/10
//    https://ncaa-api.henrygd.me/scoreboard/basketball-men/d1/2026/02/17

    val request = Request.Builder()
        .header("User-Agent", "Android")
        .header("Accept", "application/json")
        .url(url)
        .build()

    val response = withContext(Dispatchers.IO) {
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("HTTP ${response.code}")
            response.body?.string() ?: throw Exception("Empty Body")
        }
    }

    val json = Json { ignoreUnknownKeys = true }
    val processedResponse = json.decodeFromString<ApiResponse>(response)
    return processedResponse.toGames(gender)
}