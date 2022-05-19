package hegde.mahesh.avre.interpreter;

import java.io.OutputStream;
import java.io.PrintStream;

import bsh.EvalError;
import bsh.Interpreter;

public class BshInterpreter implements SnippetInterpreter {
    Interpreter ipr = new Interpreter();
    public BshInterpreter() {}

    @Override
    public void setOutputStream(OutputStream out) {
        ipr.setOut(new PrintStream(out));
    }

    @Override
    public void setErrorStream(OutputStream err) {
        ipr.setErr(new PrintStream(err));
    }

    @Override
    public Object eval(String snippet) throws SnippetEvalException {
        try {
            return ipr.eval(snippet);
        } catch (EvalError e) {
            int lineNum = e.getErrorLineNumber();
            int colNum = -1;
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new SnippetEvalException(lineNum, colNum, cause.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] completions(String partialInput, int position) {
        return new String[]{};
    }

    @Override
    public void reset() {

    }
}
