package com.example.clinicaserenidad.data.dao

import com.example.clinicaserenidad.data.model.CitaModel
import com.example.clinicaserenidad.data.model.PacienteDetailsModel
import com.example.clinicaserenidad.data.model.PacienteModel
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.ResultSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object PacienteDao {

    suspend fun login(numeroSeguro: String): PacienteModel? {
        return withContext(Dispatchers.IO) {
            val connection: Connection? = PostgresSqlConnection.getConnection()
            var paciente: PacienteModel? = null

            connection?.let {
                try {
                    val query = """
                        SELECT paciente_id, persona_id, numero_seguro, fecha_registro, estado
                        FROM pacientes
                        WHERE numero_seguro = ? AND estado = TRUE
                    """
                    val preparedStatement: PreparedStatement = it.prepareStatement(query)
                    preparedStatement.setString(1, numeroSeguro)

                    val resultSet: ResultSet = preparedStatement.executeQuery()
                    if (resultSet.next()) {
                        paciente = PacienteModel(
                            pacienteId = resultSet.getInt("paciente_id"),
                            personaId = resultSet.getInt("persona_id"),
                            numeroSeguro = resultSet.getString("numero_seguro"),
                            fechaRegistro = resultSet.getString("fecha_registro"),
                            estado = resultSet.getBoolean("estado")
                        )
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    connection.close()
                }
            }

            paciente
        }
    }

    suspend fun getPacienteDetails(pacienteId: Int): PacienteDetailsModel? = withContext(Dispatchers.IO) {
        val connection = PostgresSqlConnection.getSafeConnection()
        var pacienteDetails: PacienteDetailsModel? = null

        connection?.let {
            try {
                val query = """
                SELECT per.nombre, per.apellido, per.email
                FROM personas per
                JOIN pacientes pac ON per.persona_id = pac.persona_id
                WHERE pac.paciente_id = ?
            """
                println("Ejecutando query: $query con pacienteId = $pacienteId")

                val preparedStatement: PreparedStatement = it.prepareStatement(query)
                preparedStatement.setInt(1, pacienteId)

                val resultSet: ResultSet = preparedStatement.executeQuery()
                if (resultSet.next()) {
                    println("Datos encontrados en la tabla personas para pacienteId: $pacienteId")
                    pacienteDetails = PacienteDetailsModel(
                        nombre = resultSet.getString("nombre"),
                        apellido = resultSet.getString("apellido"),
                        email = resultSet.getString("email")
                    )
                } else {
                    println("No se encontraron datos en la tabla personas para pacienteId: $pacienteId")
                }
            } catch (e: Exception) {
                println("Error en getPacienteDetails: ${e.message}")
                e.printStackTrace()
            } finally {
                connection.close()
            }
        } ?: println("Conexión a la base de datos fallida en getPacienteDetails")

        return@withContext pacienteDetails
    }

    suspend fun getCitasByPacienteId(pacienteId: Int): List<CitaModel> = withContext(Dispatchers.IO) {
        val connection: Connection? = PostgresSqlConnection.getSafeConnection()
        val citas = mutableListOf<CitaModel>()

        connection?.let {
            try {
                val query = """
                SELECT c.cita_id, c.fecha_cita, c.motivo, c.estado
                FROM citas c
                WHERE c.paciente_id = ?
            """
                println("Ejecutando query: $query con pacienteId = $pacienteId")

                val preparedStatement: PreparedStatement = it.prepareStatement(query)
                preparedStatement.setInt(1, pacienteId)

                val resultSet: ResultSet = preparedStatement.executeQuery()
                while (resultSet.next()) {
                    citas.add(
                        CitaModel(
                            citaId = resultSet.getInt("cita_id"),
                            fechaCita = resultSet.getString("fecha_cita"),
                            motivo = resultSet.getString("motivo"),
                            estado = resultSet.getString("estado")
                        )
                    )
                }

                if (citas.isEmpty()) {
                    println("No se encontraron citas para pacienteId = $pacienteId")
                }
            } catch (e: Exception) {
                println("Error en getCitasByPacienteId: ${e.message}")
                e.printStackTrace()
            } finally {
                connection.close()
            }
        } ?: println("Conexión a la base de datos fallida en getCitasByPacienteId")

        return@withContext citas
    }

}
