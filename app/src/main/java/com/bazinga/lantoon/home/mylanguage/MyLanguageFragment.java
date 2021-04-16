package com.bazinga.lantoon.home.mylanguage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLanguageFragment extends Fragment {

    private MyLanguageViewModel myLanguageViewModel;
    private ListView listView;
    List<MyLanguageData> myLanguageDataList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myLanguageViewModel = new ViewModelProvider(this).get(MyLanguageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_language, container, false);
        listView = root.findViewById(R.id.llView);

        myLanguageViewModel.getLanguageMutableLiveData().observe(getActivity(), new Observer<List<MyLanguageData>>() {
            @Override
            public void onChanged(List<MyLanguageData> languages) {
                Log.d("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(languages));
                myLanguageDataList = languages;
                MyLanguagesAdapter adapter = new MyLanguagesAdapter(getContext(), myLanguageDataList);


                listView.setAdapter(adapter);
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("selected ", myLanguageDataList.get(position).getLearnLanguage().getImagePath());
                updateLanguage(HomeActivity.sessionManager.getUserDetails().getUid(), myLanguageDataList.get(position).getLearnLanguage().getLanguageID(), myLanguageDataList.get(position).getKnownLanguage().getLanguageID());
            }
        });

        return root;
    }

    private void updateLanguage(String uid, String learnlanguageID, String knownLanguageID) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInUserResponse> call = apiInterface.updateLanguage(uid, learnlanguageID, knownLanguageID);
        call.enqueue(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {

                if (response != null) {
                    Log.d("response updateLanguage", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                    if (response.body().getStatus().getCode() == 1035) {
                        String user = new GsonBuilder().create().toJson(response.body().getLoginData());
                        SessionManager sessionManager = new SessionManager(getContext());
                        sessionManager.createLoginSession(user);
                        Toast.makeText(getActivity(),"Language updated successfully",Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.bottom_lesson);
                    }
                }
            }

            @Override
            public void onFailure(Call<LoggedInUserResponse> call, Throwable t) {
                Log.e("response updateLanguage", t.getMessage());
            }
        });

    }
}