package com.imast.findingme.ui.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.Pet;
import com.imast.findingme.model.PetType;
import com.imast.findingme.model.Race;
import com.imast.findingme.util.ValidationUtils;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class AddPetFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(AddPetFragment.class);

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imgPetPhoto;
    private TextInputLayout tilPetName, tilPetAge, tilPetInformation;
    private RadioGroup rbgPetSex, rbgPetVaccionated;
    private RadioButton rbtnPetSex, rbtnPetVaccionated;
    private Spinner spnPetType, spnPetRace;
    private FloatingActionButton fabSave;

    public AddPetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle("Registrar Mi Mascota");

        fabSave.setOnClickListener(this);
        imgPetPhoto.setOnClickListener(this);

        ArrayAdapter<PetType> petTypeAdapter = new ArrayAdapter<PetType>(getActivity(), android.R.layout.simple_spinner_item, Config.lstPetType);
        spnPetType.setAdapter(petTypeAdapter);

        ArrayAdapter<Race> raceAdapter = new ArrayAdapter<Race>(getActivity(), android.R.layout.simple_spinner_item, Config.lstRace);
        spnPetRace.setAdapter(raceAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);
        imgPetPhoto = (ImageView) view.findViewById(R.id.imgPetPhoto);
        tilPetName = (TextInputLayout) view.findViewById(R.id.tilPetName);
        tilPetAge = (TextInputLayout) view.findViewById(R.id.tilPetAge);
        tilPetInformation = (TextInputLayout) view.findViewById(R.id.tilPetInformation);
        rbgPetSex = (RadioGroup) view.findViewById(R.id.rbgSex);
        rbgPetVaccionated = (RadioGroup) view.findViewById(R.id.rbgPetVaccionated);
        spnPetType = (Spinner) view.findViewById(R.id.spnTipo);
        spnPetRace = (Spinner) view.findViewById(R.id.spnRaza);
        fabSave = (FloatingActionButton) view.findViewById(R.id.fabSavePet);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgPetPhoto:
                getPhoto();
                break;
            case R.id.fabSavePet:
                savePet();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE) {

            if (data != null) {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                imgPetPhoto.setImageBitmap(imageBitmap);
            }
        }

    }

    private void getPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void savePet() {

        if (!ValidationUtils.isEmpty(tilPetName, tilPetAge)) {

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Finding Me");
            progress.setMessage("Grabando Mascota...");
            progress.show();

            /*BitmapDrawable drawable = (BitmapDrawable) imgPetPhoto.getDrawable();
            Bitmap bitmap = drawable.getBitmap();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 75, bos);
            byte[] bb = bos.toByteArray();
            String petPhoto64 = Base64.encodeToString(bb, 0);*/

            int sexSelectedId = rbgPetSex.getCheckedRadioButtonId();
            int vaccionatedSelectedId = rbgPetVaccionated.getCheckedRadioButtonId();

            rbtnPetSex = (RadioButton) getActivity().findViewById(sexSelectedId);
            rbtnPetVaccionated = (RadioButton) getActivity().findViewById(vaccionatedSelectedId);

            String petName = tilPetName.getEditText().getText().toString().trim();
            String petAge = tilPetAge.getEditText().getText().toString().trim();
            String petInformation = tilPetInformation.getEditText().getText().toString().trim();
            String petSex = rbtnPetSex.getText().toString().trim();
            String petVaccinated = rbtnPetVaccionated.getText().toString().trim();
            int petTypeId = ((PetType) spnPetType.getSelectedItem()).getId();
            int petRaceId = ((Race) spnPetRace.getSelectedItem()).getId();

            JSONObject jsonObject = new JSONObject();
            JSONObject jsonPet = new JSONObject();
            try {

                jsonObject.put("name", petName);
                jsonObject.put("sex", petSex);
                jsonObject.put("race_id", petRaceId);
                jsonObject.put("age", petAge);
                jsonObject.put("vaccinated", petVaccinated);
                jsonObject.put("information", petInformation);
                jsonObject.put("state", "1");
                jsonObject.put("pet_type_id", petTypeId);
                jsonObject.put("user_id", Config.user.getId());
                //jsonObject.put("", petPhoto64);
                //jsonObject.put("photo", "missing.png");

                jsonPet.put("pet", jsonObject);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.POST, "http://findmewebapp-eberttoribioupc.c9.io/users/" + Config.user.getId() + "/pets", jsonPet, new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) {
                            LOGD(TAG, "Response: " + response.toString());

                            //Type listType = new TypeToken<ArrayList<User>>() {}.getType();

                            Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                            Pet pet = gson.fromJson(response.toString(), Pet.class);

                            if (pet != null)
                            {
                                progress.dismiss();
                                Toast.makeText(getActivity().getApplicationContext(), "Se Grabó Mascota", Toast.LENGTH_SHORT).show();
                            }

                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progress.dismiss();
                            Toast.makeText(getActivity().getApplicationContext(), "Error de Conexión con el Servidor", Toast.LENGTH_SHORT).show();
                            LOGD(TAG, "Error Volley:"+ error.getMessage());
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

            VolleySingleton.getInstance(getActivity().getApplicationContext()).addToRequestQueue(jsObjRequest);

        }

    }
}
