package project.side.ikdaman.data.service

import project.side.ikdaman.data.model.user.CheckNickNameResponse
import project.side.ikdaman.data.model.user.UserInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
    @GET("/members/check")
    suspend fun checkNickName(
        @Query("nickname") nickname: String
    ): Response<CheckNickNameResponse>

    @GET("/members/me")
    suspend fun getUserInfo(): Response<UserInfoResponse>
}