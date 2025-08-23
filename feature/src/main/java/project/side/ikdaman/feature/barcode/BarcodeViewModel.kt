package project.side.ikdaman.feature.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookSearchItemEntity
import project.side.ikdaman.domain.usecase.PostBookUseCase
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import javax.inject.Inject

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase,
    private val postBookUseCase: PostBookUseCase,
) : ViewModel() {

    private val _isbn = MutableStateFlow<String?>(null)
    val isbn = _isbn.asStateFlow()

    private val _searchResult = MutableStateFlow<BookSearchItemEntity?>(null)
    val searchResult = _searchResult.asStateFlow()

    fun searchBookWithIsbn(isbn: String?) {
        viewModelScope.launch {
            if (isbn != null) {
                val result = searchBookWithIsbnUseCase(isbn)
                if (result is ApiResult.Success) {
                    if (result.data.books.isNotEmpty()) {
                        _searchResult.value = result.data.books[0]
                    }
                }

            }
        }
    }

    fun updateIsbn(newIsbn: String) {
        _isbn.update { newIsbn }
    }

    fun resetIsbn() {
        _isbn.update { null }
    }

    fun resetSearchResult() {
        _searchResult.update { null }
    }

    fun addBook(onSuccess: () -> Unit) {
        viewModelScope.launch {
            val bookItem = searchResult.value
            if (bookItem != null) {
                postBookUseCase(AddBookItem(bookItem, "")).collect {
                    if (it is ApiResult.Success) {
                        onSuccess()
                    }
                }
            }
        }
    }
}