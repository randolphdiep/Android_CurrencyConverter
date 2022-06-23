package eu.rafalsiuta.simplecalc;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class CurrencyActivity extends AppCompatActivity{

    private RecyclerView recyclerView;
    private List<Currency> currencyList;
    private CurrencyAdapter currencyAdapter;
    private SearchView searchView;
    private MenuItem searchItem;
    private Boolean state = true;
    private Currency currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_currency);
        recyclerView = findViewById(R.id.recyclerView);
        currencyList = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        apiCall();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        searchItem = menu.findItem(R.id.action_search);
        searchView = (SearchView) searchItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                currencyAdapter.filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currencyAdapter.filter(newText);
                return true;
            }
        });

        return true;
    }

    private void apiCall() {
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            for (Iterator<String> iter = obj.keys(); iter.hasNext(); ) {
                String key = iter.next();
                JSONObject obj1 = obj.getJSONObject(key);
                String name = obj1.getString("name");
                currency = new Currency(name, key);
                currencyList.add(currency);
            }
            currencyAdapter = new CurrencyAdapter(CurrencyActivity.this, currencyList);
            recyclerView.setAdapter(currencyAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open("Common-Currency.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }


}