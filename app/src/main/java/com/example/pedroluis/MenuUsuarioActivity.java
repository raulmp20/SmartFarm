package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuUsuarioActivity extends AppCompatActivity {

    String firstCheckMain = "Tgg";
    // Passando esse parâmrtro para as próximas telas
    String telefoneUser;

    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_usuario);
        Bundle extras = getIntent().getExtras();
        telefoneUser = extras.getString("telefoneUser");

        // Representante dos botões do front-end para serem manipulados pelo back-end e enviados ao front-end
        Button usuario;
        // Procure pelo componente com id = "ph" na tela citada acima
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
            // mudando a tela para a tela das informações do ph
            Intent goUsuario = new Intent(MenuUsuarioActivity.this,UsuarioActivity.class);
            goUsuario.putExtra("telefoneUser",telefoneUser);
            startActivity(goUsuario);
        });
        usuario_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent goUsuarios = new Intent(MenuUsuarioActivity.this,UsuarioActivity.class);
            goUsuarios.putExtra("telefoneUser",telefoneUser);
            startActivity(goUsuarios);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        estufas.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent goEstufa = new Intent(MenuUsuarioActivity.this,MenuEstufaActivity.class);
            startActivity(goEstufa);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        estufas_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent goEstufas = new Intent(MenuUsuarioActivity.this,EstufasCadastradasActivity.class);
            startActivity(goEstufas);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        fornecedores.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent goFornecedor = new Intent(MenuUsuarioActivity.this,FornecedoresActivity.class);
            startActivity(goFornecedor);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        fornecedores_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent goFornecedores = new Intent(MenuUsuarioActivity.this,FornecedoresActivity.class);
            startActivity(goFornecedores);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        assistencia.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent goAssistencia = new Intent(MenuUsuarioActivity.this, AssistenciaMenuActivity.class);
            startActivity(goAssistencia);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        assistencia_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent goAssistencias = new Intent(MenuUsuarioActivity.this,AssistenciaMenuActivity.class);
            startActivity(goAssistencias);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        voltar.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent sair = new Intent(MenuUsuarioActivity.this,MainActivity.class);
            startActivity(sair);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
    }
}
