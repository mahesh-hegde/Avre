package hegde.mahesh.avre.interpreter;

import androidx.annotation.Nullable;

import java.util.Locale;

public class SnippetEvalException extends Exception {
    int line;
    int column;
    Throwable cause;

    public SnippetEvalException(int line, int column, Throwable cause) {
        super(cause.getMessage());
        this.line = line;
        this.column = column;
        this.cause = cause;
    }

    @Nullable
    @Override
    public String getMessage() {
        if (line == 0) {
            return cause.getMessage();
        }
        return String.format(Locale.getDefault(), "[line %s]: %s", line, cause.toString());
    }
}
