package com.imast.findingme.ui.fragments;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.LostPet;
import com.imast.findingme.model.PetType;
import com.imast.findingme.model.Race;
import com.imast.findingme.util.ValidationUtils;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class ReportPetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(ReportPetFragment.class);

    private TextView txvPetName, txvPetSex, txvPetType, txvPetRace, txvPetAge, txvPetVaccinated, txvPetInfo;
    private TextInputLayout tilLostPetInfo;
    private FloatingActionButton fabReportar;
    private NetworkImageView imgPetPhoto;

    public ReportPetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolBar.setTitle("Reportar - " + Config.pet.getName());

        String base_url_photo = "http://findmewebapp-eberttoribioupc.c9.io/system/pets/photos/000/000/";
        String folder = String.format("%03d/thumb", Config.pet.getId()) + "/";

        String final_url_photo = base_url_photo + folder + Config.pet.getPhoto_file_name();

        String petType = "";
        String petRace = "";

        for(PetType type : Config.lstPetType) {
            if (type.getId() == Config.pet.getPet_type_id()) {
                petType = type.getName();
                break;
            }
        }

        for(Race race : Config.lstRace) {
            if (race.getId() == Config.pet.getRace_id()) {
                petRace = race.getName();
                break;
            }
        }

        imgPetPhoto.setImageUrl(final_url_photo, VolleySingleton.getInstance(getActivity().getApplicationContext()).getImageLoader());
        imgPetPhoto.setDefaultImageResId(R.drawable.ic_my_pets);
        txvPetName.setText(Config.pet.getName());
        txvPetSex.setText(Config.pet.getSex());
        txvPetAge.setText(String.valueOf(Config.pet.getAge()));
        txvPetType.setText(petType);
        txvPetRace.setText(petRace);
        txvPetVaccinated.setText(Config.pet.getVaccinated());
        txvPetInfo.setText(Config.pet.getInformation());

        fabReportar.setOnClickListener(this);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_report_pet, container, false);

        txvPetName = (TextView) view.findViewById(R.id.txvPetName);
        txvPetSex = (TextView) view.findViewById(R.id.txvPetSex);
        txvPetType = (TextView) view.findViewById(R.id.txvPetType);
        txvPetRace = (TextView) view.findViewById(R.id.txvPetRace);
        txvPetAge = (TextView) view.findViewById(R.id.txvPetAge);
        txvPetVaccinated = (TextView) view.findViewById(R.id.txvPetVaccinated);
        txvPetInfo = (TextView) view.findViewById(R.id.txvPetInfo);
        tilLostPetInfo = (TextInputLayout) view.findViewById(R.id.tilLostPetInfo);
        fabReportar = (FloatingActionButton) view.findViewById(R.id.fabReportPet);
        imgPetPhoto = (NetworkImageView) view.findViewById(R.id.petPhoto);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fabReportPet:
                reportPet();
                break;
        }
    }

    private void reportPet() {

        if (!ValidationUtils.isEmpty(tilLostPetInfo)) {

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Finding Me");
            progress.setMessage("Reportando a " + Config.pet.getName() + "...");
            progress.show();

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonLosPet = new JSONObject();

            try {

                String infoLostPet = tilLostPetInfo.getEditText().getText().toString().trim();
                String report_date = new Date().toString();
                String lost_date = new Date().toString();
                double latitude = -12.086794;
                double longitude = -77.035828;
                String found_date = "";
                int petId = Config.pet.getId();
                int userId = Config.user.getId();
                int districtId = 1;
                String status = "P";

                jsonObject.put("info", infoLostPet);
                jsonObject.put("report_date", report_date);
                jsonObject.put("lost_date", lost_date);
                jsonObject.put("latitude", latitude);
                jsonObject.put("longitude", longitude);
                jsonObject.put("found_date", found_date);
                jsonObject.put("pet_id", petId);
                jsonObject.put("user_id", userId);
                jsonObject.put("district_id", districtId);
                jsonObject.put("status", status);

                jsonLosPet.put("lost_pet", jsonObject);

                JsonObjectRequest jsArrayRequest = new JsonObjectRequest
                        (Request.Method.POST, "http://findmewebapp-eberttoribioupc.c9.io/lost_pets", jsonLosPet, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                LOGD(TAG, "Response: " + response.toString());

                                Type listType = new TypeToken<ArrayList<LostPet>>() {}.getType();

                                Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                                LostPet lostPet = gson.fromJson(response.toString(), LostPet.class);

                                if (lostPet != null) {
                                    progress.dismiss();
                                    Toast.makeText(getActivity(), "Se reportó mascota", Toast.LENGTH_SHORT).show();

                                    Fragment fragment = new HomeFragment();
                                    FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                                    fragmentTransaction.replace(R.id.frame, fragment);
                                    fragmentTransaction.commit();

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

            } catch (JSONException e) {
                e.printStackTrace();
                progress.dismiss();
            } catch (Exception ex) {
                ex.printStackTrace();
                progress.dismiss();
            }

        }

    }
}
