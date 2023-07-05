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

public class AlterarDadosEmailActivity extends AppCompatActivity {
    public MqttAndroidClient mqttAndroidClient;
    MqttHelper mqttHelper;
    String emailUser;
    boolean auxParaPublicarUmaVez = true;
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mqttHelper = new MqttHelper();
        startMqtt();
        setContentView(R.layout.activity_alterar_dados_email);


        // Botão enviar email
        Button enviar_email = findViewById(R.id.Botao_Enviar_Email);
        Button cancelar_envio = findViewById(R.id.Botao_Cancela);
        // Botão "Enviar Pin"


        // Pra pegar as informações da caixa de texto
        EditText email_alterar = findViewById(R.id.userAltera);



        enviar_email.setOnClickListener(v -> {
            // atribuindo a "e-mail" o e-mail que eu escrever na tela
            String email = email_alterar.getText().toString();


            if(email.isEmpty())
                Toast.makeText(AlterarDadosEmailActivity.this, "Insira um e-mail", Toast.LENGTH_SHORT).show();
            // Mudando a tela para a tela a de token
            if(!email.isEmpty() ) {
                //mqttHelper.publish();
                mqttHelper.publish(email, "Smart_Farm/"+mqttHelper.getClientId()+"/Identifica/Email");

            }
        });

        cancelar_envio.setOnClickListener(v ->{
            // Cancelando a tentativa de mudar a senha
            Intent mudar = new Intent(AlterarDadosEmailActivity.this, LoginActivity.class);
            startActivity(mudar);
        });
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
                Log.w("Mqtt", mqttMessage.toString());
                System.out.println("Vendo se mensagem chegou");
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Identifica/StatusPin")){
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(AlterarDadosEmailActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Intent mudar = new Intent(AlterarDadosEmailActivity.this, ConfirmTokenAltSenhaActivity.class);
                            startActivity(mudar);
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





    }




}


