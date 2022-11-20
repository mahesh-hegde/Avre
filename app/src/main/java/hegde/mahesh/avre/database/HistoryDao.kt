package hegde.mahesh.avre.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import hegde.mahesh.avre.model.HistoryItem

@Dao
interface HistoryDao {
    @Query("SELECT * FROM history")
    fun getAll(): List<HistoryItem>

    @Insert
    fun insert(item: HistoryItem?)

    @Query("DELETE FROM history")
    fun deleteAll()
}