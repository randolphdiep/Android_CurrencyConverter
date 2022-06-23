package eu.rafalsiuta.simplecalc;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.Collections;
import java.util.List;


public class HistoryAdapter extends RecyclerView.Adapter<eu.rafalsiuta.simplecalc.HistoryAdapter.Holder> {
    private Context context;
    private List<History> historyList;

    public HistoryAdapter(Context context, int history_list, List<History> historyList) {
        this.context = context;
        this.historyList = historyList;
        Collections.reverse(historyList);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.history_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        String codeInput = historyList.get(position).getCodeInput();
        String codeOutput = historyList.get(position).getCodeOutput();
        String input = historyList.get(position).getInput();
        String output = historyList.get(position).getOutput();
        String time = historyList.get(position).getTime();
        String rate = historyList.get(position).getRate();

        holder.codeInput.setText(codeInput);
        holder.codeOutput.setText(codeOutput);
        holder.input.setText(input);
        holder.output.setText(output);
        holder.time.setText(time);
        holder.rate.setText(rate);

        int id1 = context.getResources().getIdentifier(codeInput.toLowerCase(), "drawable", context.getPackageName());
        int id2 = context.getResources().getIdentifier(codeOutput.toLowerCase(), "drawable", context.getPackageName());

        try {
            Glide.with(context).load(id1).into(holder.flagFrom);
            Glide.with(context).load(id2).into(holder.flagTo);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView flagFrom, flagTo;
        TextView codeInput, codeOutput, input, output, time, rate;

        public Holder(@NonNull View itemView) {
            super(itemView);
            flagFrom = itemView.findViewById(R.id.flagFrom);
            flagTo = itemView.findViewById(R.id.flagTo);
            codeInput = itemView.findViewById(R.id.codeInputH);
            codeOutput = itemView.findViewById(R.id.codeOutputH);
            input = itemView.findViewById(R.id.inputH);
            output = itemView.findViewById(R.id.outputH);
            time = itemView.findViewById(R.id.timeTxtH);
            rate = itemView.findViewById(R.id.rateTxtH);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}
