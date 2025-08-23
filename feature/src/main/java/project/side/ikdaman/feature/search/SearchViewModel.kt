package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookSearchItemEntity
import project.side.ikdaman.domain.model.BookSearchEntity
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

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

    private val _searchResult: MutableStateFlow<List<BookSearchItemEntity>> = MutableStateFlow(listOf())
    val searchResult: StateFlow<List<BookSearchItemEntity>> = _searchResult.asStateFlow()

    private var startPage: Int = 1
    private var cachedSearchResult: BookSearchEntity = BookSearchEntity()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            paletteRepository.getPalette().collect { color ->
                selectedColor.emit(Palette.getColor(color))
            }
        }
    }

    fun updateSearchKeyword(title: String) {
        _searchKeyword.update { title }
        searchJob?.cancel()
        searchJob = viewModelScope.launch(Dispatchers.IO) {
            delay(50L)
            val result = searchBookWithTitleUseCase(
                keyword = title,
                startPage = startPage
            )
            when(result) {
                is ApiResult.Success -> {
                    _searchResult.update { result.data.books }
                    startPage = 1
                    cachedSearchResult = result.data
                } else -> {

                }
            }
        }
    }

    fun selectBook(isbn: String) {
        viewModelScope.launch {
            _selectedBookIsbn.emit(isbn)
        }
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = searchBookWithTitleUseCase(
                keyword = searchKeyword.value,
                startPage = startPage + 1
            )
            if (result is ApiResult.Success) {
                if (result.data != cachedSearchResult) {
                    startPage++
                    cachedSearchResult = result.data
                    _searchResult.update { it + result.data.books }
                }
            }
        }
    }
}