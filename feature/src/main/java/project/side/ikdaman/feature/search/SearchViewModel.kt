package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.BookSearchResult
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBookWithTitleUseCase: SearchBookWithTitleUseCase,
    private val paletteRepository: PaletteRepository
) : ViewModel() {
    val selectedColor = MutableStateFlow(Palette.first)

    private val _selectedBookIsbn = MutableSharedFlow<String?>(replay = 0)
    val selectedBookIsbn = _selectedBookIsbn.asSharedFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    val searchResult: StateFlow<BookSearchResult?> = _searchKeyword
        .debounce(200L)
        .flatMapLatest { keyword ->
            flow {
                emit(searchBookWithTitleUseCase(keyword))
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = null
        )

    init {
        getPalette()
    }

    private fun getPalette() {
        viewModelScope.launch {
            paletteRepository.getPalette().collect { color ->
                selectedColor.emit(Palette.getColor(color))
            }
        }
    }

    fun updateSearchKeyword(title: String) {
        _searchKeyword.value = title
    }

    fun emitSelectedBookIsbn(index: Int) {
        viewModelScope.launch {
            searchResult.value?.let { result ->
                if (result.books.isNotEmpty()) {
                    _selectedBookIsbn.emit(result.books[index].isbn)
                }
            }
        }
    }
}