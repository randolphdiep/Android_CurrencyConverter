package eu.rafalsiuta.simplecalc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;


public class CurrencyAdapter extends RecyclerView.Adapter<eu.rafalsiuta.simplecalc.CurrencyAdapter.Holder> {
    private Context context;
    private List<Currency> currencyList;
    private List<Currency> currencyListCopy = new ArrayList<>();

    public CurrencyAdapter(Context context, List<Currency> currencyList) {
        this.context = context;
        this.currencyList = currencyList;
        this.currencyListCopy.addAll(currencyList);
    }

    public void filter(String text) {

        currencyList.clear();
        if (text.isEmpty()) {
            currencyList.addAll(currencyListCopy);
        } else {
            text = text.toLowerCase();
            for (Currency item : currencyListCopy) {
                if (item.getCurrencyName().toLowerCase().contains(text) | item.getCurrencyCode().toLowerCase().contains(text)) {
                    currencyList.add(item);
                }
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.currency_list, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CurrencyAdapter.Holder holder, int position) {
        String name = currencyList.get(position).getCurrencyName();
        String code = currencyList.get(position).getCurrencyCode();
        holder.currencyCode.setText(code);
        holder.currencyName.setText(name);
        int id = context.getResources().getIdentifier(code.toLowerCase(), "drawable", context.getPackageName());
        try {
            Glide.with(context).load(id).into(holder.currencyFlag);
        } catch (Exception e) {
        }
    }

    @Override
    public int getItemCount() {
        return currencyList.size();
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView currencyFlag;
        TextView currencyName;
        TextView currencyCode;

        public Holder(@NonNull View itemView) {
            super(itemView);
            currencyFlag = itemView.findViewById(R.id.currencyFlag);
            currencyName = itemView.findViewById(R.id.currencyName);
            currencyCode = itemView.findViewById(R.id.currencyCode);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String code = currencyCode.getText().toString();
            int idFlag = context.getResources().getIdentifier(code.toLowerCase(), "drawable", context.getPackageName());
            Intent data = new Intent();
            Currency.packageIntent(data, code, idFlag);
            ((Activity) context).setResult(Activity.RESULT_OK, data);
            ((Activity) context).finish();
        }
    }
}
