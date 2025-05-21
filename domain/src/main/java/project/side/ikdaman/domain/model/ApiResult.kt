package project.side.ikdaman.domain.model

sealed class ApiResult<out T> {
    data class Success<out T>(val data: T) : ApiResult<T>()
    data class Error(val message: String) : ApiResult<Nothing>() {
        override fun toString(): String {
            return "Error(message='$message')"
        }
    }
    data object Loading : ApiResult<Nothing>()
}