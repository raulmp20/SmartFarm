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

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class UmiSoloActivity extends AppCompatActivity {
    MqttHelper mqttHelper;

    boolean firstCheckUmiSolo= true;

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

        setContentView(R.layout.activity_umi_solo);
        startMqtt();

        // Pegando os valores mais recentes do BD
        if (firstCheckUmiSolo) {
            mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiSolo/Info");
        }

        // Instanciando os botões
        atualizar = findViewById(R.id.Botao_atualizar_umi_solo);
        voltar = findViewById(R.id.Botao_voltar_umi_solo);
        graficos = findViewById(R.id.GraficoMedias_umi_solo);

        // Instanciando texts view
        mediaHora = findViewById(R.id.media_hora_valor_umi_solo);
        mediaDia = findViewById(R.id.media_dia_valor_umi_solo);
        valorInstantaneo = findViewById(R.id.ult_oco_valor_umi_solo);
        modulo = findViewById(R.id.valor_modulo_umi_solo);

        // Dando informações iniciais a eles
        mediaHora.setText(info);
        valorInstantaneo.setText(info);
        mediaDia.setText(info);
        modulo.setText(info);

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela menu
            Intent mudar = new Intent(UmiSoloActivity.this,MenuEstufaActivity.class);
            startActivity(mudar);
        });
        graficos.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Menu de gráficos com as médias de Umidade do Substrato", Toast.LENGTH_SHORT).show();
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
                if (firstCheckUmiSolo) {
                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorInstantaneo"))
                        valorInstantaneo.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorMedioUmaHora"))
                        mediaHora.setText(mqttMessage.toString());

                    if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorMedioUmDia"))
                        mediaDia.setText(mqttMessage.toString());

                    if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/Status"))
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
                    firstCheckUmiSolo = false;
                }
                atualizar.setOnClickListener(view -> {
                    mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/Info");
                    Toast.makeText(UmiSoloActivity.this, "Aguarde as leituras", Toast.LENGTH_SHORT).show();
                    while (true) {
                        tempo = System.currentTimeMillis();
                        if (tempo - tempoAntes >= 1000) {
                            tempoAntes = tempo;
                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorInstantaneo"))
                                valorInstantaneo.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorMedioUmaHora"))
                                mediaHora.setText(mqttMessage.toString());

                            if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/valorMedioUmDia"))
                                mediaDia.setText(mqttMessage.toString());

                            if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/umidadeSolo/Status"))
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
