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
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearchResult
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
    val selectedColor = MutableStateFlow(Palette.first)

    private val _selectedBookIsbn = MutableSharedFlow<String?>(replay = 0)
    val selectedBookIsbn = _selectedBookIsbn.asSharedFlow()

    private val _searchKeyword = MutableStateFlow("")
    val searchKeyword = _searchKeyword.asStateFlow()

    private val _searchResult: MutableStateFlow<List<BookItem>> = MutableStateFlow(listOf())
    val searchResult: StateFlow<List<BookItem>> = _searchResult.asStateFlow()

    private var startPage: Int = 1
    private var cachedSearchResult: BookSearchResult = BookSearchResult()

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
            _searchResult.update { result.books }
            startPage = 1
            cachedSearchResult = result
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
            val result = searchBookWithTitleUseCase(
                keyword = searchKeyword.value,
                startPage = startPage + 1
            )
            if (result != cachedSearchResult) {
                startPage++
                cachedSearchResult = result
                _searchResult.update { it + result.books }
            }
        }
    }
}