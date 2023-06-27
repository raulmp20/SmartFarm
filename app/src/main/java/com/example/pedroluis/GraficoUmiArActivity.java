package com.example.pedroluis;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

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

public class GraficoUmiArActivity extends AppCompatActivity {

    MqttHelper mqttHelper = new MqttHelper();
    private MqttAndroidClient mqttAndroidClient;
    private Boolean auxParaPublicarUmaVez = true;
    String val_inst;

    Button graph_1D;
    Button graph_1S;
    Button graph_1M;

    String message = "1";

    BarChart barChart;

    float valor;

    ArrayList<BarEntry> barEntries = new ArrayList<>();

    int cont = 0;

    long tempo;
    long tempoAntes = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);

        barChart = findViewById(R.id.barchart);
        graph_1D = findViewById(R.id.button_graph_1D);
        graph_1M = findViewById(R.id.button_graph_1M);
        graph_1S = findViewById(R.id.button_graph_1S);

        JoaoMqtt();

        graph_1D.setOnClickListener(view -> {
            cont = 0;
            barEntries.clear();
            publish(message, "Smart_Farm/" + mqttHelper.getClientId() + "/Sensores/UmiAr/Grafico1D");
        });

        graph_1S.setOnClickListener(view -> {
            cont = 0;
            publish(message, "Smart_Farm/" + mqttHelper.getClientId() + "/Sensores/UmiAr/Grafico1S");
        });

        graph_1M.setOnClickListener(view -> {
            cont = 0;
            publish(message, "Smart_Farm/" + mqttHelper.getClientId() + "/Sensores/UmiAr/Grafico1M");
        });

        /*
        for (int i=1; i<10; i++){
            float value = (float) (i*10.0);
            BarEntry barEntry = new BarEntry(i, value);
            barEntries.add(barEntry);
        }
         */
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
                /*
                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/pH/Grafico1D")) {
                    val_inst = mqttMessage.toString();
                    valor = Float.parseFloat(val_inst);
                    BarEntry barEntry = new BarEntry(cont, valor);
                    barEntries.add(barEntry);
                    cont++;
                }
                 */
                /*
                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Temperatura/valorMedioUmaHora"))
                    mediaHora.setText(mqttMessage.toString());

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Temperatura/valorMedioUmDia"))
                    mediaDia.setText(mqttMessage.toString());

                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/Temperatura/Status"))
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
                 */


                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/MediasHoras")) {
                    val_inst = mqttMessage.toString();
                    valor = Float.parseFloat(val_inst);
                    BarEntry barEntry = new BarEntry(cont, valor);
                    barEntries.add(barEntry);
                    cont++;

                    BarDataSet barDataSet = new BarDataSet(barEntries, "pH");
                    barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    barDataSet.setDrawValues(false);
                    barChart.setData(new BarData(barDataSet));
                    barChart.animateY(5000);
                    barChart.getDescription().setText("Valores do pH");
                    barChart.getDescription().setTextColor(Color.BLUE);
                }


                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/MediasDias")) {
                    val_inst = mqttMessage.toString();
                    valor = Float.parseFloat(val_inst);
                    BarEntry barEntry = new BarEntry(cont, valor);
                    barEntries.add(barEntry);
                    cont++;
                }

                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/MediasSemanas")) {
                    val_inst = mqttMessage.toString();
                    valor = Float.parseFloat(val_inst);
                    BarEntry barEntry = new BarEntry(cont, valor);
                    barEntries.add(barEntry);
                    cont++;
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
                                publish(message, "Smart_Farm/"+mqttHelper.getClientId()+"/Sensores/UmiAr/Grafico1D");
                                auxParaPublicarUmaVez = false;
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

