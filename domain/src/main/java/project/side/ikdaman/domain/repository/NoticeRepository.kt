package project.side.ikdaman.domain.repository

import project.side.ikdaman.domain.model.Notice
import project.side.ikdaman.domain.model.NoticeDetail
import project.side.ikdaman.domain.model.NoticeItem

interface NoticeRepository {
    suspend fun getNotices(page: Int, limit: Int = 10): NoticeItem
    suspend fun getNoticeDetail(id: Long): NoticeDetail
}

