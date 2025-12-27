package com.example.calmisalario;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.content.Intent;
import android.view.View;

import com.example.calmisalario.SalaryCalculatorActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.cardview.widget.CardView btnGoToSalary = findViewById(R.id.btnGoToSalary);

        btnGoToSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Creamos una intención para abrir la actividad de la calculadora
                Intent intent = new Intent(MainActivity.this, SalaryCalculatorActivity.class);
                startActivity(intent);
            }
        });
    }
}
