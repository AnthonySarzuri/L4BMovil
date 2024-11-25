package com.example.clinicaserenidad.data.dao

import java.sql.Connection
import java.sql.DriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PostgresSqlConnection {
    private const val URL = "jdbc:postgresql://ep-icy-sea-a4uo8zca.us-east-1.aws.neon.tech:5432/Clinica_DB?ssl=true"
    private const val USER = "Clinica_DB_owner"
    private const val PASSWORD = "WOjPbACc8y5N"

    fun getConnection(): Connection? {
        return try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: Exception) {
            println("Error al obtener la conexión: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun getSafeConnection(): Connection? = withContext(Dispatchers.IO) {
        try {
            DriverManager.getConnection(URL, USER, PASSWORD)
        } catch (e: Exception) {
            println("Error al conectar a la base de datos: ${e.message}")
            e.printStackTrace()
            null
        }
    }

    suspend fun testConnection(): Boolean = withContext(Dispatchers.IO) {
        try {
            getConnection()?.use {
                println("Conexión exitosa a la base de datos.")
                return@withContext true
            }
            false
        } catch (e: Exception) {
            println("Error al probar la conexión a la base de datos: ${e.message}")
            e.printStackTrace()
            false
        }
    }
}


