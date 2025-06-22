package project.side.ikdaman.data.service

import project.side.ikdaman.data.model.user.CheckNickNameResponse
import project.side.ikdaman.data.model.user.UserInfoResponse
import project.side.ikdaman.domain.model.UserInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query

interface UserService {
    @GET("/members/check")
    suspend fun checkNickName(
        @Query("nickname") nickname: String
    ): Response<CheckNickNameResponse>

    @GET("/members/me")
    suspend fun getUserInfo(): Response<UserInfoResponse>

    @PUT("/members/me")
    suspend fun updateUserInfo(
        @Body userInfo: UserInfo
    ): Response<UserInfoResponse>

    @DELETE("/auth/logout")
    suspend fun logout(): Response<Unit>

    @DELETE("/members/me")
    suspend fun withdraw(): Response<Unit>
}