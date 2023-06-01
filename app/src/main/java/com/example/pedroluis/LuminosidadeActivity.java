package com.example.pedroluis;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
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

public class LuminosidadeActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    private MqttAndroidClient mqttAndroidClient;
    private Boolean auxParaPublicarUmaVez = true;
    boolean firstCheckLum = true;

    // Botão atualizar
    Button atualizar;
    Button voltar;
    Button graficos;
    // Text Views
    TextView mediaHora;
    TextView mediaDia;
    TextView valorInstantaneio;
    TextView modulo;

    // Variáveis para controle de tempo
    long tempo;
    long tempoAntes = 0;

    // Adicinando uma informação inicial aos Text's View
    String info = "Em análise";

    String message = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_luminosidade);
        JoaoMqtt();

        // Instanciando os botões
        atualizar = findViewById(R.id.Botao_atualizar_l);
        voltar = findViewById(R.id.Botao_voltar_l);
        graficos = findViewById(R.id.GraficoMedias_Lumi);

        // Instanciando texts view
        mediaHora = findViewById(R.id.media_hora_valor_lumi);
        mediaDia = findViewById(R.id.media_dia_valor_lumi);
        valorInstantaneio = findViewById(R.id.ult_oco_valor_lumi);
        modulo = findViewById(R.id.valor_modulo_lumi);

        // Dando informações iniciais a eles
        mediaHora.setText(info);
        valorInstantaneio.setText(info);
        mediaDia.setText(info);
        modulo.setText(info);

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela menu
            Intent mudar = new Intent(LuminosidadeActivity.this,MenuEstufaActivity.class);
            startActivity(mudar);
        });
        graficos.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Menu de gráficos com as médias de Luminosidade", Toast.LENGTH_SHORT).show();
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
                if (firstCheckLum) {
                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorInstantaneo"))
                        valorInstantaneio.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorMedioUmaHora"))
                        mediaHora.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorMedioUmDia"))
                        mediaDia.setText(mqttMessage.toString());

                    if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/Status"))
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
                    firstCheckLum = false;
                }
                atualizar.setOnClickListener(view -> {
                    mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/Info");
                    Toast.makeText(LuminosidadeActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorInstantaneo"))
                                valorInstantaneio.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/luminosidade/Status"))
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
                                    publish(message, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Lumi/Info");
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