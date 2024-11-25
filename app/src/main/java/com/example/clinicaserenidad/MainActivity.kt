package com.example.clinicaserenidad

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.clinicaserenidad.ui.LoginActivity
import com.example.clinicaserenidad.data.dao.PostgresSqlConnection
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            val isConnected = PostgresSqlConnection.testConnection()
            if (isConnected) {
                Toast.makeText(this@MainActivity, "Conexión exitosa a la base de datos", Toast.LENGTH_SHORT).show()
                val intent = Intent(this@MainActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                Toast.makeText(this@MainActivity, "Error al conectar a la base de datos", Toast.LENGTH_LONG).show()
                finish()
            }
        }
    }
}
