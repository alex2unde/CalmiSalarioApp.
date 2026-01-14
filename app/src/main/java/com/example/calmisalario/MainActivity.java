package com.example.calmisalario;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // --- BOTÓN 1: IR A CALCULADORA DE SALARIO ---
        androidx.cardview.widget.CardView btnGoToSalary = findViewById(R.id.btnGoToSalary);
        btnGoToSalary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, SalaryCalculatorActivity.class);
                startActivity(intent);
            }
        });

        // --- BOTÓN 2: IR A CALCULADORA DE INDEMNIZACIÓN ---
        androidx.cardview.widget.CardView btnGoToIndemnizacion = findViewById(R.id.btnGoToIndemnizacion);
        btnGoToIndemnizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, IndemnizacionActivity.class);
                startActivity(intent);
            }
        });
    }
}

