package hegde.mahesh.avre.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import hegde.mahesh.avre.model.HistoryItem
import java.util.concurrent.Executors

@Database(entities = [HistoryItem::class], version = 1, exportSchema = false)
abstract class AvreDatabase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        // marking the instance as volatile to ensure atomic access to the variable
        @Volatile
        private var INSTANCE: AvreDatabase? = null
        private const val NUMBER_OF_THREADS = 4
        @JvmField
        val databaseWriteExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS)!!
        @JvmStatic
        fun getDatabase(context: Context): AvreDatabase {
            if (INSTANCE == null) {
                synchronized(AvreDatabase::class.java) {
                    if (INSTANCE == null) {
                        INSTANCE = Room.databaseBuilder(context.applicationContext,
                                AvreDatabase::class.java, "avre_database")
                                .build()
                    }
                }
            }
            return INSTANCE!!
        }
    }
}