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