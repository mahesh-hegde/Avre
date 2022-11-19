package hegde.mahesh.avre.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import hegde.mahesh.avre.R;
import hegde.mahesh.avre.model.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {
    List<HistoryItem> history;
    int begin;
    int mItemCount;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryItem> history) {
        this.inflater = LayoutInflater.from(context);
        this.history = history;
    }

    public void setBegin(int begin) {
        this.begin = begin;
        notifyItemRangeRemoved(0, mItemCount);
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
        HistoryItem current = history.get(position + begin);
        holder.code.setText(current.code);

        if (current.out.isEmpty()) {
            holder.out.setVisibility(View.GONE);
        }

        holder.out.setText(current.out);

        if (current.err.isEmpty()) {
            holder.err.setVisibility(View.GONE);
        }
        holder.err.setText(current.err);
        if (current.exception != null || current.stackTrace != null) {
            holder.result.setText(R.string.an_exception_occurred);
            holder.result.setTextColor(0xffff0000);
        } else {
            holder.result.setText(current.res);
        }
    }

    @Override
    public int getItemCount() {
        Log.d("AVRE", "size = " + history.size() + ", begin = " + begin);
        mItemCount = history.size() - begin;
        return mItemCount;
    }

    public void notifyItemAppended() {
        notifyItemInserted(getItemCount() - 1);
    }

    static class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView code, result, out, err;

        public HistoryItemViewHolder(View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.history_code);
            result = itemView.findViewById(R.id.history_result);
            out = itemView.findViewById(R.id.history_out);
            err = itemView.findViewById(R.id.history_err);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // throw new RuntimeException("Not implemented");
        }
    }
}
