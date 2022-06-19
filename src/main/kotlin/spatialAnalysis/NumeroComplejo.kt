package spatialAnalysis

class NumeroComplejo {

    var real:Double? = null
    var imaginario:Double? = null

    constructor(_real:Double?, _imaginario:Double?){
        real = _real
        imaginario = _imaginario
    }
    private operator fun invoke(_real: Double?, _imaginario: Double?) {
        real = _real
        imaginario = _imaginario
    }
    constructor(complejo: NumeroComplejo){
        this(complejo.real, complejo.imaginario)
    }

    fun sumar(segundo:NumeroComplejo):NumeroComplejo{
        val primero = this
        val real = primero.real?.plus(segundo.real!!)
        val imag = primero.imaginario?.plus(segundo.imaginario!!)
        return NumeroComplejo(real, imaginario)
    }


}