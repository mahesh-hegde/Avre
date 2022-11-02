package hegde.mahesh.avre.interpreter;

import java.io.OutputStream;
import java.io.PrintStream;

import bsh.EvalError;
import bsh.Interpreter;

public class BshInterpreter implements LanguageInterpreter {
    Interpreter ipr = new Interpreter();
    public BshInterpreter() {
        initializeInterpreter(ipr);
    }

    @Override
    public void setVariable(String variable, Object value) throws SnippetEvalException {
        try {
            ipr.set(variable, value);
        } catch (EvalError e) {
            throw convertEvalError(e);
        }
    }

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
            throw convertEvalError(e);
        }
    }

    @Override
    public String[] getCompletions(String partialInput, int position) {
        return new String[]{};
    }

    @Override
    public void reset() {
        ipr = new Interpreter();
        initializeInterpreter(ipr);
    }

    private void initializeInterpreter(Interpreter ignored) {
        // Eval startup file
    }

    private static SnippetEvalException convertEvalError(EvalError e) {
        int lineNum = e.getErrorLineNumber();
        int colNum = -1;
        Throwable cause = e.getCause();
        if (cause == null) {
            cause = e;
        }
        return new SnippetEvalException(lineNum, colNum, cause);
    }
}
