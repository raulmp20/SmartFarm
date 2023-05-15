package com.example.pedroluis;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AssistenciaMenuActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    String number = "35988921894";


    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_tecnica);

        ImageButton fale_conosco = findViewById(R.id.assistencia_fale_conosco);
        Button fale_conosco_b = findViewById(R.id.button_assistencia_fale_conosco);
        Button voltar = findViewById(R.id.voltar);

        fale_conosco.setOnClickListener(view ->{
            String url = "https://api.whatsapp.com/send?phone="+number;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        fale_conosco_b.setOnClickListener(view ->{
            String url = "https://api.whatsapp.com/send?phone="+number;
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(url));
            startActivity(i);
        });

        voltar.setOnClickListener(view ->{
            Intent intent = new Intent(AssistenciaMenuActivity.this, MenuUsuarioActivity.class);
            startActivity(intent);
        });
    }
}
