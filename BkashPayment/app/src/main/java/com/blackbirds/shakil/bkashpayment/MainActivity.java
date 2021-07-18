package com.blackbirds.shakil.bkashpayment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    EditText edtAmount;
    AppCompatButton btnCheckout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        edtAmount = findViewById(R.id.edtAmount);
        btnCheckout = findViewById(R.id.btnCheckout);

        btnCheckout.setOnClickListener(v -> {
            checkInputs();
        });
    }

    private void checkInputs() {
        String amountInString = edtAmount.getText().toString().trim();

        /** To check amount validity */
        double amount = 0.0;

        try {
            /** Use try catch so that, if input is invalid, stop taking payment here. */
            amount = Double.parseDouble(amountInString);
        }
        catch (Exception e){
            amount = 0.0;
        }

        if (amount < 1){
            edtAmount.setError("You have to pay at least BDT 1.");
            edtAmount.requestFocus();
        }
        else {
            Intent intent = new Intent(this, BkashActivity.class);
            intent.putExtra("AMOUNT", String.valueOf(amount));
            startActivity(intent);
        }
    }
}