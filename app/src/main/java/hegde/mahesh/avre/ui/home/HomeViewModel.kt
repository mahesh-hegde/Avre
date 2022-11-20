package hegde.mahesh.avre.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import hegde.mahesh.avre.database.AvreDatabase.Companion.getDatabase
import hegde.mahesh.avre.database.HistoryRepository
import hegde.mahesh.avre.interpreter.BshInterpreter
import hegde.mahesh.avre.interpreter.LanguageInterpreter
import hegde.mahesh.avre.interpreter.SnippetEvalException
import hegde.mahesh.avre.model.HistoryItem
import hegde.mahesh.avre.model.HistoryItem.Companion.failure
import hegde.mahesh.avre.model.HistoryItem.Companion.success
import java.io.ByteArrayOutputStream

/// Stores HomeFragment state
class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val interpreter: LanguageInterpreter = BshInterpreter()

    private val mCurrentSnippet = MutableLiveData("")
    val currentSnippet: LiveData<String>
        get() = mCurrentSnippet

    private val mVisibleHistoryStart = MutableLiveData(0)
    val visibleHistoryStart: LiveData<Int>
        get() = mVisibleHistoryStart

    private val mHistorySize = MutableLiveData<Int>()
    val historySize: LiveData<Int>
        get() = mHistorySize

    private var cursor = 0

    private val repo: HistoryRepository

    val historyItems: List<HistoryItem>
        get() = repo.history

    private val out = ByteArrayOutputStream()
    private val err = ByteArrayOutputStream()

    init {
        val db = getDatabase(application)
        repo = HistoryRepository(db)
        initializeInterpreter()
    }

    private fun initializeInterpreter() {
        interpreter.setOutputStream(out)
        interpreter.setErrorStream(err)
    }

    fun eval(snippet: String) {
        try {
            val result = interpreter.eval(snippet)
            val valClass = if (result != null) ": " + result.javaClass else ""
            val outStr = readOutput(out)
            val errStr = readOutput(err)
            val historyItem = success(
                snippet, outStr, errStr,
                result.toString() + valClass
            )
            repo.addItem(historyItem)
        } catch (e: Exception) {
            val failure = failure(snippet, readOutput(out), readOutput(err), e)
            repo.addItem(failure)
        }
        val size = repo.getHistorySize()
        cursor = size
        mHistorySize.value = size
    }

    fun setInterpreterVariable(name: String, value: Any?) {
        try {
            interpreter.setVariable(name, value)
        } catch (e: SnippetEvalException) {
            throw RuntimeException(e)
        }
    }

    fun clearVisibleHistory() {
        mVisibleHistoryStart.value = repo.getHistorySize()
        mHistorySize.value = 0
        cursor = 0
    }

    fun reset() {
        repo.reset()
        interpreter.reset()
        clearVisibleHistory()
    }

    fun previousSnippet() {
        val code: String
        val history: List<HistoryItem> = repo.history
        if (cursor - 1 >= 0) {
            cursor -= 1
            code = history[cursor].code
        } else if (history.isNotEmpty()) {
            code = history[0].code
        } else {
            code = ""
        }
        mCurrentSnippet.value = code
    }

    fun nextSnippet() {
        val history: List<HistoryItem> = repo.history
        cursor = (cursor + 1).coerceAtMost(history.size)
        val code: String = if (cursor == history.size) {
            ""
        } else {
            history[cursor].code
        }
        mCurrentSnippet.value = code
    }

    companion object {
        private fun readOutput(stream: ByteArrayOutputStream): String {
            val result = stream.toString()
            stream.reset()
            return result
        }
    }
}