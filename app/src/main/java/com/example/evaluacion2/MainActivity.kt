package com.example.evaluacion2

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.evaluacion2.db.AppDatabase
import com.example.evaluacion2.db.Elementos
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch( Dispatchers.IO){
            val empleadosDao = AppDatabase.getInstance(this@MainActivity).elementosDao()
        }

        val crear1 = resources.getString(R.string.botonCrear1)

        setContent {
            ListarElementosUI(crear1)
        }
    }
}




@Composable
fun ListarElementosUI(crear:String) {
    var contexto = LocalContext.current
    val (elementos, setElementos) = remember { mutableStateOf(emptyList<Elementos>()) }


    LaunchedEffect(elementos){
        withContext(Dispatchers.IO){
            val dao = AppDatabase.getInstance(contexto).elementosDao()
            setElementos( dao.findAll())
        }
    }


        if (elementos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                Text(text = "No hay elementos disponibles")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    Button(onClick ={
                        val intent = Intent(contexto, Crear::class.java)
                        contexto.startActivity(intent)
                    },
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(crear)
                    }
                }
            }


        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(elementos) { elemento ->
                    ElementosItemUI(elemento){
                            setElementos(emptyList<Elementos>())
                    }
                }

            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Button(onClick ={
                    val intent = Intent(contexto, Crear::class.java)
                    contexto.startActivity(intent)
                },
                    modifier = Modifier
                        .padding(8.dp)
                ) {
                    Text(crear)
                }
            }
        }





}

@Composable
fun ElementosItemUI(elementos: Elementos, onSave:() -> Unit = {} ){
    val contexto = LocalContext.current
    val alcanceCorrutina = rememberCoroutineScope()



    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 20.dp, horizontal = 20.dp)
    ){
        if(elementos.comprado){
            Icon(
                Icons.Filled.Check,
                contentDescription = "Elemento comprado",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch ( Dispatchers.IO ){
                        val dao = AppDatabase.getInstance( contexto ).elementosDao()
                        elementos.comprado = false
                        dao.actualizar( elementos )
                        onSave()
                    }
                }
            )
        } else {
            Icon(
                Icons.Filled.ShoppingCart,
                contentDescription = "Elemento no comprado",
                modifier = Modifier.clickable {
                    alcanceCorrutina.launch ( Dispatchers.IO ){
                        val dao = AppDatabase.getInstance( contexto ).elementosDao()
                        elementos.comprado = true
                        dao.actualizar( elementos )
                        onSave()
                    }
                }
            )
        }
        Spacer(modifier =  Modifier.width(20.dp))

        Text(
            text = elementos.elemento,
            modifier = Modifier.weight(2f)
        )
        Icon(
            Icons.Filled.Delete,
            contentDescription = "Eliminar elemento",
            modifier = Modifier.clickable {
                alcanceCorrutina.launch ( Dispatchers.IO ){
                    val dao = AppDatabase.getInstance( contexto ).elementosDao()
                    dao.eliminar( elementos )
                    onSave()
                }
            }
        )

    }
}

