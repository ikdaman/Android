package project.side.ikdaman.data.model.responses

data class BooksResponse<T>(
    val code: Int? = 0,
    val message: String? = "",
    val books: T? = null
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}