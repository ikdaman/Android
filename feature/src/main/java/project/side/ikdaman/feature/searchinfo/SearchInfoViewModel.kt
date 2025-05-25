package project.side.ikdaman.feature.searchinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import javax.inject.Inject

@HiltViewModel
class SearchInfoViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase
) : ViewModel() {

    private val _searchResult = MutableStateFlow<BookItem?>(null)
    val searchResult = _searchResult.asStateFlow()

    fun searchBookWithIsbn(isbn: String) {
        viewModelScope.launch {
            val result = searchBookWithIsbnUseCase(isbn)
            if (result.books.isNotEmpty()) {
                _searchResult.tryEmit(result.books[0])
            }
        }
    }
}