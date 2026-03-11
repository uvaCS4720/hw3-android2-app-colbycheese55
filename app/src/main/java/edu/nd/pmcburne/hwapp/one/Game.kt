package edu.nd.pmcburne.hwapp.one

import java.time.LocalDate
import java.time.LocalTime

data class Game(
    val homeName: String,
    val awayName: String,
    val homeScore: Int,
    val awayScore: Int,
    val gameState: GameState,
    val startTime: LocalTime,
    val timeLeft: Int,
)

fun Game.toEntity(date: LocalDate, gender: String): GameEntity {
    return GameEntity(
        homeName = homeName,
        awayName = awayName,
        homeScore = homeScore,
        awayScore = awayScore,
        gameState = gameState,
        startTime = startTime,
        timeLeft = timeLeft,
        date = date,
        gender = gender
    )
}