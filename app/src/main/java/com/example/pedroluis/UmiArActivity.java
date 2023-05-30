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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;

public class UmiArActivity extends AppCompatActivity {
    MqttHelper mqttHelper;

    boolean firstCheckUmiAr= true;

    // Botão atualizar
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_umi_ar);
        startMqtt();

        // Pegando os valores mais recentes do BD
        if (firstCheckUmiAr) {
            mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/ph/Info");
        }

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
            Toast.makeText(getApplicationContext(), "Menu de gráficos com as médias de Umidade do Ar", Toast.LENGTH_SHORT).show();
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
                if (firstCheckUmiAr) {
                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorInstantaneo"))
                        valorInstantaneo.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorMedioUmaHora"))
                        mediaHora.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorMedioUmDia"))
                        mediaDia.setText(mqttMessage.toString());

                    if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/Status"))
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
                    firstCheckUmiAr = false;
                }
                atualizar.setOnClickListener(view -> {
                    mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/Info");
                    Toast.makeText(UmiArActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorInstantaneo"))
                                valorInstantaneo.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeAr/Status"))
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