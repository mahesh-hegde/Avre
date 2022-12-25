package hegde.mahesh.avre.database;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import hegde.mahesh.avre.model.HistoryItem;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {HistoryItem.class}, version = 1, exportSchema = false)
public abstract class AvreDatabase extends RoomDatabase {
    public abstract HistoryDao historyDao();

    // marking the instance as volatile to ensure atomic access to the variable
    private static volatile AvreDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AvreDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AvreDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AvreDatabase.class, "avre_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
