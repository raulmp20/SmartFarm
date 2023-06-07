package com.example.pedroluis;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class QRCodeScannerActivity extends Activity {

    private static final String TAG = "QRCodeScannerActivity";
    private String code;
    private TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_scanner);

        text = findViewById(R.id.result_text);

        // Inicializa o scanner QR
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE);
        integrator.setPrompt("Escaneie o código QR");
        integrator.setCameraId(0);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }

    // Função que é chamada quando o scanner QR retorna um resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Log.d(TAG, "onActivityResult: Escaneamento cancelado");
                Toast.makeText(this, "Escaneamento cancelado", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                code = result.getContents();
                Log.d(TAG, "onActivityResult: Código escaneado: " + code);
                //Toast.makeText(this, "Código escaneado: " + code, Toast.LENGTH_SHORT).show();
                text.setText(code);



                Intent intent = new Intent(this, CadastroEstufaActivity.class);
                intent.putExtra("ID",code);

                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
