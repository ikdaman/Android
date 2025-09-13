package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.PostBookUseCase
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

private const val TAG = "SearchViewModel"

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchBookWithTitleUseCase: SearchBookWithTitleUseCase,
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase,
    private val paletteRepository: PaletteRepository,
    private val postBookUseCase: PostBookUseCase,
) : ViewModel() {
    private val _selectedBookIsbn = MutableSharedFlow<String?>(replay = 0)
    val selectedBookIsbn = _selectedBookIsbn.asSharedFlow()

    private val _searchUiState = MutableStateFlow(SearchUiState())
    val searchUiState = _searchUiState.asStateFlow()

    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            paletteRepository.getPalette().collect { color ->
                _searchUiState.update {
                    it.copy(
                        selectedColor = Palette.getColor(color)
                    )
                }
            }
        }
    }

    fun updateSearchKeyword(title: String) {
        _searchUiState.update {
            it.copy(searchKeyword = title)
        }
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(100L)
            val keyword = _searchUiState.value.searchKeyword

            _searchUiState.update { it.copy(isLoading = true) }

            val result = withContext(Dispatchers.IO) {
                searchBookWithTitleUseCase(
                    keyword = keyword,
                    startPage = _searchUiState.value.startPage
                )
            }

            _searchUiState.update {
                it.copy(
                    searchResult = result.books,
                    cachedSearchResult = result,
                    startPage = 1,
                    isLoading = false
                )
            }
        }
    }

    fun selectBook(isbn: String) {
        viewModelScope.launch {
            _selectedBookIsbn.emit(isbn)
        }
    }

    fun addItemDirectly(bookItem: BookItem, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = searchBookWithIsbnUseCase(bookItem.isbn)
            if (result.books.isNotEmpty()) {
                val item = result.books.first()
                postBookUseCase(AddBookItem(item, "")).collect {
                    if (it is ApiResult.Success) {
                        onSuccess()
                    }
                }
            }
        }
    }

    fun loadMore() {
        viewModelScope.launch(Dispatchers.IO) {
            _searchUiState.update {
                it.copy(
                    isLoading = true
                )
            }
            val result = searchBookWithTitleUseCase(
                keyword = _searchUiState.value.searchKeyword,
                startPage = _searchUiState.value.startPage + 1,
            )
            if (result != _searchUiState.value.cachedSearchResult) {
                _searchUiState.update {
                    it.copy(
                        searchResult = it.searchResult + result.books,
                        cachedSearchResult = result,
                        startPage = it.startPage + 1
                    )
                }
            }
            _searchUiState.update {
                it.copy(
                    isLoading = false
                )
            }
        }
    }
}