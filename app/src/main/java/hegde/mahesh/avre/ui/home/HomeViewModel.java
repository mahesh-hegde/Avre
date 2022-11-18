package hegde.mahesh.avre.ui.home;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import hegde.mahesh.avre.database.AvreDatabase;
import hegde.mahesh.avre.database.HistoryRepository;
import hegde.mahesh.avre.interpreter.BshInterpreter;
import hegde.mahesh.avre.interpreter.LanguageInterpreter;
import hegde.mahesh.avre.interpreter.SnippetEvalException;
import hegde.mahesh.avre.model.HistoryItem;

import java.io.ByteArrayOutputStream;
import java.util.List;

/// Stores HomeFragment state
public class HomeViewModel extends AndroidViewModel {
    private final LanguageInterpreter interpreter = new BshInterpreter();
    public LiveData<String> getCurrentSnippet() {
        return currentSnippet;
    }

    private final MutableLiveData<String> currentSnippet = new MutableLiveData<>("");

    public LiveData<Integer> getVisibleHistoryStart() {
        return visibleHistoryStart;
    }

    public final MutableLiveData<Integer> visibleHistoryStart = new MutableLiveData<>(0);

    public LiveData<Integer> getLastAppendedPosition() {
        return lastAppendedPosition;
    }

    public final MutableLiveData<Integer> lastAppendedPosition = new MutableLiveData<>(-1);

    private int cursor;

    private final HistoryRepository repo;

    public List<HistoryItem> getHistoryItems() {
        return repo.getHistory();
    }
    private final ByteArrayOutputStream out = new ByteArrayOutputStream();
    private final ByteArrayOutputStream err = new ByteArrayOutputStream();

    public HomeViewModel(Application application) {
        super(application);
        AvreDatabase db = AvreDatabase.getDatabase(application);
        repo = new HistoryRepository(db);
        interpreter.setOutputStream(out);
        interpreter.setErrorStream(err);
    }

    public void eval(String snippet) {
        try {
            Object result = interpreter.eval(snippet);
            String valClass = result != null ? (": " + result.getClass()) : "";

            String outStr = readOutput(out);
            String errStr = readOutput(err);

            HistoryItem historyItem = HistoryItem.success(snippet, outStr, errStr,
                    result + valClass);
            repo.addItem(historyItem);
        } catch (Exception e) {
            HistoryItem failure = HistoryItem.failure(snippet, readOutput(out), readOutput(err), e);
            repo.addItem(failure);
        }
        cursor = repo.getHistorySize();
        lastAppendedPosition.setValue(cursor - 1);
    }

    public void setInterpreterVariable(String name, Object value) {
        try {
            interpreter.setVariable(name, value);
        } catch (SnippetEvalException e) {
            throw new RuntimeException(e);
        }
    }

    public void clearVisibleHistory() {
        visibleHistoryStart.setValue(repo.getHistorySize());
    }

    public void reset() {
        repo.reset();
        cursor = 0;
        visibleHistoryStart.setValue(0);
        interpreter.reset();
    }

    public void previousSnippet() {
        String code;
        List<HistoryItem> history = repo.getHistory();
        if (cursor - 1 >= 0) {
            cursor = cursor - 1;
            code = history.get(cursor).code;
        } else if (!history.isEmpty()) {
            code = history.get(0).code;
        } else {
            code = "";
        }
        currentSnippet.setValue(code);
    }

    public void nextSnippet() {
        List<HistoryItem> history = repo.getHistory();
        String code;
        cursor = Math.min(cursor + 1, history.size());
        if (cursor == history.size()) {
            code = "";
        } else {
            code = history.get(cursor).code;
        }
        currentSnippet.setValue(code);
    }

    private static String readOutput(ByteArrayOutputStream stream) {
        String result = stream.toString();
        stream.reset();
        return result;
    }
}