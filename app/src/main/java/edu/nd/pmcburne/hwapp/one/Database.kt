package edu.nd.pmcburne.hwapp.one

import android.content.Context
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.Transaction
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "games",
        primaryKeys = ["date", "gender", "homeName", "awayName", "startTime"])
data class GameEntity(
    val homeName: String,
    val awayName: String,
    val homeScore: Int,
    val awayScore: Int,
    val gameState: GameState,
    val startTime: LocalTime,
    val timeLeft: Int,

    val date: LocalDate,
    val gender: String
) {
    fun toGame(): Game {
        return Game(
            homeName = homeName,
            awayName = awayName,
            homeScore = homeScore,
            awayScore = awayScore,
            gameState = gameState,
            startTime = startTime,
            timeLeft = timeLeft
        )
    }
}

@Dao
interface GameDao {

    @Query("SELECT * FROM games WHERE date = :date AND gender = :gender ORDER BY startTime")
    fun getGames(date: LocalDate, gender: String): Flow<List<GameEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateGames(games: List<GameEntity>)
}

class Converters {

    // --- LocalDate ---
    @TypeConverter
    fun fromLocalDate(date: LocalDate?): String? {
        return date?.toString()   // ISO-8601 format: "2026-03-10"
    }

    @TypeConverter
    fun toLocalDate(dateString: String?): LocalDate? {
        return dateString?.let { LocalDate.parse(it) }
    }

    // --- LocalTime ---
    @TypeConverter
    fun fromLocalTime(time: LocalTime?): String? {
        return time?.toString()  // ISO-8601 format: "18:00:00"
    }

    @TypeConverter
    fun toLocalTime(timeString: String?): LocalTime? {
        return timeString?.let { LocalTime.parse(it) }
    }

    // --- GameState enum ---
    @TypeConverter
    fun fromGameState(state: GameState?): String? {
        return state?.name
    }

    @TypeConverter
    fun toGameState(stateString: String?): GameState? {
        return stateString?.let { GameState.valueOf(it) }
    }
}

@Database(entities = [GameEntity::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}

object DatabaseProvider {

    private var db: AppDatabase? = null

    fun getDatabase(context: Context): AppDatabase {
        return db ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                AppDatabase::class.java,
                "games.db"
            ).build()
            db = instance
            instance
        }
    }
}