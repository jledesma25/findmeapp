package com.imast.findingme.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.adapters.MySearchAdapter;
import com.imast.findingme.model.MySearch;
import com.imast.findingme.util.DividerItemDecoration;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

public class MySearchFragment extends Fragment {

    private static final String TAG = makeLogTag(MySearchFragment.class);

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private MySearchAdapter mySearchtAdapter;
    private RecyclerView.LayoutManager lManager;

    public MySearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Mis Búsquedas");

        List items = new ArrayList();

        recycler.setHasFixedSize(true);

        recycler.setItemAnimator(new DefaultItemAnimator());

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        mySearchtAdapter = new MySearchAdapter(getActivity(), getActivity().getApplicationContext(), items);

        // Crear un nuevo adaptador
        adapter = mySearchtAdapter;
        recycler.setAdapter(adapter);

        loadMySearch();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_search, container, false);
        recycler = (RecyclerView) view.findViewById(R.id.recycler_my_search);
        return view;
    }

    private void loadMySearch() {

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Finding Me");
        progress.setMessage("Obteniendo Mis Bùsquedas...");
        progress.show();

        String parameters = String.format("%d/my_searches", Config.user.getId());

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/users/" + parameters, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<MySearch>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        List<MySearch> lstMySearch = gson.fromJson(response.toString(), listType);

                        if (lstMySearch.size() > 0)
                        {
                            LOGD(TAG, "Cantidad de Mis Búsquedas " + lstMySearch.size());
                            mySearchtAdapter.clearData();
                            mySearchtAdapter.setItems(lstMySearch);
                            mySearchtAdapter.notifyDataSetChanged();
                            progress.dismiss();
                        } else {
                            progress.dismiss();
                        }

                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progress.dismiss();
                        LOGD(TAG, "Error Volley:" + error.getMessage());
                        Toast.makeText(getActivity().getApplicationContext(), "Error de Conexión con el Servidor", Toast.LENGTH_SHORT).show();
                    }
                }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                params.put("Content-Type", "application/json");
                params.put("Accept", "application/json");

                return params;
            }
        };

        VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsArrayRequest);

    }

}
