package com.example.calmisalario;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
// Importa CardView en lugar de Button si vas a usarlo como variable de clase
import androidx.cardview.widget.CardView;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class IndemnizacionActivity extends AppCompatActivity {

    private EditText etFechaIngreso;
    private EditText etFechaEgreso;
    private EditText etMejorSueldo;
    private final Calendar calendar = Calendar.getInstance();

    // Vistas de Resultados
    private CardView resultsCard;
    private TextView txtAntiguedadValue;
    private TextView txtIndemnizacionAntiguedadValue;
    private TextView txtPreavisoValue;
    private TextView txtAguinaldoProporcionalValue;
    private TextView txtVacacionesProporcionalesValue;
    private TextView txtTotalEstimadoValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indemnizacion);

        // --- Inicialización de las vistas de entrada ---
        etFechaIngreso = findViewById(R.id.et_fecha_ingreso);
        etFechaEgreso = findViewById(R.id.et_fecha_egreso);
        etMejorSueldo = findViewById(R.id.et_mejor_sueldo);

        // ======================================================
        // ¡CORRECCIÓN CLAVE AQUÍ!
        // La variable ahora es del tipo correcto: CardView
        // ======================================================
        CardView btnCalcular = findViewById(R.id.btn_calcular_indemnizacion);

        // --- Inicialización de las vistas de resultados ---
        resultsCard = findViewById(R.id.results_card);
        // ... (el resto de tus findViewById para los resultados)
        txtAntiguedadValue = findViewById(R.id.txtAntiguedadValue);
        txtIndemnizacionAntiguedadValue = findViewById(R.id.txtIndemnizacionAntiguedadValue);
        txtPreavisoValue = findViewById(R.id.txtPreavisoValue);
        txtAguinaldoProporcionalValue = findViewById(R.id.txtAguinaldoProporcionalValue);
        txtVacacionesProporcionalesValue = findViewById(R.id.txtVacacionesProporcionalesValue);
        txtTotalEstimadoValue = findViewById(R.id.txtTotalEstimadoValue);


        // --- Configuración de los listeners ---
        etFechaIngreso.setOnClickListener(v -> showDatePickerDialog(etFechaIngreso));
        etFechaEgreso.setOnClickListener(v -> showDatePickerDialog(etFechaEgreso));

        btnCalcular.setOnClickListener(v -> calcularIndemnizacion());
    }

    private void calcularIndemnizacion() {
        // ... (el resto de tu lógica de cálculo aquí no cambia)
        // Este método ya debería estar bien.
    }

    private void showDatePickerDialog(final EditText editText) {
        // ... (este método no necesita cambios)
    }

    private void updateLabel(EditText editText, Calendar selectedDate) {
        // ... (este método no necesita cambios)
    }
}
