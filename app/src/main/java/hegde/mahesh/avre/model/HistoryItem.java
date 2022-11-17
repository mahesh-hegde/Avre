package hegde.mahesh.avre.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;

@Entity(tableName = "history")
public class HistoryItem {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String code;
    public String out, err, res;

    @Ignore
    public Throwable exception;

    public String stackTrace;

    public static HistoryItem success(String code, String out, String err, String res) {
        HistoryItem item = new HistoryItem();
        item.code = code;
        item.out = out;
        item.err = err;
        item.res = res;
        return item;
    }

    public static HistoryItem failure(String code, String out, String err, Exception exception) {
        HistoryItem item = new HistoryItem();
        item.code = code;
        item.out = out;
        item.err = err + exception.getMessage();
        item.exception = exception;
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        exception.printStackTrace(new PrintWriter(buffer));
        item.stackTrace = buffer.toString();
        return item;
    }
}
