package com.example.pedroluis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class PhActivity extends AppCompatActivity {
    MqttHelper mqttHelper;
    private MqttAndroidClient mqttAndroidClient;

    boolean firstCheckPh = true;

    Button atualizar;
    Button voltar;
    Button graficos;
    // Text Views
    TextView mediaHora;
    TextView mediaDia;
    TextView valorInstantaneo;
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

        setContentView(R.layout.activity_ph);
        startMqtt();

        // Pegando os valores mais recentes do BD
        if (firstCheckPh) {
            mqttHelper.publish(message, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/Info");
        }

        // Instanciando os botões
        atualizar = findViewById(R.id.Botao_atualizar_ph);
        voltar = findViewById(R.id.Botao_voltar_ph);
        graficos = findViewById(R.id.GraficoMedias_pH);

        // Instanciando texts view
        mediaHora = findViewById(R.id.media_hora_valor_ph);
        mediaDia = findViewById(R.id.media_dia_valor_ph);
        valorInstantaneo = findViewById(R.id.ult_oco_valor_ph);
        modulo = findViewById(R.id.valor_modulo_ph);

        // Dando informações iniciais a eles
        mediaHora.setText(info);
        valorInstantaneo.setText(info);
        mediaDia.setText(info);
        modulo.setText(info);

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela menu
            Intent mudar = new Intent(PhActivity.this,MenuEstufaActivity.class);
            startActivity(mudar);
        });
        graficos.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Menu de gráficos com as médias de pH", Toast.LENGTH_SHORT).show();
        });
    }

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                System.out.println("MQTT OK");
            }
            @Override
            public void connectionLost(Throwable throwable) {
                //Aparece essa mensagem sempre que a conexão for perdida
                Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }
            @SuppressLint("SetTextI18n")
            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                if (firstCheckPh) {
                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"Sensores/ph/valorInstantaneo"))
                        valorInstantaneo.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"Sensores/ph/valorMedioUmaHora"))
                        mediaHora.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"Sensores/ph/valorMedioUmDia"))
                        mediaDia.setText(mqttMessage.toString());

                    if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"Sensores/ph/Status"))
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
                    firstCheckPh = false;
                }
                atualizar.setOnClickListener(view -> {
                    mqttHelper.publish(message, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/Info");
                    Toast.makeText(PhActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/valorInstantaneo"))
                                valorInstantaneo.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/Status"))
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
    }

}
