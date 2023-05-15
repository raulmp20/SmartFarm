package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import org.eclipse.paho.android.service.MqttAndroidClient;

import java.util.ArrayList;
import java.util.List;

public class EstufasCadastradasActivity extends AppCompatActivity {

    private MqttHelper mqttHelper;
    private MqttAndroidClient mqttAndroidClient;
    boolean auxParaPublicarUmaVez = true;

    private ListView listView;
    private List<String> IdSolicitationList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_estufas_cadastradas);

        mqttHelper = new MqttHelper();

        IdSolicitationList = new ArrayList<>();


        listView = findViewById(R.id.lista_estufas);
        Button nova_estufa = findViewById(R.id.button_novo);
        Button voltar = findViewById(R.id.button_voltar);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            //Habilitar o click dos elementos da lista
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Object listItem = listView.getItemAtPosition(position);
                Intent goInfo = new Intent(EstufasCadastradasActivity.this, MenuEstufaActivity.class);
                //envia o conteudo da notificacao através de um putExtra
                goInfo.putExtra("estufa", listItem.toString());
                startActivity(goInfo);
            }
        });

        nova_estufa.setOnClickListener(view -> {
            Intent novo = new Intent(EstufasCadastradasActivity.this, CadastroEstufaActivity.class);
            startActivity(novo);
        });

        voltar.setOnClickListener(view -> {
            Intent back = new Intent(EstufasCadastradasActivity.this, MenuUsuarioActivity.class);
            startActivity(back);
        });
    }
}
