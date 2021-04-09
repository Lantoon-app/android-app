package com.bazinga.lantoon.home.profile;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.login.data.model.LoggedInUser;
import com.bazinga.lantoon.registration.model.DurationData;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class ProfileFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private ProfileViewModel profileViewModel;
    private ProfileData profileData;
    ProfilePictureData profilePictureData;
    private List<DurationData> durationDataList;
    EditText etFullName, etDOB, etPhoneNumber;
    CountryCodePicker countryCodePicker;
    DatePickerDialog.OnDateSetListener date;
    Button btnUpdate;
    ImageView ivProfilePhoto;
    Spinner spinnerDuration;
    final Calendar myCalendar = Calendar.getInstance();

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
            if (profile.getStatus().getCode() == 1023) {
                profileData = profile.getProfileData();
                if (!profileData.getPicture().equals("")) {
                    byte[] decodedString = Base64.decode(profileData.getPicture(), Base64.DEFAULT);
                    Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                    RoundedBitmapDrawable dr =
                            RoundedBitmapDrawableFactory.create(getContext().getResources(), decodedByte);
                    dr.setGravity(Gravity.CENTER);
                    dr.setCircular(true);
                    ivProfilePhoto.setBackground(null);
                    ivProfilePhoto.setImageDrawable(dr);

                }
                durationDataList = profile.getDurationData();
                System.out.println(profile.getDurationData().toString());
                DurationSpinnerAdapter durationSpinnerAdapter = new DurationSpinnerAdapter(getContext(), durationDataList);
                spinnerDuration.setAdapter(durationSpinnerAdapter);
                etFullName.setText(profileData.getUname());
                etDOB.setText(profileData.getDob());
                if (!profileData.getCountrycode().equals("")) {
                    countryCodePicker.setCountryForPhoneCode(Integer.valueOf(profileData.getCountrycode()));
                    etPhoneNumber.setText(profileData.getPhone());
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
                selectImage();
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
                profileData.setMindurationperday(Integer.valueOf(durationDataList.get(spinnerDuration.getSelectedItemPosition()).getDurationMin()));
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
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(getContext().getCacheDir(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
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
                File f = new File(getContext().getCacheDir().getPath());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);
                    bitmap = getResizedBitmap(bitmap, 400);
                    RoundedBitmapDrawable dr =
                            RoundedBitmapDrawableFactory.create(getContext().getResources(), bitmap);
                    ivProfilePhoto.setBackground(null);
                    ivProfilePhoto.setImageDrawable(dr);
                    String strBase64 = BitMapToString(bitmap);
                    SendDetail(strBase64);
                    String path = getContext().getCacheDir().getPath()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {
                Uri selectedImage = data.getData();
                String[] filePath = {MediaStore.Images.Media.DATA};
                Cursor c = getContext().getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
                //thumbnail = getResizedBitmap(thumbnail, 500);
                Log.w("path of image from gallery......******************.........", picturePath + "");
                RoundedBitmapDrawable dr =
                        RoundedBitmapDrawableFactory.create(getContext().getResources(), thumbnail);
                dr.setGravity(Gravity.CENTER);
                dr.setCircular(true);
                ivProfilePhoto.setBackground(null);
                ivProfilePhoto.setImageDrawable(dr);
                SendDetail(BitMapToString(thumbnail));
            }
        }
    }

    public String BitMapToString(Bitmap userImage1) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        userImage1.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String Document_img1 = Base64.encodeToString(b, Base64.DEFAULT);
        return Document_img1;
    }

    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    private void SendDetail(String strPicture) {
        System.out.println(strPicture);
        profilePictureData = new ProfilePictureData();
        profilePictureData.setProfilepic(strPicture);
        profilePictureData.setUserid(HomeActivity.sessionManager.getUserDetails().getUid());
        final ProgressDialog loading = new ProgressDialog(getContext());
        loading.setMessage("Please Wait...");
        loading.show();
        loading.setCanceledOnTouchOutside(false);
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<ProfilePicture> call = apiInterface.updateProfilePicture(profilePictureData);
        call.enqueue(new Callback<ProfilePicture>() {
            @Override
            public void onResponse(Call<ProfilePicture> call, Response<ProfilePicture> response) {
                if (response.body().getStatus().getCode() == 1028)
                    Log.d("profile picture data ", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));
                loading.dismiss();
            }

            @Override
            public void onFailure(Call<ProfilePicture> call, Throwable t) {
                loading.dismiss();
            }
        });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        /*for(int i=0; i<durationDataList.size(); i++){
            if(profileData.getMindurationperday() == Integer.valueOf(durationDataList.get(i).getDurationMin()))
                parent.setSelection(i);
        }*/
        //parent.setSelection(2);
    }
}