package com.imast.findingme.ui.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.LostPet;
import com.imast.findingme.model.PetType;
import com.imast.findingme.model.Race;
import com.imast.findingme.ui.MapsActivity;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

public class LostPetInfoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(LostPetInfoFragment.class);

    private TextView txvPetName, txvPetSex, txvPetType, txvPetRace, txvPetAge, txvPetVaccinated, txvPetLostInfo, txvPetInfo;
    private ImageButton btnLastLocation;
    private FloatingActionButton fabAddSearch;
    private NetworkImageView imgPetPhoto;

    public LostPetInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolBar.setTitle(Config.lostPet.getPet().getName());

        fabAddSearch.setOnClickListener(this);
        btnLastLocation.setOnClickListener(this);

        String base_url_photo = "http://findmewebapp-eberttoribioupc.c9.io/system/pets/photos/000/000/";
        String folder = String.format("%03d/thumb", Config.lostPet.getPet_id()) + "/";

        String final_url_photo = base_url_photo + folder + Config.lostPet.getPet().getPhoto_file_name();

        imgPetPhoto.setImageUrl(final_url_photo, VolleySingleton.getInstance(getActivity().getApplicationContext()).getImageLoader());
        imgPetPhoto.setDefaultImageResId(R.drawable.ic_my_pets);

        String petType = "";
        String petRace = "";

        for(PetType type : Config.lstPetType) {
            if (type.getId() == Config.lostPet.getPet().getPet_type_id()) {
                petType = type.getName();
                break;
            }
        }

        for(Race race : Config.lstRace) {
            if (race.getId() == Config.lostPet.getPet().getRace_id()) {
                petRace = race.getName();
                break;
            }
        }

        txvPetName.setText(Config.lostPet.getPet().getName());
        txvPetSex.setText(Config.lostPet.getPet().getSex());
        txvPetAge.setText(String.valueOf(Config.lostPet.getPet().getAge()));
        txvPetType.setText(petType);
        txvPetRace.setText(petRace);
        txvPetVaccinated.setText(Config.lostPet.getPet().getVaccinated());
        txvPetInfo.setText(Config.lostPet.getPet().getInformation());
        txvPetLostInfo.setText(Config.lostPet.getInfo());


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lost_pet_info, container, false);

        txvPetName = (TextView) view.findViewById(R.id.txvPetName);
        txvPetSex = (TextView) view.findViewById(R.id.txvPetSex);
        txvPetType = (TextView) view.findViewById(R.id.txvPetType);
        txvPetRace = (TextView) view.findViewById(R.id.txvPetRace);
        txvPetAge = (TextView) view.findViewById(R.id.txvPetAge);
        txvPetVaccinated = (TextView) view.findViewById(R.id.txvPetVaccinated);
        txvPetLostInfo = (TextView) view.findViewById(R.id.txvPetLostInfo);
        txvPetInfo = (TextView) view.findViewById(R.id.txvPetInfo);
        btnLastLocation = (ImageButton) view.findViewById(R.id.btnlastLocation);
        fabAddSearch = (FloatingActionButton) view.findViewById(R.id.fabAddSearch);
        imgPetPhoto = (NetworkImageView) view.findViewById(R.id.petPhoto);
        return view;
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()) {
            case R.id.fabAddSearch:
                addToMySearch();
                break;
            case  R.id.btnlastLocation:
                goToMapLastLocation();
                break;
        }

    }

    private void addToMySearch() {

        final ProgressDialog progress = new ProgressDialog(getActivity());
        progress.setTitle("Finding Me");
        progress.setMessage("Agregando a Mis Búsquedas...");
        progress.show();

        String parameters = String.format("%d?user_id=%d", Config.lostPet.getId(), Config.user.getId());

        JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                (Request.Method.GET, "http://findmewebapp-eberttoribioupc.c9.io/add_lostpet/" + parameters, null, new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        LOGD(TAG, "Response: " + response.toString());

                        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                        LostPet lostPet = gson.fromJson(response.toString(), LostPet.class);

                        if (lostPet != null)
                        {
                            Toast.makeText(getActivity(), "Se agregó a Mis Búsquedas...", Toast.LENGTH_SHORT).show();
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

    private void goToMapLastLocation() {

        Intent intent = new Intent(getActivity(), MapsActivity.class);
        startActivity(intent);

    }

}
