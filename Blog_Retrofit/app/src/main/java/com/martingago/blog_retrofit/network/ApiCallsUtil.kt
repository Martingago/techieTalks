package com.martingago.blog_retrofit.network

import android.util.Log
import com.martingago.blog_retrofit.model.APIResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

object ApiCallsUtil {

    // Valores por defecto para los reintentos y el retraso
    private const val DEFAULT_RETRY_COUNT = 10
    private const val DEFAULT_DELAY_MILLIS = 5000L

    /**
     * Realiza una llamada API con reintentos configurables.
     * Estos reintentos de ejecutarán siempre y cuando el servidor NO RESPONDA.
     *
     * Si el estado de la respuesta (success) es true/false esa será la respuesta del servidor, el error será manejado en el componente
     * Si el estado de la respuesta (success) es null, quiere decir que el servidor NO HA RESPONDIDO. Se harán hasta {retryCount} intentos a la espera
     * de la respuesta del servidor.
     *
     * @param retryCount Número máximo de intentos.
     * @param delayMillis Retraso entre intentos en milisegundos.
     * @param updateStatus Función para actualizar el estado actual de intentos que se envia a la función padre en forma de callback con cada iteracción.
     * @param apiCall Realiza una llamada  a la función API (controller) a ejecutar.
     * @return El resultado de la llamada API.
     */
    suspend fun callAPIWithRetry(
        retryCount: Int = DEFAULT_RETRY_COUNT,
        delayMillis: Long = DEFAULT_DELAY_MILLIS,
        updateStatus: (currentAttempt: Int, maxAttempts: Int) -> Unit = { _, _ -> },
        apiCall: suspend () -> APIResponse<*> //Realiza una llamada a la api proporcionada, y devolverá un objeto genérico de tipo APIResponse
    ): APIResponse<*>? {
        var currentAttempt = 0
        var result: APIResponse<*>? = null

        /**
         * Success puede tener 3 valores:
         *      true, si la peticion ha sido exitosa,
         *      false si ha habido un error
         *      null no ha habido respuesta por parte del servidor
         */

        var success: Boolean ? = null
        while (currentAttempt < retryCount && success== null) {
            try {
                currentAttempt++
                // Actualizar el estado antes de la llamada
                withContext(Dispatchers.Main) {
                    updateStatus(currentAttempt, retryCount)
                }
                // Ejecutar la llamada API
                result = apiCall()
                success = result?.success //Se obtiene el resultado de respuesta del servidor tras ejecutar la llamada
            } catch (e: Exception) {
                if (currentAttempt == retryCount) {
                    return APIResponse(
                        success = false,
                        message = e.toString(),
                        objectResponse = null
                    )
                }
                delay(delayMillis) // Esperar antes de reintentar
            }
        }
        return result
    }
}