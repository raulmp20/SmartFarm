package com.example.pedroluis;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
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
import android.widget.Switch;
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
    private String switchState = "0";
    SwitchCompat botaoSwitch1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_estufas);
        botaoSwitch1 = (SwitchCompat) findViewById(R.id.switch1);

        botaoSwitch1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchState = "1";
                    Toast.makeText(CadastroEstufaActivity.this, "Ligado", Toast.LENGTH_SHORT).show();
                }else{
                    switchState = "0";
                    Toast.makeText(CadastroEstufaActivity.this, "desligado", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /*try{ //garantindo que escaneamos o qr code
            Bundle extra = getIntent().getExtras();
            ID = extra.getString("ID");
            Toast.makeText(this, "ID: "+ID, Toast.LENGTH_SHORT).show();
            scaneado = true;
        }catch (Exception e){
            System.out.println(e);
        }*/

        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("product1");
        arrayList.add("product2");
        arrayList.add("product3");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);
        Spinner spinner = findViewById(R.id.Cadastro_Estufas_spinner);
        ;

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String item = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);


        //Boolean switchState = botaoSwitch1.isChecked();
        //switch_value = Boolean.toString(switchState);

        Button escanear = findViewById(R.id.button_escanear);
        SharedPreferences sharedPreferences = getSharedPreferences("studio.harpreet.sampleproject", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();
        EditText nome_estufa = findViewById(R.id.estufa_nome);

        escanear.setOnClickListener(new View.OnClickListener() {
            String estufa = nome_estufa.getText().toString();
            Intent intent = new Intent(CadastroEstufaActivity.this, QRCodeScannerActivity.class);

            public void onClick(View view) {
                String item_spinner;
                item_spinner = spinner.getSelectedItem().toString();

                if(estufa.isEmpty())
                    Toast.makeText(CadastroEstufaActivity.this, "PREENCHA TODOS DADOS", Toast.LENGTH_SHORT).show();
                else
                {
                    intent.putExtra("switchState", switchState);
                    intent.putExtra("nomeEstufa", estufa);
                    intent.putExtra("spinnerValue", item_spinner);
                    startActivity(intent);
                }

            }


        });
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