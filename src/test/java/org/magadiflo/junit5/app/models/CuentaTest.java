package org.magadiflo.junit5.app.models;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.magadiflo.junit5.app.exceptions.DineroInsuficienteException;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.*;

/***
 * Si observamos, tanto la clase como el método tienen
 * el modificar por default (package), esto es como buena
 * práctica, esto es para poder usarlo únicamente dentro
 * del contexto del package de test de prueba unitaria y
 * que no se pueda acceder en todas partes, es por eso
 * que debemos evitar que sea del tipo public
 */

class CuentaTest {

    Cuenta cuenta;

    @BeforeEach
        //Que se ejecute antes de iniciar cada método test
    void initMetodoTest() {
        this.cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));
        System.out.println("Iniciando el método!");
    }

    @AfterEach
        //Que se ejecute después que finalice la ejecución de cada método test
    void tearDown() {
        System.out.println("Finalizando método de prueba");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("Inicializando el test de Cuenta");
        System.out.println("-------------------------------");
    }

    @AfterAll
    static void afterAll() {
        System.out.println("-------------------------------");
        System.out.println("Finalizando el test de Cuenta");
    }

    //Clase anidada, solo la implementamos cuando queremos organizar nuestras pruebas
    //Con @Nested, indicamos que estamos organizando las pruebas en esta clase.
    //Si un método dentro de la clase falla, falla toda la clase completa
    @Nested
    @DisplayName(value = "Probando atributos de la cuenta corriente")
    class CuentaTestNombreSaldo {
        /**
         * Cuando usamos expresiones lambda para mostrar el mensaje de error de los assertions,
         * esta solo se va a ejecutar solo si el assertion falla. Mientras que, si se
         * coloca el mensaje de forma literal, siempre se creará una instancia del string del
         * mensaje, haciéndolo ineficiente.
         * Si la prueba pasa, usando las expresiones lambda, el mensaje nunca se van
         * a instanciar, y la razón es porque con las expresiones lambda estamos pasando
         * una invocación futura de un método, no se va a crear el String de forma inmediata,
         * solo si el assertion falla
         */

        @Test
        @DisplayName(value = "el nombre!")
        void testNombreCuenta() {
            //Cuenta cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));

            String esperado = "Martín";
            String real = cuenta.getPersona();

            assertNotNull(real, () -> "La cuenta no puede ser nula");
            assertEquals(esperado, real, () -> String.format("El nombre de la cuenta no es el que se esperaba. Se esperaba %s pero se obtuvo %s", esperado, real));
            assertTrue(real.equals("Martín"), () -> "Nombre cuenta esperada debe ser igual a la real");
        }

        @Test
        @DisplayName(value = "el saldo!")
        void testSaldoCuenta() {
            //Cuenta cuenta = new Cuenta("Martín", new BigDecimal("1000.12345"));

            assertNotNull(cuenta.getSaldo());
            assertEquals(1000.12345d, cuenta.getSaldo().doubleValue());
            assertFalse(cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); //compareTo: -1, 0, 1
            assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //compareTo: -1, 0, 1
        }

        @Test
        @DisplayName(value = "Testeando referencias que sean iguales")
        void testReferenciaCuenta() {
            Cuenta cuentaActual = new Cuenta("Alicia Flores", new BigDecimal("2500.50"));
            Cuenta cuentaEsperada = new Cuenta("Alicia Flores", new BigDecimal("2500.50"));

            //Se comparan por valor, ya que agregamos en la clase Cuenta el método equals(...)
            assertEquals(cuentaEsperada, cuentaActual);
        }
    }

    @Nested
    class CuentaOperaciones {
        @Test
        void testDebitoCuenta() { //Débito, restará el monto proporcionado a nuestro saldo
            cuenta = new Cuenta("Gaspar", new BigDecimal("1000.00"));
            cuenta.debito(new BigDecimal("100"));

            assertNotNull(cuenta.getSaldo());
            assertEquals(900, cuenta.getSaldo().intValue());
            assertEquals("900.00", cuenta.getSaldo().toPlainString());
        }

        @Test
        void testCreditoCuenta() { //Crédito, sumará el monto proporcionado a nuestro saldo
            cuenta = new Cuenta("Gaspar", new BigDecimal("1000.00"));
            cuenta.credito(new BigDecimal("100"));

            assertNotNull(cuenta.getSaldo());
            assertEquals(1100, cuenta.getSaldo().intValue());
            assertEquals("1100.00", cuenta.getSaldo().toPlainString());
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
    }

    @Test
    void testDineroInsuficienteExceptionCuenta() {
        this.cuenta = new Cuenta("Alicia", new BigDecimal("1000.00"));

        //Para los manejos de excepciones los haremos con expresiones Lambda (función de flecha)
        Exception exception = assertThrows(DineroInsuficienteException.class, () -> {
            this.cuenta.debito(new BigDecimal("1500"));
        });

        String mensajeActual = exception.getMessage();
        String mensajeEsperado = "Dinero Insuficiente";
        assertEquals(mensajeEsperado, mensajeActual);
    }

    /**
     * Cuando tenemos varios Assertions dentro de un método test, y no manejemos el
     * assertAll, ocurrirá que cuando ejecutemos el test, y uno de los assertions falle,
     * todos los demás assertions siguientes no serán ejecutados. Por el contrario, si usamos
     * el assertAll, esto permitirá que los demás assertions se ejecuten
     * sin problemas, así falle algún assertion. De esta forma podremos ver qué
     * assertions pasaron y cuáles fallaron.
     *
     * @Disabled, esta prueba no se ejecutará, así tenga errores, con disabled nos saltamos
     * esta prueba y continuamos con las demás
     * <p>
     * fail(), forzamos a que falle el método test testRelacionBancoCuentas(),
     * esto para probar la anotación @Disabled, ya que estamos suponiendo que ese método de prueba
     * falla testRelacionBancoCuentas() y como quiero probar los demás métodos sin tomar en cuenta
     * ese que falla, es que quiero saltarme esa prueba para ver la ejecución de las demás
     */
    @Test
    //@Disabled
    @DisplayName(value = "Probando relaciones entre las cuentas y el banco con assertAll")
    void testRelacionBancoCuentas() {
        //fail();

        Cuenta cuentaOrigen = new Cuenta("Alicia", new BigDecimal("2500"));
        Cuenta cuentaDestino = new Cuenta("Rachul", new BigDecimal("1000"));

        Banco banco = new Banco();
        banco.setNombre("Banco de la Nación");
        banco.addCuenta(cuentaOrigen).addCuenta(cuentaDestino);
        banco.transferir(cuentaOrigen, cuentaDestino, new BigDecimal("500"));

        assertAll(
                () -> assertEquals("2000", cuentaOrigen.getSaldo().toPlainString(), () -> "El valor del saldo de la cuentaOrigen no es el esperado"),
                () -> assertEquals("1500", cuentaDestino.getSaldo().toPlainString()),
                () -> assertEquals(2, banco.getCuentas().size()),
                () -> assertEquals("Banco de la Nación", cuentaOrigen.getBanco().getNombre()),
                () -> assertEquals("Alicia", banco.getCuentas().stream().filter(cuenta -> cuenta.getPersona().equals("Alicia")).findFirst().get().getPersona()),
                () -> assertTrue(banco.getCuentas().stream().anyMatch(cuenta -> cuenta.getPersona().equals("Alicia")))
        );
    }

    @Nested
    class SistemaOperativoTest {
        @Test
        @EnabledOnOs(OS.WINDOWS)
        void testSoloWindows() {

        }

        @Test
        @EnabledOnOs({OS.LINUX, OS.MAC})
        void testSoloLinuxMac() {

        }

        @Test
        @DisabledOnOs(OS.WINDOWS)
        void testNoWindows() {

        }
    }

    @Nested
    class JavaVersionTest {
        @Test
        @EnabledOnJre(JRE.JAVA_8)
        void testSoloEnJDK8() {
        }

        @Test
        @EnabledOnJre(JRE.JAVA_17)
        void testSoloJDK17() {
        }

        @Test
        @DisabledOnJre(JRE.JAVA_17)
        void testNoJDK17() {
        }
    }

    @Nested
    class SystemPropertiesTest {
        @Test
        void testImprimirSystemProperties() {
            Properties properties = System.getProperties();
            properties.forEach((key, value) -> System.out.printf("%s : %s %n", key, value));
        }

        @Test
        @EnabledIfSystemProperty(named = "java.version", matches = "17.*")
            //matches, se puede usar expresiones regulares o sin expresiones regulares
        void testJavaVersion() {

        }

        @Test
        @DisabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testSolo64Bits() {
        }

        @Test
        @EnabledIfSystemProperty(named = "os.arch", matches = ".*32.*")
        void testNo64Bits() {
        }

        @Test
        @EnabledIfSystemProperty(named = "user.name", matches = "USUARIO")
        void testUsernameOS() {
        }

        @Test
        @EnabledIfSystemProperty(named = "ENV", matches = "dev")
        void testDev() {

        }
    }

    class VariableAmbienteTest {
        @Test
        void testImprimirVariablesAmbiente() {
            Map<String, String> getEnvironment = System.getenv();
            getEnvironment.forEach((key, value) -> System.out.printf("%s = %s %n", key, value));
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "JAVA_HOME", matches = ".*jdk-17.0.4.1.*")
        void testJavaHome() {
        }

        @Test
        @EnabledIfEnvironmentVariable(named = "NUMBER_OF_PROCESSORS", matches = "8")
        void testProcesadores() {

        }

        @Test
        @EnabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "dev")
        void testEnv() {
        }

        @Test
        @DisabledIfEnvironmentVariable(named = "ENVIRONMENT", matches = "prod")
        void testEnvProdDisabled() {
        }
    }

    /***
     * Assumptions, es para evaluar una expresión true o false de forma programática.
     * assumeTrue(...), asumimos que el valor contenido será un true, si se cumple continuamos, si no se cumple no se ejecuta la prueba
     */
    @Test
    void testSaldoCuentaDev() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));
        // esDev, dentro del assumeTrue(...), Si esto devuelve true, se ejecutan las líneas siguientes (assertNotNull....),
        // caso contrario no se ejecutarán
        assumeTrue(esDev);

        assertNotNull(this.cuenta.getSaldo());
        assertEquals(1000.12345d, this.cuenta.getSaldo().doubleValue());
        assertFalse(this.cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); //compareTo: -1, 0, 1
        assertTrue(this.cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //compareTo: -1, 0, 1
    }

    @Test
    void testSaldoCuentaDev2() {
        boolean esDev = "dev".equals(System.getProperty("ENV"));

        //Nuestro ambiente de desarrollo está en dev, por lo tanto este sí se ejecutará
        assumingThat(esDev, () -> {
            assertNotNull(this.cuenta.getSaldo());
            assertEquals(1000.12345d, this.cuenta.getSaldo().doubleValue());
        });

        //Estas pruebas sí se ejecutarán
        assertFalse(this.cuenta.getSaldo().compareTo(BigDecimal.ZERO) < 0); //compareTo: -1, 0, 1
        assertTrue(this.cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0); //compareTo: -1, 0, 1

    }

    //Indicamos que repetiremos 5 veces la prueba, esto provocará que en los resultados se muestren
    // las 5 repeticiones de esta prueba. Esto lo podríamos usar, por ejemplo, cuando hayan datos aleatorios
    @RepeatedTest(value = 5, name = "{displayName}: Repetición número {currentRepetition} de {totalRepetitions}")
    @DisplayName(value = "Probando debito cuenta repetir")
    void testDebitoCuentaRepetir(RepetitionInfo info) { //El framework aplica DI en esos parámetros (Se obtienen la repetición actual y el total de repeticiones)
        if (info.getCurrentRepetition() == 3) {
            System.out.println("Estamos en la repetición " + info.getCurrentRepetition());
        }

        cuenta = new Cuenta("Gaspar", new BigDecimal("1000.00"));
        cuenta.debito(new BigDecimal("100"));

        assertNotNull(cuenta.getSaldo());
        assertEquals(900, cuenta.getSaldo().intValue());
        assertEquals("900.00", cuenta.getSaldo().toPlainString());
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {argumentsWithNames}")
    @ValueSource(strings = {"100", "200", "300", "500", "700", "1000"})
    void testDebitoCuentaParametrizadoValueSource(String monto) {
        this.cuenta = new Cuenta("Gaspar", new BigDecimal("1000.50"));
        this.cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {argumentsWithNames}")
    @CsvSource({"1,100", "2,200", "3,300", "4,500", "5,700", "6,1000"})
    void testDebitoCuentaParametrizadoCsvSource(String index, String monto) {
        System.out.printf("%s -> %s %n", index, monto);
        this.cuenta = new Cuenta("Gaspar", new BigDecimal("1000.50"));
        this.cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {argumentsWithNames}")
    @CsvFileSource(resources = "/data.csv")
    void testDebitoCuentaParametrizadoCsvFileSource(String monto) {
        this.cuenta = new Cuenta("Gaspar", new BigDecimal("1000.50"));
        this.cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    @ParameterizedTest(name = "número {index} ejecutando con valor {argumentsWithNames}")
    @MethodSource(value = "montoList")
    void testDebitoCuentaMethodSource(String monto) {
        this.cuenta = new Cuenta("Gaspar", new BigDecimal("1000.50"));
        this.cuenta.debito(new BigDecimal(monto));

        assertNotNull(cuenta.getSaldo());
        assertTrue(cuenta.getSaldo().compareTo(BigDecimal.ZERO) > 0);
    }

    static List<String> montoList() {
        return Arrays.asList("100", "200", "300", "500", "700", "1000");
    }
}