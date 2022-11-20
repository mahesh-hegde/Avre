package hegde.mahesh.avre.interpreter

import bsh.EvalError
import bsh.Interpreter
import java.io.OutputStream
import java.io.PrintStream

class BshInterpreter : LanguageInterpreter {
    private var ipr = Interpreter()

    @Throws(SnippetEvalException::class)
    override fun setVariable(variable: String, value: Any?) {
        try {
            ipr[variable] = value
        } catch (e: EvalError) {
            throw convertEvalError(e)
        }
    }

    override fun setOutputStream(out: OutputStream) {
        ipr.out = PrintStream(out)
    }

    override fun setErrorStream(err: OutputStream) {
        ipr.err = PrintStream(err)
    }

    @Throws(SnippetEvalException::class)
    override fun eval(snippet: String): Any {
        return try {
            ipr.eval(snippet)
        } catch (e: EvalError) {
            throw convertEvalError(e)
        }
    }

    override fun getCompletions(partialInput: String, position: Int): Array<String> {
        return arrayOf()
    }

    override fun reset() {
        ipr = Interpreter()
    }

    companion object {
        private fun convertEvalError(e: EvalError): SnippetEvalException {
            val lineNum = e.errorLineNumber
            var cause = e.cause
            if (cause == null) {
                cause = e
            }
            return SnippetEvalException(lineNum, cause)
        }
    }
}