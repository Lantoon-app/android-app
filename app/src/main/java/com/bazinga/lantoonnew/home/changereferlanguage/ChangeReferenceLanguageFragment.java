package com.bazinga.lantoonnew.home.changereferlanguage;

import static com.bazinga.lantoonnew.home.HomeActivity.setToolbar;

import android.os.Build;
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
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.bazinga.lantoonnew.CommonFunction;
import com.bazinga.lantoonnew.NetworkUtil;
import com.bazinga.lantoonnew.R;
import com.bazinga.lantoonnew.home.HomeActivity;
import com.bazinga.lantoonnew.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoonnew.registration.langselection.model.Language;
import com.bazinga.lantoonnew.retrofit.ApiClient;
import com.bazinga.lantoonnew.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeReferenceLanguageFragment extends Fragment {


    ChangeReferenceLanguageViewModel changeReferenceLanguageViewModel;
    GridView listView;
    List<Language> LanguageDataList;
    boolean fragmentDestroyed = false;
    ChangeReferenceLanguageAdapter changeReferenceLanguageAdapter;
    ProgressBar progressBar;
    EditText et_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_refer_language, container, false);
        setToolbar(true, getString(R.string.support_language));
        listView = root.findViewById(R.id.llView);
        et_search = root.findViewById(R.id.et_search);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        //listView.setDivider(null);
        changeReferenceLanguageViewModel = new ViewModelProvider(this).get(ChangeReferenceLanguageViewModel.class);

        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            progressBar.setVisibility(View.VISIBLE);
            changeReferenceLanguageViewModel.getLanguageMutableLiveData().observe(getActivity(), new Observer<List<Language>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(List<Language> languages) {
                    progressBar.setVisibility(View.GONE);
                    if (!fragmentDestroyed) {
                        LanguageDataList = languages;
                        LanguageDataList.removeIf(s -> s.getLanguageID().equalsIgnoreCase(String.valueOf(HomeActivity.sessionManager.getLearnLangId())));

                        changeReferenceLanguageAdapter = new ChangeReferenceLanguageAdapter(getContext(), LanguageDataList,HomeActivity.sessionManager.getKnownLangId());
                        listView.setAdapter(changeReferenceLanguageAdapter);
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
                changeReferenceLanguageAdapter.getFilter().filter(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Language language = (Language) parent.getItemAtPosition(position);
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
                    updateReferenceLanguage(HomeActivity.sessionManager.getUid(), HomeActivity.sessionManager.getLearnLangId(), language.getLanguageID());
                } else {
                    CommonFunction.netWorkErrorAlert(getActivity());
                }

            }
        });
        listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

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
                        /*HomeActivity.sessionManager.setLearnLangId(response.body().getLoginData().getLearnlangId());
                        HomeActivity.sessionManager.setLearnLangName(response.body().getLoginData().getLearnlangObj().getLanguageName());
                        HomeActivity.sessionManager.setLearnLangNativeName(response.body().getLoginData().getLearnlangObj().getNativeName());*/

                        HomeActivity.sessionManager.setKnownLangId(response.body().getLoginData().getKnownlangId());
                        HomeActivity.sessionManager.setKnownLangName(response.body().getLoginData().getKnownlangObj().getLanguageName());
                        HomeActivity.sessionManager.setKnownLangNativeName(response.body().getLoginData().getKnownlangObj().getNativeName());
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