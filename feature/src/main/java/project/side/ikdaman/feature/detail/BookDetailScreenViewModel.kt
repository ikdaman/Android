package project.side.ikdaman.feature.detail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.usecase.DeleteLogUseCase
import project.side.ikdaman.domain.usecase.GetBookDetailLogUseCase
import project.side.ikdaman.domain.usecase.GetBookDetailUseCase
import project.side.ikdaman.domain.usecase.UpdateBookLogUseCase
import javax.inject.Inject

@HiltViewModel
class BookDetailScreenViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val getBookDetailLogUseCase: GetBookDetailLogUseCase,
    private val updateBookLogUseCase: UpdateBookLogUseCase,
    private val deleteLogUseCase: DeleteLogUseCase
) : ViewModel() {

    private val errorMessage = MutableStateFlow("")
    val errorMessageState = errorMessage.asStateFlow()

    private val bookDetail = MutableStateFlow<ApiResult<BookDetail>>(ApiResult.Loading)
    val bookDetailState = bookDetail.asStateFlow()

    private val bookLog = MutableStateFlow<ApiResult<BookLog>>(ApiResult.Loading)
    val bookLogState = bookLog.asStateFlow()

    private val snackbarMessage = MutableStateFlow("")
    val snackBarState = snackbarMessage.asStateFlow()


    val isLoading = MutableStateFlow(false)

    var currentPage = 1
    var isLastPage = false
    var isLoadingMore = false

    private fun getBookDetail(bookId: String) {
        viewModelScope.launch {
            getBookDetailUseCase(bookId).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        isLoading.emit(false)
                        bookDetail.emit(result)
                    }
                    is ApiResult.Error -> {
                        isLoading.emit(false)
                        errorMessage.emit(result.message)
                    }
                    ApiResult.Loading -> {
                        isLoading.emit(true)
                    }
                }
            }

        }
    }

    fun getBookDetailLog(bookId: String, page: Int = 1, limit: Int = 10) {
        viewModelScope.launch {
            getBookDetailLogUseCase(bookId, page, limit).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        isLoading.emit(false)
                        val current = bookLog.value
                        val combinedLogs = if (page > 1 && current is ApiResult.Success) {
                            current.data.booklogs + result.data.booklogs
                        } else {
                            result.data.booklogs
                        }
                        val mergedData = result.data.copy(booklogs = combinedLogs)
                        bookLog.emit(ApiResult.Success(mergedData))
                        if (result.data.booklogs.size < limit) {
                            isLastPage = true
                        }
                    }
                    is ApiResult.Error -> {
                        isLoading.emit(false)
                        errorMessage.emit(result.message)
                    }
                    ApiResult.Loading -> {
                        isLoading.emit(true)
                    }
                }
            }
        }
    }

    fun loadMoreLogs(bookId: String) {
        Log.i("BookDetailScreenViewModel", "loadMoreLogs called with bookId: $bookId, currentPage: $currentPage, isLastPage: $isLastPage, isLoadingMore: $isLoadingMore")
        if (isLoadingMore || isLastPage) return

        isLoadingMore = true
        currentPage++

        getBookDetailLog(bookId, currentPage).also {
            isLoadingMore = false
        }
    }

    fun initialize(bookId: String) {
        getBookDetail(bookId)
        getBookDetailLog(bookId)
    }

    fun updateLog(bookId: String, logId: Int, type: String, content: String) {
        viewModelScope.launch {
            updateBookLogUseCase(bookId, logId, type, content).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        isLoading.emit(false)
                        getBookDetailLog(bookId)
                        showSnackBarMessage("기록이 수정되었습니다.")
                    }
                    is ApiResult.Error -> {
                        isLoading.emit(false)
                        errorMessage.emit(result.message)
                    }
                    ApiResult.Loading -> {
                        isLoading.emit(true)
                    }
                }
            }
        }
    }

    fun deleteLog(bookId: String, logId: Int, type: String) {
        viewModelScope.launch {
            deleteLogUseCase(bookId, logId, type).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        isLoading.emit(false)
                        showSnackBarMessage("기록이 삭제되었습니다.")
                        getBookDetailLog(bookId)
                    }
                    is ApiResult.Error -> {
                        isLoading.emit(false)
                        errorMessage.emit(result.message)
                    }
                    ApiResult.Loading -> {
                        isLoading.emit(true)
                    }
                }
            }
        }
    }


    fun showSnackBarMessage(s: String) {
        viewModelScope.launch {
            snackbarMessage.emit(s)
        }
    }
}