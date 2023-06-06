package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MenuEstufaActivity extends AppCompatActivity {
    String firstCheckMain = "Tgg";
    // Passando esse parâmrtro para as próximas telas

    String emailAntes;
    String telefoneAntes;

    @Override  // coloca coisas basicas da tela, funcionalidades
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //fazendo com o cód olhe para tela de menu
        setContentView(R.layout.activity_menu);

        Bundle extras = getIntent().getExtras();
        emailAntes = extras.getString("emailA");
        telefoneAntes = extras.getString("telefoneA");

        // Representante dos botões do front-end para serem manipulados pelo back-end e enviados ao front-end
        Button config;

        // Procure pelo componente com id = "ph" na tela citada acima
        config = findViewById(R.id.button_config);

        // Farei o mesmo esquema repetidamente
        ImageButton config_image;
        config_image = findViewById(R.id.config);

        Button PH;
        PH = findViewById(R.id.button_ph);

        ImageButton image_ph;
        image_ph = findViewById(R.id.ph);

        Button umi_ar;
        umi_ar = findViewById(R.id.button_umi_ar);

        ImageButton umi_ar_image;
        umi_ar_image = findViewById(R.id.umi_ar);

        Button temp;
        temp = findViewById(R.id.button_temp);

        ImageButton temp_image;
        temp_image = findViewById(R.id.temp);

        Button umi_substrato;
        umi_substrato = findViewById(R.id.button_umi_substrato);

        ImageButton umi_substrato_image;
        umi_substrato_image = findViewById(R.id.umi_substrato);

        Button lumi;
        lumi = findViewById(R.id.button_lumi);

        ImageButton lumi_image;
        lumi_image = findViewById(R.id.lumi);


        Button sair;
        sair = findViewById(R.id.button_sair);

        // Condições caso eu clique nos botões
        config.setOnClickListener(v-> {
            // mudando para tela de config
            Intent mudar = new Intent(MenuEstufaActivity.this,ConfigEstufaActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
        });
        config_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,ConfigEstufaActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        PH.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,PhActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        image_ph.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,PhActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_ar.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiArActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_ar_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiArActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        temp.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, TemperaturaActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        temp_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,TemperaturaActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        umi_substrato.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, UmiSoloActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        umi_substrato_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,UmiSoloActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        lumi.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, LuminosidadeActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
        lumi_image.setOnClickListener(v ->{
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this, LuminosidadeActivity.class);
            mudar.putExtra("emailA", emailAntes);
            mudar.putExtra("telefoneA", telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });

        sair.setOnClickListener(v-> {
            // mudando a tela para a tela das informações do ph
            Intent mudar = new Intent(MenuEstufaActivity.this,MenuUsuarioActivity.class);
            mudar.putExtra("emailUser",emailAntes);
            mudar.putExtra("telefoneUser",telefoneAntes);
            startActivity(mudar);
            // Exclui essa tela ao sair para não guardar as info que pus nela
            onRestart();
        });
    }
}
