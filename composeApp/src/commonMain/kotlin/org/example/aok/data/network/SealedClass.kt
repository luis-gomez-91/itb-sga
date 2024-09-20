package org.example.aok.data.network

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