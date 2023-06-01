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

public class ConfirmTokenAltDadosActivity extends AppCompatActivity {

    MqttHelper mqttHelper = new MqttHelper();

    private MqttAndroidClient mqttAndroidClient;
    boolean auxParaPublicarUmaVez = true;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados_token);

        try {
            startMqtt();
        }catch (Exception e){
            Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
        }
        //JoaoMqtt();

        EditText token = findViewById(R.id.tokenCadastroo);

        Button confirmToken = findViewById(R.id.Botao_Confirmar_ADT);
        confirmToken.setOnClickListener(view -> {
            String tokenAux = token.getText().toString();
            if(!tokenAux.isEmpty()) {
                if (tokenAux.matches("^[0-9]{6}$"))
                    try {
                        mqttHelper.publish(tokenAux, "Smart_Farm/" + mqttHelper.getClientId() + "/AltDados/Token");
                    }catch (Exception e){
                        Toast.makeText(this, "Erro: "+e.toString(), Toast.LENGTH_SHORT).show();
                    }

                else
                    Toast.makeText(ConfirmTokenAltDadosActivity.this, "Token Inválido", Toast.LENGTH_SHORT).show();
            }else
                Toast.makeText(ConfirmTokenAltDadosActivity.this, "Insira um Token", Toast.LENGTH_SHORT).show();
        });

        Button reenviarToken = findViewById(R.id.ReenviarCodigo_ADT);

        reenviarToken.setOnClickListener(view -> {
            publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Changes/ResendToken");
        });


        Button cancell = findViewById(R.id.Botao_Cancelar_ADT);
        cancell.setOnClickListener(view ->{
            Intent intent = new Intent(ConfirmTokenAltDadosActivity.this, MenuUsuarioActivity.class);
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
                Log.w("Debug", mqttMessage.toString());
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/AltDados/Status/Token")) {
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfirmTokenAltDadosActivity.this, "Token Incorreto", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(ConfirmTokenAltDadosActivity.this, "Dados Alterados", Toast.LENGTH_SHORT).show();
                            Intent dados_alterados = new Intent(ConfirmTokenAltDadosActivity.this, LoginActivity.class);
                            startActivity(dados_alterados);
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
