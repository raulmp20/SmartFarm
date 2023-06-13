package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.ArrayList;

public class CadastroEstufaActivity extends AppCompatActivity {

    //MqttHelper mqttHelper;

    @Override
    public void onBackPressed() {
    }



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        //LOGICA PARA SPINNER
        //final String[] item_spinner = new String[1];
        Spinner spinner = findViewById(R.id.plantsCadastro_spinner);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position1, long l1) {
                String item = adapterView.getItemAtPosition(position1).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("Alface");
        arrayList.add("Couve");
        arrayList.add("Morango");
        ArrayAdapter<String> adapter1 =
                new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, arrayList);

        adapter1.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter1);

        EditText nome_estufa = findViewById(R.id.estufa_nome);

        setContentView(R.layout.activity_cadastro_estufas);
        Button escanear = findViewById(R.id.button_escanear);


        escanear.setOnClickListener(view -> {
            String estufa = nome_estufa.getText().toString();
            if(estufa.isEmpty())
                Toast.makeText(CadastroEstufaActivity.this, "PREENCHA TODOS OS DADOS PARA CONTINUAR", Toast.LENGTH_SHORT).show();
            else {
                Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
                //intent.putExtra("itemSelecionado", item_spinner[0]);
                intent.putExtra("nomeEstufa", estufa);
                startActivity(intent);
            }
        });

        Button voltar = findViewById(R.id.button_voltar);

        voltar.setOnClickListener(view ->{
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