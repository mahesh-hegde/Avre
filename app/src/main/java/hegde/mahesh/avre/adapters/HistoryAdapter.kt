package hegde.mahesh.avre.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import hegde.mahesh.avre.R
import hegde.mahesh.avre.adapters.HistoryAdapter.HistoryItemViewHolder
import hegde.mahesh.avre.model.HistoryItem

class HistoryAdapter(context: Context?, history: List<HistoryItem>) : RecyclerView.Adapter<HistoryItemViewHolder>() {
    private var mHistory: List<HistoryItem>
    private var mBegin = 0
    private var mItemCount = 0
    private var mInflater: LayoutInflater

    init {
        mInflater = LayoutInflater.from(context)
        this.mHistory = history
    }

    fun setBegin(begin: Int) {
        this.mBegin = begin
        notifyItemRangeRemoved(0, mItemCount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryItemViewHolder {
        val itemView = mInflater.inflate(R.layout.history_item, parent, false)
        return HistoryItemViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: HistoryItemViewHolder, position: Int) {
        val current = mHistory[position + mBegin]
        holder.code.text = current.code
        if (current.out.isNullOrEmpty()) {
            holder.out.visibility = View.GONE
        }
        holder.out.text = current.out
        if (current.err.isNullOrEmpty()) {
            holder.err.visibility = View.GONE
        }
        holder.err.text = current.err
        if (current.exception != null || current.stackTrace != null) {
            holder.result.setText(R.string.an_exception_occurred)
            holder.result.setTextColor(-0x10000)
        } else {
            holder.result.text = current.res
        }
    }

    override fun getItemCount(): Int {
        Log.d("AVRE", "size = " + mHistory.size + ", begin = " + mBegin)
        mItemCount = mHistory.size - mBegin
        return mItemCount
    }

    fun notifyItemAppended() {
        notifyItemInserted(itemCount - 1)
    }

    class HistoryItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var code: TextView
        var result: TextView
        var out: TextView
        var err: TextView

        init {
            code = itemView.findViewById(R.id.history_code)
            result = itemView.findViewById(R.id.history_result)
            out = itemView.findViewById(R.id.history_out)
            err = itemView.findViewById(R.id.history_err)
            itemView.setOnClickListener(this)
        }

        override fun onClick(v: View) {
            // throw new RuntimeException("Not implemented");
        }
    }
}