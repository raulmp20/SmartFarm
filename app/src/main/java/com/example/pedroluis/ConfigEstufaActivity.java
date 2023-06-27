package com.example.pedroluis;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.content.Intent;
import android.hardware.camera2.CameraManager;
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
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class ConfigEstufaActivity extends AppCompatActivity {
    // Pra consultar o banco de dados
    public MqttAndroidClient mqttAndroidClient;
    // Acessar o banco de dados, var. aux. para banco de dados
    MqttHelper mqttHelper;

    String emailAntes;
    String telefoneAntes;
    String notif;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_config_estufa);
        startMqtt();

        Bundle extras = getIntent().getExtras();
        emailAntes = extras.getString("emailA");
        telefoneAntes = extras.getString("telefoneA");

        // Pegando as informações das caixas texto
        EditText novo_nome_att;
        novo_nome_att = findViewById(R.id.estufa_novo_nome);

        SwitchCompat botaoSwitch;
        botaoSwitch = findViewById(R.id.switch2);
        // Botão "salvar"
        Button salvar;
        salvar = findViewById(R.id.button_salvar_config);
        // Botão "voltar"
        Button voltar;
        voltar = findViewById(R.id.button_voltar_config);

        Spinner spinner = findViewById(R.id.plants_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Alface");
        arrayList.add("Couve");
        arrayList.add("Morango");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);

        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        botaoSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Toast.makeText(ConfigEstufaActivity.this, "ON", Toast.LENGTH_SHORT).show();
                    notif = "1";
                }else{
                    Toast.makeText(ConfigEstufaActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                    notif = "0";
                    mqttHelper.publish("0", "Smart_Farm/"+mqttHelper.getClientId()+"/Estufa/Atualiza/Notf");
                }
            }
        });

        // Atualizando os dados
        salvar.setOnClickListener(v -> {

            // Pegando as informações dos EditText e adicionando a uma string
            String nomeNovo = novo_nome_att.getText().toString();

            // Verificando se a senha é igual a senha confirmada
            if (!nomeNovo.isEmpty()) {
                mqttHelper.publish(nomeNovo, "Smart_Farm/"+mqttHelper.getClientId()+"/Estufa/Atualiza/Nome");
            } else {
                Toast.makeText(ConfigEstufaActivity.this, "Digite um novo nome", Toast.LENGTH_SHORT).show();
            }
            if (notif == "1"){
                mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/Estufa/Atualiza/Notf");
            }
            else mqttHelper.publish("0", "Smart_Farm/"+mqttHelper.getClientId()+"/Estufa/Atualiza/Notf");

        });
        // Voltando ao menu
        voltar.setOnClickListener(v -> {
            // Mudando a tela para a tela menu
            Intent mudar = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);
            mudar.putExtra("emailA",emailAntes);
            mudar.putExtra("telefoneA",telefoneAntes);
            startActivity(mudar);
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
                // Aparece essa mensagem sempre que a conexão for perdida
                Toast.makeText(getApplicationContext(), "Conexão perdida", Toast.LENGTH_SHORT).show();
            }

            @Override
            // messageArrived é uma função que é chamada toda vez que o cliente MQTT recebe uma mensagem
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Debug", mqttMessage.toString());
                if (topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Estufa/Atualiza/Status")) {
                    switch (mqttMessage.toString()) {
                        // TROCAR: 00 -> E-mail não encontrado, 01 -> Pin inválido, 11 -> Senha atualizada
                        case ("00"):
                            Toast.makeText(ConfigEstufaActivity.this, "Falha ao atualizar", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(ConfigEstufaActivity.this, "Nome já existe", Toast.LENGTH_SHORT).show();
                            break;
                        case ("11"):
                            Toast.makeText(ConfigEstufaActivity.this, "Nome atualizado", Toast.LENGTH_SHORT).show();
                            Intent saveNome = new Intent(ConfigEstufaActivity.this, MenuEstufaActivity.class);
                            startActivity(saveNome);
                            break;
                        default:
                            break;
                    }
                }
                if(topic.equals("Smart_Farm/Atualiza/StatusPin")) {
                    switch (mqttMessage.toString()) {
                        case ("0"):
                            Toast.makeText(ConfigEstufaActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            Toast.makeText(ConfigEstufaActivity.this, "Pin Enviado", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            break;
                    }
                }
            }
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
            }
        });
    }

    //Bloco que envia comandos para o broker

}