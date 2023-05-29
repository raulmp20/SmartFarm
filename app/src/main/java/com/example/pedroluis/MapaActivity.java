package com.example.pedroluis;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdateFactory;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapaActivity extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap mMap;

    float zoom = 11.0f; //VARIÁVEL PARA O ZOOM VAI ATÉ 21.0F

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

    double lo;
    double la;
    double lo1;
    double la1;
    double lo2;
    double la2;

    String telefoneUser;
    String emailUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle extras = getIntent().getExtras();
        telefoneUser = extras.getString("telefoneUser");
        emailUser = extras.getString("emailUser");

        setContentView(R.layout.activity_mapa);

        Button voltar = findViewById(R.id.button_voltar_mapa);
        voltar.setOnClickListener(v->{
            Intent intent = new Intent(MapaActivity.this, FornecedoresActivity.class);
            intent.putExtra("emailU", emailUser);
            intent.putExtra("telefoneU", telefoneUser);
            startActivity(intent);
        });




        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Adiciona o marcador em santa rita
        LatLng SRS = new LatLng(-22.252980039557748, -45.704704303946194);

        //Gerando os marcadores nos pontos de venda
        lat = getIntent().getStringExtra("Latitude");
        longi = getIntent().getStringExtra("Longitude");

        lat1 = getIntent().getStringExtra("Latitude1");
        longi1 = getIntent().getStringExtra("Longitude1");

        lat2 = getIntent().getStringExtra("Latitude2");
        longi2 = getIntent().getStringExtra("Longitude2");

        nomeponto = getIntent().getStringExtra("ponto");
        nomeponto1 = getIntent().getStringExtra("ponto1");
        nomeponto2 = getIntent().getStringExtra("ponto2");

        nomeendereco = getIntent().getStringExtra("endereco");
        nomeendereco1 = getIntent().getStringExtra("endereco1");
        nomeendereco2 = getIntent().getStringExtra("endereco2");
        la = Double.parseDouble(lat);
        lo = Double.parseDouble(longi);

        la1 = Double.parseDouble(lat1);
        lo1 = Double.parseDouble(longi1);

        la2 = Double.parseDouble(lat2);
        lo2 = Double.parseDouble(longi2);

        LatLng ponto = new LatLng(la, lo);
        LatLng ponto1 = new LatLng(la1, lo1);
        LatLng ponto2 = new LatLng(la2, lo2);

        mMap.addMarker(new MarkerOptions().position(ponto).title(nomeponto));
        mMap.addMarker(new MarkerOptions().position(ponto1).title(nomeponto1));
        mMap.addMarker(new MarkerOptions().position(ponto2).title(nomeponto2));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(SRS,zoom));
        final LatLng EnderecoLatLng = new LatLng(la, lo);
        Marker Ponto = mMap.addMarker(
                new MarkerOptions()
                        .position(EnderecoLatLng)
                        .title(nomeendereco));
        Ponto.showInfoWindow();

        final LatLng Endereco1LatLng = new LatLng(la1, lo1);
        Marker Ponto1 = mMap.addMarker(
                new MarkerOptions()
                        .position(Endereco1LatLng)
                        .title(nomeendereco1));
        Ponto1.showInfoWindow();

        final LatLng Endereco2LatLng = new LatLng(la2, lo2);
        Marker Ponto2 = mMap.addMarker(
                new MarkerOptions()
                        .position(Endereco2LatLng)
                        .title(nomeendereco2));
        Ponto2.showInfoWindow();
    }


}