package com.eventor.activities;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.eventor.AppDatabase;
import com.eventor.HttpHandler;
import com.eventor.model.Activity;
import com.eventor.MainActivity;
import com.eventor.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class SplashActivity extends AppCompatActivity {
    private String TAG = SplashActivity.class.getSimpleName();
    private Activity activity;
    private AppDatabase database;
    private ProgressBar pgsBar;
    private ViewGroup viewGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        database = AppDatabase.getDatabase(getApplicationContext());
        viewGroup = (ViewGroup) ((ViewGroup) this
                .findViewById(android.R.id.content)).getChildAt(0);
        pgsBar = (ProgressBar)findViewById(R.id.pBar);

        // database.activityDao().removeAllActivities();

        final List<Activity> activities = database.activityDao().getAllActivity();
        if (activities.size()==0) {
            new GetContacts().execute();
        } else {
            int secondsDelayed = 1;
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                    finish();
                }
            }, secondsDelayed * 1000);
        }
    }

    private class GetContacts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pgsBar.setVisibility(viewGroup.VISIBLE);
            Toast.makeText(SplashActivity.this,"Downloading data",Toast.LENGTH_LONG).show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response
            String urlSporthal = "https://opendata.arcgis.com/datasets/b5f4516df41545299730850103c6443d_643.geojson";
            String urlZwembad = "https://opendata.arcgis.com/datasets/b760e319033841348469bacb34c5e259_644.geojson";
            String urlCultuur = "https://opendata.arcgis.com/datasets/5ccff54ed791480aa91bc8f5fcf2e9ab_292.geojson";
            String urlErfgoed = "https://opendata.arcgis.com/datasets/1318485a5b5f4bb0b17f4547dfef929f_293.geojson";
            String jsonStrSporthal = sh.makeServiceCall(urlSporthal);
            String jsonStrZwembad = sh.makeServiceCall(urlZwembad);
            String jsonStrCultuur = sh.makeServiceCall(urlCultuur);
            String jsonStrErfgoed = sh.makeServiceCall(urlErfgoed);

            if (jsonStrSporthal != null && jsonStrZwembad != null && jsonStrCultuur != null && jsonStrErfgoed != null) {
                try {
                    JSONObject jsonObjSporthal = new JSONObject(jsonStrSporthal);
                    JSONObject jsonObjZwembad = new JSONObject(jsonStrZwembad);
                    JSONObject jsonObjCultuur = new JSONObject(jsonStrCultuur);
                    JSONObject jsonObjErfgoed = new JSONObject(jsonStrErfgoed);

                    // Getting JSON Array node
                    JSONArray features = jsonObjSporthal.getJSONArray("features");

                    // looping through All Sporthalls
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject activity = features.getJSONObject(i);
                        JSONObject aProps = activity.getJSONObject("properties");
                        if (aProps.getString("publiek").equals("openbaar")) {
                            database.activityDao().addActivity(new Activity(database.activityDao().getNextId(), aProps.getString("naam"), aProps.getString("straat"), aProps.getString("type"),
                                    aProps.getString("pero"), aProps.getString("huisnummer"), aProps.getInt("postcode"), aProps.getString("district")));
                        }
                    }

                    // Getting JSON Array node
                    features = jsonObjZwembad.getJSONArray("features");

                    // looping through All Swimminghalls
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject activity = features.getJSONObject(i);
                        JSONObject aProps = activity.getJSONObject("properties");
                        if (aProps.getString("publiek").equals("openbaar")) {
                            database.activityDao().addActivity(new Activity(database.activityDao().getNextId(), aProps.getString("naam"), aProps.getString("straat"), aProps.getString("type"),
                                    aProps.getString("pero"), aProps.getString("huisnummer"), aProps.getInt("postcode"), aProps.getString("district")));
                        }
                    }

                    // Getting JSON Array node
                    features = jsonObjCultuur.getJSONArray("features");

                    // looping through All Cultural places
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject feature = features.getJSONObject(i);
                        JSONObject aProps = feature.getJSONObject("properties");
                        database.activityDao().addActivity(new Activity(database.activityDao().getNextId(), aProps.getString("naam"), aProps.getString("straat"), aProps.getString("type"),
                                aProps.getString("categorie"), aProps.getString("huisnr"), aProps.getInt("postcode"), aProps.getString("gemeente")));
                    }

                    // Getting JSON Array node
                    features = jsonObjErfgoed.getJSONArray("features");

                    // looping through All Historic places
                    for (int i = 0; i < features.length(); i++) {
                        JSONObject feature = features.getJSONObject(i);
                        JSONObject aProps = feature.getJSONObject("properties");
                        database.activityDao().addActivity(new Activity(database.activityDao().getNextId(), aProps.getString("naam"), aProps.getString("straat"), aProps.getString("type"),
                                aProps.getString("categorie"), aProps.getString("huisnr"), aProps.getInt("postcode"), aProps.getString("gemeente")));
                    }

                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }
    }
}