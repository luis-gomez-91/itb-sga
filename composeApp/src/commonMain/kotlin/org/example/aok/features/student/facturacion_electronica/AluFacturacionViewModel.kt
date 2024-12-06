package org.example.aok.features.student.facturacion_electronica

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.JsonPrimitive
import org.example.aok.core.createHttpClient
import org.example.aok.core.logInfo
import org.example.aok.data.network.AluFacturacion
import org.example.aok.data.network.Report
import org.example.aok.data.network.AluFacturacionResult
import org.example.aok.data.network.Error
import org.example.aok.data.network.ReportForm
import org.example.aok.features.common.home.HomeViewModel

class AluFacturacionViewModel() : ViewModel() {
    private val client = createHttpClient()
    private val service = AluFacturacionService(client)

    private val _data = MutableStateFlow<List<AluFacturacion>>(emptyList())
    val data: StateFlow<List<AluFacturacion>> = _data

    private val _RIDE = MutableStateFlow<Report?>(null)
    val RIDE: StateFlow<Report?> = _RIDE


    fun onloadAluFacturacion(id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val result = service.fetchAluFacturacion(id)
                logInfo("alu_facturacion", "$result")

                when (result) {
                    is AluFacturacionResult.Success -> {
                        _data.value = result.aluFacturacion
                        homeViewModel.clearError()
                    }
                    is AluFacturacionResult.Failure -> {
                        homeViewModel.addError(result.error)
                    }
                }
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

    fun downloadRIDE(reportName: String, id: Int, homeViewModel: HomeViewModel) {
        homeViewModel.changeLoading(true)
        viewModelScope.launch {
            try {
                val form = ReportForm(
                reportName = reportName,
                params = mapOf(
                        "factura" to JsonPrimitive(id)
                    )
                )
                homeViewModel.onloadReport(form)
            } catch (e: Exception) {
                homeViewModel.addError(Error(title = "Error", error = "${e.message}"))
            } finally {
                homeViewModel.changeLoading(false)
            }
        }
    }

}
