package com.imast.findingme.ui.fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

import com.imast.findingme.Config;
import com.imast.findingme.R;
import com.imast.findingme.model.District;
import com.imast.findingme.model.Profile;
import com.imast.findingme.util.MyUtils;
import com.imast.findingme.util.ValidationUtils;
import com.imast.findingme.util.VolleySingleton;

import static com.imast.findingme.util.LogUtils.LOGD;
import static com.imast.findingme.util.LogUtils.makeLogTag;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyInfoFragment extends Fragment implements View.OnClickListener {

    private static final String TAG = makeLogTag(MyInfoFragment.class);

    static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView imgOwnerPhoto;
    TextView txvEmail;
    private TextInputLayout tilFullname, tilSurname, tilAddress;
    private RadioGroup rbgSex;
    private Spinner spnDistrict;
    private FloatingActionButton fabSaveMyInfo;

    public MyInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imgOwnerPhoto.setOnClickListener(this);
        fabSaveMyInfo.setOnClickListener(this);

        ArrayAdapter<District> districtAdapter = new ArrayAdapter<District>(getActivity(), android.R.layout.simple_spinner_item, Config.lstDistrict);
        //districtAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnDistrict.setAdapter(districtAdapter);

        setProfile();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_info, container, false);
        spnDistrict = (Spinner) view.findViewById(R.id.spnDistrict);
        imgOwnerPhoto = (ImageView) view.findViewById(R.id.ownerPhoto);
        txvEmail = (TextView) view.findViewById(R.id.txvEmail);
        tilFullname = (TextInputLayout) view.findViewById(R.id.tilFullname);
        tilSurname = (TextInputLayout) view.findViewById(R.id.tilSurname);
        tilAddress = (TextInputLayout) view.findViewById(R.id.tilAddress);
        rbgSex = (RadioGroup) view.findViewById(R.id.rbgSexo);
        fabSaveMyInfo = (FloatingActionButton) view.findViewById(R.id.fabSaveMyInfo);
        return view;
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.fabSaveMyInfo:
                updateProfile();
                break;
            case R.id.ownerPhoto:
                getPhoto();
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
                imgOwnerPhoto.setImageBitmap(imageBitmap);
            }
        }

    }

    private void getPhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void setProfile() {

        if (Config.profile != null) {

            txvEmail.setText(Config.user.getEmail());
            tilFullname.getEditText().setText(Config.profile.getName());
            tilSurname.getEditText().setText(Config.profile.getLastname());
            tilAddress.getEditText().setText(Config.profile.getAddress());

            MyUtils.SelectSpinnerItemByValue(spnDistrict, Config.profile.getId());

            int sexId = Config.profile.getSex().equals("M") ? R.id.rbtnSexoM : R.id.rbtnSexoF;
            rbgSex.check(sexId);
        }

    }

    private void updateProfile() {

        if (!ValidationUtils.isEmpty(tilFullname, tilSurname, tilAddress)) {

            final ProgressDialog progress = new ProgressDialog(getActivity());
            progress.setTitle("Finding Me");
            progress.setMessage("Actualizar Mi Información...");
            progress.show();

            int sexSelectedId = rbgSex.getCheckedRadioButtonId();
            RadioButton rbtnSexo = (RadioButton) getActivity().findViewById(sexSelectedId);

            District district = (District) spnDistrict.getSelectedItem();

            String name = tilFullname.getEditText().getText().toString().trim();
            String lastname = tilSurname.getEditText().getText().toString().trim();
            String address = tilAddress.getEditText().getText().toString().trim();
            String sex = rbtnSexo.getText().toString().trim();
            int districId = district.getId();


            JSONObject jsonObject = new JSONObject();
            JSONObject jsonMyInfo = new JSONObject();

            try {

                jsonObject.put("name", name);
                jsonObject.put("lastname", lastname);
                jsonObject.put("address", address);
                jsonObject.put("sex", sex);
                jsonObject.put("user_id", Config.user.getId());
                jsonObject.put("district_id", districId);

                jsonMyInfo.put("profile", jsonObject);

                JsonObjectRequest jsObjRequest = new JsonObjectRequest
                        (Request.Method.PUT, "http://findmewebapp-eberttoribioupc.c9.io/users/" + Config.user.getId() + "/profiles", jsonMyInfo, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                LOGD(TAG, "Response: " + response.toString());

                                try {
                                    Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

                                    Profile profile = gson.fromJson(response.toString(), Profile.class);

                                    if (profile != null)
                                    {
                                        Config.profile = profile;
                                        progress.dismiss();
                                        Toast.makeText(getActivity().getApplicationContext(), "Se Actualizó Mi Información", Toast.LENGTH_SHORT).show();
                                    }
                                } catch (Exception ex) {
                                    progress.dismiss();
                                    ex.printStackTrace();
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

            } catch (JSONException e) {
                progress.dismiss();
                e.printStackTrace();
            } catch (Exception e) {
                progress.dismiss();
                e.printStackTrace();
            }

        }

    }
}
