package com.example.evaluacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.evaluacion2.db.AppDatabase
import com.example.evaluacion2.db.Elementos
import com.example.evaluacion2.ui.theme.Evaluacion2Theme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Crear : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val crear2 = resources.getString(R.string.botonCrear2)
        setContent {
            CrearElementoUI(crear2)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearElementoUI(crear:String){

    var producto by remember { mutableStateOf("") }
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()


    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Icon(
            Icons.Filled.ShoppingCart,
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp)
        )
        Spacer(modifier= Modifier.height(20.dp))
        TextField(
            placeholder = { Text(text = "Producto") },
            value =producto,
            onValueChange = { producto = it },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
        )
        Spacer(modifier= Modifier.height(20.dp))
        Button(onClick = {
            alcanceCorrutina.launch(Dispatchers.IO) {

                val dao = AppDatabase.getInstance(contexto).elementosDao()
                val nuevoElemento = Elementos(elemento = producto, comprado = false)
                val idGenerada = dao.insertar(nuevoElemento)
                val intent = Intent(contexto, MainActivity::class.java)
                contexto.startActivity(intent)

            }
        }) {
            Text(crear)
        }

    }
}