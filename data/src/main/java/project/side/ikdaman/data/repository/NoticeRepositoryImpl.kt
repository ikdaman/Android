package project.side.ikdaman.data.repository

import project.side.ikdaman.data.service.NoticeService
import project.side.ikdaman.domain.model.NoticeDetail
import project.side.ikdaman.domain.model.NoticeItem
import project.side.ikdaman.domain.repository.NoticeRepository
import javax.inject.Inject

class NoticeRepositoryImpl @Inject constructor(
    private val noticeService: NoticeService
) : NoticeRepository {
    override suspend fun getNotices(page: Int, limit: Int): NoticeItem {
        val responseBody = noticeService.getNotices(
            page, limit
        ).body()
        if (responseBody == null) {
            throw IllegalStateException("responseBody is null")
        } else {
            return responseBody
        }
    }

    override suspend fun getNoticeDetail(id: Long): NoticeDetail {
        val responseBody = noticeService.getNoticeDetail(id).body()
        if (responseBody == null){
            throw IllegalStateException("responseBody is null")
        }else{
            return responseBody
        }
    }
}