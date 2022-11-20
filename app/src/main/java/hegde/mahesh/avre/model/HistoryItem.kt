package hegde.mahesh.avre.model

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.ByteArrayOutputStream
import java.io.PrintWriter

@Entity(tableName = "history")
data class HistoryItem(
        var code: String = "",
        var out: String? = null,
        var err: String? = null,
        var res: String? = null,
        @Ignore val exception: Throwable? = null,
        var stackTrace: String? = null,
) {
    @PrimaryKey(autoGenerate = true)
    var id = 0

    companion object {
        fun success(code: String, out: String?, err: String?, res: String?): HistoryItem {
            return HistoryItem(code, out, err, res, null, null)
        }

        fun failure(code: String, out: String?, err: String, exception: Exception): HistoryItem {
            return HistoryItem(code, out, err, null, exception, getStackTrace(exception))
        }

        private fun getStackTrace(exception: Throwable): String {
            val buffer = ByteArrayOutputStream()
            exception.printStackTrace(PrintWriter(buffer))
            return buffer.toString()
        }
    }
}