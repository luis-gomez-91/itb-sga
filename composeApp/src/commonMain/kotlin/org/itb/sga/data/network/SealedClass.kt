package org.itb.sga.data.network

import org.itb.sga.data.network.alu_consulta_general.AluConsultaGeneral
import org.itb.sga.data.network.pro_clases.Asistencia
import org.itb.sga.data.network.pro_clases.ComenzarClase
import org.itb.sga.data.network.pro_clases.LeccionGrupo
import org.itb.sga.data.network.pro_cronograma.ProCronograma
import org.itb.sga.data.network.pro_entrega_actas.ProEntregaActas
import org.itb.sga.data.network.pro_evaluaciones.ProEvaluaciones
import org.itb.sga.data.network.reportes.DjangoModel
import org.itb.sga.data.network.reportes.ReporteCategoria
import org.itb.sga.data.network.solicitud_becas.FichaSocioeconomica
import org.itb.sga.data.network.solicitud_becas.SolicitudBeca

sealed class LoginResult {
    data class Success(val login: Login) : LoginResult()
    data class Failure(val error: Error) : LoginResult()
}

sealed class HomeResult {
    data class Success(val home: Home) : HomeResult()
    data class Failure(val error: Error) : HomeResult()
}

sealed class InscripcionResult {
    data class Success(val inscripciones: Inscripciones) : InscripcionResult()
    data class Failure(val error: Error) : InscripcionResult()
}

sealed class DocentesResult {
    data class Success(val docentes: Docentes) : DocentesResult()
    data class Failure(val error: Error) : DocentesResult()
}

sealed class AccountResult {
    data class Success(val account: Account) : AccountResult()
    data class Failure(val error: Error) : AccountResult()
}

sealed class AluFinanzasResult {
    data class Success(val aluFinanza: List<Rubro>) : AluFinanzasResult()
    data class Failure(val error: Error) : AluFinanzasResult()
}

sealed class AluCronogramaResult {
    data class Success(val aluCronograma: List<AluCronograma>) : AluCronogramaResult()
    data class Failure(val error: Error) : AluCronogramaResult()
}

sealed class AluMallaResult {
    data class Success(val aluMalla: List<AluMalla>) : AluMallaResult()
    data class Failure(val error: Error) : AluMallaResult()
}

sealed class AluHorarioResult {
    data class Success(val aluHorario: List<AluHorario>) : AluHorarioResult()
    data class Failure(val error: Error) : AluHorarioResult()
}

sealed class PagoOnlineResult {
    data class Success(val pagoOnline: PagoOnline) : PagoOnlineResult()
    data class Failure(val error: Error) : PagoOnlineResult()
}

sealed class AluMateriasResult {
    data class Success(val aluMateria: List<AluMateria>) : AluMateriasResult()
    data class Failure(val error: Error) : AluMateriasResult()
}

sealed class AluFacturacionResult {
    data class Success(val aluFacturacion: List<AluFacturacion>) : AluFacturacionResult()
    data class Failure(val error: Error) : AluFacturacionResult()
}

sealed class AluNotasResult {
    data class Success(val tipoAsignaturaNota: TipoAsignaturaNota) : AluNotasResult()
    data class Failure(val error: Error) : AluNotasResult()
}

sealed class ReportResult {
    data class Success(val report: Report) : ReportResult()
    data class Failure(val error: Error) : ReportResult()
}

sealed class ProClasesResult {
    data class Success(val proClases: ProClases) : ProClasesResult()
    data class Failure(val error: Error) : ProClasesResult()
}

sealed class ProHorariosResult {
    data class Success(val proHorarios: List<ProHorario>) : ProHorariosResult()
    data class Failure(val error: Error) : ProHorariosResult()
}

sealed class AluSolicitudesResult {
    data class Success(val aluSolicitudes: List<AluSolicitud>) : AluSolicitudesResult()
    data class Failure(val error: Error) : AluSolicitudesResult()
}

sealed class AluDocumentosResult {
    data class Success(val aluDocumentos: List<AluDocumento>) : AluDocumentosResult()
    data class Failure(val error: Error) : AluDocumentosResult()
}

sealed class DocBibliotecaResult {
    data class Success(val docBiblioteca: DocBibliotecas) : DocBibliotecaResult()
    data class Failure(val error: Error) : DocBibliotecaResult()
}

sealed class AluSolicitudBecaResult {
    data class Success(val data: List<SolicitudBeca>) : AluSolicitudBecaResult()
    data class Failure(val error: Error) : AluSolicitudBecaResult()
}

sealed class FichaSocioeconomicaResult {
    data class Success(val data: FichaSocioeconomica) : FichaSocioeconomicaResult()
    data class Failure(val error: Error) : FichaSocioeconomicaResult()
}

sealed class AluConsultaGeneralResult {
    data class Success(val data: AluConsultaGeneral) : AluConsultaGeneralResult()
    data class Failure(val error: Error) : AluConsultaGeneralResult()
}

sealed class AluMatriculaResult {
    data class Success(val data: AluConsultaGeneral) : AluMatriculaResult()
    data class Failure(val error: Error) : AluMatriculaResult()
}

sealed class PaymentResult {
    data class Success(val transactionId: String) : PaymentResult() // Pago exitoso
    data class Failure(val message: String) : PaymentResult() // Fallo en el pago
}

sealed class ProCronogramaResult {
    data class Success(val data: List<ProCronograma>) : ProCronogramaResult()
    data class Failure(val error: Error) : ProCronogramaResult()
}

sealed class ReportesResult {
    data class Success(val data: List<ReporteCategoria>) : ReportesResult()
    data class Failure(val error: Error) : ReportesResult()
}

sealed class ProEvaluacionesResult {
    data class Success(val data: ProEvaluaciones) : ProEvaluacionesResult()
    data class Failure(val error: Error) : ProEvaluacionesResult()
}

sealed class LeccionGrupoResult {
    data class Success(val data: LeccionGrupo) : LeccionGrupoResult()
    data class Failure(val error: Error) : LeccionGrupoResult()
}

sealed class ComenzarClaseResult {
    data class Success(val data: ComenzarClase) : ComenzarClaseResult()
    data class Failure(val error: Error) : ComenzarClaseResult()
}

sealed class UpdateAsistenciaResult {
    data class Success(val data: Asistencia) : UpdateAsistenciaResult()
    data class Failure(val error: Error) : UpdateAsistenciaResult()
}

sealed class ProEntregaActasResult {
    data class Success(val data: List<ProEntregaActas>) : ProEntregaActasResult()
    data class Failure(val error: Error) : ProEntregaActasResult()
}

sealed class DjangoModelResult {
    data class Success(val data: DjangoModel) : DjangoModelResult()
    data class Failure(val error: Error) : DjangoModelResult()
}