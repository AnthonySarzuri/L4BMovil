package com.example.clinicaserenidad.data.model

data class PacienteModel(
    val pacienteId: Int,
    val personaId: Int,
    val numeroSeguro: String?,
    val fechaRegistro: String,
    val estado: Boolean
)