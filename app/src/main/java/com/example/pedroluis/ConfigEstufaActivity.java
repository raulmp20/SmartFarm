package com.example.pedroluis;

import static com.example.pedroluis.UsuarioActivity.SHARED_PREFS;
import static com.example.pedroluis.UsuarioActivity.sharedpreferences;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.DialogInterface;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
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
    private TextView caixa_prodEstufa;
    String [] valores_separados;
    SwitchCompat botaoSwitch;
    String pos_spinner1;
    String valores_estufa;
    int switch_value;
    int spinner_value;

    boolean switch_bool;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startMqtt();
        setContentView(R.layout.activity_config_estufa);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        valores_estufa = sharedpreferences.getString("valores", "");

        //setando inicialmente valor do switch e spinner
        valores_separados = valores_estufa.split("/");
        switch_value = Integer.parseInt(valores_separados[0]);
        spinner_value = Integer.parseInt(valores_separados[1]);
        switch_bool = (switch_value != 0);


        // Pegando as informações das caixas texto
        EditText novo_nome_att;
        novo_nome_att = findViewById(R.id.estufa_novo_nome);
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        nome_estufa = sharedpreferences.getString("estufa", "");
        caixa_nomeEstufa = findViewById(R.id.estufa_nome_atual);
        caixa_prodEstufa = findViewById(R.id.estufa_prod_atual);
        caixa_nomeEstufa.setText(nome_estufa);
        botaoSwitch = (SwitchCompat) findViewById(R.id.switch2);
        botaoSwitch.setChecked(switch_bool);

        // Botão "salvar"
        Button salvar;
        salvar = findViewById(R.id.button_salvar_config);
        // Botão "voltar"
        Button voltar;
        voltar = findViewById(R.id.button_voltar_config);

        Spinner spinner = findViewById(R.id.plants_spinner);
        switch (spinner_value) {
            case 0:
                caixa_prodEstufa.setText("Alface");
                break;
            case 1:
                caixa_prodEstufa.setText("Pimenta");
                break;
            case 2:
                caixa_prodEstufa.setText("Morango");
                break;
        }

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
                pos_spinner1 = Integer.toString(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Alface");
        arrayList.add("Couve");
        arrayList.add("Morango");

        spinner.setSelection(spinner_value);
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);

        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        // Atualizando os dados
        salvar.setOnClickListener(new View.OnClickListener(){
            // Pegando as informações dos EditText e adicionando a uma string


            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmaConversa = new AlertDialog.Builder(ConfigEstufaActivity.this);
                confirmaConversa.setTitle("Alterar Dados");
                confirmaConversa.setMessage("Confirmas alterações na Estufas?");
                confirmaConversa.setCancelable(false);
                String nomeNovo = novo_nome_att.getText().toString();
                confirmaConversa.setPositiveButton("Confirmar Mudanças", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (!nomeNovo.isEmpty()) {
                            //A MAGICA É AQUI
                            mqttHelper.publish(nomeNovo+"/"+switchState1+"/"+pos_spinner1+"/"+nome_estufa, "Smart_Farm/"+mqttHelper.getClientId()+"/AtualizaEstufa/Dados");

                            //startActivity(salvar_info);
                        } else {
                            Toast.makeText(ConfigEstufaActivity.this, "Digite um novo nome", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                confirmaConversa.setNegativeButton("Cancelar", null);
                confirmaConversa.create().show();
            }
        });
        // Voltando ao menu
        voltar.setOnClickListener(v -> {
            // Mudando a tela para a tela menu
            Intent mudar = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);

            startActivity(mudar);
        });

    }

    /*private void JoaoMqtt() {
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

                    //subscribeToTopic();
                    try {
                        mqttAndroidClient.subscribe("Smart_Farm/"+mqttHelper.getClientId()+"/#", 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken asyncActionToken) {
                                Log.w("Mqtt", "Subscribed!");
                                mqttHelper.publish("lanna_olha_ai" , "Smart_Farm/" + mqttHelper.getClientId() + "/Estufa/Atualiza/Nome");
                                Toast.makeText(ConfigEstufaActivity.this, "testefoda", Toast.LENGTH_SHORT).show();

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
    }*/

    //Bloco que envia comandos para o broker

    private void startMqtt() {
        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
            }
            @Override
            public void connectionLost(Throwable throwable) {
                //Aparece essa mensagem sempre que a conexão for perdida
                //Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }
            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                // Exibindo na tela os retornos do Banco de Dados
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/AtualizaStatus/Status")){
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfigEstufaActivity.this, "Troca Não Efetuada", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):

                            break;

                    }
                }
                Intent mudar = new Intent(ConfigEstufaActivity.this, EstufasCadastradasActivity.class);
                startActivity(mudar);
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }

}