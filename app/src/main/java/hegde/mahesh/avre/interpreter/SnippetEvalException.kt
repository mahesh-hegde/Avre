package hegde.mahesh.avre.interpreter

import java.util.*

class SnippetEvalException(private var line: Int, override var cause: Throwable) : Exception(cause.message) {
    override val message: String?
        get() = if (line == 0) {
            cause.message
        } else String.format(Locale.getDefault(), "[line %s]: %s", line, cause.toString())
}