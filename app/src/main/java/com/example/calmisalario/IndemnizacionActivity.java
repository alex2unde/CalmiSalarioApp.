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
    private TextView txtIntegracionValue;
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
        androidx.cardview.widget.CardView btnCalcular = findViewById(R.id.btn_calcular_indemnizacion);

        // --- Inicialización de las vistas de resultados ---
        resultsCard = findViewById(R.id.results_card);
        // ... (el resto de tus findViewById para los resultados)
        txtAntiguedadValue = findViewById(R.id.txtAntiguedadValue);
        txtIndemnizacionAntiguedadValue = findViewById(R.id.txtIndemnizacionAntiguedadValue);
        txtPreavisoValue = findViewById(R.id.txtPreavisoValue);
        txtIntegracionValue = findViewById(R.id.txtIntegracionValue);
        txtAguinaldoProporcionalValue = findViewById(R.id.txtAguinaldoProporcionalValue);
        txtVacacionesProporcionalesValue = findViewById(R.id.txtVacacionesProporcionalesValue);
        txtTotalEstimadoValue = findViewById(R.id.txtTotalEstimadoValue);


        // --- Configuración de los listeners ---
        etFechaIngreso.setOnClickListener(v -> showDatePickerDialog(etFechaIngreso));
        etFechaEgreso.setOnClickListener(v -> showDatePickerDialog(etFechaEgreso));

        btnCalcular.setOnClickListener(v -> calcularIndemnizacion());
    }

    private void calcularIndemnizacion() {
        // --- 1. Obtener y validar los datos de entrada ---
        String fechaIngresoStr = etFechaIngreso.getText().toString();
        String fechaEgresoStr = etFechaEgreso.getText().toString();        String mejorSueldoStr = etMejorSueldo.getText().toString();

        RadioGroup rgTipoSalida = findViewById(R.id.rg_tipo_salida);
        boolean esDespidoSinCausa = (rgTipoSalida.getCheckedRadioButtonId() == R.id.rb_despido_sin_causa);

        if (fechaIngresoStr.isEmpty() || fechaEgresoStr.isEmpty() || mejorSueldoStr.isEmpty() || mejorSueldoStr.equals(".")) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        double mejorSueldo = Double.parseDouble(mejorSueldoStr);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        Date fechaIngreso;
        Date fechaEgreso;
        try {
            fechaIngreso = sdf.parse(fechaIngresoStr);
            fechaEgreso = sdf.parse(fechaEgresoStr);
        } catch (ParseException e) {
            Toast.makeText(this, "Formato de fecha inválido", Toast.LENGTH_SHORT).show();
            return;
        }

        // --- 2. Realizar los cálculos ---

        // Cálculo de Antigüedad
        long diffInMillis = Math.abs(fechaEgreso.getTime() - fechaIngreso.getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        int aniosAntiguedad = (int) (diffInDays / 365.25);
        int diasRestantes = (int) (diffInDays % 365.25);
        int mesesAntiguedad = diasRestantes / 30;

        // Lógica de cálculo principal
        double indemnizacionPorAntiguedad = 0;
        double preaviso = 0;
        double integracionMesDespido = 0; // Se inicializa en 0

        if (esDespidoSinCausa) {
            // Indemnización por antigüedad: 1 sueldo por cada año de servicio o fracción mayor de 3 meses.
            int aniosParaIndemnizacion = aniosAntiguedad;
            if (diasRestantes > 90) { // Fracción mayor a 3 meses
                aniosParaIndemnizacion++;
            }
            indemnizacionPorAntiguedad = mejorSueldo * aniosParaIndemnizacion;

            // Preaviso: 1 sueldo si antigüedad < 5 años, 2 si es >= 5 años.
            preaviso = (aniosAntiguedad < 5) ? mejorSueldo : mejorSueldo * 2;

            // =========================================================
            //  NUEVA LÓGICA: INTEGRACIÓN DEL MES DE DESPIDO
            // =========================================================
            Calendar calEgreso = Calendar.getInstance();
            calEgreso.setTime(fechaEgreso);
            int ultimoDiaDelMes = calEgreso.getActualMaximum(Calendar.DAY_OF_MONTH);
            int diaDeEgreso = calEgreso.get(Calendar.DAY_OF_MONTH);

            if (diaDeEgreso != ultimoDiaDelMes) {
                int diasFaltantes = ultimoDiaDelMes - diaDeEgreso;
                // Se calcula el valor diario del sueldo y se multiplica por los días restantes.
                integracionMesDespido = (mejorSueldo / 30.0) * diasFaltantes;
            }
        }

        // Rubros proporcionales (se pagan en ambos casos, despido o renuncia)
        Calendar calEgreso = Calendar.getInstance(); // Se puede reutilizar la instancia anterior
        calEgreso.setTime(fechaEgreso);
        int diaDelAnio = calEgreso.get(Calendar.DAY_OF_YEAR);

        // SAC Proporcional (Aguinaldo)
        double sacProporcional = (mejorSueldo / 2 / 182.5) * diaDelAnio;

        // Vacaciones No Gozadas Proporcionales (cálculo simplificado)
        double diasVacacionesPorAnio = 14; // Menos de 5 años
        if (aniosAntiguedad >= 5 && aniosAntiguedad < 10) diasVacacionesPorAnio = 21;
        else if (aniosAntiguedad >= 10 && aniosAntiguedad < 20) diasVacacionesPorAnio = 28;
        else if (aniosAntiguedad >= 20) diasVacacionesPorAnio = 35;

        double vacacionesProporcionales = (diasVacacionesPorAnio / 365.0) * diaDelAnio * (mejorSueldo / 25.0);

        // Se añade 'integracionMesDespido' a la suma total
        double totalEstimado = indemnizacionPorAntiguedad + preaviso + integracionMesDespido + sacProporcional + vacacionesProporcionales;


        // --- 3. Mostrar los resultados ---
        // (Esta parte no cambia, pero ahora mostrará el nuevo cálculo)
        resultsCard.setVisibility(View.VISIBLE);

        txtAntiguedadValue.setText(String.format(Locale.getDefault(), "%d años y %d meses", aniosAntiguedad, mesesAntiguedad));
        txtIndemnizacionAntiguedadValue.setText(String.format(Locale.getDefault(), "$%,.2f", indemnizacionPorAntiguedad));
        txtPreavisoValue.setText(String.format(Locale.getDefault(), "$%,.2f", preaviso));
        txtIntegracionValue.setText(String.format(Locale.getDefault(), "$%,.2f", integracionMesDespido));
        txtAguinaldoProporcionalValue.setText(String.format(Locale.getDefault(), "$%,.2f", sacProporcional));
        txtVacacionesProporcionalesValue.setText(String.format(Locale.getDefault(), "$%,.2f", vacacionesProporcionales));
        txtTotalEstimadoValue.setText(String.format(Locale.getDefault(), "$%,.2f", totalEstimado));
    }

    //metodo para mostrar calendario
    private void showDatePickerDialog(final EditText editText) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    IndemnizacionActivity.this,
                    (view, year1, monthOfYear, dayOfMonth) -> {
                        Calendar selectedDate = Calendar.getInstance();
                        selectedDate.set(year1, monthOfYear, dayOfMonth);
                        updateLabel(editText, selectedDate);
                    },
                    year, month, day);
            datePickerDialog.show();
    }
//    Define un formato de fecha: String myFormat = "dd/MM/yyyy";
//    le dice cómo quieres que se vea la fecha (día, mes, año).
    void updateLabel(EditText editText, Calendar selectedDate) {
        String myFormat = "dd/MM/yyyy"; //Formato de fecha
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
        editText.setText(sdf.format(selectedDate.getTime()));
    }
}
