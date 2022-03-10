package com.bazinga.lantoon.home.payment.payu

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bazinga.lantoon.Key
import com.bazinga.lantoon.R
import com.bazinga.lantoon.Tags
import com.bazinga.lantoon.databinding.ActivityPayuBinding
import com.bazinga.lantoon.home.HomeActivity
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.payu.base.models.BaseApiLayerConstants
import com.payu.base.models.ErrorResponse
import com.payu.base.models.PayUPaymentParams
import com.payu.checkoutpro.PayUCheckoutPro
import com.payu.checkoutpro.utils.PayUCheckoutProConstants
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_NAME
import com.payu.checkoutpro.utils.PayUCheckoutProConstants.CP_HASH_STRING
import com.payu.ui.model.listeners.PayUCheckoutProListener
import com.payu.ui.model.listeners.PayUHashGenerationListener

class PayUActivity : AppCompatActivity() {

    private var userName = ""
    private var phoneNumber = ""
    private var emailId = ""
    private var txnId = ""

    private var pdPackageId = ""
    private var pdPackageName = ""
    private var pdChaptersUnlocked = ""
    private var pdTotalDuration = ""
    private var pdPrice = ""
    private var pdTax = ""
    private var pdTotalAmount = ""
    private var pdSurl = ""
    private var pdFurl = ""

    private var key = Key.payULantoonKey
    private var salt = Key.payULantoonSalt

    private lateinit var binding: ActivityPayuBinding

    // variable to track event time
    private var mLastClickTime: Long = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payu)
        initDatas()
        initListeners()
        initUI()
    }

    private fun initDatas() {
        userName = intent.getStringExtra(Tags.TAG_USERNAME).toString()
        phoneNumber = intent.getStringExtra(Tags.TAG_PHONE_NUMBER).toString()
        emailId = intent.getStringExtra(Tags.TAG_EMAILID).toString()
        txnId = intent.getStringExtra(Tags.TAG_PAYMENT_TXN_ID).toString()

        pdPackageId = intent.getStringExtra(Tags.TAG_PACKAGE_ID).toString()
        pdPackageName = intent.getStringExtra(Tags.TAG_PACKAGE_NAME).toString()
        pdChaptersUnlocked = intent.getStringExtra(Tags.TAG_PACKAGE_CHAPTERS_UNLOCKED).toString()
        pdTotalDuration = intent.getStringExtra(Tags.TAG_PACKAGE_TOTAL_DURATION).toString()
        pdPrice = intent.getStringExtra(Tags.TAG_PACKAGE_PRICE).toString()
        //pdTax = pdTax
        pdTotalAmount = intent.getStringExtra(Tags.TAG_PACKAGE_PRICE).toString()
        pdSurl = intent.getStringExtra(Tags.TAG_PACKAGE_S_URL).toString()
        pdFurl = intent.getStringExtra(Tags.TAG_PACKAGE_F_URL).toString()
    }

    private fun initUI() {
        val price = pdPrice.toDouble().toString()
        binding.pdPackageName.setText(pdPackageName)
        binding.pdPackageLanguage.setText("")
        binding.pdChaptersUnlocked.setText(pdChaptersUnlocked)
        binding.pdTotalDuration.setText(pdTotalDuration + " Days")
        binding.pdPrice.setText(price + " " + intent.getStringExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL).toString())
        binding.pdTax.setText(pdTax)
        binding.pdTotalAmount.setText(price + " " + intent.getStringExtra(Tags.TAG_PACKAGE_CURRENCY_SYMBOL).toString())
    }


    private fun initListeners() {
        binding.radioGrpEnv.setOnCheckedChangeListener { radioGroup: RadioGroup, i: Int ->
            when (i) {
                R.id.radioBtnTest -> updateTestEnvDetails()
                R.id.radioBtnProduction -> updateProdEnvDetails()
                else -> updateTestEnvDetails()
            }
        }


    }


    private fun updateTestEnvDetails() {
        //For testing
        key = Key.payUtestKey
        salt = Key.payUtestSalt
    }

    private fun updateProdEnvDetails() {
        //For Production
        key = Key.payULantoonKey
        salt = Key.payULantoonSalt
    }

    fun startPayment(view: View) {
        // Preventing multiple clicks, using threshold of 1 second
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return
        }
        mLastClickTime = SystemClock.elapsedRealtime()

        val paymentParams = preparePayUBizParams()
        initUiSdk(paymentParams)
    }

    fun preparePayUBizParams(): PayUPaymentParams {

        return PayUPaymentParams.Builder().setAmount("1.0")
                .setIsProduction(binding.radioBtnProduction.isChecked)
                .setKey(key)
                .setProductInfo(pdPackageName)
                .setPhone(phoneNumber)
                .setTransactionId(txnId)
                .setFirstName(userName)
                .setEmail(emailId)
                .setSurl(pdSurl)
                .setFurl(pdFurl)
                .build()
    }

    private fun initUiSdk(payUPaymentParams: PayUPaymentParams) {
        PayUCheckoutPro.open(
                this,
                payUPaymentParams,
                object : PayUCheckoutProListener {

                    override fun onPaymentSuccess(response: Any) {
                        processSuccessResponse(response)
                    }

                    override fun onPaymentFailure(response: Any) {
                        processFailureResponse(response)
                    }

                    override fun onPaymentCancel(isTxnInitiated: Boolean) {
                        showSnackBar(resources.getString(R.string.transaction_cancelled_by_user))
                    }

                    override fun onError(errorResponse: ErrorResponse) {

                        val errorMessage: String
                        if (errorResponse != null && errorResponse.errorMessage != null && errorResponse.errorMessage!!.isNotEmpty())
                            errorMessage = errorResponse.errorMessage!!
                        else
                            errorMessage = resources.getString(R.string.some_error_occurred)
                        showSnackBar(errorMessage)
                    }

                    override fun generateHash(
                            map: HashMap<String, String?>,
                            hashGenerationListener: PayUHashGenerationListener
                    ) {
                        if (map.containsKey(CP_HASH_STRING)
                                && map.containsKey(CP_HASH_STRING) != null
                                && map.containsKey(CP_HASH_NAME)
                                && map.containsKey(CP_HASH_NAME) != null
                        ) {

                            val hashData = map[CP_HASH_STRING]
                            val hashName = map[CP_HASH_NAME]

                            var hash: String? = null


                            //calculate SDH-512 hash using hashData and salt
                            hash = HashGenerationUtils.generateHashFromSDK(
                                    hashData!!,
                                    salt
                            )
                            Log.d("hashData", hashData)
                            if (hash != null) {
                                Log.d("hash", hash)
                            }
                            if (hashName != null) {
                                Log.d("hashname", hashName)
                            }


                            if (!TextUtils.isEmpty(hash)) {
                                val hashMap: HashMap<String, String?> = HashMap()
                                hashMap[hashName!!] = hash!!
                                hashGenerationListener.onHashGenerated(hashMap)
                            }
                        }
                    }

                    override fun setWebViewProperties(webView: WebView?, bank: Any?) {
                    }
                })
    }


    private fun showSnackBar(message: String) {
        Snackbar.make(binding.clMain, message, Snackbar.LENGTH_LONG).show()
    }

    private fun processSuccessResponse(response: Any) {
        response as HashMap<*, *>
        Log.d(
                BaseApiLayerConstants.SDK_TAG,
                "payuResponse ; > " + response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                        + ", merchantResponse : > " + response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
        )

        alertbox("Payment Successfull", "You have unlocked the new chapters")

    }

    private fun processFailureResponse(response: Any) {
        response as HashMap<*, *>
        Log.d(
                BaseApiLayerConstants.SDK_TAG,
                "payuResponse ; > " + response[PayUCheckoutProConstants.CP_PAYU_RESPONSE]
                        + ", merchantResponse : > " + response[PayUCheckoutProConstants.CP_MERCHANT_RESPONSE]
        )
        alertbox("Payment Failure", "Try after sometime")

    }

    private fun alertbox(title: String, message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)

        builder.setPositiveButton(android.R.string.ok) { dialog, which ->
            startHomeActivity()
            dialog.dismiss()
        }

        builder.show()
    }

    private fun startHomeActivity() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)

    }

}
