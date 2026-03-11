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

fun ApiResponse.toGames(gender: String): List<Game> {
    val formatter = DateTimeFormatter.ofPattern("h:mm a", Locale.US)

    return games.map { g ->
        val localTime = LocalTime.parse(g.game.startTime.removeSuffix(" ET"), formatter)

        val timeParts = g.game.contestClock.split(":")
        val timeLeft = (timeParts.getOrNull(0)?.toIntOrNull() ?: 0) * 60 +
                (timeParts.getOrNull(1)?.toIntOrNull() ?: 0)

        val gameState = when(g.game.currentPeriod) {
            "pre" -> GameState.FUTURE
            "1st" -> if (gender == "Women") GameState.Q1 else GameState.H1
            "2nd" -> if (gender == "Women") GameState.Q2 else GameState.H2
            "3rd" -> GameState.Q3
            "4th" -> GameState.Q4
            "FINAL" -> GameState.DONE
            else -> GameState.ERROR
        }

        Game(
            homeName = g.game.home.names.short,
            awayName = g.game.away.names.short,
            homeScore = g.game.home.score.toIntOrNull() ?: 0,
            awayScore = g.game.away.score.toIntOrNull() ?: 0,
            gameState = gameState,
            startTime = localTime,
            timeLeft = timeLeft
        )
    }
}