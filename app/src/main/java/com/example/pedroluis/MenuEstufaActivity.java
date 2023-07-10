package com.example.pedroluis;

import static com.example.pedroluis.UsuarioActivity.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.content.SharedPreferences;

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

public class MenuEstufaActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    private MqttAndroidClient mqttAndroidClient;
    boolean auxParaPublicarUmaVez = true;


    String firstCheckMain = "Tgg";
    // Passando esse parâmrtro para as próximas telas

    public static final String SHARED_PREFS = "shared_prefs";
    String nome_estufa;

    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mqttHelper = new MqttHelper();
        JoaoMqtt();
        //fazendo com o cód olhe para tela de menu
        setContentView(R.layout.activity_menu);
        // Pega os dados de sessão
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        nome_estufa = sharedpreferences.getString("estufa", "");
        Bundle extras = getIntent().getExtras();



        // Representante dos botões do front-end para serem manipulados pelo back-end e enviados ao front-end
        Button config;

        // Procure pelo componente com id = "ph" na tela citada acima
        config = findViewById(R.id.button_config);

        // Farei o mesmo esquema repetidamente
        ImageButton config_image;
        config_image = findViewById(R.id.config);

        Button PH;
        PH = findViewById(R.id.button_ph);

        ImageButton image_ph;
        image_ph = findViewById(R.id.ph);

        Button umi_ar;
        umi_ar = findViewById(R.id.button_umi_ar);

        ImageButton umi_ar_image;
        umi_ar_image = findViewById(R.id.umi_ar);

        Button temp;
        temp = findViewById(R.id.button_temp);

        ImageButton temp_image;
        temp_image = findViewById(R.id.temp);

        Button umi_substrato;
        umi_substrato = findViewById(R.id.button_umi_substrato);

        ImageButton umi_substrato_image;
        umi_substrato_image = findViewById(R.id.umi_substrato);

        Button lumi;
        lumi = findViewById(R.id.button_lumi);

        ImageButton lumi_image;
        lumi_image = findViewById(R.id.lumi);


        Button sair;
        sair = findViewById(R.id.button_sair);

        // Condições caso eu clique nos botões
        config.setOnClickListener(v-> {
            // mudando para tela de config
            Intent mudar = new Intent(MenuEstufaActivity.this,ConfigEstufaActivity.class);
            publish(nome_estufa, "Smart_Farm/" + mqttHelper.getClientId() + "/GetEstufas/Dados");
            startActivity(mudar);
        });
        config_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,ConfigEstufaActivity.class);
            publish(nome_estufa, "Smart_Farm/" + mqttHelper.getClientId() + "/GetEstufas/Dados");
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        PH.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,PhActivity.class);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        image_ph.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,PhActivity.class);
            mudar.putExtra("estufa", nome_estufa);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_ar.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiArActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_ar_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiArActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        temp.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, TemperaturaActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        temp_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,TemperaturaActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        umi_substrato.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, UmiSoloActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_substrato_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiSoloActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        lumi.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, LuminosidadeActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        lumi_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, LuminosidadeActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        sair.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,MenuUsuarioActivity.class);

            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
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
