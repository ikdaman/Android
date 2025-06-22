package project.side.ikdaman.data.model.user

data class CheckNickNameResponse(
    val available: Boolean,
    val code: Int = 0,
    val message: String = ""
)
