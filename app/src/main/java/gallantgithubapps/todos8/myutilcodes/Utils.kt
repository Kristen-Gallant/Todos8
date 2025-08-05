package gallantgithubapps.todos8.myutilcodes


import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

fun formatDateRelative(dateString: String): String {
    return try {
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val inputDate = LocalDate.parse(dateString, formatter)
        val today = LocalDate.now()

        val daysDiff = today.until(inputDate).days

        when (daysDiff) {
            0 -> "Today"
            1 -> "Tomorrow"
            2 -> "In 2 days"
            3 -> "In 3 days"
            4 -> "In 4 days"
            5 -> "In 5 days"
            else -> {

                val dayOfWeek = inputDate.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                val day = inputDate.dayOfMonth
                val suffix = when {
                    day in 11..13 -> "th"
                    day % 10 == 1 -> "st"
                    day % 10 == 2 -> "nd"
                    day % 10 == 3 -> "rd"
                    else -> "th"
                }
                val month = inputDate.month.getDisplayName(TextStyle.FULL, Locale.ENGLISH)
                val year = inputDate.year
                "$dayOfWeek $day$suffix $month $year"
            }
        }
    } catch (e: Exception) {
        "Invalid date"
    }
}
