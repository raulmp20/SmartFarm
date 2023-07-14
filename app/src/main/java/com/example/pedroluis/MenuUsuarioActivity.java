package com.example.pedroluis;

import static com.example.pedroluis.UsuarioActivity.SHARED_PREFS;
import static com.example.pedroluis.UsuarioActivity.sharedpreferences;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class MenuUsuarioActivity extends AppCompatActivity {

    String firstCheckMain = "Tgg";
    // Passando esse parâmrtro para as próximas telas
    String telefoneUser;
    String emailUser;
    @Override
    public void onBackPressed() {
    }
    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);


        // Representante dos botões do front-end para serem manipulados pelo back-end e enviados ao front-end
        Button usuario;
        usuario = findViewById(R.id.button_usuario);

        // Farei o mesmo esquema repetidamente
        ImageButton usuario_image;
        usuario_image = findViewById(R.id.usuario);

        Button estufas;
        estufas = findViewById(R.id.button_estufas);

        ImageButton estufas_image;
        estufas_image = findViewById(R.id.estufas);

        Button fornecedores;
        fornecedores = findViewById(R.id.button_fornecedores);

        ImageButton fornecedores_image;
        fornecedores_image = findViewById(R.id.fornecedores);

        Button assistencia;
        assistencia = findViewById(R.id.button_assistencia);

        ImageButton assistencia_image;
        assistencia_image = findViewById(R.id.assistencia);


        Button voltar;
        voltar = findViewById(R.id.button_sair);

        // Condições caso eu clique nos botões
        usuario.setOnClickListener(v-> {
            // mudando a tela para a tela de usuário
            Intent goUsuario = new Intent(MenuUsuarioActivity.this,UsuarioActivity.class);

            startActivity(goUsuario);
        });
        usuario_image.setOnClickListener(v ->{
            // mudando a tela para a tela de usuário
            Intent goUsuarios = new Intent(MenuUsuarioActivity.this,UsuarioActivity.class);

            startActivity(goUsuarios);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        estufas.setOnClickListener(v-> {
            // mudando a tela para a tela de estufas cadastradas
            Intent goEstufa = new Intent(MenuUsuarioActivity.this,EstufasCadastradasActivity.class);

            startActivity(goEstufa);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        estufas_image.setOnClickListener(v ->{
            // mudando a tela para a tela de estufas cadastradas
            Intent goEstufas = new Intent(MenuUsuarioActivity.this,EstufasCadastradasActivity.class);

            startActivity(goEstufas);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        fornecedores.setOnClickListener(v-> {
            // mudando a tela para a tela de fornecedores
            Intent goFornecedor = new Intent(MenuUsuarioActivity.this,FornecedoresActivity.class);

            startActivity(goFornecedor);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        fornecedores_image.setOnClickListener(v ->{
            // mudando a tela para a tela de fornecedores
            Intent goFornecedores = new Intent(MenuUsuarioActivity.this,FornecedoresActivity.class);

            startActivity(goFornecedores);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        assistencia.setOnClickListener(v-> {
            // mudando a tela para a tela de assistência
            Intent goAssistencia = new Intent(MenuUsuarioActivity.this, AssistenciaMenuActivity.class);

            startActivity(goAssistencia);
        });
        assistencia_image.setOnClickListener(v ->{
            // mudando a tela para a tela de assisência
            Intent goAssistencias = new Intent(MenuUsuarioActivity.this,AssistenciaMenuActivity.class);

            startActivity(goAssistencias);
        });

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela main
            sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();

            editor.putString("email",null);
            editor.putString("senha",null);
            editor.apply();
            Intent sair = new Intent(MenuUsuarioActivity.this,LoginActivity.class);
            startActivity(sair);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
    }
}
