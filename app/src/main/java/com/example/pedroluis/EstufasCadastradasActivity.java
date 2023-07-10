package com.example.pedroluis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

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
import java.util.ArrayList;
import java.util.List;

public class EstufasCadastradasActivity extends AppCompatActivity {

    private MqttHelper mqttHelper;
    private MqttAndroidClient mqttAndroidClient;
    boolean auxParaPublicarUmaVez = true;

    public static SharedPreferences sharedpreferences;
    // Chaves constantes para sharedpreferences
    public static final String SHARED_PREFS = "shared_prefs";
    String message = "1";

    private ListView listView;

    private List<String> IdEstufaList;
    private ArrayAdapter<String> adapter;
    String emailUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_estufas_cadastradas);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        emailUser = sharedpreferences.getString("email", "");

        mqttHelper = new MqttHelper();

        IdEstufaList = new ArrayList<>();



        listView = findViewById(R.id.lista_estufas);
        Button nova_estufa = findViewById(R.id.button_novo);
        Button voltar = findViewById(R.id.button_voltar);

        JoaoMqtt();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Habilitar o click dos elementos da lista
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                Intent goInfo = new Intent(EstufasCadastradasActivity.this, MenuEstufaActivity.class);
                //envia o conteudo da notificacao através de um putExtra

                sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("estufa",listItem.toString());

                editor.apply();
                startActivity(goInfo);
            }
        });

        nova_estufa.setOnClickListener(view -> {
            Intent novo = new Intent(EstufasCadastradasActivity.this, QRCodeScannerActivity.class);

            startActivity(novo);
        });

        voltar.setOnClickListener(view -> {
            Intent back = new Intent(EstufasCadastradasActivity.this, MenuUsuarioActivity.class);

            startActivity(back);
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Estufas/User")) {
                    //Adiciona as notificações retornadas pelo broker a lista
                    IdEstufaList.add(mqttMessage.toString());

                    adapter = new ArrayAdapter<String>(EstufasCadastradasActivity.this,android.R.layout.simple_list_item_1,IdEstufaList);
                    listView.setAdapter(adapter);
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
                                Log.w("Mqtt", "Subscribed!");
                                if(auxParaPublicarUmaVez) {
                                    //Publica no topico toda vez que se entra na página.
                                    publish(emailUser, "Smart_Farm/" + mqttHelper.getClientId() + "/GetEstufas/User");
                                    auxParaPublicarUmaVez = false;
                                }
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
}
