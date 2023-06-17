# TPE - Criptografía y Seguridad

## Autores - Grupo 15
- [Tomas Alvarez Escalante](https://github.com/tomalvarezz)
- [Alejo Caeiro](https://github.com/AleCaeiro)
- [Lucas Agustin Ferreiro](https://github.com/lukyferreiro)
- [Roman Gomez Kiss](https://github.com/rgomezkiss)


## Contexto

La criptografía visual es un concepto introducido en 1994 por Adi Shamir, quien 
considera un nuevo tipo de esquema criptográfico (como una extensión de esquemas
de secreto compartido) para decodificar imágenes secretas sin usar cálculos criptográficos clásicos.

Tanto Shamir como George Blakley exponen ([aqui](docs/Secreto_Compartido.pdf)) que guardar
la clave en un solo lugar es altamente riesgoso y guardar múltiples copias en diferentes
lugares solo aumenta la brecha de seguridad. Entonces el secreto (D) deberá dividirse en
un número fijo de partes (D1, D2, ..., Dn) de forma tal que: 
1. Conociendo un subconjunto de k cualesquiera de esas partes se pueda reconstruir D. 
2. Conociendo un subconjunto de k-1 cualesquiera de esas partes el valor D quede indeterminado. 

La esteganografía es la ciencia que se ocupa de la manera de ocultar un mensaje.
El objetivo es proteger información sensible, pero a diferencia de la criptografía que hace
ininteligible dicha información, la esteganografía logra que la información pase completamente
desapercibida al ocultar su existencia misma. 

# Introducción del TPE

En este TPE se realizó un programa en lenguaje Java que implementa el algoritmo de Imagen
Secreta Compartida descrito en el documento [“(k,n) secret image sharing scheme capable of
cheating detection"](docs/Paper_Algoritmo.pdf).

Dicho algoritmo propone un esquema para compartir una imagen secreta basado en el método de Shamir.
Para lograr que la imagen que se oculta en las sombras sea prácticamente imperceptible, en el
documento se menciona la posibilidad de hacer uso esteganografía. 

De esta forma, el programa permitirá: 
- Distribuir una imagen secreta de extensión “.bmp” en otras imágenes también de extensión “.bmp” que serán las sombras en un esquema (k, n) de secreto compartido. 
- Recuperar una imagen secreta de extensión “.bmp” a partir de k imágenes, también de extensión “.bmp” 

# Requisitos

- [Java JDK 17 LTS](https://www.oracle.com/java/technologies/downloads/#java17)

Nota: este programa fue testeado en Pampero con la versión Java JDK 18.
Para versiones anteriores, el programa fue testeado únicamente con Java 15, 16 y 17.

# Compilación

Para compilar se debe estar posicionado en la carpeta raiz del proyecto y correr:

```shell
./compile.sh
```

Esto generará un archivo ss.jar.

# Ejecución

Para ejecutar el programa se debe estar posicionado en la carpeta raiz del proyecto y correr:

```shell
java -jar ./ss.jar <modo> <imagenSecreta> <k> <directorio>
```

donde:
- **Modo**: tiene dos posibles valores:
  - **d**: para distribuir una imagen secreta en otras imágenes.
  - **r**: para recuperar una imagen secreta a partir de otras imágenes.
- **imagenSecreta**: corresponde al nombre de un archivo de extensión .bmp, y tiene dos usos según el modo elegido:
  - Con la opción (**d**) este archivo debe existir, ya que es la imagen a ocultar y debe ser una imagen en blanco y negro (8 bits por pixel)
  - Con la opción (**r**) será el nombre del archivo de salida, con la imagen secreta revelada.
- **k**: es la cantidad mínima de sombras necesarias para recuperar el secreto en un esquema (k, n).
- **directorio**: path a un directorio que debe contener imágenes de extensión .bmp, de 8 bits por píxel, de igual tamaño que la imagen secreta. 
Además, deberá verificarse que existan por lo menos k imágenes en el directorio. Este parámetro tiene dos posibles usos según el modo:
  - Si se elige el modo (**d**), es el path al directorio donde se encuentran las imágenes en las que se distribuirá el secreto
  - Si se elige el modo (**r**), es el path al directorio donde están las imágenes que contienen oculto el secreto.

## Ejemplo de ejecución

### Recuperación

Si corremos:

```shell
java -jar ./ss.jar r ./src/resources/secretImage/secreto.bmp 5 ./src/resources/grupo15
```

obtendremos la imagen ocultada por la catedra (k=5) en el archivo */src/resources/secretImage/secreto.bmp*

### Distribución

Si corremos:

```shell
java -jar ./ss.jar d ./src/resources/secretImage/secreto.bmp 5 ./src/resources/grupo15 
```

esconderemos la imagen *./src/resources/secretImage/secreto.bmp* con k=5 las imagenes de la carpeta */src/resources/grupo15* 

