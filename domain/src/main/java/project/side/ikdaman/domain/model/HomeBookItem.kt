package project.side.ikdaman.domain.model

data class HomeBookItem(
    val id: String,
    val imageUrl: String,
    val lastEditedTime: Long,
    val title: String,
    val author: String,
    var progress: Float = 0F,
    var firstImpression: String = ""
) {
    fun getLeftDay(): Int {
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