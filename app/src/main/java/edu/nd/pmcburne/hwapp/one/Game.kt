package edu.nd.pmcburne.hwapp.one

import java.time.LocalTime

data class Game(
    val homeName: String,
    val awayName: String,
    val homeScore: Int,
    val awayScore: Int,
    val gameState: String,
    val startTime: LocalTime,
    val timeLeft: Int,

    ) {

}