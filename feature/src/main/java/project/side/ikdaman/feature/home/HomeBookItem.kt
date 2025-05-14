package project.side.ikdaman.feature.home

data class HomeBookItem(
    val id: String = "",
    val imageUrl: String = "",
    val addedDateTime: Long = 0L,
    val lastEditedDateTime: Long = 0L,
    val title: String = "",
    val author: String = "",
    var progress: Float = 0F,
    var firstImpression: String = "",
    var totalPage: Int = 0,
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