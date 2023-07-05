package com.example.pedroluis;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AssistenciaMenuActivity extends AppCompatActivity {

    MqttHelper mqttHelper;

    String number = "35988921894";
    String number1 = "35998580092";

    String telefoneUser;

    String emailUser;

    @Override
    public void onBackPressed() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistencia_tecnica);


        ImageButton fale_conosco = findViewById(R.id.assistencia_fale_conosco);
        Button fale_conosco_b = findViewById(R.id.button_assistencia_fale_conosco);
        ImageButton converse_com = findViewById(R.id.assistencia_converse_com);
        Button voltar = findViewById(R.id.voltar);

        fale_conosco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmaConversa = new AlertDialog.Builder(AssistenciaMenuActivity.this);
                confirmaConversa.setTitle("Desenvolvedores: Raul Moreno Pereira e Vinícius de Souza Simões");
                confirmaConversa.setMessage("Estudantes do Instituto Nacional de Telecomunicações (Inatel), do curso de Engenharia de Computação e Software, atualmente " +
                        "trabalham no desenvolvimento do aplicado AquaFarm, destinado a produtores rurais para otimizar suas produções." +
                        "O projeto está sendo desenvolvido dentro do CS&I Lab");
                confirmaConversa.setCancelable(false);
                confirmaConversa.setPositiveButton("Falar conosco", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String url = "https://api.whatsapp.com/send?phone="+number;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                confirmaConversa.setNegativeButton("Cancelar", null);
                confirmaConversa.create().show();
            }

    });
        converse_com.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmaConversa = new AlertDialog.Builder(AssistenciaMenuActivity.this);
                confirmaConversa.setTitle("Especialista");
                confirmaConversa.setMessage("Aqui será inserido o contato de um especialista da área que possa dar dicas de cultivo e soluções para o agricultor");
                confirmaConversa.setCancelable(false);
                confirmaConversa.setPositiveButton("Falar conosco", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String url = "https://api.whatsapp.com/send?phone="+number1;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                confirmaConversa.setNegativeButton("Cancelar", null);
                confirmaConversa.create().show();
            }
        });

        fale_conosco_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmaConversa = new AlertDialog.Builder(AssistenciaMenuActivity.this);
                confirmaConversa.setTitle("Desenvolvedores: Raul Moreno Pereira e Vinícius de Souza Simões");
                confirmaConversa.setMessage("Estudantes do Instituto Nacional de Telecomunicações (Inatel), do curso de Engenharia de Computação e Software, atualmente " +
                        "trabalham no desenvolvimento do aplicado AquaFarm, destinado a produtores rurais para otimizar suas produções." +
                        "O projeto está sendo desenvolvido dentro do CS&I Lab");
                confirmaConversa.setCancelable(false);
                confirmaConversa.setPositiveButton("Falar conosco", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        String url = "https://api.whatsapp.com/send?phone="+number;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    }
                });
                confirmaConversa.setNegativeButton("Cancelar", null);
                confirmaConversa.create().show();
        }
    });

        voltar.setOnClickListener(view ->{
            Intent voltar_usuario = new Intent(AssistenciaMenuActivity.this, MenuUsuarioActivity.class);
            startActivity(voltar_usuario);
        });
    }
}
