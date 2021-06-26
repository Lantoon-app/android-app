package com.bazinga.lantoon.home.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.registration.model.DurationData;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;


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
    private static final int MY_PERMISSION_REQUEST_CODE = 111;

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
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            profileViewModel.getUser().observe(getActivity(), profile -> {
                if (!fragmentDestroyed) {
                    if (profile.getStatus().getCode() == 1023) {
                        profileData = profile.getProfileData();
                        Log.d("profileData ", new GsonBuilder().setPrettyPrinting().create().toJson(profileData));
                        if (!profileData.getPicture().equals("") || profileData.getPicture() != null) {

                            Glide.with(this).load(profileData.getPicture())
                                    .circleCrop().placeholder(R.drawable.photo_upload_icon)
                                    .addListener(new RequestListener<Drawable>() {
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
        } else
            CommonFunction.netWorkErrorAlert(getActivity());

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel( year,  monthOfYear+1,
                 dayOfMonth);
            }

        };
        ivProfilePhoto.setOnClickListener(this::onClick);
        etDOB.setOnClickListener(this::onClick);
        btnUpdate.setOnClickListener(this::onClick);


        return root;
    }
    private void updateLabel(int year, int monthOfYear,
                             int dayOfMonth) {
        String myFormat = "YYYY-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat);
        //etDOB.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
        etDOB.setText(sdf.format(myCalendar.getTime()));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivProfilePhoto:
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0){
                    if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions( //Method of Fragment
                                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},MY_PERMISSION_REQUEST_CODE
                        );
                        Toast.makeText(getContext(),"Need Storage Permission",Toast.LENGTH_SHORT).show();
                    } else {
                        selectImage();
                    }
                }
                else
                    CommonFunction.netWorkErrorAlert(getActivity());
                break;
            case R.id.etDOB:
                new DatePickerDialog(getContext(), date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnUpdate:
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
                    profileData.setUname(etFullName.getText().toString());
                    profileData.setDob(etDOB.getText().toString());
                    profileData.setCountrycode(countryCodePicker.getSelectedCountryCode());
                    profileData.setPhone(etPhoneNumber.getText().toString());
                    profileData.setMindurationperday(Integer.valueOf(durationDataList.get(spinnerDuration.getSelectedItemPosition()).getId()));
                    profileViewModel.postProfileData(profileData);
                } else
                    CommonFunction.netWorkErrorAlert(getActivity());

                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == MY_PERMISSION_REQUEST_CODE) {
            if (permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                selectImage();
            }
        }
    }
    private void selectImage() {
        //Create a View object yourself through inflater
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(getContext().LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_select_image, null);

        //Specify the length and width through constants
        int width = LinearLayout.LayoutParams.MATCH_PARENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;

        //Make Inactive Items Outside Of PopupWindow
        boolean focusable = false;

        //Create a window with our parameters
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        //Set the location of the window on the screen
        popupWindow.showAtLocation(getView(), Gravity.CENTER, 0, 0);

        //Initialize the elements of our window, install the handler


        ImageView ivCamera = popupView.findViewById(R.id.ivCamera);
        ImageView ivGallery = popupView.findViewById(R.id.ivGallery);
        ImageButton imgBtnClose = popupView.findViewById(R.id.imgBtnClose);

        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, 11);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        ivGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 22);
            }
        });

        imgBtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupWindow.dismiss();
            }
        });


        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                popupWindow.dismiss();
                return true;
            }
        });

    }

    @SuppressLint("LongLogTag")
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == 11) {
                Bitmap photo = (Bitmap) data.getExtras().get("data");
                // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
                Uri tempUri = getImageUri(getApplicationContext(), photo);
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(tempUri, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0)
                    SendDetail(picturePath);
                else CommonFunction.netWorkErrorAlert(getActivity());
                c.close();


            } else if (requestCode == 22) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0)
                    SendDetail(picturePath);
                else CommonFunction.netWorkErrorAlert(getActivity());
                c.close();

            }
        }
    }



    public Uri getImageUri(Context inContext, Bitmap inImage) {

        Bitmap OutImage = Bitmap.createScaledBitmap(inImage, 1000, 1000, true);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), OutImage, "Title", null);
        return Uri.parse(path);
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
                    Glide.with(getContext())
                            .load(response.body().getData().getProfilepic())
                            .circleCrop()
                            .into(ivProfilePhoto);
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