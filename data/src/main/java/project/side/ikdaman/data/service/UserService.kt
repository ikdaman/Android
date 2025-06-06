package project.side.ikdaman.data.service

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/members/check")
    suspend fun checkNickName(
        @Query("nickname") nickname: String
    ): Response<CheckNickNameResponse>
}

data class CheckNickNameResponse(
    val available: Boolean,
    val code: Int = 0,
    val message: String = ""
)