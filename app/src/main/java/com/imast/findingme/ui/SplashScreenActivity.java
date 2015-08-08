package com.imast.findingme.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.District;
import com.imast.findingme.model.PetType;
import com.imast.findingme.model.Race;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

public class SplashScreenActivity extends AppCompatActivity {

    private static final String TAG = makeLogTag(SplashScreenActivity.class);

    private int counterLoadedData = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        loadInitData();

        Thread loading = new Thread() {
            public void run() {
                try {

                    sleep(5000);

                    Intent main = new Intent(SplashScreenActivity.this, LoginActivity.class);
                    startActivity(main);
                    finish();

                    /*if (counterLoadedData == 3) {

                    }*/

                }

                catch (Exception e) {
                    e.printStackTrace();
                }

                finally {

                }
            }
        };

        loading.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void loadInitData() {

        loadDistrict();
        loadPetType();
        loadRace();

    }

    private void loadDistrict() {

        Toast.makeText(getApplicationContext(), "Cargando Distritos", Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/districts.json", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<District>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        Config.lstDistrict = gson.fromJson(response.toString(), listType);

                        counterLoadedData++;

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOGD(TAG, "Error Volley:"+ error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error de Conexión con el Servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }

    private void loadPetType() {

        Toast.makeText(getApplicationContext(), "Cargando Tipo Mascotas", Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/pet_types.json", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<PetType>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        Config.lstPetType = gson.fromJson(response.toString(), listType);

                        counterLoadedData++;

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOGD(TAG, "Error Volley:"+ error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error de Conexión con el Servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }

    private void loadRace() {

        Toast.makeText(getApplicationContext(), "Cargando Razas", Toast.LENGTH_SHORT).show();

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/races.json", null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<Race>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        Config.lstRace = gson.fromJson(response.toString(), listType);

                        counterLoadedData++;

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        LOGD(TAG, "Error Volley:"+ error.getMessage());
                        Toast.makeText(getApplicationContext(), "Error de Conexión con el Servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        VolleySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsArrayRequest);
    }
}
