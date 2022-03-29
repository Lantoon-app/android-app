package com.bazinga.lantoon.home.payment;

import static com.bazinga.lantoon.Key.paymentWallProjectKey;
import static com.bazinga.lantoon.Key.paymentWallSecretKey;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.NetworkUtil;
import com.bazinga.lantoon.R;
import com.bazinga.lantoon.Tags;
import com.bazinga.lantoon.home.payment.model.PaymentPackage;
import com.bazinga.lantoon.home.payment.model.TransactionResponse;
import com.bazinga.lantoon.home.payment.paymentwall.PaymentWallActivity;
import com.bazinga.lantoon.home.payment.payu.PayUActivity;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
                    ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                    Call<TransactionResponse> call = apiInterface.getPaymentTxnId();
                    call.enqueue(new Callback<TransactionResponse>() {
                        @Override
                        public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                           CommonFunction.printServerResponse("TransactionResponse",response.body());
                            if (response.body().getTrasactionData() != null || response.body().getTrasactionData().getTransactinId() != null || response.body().getTrasactionData().getTransactinId() != "")
                                    startPayment(sessionManager.getRegionCode(),response.body().getTrasactionData().getTransactinId(), "9999999999", sessionManager.getEmailId(), position);
                            else
                                Snackbar.make(getContext(), listView, getActivity().getString(R.string.some_error_occurred), Snackbar.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFailure(Call<TransactionResponse> call, Throwable t) {
                            Snackbar.make(getContext(), listView, t.getMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });


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

    private void startPayment(String regionCode, String txnId, String phnNumber, String emailID, int position) {
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {

            //if(regionCode.equals("569")) {
            if(regionCode.equals("896")) {
                Intent paymentIntent = new Intent(getActivity(), PayUActivity.class);
                paymentIntent.putExtra(Tags.TAG_USER_ID, sessionManager.getUid());
                paymentIntent.putExtra(Tags.TAG_LEARN_LANGUAGE_ID, sessionManager.getLearnLangId().toString());
                paymentIntent.putExtra(Tags.TAG_LEARN_LANGUAGE_NAME, sessionManager.getLearnLangName());
                paymentIntent.putExtra(Tags.TAG_USERNAME, sessionManager.getUserName());
                paymentIntent.putExtra(Tags.TAG_PHONE_NUMBER, phnNumber);
                paymentIntent.putExtra(Tags.TAG_EMAILID, emailID);
                paymentIntent.putExtra(Tags.TAG_PAYMENT_TXN_ID, txnId);

                paymentIntent.putExtra(Tags.TAG_PACKAGE_ID, paymentPackageList.get(position).getPackageId());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_NAME, paymentPackageList.get(position).getPackageName());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CHAPTERS_UNLOCKED, paymentPackageList.get(position).getChaptersUnlocked());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_TOTAL_DURATION, paymentPackageList.get(position).getDurationInDays());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_PRICE, paymentPackageList.get(position).getPrice());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY, paymentPackageList.get(position).getCurrency());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY_CODE, paymentPackageList.get(position).getCurrencyCode());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL, paymentPackageList.get(position).getCurrencySymbol());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_REGION_CODE, paymentPackageList.get(position).getRegionCode());

                paymentIntent.putExtra(Tags.TAG_PACKAGE_S_URL, "https://payuresponse.firebaseapp.com/success");
                paymentIntent.putExtra(Tags.TAG_PACKAGE_F_URL, "https://payuresponse.firebaseapp.com/failure");
                getActivity().startActivity(paymentIntent);
            //}else if (regionCode.equals("896")){
            }else if (regionCode.equals("569")){
                Intent paymentIntent = new Intent(getActivity(), PaymentWallActivity.class);
                paymentIntent.putExtra(Tags.TAG_USER_ID, sessionManager.getUid());
                paymentIntent.putExtra(Tags.TAG_LEARN_LANGUAGE_ID, sessionManager.getLearnLangId().toString());
                paymentIntent.putExtra(Tags.TAG_LEARN_LANGUAGE_NAME, sessionManager.getLearnLangName());
                paymentIntent.putExtra(Tags.TAG_USERNAME, sessionManager.getUserName());
                paymentIntent.putExtra(Tags.TAG_PHONE_NUMBER, phnNumber);
                paymentIntent.putExtra(Tags.TAG_EMAILID, emailID);
                paymentIntent.putExtra(Tags.TAG_PAYMENT_TXN_ID, txnId);

                paymentIntent.putExtra(Tags.TAG_PACKAGE_ID, paymentPackageList.get(position).getPackageId());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_NAME, paymentPackageList.get(position).getPackageName());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CHAPTERS_UNLOCKED, paymentPackageList.get(position).getChaptersUnlocked());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_TOTAL_DURATION, paymentPackageList.get(position).getDurationInDays());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_PRICE, paymentPackageList.get(position).getPrice());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY, paymentPackageList.get(position).getCurrency());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY_CODE, paymentPackageList.get(position).getCurrencyCode());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL, paymentPackageList.get(position).getCurrencySymbol());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_REGION_CODE, paymentPackageList.get(position).getRegionCode());
                paymentIntent.putExtra(Tags.TAG_PACKAGE_S_URL, "https://payuresponse.firebaseapp.com/success");
                paymentIntent.putExtra(Tags.TAG_PACKAGE_F_URL, "https://payuresponse.firebaseapp.com/failure");
                getActivity().startActivity(paymentIntent);


            }

        } else {
            CommonFunction.netWorkErrorAlert(getActivity());
        }
    }

    public static String hashCal(String type, String str) {
        byte[] hashseq = str.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance(type);
            algorithm.reset();
            algorithm.update(hashseq);
            byte messageDigest[] = algorithm.digest();
            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if (hex.length() == 1) {
                    hexString.append("0");
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException nsae) {

        }
        return hexString.toString();
    }
}