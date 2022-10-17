package org.magadiflo.junit5.app.models;

import org.junit.jupiter.api.Test;
import org.magadiflo.junit5.app.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/***
 * Si observamos, tanto la clase como el método tienen
 * el modificar por default (package), esto es como buena
 * práctica, esto es para poder usarlo únicamente dentro
 * del contexto del package de test de prueba unitaria y
 * que no se pueda acceder en todas partes, es por eso
 * que debemos evitar que sea del tipo public
 */

class CuentaTest {

    @Test
    void testNombreCuenta() {
        Cuenta cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));

        String esperado = "Martín";
        String real = cuenta.getPersona();

        assertNotNull(real);
        assertEquals(esperado, real);
        assertTrue(real.equals("Martín"));
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1000.12345d, cuenta.getSaldo().doubleValue());
        assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); //compareTo: -1, 0, 1
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //compareTo: -1, 0, 1
    }

    @Test
    void testReferenciaCuenta() {
        Cuenta cuentaActual = new Cuenta("Alicia Flores", new BigDecimal("2500.50"));
        Cuenta cuentaEsperada = new Cuenta("Alicia Flores", new BigDecimal("2500.50"));

        //Se comparan por valor, ya que agregamos en la clase Cuenta el método equals(...)
        assertEquals(cuentaEsperada, cuentaActual);
    }

    @Test
    void testDebitoCuenta() { //Débito, restará el monto proporcionado a nuestro saldo
        Cuenta cuenta = new Cuenta("Gaspar", new BigDecimal("1000.00"));
        cuenta.debito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testCreditoCuenta() { //Crédito, sumará el monto proporcionado a nuestro saldo
        Cuenta cuenta = new Cuenta("Gaspar", new BigDecimal("1000.00"));
        cuenta.credito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(1100, cuenta.getSaldo().intValue());
        assertEquals("1100.00", cuenta.getSaldo().toPlainString());
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        Cuenta cuenta = new Cuenta("Alicia", new BigDecimal("1000.00"));

        //Para los manejos de excepciones los haremos con expresiones Lambda (función de flecha)
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            cuenta.debito(new BigDecimal("1500"));
        });

        String mensajeActual = exception.getMessage();
        String mensajeEsperado = "Dinero Insuficiente";
        assertEquals(mensajeEsperado, mensajeActual);
    }

    @Test
    void testTransferirDineroCuentas() {
        Cuenta cuentaOrigen = new Cuenta("Alicia", new BigDecimal("2500"));
        Cuenta cuentaDestino = new Cuenta("Rachul", new BigDecimal("1000"));

        Banco banco = new Banco();
        banco.setNombre("Banco de la Nación");
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal("500"));

        assertEquals("2000", cuentaOrigen.getSaldo().toPlainString());
        assertEquals("1500", cuentaDestino.getSaldo().toPlainString());
    }

    /**
     * Cuando tenemos varios Assertions dentro de un método test, y no manejemos el
     * assertAll, ocurrirá que cuando ejecutemos el test, y uno de los assertions falle,
     * todos los demás assertions siguientes no serán ejecutados. Por el contrario, si usamos
     * el assertAll, esto permitirá que los demás assertions se ejecuten
     * sin problemas, así falle algún assertion. De esta forma podremos ver qué
     * assertions pasaron y cuáles fallaron
     */
    @Test
    void testRelacionBancoCuentas() {
        Cuenta cuentaOrigen = new Cuenta("Alicia", new BigDecimal("2500"));
        Cuenta cuentaDestino = new Cuenta("Rachul", new BigDecimal("1000"));

        Banco banco = new Banco();
        banco.setNombre("Banco de la Nación");
        banco.addCuenta(cuentaOrigen).addCuenta(cuentaDestino);
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal("500"));

        assertAll(
                () -> assertEquals("2000", cuentaOrigen.getSaldo().toPlainString()),
                () -> assertEquals("1500", cuentaDestino.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Banco de la Nación", cuentaOrigen.getBanco().getNombre()),
                () -> assertEquals("Alicia", banco.getCuentas().stream().filter(cuenta -> cuenta.getPersona().equals("Alicia")).findFirst().get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream().anyMatch(cuenta -> cuenta.getPersona().equals("Alicia")))
        );
    }
}