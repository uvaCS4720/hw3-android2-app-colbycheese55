package edu.nd.pmcburne.hwapp.one

import kotlinx.serialization.Serializable
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale


@Serializable
data class ApiResponse(
    val games: List<ApiGameWrapper>
)

@Serializable
data class ApiGameWrapper(
    val game: ApiGame
)

@Serializable
data class ApiGame(
    val away: ApiHomeAway,
    val home: ApiHomeAway,
    val startTime: String,
    val currentPeriod: String,
    val contestClock: String
)

@Serializable
data class ApiHomeAway(
    val score: String,
    val names: ApiNames
)

@Serializable
data class ApiNames(
    val short: String
)

fun ApiResponse.toGames(): List<Game> {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    return games.map { g ->
        val localTime = LocalTime.parse(g.game.startTime.removeSuffix(" ET"), formatter)

        val timeParts = g.game.contestClock.split(":")
        val timeLeft = (timeParts.getOrNull(0)?.toIntOrNull() ?: 0) * 60 +
                (timeParts.getOrNull(1)?.toIntOrNull() ?: 0)

        Game(
            homeName = g.game.home.names.short,
            awayName = g.game.away.names.short,
            homeScore = g.game.home.score.toIntOrNull() ?: -1,
            awayScore = g.game.away.score.toIntOrNull() ?: -1,
            gameState = g.game.currentPeriod,
            startTime = localTime,
            timeLeft = timeLeft
        )
    }
}