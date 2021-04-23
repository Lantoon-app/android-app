package com.bazinga.lantoon.home.changereferlanguage;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeReferenceLanguageFragment extends Fragment {


    ChangeReferenceLanguageViewModel changeReferenceLanguageViewModel;
    ListView listView;
    List<Language> LanguageDataList;
    boolean fragmentDestroyed = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_refer_language, container, false);
        listView = root.findViewById(R.id.llView);
        listView.setDivider(null);
        changeReferenceLanguageViewModel = new ViewModelProvider(this).get(ChangeReferenceLanguageViewModel.class);
        changeReferenceLanguageViewModel.getLanguageMutableLiveData().observe(getActivity(), new Observer<List<Language>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Language> languages) {
                if (!fragmentDestroyed) {
                    LanguageDataList = languages;
                    LanguageDataList.removeIf(s -> s.getLanguageID().equalsIgnoreCase(String.valueOf(HomeActivity.sessionManager.getLearLang())));

                    ChangeReferenceLanguageAdapter changeReferenceLanguageAdapter = new ChangeReferenceLanguageAdapter(getContext(), LanguageDataList);
                    listView.setAdapter(changeReferenceLanguageAdapter);
                }
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                updateReferenceLanguage(HomeActivity.sessionManager.getUid(), HomeActivity.sessionManager.getLearLang(), LanguageDataList.get(position).getLanguageID());
            }
        });


        return root;
    }

    private void updateReferenceLanguage(String uid, int learnlanguageID, String knownLanguageID) {
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInUserResponse> call = apiInterface.updateReferLanguage(uid, learnlanguageID, knownLanguageID);
        call.enqueue(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {

                if (response != null) {
                    Log.d("response updateLanguage", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                    if (response.body().getStatus().getCode() == 1047) {
                        HomeActivity.sessionManager.setKnownLang(response.body().getLoginData().getKnownlang());
                        Toast.makeText(getActivity(), "Reference Language updated successfully", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        fragmentDestroyed = true;
    }
}