package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

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

public class ConfirmTokenCadastroActivity extends AppCompatActivity {

    private MqttHelper mqttHelper;

    private MqttAndroidClient mqttAndroidClient;

    String emailUser;
    boolean auxParaPublicarUmaVez = true;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_token);

        mqttHelper = new MqttHelper();
        JoaoMqtt();

        Bundle extras = getIntent().getExtras();
        emailUser = extras.getString("emailC");

        EditText token = findViewById(R.id.tokenCadastroo);

        Button confirmToken = findViewById(R.id.Botao_Confirmar_ADT);
        confirmToken.setOnClickListener(view -> {
            String tokenAux = token.getText().toString();
            if(!tokenAux.isEmpty()) {
                if (tokenAux.matches("^[0-9]{6}$")){
                    publish(tokenAux,"Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Token");
                }
                else
                    Toast.makeText(ConfirmTokenCadastroActivity.this, "Token Inválido", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(ConfirmTokenCadastroActivity.this, "Insira um Token", Toast.LENGTH_SHORT).show();
        });

        Button reenviarToken = findViewById(R.id.ReenviarCodigo_ADT);

        reenviarToken.setOnClickListener(view -> {
            mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Altera/ReenviaToken");
        });


        Button cancell = findViewById(R.id.Botao_Cancelar_ADT);
        cancell.setOnClickListener(view ->{
            Intent intent = new Intent(ConfirmTokenCadastroActivity.this, CadastroActivity.class);
            startActivity(intent);
        });
    }
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Status/Token")) {
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfirmTokenCadastroActivity.this, "Token Incorreto", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(ConfirmTokenCadastroActivity.this, "Cadastro Completo", Toast.LENGTH_SHORT).show();
                            Intent cadastroCompleto = new Intent(ConfirmTokenCadastroActivity.this, LoginActivity.class);
                            startActivity(cadastroCompleto);
                            break;
                        default:
                            break;
                    }
                }
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
                                    //Publica no topico toda vez que se entra na página.
                                    System.out.println("Email: "+emailUser);
                                    byte[] encodedPayload = new byte[0];
                                    //teste de conexão
                                    try {
                                        encodedPayload = emailUser.getBytes("UTF-8");
                                        MqttMessage message = new MqttMessage(encodedPayload);
                                        mqttAndroidClient.publish("Smart_Farm/" + mqttHelper.getClientId() + "/Cadastro/SendEmail", message);
                                    } catch (UnsupportedEncodingException | MqttException e) {
                                        e.printStackTrace();
                                    }
                                    //mqttAndroidClient.publish("Smart_Farm/" + mqttHelper.getClientId() + "/Cadastro/SendEmail",emailUser);
                                    //mqttHelper.publish(emailUser, "Smart_Farm/" + mqttHelper.getClientId() + "/Cadastro/SendEmail");
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

    void publish(String payload, String topic) {
        byte[] encodedPayload = new byte[0];
        //teste de conexão
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            mqttAndroidClient.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
}
