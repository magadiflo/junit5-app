# JUnit 5

Para crear una clase de test a partir de la clase que deseamos probar,
podemos hacerlo de dos formas:

### Manual

Dentro del package test/java, crear el mismo package de la clase que se va a probar,
en nuestro caso:  
**org.magadiflo.junit5.app.models**  
Es decir, por convención, nuestro archivo de test debe tener el mismo package de
la clase original. Además, la clase debe finalizar con Test  
**CuentaTest**

### Usando Intellij IDEA

Para automatizar la creación de nuestro archivo de prueba con nuestro IDE de Intellij,
debemos situarnos en la clase a probar y usar una
de las siguientes opciones que se muestran a continuación:

Opción 1

```
Alt + Insert
opción Test...
```

Opción 2

```
Click en el nombre de la clase
Alt + Enter
Opcion Create Test...
```

## Ejecutar Test

Para ejecutar el test, podemos usar lo siguiente

Opción 1

```
Click derecho en el archivo de prueba
Run 'ArchivoTest'
```

Opción 2

```
Control + shift + F10
```

NOTA:

- Si esas opciones se ejecutan sobre un método
  en específico, solo se ejecutará el test de dicho método,
  en caso el cursor esté posicionado en el nombre de la clase o un area
  que no sea un método ejecutará todas las pruebas
- La ejecución del orden de las pruebas no es secuencial, es decir
  puede que se ejecute primero el método A y luego el método B, o en otro caso
  primero el método B y luego el método A, eso depende del motor de pruebas de
  JUnit, y eso está bien, ya que al ser pruebas unitarias se, nos centramos únicamente
  en probar el método, y este no debería tener relación con otros métodos

## Creando variable del sistema

En el apartado de ejecución del proyecto, clickeamos en el select y
seleccioamos: **Edit configurations...**

Se abrirá una ventana, en el lado izquierdo seleccinoamos **CuentaTest** y
en la parte derecha, sección **Build an run** observaremos una propiedad
colocada -ea, solo le agrgaremos lo siguiente **-DENV=dev,**
finalmente querdaría: **-ea -DENV=dev**, de esa manera hemos agregado
nuestra propiedad ENV y su valor dev.

NOTA: -D, significa que vamos a configurar una propiedad del sistema
(System property)

## Creando variable de entorno

Similar a la creación de la variable del sistema, pero esta vez la variable
se creará en el apartado de **Environment variables**  
ENVIRONMENT=dev

## Ejecutar una etiqueta en particular

- Menú desplegable Select Run/Debug configuration
- Edit Configurations...
- En la sección de **Build and run**
- Por defecto está seleccionado Class, nosotros seleccionaremos Tags
- En el campo agregar las etiquetas a ejecutar, ejemplo: param

## Ejecutar pruebas sin el IDE, solo desde una Terminal

Debemos agregar el siguiente plugin al pom.xml

```
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-surefire-plugin</artifactId>
    <version>2.22.2</version>
    
    // Si queremos ejecutar un tag en particular, sino podemos omitir este <configuration>
    <configuration>
        <groups>cuenta</groups>
    </configuration>
</plugin>
```

Luego hay dos formas de ejecutar:

- **Primera opción:** Como IntelliJ viene con el maven integrado es que podemos ejecutar
  desde la opción Maven/junit5-app/Lifecycle/test. Pero esto sería casi lo mismo que usar
  el IDE, y lo que queremos es no usar el ide, sino el terminal. Entonces vamos a la segunda opción.

- **Segunda opción:** Descargar maven y configurar las variables de entorno del sistema con
  la ruta donde se instaló maven hasta el directorio \bin, similar a como configuramos el
  JAVA_HOME para la ruta de java, luego ir a la raíz del proyecto donde
  está el pom.xml, copiar toda la ruta, mediante cmd posicionarnos en dicha ruta
  y ejecutar:

```
  mvn test 
```