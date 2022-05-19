package hegde.mahesh.avre.interpreter;

import java.io.OutputStream;

public interface SnippetInterpreter {
    void setOutputStream(OutputStream out);
    void setErrorStream(OutputStream err);
    Object eval(String snippet) throws SnippetEvalException;
    String[] completions(String partialInput, int position);
    void reset();
}
