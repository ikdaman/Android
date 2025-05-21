package project.side.ikdaman.data.model

data class ApiResponse<T>(
    val code: Int? = 0,
    val message: String? = "",
    val books: T? = null
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}