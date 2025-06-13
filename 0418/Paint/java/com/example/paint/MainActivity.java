package com.example.drawingapp;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private com.example.drawingapp.DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawingView = findViewById(R.id.drawingView);

        ImageButton btnBlack = findViewById(R.id.btnBlack);
        ImageButton btnRed = findViewById(R.id.btnRed);
        ImageButton btnGreen = findViewById(R.id.btnGreen);
        ImageButton btnBlue = findViewById(R.id.btnBlue);
        Button btnClear = findViewById(R.id.btnClear);

        btnBlack.setOnClickListener(v -> drawingView.setPenColor(Color.BLACK));
        btnRed.setOnClickListener(v -> drawingView.setPenColor(Color.RED));
        btnGreen.setOnClickListener(v -> drawingView.setPenColor(Color.GREEN));
        btnBlue.setOnClickListener(v -> drawingView.setPenColor(Color.BLUE));
        btnClear.setOnClickListener(v -> drawingView.clearCanvas());
    }
}