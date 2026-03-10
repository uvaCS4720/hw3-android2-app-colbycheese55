package edu.nd.pmcburne.hwapp.one

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import edu.nd.pmcburne.hwapp.one.ui.theme.HWStarterRepoTheme
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HWStarterRepoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val vm = GameViewModel()
                    vm.fetchApiData()
                    app(vm)
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
                val timeRemainingFormatted = if (game.gameState != "FINAL")
                    "${game.gameState} - ${game.timeLeft / 60}:${game.timeLeft % 60} left"
                    else "FINAL"
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
            if (game.gameState == "FINAL") {
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

@Composable
fun app(vm: GameViewModel) {
    val context = LocalContext.current
    val today = LocalDate.now()
    val selectedDate by vm.selectedDate.collectAsState()
    val selectedGender by vm.selectedGender.collectAsState()
    val games by vm.entries.collectAsState()
    val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    var dropdownExpanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = "Basketball Game Viewer",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Box(
                modifier = Modifier
                    .weight(1f)
                    .clickable {
                        android.app.DatePickerDialog(
                            context,
                            { _, year, month, day ->
                                vm.setDate(LocalDate.of(year, month + 1, day))
                            },
                            today.year,
                            today.monthValue - 1,
                            today.dayOfMonth
                        ).show()
                    }
            ) {
                OutlinedTextField(
                    value = selectedDate.format(dateFormatter),
                    onValueChange = {},
                    enabled = false,
                    label = {Text("Date")},
                    modifier = Modifier.fillMaxWidth(),
                    trailingIcon = {
                        Icon(Icons.Default.DateRange, "Select date")
                    }
                )
            }

            Box (
                modifier = Modifier.weight(1f)
            ){
                OutlinedTextField(
                    value = selectedGender,
                    onValueChange = {},
                    enabled = false,
                    label = { Text("Gender") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dropdownExpanded = true },
                    trailingIcon = {
                        Icon(Icons.Default.ArrowDropDown, "Select gender")
                    }
                )

                DropdownMenu(
                    expanded = dropdownExpanded,
                    onDismissRequest = { dropdownExpanded = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Men") },
                        onClick = {
                            vm.setGender("Men")
                            dropdownExpanded = false
                        }
                    )

                    DropdownMenuItem(
                        text = { Text("Women") },
                        onClick = {
                            vm.setGender("Women")
                            dropdownExpanded = false
                        }
                    )
                }
            }
        }
        for (game in games) {
            gameCard(game)
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