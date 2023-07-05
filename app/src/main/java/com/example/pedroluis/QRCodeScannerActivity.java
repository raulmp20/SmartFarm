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
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


import java.io.UnsupportedEncodingException;

public class QRCodeScannerActivity extends Activity {
    // Pra consultar o banco de dados
    public MqttAndroidClient mqttAndroidClient;
    // Acessar o banco de dados, var. aux. para banco de dados
    private MqttHelper mqttHelper;
    private static final String TAG = "QRCodeScannerActivity";
    private String code;
    private TextView text;

    private boolean auxParaPublicarUmaVez = true;
    String switchState;
    String nomeEstufa;
    String valorSpinner;

    String telefoneUser;
    String emailUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        startMqtt();

        /*switchState = extras.getString("switchState");
        nomeEstufa = extras.getString("nomeEstufa");
        valorSpinner = extras.getString("spinnerValue");*/

        setContentView(R.layout.activity_qrcode_scanner);
        mqttHelper = new MqttHelper();
        text = findViewById(R.id.result_text);

        /*Toast.makeText(this, switchState, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, nomeEstufa, Toast.LENGTH_SHORT).show();
        Toast.makeText(this, valorSpinner, Toast.LENGTH_SHORT).show();*/
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
                /*
                Log.d(TAG, "onActivityResult: Código escaneado: " + code);
                Toast.makeText(this, "Código escaneado: " + code, Toast.LENGTH_SHORT).show();
                //text.setText(code);

                System.out.println(code);*/

                //PUBLICAND TODOS DADOS DE UMA VEZ
                //JoaoMqtt();

                Intent intent = new Intent(this, CadastroEstufaActivity.class);

                /*intent.putExtra("switchState",switchState);
                intent.putExtra("nomeEstufa",nomeEstufa);
                intent.putExtra("spinnerValue",valorSpinner);
                intent.putExtra("ID",code);*/
                intent.putExtra("code",code);

                startActivity(intent);
                //finish();
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }
    /*void publish(String payload, String topic) {
        byte[] encodedPayload = new byte[0];
        //teste de conexão
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            mqttAndroidClient.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }*/

    private void JoaoMqtt() {
        mqttAndroidClient = new MqttAndroidClient(getApplicationContext(), mqttHelper.getServerUri(), mqttHelper.getClientId());
        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
                // Exibindo na tela os retornos do Banco de Dados


            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

        });
        //connect();
        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setAutomaticReconnect(true);
        mqttConnectOptions.setCleanSession(false);
        mqttConnectOptions.setUserName(mqttHelper.getUsername());
        mqttConnectOptions.setPassword(mqttHelper.getPassword().toCharArray());

        try {

            mqttAndroidClient.connect(mqttConnectOptions, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {

                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions();
                    disconnectedBufferOptions.setBufferEnabled(true);
                    disconnectedBufferOptions.setBufferSize(100);
                    disconnectedBufferOptions.setPersistBuffer(false);
                    disconnectedBufferOptions.setDeleteOldestMessages(false);
                    mqttAndroidClient.setBufferOpts(disconnectedBufferOptions);
                    //subscribeToTopic();
                    try {
                        mqttAndroidClient.subscribe("Smart_Farm/"+mqttHelper.getClientId()+"/#", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.w("Mqtt", "Subscribed!!!!");
                                if(auxParaPublicarUmaVez) {
                                    //mqttHelper.publish("nomeEstufa", "Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/dados");
                                    auxParaPublicarUmaVez = false;
                                }
                            }

                            @Override
                            public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                Log.w("Mqtt", "Subscribed fail!");
                            }
                        });

                    } catch (MqttException ex) {
                        System.err.println("Exceptionst subscribing");
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    Log.w("Mqtt", "Failed to connect to: " + mqttHelper.getServerUri() + exception.toString());
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }
            @Override
            public void connectionLost(Throwable throwable) {
                //Aparece essa mensagem sempre que a conexão for perdida
                //Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }
            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }
}
