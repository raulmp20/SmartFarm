package com.example.pedroluis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

public class CadastroEstufaActivity extends AppCompatActivity {

    //MqttHelper mqttHelper;

    @Override
    public void onBackPressed() {
    }


    SwitchCompat botaoSwitch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estufas);

        /*try{ //garantindo que escaneamos o qr code
            Bundle extra = getIntent().getExtras();
            ID = extra.getString("ID");
            Toast.makeText(this, "ID: "+ID, Toast.LENGTH_SHORT).show();
            scaneado = true;
        }catch (Exception e){
            System.out.println(e);
        }*/
        /*Spinner spinner1 = findViewById(R.id.plantsCadastro_spinner);;

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView1, View view1, int position1, long l1) {
                String item1 = adapterView1.getItemAtPosition(position1).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView1) {

            }
        });


        ArrayList<String> arrayList1 = new ArrayList<>();
        arrayList1.add("product1");
        arrayList1.add("product2");
        arrayList1.add("product3");
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList1);

        adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner1.setAdapter(adapter1);*/

        EditText nome_estufa = findViewById(R.id.estufa_nome);

        botaoSwitch1 = findViewById(R.id.switch1);
        setContentView(R.layout.activity_cadastro_estufas);
        Button escanear = findViewById(R.id.button_escanear);
        SharedPreferences sharedPreferences = getSharedPreferences("studio.harpreet.sampleproject", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        botaoSwitch1.setChecked(sharedPreferences.getBoolean("switch", true));


        escanear.setOnClickListener(new View.OnClickListener() {
            String estufa = nome_estufa.getText().toString();

            public void onClick (View view){
                if (botaoSwitch1.isChecked()) {
                    editor.putBoolean("switch", true);
                    editor.apply();
                    Toast.makeText(CadastroEstufaActivity.this, "1", Toast.LENGTH_SHORT).show();
                }
                if (!botaoSwitch1.isChecked()) {
                    editor.putBoolean("switch", false);
                    editor.apply();
                    Toast.makeText(CadastroEstufaActivity.this, "0", Toast.LENGTH_SHORT).show();
                }
                editor.commit();
                Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
                Toast.makeText(CadastroEstufaActivity.this, "Configurações salvas", Toast.LENGTH_SHORT).show();
                //intent.putExtra("nomeEstufa", estufa);
                // startActivity(intent);

            }});

                    Button voltar = findViewById(R.id.button_voltar);

                    voltar.setOnClickListener(view -> {
                        Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
                        startActivity(intent);
                    });
                }

    /*private void startMqtt() {
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
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Login/Status")){
                    switch (mqttMessage.toString()){
                        case ("00"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("10"):
                            Toast.makeText(LoginActivity.this, "Senha incorreta", Toast.LENGTH_SHORT).show();
                            break;
                        case ("11"):
                            mqttHelper.publish("1", "Smart_Farm/"+mqttHelper.getClientId()+"/GetInfos/Telefone");

                            break;
                        default:
                            break;
                    }
                }
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/Infos/Telefone")) {
                    String telefoneL = mqttMessage.toString();
                    Intent logado = new Intent(LoginActivity.this, MenuUsuarioActivity.class);
                    logado.putExtra("telefoneUser", telefoneL);
                    logado.putExtra("emailUser", email);
                    startActivity(logado);
                    // Exclui essa tela ao sair para não guardar as info que pus nela
                    onRestart();
                }
                if(topic.equals("Smart_Farm/"+mqttHelper.getClientId()+"/RecSenha/Status")){
                    switch (mqttMessage.toString()){
                        case ("0"):
                            Toast.makeText(LoginActivity.this, "E-mail não encontrado", Toast.LENGTH_SHORT).show();
                            break;
                        case ("1"):
                            // CODAR AQUI
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
    }*/
    }