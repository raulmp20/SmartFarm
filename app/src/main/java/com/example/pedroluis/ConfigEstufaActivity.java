package com.example.pedroluis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class ConfigEstufaActivity extends AppCompatActivity {
    // Pra consultar o banco de dados
    public MqttAndroidClient mqttAndroidClient;
    // Acessar o banco de dados, var. aux. para banco de dados
    MqttHelper mqttHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_config_estufa);
        //startMqtt();
        // Pegando as informações das caixas texto
        //EditText novo_nome_att;
        //novo_nome_att = findViewById(R.id.estufa_novo_nome);
        Button troca_senha;
        troca_senha = findViewById(R.id.TrocaSenha_conta);
        // Botão "salvar"
        Button salvar;
        salvar = findViewById(R.id.button_salvar_config);
        // Botão "voltar"
        Button voltar;
        voltar = findViewById(R.id.button_voltar_config);

        // Atualizando os dados
        salvar.setOnClickListener(v -> {
            /*
            // Pegando as informações dos EditText e adicionando a uma string
            String senhaNova = senha_att.getText().toString();
            String confirmSenhaNova = confirm_senha_att.getText().toString();
            String email = email_att.getText().toString();
            String pin = pin_att.getText().toString();
            // Verificando se a senha é igual a senha confirmada
            if (senhaNova.equals(confirmSenhaNova) && !confirmSenhaNova.isEmpty()) {
                publish(email, "Smart_Farm/Atualiza/Email");
                publish(senhaNova, "Smart_Farm/Atualiza/Senha");
                publish(pin, "Smart_Farm/Atualiza/Token");
            } else if (!senhaNova.equals(confirmSenhaNova)) {
                Toast.makeText(ConfigEstufaActivity.this, "As senhas não se coincidem", Toast.LENGTH_SHORT).show();
            }
            if(senhaNova.isEmpty()) {
                Toast.makeText(ConfigEstufaActivity.this, "Insira uma senha", Toast.LENGTH_SHORT).show();
            }
            if(confirmSenhaNova.isEmpty()) {
                Toast.makeText(ConfigEstufaActivity.this, "Insira uma confirmação de senha", Toast.LENGTH_SHORT).show();
            }
            if (email.isEmpty()) {
                Toast.makeText(ConfigEstufaActivity.this, "Insira um e-mail", Toast.LENGTH_SHORT).show();
            }
             */
            Intent save = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);
            startActivity(save);
        });
        // Voltando ao menu
        voltar.setOnClickListener(v -> {
            // Mudando a tela para a tela menu
            Intent mudar = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);
            startActivity(mudar);
        });

        troca_senha.setOnClickListener(v -> {
            // Mudando a tela para a tela menu
            Intent mudar = new Intent(ConfigEstufaActivity.this, TrocaSenhaActivity.class);
            startActivity(mudar);
        });
    }

    /*private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                System.out.println("MQTT OK");
            }

            @Override
            public void connectionLost(Throwable throwable) {
                // Aparece essa mensagem sempre que a conexão for perdida
                Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }

            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                if (topic.equals("Smart_Farm/Atualiza/Status")) {
                    switch (mqttMessage.toString()) {
                        // TROCAR: 00 -> E-mail não encontrado, 01 -> Pin inválido, 11 -> Senha atualizada
                        case ("00"):
                            Toast.makeText(ConfigEstufaActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(ConfigEstufaActivity.this, "Pin inválido", Toast.LENGTH_SHORT).show();
                            break;
                        case ("11"):
                            Toast.makeText(ConfigEstufaActivity.this, "Senha atualizada", Toast.LENGTH_SHORT).show();
                            Intent SenhaTrocada = new Intent(ConfigEstufaActivity.this, LoginActivity.class);
                            startActivity(SenhaTrocada);
                            break;
                        default:
                            break;
                    }
                }
                if(topic.equals("Smart_Farm/Atualiza/StatusPin")) {
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfigEstufaActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(ConfigEstufaActivity.this, "Pin Enviado", Toast.LENGTH_SHORT).show();
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

    //Bloco que envia comandos para o broker
    void publish(String payload, String topic) {
        byte[] encodedPayload = new byte[0];
        //teste de conexão
        try {
            encodedPayload = payload.getBytes("UTF-8");
            MqttMessage message = new MqttMessage(encodedPayload);
            mqttHelper.mqttAndroidClient.publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }*/
}