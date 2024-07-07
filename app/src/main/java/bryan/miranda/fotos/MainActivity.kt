package bryan.miranda.fotos

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import modelo.ClaseConexion
import java.io.ByteArrayOutputStream
import java.sql.SQLException
import java.util.UUID

class MainActivity : AppCompatActivity() {
    val codigo_opcion_galeria = 102
    val codigo_opcion_tomar_foto = 103
    private val CAMERA_REQUEST_CODE = 0

    lateinit var imageView: ImageView
    lateinit var miPath: String
    lateinit var txtCorreo: EditText
    lateinit var txtClave: EditText
    val uuid = UUID.randomUUID().toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        //1-Mandar a traer todos los elementos de la vista
        imageView = findViewById(R.id.imgPerfil)
        val btnGaleria = findViewById<Button>(R.id.btnGaleria)
        val btnFoto = findViewById<Button>(R.id.btnFoto)
        val txtUsuario = findViewById<EditText>(R.id.txtBuscarFotoUsuario)
        val btnBuscar = findViewById<Button>(R.id.btnBuscar)
        val btnGuardar = findViewById<Button>(R.id.btnGuardarUsuario)
        txtCorreo = findViewById(R.id.txtCorreo)
        txtClave = findViewById(R.id.txtClave)

        btnGaleria.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, codigo_opcion_galeria)
        }

        btnFoto.setOnClickListener {
            //Al darle clic al botón de la camara pedimos los permisos primero
            checkCameraPermission()
        }

        btnGuardar.setOnClickListener {
            val correo = txtCorreo.text.toString().trim()
            val clave = txtClave.text.toString().trim()
            val imageUri = miPath

            if (correo.isNotEmpty() && clave.isNotEmpty() && imageUri != null) {
                guardarUsuarioConFoto(correo, clave, imageUri)
            } else {
                Toast.makeText(
                    this,
                    "Completa todos los campos y selecciona una foto",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        btnBuscar.setOnClickListener {
            val username = txtUsuario.text.toString().trim()
            buscarFotoDeUsuario(username)
        }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
            //El permiso no está aceptado, entonces se lo pedimos
            requestCameraPermission()
        } else {

        }
    }
    private fun requestCameraPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                android.Manifest.permission.CAMERA)) {
            //El usuario ya ha rechazado el permiso anteriormente, debemos informarle que vaya a ajustes.
        } else {
            //El usuario nunca ha aceptado ni rechazado, así que le pedimos que acepte el permiso.
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            CAMERA_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //El usuario ha aceptado el permiso, no tiene porqué darle de nuevo al botón, podemos lanzar la funcionalidad desde aquí.
                    //Abrimos la camara:
                    val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                    startActivityForResult(intent, codigo_opcion_tomar_foto)
                } else {
                    //El usuario ha rechazado el permiso, podemos desactivar la funcionalidad o mostrar una vista/diálogo.
                }
                return
            }
            else -> {
                // Este else lo dejamos por si sale un permiso que no teníamos controlado.
            }
        }
    }

    //Esta función onActivityResult se encarga de capturar lo que pasa al abrir la geleria o la camara
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {

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


                codigo_opcion_tomar_foto -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        subirimagenFirebase(it) { url ->
                            miPath = url
                            imageView.setImageBitmap(it)
                        }
                    }
                }
            }
        }
    }

    //Subir la imagen a Firebase Storage
    private fun subirimagenFirebase(bitmap: Bitmap, onSuccess: (String) -> Unit) {
        val storageRef = Firebase.storage.reference
        val imageRef = storageRef.child("images/${uuid}.jpg")
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnFailureListener {
            Toast.makeText(this@MainActivity, "Error al subir la imagen", Toast.LENGTH_SHORT).show()

        }.addOnSuccessListener { taskSnapshot ->
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }
    }


    //Guardar el usuario con su correo, clave y foto (falta agregarle un UUID)
    private fun guardarUsuarioConFoto(correo: String, clave: String, imageUri: String) {
        try {
            GlobalScope.launch(Dispatchers.IO) {
                val objConexion = ClaseConexion().cadenaConexion()
                val statement =
                    objConexion?.prepareStatement("INSERT INTO tbMisUsuarios (UUID, correo, contrasena, FotoURI) VALUES (?, ?, ?, ?)")!!
                statement.setString(1, uuid)
                statement.setString(2, correo)
                statement.setString(3, clave)
                statement.setString(4, imageUri)
                statement.executeUpdate()
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MainActivity, "Datos guardados", Toast.LENGTH_SHORT).show()
                    txtCorreo.text.clear()
                    txtClave.text.clear()
                    imageView.setImageResource(0)
                    imageView.tag = null
                }
            }
        } catch (e: SQLException) {
            println("Error al guardar usuario: $e")
        }
    }


    //Función para buscar una foto usando el correo electronico
    private fun buscarFotoDeUsuario(username: String) {
        GlobalScope.launch(Dispatchers.IO) {
            try {
                val objConexion = ClaseConexion().cadenaConexion()

                val statement =
                    objConexion?.prepareStatement("SELECT FotoURI FROM tbMisUsuarios WHERE correo = ?")!!
                statement.setString(1, username)
                val resultSet = statement.executeQuery()
                if (resultSet.next()) {
                    val imageUriString = resultSet.getString("FotoURI")
                    if (imageUriString != null) {
                        withContext(Dispatchers.Main) {
                            val imageUri = Uri.parse(imageUriString)
                            imageView.setImageURI(imageUri)
                        }
                    } else {
                        println("error")
                    }
                } else {
                    println("error")
                }

            } catch (e: SQLException) {
                println("error $e")
            }
        }
    }

}