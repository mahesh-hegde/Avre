package hegde.mahesh.avre.interpreter;

import androidx.annotation.Nullable;

import java.util.Locale;

public class SnippetEvalException extends Exception {
    int lineBegin, colBegin;
    int lineEnd, colEnd;

    public SnippetEvalException(int lineBegin, int colBegin, String message) {
        super(message);
        this.lineBegin = lineBegin;
        this.colBegin = colBegin;
    }

    public SnippetEvalException(String message) {
        super(message);
    }

    public SnippetEvalException(int lineBegin, int colBegin, int lineEnd, int colEnd, String message) {
        super(message);
        assert lineBegin != -1;
        this.lineBegin = lineBegin;
        this.colBegin = colBegin;
        this.lineEnd = lineEnd;
        this.colEnd = colEnd;
    }

    @Nullable
    @Override
    public String getMessage() {
        if (colBegin == -1) {
            return String.format(Locale.getDefault(), "line %d -> %s", lineBegin, super.getMessage());
        }
        return String.format(Locale.getDefault(),
                "%d:%d -> %s", lineBegin, colBegin, super.getMessage());
    }
}
