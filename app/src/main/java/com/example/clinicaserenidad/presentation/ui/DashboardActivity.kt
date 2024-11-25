package com.example.clinicaserenidad.presentation.ui

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.clinicaserenidad.R
import com.example.clinicaserenidad.data.dao.PacienteDao
import com.example.clinicaserenidad.presentation.adapter.CitaAdapter
import kotlinx.coroutines.launch

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val pacienteId = intent.getIntExtra("PACIENTE_ID", -1)
        val textViewPacienteInfo = findViewById<TextView>(R.id.textViewPacienteInfo)
        val recyclerViewCitas = findViewById<RecyclerView>(R.id.recyclerViewCitas)

        recyclerViewCitas.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launch {
            try {
                val pacienteDetails = PacienteDao.getPacienteDetails(pacienteId)
                if (pacienteDetails != null) {
                    textViewPacienteInfo.text = "Nombre: ${pacienteDetails.nombre}\nApellido: ${pacienteDetails.apellido}\nEmail: ${pacienteDetails.email}"
                } else {
                    Toast.makeText(this@DashboardActivity, "No se encontraron detalles para este paciente.", Toast.LENGTH_SHORT).show()
                }

                val citas = PacienteDao.getCitasByPacienteId(pacienteId)
                if (citas.isNotEmpty()) {
                    recyclerViewCitas.adapter = CitaAdapter(citas)
                } else {
                    Toast.makeText(this@DashboardActivity, "No se encontraron citas para este paciente.", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@DashboardActivity, "Error al cargar los datos.", Toast.LENGTH_SHORT).show()
                println("Error en DashboardActivity: ${e.message}")
            }
        }
    }
}
