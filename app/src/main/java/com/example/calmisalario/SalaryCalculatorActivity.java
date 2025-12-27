package com.example.calmisalario;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import androidx.cardview.widget.CardView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.example.calmisalario.model.CalculatorModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.NumberFormat;
import java.util.Locale;

public class SalaryCalculatorActivity extends AppCompatActivity {

    // Input Views
    private Spinner spCategoria;
    private Spinner spAntiguedad;
    private RadioGroup rgTitulo;
    private RadioGroup rgMesCompleto;
    private EditText etHorasExtra50;
    private EditText etHorasExtra100;
    private EditText etViatico; // Adelanto
    private CardView btnCalcular;
    private BottomNavigationView bottomNavigationView;

    // Result Views
    private TextView txtBasicoValue;
    private TextView txtAntiguedadValue;
    private TextView txtExtras50Value;
    private TextView txtExtras100Value;
    private TextView txtTituloSecundarioValue;
    private TextView txtPresentismoPerfValue;
    private TextView txtPresentismoComplValue;
    private TextView txtAsigNoRemunerativaValue;
    private TextView txtRefrigerioValue;
    private TextView txtJubilacionValue;
    private TextView txtLey19032Value;
    private TextView txtSindicatoValue;
    private TextView txtSepelioValue;
    private TextView txtObraSocialValue;
    private TextView txtAnticipoValue;
    private TextView txtSueldoLiquidoValue;

    private CalculatorModel calculatorModel;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salary_calculator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            v.setPadding(v.getPaddingLeft(), v.getPaddingTop(), v.getPaddingRight(), insets.getSystemWindowInsetBottom());
            return insets;
        });

        calculatorModel = new CalculatorModel();

        initializeViews();
        setupSpinners();
        setupBottomNavigation();

        btnCalcular.setOnClickListener(v -> calculateAndShowResults());
    }

    private void initializeViews() {
        // Inputs
        spCategoria = findViewById(R.id.spCategoria);
        spAntiguedad = findViewById(R.id.spAntiguedad);
        rgTitulo = findViewById(R.id.rgTitulo);
        rgMesCompleto = findViewById(R.id.rgMesCompleto);
        etHorasExtra50 = findViewById(R.id.etHorasExtra50);
        etHorasExtra100 = findViewById(R.id.etHorasExtra100);
        etViatico = findViewById(R.id.etViatico);
        btnCalcular = findViewById(R.id.btnCalcular);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Results
        txtBasicoValue = findViewById(R.id.txtBasicoValue);
        txtAntiguedadValue = findViewById(R.id.txtAntiguedadValue);
        txtExtras50Value = findViewById(R.id.txtExtras50Value);
        txtExtras100Value = findViewById(R.id.txtExtras100Value);
        txtTituloSecundarioValue = findViewById(R.id.txtTituloSecundarioValue);
        txtPresentismoPerfValue = findViewById(R.id.txtPresentismoPerfValue);
        txtPresentismoComplValue = findViewById(R.id.txtPresentismoComplValue);
        txtAsigNoRemunerativaValue = findViewById(R.id.txtAsigNoRemunerativaValue);
        txtRefrigerioValue = findViewById(R.id.txtRefrigerioValue);
        txtJubilacionValue = findViewById(R.id.txtJubilacionValue);
        txtLey19032Value = findViewById(R.id.txtLey19032Value);
        txtSindicatoValue = findViewById(R.id.txtSindicatoValue);
        txtSepelioValue = findViewById(R.id.txtSepelioValue);
        txtObraSocialValue = findViewById(R.id.txtObraSocialValue);
        txtAnticipoValue = findViewById(R.id.txtAnticipoValue);
        txtSueldoLiquidoValue = findViewById(R.id.txtSueldoLiquidoValue);
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> categoriaAdapter = ArrayAdapter.createFromResource(this,
                R.array.categorias_array, android.R.layout.simple_spinner_item);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategoria.setAdapter(categoriaAdapter);

        Integer[] antiguedadValues = new Integer[41];
        for (int i = 0; i <= 40; i++) {
            antiguedadValues[i] = i;
        }
        ArrayAdapter<Integer> antiguedadAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, antiguedadValues);
        antiguedadAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAntiguedad.setAdapter(antiguedadAdapter);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.action_share) {
                shareCalculator();
                return true;
            } else if (itemId == R.id.action_help) {
                showHelp();
                return true;
            } else if (itemId == R.id.action_indemnification) {
                showIndemnificationToast();
                return true;
            }
            return false;
        });
    }

    private void shareCalculator() {
        String url = "https://wa.me/?text=%C2%A1Mir%C3%A1%20esta%20calculadora%20de%20salarios%20de%20bodega%20y%20vi%C3%B1a!https:%20%20//https://alex2unde.github.io/calc-mi-salario";
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }

    private void showHelp() {
        Toast.makeText(this, "Ayuda: Próximamente...", Toast.LENGTH_SHORT).show();
    }

    private void showIndemnificationToast() {
        Toast.makeText(this, "Calcular Indemnización: Próximamente...", Toast.LENGTH_SHORT).show();
    }

    private void calculateAndShowResults() {
        try {
            String categoria = spCategoria.getSelectedItem().toString();
            int antiguedad = (Integer) spAntiguedad.getSelectedItem();
            boolean tieneTitulo = ((RadioButton) findViewById(rgTitulo.getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase("Sí");
            boolean trabajoMesCompleto = ((RadioButton) findViewById(rgMesCompleto.getCheckedRadioButtonId())).getText().toString().equalsIgnoreCase("Sí");
            int horasExt50 = getIntFromEditText(etHorasExtra50);
            int horasExt100 = getIntFromEditText(etHorasExtra100);
            double adelanto = getDoubleFromEditText(etViatico);

            // Assumptions
            int horasTrabajadas = 0;
            boolean tieneObraSocial = true;

            CalculatorModel.CalculationResult result = calculatorModel.calculate(
                    categoria, antiguedad, tieneTitulo, trabajoMesCompleto,
                    horasTrabajadas, horasExt50, horasExt100, tieneObraSocial, adelanto
            );

            displayResults(result, adelanto);

        } catch (Exception e) {
            Toast.makeText(this, "Error en los datos ingresados", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void displayResults(CalculatorModel.CalculationResult result, double adelanto) {
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("es", "AR"));

        txtBasicoValue.setText(currencyFormat.format(result.sueldoBasico));
        txtAntiguedadValue.setText(currencyFormat.format(result.adicionalAntiguedad));
        txtExtras50Value.setText(currencyFormat.format(result.horasExtras50));
        txtExtras100Value.setText(currencyFormat.format(result.horasExtras100));
        txtTituloSecundarioValue.setText(currencyFormat.format(result.adicionalTitulo));
        txtPresentismoPerfValue.setText(currencyFormat.format(result.presentismo));
        txtPresentismoComplValue.setText(currencyFormat.format(result.presentismoAdicional));
        txtAsigNoRemunerativaValue.setText(currencyFormat.format(result.asignacionNoRemunerativa));
        txtRefrigerioValue.setText(currencyFormat.format(result.refrigerio));

        txtJubilacionValue.setText(currencyFormat.format(result.jubilacion * -1));
        txtLey19032Value.setText(currencyFormat.format(result.ley19032 * -1));
        txtSindicatoValue.setText(currencyFormat.format(result.sindicato * -1));
        txtSepelioValue.setText(currencyFormat.format(result.sepelio * -1));
        txtObraSocialValue.setText(currencyFormat.format(result.obraSocial * -1));
        txtAnticipoValue.setText(currencyFormat.format(adelanto * -1));

        txtSueldoLiquidoValue.setText(currencyFormat.format(result.sueldoFinal));
    }

    private int getIntFromEditText(EditText editText) {
        String text = editText.getText().toString();
        return text.isEmpty() ? 0 : Integer.parseInt(text);
    }

    private double getDoubleFromEditText(EditText editText) {
        String text = editText.getText().toString();
        return text.isEmpty() ? 0.0 : Double.parseDouble(text);
    }
}
