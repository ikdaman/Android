package project.side.ikdaman.feature.add_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import project.side.ikdaman.domain.usecase.GetBookDetailUseCase
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor(
    private val repository: MyBooksApiRepository,
    private val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {

    private val bookDetail = MutableStateFlow<ApiResult<BookDetail>>(ApiResult.Loading)
    val bookDetailState = bookDetail.asStateFlow()

    private val errorMessage = MutableStateFlow("")
    val errorMessageState = errorMessage.asStateFlow()

    fun getBookInfo(bookId: String) = viewModelScope.launch {
        getBookDetailUseCase(bookId).collect {
            bookDetail.emit(it)
        }
    }

    fun addFirstImpression(
        bookId: String,
        firstImpression: String,
        onSuccess: () -> Unit = {}
    ) = viewModelScope.launch {
        repository.postImpression(bookId, firstImpression).collect {
            when (it) {
                is ApiResult.Loading -> {
                    // Handle loading state
                }

                is ApiResult.Success -> {
                    onSuccess()
                }

                is ApiResult.Error -> {
                    errorMessage.emit(it.message)
                }
            }
        }
    }

    fun addMiddleRecord(data: BookDetail, text: String, page: Int, onSuccess: () -> Unit = {}) = viewModelScope.launch {
        val bookId = data.mybookId
        val totalPage = data.bookInfo.totalPage
        if (page == totalPage) {
            addCompletedRecord(bookId, text, onSuccess)
            return@launch
        }
        repository.addThink(bookId, text, page).collect {
            when (it) {
                is ApiResult.Loading -> {
                    // Handle loading state
                }

                is ApiResult.Success -> {
                    onSuccess()
                }

                is ApiResult.Error -> {
                    errorMessage.emit(it.message)
                }
            }
        }
    }

    fun addCompletedRecord(bookId: String, content: String, onSuccess: () -> Unit = {}) = viewModelScope.launch {
            repository.addCompleted(bookId, content).collect {
                when (it) {
                    is ApiResult.Loading -> {
                        // Handle loading state
                    }

                    is ApiResult.Success -> {
                        onSuccess()
                    }

                    is ApiResult.Error -> {
                        errorMessage.emit(it.message)
                    }
                }
            }
        }
}