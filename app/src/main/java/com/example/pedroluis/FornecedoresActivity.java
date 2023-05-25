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
    // LONGITUDE E LATITUDE
    String lat;
    String longi;
    String nomeponto;
    String nomeendereco;
    String lat1;
    String longi1;
    String nomeponto1;
    String nomeendereco1;
    String lat2;
    String longi2;
    String nomeponto2;
    String nomeendereco2;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_find_products);


        Button voltar = findViewById(R.id.button_voltar_products);
        buttonSubstrato = findViewById(R.id.Substrato);
        buttonNutrientes = findViewById(R.id.Nutrientes);
        buttonPlantas = findViewById(R.id.Plantas);


        buttonSubstrato.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de substrato", Toast.LENGTH_SHORT).show();
            Intent Enviadados = new Intent(FornecedoresActivity.this, MapaActivity.class);

            //DEFININDO AS LAT E LONGI DOS PONTOS DE VENDAS
            lat = "-22.25130700903474";
            longi = "-45.70542177792108";

            lat1 = "-22.251758478350066";
            longi1 = "-45.70236248816955";

            lat2 = "-22.2525582883281";
            longi2 = "-45.84273296562191";

            nomeponto = "Flora Quintal e Jardim";
            nomeponto1 = "Shiva Plantas";
            nomeponto2 = "Loja de Bonsai";

            nomeendereco ="R. Francisco M. da Costa, 142 - Centro, Santa Rita do Sapucaí - MG, 37540-000" ;
            nomeendereco1 ="R. Doutor José Ribeiro de Carvalho, 66 - Centro, Santa Rita do Sapucaí - MG, 37540-000";
            nomeendereco2 = "Santa Rita do Sapucaí - MG, 37540-000";

            //ENVIANDO OS DADOS DE LAT E LONGI COM O NOME DO ESTABELECIMENTO
            Enviadados.putExtra("Latitude", lat);
            Enviadados.putExtra("Longitude", longi);
            Enviadados.putExtra("ponto", nomeponto);
            Enviadados.putExtra("endereco", nomeendereco);

            Enviadados.putExtra("Latitude1", lat1);
            Enviadados.putExtra("Longitude1", longi1);
            Enviadados.putExtra("ponto1", nomeponto1);
            Enviadados.putExtra("endereco1", nomeendereco1);


            Enviadados.putExtra("Latitude2", lat2);
            Enviadados.putExtra("Longitude2", longi2);
            Enviadados.putExtra("ponto2", nomeponto2);
            Enviadados.putExtra("endereco2", nomeendereco2);
            startActivity(Enviadados);
        });

        buttonNutrientes.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de nutrientes", Toast.LENGTH_SHORT).show();
            Intent Enviadados = new Intent(FornecedoresActivity.this, MapaActivity.class);

            //DEFININDO AS LAT E LONGI DOS PONTOS DE VENDAS
            lat = "-22.25409846381255";
            longi = "-45.70356814678672";

            lat1 = "-22.24507838556926";
            longi1 = "-45.71000042430278";

            lat2 = "-22.432416753096856";
            longi2 = "-45.46694495360506";

            nomeponto = "Casa do Farelo";
            nomeponto1 = "Bonagro Soluções Agrícolas";
            nomeponto2 = "P j Fertilizante";

            nomeendereco ="R. Adélino Carneiro Pinto, 22 - Centro, Santa Rita do Sapucaí - MG, 37540-000" ;
            nomeendereco1 ="R. José Pinto Vilela, n° 645 - Ozório Machado, Santa Rita do Sapucaí - MG, 37540-000";
            nomeendereco2 = "R. Geraldino Campista, 342 - Santo Antonio, Itajubá - MG, 37503-130";

            //ENVIANDO OS DADOS DE LAT E LONGI COM O NOME DO ESTABELECIMENTO
            Enviadados.putExtra("Latitude", lat);
            Enviadados.putExtra("Longitude", longi);
            Enviadados.putExtra("ponto", nomeponto);
            Enviadados.putExtra("endereco", nomeendereco);

            Enviadados.putExtra("Latitude1", lat1);
            Enviadados.putExtra("Longitude1", longi1);
            Enviadados.putExtra("ponto1", nomeponto1);
            Enviadados.putExtra("endereco1", nomeendereco1);


            Enviadados.putExtra("Latitude2", lat2);
            Enviadados.putExtra("Longitude2", longi2);
            Enviadados.putExtra("ponto2", nomeponto2);
            Enviadados.putExtra("endereco2", nomeendereco2);
            startActivity(Enviadados);
        });

        buttonPlantas.setOnClickListener(v->{
            Toast.makeText(getApplicationContext(), "Mapa com os locais mais proximos de venda de mudas", Toast.LENGTH_SHORT).show();
            Intent Enviadados = new Intent(FornecedoresActivity.this, MapaActivity.class);

            //DEFININDO AS LAT E LONGI DOS PONTOS DE VENDAS
            lat = "-22.244142537373907";
            longi = "-45.70785550096605";

            lat1 = "-22.25538100269502";
            longi1 = "-45.697547257791875";

            lat2 = "-22.253945030490787";
            longi2 = "-45.70355164254022";

            nomeponto = "Casa das Rações";
            nomeponto1 = "Agropecuária Santa Luzia";
            nomeponto2 = "Casa do Farelo";

            nomeendereco ="R. Padre Vítor - Maristela, Santa Rita do Sapucaí - MG, 37540-000" ;
            nomeendereco1 ="R. Felíciano Teles, 264 - Inatel, Santa Rita do Sapucaí - MG, 37540-000";
            nomeendereco2 = "R. Adélino Carneiro Pinto, 22 - Centro, Santa Rita do Sapucaí - MG, 37540-000";

            //ENVIANDO OS DADOS DE LAT E LONGI COM O NOME DO ESTABELECIMENTO
            Enviadados.putExtra("Latitude", lat);
            Enviadados.putExtra("Longitude", longi);
            Enviadados.putExtra("ponto", nomeponto);
            Enviadados.putExtra("endereco", nomeendereco);

            Enviadados.putExtra("Latitude1", lat1);
            Enviadados.putExtra("Longitude1", longi1);
            Enviadados.putExtra("ponto1", nomeponto1);
            Enviadados.putExtra("endereco1", nomeendereco1);


            Enviadados.putExtra("Latitude2", lat2);
            Enviadados.putExtra("Longitude2", longi2);
            Enviadados.putExtra("ponto2", nomeponto2);
            Enviadados.putExtra("endereco2", nomeendereco2);
            startActivity(Enviadados);
        });



        voltar.setOnClickListener(view ->{
            Intent intent = new Intent(FornecedoresActivity.this, MenuUsuarioActivity.class);
            startActivity(intent);
        });
    }
}