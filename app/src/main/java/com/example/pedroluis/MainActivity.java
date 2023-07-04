package com.example.pedroluis;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Botão login e cadastro
        Button fazer_login = findViewById(R.id.ir_login);
        Button ir_cadastro = findViewById(R.id.ir_cadastro);

        fazer_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent login= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(login);
            }
        });

        ir_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cadastrar = new Intent(MainActivity.this,CadastroActivity.class);
                startActivity(cadastrar);
            }
        });

    }

}