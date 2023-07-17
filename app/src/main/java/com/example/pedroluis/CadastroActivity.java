package com.example.pedroluis;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class CadastroActivity extends AppCompatActivity {
    @Override
    public void onBackPressed() {
    }
    private ProgressBar progressBar;
    MqttHelper mqttHelper;
    String email_cadastro;
    String senha_cadastro;
    String telefone_cadastro;
    String confirm_senha_cadastro;

    // Cria um objeto para gerenciamento de Sessão
    public static SharedPreferences sharedpreferences;

    // Chaves constantes para sharedpreferences
    public static final String SHARED_PREFS = "shared_prefs";
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        progressBar = findViewById(R.id.Loading_bar);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        mqttHelper  = new MqttHelper();
        startMqtt();

        // Pegando as informações das caixas texto
        EditText senha_c = findViewById(R.id.senhaCadastro);
        EditText senha_c_confirm = findViewById(R.id.confirmSenhaCadastro);
        EditText email_c = findViewById(R.id.emailCadastro);
        EditText telefone_c = findViewById(R.id.confirmTelefoneCadastro);

        Button realizar_Cadastro = findViewById(R.id.fazer_cadastro1);
        Button voltar_Cadastro = findViewById(R.id.voltar_cadastro1);

        realizar_Cadastro.setOnClickListener(view -> {
            senha_cadastro = senha_c.getText().toString();
            confirm_senha_cadastro = senha_c_confirm.getText().toString();
            email_cadastro = email_c.getText().toString();
            telefone_cadastro = telefone_c.getText().toString();
            new LoadDataTask().execute();
            // Verificando se a senha é igual a senha confirmada
            if(senha_cadastro.equals(confirm_senha_cadastro) && !senha_cadastro.isEmpty()) {
                // Aplicando REGEX no nome
                // Aplicando REGEX no email
                if (email_cadastro.matches("^[a-zA-Z0-9.]+@(gmail\\.com|hotmail\\.com|yahoo\\.com\\.br)$")) {
                    //mqttHelper.publish(telefone_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Telefone");
                    //mqttHelper.publish(senha_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Senha");
                    //mqttHelper.publish(email_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/E-mail");
                    mqttHelper.publish(telefone_cadastro+"/"+senha_cadastro+"/"+email_cadastro, "Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/DadosTSE");
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

        voltar_Cadastro.setOnClickListener(view ->{
            Intent voltar = new Intent(CadastroActivity.this, LoginActivity.class);
            startActivity(voltar);
        });
    }
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Antes de iniciar a tarefa, exibe a barra de progresso e oculta o botão
            progressBar.setVisibility(View.VISIBLE);

        }

        @Override
        protected Void doInBackground(Void... voids) {
            // Simulação de carregamento de dados
            try {
                Thread.sleep(3000); // Espera de 3 segundos (simulando carregamento)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            // Após o término da tarefa, oculta a barra de progresso e abre a próxima atividade
            progressBar.setVisibility(View.INVISIBLE);
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
                // Exibindo na tela os retornos do Banco de Dados
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Cadastro/Status")){
                    switch (mqttMessage.toString()){
                        case ("01"):
                            Toast.makeText(CadastroActivity.this, "Telefone ou Email já cadastrado", Toast.LENGTH_SHORT).show();
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
