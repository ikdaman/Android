package project.side.ikdaman.feature.mypage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.Notice
import project.side.ikdaman.domain.model.NoticeDetail
import project.side.ikdaman.domain.repository.NoticeRepository
import javax.inject.Inject

data class NoticeUiState(
    val notices: List<Notice> = emptyList(),
    val currentPage: Int = 1,
    val totalPage: Int = 1,
    val expandedNotices: Map<Long, NoticeDetail> = emptyMap()
)

sealed interface NoticeErrorEvent {
    val message: String

    data class NoticeLoadErrorEvent(override val message: String) : NoticeErrorEvent
}

@HiltViewModel
class NoticeViewModel @Inject constructor(
    private val noticeRepository: NoticeRepository
) : ViewModel() {

    private val _noticeUiState: MutableStateFlow<NoticeUiState> = MutableStateFlow(NoticeUiState())
    val noticeUiState = _noticeUiState.asStateFlow()

    private val _noticeErrorEvent: MutableSharedFlow<NoticeErrorEvent> = MutableSharedFlow()
    val noticeErrorEvent = _noticeErrorEvent.asSharedFlow()

    fun getNotices(page: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val notices = noticeRepository.getNotices(page)
                _noticeUiState.update {
                    it.copy(
                        notices = notices.notices,
                        totalPage = notices.totalPages
                    )
                }
            } catch (e: Exception) {
                _noticeErrorEvent.emit(NoticeErrorEvent.NoticeLoadErrorEvent(e.message ?: "알 수 없는 에러가 발생했습니다."))
            }
        }
    }

    fun changeCurrentPage(page: Int) {
        _noticeUiState.update {
            it.copy(currentPage = page)
        }
    }

    fun toggleNoticeExpansion(id: Long) {
        val current = _noticeUiState.value
        if (current.expandedNotices.contains(id)) {
            _noticeUiState.update {
                it.copy(expandedNotices = it.expandedNotices - id)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {
                try {
                    val noticeDetail = noticeRepository.getNoticeDetail(id)
                    _noticeUiState.update {
                        it.copy(expandedNotices = it.expandedNotices + (id to noticeDetail))
                    }
                } catch (e: Exception) {
                    _noticeErrorEvent.emit(NoticeErrorEvent.NoticeLoadErrorEvent(e.message ?: "알 수 없는 에러가 발생했습니다."))
                }
            }
        }
    }
}