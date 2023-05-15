package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class CadastroEstufaActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_cadastro_estufas);

        Button cadastrar = findViewById(R.id.button_salvar);
        cadastrar.setOnClickListener(view -> {
            Intent cads  = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
            startActivity(cads);
        });

        Button voltar = findViewById(R.id.button_voltar);


        voltar.setOnClickListener(view ->{
            Intent intent = new Intent(CadastroEstufaActivity.this, EstufasCadastradasActivity.class);
            startActivity(intent);
        });
    }
}
