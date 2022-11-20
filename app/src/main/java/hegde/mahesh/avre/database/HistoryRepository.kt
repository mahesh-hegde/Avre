package hegde.mahesh.avre.database

import hegde.mahesh.avre.model.HistoryItem

class HistoryRepository(db: AvreDatabase) {
    private var dao: HistoryDao
    val history: MutableList<HistoryItem> = ArrayList()
    init {
        dao = db.historyDao()
        db.queryExecutor.execute { history.addAll(dao.getAll()) }
    }

    fun addItem(item: HistoryItem) {
        AvreDatabase.databaseWriteExecutor.execute { dao.insert(item) }
        history.add(item)
    }

    fun getHistorySize(): Int {
        return history.size
    }

    fun reset() {
        history.clear()
        AvreDatabase.databaseWriteExecutor.execute(dao::deleteAll)
    }
}