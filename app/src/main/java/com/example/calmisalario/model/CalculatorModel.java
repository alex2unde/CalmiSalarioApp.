package com.example.calmisalario.model;

import java.util.HashMap;
import java.util.Map;

public class CalculatorModel {

    public static class CalculationResult {
        public double sueldoBasico;
        public double adicionalAntiguedad;
        public double horasExtras50;
        public double horasExtras100;
        public double adicionalTitulo;
        public double presentismo;
        public double presentismoAdicional;
        public double asignacionNoRemunerativa;
        public double refrigerio;
        public double jubilacion;
        public double ley19032;
        public double sindicato;
        public double sepelio;
        public double obraSocial;
        public double sueldoFinal;
    }

    private static final Map<String, Double> valorCategoria = new HashMap<>();
    private static final Map<String, Double> valorHora = new HashMap<>();
    private static final Map<String, Double> asigNoRemMap = new HashMap<>();

    static {
        valorCategoria.put("Op. Comun", 484592.0);
        valorCategoria.put("Op. Especializado", 533052.0);
        valorCategoria.put("Op. Calificado", 571819.0);
        valorCategoria.put("Op. Foguista", 610586.0);

        valorHora.put("Op. Comun", 2563.0);
        valorHora.put("Op. Especializado", 2820.0);
        valorHora.put("Op. Calificado", 3025.0);
        valorHora.put("Op. Foguista", 3230.0);

        asigNoRemMap.put("Op. Comun", 160554.0);
        asigNoRemMap.put("Op. Especializado", 176610.0);
        asigNoRemMap.put("Op. Calificado", 202299.0);
        asigNoRemMap.put("Op. Foguista", 208721.0);
    }

    private static final double TITULO = 24230;
    private static final double PRESENT_PERFEC = 24230;
    private static final double PRESENT_COMPLET = 48460;
    private static final double REFRIGERIO = 163785;
    private static final double SEPULIO = 12943;
    private static final double JUBILACION_RATE = 0.11;
    private static final double LEY19032_RATE = 0.03;
    private static final double SINDICATO_RATE = 0.023;
    private static final double OBRA_SOCIAL_RATE = 0.03;

    public CalculationResult calculate(
            String categoria, int antiguedad, boolean tieneTitulo, boolean trabajoMesCompleto,
            int horasTrabajadas, int horasExt50, int horasExt100, boolean tieneObraSocial, double adelanto) {

        CalculationResult result = new CalculationResult();

        Double valor = valorCategoria.get(categoria);
        double valorBaseCategoria = (valor != null) ? valor : 0.0;

        Double hora = valorHora.get(categoria);
        double valorBaseHora = (hora != null) ? hora : 0.0;

        if (trabajoMesCompleto) {
            result.sueldoBasico = valorBaseCategoria;
        } else {
            result.sueldoBasico = valorBaseHora * horasTrabajadas;
        }

        result.adicionalAntiguedad = result.sueldoBasico * 0.01 * antiguedad;
        result.adicionalTitulo = tieneTitulo ? TITULO : 0;

        result.presentismo = PRESENT_PERFEC;
        result.presentismoAdicional = PRESENT_COMPLET;
        result.refrigerio = REFRIGERIO;
        Double asig = asigNoRemMap.get(categoria);
        result.asignacionNoRemunerativa = (asig != null) ? asig : 0.0;

        double valorHoraCalculado = trabajoMesCompleto ? (valorBaseCategoria / 189) : valorBaseHora;
        result.horasExtras50 = valorHoraCalculado * 1.5 * horasExt50;
        result.horasExtras100 = valorHoraCalculado * 2 * horasExt100;

        double subTotalRemunerativo = result.sueldoBasico + result.adicionalAntiguedad + result.adicionalTitulo +
                result.presentismo + result.presentismoAdicional + result.horasExtras50 + result.horasExtras100;

        result.jubilacion = subTotalRemunerativo * JUBILACION_RATE;
        result.ley19032 = subTotalRemunerativo * LEY19032_RATE;
        result.sindicato = subTotalRemunerativo * SINDICATO_RATE;
        result.sepelio = SEPULIO;
        result.obraSocial = tieneObraSocial ? (subTotalRemunerativo * OBRA_SOCIAL_RATE) : 0;

        double totalDescuentos = result.jubilacion + result.ley19032 + result.sindicato +
                result.sepelio + result.obraSocial + adelanto;

        double totalHaberes = subTotalRemunerativo + result.refrigerio + result.asignacionNoRemunerativa;

        result.sueldoFinal = totalHaberes - totalDescuentos;

        return result;
    }
}
