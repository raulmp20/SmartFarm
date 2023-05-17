package com.example.pedroluis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class LoginActivity extends AppCompatActivity {
    // Pra consultar o banco de dados
    public MqttAndroidClient mqttAndroidClient;
    // Acessar o banco de dados, var. aux. para banco de dados
    MqttHelper mqttHelper;

    String email;

    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mqttHelper = new MqttHelper();
        startMqtt();
        // Pega os dados de sessão
        setContentView(R.layout.activity_login);
        // Botão login
        Button fazer_login = findViewById(R.id.fazer_login);
        // Botão "esqueci senha"
        Button rec_senha = findViewById(R.id.button_rec_senha);
        // Pra pegar as informações da caixa de texto
        EditText email_login = findViewById(R.id.userLogin);
        EditText senha_login = findViewById(R.id.senhaLogin);
        Button ir_cadastro = findViewById(R.id.fazer_cadastro);

        // pra fazer algo quando clicamos no botão
        fazer_login.setOnClickListener(view -> {

            // atribuindo a "e-mail" o e-mail que eu escrever na tela
            email = email_login.getText().toString();
            // atribuindo a "senha" a senha que eu escrever na tela
            String senha = senha_login.getText().toString();
            if(email.isEmpty())
                Toast.makeText(LoginActivity.this, "Insira um e-mail", Toast.LENGTH_SHORT).show();
            if(senha.isEmpty())
                Toast.makeText(LoginActivity.this, "Insira uma senha", Toast.LENGTH_SHORT).show();
            if(!email.isEmpty() && !senha.isEmpty()) {
                // verificar se existe o login

                //mqttHelper.publish();
                mqttHelper.publish(email, "Smart_Farm/"+mqttHelper.getClientId()+"/Login/Email");
                mqttHelper.publish(senha, "Smart_Farm/"+mqttHelper.getClientId()+"/Login/Senha");

            }

        });
        // Recuperando a senha
        rec_senha.setOnClickListener(view -> {
            // Enviando um email para o usuário trocar a senha
            Intent recuperar = new Intent(LoginActivity.this, AlterarDadosEmailActivity.class);
            startActivity(recuperar);
        });

        ir_cadastro.setOnClickListener(view -> {
            // Enviando um email para o usuário trocar a senha
            Intent cadastro = new Intent(LoginActivity.this, CadastroActivity.class);
            startActivity(cadastro);
        });
    }

    /*
    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                System.out.println("MQTT OK");
            }
            @Override
            public void connectionLost(Throwable throwable) {
                // Aparece essa mensagem sempre que a conexão for perdida
                //Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }
            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                if(topic.equals("Smart_Farm/Login/Status")){
                    switch (mqttMessage.toString()){
                        case ("00"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                            break;
                        case ("11"):
                            Intent logado = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
                            startActivity(logado);
                            // Exclui essa tela ao sair para não guardar as info que pus nela
                            onRestart();
                            break;
                        default:
                            break;
                    }
                }
                if(topic.equals("Smart_Farm/RecSenha/Status")){
                    switch (mqttMessage.toString()){
                        case ("0"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            // CODAR AQUI
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
            mqttHelper.getMqttAndroidClient().publish(topic, message);
        } catch (UnsupportedEncodingException | MqttException e) {
            e.printStackTrace();
        }
    }
    */
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Login/Status")){
                    switch (mqttMessage.toString()){
                        case ("00"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                            break;
                        case ("11"):
                            mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/GetInfos/Telefone");

                            break;
                        default:
                            break;
                    }
                }
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Infos/Telefone")) {
                    String telefoneL = mqttMessage.toString();
                    Intent logado = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
                    logado.putExtra("telefoneUser", telefoneL);
                    logado.putExtra("emailUser", email);
                    startActivity(logado);
                    // Exclui essa tela ao sair para não guardar as info que pus nela
                    onRestart();
                }
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/RecSenha/Status")){
                    switch (mqttMessage.toString()){
                        case ("0"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                             // CODAR AQUI
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
}