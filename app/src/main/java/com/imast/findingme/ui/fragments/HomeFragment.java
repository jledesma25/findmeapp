package com.imast.findingme.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.imast.findingme.adapters.LostPetAdapter;
import com.imast.findingme.model.District;
import com.imast.findingme.model.LostPet;
import com.imast.findingme.model.PetType;
import com.imast.findingme.util.DividerItemDecoration;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(HomeFragment.class);

    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private LostPetAdapter lostPetAdapter;
    private RecyclerView.LayoutManager lManager;

    private LinearLayout llSearchLostPets;
    private Spinner spnDistrict;
    private Spinner spnPetType;
    private Button btnSearchLostPets;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Inicio");

        ArrayAdapter<District> distritoAdapter = new ArrayAdapter<District>(getActivity(), android.R.layout.simple_spinner_item, Config.lstDistrict);
        distritoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDistrict.setAdapter(distritoAdapter);


        ArrayAdapter<PetType> petTypeAdapter = new ArrayAdapter<PetType>(getActivity(), android.R.layout.simple_spinner_item, Config.lstPetType);
        petTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnPetType.setAdapter(petTypeAdapter);

        btnSearchLostPets.setOnClickListener(this);

        List items = new ArrayList();

        recycler.setHasFixedSize(true);

        recycler.setItemAnimator(new DefaultItemAnimator());

        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        recycler.addItemDecoration(new DividerItemDecoration(getActivity(), null));

        lostPetAdapter = new LostPetAdapter(getActivity().getApplicationContext(), getActivity(), items);

        // Crear un nuevo adaptador
        adapter = lostPetAdapter;
        recycler.setAdapter(adapter);

        searchLostPets();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        llSearchLostPets = (LinearLayout) view.findViewById(R.id.llSearchLostPets);
        spnDistrict = (Spinner) view.findViewById(R.id.district_spinner);
        spnPetType = (Spinner) view.findViewById(R.id.petType_spinner);
        btnSearchLostPets = (Button) view.findViewById(R.id.btnSearchLostPets);
        recycler = (RecyclerView) view.findViewById(R.id.my_recycler_view);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.action_search:
                showHideSearch();
                break;
        }

        return true;

    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.btnSearchLostPets:
                searchLostPets();
                break;
        }

    }

    private void searchLostPets() {

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Finding Me");
        progress.setMessage("Obteniendo Mascotas perdidas...");
        progress.show();

        llSearchLostPets.setVisibility(View.GONE);

        District district = (District) spnDistrict.getSelectedItem();
        PetType petType = (PetType) spnPetType.getSelectedItem();

        String parameters = String.format("?district_id=%d&pet_type_id=%d", district.getId(), petType.getId());

        JsonArrayRequest jsArrayRequest = new JsonArrayRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/lost_pets" + parameters, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Type listType = new TypeToken<ArrayList<LostPet>>() {}.getType();

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        List<LostPet> lstLostPet = gson.fromJson(response.toString(), listType);

                        if (lstLostPet.size() > 0)
                        {
                            LOGD(TAG, "Cantidad de LostPets " + lstLostPet.size());
                            lostPetAdapter.clearData();
                            lostPetAdapter.setItems(lstLostPet);
                            lostPetAdapter.notifyDataSetChanged();
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

    private void showHideSearch() {
        llSearchLostPets.setVisibility(llSearchLostPets.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
    }

}
