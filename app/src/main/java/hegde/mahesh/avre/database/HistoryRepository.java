package hegde.mahesh.avre.database;

import hegde.mahesh.avre.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private final List<HistoryItem> history = new ArrayList<>();

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void addItem(HistoryItem item) {
        history.add(item);
    }

    public int getHistorySize() {
        return history.size();
    }

    public HistoryItem getItem(int i) {
        return history.get(i);
    }

    public void reset() {
        history.clear();
    }
}
