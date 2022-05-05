package hegde.mahesh.avre.model;

import kotlin.reflect.KVariance;

public class HistoryItem {

    String code;
    String out, err, res;
    Exception exception;

    public static HistoryItem success(String code, String out, String err, String res) {
        HistoryItem item = new HistoryItem();
        item.code = code;
        item.out = out;
        item.err = err;
        item.res = res;
        return item;
    }

    public static HistoryItem failure(String code, String out, String err, Exception exception) {
        HistoryItem item = new HistoryItem();
        item.code = code;
        item.out = out;
        item.err = err;
        item.exception = exception;
        return item;
    }
}
