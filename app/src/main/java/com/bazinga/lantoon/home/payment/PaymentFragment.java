package com.bazinga.lantoon.home.payment;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.home.HomeActivity;
import com.bazinga.lantoon.home.changereferlanguage.ChangeReferenceLanguageAdapter;
import com.bazinga.lantoon.home.changereferlanguage.ChangeReferenceLanguageViewModel;
import com.bazinga.lantoon.home.chapter.ChapterViewModel;
import com.bazinga.lantoon.home.chapter.ChapterViewModelFactory;
import com.bazinga.lantoon.home.payment.model.PaymentPackage;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.registration.langselection.model.Language;
import com.payu.base.models.ErrorResponse;
import com.payu.base.models.PayUPaymentParams;
import com.payu.checkoutpro.PayUCheckoutPro;
import com.payu.checkoutpro.utils.PayUCheckoutProConstants;
import com.payu.ui.model.listeners.PayUCheckoutProListener;
import com.payu.ui.model.listeners.PayUHashGenerationListener;

import java.util.HashMap;
import java.util.List;

public class PaymentFragment extends Fragment {

    SessionManager sessionManager;
    PaymentViewModel paymentViewModel;
    ListView listView;
    List<PaymentPackage> paymentPackageList;
    boolean fragmentDestroyed = false;
    PaymentPackageAdapter paymentPackageAdapter;
    ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        sessionManager = new SessionManager(getContext());
        View root = inflater.inflate(R.layout.fragment_payment, container, false);
        listView = root.findViewById(R.id.llView);
        progressBar = root.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        listView.setDivider(null);
        paymentViewModel = new ViewModelProvider(this,
                new PaymentViewModelFactory(sessionManager.getUid())).get(PaymentViewModel.class);

        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            progressBar.setVisibility(View.VISIBLE);
            paymentViewModel.getPaymentPackageMutableLiveData().observe(getActivity(), new Observer<List<PaymentPackage>>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onChanged(List<PaymentPackage> paymentPackages) {
                    progressBar.setVisibility(View.GONE);
                    if (!fragmentDestroyed) {
                        paymentPackageList = paymentPackages;

                        paymentPackageAdapter = new PaymentPackageAdapter(getContext(), paymentPackageList);
                        listView.setAdapter(paymentPackageAdapter);
                    }
                }
            });
        } else {
            CommonFunction.netWorkErrorAlert(getActivity());
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
                    PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
                    builder.setAmount("100.0")
                            .setIsProduction(false)
                            .setProductInfo(paymentPackageList.get(position).getPackageName())
                            .setKey("gnB96B")
                            .setPhone("")
                            .setTransactionId("test12345")
                            .setFirstName(sessionManager.getUserName())
                            .setEmail("nizamcseb@gmail.com")
                            .setSurl("https://www.lantoon.net/")
                            .setFurl("https://www.lantoon.net/")
                            .setUserCredential("")
                            .setAdditionalParams(new HashMap<>()); //Optional, can contain any additional PG params
                    PayUPaymentParams payUPaymentParams = builder.build();
                   /* PayUCheckoutPro.open(
                            this,
                            payUPaymentParams,
                            new PayUCheckoutProListener() {

                                @Override
                                public void onPaymentSuccess(Object response) {
                                    //Cast response object to HashMap
                                    HashMap<String,Object> result = (HashMap<String, Object>) response;
                                    String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                    String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);
                                }

                                @Override
                                public void onPaymentFailure(Object response) {
                                    //Cast response object to HashMap
                                    HashMap<String,Object> result = (HashMap<String, Object>) response;
                                    String payuResponse = (String)result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                    String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);
                                }

                                @Override
                                public void onPaymentCancel(boolean isTxnInitiated) {
                                }

                                @Override
                                public void onError(ErrorResponse errorResponse) {
                                    String errorMessage = errorResponse.getErrorMessage();
                                }

                                @Override
                                public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {
                                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                                }

                                @Override
                                public void generateHash(HashMap<String, String> valueMap, PayUHashGenerationListener hashGenerationListener) {
                                    String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                                    String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                                    if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {
                                        //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                                        String hash = hashString;
                                        HashMap<String, String> dataMap = new HashMap<>();
                                        dataMap.put(hashName, hash);
                                        hashGenerationListener.onHashGenerated(dataMap);
                                    }
                                }
                            }
                    );*/
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
}