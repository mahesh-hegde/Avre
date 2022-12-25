package hegde.mahesh.avre.database;

import hegde.mahesh.avre.model.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepository {
    private final HistoryDao dao;
    private List<HistoryItem> history = new ArrayList<>();

    public HistoryRepository(AvreDatabase db) {
        dao = db.historyDao();
        db.getQueryExecutor().execute(() -> history = dao.getAll());
    }

    public List<HistoryItem> getHistory() {
        return history;
    }

    public void addItem(HistoryItem item) {
        AvreDatabase.databaseWriteExecutor.execute(() -> dao.insert(item));
        history.add(item);
    }

    public int getHistorySize() {
        return history.size();
    }

    public void reset() {
        history.clear();
        AvreDatabase.databaseWriteExecutor.execute(dao::deleteAll);
    }
}
