package com.imast.findingme.ui.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.imast.findingme.adapters.MyPetAdapter;
import com.imast.findingme.model.Pet;
import com.imast.findingme.util.DividerItemDecoration;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;


public class MyPetsFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(MyPetsFragment.class);

    private FloatingActionButton fabAddPet;

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private MyPetAdapter myPetAdapter;
    private RecyclerView.LayoutManager lManager;


    public MyPetsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Mis Mascotas");

        fabAddPet.setOnClickListener(this);

        List items = new ArrayList();

        recycler.setHasFixedSize(true);
        recycler.setItemAnimator(new DefaultItemAnimator());
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        myPetAdapter = new MyPetAdapter(getActivity().getApplicationContext(), getActivity(), items);

        adapter = myPetAdapter;
        recycler.setAdapter(adapter);

        getMyPets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_my_pets, container, false);

        fabAddPet = (FloatingActionButton) view.findViewById(R.id.fabAddPet);
        recycler = (RecyclerView) view.findViewById(R.id.my_pets_recycler);

        return view;
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fabAddPet:
                goToAddPet();
                break;
        }

    }

    private void goToAddPet() {

        Fragment fragment = new AddPetFragment();

        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void getMyPets() {

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Finding Me");
        progress.setMessage("Obteniendo Mis Mascotas...");
        progress.show();

        String parameters = String.format("/%d/pets", Config.user.getId());

        LOGD(TAG, parameters);

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/users" + parameters, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<Pet>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        List<Pet> lstMyPet = gson.fromJson(response.toString(), listType);

                        if (lstMyPet.size() > 0)
                        {
                            LOGD(TAG, "Cantidad de MyPets " + lstMyPet.size());
                            myPetAdapter.clearData();
                            myPetAdapter.setItems(lstMyPet);
                            myPetAdapter.notifyDataSetChanged();
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
