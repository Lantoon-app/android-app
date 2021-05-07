package com.bazinga.lantoon.home.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.registration.model.DurationData;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.io.File;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ProfileViewModel profileViewModel;
    private ProfileData profileData;
    private List<DurationData> durationDataList;
    EditText etFullName, etDOB, etPhoneNumber;
    CountryCodePicker countryCodePicker;
    DatePickerDialog.OnDateSetListener date;
    Button btnUpdate;
    ImageView ivProfilePhoto;
    Spinner spinnerDuration;
    final Calendar myCalendar = Calendar.getInstance();
    boolean fragmentDestroyed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        ivProfilePhoto = root.findViewById(R.id.ivProfilePhoto);
        etFullName = root.findViewById(R.id.etFullName);
        etDOB = root.findViewById(R.id.etDOB);
        etPhoneNumber = root.findViewById(R.id.etPhoneNumber);
        countryCodePicker = (CountryCodePicker) root.findViewById(R.id.countryCodePicker);
        spinnerDuration = root.findViewById(R.id.spinnerDuration);
        btnUpdate = root.findViewById(R.id.btnUpdate);
        spinnerDuration.setOnItemSelectedListener(this);

        profileViewModel.getUser().observe(getActivity(), profile -> {
            if (!fragmentDestroyed) {
                if (profile.getStatus().getCode() == 1023) {
                    profileData = profile.getProfileData();
                    Log.d("profileData ", new GsonBuilder().setPrettyPrinting().create().toJson(profileData));
                    if (!profileData.getPicture().equals("") || profileData.getPicture() != null) {

                        Glide.with(this).load(profileData.getPicture()).circleCrop().addListener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable @org.jetbrains.annotations.Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                ivProfilePhoto.setBackground(null);
                                return false;
                            }
                        }).into(ivProfilePhoto);

                    }
                    durationDataList = profile.getDurationData();
                    System.out.println(profile.getDurationData().toString());
                    DurationSpinnerAdapter durationSpinnerAdapter = new DurationSpinnerAdapter(getContext(), durationDataList);
                    spinnerDuration.setAdapter(durationSpinnerAdapter);
                    spinnerDuration.setSelection(profileData.getMindurationperday() - 1);
                    etFullName.setText(profileData.getUname());
                    etDOB.setText(profileData.getDob());
                    if (!profileData.getCountrycode().equals("")) {
                        countryCodePicker.setCountryForPhoneCode(Integer.valueOf(profileData.getCountrycode()));
                        etPhoneNumber.setText(profileData.getPhone());
                    }
                }
                if (profile.getStatus().getCode() == 1025) {
                    Snackbar.make(getView(), profile.getStatus().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        date = (view, year, monthOfYear, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            etDOB.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        };
        ivProfilePhoto.setOnClickListener(this::onClick);
        etDOB.setOnClickListener(this::onClick);
        btnUpdate.setOnClickListener(this::onClick);


        return root;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfilePhoto:
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0)
                    selectImage();
                else
                    Snackbar.make(getView(), getString(R.string.msg_network_failed), Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.etDOB:
                new DatePickerDialog(getContext(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnUpdate:
                profileData.setUname(etFullName.getText().toString());
                profileData.setDob(etDOB.getText().toString());
                profileData.setCountrycode(countryCodePicker.getSelectedCountryCode());
                profileData.setPhone(etPhoneNumber.getText().toString());
                profileData.setMindurationperday(Integer.valueOf(durationDataList.get(spinnerDuration.getSelectedItemPosition()).getId()));
                profileViewModel.postProfileData(profileData);
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] options = {"Take Photo", "Choose from Gallery", "Cancel"};
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {

                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        startActivityForResult(takePictureIntent, 1);
                    } catch (ActivityNotFoundException e) {
                        // display error state to the user
                    }
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);
                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                SendDetail(picturePath);
                c.close();


            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                SendDetail(picturePath);
                c.close();

            }
        }
    }


    private void SendDetail(String strPicture) {
        System.out.println(strPicture);
        File imageFile = new File(strPicture);
        RequestBody reqBody = RequestBody.create(MediaType.parse("multipart/form-file"), imageFile);
        MultipartBody.Part partImage = MultipartBody.Part.createFormData("profilepic", imageFile.getName(), reqBody);
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), HomeActivity.sessionManager.getUid());
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setMessage("Please Wait...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfilePicture> call = apiInterface.updateProfilePicture(userid, partImage);
        call.enqueue(new Callback<ProfilePicture>() {
            @Override
            public void onResponse(Call<ProfilePicture> call, Response<ProfilePicture> response) {
                if (response.body().getStatus().getCode() == 1028) {
                    ivProfilePhoto.setBackground(null);
                    Glide.with(getContext()).load(response.body().getData().getProfilepic()).circleCrop().into(ivProfilePhoto);
                    Log.d("profile picture data ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                    HomeActivity.sessionManager.setProfilePic(response.body().getData().getProfilepic());
                    Snackbar.make(getView(), response.body().getStatus().getMessage(), Snackbar.LENGTH_SHORT).show();
                }
                Log.d("profile picture data ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ProfilePicture> call, Throwable t) {
                Log.d("profile picture error ", t.getMessage());
                Snackbar.make(getView(), t.getMessage(), Snackbar.LENGTH_LONG).show();
                loading.dismiss();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }
}