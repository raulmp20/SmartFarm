package com.example.pedroluis;

import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

public class GraficoPhActivity extends AppCompatActivity {

    ArrayList barArrayList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafico);
        BarChart barChart = findViewById(R.id.barchart);
        getData();
        BarDataSet barDataSet = new BarDataSet(barArrayList, "Valor Instantaneo");
        BarData barData = new BarData(barDataSet);
        barChart.setData(barData);
        //color bar data set
        barDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //text color
        barDataSet.setValueTextColor(Color.BLACK);
        //setting text size
        barDataSet.setValueTextSize(16f);
        barChart.getDescription().setEnabled(true);
    }

    private void getData(){
        barArrayList = new ArrayList<>();
        barArrayList.add(new BarEntry(2f, 10));
        barArrayList.add(new BarEntry(3f, 20));
        barArrayList.add(new BarEntry(4f, 30));
        barArrayList.add(new BarEntry(5f, 40));
        barArrayList.add(new BarEntry(6f, 50));
    }
}
