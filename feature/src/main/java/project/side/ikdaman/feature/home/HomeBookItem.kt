package project.side.ikdaman.feature.home

data class HomeBookItem(
    val id: String,
    val imageUrl: String,
    val addedDateTime: Long,
    val lastEditedDateTime: Long,
    val title: String,
    val author: String,
    var progress: Float = 0F,
    var firstImpression: String = ""
) {
    fun getLeftDay(): Int {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - addedDateTime
        val days = (diff / (1000 * 60 * 60 * 24)).toInt()
        return if (days < 0) {
            0
        } else {
            days
        }
    }

    fun isCompleted(): Boolean {
        return progress >= 1f
    }
}