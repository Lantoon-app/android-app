package com.bazinga.lantoon.home.payment;

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
import com.bazinga.lantoon.home.payment.payu.PayUActivity;
import com.bazinga.lantoon.login.SessionManager;
import com.bazinga.lantoon.retrofit.ApiClient;
import com.bazinga.lantoon.retrofit.ApiInterface;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.GsonBuilder;

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
                                    startPayment(response.body().getTrasactionData().getTransactinId(), "9999999999", "nizamcseb@gmail.com", position);
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

                /*  if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
                 *//* HashMap<String,Object> additionalParamsMap = new HashMap<>();
                    additionalParamsMap.put(PayUCheckoutProConstants.CP_UDF1, "udf1");
                    additionalParamsMap.put(PayUCheckoutProConstants.CP_UDF2, "udf2");
                    additionalParamsMap.put(PayUCheckoutProConstants.CP_UDF3, "udf3");
                    additionalParamsMap.put(PayUCheckoutProConstants.CP_UDF4, "udf4");
                    additionalParamsMap.put(PayUCheckoutProConstants.CP_UDF5, "udf5");*//*

                    PayUPaymentParams.Builder builder = new PayUPaymentParams.Builder();
                    builder.setAmount("1.0")
                            .setIsProduction(false)
                            .setProductInfo(paymentPackageList.get(position).getPackageName())
                            .setKey(Key.payUtestKey)
                            .setPhone("9999999999")
                            .setTransactionId(String.valueOf(System.currentTimeMillis()))
                            .setFirstName(sessionManager.getUserName())
                            .setEmail("nizamcseb@gmail.com")
                            .setSurl("https://www.lantoon.net/")
                            .setFurl("https://www.lantoon.net/");
                            //.setUserCredential("")
                            //.setAdditionalParams(additionalParamsMap); //Optional, can contain any additional PG params
                    PayUPaymentParams payUPaymentParams = builder.build();
                    PayUCheckoutPro.open(
                            getActivity(),
                            payUPaymentParams,
                            new PayUCheckoutProListener() {

                                @Override
                                public void onPaymentSuccess(Object response) {
                                    //Cast response object to HashMap
                                    HashMap<String, Object> result = (HashMap<String, Object>) response;
                                    Log.d("payuResponse success body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                                    String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                    String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);

                                    Log.d("payuResponse success", payuResponse);
                                    Log.d("merchantResponse success", merchantResponse);
                                }

                                @Override
                                public void onPaymentFailure(Object response) {
                                    //Cast response object to HashMap
                                    HashMap<String, Object> result = (HashMap<String, Object>) response;
                                    Log.d("payuResponse faliure body= ", new GsonBuilder().setPrettyPrinting().create().toJson(response));
                                    String payuResponse = (String) result.get(PayUCheckoutProConstants.CP_PAYU_RESPONSE);
                                    String merchantResponse = (String) result.get(PayUCheckoutProConstants.CP_MERCHANT_RESPONSE);

                                    Log.d("payuResponse faliure", payuResponse);
                                    Log.d("merchantResponse faliure", merchantResponse);
                                }

                                @Override
                                public void onPaymentCancel(boolean isTxnInitiated) {
                                }

                                @Override
                                public void onError(ErrorResponse errorResponse) {
                                    String errorMessage = errorResponse.getErrorMessage();
                                    Log.d("payuResponse errorMessage", errorMessage);
                                }

                                @Override
                                public void setWebViewProperties(@Nullable WebView webView, @Nullable Object o) {
                                    //For setting webview properties, if any. Check Customized Integration section for more details on this
                                }

                                @Override
                                public void generateHash(HashMap<String, String> valueMap, PayUHashGenerationListener hashGenerationListener) {
                                    String hashName = valueMap.get(PayUCheckoutProConstants.CP_HASH_NAME);
                                    String hashData = valueMap.get(PayUCheckoutProConstants.CP_HASH_STRING);
                                    System.out.println("hashData" + hashData);

                                    if (!TextUtils.isEmpty(hashName) && !TextUtils.isEmpty(hashData)) {

                                        //Do not generate hash from local, it needs to be calculated from server side only. Here, hashString contains hash created from your server side.
                                        String hash = "";
                                        //String hash = "f5601c723fdd79552c8cbe2ba1840629bc1cd00c8a7b865afbe35929169e6375e261c76b1040c0d4d446d33f571285353f58793278692ea8b132330eba5c07d6";
                                        hash = hashCal("SHA-512",hashData+Key.payUtestSalt);
                                        Log.d("hashData",hashData+Key.payUtestSalt);
                                        Log.d("hash",hash);
                                        Log.d("hashname",hashName);
                                        if (hash != "") {
                                            HashMap<String, String> dataMap = new HashMap<>();
                                            dataMap.put(hashName, hash);
                                            hashGenerationListener.onHashGenerated(dataMap);
                                        }
                                       *//* HashCodeGenerateViewModel hashCodeGenerateViewModel = new ViewModelProvider(getActivity(), new HashCodeViewModelFactory(hashData)).get(HashCodeGenerateViewModel.class);

                                        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
                                            hashCodeGenerateViewModel.getHashCodeFromServerMutableLiveData().observe(getActivity(), new Observer<String>() {
                                                @Override
                                                public void onChanged(String s) {
                                                    Log.d("hashCode", s);
                                                    HashMap<String, String> dataMap = new HashMap<>();
                                                    dataMap.put(hashName, s);
                                                    hashGenerationListener.onHashGenerated(dataMap);
                                                }
                                            });

                                        } else {
                                            CommonFunction.netWorkErrorAlert(getActivity());
                                        }*//*
                                    }
                                }
                            }
                    );

                } else {
                    CommonFunction.netWorkErrorAlert(getActivity());
                }*/

            }
        });
        listView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

            }
        });


        return root;
    }

    private void startPayment(String txnId, String phnNumber, String emailID, int position) {
        if (NetworkUtil.getConnectivityStatus(getContext()) != 0) {
            Intent paymentIntent = new Intent(getActivity(), PayUActivity.class);
            paymentIntent.putExtra(Tags.TAG_USER_ID, sessionManager.getUid());
            paymentIntent.putExtra(Tags.TAG_LEARN_LANGUAGE, sessionManager.getLearnLangId().toString());
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