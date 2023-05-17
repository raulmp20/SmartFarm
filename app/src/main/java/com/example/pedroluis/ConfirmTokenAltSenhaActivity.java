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

public class ConfirmTokenAltSenhaActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_alterar_dados_token);

        EditText token = findViewById(R.id.tokenCadastroo);

        startMqtt();

        Button confirmToken = findViewById(R.id.Botao_Confirmar_ADT);
        confirmToken.setOnClickListener(view -> {
            String tokenAux = token.getText().toString();
            if(!tokenAux.isEmpty())

                if (tokenAux.matches("^[0-9]{6}$")) {
                    System.out.println(tokenAux);
                    mqttHelper.publish(tokenAux, "Smart_Farm/" + mqttHelper.getClientId() + "/Altera/Token");
                }
                else
                    Toast.makeText(ConfirmTokenAltSenhaActivity.this, "Token Inválido", Toast.LENGTH_SHORT).show();
            else
                Toast.makeText(ConfirmTokenAltSenhaActivity.this, "Insira um Token", Toast.LENGTH_SHORT).show();

            Intent confirm  = new Intent(ConfirmTokenAltSenhaActivity.this, TrocaSenhaActivity.class);
            startActivity(confirm);
        });

        Button reenviarToken = findViewById(R.id.ReenviarCodigo_ADT);

        reenviarToken.setOnClickListener(view -> {
          mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Altera/ReenviaToken");
        });



        Button cancell = findViewById(R.id.Botao_Cancelar_ADT);
        cancell.setOnClickListener(view ->{
            Intent intent = new Intent(ConfirmTokenAltSenhaActivity.this, LoginActivity.class);
            startActivity(intent);
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Identifica/Status/Token")){
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfirmTokenAltSenhaActivity.this, "Token Incorreto", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Intent mudar = new Intent(ConfirmTokenAltSenhaActivity.this, TrocaSenhaActivity.class);
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