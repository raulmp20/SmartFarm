package com.example.pedroluis;

import static com.example.pedroluis.LoginActivity.SHARED_PREFS;
import static com.example.pedroluis.LoginActivity.sharedpreferences;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.view.Window;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    String emailUsuario;
    String senhaUsuario;
    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Botão login e cadastro
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        emailUsuario = sharedpreferences.getString("email", "");
        senhaUsuario = sharedpreferences.getString("senha", "");

        if(!emailUsuario.isEmpty() && !senhaUsuario.isEmpty()){
            Intent entrar= new Intent(MainActivity.this,MenuUsuarioActivity.class);
            startActivity(entrar);
        }

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