package com.example.covid19tracker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.covid19tracker.Models.All;
import com.example.covid19tracker.Models.Country;
import com.leo.simplearcloader.SimpleArcLoader;

import org.eazegraph.lib.models.PieModel;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AffectedCountries extends AppCompatActivity {

    EditText edtSearch;
    ListView listView;
    SimpleArcLoader simpleArcLoader;
    private static final String TAG = "Shannukk";

    public static List<Country> countryModelsList = new ArrayList<>();
    Country countryModel;
    MyCustomAdapter myCustomAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_affected_countries);

        edtSearch = findViewById(R.id.edtSearch);
        listView = findViewById(R.id.listView);
        simpleArcLoader = findViewById(R.id.loader);

        getSupportActionBar().setTitle("Affected Countries");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        fetchData();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                startActivity(new Intent(getApplicationContext(),DetailActivity.class).putExtra("position",position));
            }
        });


        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                myCustomAdapter.getFilter().filter(s);
                myCustomAdapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }



    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home)
            finish();
        return super.onOptionsItemSelected(item);
    }

    private void fetchData() {

        simpleArcLoader.start();

        Methods methods=RetrofitClient.getRetrofitInstance().create(Methods.class);
        Call<List<Country>> call = methods.getContDAta();
        call.enqueue(new Callback<List<Country>>() {
            @Override
            public void onResponse(Call<List<Country>> call, Response<List<Country>> response) {

                try {
                    for (int i = 0; i < response.body().size(); i++) {

                        String countryName = response.body().get(i).getCountry();
                        String cases = response.body().get(i).getCases();
                        String todayCases = response.body().get(i).getTodayCases();
                        String deaths = response.body().get(i).getDeaths();
                        String todayDeaths = response.body().get(i).getTodayDeaths();
                        String recovered = response.body().get(i).getRecovered();
                        String active = response.body().get(i).getActive();
                        String critical = response.body().get(i).getCritical();
                        String flagUrl = response.body().get(i).getCountryInfo().getFlag1();
                        Log.e(TAG, "onResponse: " + cases);
                        Log.e(TAG, "onResponse: " + flagUrl);

                        countryModel = new Country(flagUrl, countryName, cases, todayCases, deaths, todayDeaths, recovered, active, critical);
                        countryModelsList.add(countryModel);

                        myCustomAdapter = new MyCustomAdapter(AffectedCountries.this, countryModelsList);
                        listView.setAdapter(myCustomAdapter);
                        simpleArcLoader.stop();
                        simpleArcLoader.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    simpleArcLoader.stop();
                    simpleArcLoader.setVisibility(View.GONE);
                }
            }

            @Override
            public void onFailure(Call<List<Country>> call, Throwable t) {
                Log.e(TAG, "onFailure: "+ t.getMessage() );

            }
        });

//        String url  = "https://corona.lmao.ninja/v2/countries/";
//
//        simpleArcLoader.start();
//
//        StringRequest request = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//
//                        try {
//                            JSONArray jsonArray = new JSONArray(response);
//
//                            for(int i=0;i<jsonArray.length();i++){
//
//                                JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                                String countryName = jsonObject.getString("country");
//                                String cases = jsonObject.getString("cases");
//                                String todayCases = jsonObject.getString("todayCases");
//                                String deaths = jsonObject.getString("deaths");
//                                String todayDeaths = jsonObject.getString("todayDeaths");
//                                String recovered = jsonObject.getString("recovered");
//                                String active = jsonObject.getString("active");
//                                String critical = jsonObject.getString("critical");
//
//                                JSONObject object = jsonObject.getJSONObject("countryInfo");
//                                String flagUrl = object.getString("flag");
//
//                                countryModel = new Country(flagUrl,countryName,cases,todayCases,deaths,todayDeaths,recovered,active,critical);
//                                countryModelsList.add(countryModel);
//
//
//                            }
//
//                                myCustomAdapter = new MyCustomAdapter(AffectedCountries.this,countryModelsList);
//                                listView.setAdapter(myCustomAdapter);
//                                simpleArcLoader.stop();
//                                simpleArcLoader.setVisibility(View.GONE);
//
//
//
//
//
//
//                        } catch (JSONException e) {
//                            e.printStackTrace();
//                            simpleArcLoader.stop();
//                            simpleArcLoader.setVisibility(View.GONE);
//                        }
//
//
//                    }
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                simpleArcLoader.stop();
//                simpleArcLoader.setVisibility(View.GONE);
//                Toast.makeText(AffectedCountries.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        RequestQueue requestQueue = Volley.newRequestQueue(this);
//        requestQueue.add(request);


    }

}
