package domain.valueobjects;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

public class Divisa {
    private final BigDecimal monto;
    private final Currency divisa;

    /**
     * Constructor privado para que la creación de instancias
     * se maneje de manera interna para la inmutabilidad
     * @param monto monto a inicializar la instancia
     * @param divisa código de divisa a crear la moneda
     */
    private Divisa(BigDecimal monto, Currency divisa) {
        // obtiene la cantidad de decimales para la moneda escogida
        // para CLP por defecto 2
        int cantidadDecimales = divisa.getDefaultFractionDigits();

        // Establece el número de decimales y el formato de redondeo
        // indicando que redondeará cuando sea mayor o igual a 0.5
        this.monto = monto.setScale(cantidadDecimales, RoundingMode.HALF_UP);
        this.divisa = divisa;
    }

    /**
     * Método "factory" privado que crea una moneda o divisa dado un monto
     * y currency elegido.
     *
     * @param monto  monto a inicializar la instancia
     * @param divisa divisa seleccionada
     * @return instancia de divisa con el monto y código de divisa seleccionado
     */
    private static Divisa of(BigDecimal monto, Currency divisa) {
        // Hace las validaciones necesarias antes de crear la instancia
        validaMontoNoNulo(monto);
        validaDivisaNoNula(divisa);

        return new Divisa(monto, divisa);
    }

    /**
     * Método de creación concreto para divisa en peso chileno
     * @param monto monto a inicializar la instancia
     * @return instancia de divisa inmutable
     */
    public static Divisa pesos(double monto) {
        // convierte el tipo de número a un BigDecimal
        BigDecimal montoBigDecimal = BigDecimal.valueOf(monto);

        // Llama al factory de divisas o monedas que realiza los pasos o
        // lógicas necesarias para su creación
        return of(montoBigDecimal, Currency.getInstance("CLP"));
    }

    /**
     * Método de creación concreto para divisa en peso chileno
     * @param monto monto a instanciar
     * @return instancia de divisa inmutable
     */
    public static Divisa pesos(BigDecimal monto) {
        return of(monto, Currency.getInstance("CLP"));
    }

    // Operaciones matemáticas para una divisa

    /**
     * Método que permite sumar 2 montos con una misma divisa
     * @param otro instancia de divisa que se desea sumar
     * @return instancia de moneda con el nuevo monto
     */
    public Divisa sumar(Divisa otro) {
        validaMismaDivisa(otro);
        return new Divisa(monto.add(otro.monto), divisa);
    }

    /**
     * Método que permite restar 2 montos con una misma divisa
     * @param otro instancia de divisa que se desea restar
     * @return instancia de moneda con el nuevo monto
     */
    public Divisa restar(Divisa otro) {
        validaMismaDivisa(otro);
        return new Divisa(monto.subtract(otro.monto), divisa);
    }

    /**
     * Método que permite multiplicar un monto de una divisa con
     * otra, siempre y cuando posean la misma divisa
     * @param otro instancia de divisa que se desea multiplicar
     * @return instancia de moneda con el nuevo monto
     */
    public Divisa multiplicar(Divisa otro) {
        validaMismaDivisa(otro);
        return new Divisa(monto.multiply(otro.monto), divisa);
    }

    /**
     * Método que permite dividir un monto de una divisa con otra,
     * siempre y cuando posean la misma divisa
     * @param otro instancia de divisa que se desea dividir
     * @return instancia de moneda con el nuevo monto
     */
    public Divisa dividir(Divisa otro) {
        validaMismaDivisa(otro);
        return new Divisa(monto.divide(otro.monto, RoundingMode.HALF_UP), divisa);
    }

    // Comparaciones

    /**
     * Método que compara si la divisa es mayor a otra
     * @param otro otra divisa a comparar
     * @return true si es mayor
     */
    public boolean esMayorQue(Divisa otro) {
        validaMismaDivisa(otro);
        return monto.compareTo(otro.monto) > 0;
    }

    /**
     * Método que compara si la divisa es mayor o igual a otra
     * @param otro otra divisa a comparar
     * @return true si es mayor o igual
     */
    public boolean esMayorOIgualQue(Divisa otro) {
        validaMismaDivisa(otro);
        return monto.compareTo(otro.monto) >= 0;
    }

    /**
     * Método que compara si la divisa es menor a otra
     * @param otro otra divisa a comparar
     * @return true si la divisa es menor que la pasada por argumento
     */
    public boolean esMenorQue(Divisa otro) {
        validaMismaDivisa(otro);
        return monto.compareTo(otro.monto) < 0;
    }

    /**
     * Método que compara si la divisa es menor o igual a otra
     * @param otro otra divisa a comparar
     * @return true si la divisa es menor o igual a la pasada por argumento
     */
    public boolean esMenorOIgualQue(Divisa otro) {
        validaMismaDivisa(otro);
        return monto.compareTo(otro.monto) <= 0;
    }

    /**
     * Indíca si el monto de la divisa es cero
     * @return true si es cero
     */
    public boolean esCero() {
        return monto.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * Indíca si el monto de la divisa es positivo
     * @return true si es positivo (mayor a cero)
     */
    public boolean esPositivo() {
        return monto.compareTo(BigDecimal.ZERO) > 0;
    }

    /**
     * Indíca si el monto de la divisa es negativo
     * @return true si es negativo (menor a cero)
     */
    public boolean esNegativo() {
        return monto.compareTo(BigDecimal.ZERO) < 0;
    }

    /**
     * Obtiene el monto de la divisa
     * @return obtiene el monto
     */
    public BigDecimal getMonto() {
        return monto;
    }

    /**
     * Método que válida si la divisa pasada por argumento
     * es igual a la consultada. En caso de que ambas instancias posean
     * distintas divisas lanza un error IllegalArgumentException
     * @param otro divisa a validar
     */
    private void validaMismaDivisa(Divisa otro) {
        validaDivisaNoNula(otro.divisa);

        // Verifica si las divisas son iguales antes de hacer operaciones
        if (!this.divisa.equals(otro.divisa)) {
            throw new IllegalArgumentException("No se puede operar con divisas diferentes");
        }
    }

    /**
     * Método que valída que el monto de la divisa no sea un valor nulo,
     * en caso de ser nulo, lanza un error IllegalArgumentException
     * @param monto monto a validar
     */
    private static void validaMontoNoNulo(BigDecimal monto) {
        if ( monto == null ) {
            throw new IllegalArgumentException("El monto no puede ser nulo");
        }
    }

    /**
     * Método que valída que el código de la divisa no sea un valor nulo,
     * en caso de ser nulo, lanza un error IllegalArgumentException
     * @param divisa divisa a validar
     */
    private static void validaDivisaNoNula(Currency divisa) {
        if ( divisa == null ) {
            throw new IllegalArgumentException("La divisa no puede ser nulo");
        }
    }

    @Override
    public String toString() {
        return String.format("%s %,.0f",
                divisa.getSymbol(),
                monto.doubleValue());
    }
}
