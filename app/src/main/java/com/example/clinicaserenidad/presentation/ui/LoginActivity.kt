package com.example.clinicaserenidad.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.clinicaserenidad.R
import com.example.clinicaserenidad.data.dao.PacienteDao
import com.example.clinicaserenidad.presentation.ui.DashboardActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val numeroSeguroEditText = findViewById<EditText>(R.id.editTextNumeroSeguro)
        val loginButton = findViewById<Button>(R.id.buttonLogin)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        loginButton.setOnClickListener {
            val numeroSeguro = numeroSeguroEditText.text.toString().trim()

            if (numeroSeguro.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                lifecycleScope.launch {
                    val paciente = PacienteDao.login(numeroSeguro)
                    progressBar.visibility = View.GONE

                    if (paciente != null) {
                        Toast.makeText(this@LoginActivity, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()

                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        intent.putExtra("PACIENTE_ID", paciente.pacienteId)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "Número de seguro no válido o paciente inactivo", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Por favor ingresa tu número de seguro", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
