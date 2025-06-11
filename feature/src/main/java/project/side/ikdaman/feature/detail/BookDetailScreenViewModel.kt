package project.side.ikdaman.feature.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookDetail
import project.side.ikdaman.domain.model.BookLog
import project.side.ikdaman.domain.usecase.GetBookDetailLogUseCase
import project.side.ikdaman.domain.usecase.GetBookDetailUseCase
import javax.inject.Inject

@HiltViewModel
class BookDetailScreenViewModel @Inject constructor(
    private val getBookDetailUseCase: GetBookDetailUseCase,
    private val getBookDetailLogUseCase: GetBookDetailLogUseCase
) : ViewModel() {

    private val errorMessage = MutableStateFlow("")
    val errorMessageState = errorMessage.asStateFlow()

    private val bookDetail = MutableStateFlow<ApiResult<BookDetail>>(ApiResult.Loading)
    val bookDetailState = bookDetail.asStateFlow()

    private val bookLog = MutableStateFlow<ApiResult<BookLog>>(ApiResult.Loading)
    val bookLogState = bookLog.asStateFlow()

    val isLoading = MutableStateFlow(false)

    fun getBookDetail(bookId: String) {
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

    fun getBookDetailLog(bookId: String, page: Int = 1, limit: Int = 20) {
        viewModelScope.launch {
            getBookDetailLogUseCase(bookId, page, limit).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        isLoading.emit(false)
                        bookLog.emit(result)
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

    fun initialize(bookId: String) {
        getBookDetail(bookId)
        getBookDetailLog(bookId)
    }

}