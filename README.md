# TPE - Criptografía y Seguridad

## Autores
- [Tomas Alvarez Escalante](https://github.com/tomalvarezz)
- [Alejo Caeiro](https://github.com/AleCaeiro)
- [Lucas Agustin Ferreiro](https://github.com/lukyferreiro)
- [Roman Gomez Kiss](https://github.com/rgomezkiss)


# Contexto

La criptografía visual es un concepto introducido en 1994 por Adi Shamir, quienes 
consideran un nuevo tipo de esquema criptográfico (como una extensión de esquemas
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

En este TPE se busca realizar un programa en lenguaje Java que implemente el algoritmo de Imagen
Secreta Compartida descripto en el documento [“(k,n) secret image sharing scheme capable of
cheating detection"](docs/Paper_Algoritmo.pdf).

Dicho algoritmo propone un esquema para compartir una imagen secreta basado en el método de Shamir.
Para lograr que la imagen que se oculta en las sombras sea prácticamente imperceptible, en el
documento se menciona la posibilidad de hacer uso esteganografía. 

De esta forma, el programa permitirá: 
- Distribuir una imagen secreta de extensión “.bmp” en otras imágenes también de extensión “.bmp” que serán las sombras en un esquema (k, n) de secreto compartido. 
- Recuperar una imagen secreta de extensión “.bmp” a partir de k imágenes, también de extensión “.bmp” 

# Instalación y compilación

Para correr el proyecto ... :