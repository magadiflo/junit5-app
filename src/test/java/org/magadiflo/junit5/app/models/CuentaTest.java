package org.magadiflo.junit5.app.models;

import org.junit.jupiter.api.Test;

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

        assertEquals(esperado, real);
        assertTrue(real.equals("Martín"));
    }

    @Test
    void testSaldoCuenta() {
        Cuenta cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));

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
}