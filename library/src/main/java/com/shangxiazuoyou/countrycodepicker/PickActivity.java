package com.shangxiazuoyou.countrycodepicker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class PickActivity extends AppCompatActivity {

    private ArrayList<Country> selectedCountries = new ArrayList<>();
    private ArrayList<Country> allCountries = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pick);
        RecyclerView rvPick = (RecyclerView) findViewById(R.id.rv_pick);
        final EditText etSearch = (EditText) findViewById(R.id.edt_search);
        TextView tvCancel = (TextView) findViewById(R.id.tv_cancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PickActivity.this.finish();
            }
        });
        allCountries.clear();
        allCountries.addAll(Country.getAll(this, null));
        selectedCountries.clear();
        selectedCountries.addAll(allCountries);
        final CAdapter adapter = new CAdapter(selectedCountries);
        rvPick.setAdapter(adapter);
        final LinearLayoutManager manager = new LinearLayoutManager(this);
        rvPick.setLayoutManager(manager);
        rvPick.setAdapter(adapter);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String string = s.toString();
                selectedCountries.clear();
                for (Country country : allCountries) {
                    if (country.name.toLowerCase().contains(string.toLowerCase()))
                        selectedCountries.add(country);
                }
                adapter.update(selectedCountries);
            }
        });
    }

    class CAdapter extends PinyinAdapter<RecyclerView.ViewHolder> {

        public CAdapter(List<? extends PinyinEntity> entities) {
            super(entities);
        }

        @Override
        public RecyclerView.ViewHolder onCreateHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(getLayoutInflater().inflate(R.layout.item_country, parent, false));
        }

        @Override
        public void onBindHolder(RecyclerView.ViewHolder holder, PinyinEntity entity, int position) {
            ViewHolder viewHolder = (ViewHolder) holder;
            final Country country = (Country) entity;
            viewHolder.tvName.setText(country.name);
            viewHolder.tvCode.setText("+" + country.code);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent data = new Intent();
                    data.putExtra("country", country.toJson());
                    setResult(Activity.RESULT_OK, data);
                    finish();
                }
            });
        }
    }
}
