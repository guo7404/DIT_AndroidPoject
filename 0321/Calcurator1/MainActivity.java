package com.example.calcurator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    EditText eText1;
    EditText eText2;
    EditText eText3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button bPlus = (Button)findViewById(R.id.plus);
        Button bMinus = (Button)findViewById(R.id.minus);
        Button bMultiplied = (Button)findViewById(R.id.multiplied);
        Button bDivided = (Button)findViewById(R.id.divided);
        eText1 = (EditText)findViewById(R.id.edit1);
        eText2 = (EditText)findViewById(R.id.edit2);
        eText3 = (EditText)findViewById(R.id.edit3);
    }

    public void clicked_plus(View e){
        String s1 = eText1.getText().toString();
        String s2 = eText2.getText().toString();
        int result = Integer.parseInt(s1)+ Integer.parseInt(s2);
        eText3.setText(""+result);
    }
}