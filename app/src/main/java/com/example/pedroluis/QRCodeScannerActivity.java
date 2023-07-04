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
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.android.service.MqttAndroidClient;

public class QRCodeScannerActivity extends Activity {
    // Pra consultar o banco de dados
    public MqttAndroidClient mqttAndroidClient;
    // Acessar o banco de dados, var. aux. para banco de dados
    MqttHelper mqttHelper;
    private static final String TAG = "QRCodeScannerActivity";
    private String code;
    private TextView text;
    String switchState;
    String nomeEstufa;
    String valorSpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();

        switchState = extras.getString("switchState");
        nomeEstufa = extras.getString("nomeEstufa");
        valorSpinner = extras.getString("spinnerValue");
        setContentView(R.layout.activity_qrcode_scanner);
        mqttHelper = new MqttHelper();
        text = findViewById(R.id.result_text);

        Toast.makeText(this, switchState, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, nomeEstufa, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, valorSpinner, Toast.LENGTH_SHORT).show();
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
                Toast.makeText(this, "Escaneamento cancelado!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                code = result.getContents();
                Log.d(TAG, "onActivityResult: Código escaneado: " + code);
                Toast.makeText(this, "Código escaneado: " + code, Toast.LENGTH_SHORT).show();
                //text.setText(code);

                System.out.println(code);

                Intent intent = new Intent(this, EstufasCadastradasActivity.class);

                /*intent.putExtra("switchState",switchState);
                intent.putExtra("nomeEstufa",nomeEstufa);
                intent.putExtra("spinnerValue",valorSpinner);
                intent.putExtra("ID",code);*/
                //PUBLICAND TODOS DADOS DE UMA VEZ
                mqttHelper.publish(code,
                        "Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/dados");

                startActivity(intent);
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
}
