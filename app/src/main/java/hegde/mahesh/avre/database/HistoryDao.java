package hegde.mahesh.avre.database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import hegde.mahesh.avre.model.HistoryItem;

import java.util.List;

@Dao
public interface HistoryDao {
    @Query("SELECT * FROM history")
    List<HistoryItem> getAll();

    @Insert
    void insert(HistoryItem item);

    @Query("DELETE FROM history")
    void delete();
}
