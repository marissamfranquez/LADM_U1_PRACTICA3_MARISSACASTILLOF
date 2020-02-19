package mx.edu.ittepic.ladm_u1_practica3

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    var valor=0
    val vector : Array<Int> = Array (10,{0})
    var posicion = 0
    var cadena =""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        asignar.setOnClickListener {
            insertarEnVector()

        }
        mostrar.setOnClickListener{
          mostrarDatosEnLista()
        }
        guardar.setOnClickListener{
            guardarAchivoSD()
        }
        leer.setOnClickListener{
            leerArchivoSD()
        }
    }

    private fun insertarEnVector() {


        if(editText5.text.isEmpty()||editText6.text.isEmpty()){
            mensaje("ERROR TODOS LOS CAMPOS DEBEN ESTAR LLENOS")
            return
        }
        if(editText5.text.toString().toInt()<0 || editText5.text.toString().toInt()>9){

            mensaje("ERROR, LA POSICION DEL VECTOR ES ENTRE 0 Y 9")
            return
        }
        valor=editText6.text.toString().toInt()
        posicion=editText5.text.toString().toInt()
        vector[posicion]=valor
        limpiarCampos()
        mensaje("SE INSERTO EL VALOR")
    }

    fun mostrarDatosEnLista() {
        cadena=""

        (0..9).forEach {

            cadena = cadena+vector[it]
            cadena=cadena + "-"
        }
        vista.setText(cadena)


    }

    private fun limpiarCampos() {
        editText6.setText("")
        editText5.setText("")
    }
    fun noSD():Boolean{
        var estado = Environment.getExternalStorageState()

        if (estado != Environment.MEDIA_MOUNTED){
            return true

        }
        return false
    }

    fun guardarAchivoSD(){

        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }

        if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)==PackageManager.PERMISSION_DENIED){

            //PERMISO NO CONCEDIDO, ENTONCES SE SOLICITA
            ActivityCompat.requestPermissions(this,arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE,android.Manifest.permission.READ_EXTERNAL_STORAGE),0)
        }
        else{
            //mensaje("LOS PERMISOS YA FUERON OTORGADOS")
        }

        try {

            val rutaSD = Environment.getExternalStorageDirectory()
            var data = editText3.text.toString()+".txt"
            Toast.makeText(this,data,Toast.LENGTH_LONG).show()
            val flujo = File(rutaSD.absolutePath, data )
            val flujoSalida = OutputStreamWriter(FileOutputStream(flujo))
            flujoSalida.write(cadena)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Se guardo correctamente")
            editText3.setText("")
            vista.setText("")
        } catch (error: Exception) {
            mensaje(error.message.toString())
        }
    }

    fun leerArchivoSD(){
        if (noSD()){
            mensaje("NO HAY MEMORIA EXTERNA")
            return
        }
        var nom = editText4.text.toString()+".txt"
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath,nom)
            var flujoEntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEntrada.readLine()

            vista.setText(data)
            flujoEntrada.close()

        }catch (error: IOException){
            mensaje(error.message.toString())
        }

    }

    fun mensaje (m: String){
        AlertDialog.Builder(this).setTitle("ATENCION")
            .setMessage(m)
            .setPositiveButton("ACEPTAR"){d,i->}
            .show()
    }


}
