package com.blackbirds.shakil.bkashpayment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class PaymentSuccessActivity extends AppCompatActivity {

    TextView txtResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_success);

        txtResult = findViewById(R.id.txtResult);

        if (getIntent().getExtras() == null){
            txtResult.setText("Failed to get data from bkash");
            return;
        }
        else {
            txtResult.setText("TransactionID= " + getIntent().getExtras().getString("TRANSACTION_ID") + "\n\n" +
                    "PaidAmount= " + getIntent().getExtras().getString("PAID_AMOUNT") + "\n\n" +
                    "OtherData= " + getIntent().getExtras().getString("PAYMENT_SERIALIZE") + "\n\n");
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}