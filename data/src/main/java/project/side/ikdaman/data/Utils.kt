package project.side.ikdaman.data

import project.side.ikdaman.data.model.BaseDto
import project.side.ikdaman.domain.model.ApiResult
import retrofit2.Response

fun<K> toApiResult(response: Response<out BaseDto<K>>, message: String = "Unknown error"): ApiResult<K> {
    return if (response.isSuccessful && response.body() != null) {
        ApiResult.Success(response.body()!!.toEntity())
    } else {
        ApiResult.Error(message)
    }
}