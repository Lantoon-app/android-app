package com.bazinga.lantoon.home.payment.paymentwall;

import static com.bazinga.lantoon.Key.paymentWallProjectKey;
import static com.bazinga.lantoon.Key.paymentWallSecretKey;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bazinga.lantoon.CommonFunction;
import com.bazinga.lantoon.R;
//import com.paymentwall.alipayadapter.PsAlipay;
import com.bazinga.lantoon.Tags;
import com.google.gson.GsonBuilder;
import com.paymentwall.pwunifiedsdk.core.PaymentSelectionActivity;
import com.paymentwall.pwunifiedsdk.core.UnifiedRequest;
import com.paymentwall.pwunifiedsdk.util.Key;
import com.paymentwall.pwunifiedsdk.util.ResponseCode;


public class PaymentWallActivity extends Activity {

    String userId = "", learLangId = "", learLangName = "", userName = "", phoneNumber = "", emailId = "", txnId = "", pdPackageId = "", pdPackageName = "", pdChaptersUnlocked = "", pdTotalDuration = "", pdPrice = "", pdTax = "", pdTotalAmount = "", pdSurl = "", pdFurl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_wall);
        initView();
        Button pay_now_button = findViewById(R.id.pay_now_button);
        pay_now_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPayment();
            }
        });

        Intent intent = new Intent(this, BrickService.class);
        startService(intent);
    }

    private void initDatas() {
        userId = getIntent().getStringExtra(Tags.TAG_USER_ID);
        learLangId = getIntent().getStringExtra(Tags.TAG_LEARN_LANGUAGE_ID);
        learLangName = getIntent().getStringExtra(Tags.TAG_LEARN_LANGUAGE_NAME);
        userName = getIntent().getStringExtra(Tags.TAG_USERNAME);
        phoneNumber = getIntent().getStringExtra(Tags.TAG_PHONE_NUMBER);
        emailId = getIntent().getStringExtra(Tags.TAG_EMAILID);
        txnId = getIntent().getStringExtra(Tags.TAG_PAYMENT_TXN_ID);

        pdPackageId = getIntent().getStringExtra(Tags.TAG_PACKAGE_ID);
        pdPackageName = getIntent().getStringExtra(Tags.TAG_PACKAGE_NAME);
        pdChaptersUnlocked = getIntent().getStringExtra(Tags.TAG_PACKAGE_CHAPTERS_UNLOCKED);
        pdTotalDuration = getIntent().getStringExtra(Tags.TAG_PACKAGE_TOTAL_DURATION);
        pdPrice = getIntent().getStringExtra(Tags.TAG_PACKAGE_PRICE);
        //pdTax = pdTax
        pdTotalAmount = getIntent().getStringExtra(Tags.TAG_PACKAGE_PRICE);
        pdSurl = getIntent().getStringExtra(Tags.TAG_PACKAGE_S_URL);
        pdFurl = getIntent().getStringExtra(Tags.TAG_PACKAGE_F_URL);
    }

    private void initView() {
        TextView pd_package_name = findViewById(R.id.pd_package_name);
        TextView pd_package_language = findViewById(R.id.pd_package_language);
        TextView pd_chapters_unlocked = findViewById(R.id.pd_chapters_unlocked);
        TextView pd_total_duration = findViewById(R.id.pd_total_duration);
        TextView pd_price = findViewById(R.id.pd_price);
        TextView pd_tax = findViewById(R.id.pd_tax);
        TextView pd_total_amount = findViewById(R.id.pd_total_amount);

        pd_package_name.setText(getIntent().getStringExtra(Tags.TAG_PACKAGE_NAME));
        pd_package_language.setText(getIntent().getStringExtra(Tags.TAG_LEARN_LANGUAGE_NAME));
        pd_chapters_unlocked.setText(getIntent().getStringExtra(Tags.TAG_PACKAGE_CHAPTERS_UNLOCKED));
        pd_total_duration.setText(getIntent().getStringExtra(Tags.TAG_PACKAGE_TOTAL_DURATION) + " Days");
        pd_price.setText(getIntent().getStringExtra(Tags.TAG_PACKAGE_PRICE) + " " + getIntent().getStringExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL));
        pd_tax.setText("");
        pd_total_amount.setText(getIntent().getStringExtra(Tags.TAG_PACKAGE_PRICE) + " " + getIntent().getStringExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL));
    }

    private void startPayment() {
        /*UnifiedRequest request = new UnifiedRequest();
                request.setPwProjectKey(paymentWallProjectKey);
                request.setPwSecretKey(paymentWallSecretKey);
                request.setAmount(Double.parseDouble(paymentPackageList.get(position).getPrice()));
                request.setCurrency(paymentPackageList.get(position).getCurrencyCode());
                request.setItemName(paymentPackageList.get(position).getPackageName());
                request.setItemId(paymentPackageList.get(position).getPackageId());
                request.setUserId(sessionManager.getUid());
                request.setSignVersion(3);
                request.setItemResID(R.drawable.logo);
                request.setTimeout(30000);
                request.addCustomParam("own_transactin_id", txnId);*/
        UnifiedRequest request = new UnifiedRequest();
       /* request.setPwProjectKey(paymentWallProjectKey);
        request.setPwSecretKey(paymentWallSecretKey);
        //request.setAmount(Double.parseDouble(getIntent().getStringExtra(Tags.TAG_PACKAGE_PRICE)));
        request.setAmount(1.0);
        //request.setCurrency(getIntent().getStringExtra(Tags.TAG_PACKAGE_CURRENCY_CODE));
        request.setCurrency("USD");
        request.setItemName("pdPackageName");
        request.setItemId("1234565");
        request.setUserId("userId123");
        request.setItemResID(R.drawable.logo);
        request.setTimeout(30000);
        request.setSignVersion(3);

        request.setTestMode(false);*/

        //request.addBrick();
        //request.addMint();
        request.setPwProjectKey(paymentWallProjectKey);
        request.setPwSecretKey(paymentWallSecretKey); //optional, if you add your secret key here, signature will generate from SDK but we highly recommend don't expose secret key to front end. You can use your server calculate it.
        request.setAmount(1.0);
        request.setCurrency("USD");
        request.setItemName("pdPackageName");
        request.setItemId("1234565");
        request.setUserId("userId123");
        request.setSignVersion(3);
        request.setItemResID(R.drawable.logo);
        request.setTimeout(30000);

        request.addCustomParam("timeStamp", System.currentTimeMillis() / 1000 + "");
        request.addCustomParam("own_order_id", "o123456");
        request.addCustomParam("shopname", "ecopark");

        Log.d("PaymentWallActivity", new GsonBuilder().setPrettyPrinting().create().toJson(request));
        //request.addMobiamo();

        // request.addPwLocal();
        // request.addPwlocalParams(Const.P.EMAIL, "fixed");
        //request.addPwlocalParams(Const.P.WIDGET, "pw");
        //request.addPwlocalParams(Const.P.EVALUATION, "1");

/*
        PsAlipay alipay = new PsAlipay();
        alipay.setAppId(Constants.ALIPAY.APP_ID);
        alipay.setPaymentType(Constants.ALIPAY.PAYMENT_TYPE);
        // extra params for international account
        alipay.setItbPay(Constants.ALIPAY.IT_B_PAY);
        alipay.setForexBiz(Constants.ALIPAY.FOREX_BIZ);
        alipay.setAppenv(Constants.ALIPAY.APPENV);

        ExternalPs alipayPs = new ExternalPs("alipay", "Alipay", 0, alipay);
        request.add(alipayPs);*/

        Intent intent = new Intent(getApplicationContext(), PaymentSelectionActivity.class);
        intent.putExtra(Key.REQUEST_MESSAGE, request);
        startActivityForResult(intent, PaymentSelectionActivity.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case ResponseCode.ERROR:
                // There is an error with the payment
                CommonFunction.postPaymentPurchaseDetails(this, this,
                        "Payment Failure",
                        "Try after sometime",
                        "failure",
                        txnId,
                        pdPackageId,
                        userId,
                        learLangId,
                        pdTotalAmount,
                        pdTotalAmount,
                        "card",
                        pdChaptersUnlocked,
                        pdTotalDuration);
                break;
            case ResponseCode.CANCEL:
                // User cancels the payment
                break;
            case ResponseCode.SUCCESSFUL:
                // The payment is successful
                CommonFunction.postPaymentPurchaseDetails(this,this,
                        "Payment Successfull",
                        "You have unlocked the new chapters",
                        "success",
                        txnId,
                        pdPackageId,
                        userId,
                        learLangId,
                        pdTotalAmount,
                        pdTotalAmount,
                        "card",
                        pdChaptersUnlocked, pdTotalDuration);
                break;
            case ResponseCode.FAILED:
                // The payment was failed
                break;
            default:
                break;
        }
    }
}
