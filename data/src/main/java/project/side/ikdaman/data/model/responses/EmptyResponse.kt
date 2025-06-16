package project.side.ikdaman.data.model.responses

data class EmptyResponse(
    val code: Int? = 0,
    val message: String? = "",
) {
    fun isSuccess(): Boolean {
        return code == 0
    }
}
