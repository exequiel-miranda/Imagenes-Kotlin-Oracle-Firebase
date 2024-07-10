# Subir imagenes de la galeria o tomando una foto a Firebase y a Oracle

En este repositorio aprenderemos a seleccionar una foto de la galería o por medio de una foto, después la subiremos a Firebase Storage y luego guardaremos el link de la foto en Firebase en una base de datos en Oracle. </br>
Para esto, este proyecto tiene esta interfaz que simula una pantalla de registro de usuarios donde podemos guardar un usuario con correo eletrónico, contraseña y una foto de perfil: </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/3c86ca41-d887-4a8f-b0ee-41865b83dd67)</br>
</br></br>
* Cosas importantes antes de empezar: </br>
Necesitamos colocar los permisos de acceso a la camara y a la galeria en el AndroidManifest.xml </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/6c1c6b8c-528a-4f94-893b-75ef71d21d6d)
</br>
</br>

<hr>

Código en la función onCreate:
</br>

Empezamos declarando constantes y variables a nivel de clase, declararlas aqui hace que pueda acceder a ellas desde cualquier función de esta clase (no solo desde la función onCreate)</br>
Vean como inicio declarando constantes que tienen códigos, estas se declaran cuando tengo dos o mas opciones que el usuario puede escoger, me ayuda a asignarle un código a cada opción, por ejemplo, si el usuario tiene la opción para escoger la imagen desde galeria o desde la camara, entonces, para identificar que opción escogió lo hago con estos códigos, de tal manera que si el código es "102" es por que escogió la galeria y si el código es "103" es por que eligió la camara. <Strong>Estos códigos pueden ser cualquiera, no hay un numero en especifico</Strong> </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/29d5ec88-6fdb-4561-827c-17197433d805) </br>
Luego, declaro variable que se van a inicializar después, sabemos que inicializar es asignarle un valor a las variables, por ejemplo: val nombreProfe = "Exequiel", esta variable ya está inicializada por que se el valor que va a contener, pero, si no se cual es el valor o no la quiero inicializar al inicio, entonces la declaro como <Strong>lateinit var</Strong> solo funciona con variables (var) y solo puede aplicarse a tipos de referencia (como objetos) y a cadenas de String, no a tipos primitivos (como Int, Double, etc.). </br>
Tenemos que tener cuidado y asegurarse que la variable lateinit sea inicializada antes de que se acceda a ella usarla o mostrarla en pantalla, de lo contrario, se lanzará un error llamado UninitializedPropertyAccessException.</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/0952629f-41de-4215-98b2-a79b892bbdfd)</br>

Luego, creo una constante que contiene un UUID, ya que quiero que con este UUID se guarde el usuario y el nombre de la foto, si los dos tienen la misma foto, es más semcillo luego buscar la foto asignada al usuario</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/88787aa3-a1c6-4f97-99f5-08961b3f5f1d)</br></br>

Luego, ya en el método onCreate, mando a trer todos los elementos de la vista, noten como inicializo algunos componentes que declaré anteriormente, y como los inicialicé arriba en la clase puedo acceder a ellos desde otros métodos.</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/1c3763a6-0243-4433-8b5e-1c043edec9fc)</br></br>

Ahora, vamos a programar estos dos botones:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/9f2addf5-46b5-422b-ace6-031b4a9df205)
</br>
Al darle clic, comprobamos si el usuario aceptó los permisos (si no comprobamos que el usuario ya aceptó los permisos para abria la camara la aplicación se cierra)</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/88d212aa-0da8-4c07-a057-4a38c0213022)</br>
Este es el código de la función para comprobar si el usuario ya aceptó los servicios:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/ceff0df0-22b4-4ca8-aae4-b29169c95fd3)
</br>
Si el permiso nunca lo ha aceptado, entonces se lo pedimos, pero si ya aceptó el permiso antes ya no se lo volvemos a pedir</br>
</br>
Y aqui están las funciones para solicitar ese permiso</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/8a968424-9f8f-461a-bc64-2573a3bbb5ec)</br>
Estas funciones son las que hacen aparecer este cuadrito que dice si aceptamos los permisos o no</br></br>

El método onRequestPermissionsResult se llama cuando el usuario responde a una solicitud de permisos. Este método ya es propio del lenguaje Kotlin</br>
El método utiliza una estructura when (similar a switch en otros lenguajes) para manejar diferentes códigos de solicitud de permisos</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/6b422e53-1c88-4932-a737-6b2e61e00f17)</br>
En otras palabras: el usuario acaba de aceptar los permisos en el cuadrito que nos aparece, entonces ¿que hacemos despues de eso? pues eso lo manejamos con esta función</br>
OJO: Fijense como para abrir la camara usamos:
```kotlin
  val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
  startActivityForResult(intent, codigo_opcion_tomar_foto)
```
Y para abrir la galeria usamos:
```kotlin
  val intent = Intent(Intent.ACTION_PICK
  intent.type = "image/*"
  startActivityForResult(intent, codigo_opcion_galeria)
```
</br>
📍 Checkpoint 📍</br>
Ahorita lo que tenemos es que al precionar los botones de la galeria y de la camara primero se comprueban los permisos, luego si no los tiene se le pide los permisos al usuario, y si los acepta se abre la galeria o la camara dependiendo de que botón precionó. Entonces, hasta este checkpoint tenemos abierta la camara o la galeria, continuemos y veamos que pasa después de abrir la camara o la galeria</br></br>

Veamos que hacemos después de seleccionar una imagen o tomar una foto.</br>
Esto lo hacemos con una función de Kotlin nuevamente que ya viene solo para que nosotros la usemos, llamada "onActivityResult" o sea, que pasa después de abrir la camara o galería </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/f2c38353-464b-457e-b650-93c4d70915a0)</br>
Esta función obtiene el código (recuerdan el que definimos arriba?) y si el código (o la opción) se eejcutó correctamente vamos a realizar una cosa u otra</br>
Eso lo hacemos con "when" que es como el "switch case" en otros lenguajes de programación.</br>
Entonces, cuando se haya escogido el código de la galeria haremos algo, y cuando se haya escodigo el código de la camara haremos otra cosa.</br>
OJO: esto haremos si escogieron la galeria:
```kotlin
     codigo_opcion_galeria -> {
      val imageUri: Uri? = data?.data
      imageUri?.let {
        val imageBitmap = MediaStore.Images.Media.getBitmap(contentResolver, it)
        subirimagenFirebase(imageBitmap) { url ->
        miPath = url
        imageView.setImageURI(it)
        }
      }
    }
```
Obtiene el Uri de la imagen seleccionada.</br>
Convierte el Uri a un Bitmap.</br>
Llama a subirimagenFirebase para subir la imagen a Firebase y obtiene una URL de la imagen.</br>
Almacena la URL en la variable miPath.</br>
Muestra la imagen en un ImageView.</br>


Y esto haremos si escogieron la camara:
```kotlin
    codigo_opcion_tomar_foto -> {
      val imageBitmap = data?.extras?.get("data") as? Bitmap
      imageBitmap?.let {
        subirimagenFirebase(it) { url ->
        miPath = url
        imageView.setImageBitmap(it)
        }
      }
    }
```
Obtiene el Bitmap de la foto tomada.</br>
Llama a subirimagenFirebase para subir la imagen a Firebase y obtiene una URL de la imagen.</br>
Almacena la URL en la variable miPath.</br>
Muestra la imagen en un ImageView.</br>





