# Subir imagenes de la galeria o tomando una foto a Firebase y a Oracle

En este repositorio aprenderemos a seleccionar una foto de la galería o por medio de una foto, después la subiremos a Firebase Storage y luego guardaremos el link de la foto en Firebase en una base de datos en Oracle. </br>
Para esto, este proyecto tiene esta interfaz que simula una pantalla de registro de usuarios donde podemos guardar un usuario con correo eletrónico, contraseña y una foto de perfil: </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/3c86ca41-d887-4a8f-b0ee-41865b83dd67)</br>
</br></br>
* Cosas importantes antes de empezar: </br>
Necesitamos colocar los permisos de acceso a la camara y a la galeria en el AndroidManifest.xml </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/b0aef078-9e9d-43e2-8a5a-d0f533a6fab0)</br>
</br></br>

<hr>

Empezamos declarando constantes y variables a nivel de clase, declararlas aqui hace que pueda acceder a ellas desde cualquier función de esta clase (no solo desde la función onCreate)</br>
Vean como inicio declarando constantes que tienen códigos, estas se declaran cuando tengo dos o mas opciones que el usuario puede escoger, me ayuda a asignarle un código a cada opción, por ejemplo, si el usuario tiene la opción para escoger la imagen desde galeria o desde la camara, entonces, para identificar que opción escogió lo hago con estos códigos, de tal manera que si el código es "102" es por que escogió la galeria y si el código es "103" es por que eligió la camara. <Strong>Estos códigos pueden ser cualquiera, no hay un numero en especifico</Strong> </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/29d5ec88-6fdb-4561-827c-17197433d805) </br>
Luego, declaro variable que se van a inicializar después, sabemos que inicializar es asignarle un valor a las variables, por ejemplo: val nombreProfe = "Exequiel", esta variable ya está inicializada por que se el valor que va a contener, pero, si no se cual es el valor o no la quiero inicializar al inicio, entonces la declaro como <Strong>lateinit var</Strong> solo funciona con variables (var) y solo puede aplicarse a tipos de referencia (como objetos) y a cadenas de String, no a tipos primitivos (como Int, Double, etc.). </br>
Tenemos que tener cuidado y asegurarse que la variable lateinit sea inicializada antes de que se acceda a ella usarla o mostrarla en pantalla, de lo contrario, se lanzará un error llamado UninitializedPropertyAccessException.</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/0952629f-41de-4215-98b2-a79b892bbdfd)</br>

Luego, creo una constante que contiene un UUID, ya que quiero que con este UUID se guarde el usuario y el nombre de la foto, si los dos tienen la misma foto, es más semcillo luego buscar la foto asignada al usuario</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/88787aa3-a1c6-4f97-99f5-08961b3f5f1d)



