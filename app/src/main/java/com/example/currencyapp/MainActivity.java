package com.example.currencyapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import java.util.*;

public class MainActivity extends AppCompatActivity {

    private EditText etSearch;
    private ListView lvRates;
    private TextView tvBaseCurrency;
    private ArrayAdapter<String> adapter;

    private final List<String> masterRateList = new ArrayList<>();

    private final List<String> filteredRateList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        etSearch = findViewById(R.id.etSearch);
        lvRates = findViewById(R.id.lvRates);
        tvBaseCurrency = findViewById(R.id.tvBaseCurrency);


        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredRateList);
        lvRates.setAdapter(adapter);


        String apiUrl = "https://www.floatrates.com/daily/usd.xml";
        new DataLoader(this).execute(apiUrl);

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterRates(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });


        lvRates.setOnItemClickListener((parent, view, position, id) -> {
            String item = filteredRateList.get(position);
            Toast.makeText(MainActivity.this, item, Toast.LENGTH_SHORT).show();
        });
    }

    public void updateData(List<String> newRates, String baseCurrency) {
        if (newRates == null) return;
        masterRateList.clear();
        masterRateList.addAll(newRates);

        filteredRateList.clear();
        filteredRateList.addAll(newRates);

        // update base currency label
        if (baseCurrency != null && !baseCurrency.isEmpty()) {
            tvBaseCurrency.setText("Base: " + baseCurrency);
        }

        adapter.notifyDataSetChanged();
    }

    private void filterRates(String query) {
        filteredRateList.clear();
        if (query == null || query.trim().isEmpty()) {
            filteredRateList.addAll(masterRateList);
        } else {
            String lower = query.trim().toLowerCase(Locale.ROOT);
            for (String item : masterRateList) {
                if (item.toLowerCase(Locale.ROOT).contains(lower)) {
                    filteredRateList.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
