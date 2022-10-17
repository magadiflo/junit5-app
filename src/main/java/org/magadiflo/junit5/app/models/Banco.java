package org.magadiflo.junit5.app.models;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Banco {

    private String nombre;

    private List<Cuenta> cuentas;

    public Banco() {
        this.cuentas = new ArrayList<>();
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Cuenta> getCuentas() {
        return cuentas;
    }

    public void setCuentas(List<Cuenta> cuentas) {
        this.cuentas = cuentas;
    }

    public Banco addCuenta(Cuenta cuenta) {
        this.cuentas.add(cuenta);
        return this;
    }

    public void transferir(Cuenta origen, Cuenta destino, BigDecimal monto) {
        origen.debito(monto);
        destino.credito(monto);
    }
}
