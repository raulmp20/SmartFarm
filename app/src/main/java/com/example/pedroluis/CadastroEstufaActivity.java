package com.example.pedroluis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

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
import java.util.ArrayList;

public class CadastroEstufaActivity extends AppCompatActivity {

    // Cria um objeto para gerenciamento de Sessão
    public static SharedPreferences sharedpreferences;
    // Chaves constantes para sharedpreferences
    public static final String SHARED_PREFS = "shared_prefs";

    @Override
    public void onBackPressed() {
    }
    private MqttAndroidClient mqttAndroidClient;
    private MqttHelper mqttHelper;
    private String switchState = "0";
    SwitchCompat botaoSwitch1;
    String pos_spinner;
    String emailUser;
    String item_spinner;
    String code;
    String estufa;
    int code2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Pega os dados de sessão
        // Pega os dados de sessão
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailUser = sharedpreferences.getString("email", "");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estufas);
        botaoSwitch1 = (SwitchCompat) findViewById(R.id.switch1);
        mqttHelper = new MqttHelper();


        Bundle extras = getIntent().getExtras();
        if(extras.getString("code") != null){
            //Aqui farei a lógica que permitirá Qr Codes com a chave de permissão
            code = extras.getString("code");
            code2 = Integer.valueOf(code);
        }





        botaoSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchState = "1";
                }else{
                    switchState = "0";
                }
            }
        });



        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Alface");
        arrayList.add("Couve");
        arrayList.add("Morango");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        Spinner spinner = findViewById(R.id.Cadastro_Estufas_spinner);
        ;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
                pos_spinner = Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        //Boolean switchState = botaoSwitch1.isChecked();
        //switch_value = Boolean.toString(switchState);

        Button escanear = findViewById(R.id.button_escanear);
        SharedPreferences sharedPreferences = getSharedPreferences("studio.harpreet.sampleproject", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        EditText nome_estufa = findViewById(R.id.estufa_nome);

        escanear.setOnClickListener(new View.OnClickListener() {


            public void onClick(View view) {

                item_spinner = spinner.getSelectedItem().toString();
                estufa = nome_estufa.getText().toString();
                if(estufa.isEmpty())
                    Toast.makeText(CadastroEstufaActivity.this, "PREENCHA TODOS DADOS", Toast.LENGTH_SHORT).show();
                else
                {

                    /*intent.putExtra("switchState", switchState);
                    intent.putExtra("nomeEstufa", estufa);
                    intent.putExtra("spinnerValue", item_spinner);*/
                    JoaoMqtt();

                }

            }


        });
        Button voltar = findViewById(R.id.button_voltar);
        voltar.setOnClickListener(view -> {
            Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/pesquisa/Status")){
                    switch (mqttMessage.toString()){
                        case ("1"):
                            Toast.makeText(CadastroEstufaActivity.this, "Uma Estufa com este nome já está registrada!", Toast.LENGTH_SHORT).show();
                            break;
                        case ("0"):
                            publish(code2+"/"+estufa+"/"+switchState+"/"+emailUser+"/"+pos_spinner, "Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/dados");
                            Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);

                            startActivity(intent);
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
                                publish(estufa+"/"+emailUser, "Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/pesquisaInicial");

                                //publish(code2+"/"+estufa+"/"+switchState+"/"+emailUser+"/"+pos_spinner, "Smart_Farm/"+mqttHelper.getClientId()+"/CadastroEstufa/dados");
                                //Intent intent = new Intent(CadastroEstufaActivity.this,EstufasCadastradasActivity.class);

                                //,startActivity(intent);
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
                /*
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

                 */
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