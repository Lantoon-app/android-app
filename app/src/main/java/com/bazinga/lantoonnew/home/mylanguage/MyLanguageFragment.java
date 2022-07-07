package com.bazinga.lantoonnew.home.mylanguage;

import static com.bazinga.lantoonnew.home.HomeActivity.setToolbar;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.NetworkUtil;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoonnew.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyLanguageFragment extends Fragment {

    private MyLanguageViewModel myLanguageViewModel;
    private GridView listView;
    List<MyLanguageData> myLanguageDataList;
    MyLanguagesAdapter adapter;
    boolean fragmentDestroyed = false;
    ProgressBar progressBar;
    EditText et_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myLanguageViewModel = new ViewModelProvider(this).get(MyLanguageViewModel.class);
        View root = inflater.inflate(R.layout.fragment_my_language, container, false);
        setToolbar(false, getString(R.string.my_languages));
        listView = root.findViewById(R.id.llView);
        et_search = root.findViewById(R.id.et_search);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //listView.setDivider(null);
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            progressBar.setVisibility(View.VISIBLE);
            myLanguageViewModel.getLanguageMutableLiveData().observe(getActivity(), new Observer<List<MyLanguageData>>() {
                @Override
                public void onChanged(List<MyLanguageData> languages) {
                    progressBar.setVisibility(View.GONE);
                    Log.d("response body= ", new GsonBuilder().setPrettyPrinting().create().toJson(languages));
                    if (!fragmentDestroyed) {
                        myLanguageDataList = languages;
                        adapter = new MyLanguagesAdapter(getContext(), myLanguageDataList, HomeActivity.sessionManager.getLearnLangId());
                        listView.setAdapter(adapter);

                    }
                }
            });
        } else {
            CommonFunction.netWorkErrorAlert(getActivity());
        }
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.d("charSequence",charSequence.toString());
                adapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MyLanguageData myLanguageData = (MyLanguageData) parent.getItemAtPosition(position);
                Log.d("selected ", myLanguageData.getLearnLanguage().getImagePath());
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0)
                    updateLanguage(HomeActivity.sessionManager.getUid(), myLanguageData.getLearnLanguage().getLanguageID(), HomeActivity.sessionManager.getKnownLangId());
                else CommonFunction.netWorkErrorAlert(getActivity());
            }
        });

        return root;
    }

    private void updateLanguage(String uid, String learnlanguageID, int knownLanguageID) {
        Log.d("updateLanguage uid",uid);
        Log.d("updateLanguage learnlanguageID",learnlanguageID);
        Log.d("updateLanguage knownLanguageID",String.valueOf(knownLanguageID));
        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
        Call<LoggedInUserResponse> call = apiInterface.updateLanguage(uid, learnlanguageID, knownLanguageID);
        call.enqueue(new Callback<LoggedInUserResponse>() {
            @Override
            public void onResponse(Call<LoggedInUserResponse> call, Response<LoggedInUserResponse> response) {

                if (response != null) {
                    Log.d("response updateLanguage", new GsonBuilder().setPrettyPrinting().create().toJson(response.body()));

                    if (response.body().getStatus().getCode() == 1035) {
                        HomeActivity.sessionManager.setSpeakCode(response.body().getLoginData().getSpeakCode());
                        HomeActivity.sessionManager.setLearnLangId(response.body().getLoginData().getLearnlangId());
                        HomeActivity.sessionManager.setLearnLangName(response.body().getLoginData().getLearnlangObj().getLanguageName());
                        HomeActivity.sessionManager.setLearnLangNativeName(response.body().getLoginData().getLearnlangObj().getNativeName());

                        HomeActivity.sessionManager.setKnownLangId(response.body().getLoginData().getKnownlangId());
                        HomeActivity.sessionManager.setKnownLangName(response.body().getLoginData().getKnownlangObj().getLanguageName());
                        HomeActivity.sessionManager.setKnownLangNativeName(response.body().getLoginData().getKnownlangObj().getNativeName());
                        Toast.makeText(getActivity(), "Language updated successfully", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(getView()).navigate(R.id.bottom_lesson);
                        HomeActivity.setToolbar(true,null);
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