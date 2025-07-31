package project.side.ikdaman.data.service

import project.side.ikdaman.domain.model.NoticeDetail
import project.side.ikdaman.domain.model.NoticeItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface NoticeService {
    @GET("notices")
    fun getNotices(
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<NoticeItem>

    @GET("notices/{id}")
    fun getNoticeDetail(
        @Path("id") id: Long
    ): Response<NoticeDetail>
}