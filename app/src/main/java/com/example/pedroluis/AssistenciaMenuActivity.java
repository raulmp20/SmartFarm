package com.example.pedroluis;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
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
            AlertDialog.Builder comfirmaAssistencia = new AlertDialog.Builder(AssistenciaMenuActivity.this);
            comfirmaAssistencia.setTitle("Desenvolvedor: Raul Moreno Pereira");
            comfirmaAssistencia.setMessage("Estudante do Instituto Nacional de Telecomunicações (Inatel), do curso de Engenharia de Computação, atualmente" +
                    "trabalha no desenvolvimento do aplicado AquaFarm, destinado a produtores rurais para otimizar suas produções." +
                    "O projeto está sendo desenvolvido dentro do CS&I Lab");
            comfirmaAssistencia.setCancelable(false);
            comfirmaAssistencia.setPositiveButton("Pedir assistência", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int witch) {
                    String url = "https://api.whatsapp.com/send?phone="+number;
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                }
            });
            comfirmaAssistencia.setPositiveButton("Cancelar",null);
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
