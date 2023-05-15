package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class FornecedoresActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    Button buttonSubstrato;
    Button buttonNutrientes;
    Button buttonPlantas;
    Button buttonSementes;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_find_products);


        Button voltar = findViewById(R.id.button_voltar_products);
        buttonSubstrato = findViewById(R.id.Substrato);
        buttonNutrientes = findViewById(R.id.Nutrientes);
        buttonPlantas = findViewById(R.id.Plantas);
        buttonSementes = findViewById(R.id.Sementes);

        buttonSubstrato.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de substrato", Toast.LENGTH_SHORT).show();
        });

        buttonNutrientes.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de nutrientes", Toast.LENGTH_SHORT).show();
        });

        buttonPlantas.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de mudas", Toast.LENGTH_SHORT).show();
        });

        buttonSementes.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de sementes", Toast.LENGTH_SHORT).show();
        });

        voltar.setOnClickListener(view ->{
            Intent intent = new Intent(FornecedoresActivity.this, MenuUsuarioActivity.class);
            startActivity(intent);
        });
    }
}
