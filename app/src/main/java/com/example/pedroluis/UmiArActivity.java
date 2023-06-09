package com.example.pedroluis;

import static com.example.pedroluis.UsuarioActivity.SHARED_PREFS;
import static com.example.pedroluis.UsuarioActivity.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
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

public class UmiArActivity extends AppCompatActivity {
    MqttHelper mqttHelper = new MqttHelper();
    private MqttAndroidClient mqttAndroidClient;
    private Boolean auxParaPublicarUmaVez = true;

    String emailAntes;
    String telefoneAntes;

    Button atualizar;
    Button voltar;
    Button graficos;
    // Text Views
    TextView mediaHora;
    TextView mediaDia;
    TextView valorInstantaneo;
    String val_inst;
    TextView modulo;
    // Variáveis para controle de tempo
    long tempo;
    long tempoAntes = 0;

    String idEstufa;
    String nome_estufa;
    // Adicinando uma informação inicial aos Text's View
    String info = "Em análise";

    String message = "1";

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_umi_ar);

        JoaoMqtt();

        Bundle extras = getIntent().getExtras();
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        idEstufa = sharedpreferences.getString("idEstufa", "");

        // Instanciando os botões
        atualizar = findViewById(R.id.Botao_atualizar_umi_ar);
        voltar = findViewById(R.id.Botao_voltar_umi_ar);
        graficos = findViewById(R.id.GraficoMedias_umi_ar);

        // Instanciando texts view
        mediaHora = findViewById(R.id.media_hora_valor_umi_ar);
        mediaDia = findViewById(R.id.media_dia_valor_umi_ar);
        valorInstantaneo = findViewById(R.id.ult_oco_valor_umi_ar);
        modulo = findViewById(R.id.valor_modulo_umi_ar);

        // Dando informações iniciais a eles
        mediaHora.setText(info);
        valorInstantaneo.setText(info);
        mediaDia.setText(info);
        modulo.setText(info);

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela menu
            Intent mudar = new Intent(UmiArActivity.this,MenuEstufaActivity.class);

            startActivity(mudar);
        });
        graficos.setOnClickListener(v->{
            Intent intent_graphUmiAr = new Intent(UmiArActivity.this, GraficoUmiArActivity.class);
            startActivity(intent_graphUmiAr);
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

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/ValorInstantaneo")) {
                    val_inst = mqttMessage.toString();
                    valorInstantaneo.setText(val_inst);
                }
                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/valorMedioUmaHora"))
                    mediaHora.setText(mqttMessage.toString());

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/valorMedioUmDia"))
                    mediaDia.setText(mqttMessage.toString());

                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/Status"))
                    switch (mqttMessage.toString()){
                        case("1"):
                            modulo.setText("Em funcionamento");
                            break;
                        case("0"):
                            modulo.setText("Não está funcionando");
                            break;
                        default:
                            break;
                    }

                atualizar.setOnClickListener(view -> {
                    publish(idEstufa, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/Info");
                    Toast.makeText(UmiArActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/valorInstantaneo"))
                                valorInstantaneo.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/Status"))
                                switch (mqttMessage.toString()){
                                    case("1"):
                                        modulo.setText("Em funcionamento");
                                        break;
                                    case("0"):
                                        modulo.setText("Não está funcionando");
                                        break;
                                    default:
                                        break;
                                }
                            break;
                        }
                    }
                });
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
                                if(auxParaPublicarUmaVez) {
                                    publish(idEstufa, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/Info");
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