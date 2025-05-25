package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.BookSearch
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchBookWithTitleUseCase: SearchBookWithTitleUseCase) :
    ViewModel() {

    private val _searchResult: MutableStateFlow<BookSearch?> = MutableStateFlow(null)
    val searchResult = _searchResult.asStateFlow()

    private val _selectedBookIsbn = MutableSharedFlow<String?>(replay = 1)
    val selectedBookIsbn = _selectedBookIsbn.asSharedFlow()

    fun searchBookWithTitle(title: String) {
        viewModelScope.launch {
            val result = searchBookWithTitleUseCase(title)
            _searchResult.emit(result)
        }
    }

    fun emitSelectedBookIsbn(index: Int) {
        _searchResult.value?.let { result ->
            if (result.books.isNotEmpty()) {
                _selectedBookIsbn.tryEmit(result.books[index].isbn)
            }
        }
    }
}