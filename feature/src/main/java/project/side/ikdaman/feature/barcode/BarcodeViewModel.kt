package project.side.ikdaman.feature.barcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import javax.inject.Inject

private val TAG = "BarcodeViewModel"

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase
) : ViewModel() {

    private val _isbn = MutableStateFlow<String?>(null)
    val isbn = _isbn.asStateFlow()

    private val _searchResult = MutableStateFlow<BookItem?>(null)
    val searchResult = _searchResult.asStateFlow()

    fun searchBookWithIsbn(isbn: String?) {
        viewModelScope.launch {
            if (isbn != null) {
                val result = searchBookWithIsbnUseCase(isbn)
                if (result.books.isNotEmpty()) {
                    _searchResult.value = result.books[0]
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
}