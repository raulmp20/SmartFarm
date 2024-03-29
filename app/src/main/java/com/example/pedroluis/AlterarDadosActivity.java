package com.example.pedroluis;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AlterarDadosActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    private ProgressBar progressBar1;
    String emailAntes;
    String telefoneAntes;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_dados);
        progressBar1 = findViewById(R.id.Loading_bar1);

        //Email anterior é usado para que seja enviado o email p/troca de dados
        Bundle extras = getIntent().getExtras();
        emailAntes = extras.getString("emailA");

        startMqtt();

        EditText email_alt = findViewById(R.id.Email_box_alt);
        EditText telefone_alt = findViewById(R.id.Numero_box_alt);

        Button trocaDadosconfirm = findViewById(R.id.button_salvar_dados);

        // pra fazer algo quando clicamos no botão
        trocaDadosconfirm.setOnClickListener(view -> {
                    new LoadDataTask().execute();
                    // atribuindo a "e-mail" o e-mail que eu escrever na tela
                    String email = email_alt.getText().toString();
                    // atribuindo a "senha" a senha que eu escrever na tela
                    String telefone = telefone_alt.getText().toString();
                    if (email.isEmpty())
                        Toast.makeText(AlterarDadosActivity.this, "Insira um e-mail", Toast.LENGTH_SHORT).show();
                    if (telefone.isEmpty())
                        Toast.makeText(AlterarDadosActivity.this, "Insira um numero de telefone", Toast.LENGTH_SHORT).show();
                    if (!email.isEmpty() && !telefone.isEmpty()) {
                        // verificar se existe o login
                        System.out.println(email);
                        //if (email.matches("^[a-zA-Z0-9.]+@(gmail\\.com|hotmail\\.com|yahoo\\.com\\.br)$")) {
                            mqttHelper.publish(telefone, "Smart_Farm/" + mqttHelper.getClientId() + "/AltDados/Telefone");
                            mqttHelper.publish(emailAntes, "Smart_Farm/" + mqttHelper.getClientId() + "/AltDados/E-mail_Antes");
                            mqttHelper.publish(email, "Smart_Farm/" + mqttHelper.getClientId() + "/AltDados/E-mail");
                        //} else
                            //Toast.makeText(AlterarDadosActivity.this, "Coloque um e-mail válido", Toast.LENGTH_SHORT).show();


                    }
                });


        Button voltar = findViewById(R.id.button_voltar_trocadados);


        voltar.setOnClickListener(view ->{
            Intent intent = new Intent(AlterarDadosActivity.this, MenuUsuarioActivity.class);

            startActivity(intent);
        });
    }
    private class LoadDataTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            // Antes de iniciar a tarefa, exibe a barra de progresso e oculta o botão
            progressBar1.setVisibility(View.VISIBLE);

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
            progressBar1.setVisibility(View.INVISIBLE);
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/AltDados/Status")){
                    switch (mqttMessage.toString()){
                        case ("01"):
                            Toast.makeText(AlterarDadosActivity.this, "Telefone já cadastrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(AlterarDadosActivity.this, "E-mail já cadastrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("0"):
                            Toast.makeText(AlterarDadosActivity.this, "Erro, tente novamente", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(AlterarDadosActivity.this, "Primeiro Passo Realizado", Toast.LENGTH_SHORT).show();
                            Intent dados_alt = new Intent(AlterarDadosActivity.this, ConfirmTokenAltDadosActivity.class);
                            startActivity(dados_alt);
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
