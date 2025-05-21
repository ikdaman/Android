package project.side.ikdaman.domain.model

data class HomeBookItem(
    val id: String = "",
    val imageUrl: String = "",
    val addedDateTime: Long = 0L,
    val lastEditedDateTime: Long = 0L,
    val title: String = "",
    val author: String = "",
    val id: String,
    val imageUrl: String,
    val lastEditedTime: Long,
    val title: String,
    val author: String,
    var progress: Float = 0F,
    var firstImpression: String = "",
    var totalPage: Int = 0,
) {
    fun getElapsedDays(): Int {
        val currentTime = System.currentTimeMillis()
        val diff = currentTime - lastEditedTime
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