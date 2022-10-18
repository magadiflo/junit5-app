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

## Creando variable de 
En el apartado de ejecución del proyecto, clickeamos en el select y
seleccioamos: **Edit configurations...**

Se abrirá una ventana, en el lado izquierdo seleccinoamos **CuentaTest** y 
en la parte derecha, sección **Build an run** observaremos una propiedad
colocada -ea, solo le agrgaremos lo siguiente **-DENV=dev,**
finalmente querdaría: **-ea -DENV=dev**, de esa manera hemos agregado 
nuestra propiedad ENV y su valor dev. 

NOTA: -D, significa que vamos a configurar una propiedad del sistema
(System property)