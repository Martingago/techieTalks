import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DateFormatterUtil {
    companion object {

        // Función para formatear la fecha a formato "dd/MM/yyyy"
        @RequiresApi(Build.VERSION_CODES.O)
        fun formatToDateString(dateString: String): String {
            val inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
            val outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            val date = LocalDateTime.parse(dateString, inputFormatter)
            return date.format(outputFormatter)
        }
    }
}