package com.bazinga.lantoon.home.changereferlanguage;

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
import com.bazinga.lantoon.home.mylanguage.MyLanguageViewModel;
import com.bazinga.lantoon.home.mylanguage.MyLanguagesAdapter;
import com.bazinga.lantoon.home.mylanguage.model.MyLanguageData;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.login.data.model.LoggedInUserResponse;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeReferenceLanguageFragment extends Fragment {


    ChangeReferenceLanguageViewModel changeReferenceLanguageViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_change_refer_language, container, false);
        changeReferenceLanguageViewModel = new ViewModelProvider(this).get(ChangeReferenceLanguageViewModel.class);


        return root;
    }


}