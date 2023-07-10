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

public class LuminosidadeActivity extends AppCompatActivity {
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




    // Adicinando uma informação inicial aos Text's View
    String info = "Em análise";
    String nome_estufa;
    String idEstufa;
    String message = "1";

    private Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_luminosidade);

        JoaoMqtt();

        Bundle extras = getIntent().getExtras();
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        idEstufa = sharedpreferences.getString("idEstufa", "");

        // Instanciando os botões
        atualizar = findViewById(R.id.Botao_atualizar_l);
        voltar = findViewById(R.id.Botao_voltar_l);
        graficos = findViewById(R.id.GraficoMedias_Lumi);

        // Instanciando texts view
        mediaHora = findViewById(R.id.media_hora_valor_lumi);
        mediaDia = findViewById(R.id.media_dia_valor_lumi);
        valorInstantaneo = findViewById(R.id.ult_oco_valor_lumi);
        modulo = findViewById(R.id.valor_modulo_lumi);

        // Dando informações iniciais a eles
        mediaHora.setText(info);
        valorInstantaneo.setText(info);
        mediaDia.setText(info);
        modulo.setText(info);

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela menu
            Intent mudar = new Intent(LuminosidadeActivity.this,MenuEstufaActivity.class);

            startActivity(mudar);
        });
        graficos.setOnClickListener(v->{
            Intent intent = new Intent(LuminosidadeActivity.this, GraficoLumiActivity.class);
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

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/ValorInstantaneo")) {
                    val_inst = mqttMessage.toString();
                    valorInstantaneo.setText(val_inst);
                }
                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/valorMedioUmaHora"))
                    mediaHora.setText(mqttMessage.toString());

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/valorMedioUmDia"))
                    mediaDia.setText(mqttMessage.toString());

                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/Status"))
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
                    publish(idEstufa, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/Info");
                    Toast.makeText(LuminosidadeActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/valorInstantaneo"))
                                valorInstantaneo.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/Status"))
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
                                    publish(idEstufa, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Luminosidade/Info");
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