package com.example.myapplication3;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.util.EnumSet;

import io.mpos.accessories.AccessoryFamily;
import io.mpos.accessories.parameters.AccessoryParameters;
import io.mpos.provider.ProviderMode;
import io.mpos.transactions.Transaction;
import io.mpos.transactions.parameters.TransactionParameters;
import io.mpos.ui.shared.MposUi;
import io.mpos.ui.shared.model.MposUiConfiguration;

public class MainActivity extends AppCompatActivity {


    private String transactionID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void buttonRefundClick(View v)
    {
        TextView tv = (TextView)findViewById(R.id.textView);
        tv.setText("Payworks Refund");

        MposUi ui = MposUi.initialize(this, ProviderMode.MOCK,
                "merchantIdentifier", "merchantSecretKey");

        TransactionParameters transactionParameters = new TransactionParameters.Builder()
                .refund(transactionID)
                // For partial refunds, specify the amount to be refunded
                // and the currency from the original transaction
                .amountAndCurrency(new BigDecimal("1.00"), io.mpos.transactions.Currency.EUR)
                .build();
        Intent intent = ui.createTransactionIntent(transactionParameters);
        startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);


    }

    public void paymentButtonClicked(View v) {

        MposUi ui = MposUi.initialize(this, ProviderMode.MOCK,
                "merchantIdentifier", "merchantSecretKey");

        ui.getConfiguration().setSummaryFeatures(EnumSet.of(
                // Add this line, if you do want to offer printed receipts
                // MposUiConfiguration.SummaryFeature.PRINT_RECEIPT,
                MposUiConfiguration.SummaryFeature.SEND_RECEIPT_VIA_EMAIL)
        );

        // Start with a mocked card reader:
        AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MOCK)
                .mocked()
                .build();
        ui.getConfiguration().setTerminalParameters(accessoryParameters);

        // Add this line if you would like to collect the customer signature on the receipt (as opposed to the digital signature)
        // ui.getConfiguration().setSignatureCapture(MposUiConfiguration.SignatureCapture.ON_RECEIPT);


    /* When using the Bluetooth Miura, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.MIURA_MPI)
                                                                     .bluetooth()
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    */




    /* When using Verifone readers via WiFi or Ethernet, use the following parameters:
    AccessoryParameters accessoryParameters = new AccessoryParameters.Builder(AccessoryFamily.VERIFONE_VIPA)
                                                                     .tcp("192.168.254.123", 16107)
                                                                     .build();
    ui.getConfiguration().setTerminalParameters(accessoryParameters);
    */

        TransactionParameters transactionParameters = new TransactionParameters.Builder()
                .charge(new BigDecimal("5.00"), io.mpos.transactions.Currency.EUR)
                .subject("Bouquet of Flowers")
                .customIdentifier("yourReferenceForTheTransaction")
                .build();

        Intent intent = ui.createTransactionIntent(transactionParameters);
        startActivityForResult(intent, MposUi.REQUEST_CODE_PAYMENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MposUi.REQUEST_CODE_PAYMENT) {
            if (resultCode == MposUi.RESULT_CODE_APPROVED) {
                // Transaction was approved
                // Grab the processed transaction in case you need it
                // (e.g. the transaction identifier for a refund).
                // Keep in mind that the returned transaction might be null
                // (e.g. if it could not be registered).
                Transaction transaction = MposUi.getInitializedInstance().getTransaction();
                transactionID = transaction.getIdentifier();

                Toast.makeText(this, transactionID , Toast.LENGTH_LONG).show();
                TextView tv = (TextView)findViewById(R.id.textView);

                tv.setText(transactionID);

            } else {
                // Card was declined, or transaction was aborted, or failed
                // (e.g. no internet or accessory not found)
                Toast.makeText(this, "Transaction was declined, aborted, or failed",
                        Toast.LENGTH_LONG).show();
            }

        }
    }
}
