package hegde.mahesh.avre.interpreter;

import java.io.OutputStream;

public interface SnippetInterpreter {
    void setVariable(String variable, Object value) throws SnippetEvalException;
    void setOutputStream(OutputStream out);
    void setErrorStream(OutputStream err);
    Object eval(String snippet) throws SnippetEvalException;
    String[] completions(String partialInput, int position) throws SnippetEvalException;
    void reset();
}
