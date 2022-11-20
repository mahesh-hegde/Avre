package hegde.mahesh.avre.interpreter

import java.io.OutputStream

interface LanguageInterpreter {
    @Throws(SnippetEvalException::class)
    fun setVariable(variable: String, value: Any?)
    fun setOutputStream(out: OutputStream)
    fun setErrorStream(err: OutputStream)

    @Throws(SnippetEvalException::class)
    fun eval(snippet: String): Any?

    @Throws(SnippetEvalException::class)
    fun getCompletions(partialInput: String, position: Int): Array<String>
    fun reset()
}