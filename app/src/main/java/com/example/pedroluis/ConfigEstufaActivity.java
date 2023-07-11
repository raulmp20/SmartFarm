package com.example.pedroluis;

import static com.example.pedroluis.UsuarioActivity.SHARED_PREFS;
import static com.example.pedroluis.UsuarioActivity.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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

public class ConfigEstufaActivity extends AppCompatActivity {
    // Pra consultar o banco de dados
    private MqttAndroidClient mqttAndroidClient;

    // Acessar o banco de dados, var. aux. para banco de dados
    private MqttHelper mqttHelper;

    private String switchState1 = "0";
    String mensagem;
    String emailAntes;
    String telefoneAntes;
    String nome_estufa;
    private TextView caixa_nomeEstufa;
    SwitchCompat botaoSwitch;
    String valores_estufa;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mqttHelper = new MqttHelper();
        JoaoMqtt();
        setContentView(R.layout.activity_config_estufa);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        valores_estufa = sharedpreferences.getString("valores", "");

        Toast.makeText(ConfigEstufaActivity.this, valores_estufa, Toast.LENGTH_SHORT).show();

        // Pegando as informações das caixas texto
        EditText novo_nome_att;
        novo_nome_att = findViewById(R.id.estufa_novo_nome);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        nome_estufa = sharedpreferences.getString("estufa", "");
        caixa_nomeEstufa = findViewById(R.id.estufa_nome_atual);
        caixa_nomeEstufa.setText(nome_estufa);
        botaoSwitch = (SwitchCompat) findViewById(R.id.switch2);
        // Botão "salvar"
        Button salvar;
        salvar = findViewById(R.id.button_salvar_config);
        // Botão "voltar"
        Button voltar;
        voltar = findViewById(R.id.button_voltar_config);

        Spinner spinner = findViewById(R.id.plants_spinner);
        botaoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    switchState1 = "1";
                    Toast.makeText(ConfigEstufaActivity.this, "Ligado", Toast.LENGTH_SHORT).show();

                } else {
                    switchState1 = "0";
                    Toast.makeText(ConfigEstufaActivity.this, "desligado", Toast.LENGTH_SHORT).show();

                }

            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Alface");
        arrayList.add("Couve");
        arrayList.add("Morango");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);

        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        // Atualizando os dados
        salvar.setOnClickListener(v -> {

            // Pegando as informações dos EditText e adicionando a uma string
            String nomeNovo = novo_nome_att.getText().toString();

            // Verificando se a senha é igual a senha confirmada
            if (!nomeNovo.isEmpty()) {
                mqttHelper.publish(nomeNovo, "Smart_Farm/" + mqttHelper.getClientId() + "/Estufa/Atualiza/Nome");
            } else {
                Toast.makeText(ConfigEstufaActivity.this, "Digite um novo nome", Toast.LENGTH_SHORT).show();
            }


        });
        // Voltando ao menu
        voltar.setOnClickListener(v -> {
            // Mudando a tela para a tela menu
            Intent mudar = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);

            startActivity(mudar);
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
                    Toast.makeText(ConfigEstufaActivity.this, "DEU CERTO", Toast.LENGTH_SHORT).show();

                    //subscribeToTopic();
                    try {
                        mqttAndroidClient.subscribe("Smart_Farm/"+mqttHelper.getClientId()+"/#", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.w("Mqtt", "Subscribed!");
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
    //Bloco que envia comandos para o broker
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

    //Bloco que envia comandos para o broker

}