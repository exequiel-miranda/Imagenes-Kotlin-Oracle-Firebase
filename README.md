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

En especifico, esta es la función para subir la imagen a Firebase:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/619821ec-8e17-4eb5-a3a5-55487c118cb2)</br>
Noten como le colocamos un nombre, para colocarle un nombre puede ser cualquier nombre, pero nosotros, le ponemos como nombre la variable uuid que definimos a nivel de clase, de tal manera que se guarda asi en firebase:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/e50be02a-8127-425f-b4e5-ff4dd4613f46)</br>
Ese mismo UUID con el que se guardará la imagen, es el UUID que usaremos para colocarselo al nuevo usuario, de tal manera que tanto el cliente como la foto tengan el mismo UUID, esto nos ayudará a identificar que foto corresponde a cada usuario</br> 
Luego de colocarle un nombre, la comprime en BitMap y la sube a Firebase, luego de eso, vean como le agregamos dos listener
```kotlin
      uploadTask.addOnFailureListener {
            Toast.makeText(this@MainActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
```
que es onFailureListener (Cuando nos falla) le mostramos un Toast
y un onSuccessListerner (cuando se completa exitosamente) que obtemos la URL de donde se subió la imagen (muy importante esto por que esta URL es la que guardaremos en Oracle)</br>

### ¿Como conectar Firebase a mi proyecto Android?</br>
Si no tenemos ningun proyecto nos aparecerá asi:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/e5ad9ade-f63a-44de-8ea1-02da4d3c87fc)</br>
Y damos clic en "Crear uevo proyecto"</br>
Si ya tenemos proyectos igualmente damos clic en Agregar Proyecto</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/13438149-a02c-42f7-8c0c-58fd8ad316d1)</br>

Le colocamos un nombre a nuestro proyecto en Firebase </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/cf6b3652-1f32-452b-9f06-e531cec37cbb)</br>

Como segundo y ultimo paso para crear el proyecto en Firebase, nos muestra la opción para habilitar Google Analytics, pero como nosotros no las vamos a ocupar, solo la desactivamos y la damos clic en "Crear proyecto"</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/34080848-51f0-4dc6-bb17-ce9c2fc60744)</br>

Y seleccionamos que queremos conectar nuestro proyecto de Firebase a una aplicación Android</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/935b579f-3766-4478-bd51-7b6e193c1ffa)</br>
Y seguimos los 4 pasos para conectar nuestro proyecto de Firebase a nuestra aplicación Android</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/54a48371-bdc4-4127-8b0c-909053d0b01a)</br>

En este primer paso nos pide que coloquemos el nombre del paquete, para estar seguros de cual es el nombre de nuestro paquete nos recomienda ir al archivo build.gradle a nivel de app, y tomamos  el valor de donde dice "applicationId"
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/e8e1cf27-2fec-4b22-abe3-dd08a54a8f03)
</br>
Y me queda asi el primer paso ya que los otros dos campos son opcionales, y damos clic en "Registrar app"</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/2ab6d66e-568f-4f2d-8399-ef1b85c7dac5)
</br>
Segundo paso, descargamos y agregamos el archivo donde nos indica</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/cfb764f8-9a4d-4d9e-93af-bc964ce7b318)</br>
Recuerden que en Android podemos ver nuestro proyecto desde diferentes formas, una de ella es el modo de vista proyecto,</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/df112da5-77c8-41fa-a88f-4391194842a5)</br>

Aqui, abrimos la primer carpeta, y luego la carpeta app, aqui será donde arrastraremos el archivo que nos indica Firebase</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/8e3b40f5-9dda-4868-82b9-2a05cb4bd20e)
</br>

🌠 Recuerden que luego debemos cambiar a la vista Android habitual</br>

Ahora vamos con el tercer paso (y ultimo), tenenemos que agregar las librerias de Firebase a nuestro proyecto Android:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/91f72b88-7874-4a19-b598-61af16a717e3)</br>

Primero, agregamos esta linea en el archivo build.gradle a nivel de proyecto</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/3b07b375-bcfc-45e7-b3a4-fae5c7b4da51)</br>
De esta manera:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/d8c3faab-2020-402a-b35d-270a2efeee9c)</br>

Y para finalizar, en el archivo build.gradle a nivel de modulo agregamos estas dos lineas (la primera no)</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/0145d746-55fe-4dc9-84e5-cc7398bf003e)</br>
Asi:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/2e363fb3-d803-4cdd-9c05-d8bb97555d28)</br>
Luego damos clic en "Siguiente"</br>
Luego de completar todos los pasos damos clic en "ir a consola"</br>
Y nos cargará el proyecto ya unido con mi aplicación, luego, damos clic en Storage para habilitar el almacenamiento de Firebase donde guardaremos nuestras fotos</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/f5c8b1fc-ab13-45ca-b1f8-01a2c05d3e77)</br>

Damos clic en "Comenzar"</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/09c46863-6787-43fa-bcee-fa9504d60fc0)</br>
Nos aseguramos de dar clic en modo prueba, para que nos den 30 dias para guardar nuestros archivos (luego extendemos este plazo)</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/801f97d0-0a28-4329-b534-bec08eb874f1)</br>

Dejamos la ubicación por defecto y damos clic en "listo"</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/e15bc720-8f27-494d-86fe-71953b3eb778)</br>
Y ya tenemos conectado nuestro proyecto Firebase con nuestra aplicación Android y habilitada la opción de Storage</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/6030c75b-7172-48b4-a783-4722c233ba9a)</br>

¿Recuerdan que al inicio programamos que nos diera 30 dias de almacenamiento? pues lo extenderemos desde la pestaña de "Reglas" y le colocamos una fecha de expiración superior:</br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/3b3731c7-9562-4475-baec-96305826c7d7)
</br>

Y... por ultimo, la función para guardar el usuario en oracle incluyendo el correo, contraseña y la URL de la foto </br>
![image](https://github.com/exequiel-miranda/Imagenes-Kotlin-Oracle-Firebase/assets/94820436/8e922dcb-acd4-4477-b24e-0507d0b6f97c)</br>ç











