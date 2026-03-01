package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Composable
fun gameCard(game: Game) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        elevation = CardDefaults.cardElevation(6.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            // Teams Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(horizontalAlignment = Alignment.Start) {
                    Text(
                        text = game.homeName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "(Home)")
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = game.awayName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(text = "(Away)")
                }
            }

            Divider()

            // Game Status Section

            if (game.gameState == "future") {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val formattedTime = game.startTime.format(formatter)

                Text(
                    text = "Starting at $formattedTime",
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            else {
                val timeRemainingFormatted = if (game.gameState != "finished")
                    "${game.gameState} - ${game.timeLeft / 60}:${game.timeLeft % 60} left"
                    else "finished"
                Text(
                    text = timeRemainingFormatted,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Text(
                        text = game.homeScore.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = game.awayScore.toString(),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Winner Section
            if (game.gameState == "finished") {
                val winner = if (game.homeScore > game.awayScore)
                    game.homeName else game.awayName

                Text(
                    text = "Winner: $winner",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Preview
@Composable
fun prev1_gameCard() {
    val game = Game(
        homeName = "homies",
        awayName = "awayies",
        homeScore = 10,
        awayScore = 15,
        gameState = "4th quarter",
        startTime = LocalTime.now(),
        timeLeft = 100,
    )

    gameCard(game)
}

@Preview
@Composable
fun prev2_gameCard() {
    val game = Game(
        homeName = "homies",
        awayName = "awayies",
        homeScore = 0,
        awayScore = 0,
        gameState = "future",
        startTime = LocalTime.now(),
        timeLeft = 100,
    )

    gameCard(game)
}

@Preview
@Composable
fun prev3_gameCard() {
    val game = Game(
        homeName = "homies",
        awayName = "awayies",
        homeScore = 10,
        awayScore = 15,
        gameState = "finished",
        startTime = LocalTime.now(),
        timeLeft = 100,
    )

    gameCard(game)
}