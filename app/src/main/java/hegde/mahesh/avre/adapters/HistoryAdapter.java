package hegde.mahesh.avre.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import hegde.mahesh.avre.R;
import hegde.mahesh.avre.model.HistoryItem;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryItemViewHolder> {
    List<HistoryItem> history;
    LayoutInflater inflater;

    public HistoryAdapter(Context context, List<HistoryItem> history) {
        this.inflater = LayoutInflater.from(context);
        this.history = history;
    }

    @NonNull
    @Override
    public HistoryItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.history_item, parent, false);
        return new HistoryItemViewHolder(itemView, this);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryItemViewHolder holder, int position) {
        HistoryItem current = history.get(position);
        holder.code.setText(current.code);
        if (current.out.isEmpty()) {
            holder.out.setVisibility(View.GONE);
        }
        holder.out.setText(current.out);

        if (current.err.isEmpty()) {
            holder.err.setVisibility(View.GONE);
        }
        holder.err.setText(current.err);
        if (current.exception != null) {
            holder.result.setText(current.exception.getMessage());
            holder.result.setHintTextColor(0xffff0000);
        } else {
            holder.result.setText(current.res);
        }
    }

    @Override
    public int getItemCount() {
        return history.size();
    }

    static class HistoryItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView code, result, out, err;
        RecyclerView.Adapter<HistoryItemViewHolder> adapter;

        public HistoryItemViewHolder(View itemView, HistoryAdapter adapter) {
            super(itemView);
            code = itemView.findViewById(R.id.history_code);
            result = itemView.findViewById(R.id.history_result);
            out = itemView.findViewById(R.id.history_out);
            err = itemView.findViewById(R.id.history_err);
            itemView.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            // Implement onClick
        }

    }
}
