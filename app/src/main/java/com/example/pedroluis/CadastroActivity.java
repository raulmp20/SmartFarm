package com.example.pedroluis;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class CadastroActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    String email_cadastro;

    // Cria um objeto para gerenciamento de Sessão
    public static SharedPreferences sharedpreferences;

    // Chaves constantes para sharedpreferences
    public static final String SHARED_PREFS = "shared_prefs";
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        mqttHelper  = new MqttHelper();
        startMqtt();
        // Pegando as informações das caixas texto
        EditText senha_c = findViewById(R.id.senhaCadastro);
        EditText senha_c_confirm = findViewById(R.id.confirmSenhaCadastro);
        EditText email_c = findViewById(R.id.emailCadastro);
        EditText telefone_c = findViewById(R.id.confirmTelefoneCadastro);

        Button realizar_Cadastro = findViewById(R.id.fazer_cadastro1);


        realizar_Cadastro.setOnClickListener(view -> {
            String senha_cadastro = senha_c.getText().toString();
            String confirm_senha_cadastro = senha_c_confirm.getText().toString();
            email_cadastro = email_c.getText().toString();
            String telefone_cadastro = telefone_c.getText().toString();

            // Verificando se a senha é igual a senha confirmada
            if(senha_cadastro.equals(confirm_senha_cadastro) && !senha_cadastro.isEmpty()) {
                // Aplicando REGEX no nome
                // Aplicando REGEX no email
                if (email_cadastro.matches("^[a-zA-Z0-9.]+@(gmail\\.com|hotmail\\.com|yahoo\\.com\\.br)$")) {
                    mqttHelper.publish(telefone_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Telefone");
                    mqttHelper.publish(senha_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Senha");
                    mqttHelper.publish(email_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/E-mail");
                }
                else if (!email_cadastro.isEmpty())
                    Toast.makeText(CadastroActivity.this, "Coloque um e-mail válido", Toast.LENGTH_SHORT).show();

            } else if (!senha_cadastro.equals(confirm_senha_cadastro)){
                Toast.makeText(CadastroActivity.this, "As senhas não se coincidem", Toast.LENGTH_SHORT).show();
            }
            if(telefone_cadastro.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Insira um numero de telefone", Toast.LENGTH_SHORT).show();
            }
            if(senha_cadastro.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Insira uma senha", Toast.LENGTH_SHORT).show();
            }
            if(confirm_senha_cadastro.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Insira uma confirmação de senha", Toast.LENGTH_SHORT).show();
            }
            if (email_cadastro.isEmpty()) {
                Toast.makeText(CadastroActivity.this, "Insira um e-mail", Toast.LENGTH_SHORT).show();
            }


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
                // Exibindo na tela os retornos do Banco de Dados
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Status")){
                    switch (mqttMessage.toString()){
                        case ("01"):
                            Toast.makeText(CadastroActivity.this, "Telefone já cadastrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(CadastroActivity.this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("0"):
                            Toast.makeText(CadastroActivity.this, "Erro no cadastro, tente novamente", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(CadastroActivity.this, "Primeiro Passo Realizado", Toast.LENGTH_SHORT).show();
                            Intent cadastrado = new Intent(CadastroActivity.this, ConfirmTokenCadastroActivity.class);
                            cadastrado.putExtra("emailC", email_cadastro);
                            startActivity(cadastrado);
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
