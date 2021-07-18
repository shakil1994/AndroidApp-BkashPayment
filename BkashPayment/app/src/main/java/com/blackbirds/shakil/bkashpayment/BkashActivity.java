package com.blackbirds.shakil.bkashpayment;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.blackbirds.shakil.bkashpayment.Model.BkashJavaScriptInterface;
import com.blackbirds.shakil.bkashpayment.Model.PaymentRequest;
import com.google.gson.Gson;

public class BkashActivity extends AppCompatActivity {

    WebView webViewBkashPayment;
    ProgressBar progressBar;
    String amount = "";
    String request = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bkash);

        webViewBkashPayment = findViewById(R.id.webViewBkashPayment);
        progressBar = findViewById(R.id.progressBar);

        /** Check there is any intent data or not */
        if (getIntent().getExtras() == null){
            /** No data */
            Toast.makeText(this, "Amount is empty. You can't pay through bkash. Try again", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            /** Make sure your keyName is same as MainActivity */
            amount = getIntent().getExtras().getString("AMOUNT");

            /** Create a PaymentRequest Model */
            PaymentRequest paymentRequest = new PaymentRequest();
            paymentRequest.setAmount(amount);
            paymentRequest.setIntent("sale");

            Gson gson = new Gson();
            request = gson.toJson(paymentRequest);

            WebSettings webSettings = webViewBkashPayment.getSettings();
            webSettings.setJavaScriptEnabled(true);

            /**
             * Below part is for enabling webview settings for using javaScript and accessing html and other assets
             */
            webViewBkashPayment.setClickable(true);
            webViewBkashPayment.getSettings().setDomStorageEnabled(true);
            webViewBkashPayment.getSettings().setAppCacheEnabled(false);
            webViewBkashPayment.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
            webViewBkashPayment.clearCache(true);
            webViewBkashPayment.getSettings().setAllowFileAccessFromFileURLs(true);
            webViewBkashPayment.getSettings().setAllowUniversalAccessFromFileURLs(true);

            /**
             * To control any kind of interaction from html fil
             */
            webViewBkashPayment.addJavascriptInterface(new BkashJavaScriptInterface(this), "KinYardsPaymentData");

            /** API Host Link */
            webViewBkashPayment.loadUrl("https://mominulcse7.com/API/bkash/payment.php");

            webViewBkashPayment.setWebViewClient(new CheckoutWebViewClient());

        }
    }

    public class CheckoutWebViewClient extends WebViewClient {

        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error){
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.d("External URL: ", url);
            if (url.equals("https://www.bkash.com/terms-and-conditions")){
                Intent myIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(myIntent);
                return true;
            }
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            String paymentRequest = "{paymentRequest: " + request + "}";
            webViewBkashPayment.loadUrl("javascript:callReconfigure(" + paymentRequest + " )");
            webViewBkashPayment.loadUrl("javascript:clickPayButton");
            progressBar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        /** People can press back button and payment may cancel, so alert he really want to cancel payment or not */
        popupPaymentCancelAlert();
    }

    private void popupPaymentCancelAlert() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setMessage("Want to cancel payment process?");
        alert.setCancelable(false);
        alert.setIcon(R.drawable.ic_launcher_background);
        alert.setTitle("Alert!");
        alert.setPositiveButton("Yes", (dialog, which) -> {
            Toast.makeText(this, "Payment Canceled", Toast.LENGTH_SHORT).show();
            BkashActivity.super.onBackPressed();
        });
        alert.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
    }
}