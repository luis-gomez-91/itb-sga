package org.example.aok.features.student.facturacion_electronica

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluFacturacion
import org.example.aok.data.network.AluFacturacionResult
import org.example.aok.data.network.AluFacturacionXML

class AluFacturacionViewModel : ViewModel() {
    private val client = createHttpClient()
    private val service = AluFacturacionService(client)

    private val _data = MutableStateFlow<List<AluFacturacion>>(emptyList())
    val data: StateFlow<List<AluFacturacion>> = _data

    private val _error = MutableStateFlow<String?>(null) // Iniciando como null
    val error: StateFlow<String?> = _error

    private val _isLoading = MutableStateFlow<Boolean>(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _XML = MutableStateFlow<AluFacturacionXML?>(null)
    val XML: StateFlow<AluFacturacionXML?> = _XML


    fun onloadAluFacturacion(id: Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val result = service.fetchAluFacturacion(id)
                logInfo("alu_facturacion", "$result")

                when (result) {
                    is AluFacturacionResult.Success -> {
                        _data.value = result.aluFacturacion
                        _error.value = null // Limpiar errores en caso de éxito
                    }
                    is AluFacturacionResult.Failure -> {
                        _error.value = result.error.error ?: "Ocurrió un error desconocido"
                    }
                }
            } catch (e: Exception) {
                _error.value = "Error al cargar los datos: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun downloadRIDE(url: String) {
        viewModelScope.launch {
            try {
                _XML.value = service.downloadXML(url)
                logInfo("alu_facturacion", "$_XML.value")
            } catch (e: Exception) {
                _error.value = "Error al descargar y guardar el archivo: ${e.message}"
            }
        }
    }

}
