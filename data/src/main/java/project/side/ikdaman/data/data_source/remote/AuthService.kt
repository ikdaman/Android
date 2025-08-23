package project.side.ikdaman.data.data_source.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/login")
    suspend fun login(
        @Header("social-token") accessToken: String,
        @Body loginRequest: LoginRequest
    ): Response<LoginResponse>
}

data class LoginRequest(
    val provider: String,
    val providerId: String
)

data class LoginResponse(
    val nickname: String?
)