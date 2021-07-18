package com.blackbirds.shakil.bkashpayment.Model;

import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;

import com.blackbirds.shakil.bkashpayment.PaymentSuccessActivity;

public class BkashJavaScriptInterface {
    Context context;

    /** Instantiate the interface and set the context */
    public BkashJavaScriptInterface(Context context) {
        this.context = context;
    }

    /** Show a toast from the web page */
    @JavascriptInterface
    public void OnPaymentSuccess(String data){
        /** Filtering received data coming from bkash edn point */
        String[] paymentData = data.split("&");
        String paymentID = paymentData[0].trim().replace("PaymentID= ", "").trim();
        String transactionID = paymentData[1].trim().replace("TransactionID= ", "").trim();
        String amount = paymentData[2].trim().replace("Amount= ", "").trim();

        /** Payment successful. Go to another activity and save order data to database */
        Intent intent = new Intent(context, PaymentSuccessActivity.class);
        intent.putExtra("TRANSACTION_ID", transactionID);
        intent.putExtra("PAID_AMOUNT", amount);
        intent.putExtra("PAYMENT_SERIALIZE", data);
        context.startActivity(intent);
    }
}
