package project.side.ikdaman.feature.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.BookShelfItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.GetBooksOnShelfUseCase
import javax.inject.Inject

@HiltViewModel
class BookShelfViewModel @Inject constructor(
    private val paletteRepository: PaletteRepository,
    private val getBooksOnShelfUseCase: GetBooksOnShelfUseCase
) : ViewModel() {
    val selectedColor = MutableStateFlow(Palette.first)

    private val _uiState: MutableStateFlow<BookShelfUiState> = MutableStateFlow(BookShelfUiState())
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<String>()
    val uiEvent = _uiEvent.asSharedFlow()

    private val _keyword = MutableStateFlow<String?>(null)
    val keyword = _keyword.asStateFlow()

    init {
        getPalette()
        observeKeyword()
    }

    private fun getPalette() {
        viewModelScope.launch {
            paletteRepository.getPalette().collect { color ->
                selectedColor.emit(Palette.getColor(color))
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun observeKeyword() {
        viewModelScope.launch {
            _keyword.debounce(DEBOUNCE_TIME)
                .map { it?.trim() }
                .distinctUntilChanged()
                .collectLatest { keyword ->
                    getBooks(keyword = keyword)
                }
        }
    }

    fun getBooks(
        filter: BookShelfFilter = _uiState.value.filter,
        keyword: String? = _keyword.value,
        page: Int = 1,
        limit: Int = PAGE_SIZE,
        isLoadMore: Boolean = false,
    ) {
        viewModelScope.launch {
            getBooksOnShelfUseCase(
                status = filter.value,
                keyword = keyword,
                page = page,
                limit = limit
            ).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _uiState.emit(
                            _uiState.value.copy(
                                isLoading = false,
                                books = if (isLoadMore) _uiState.value.books + it.data.books else it.data.books,
                                totalPage = it.data.totalPage,
                                nowPage = it.data.nowPage
                            )
                        )
                    }

                    is ApiResult.Error -> {
                        _uiState.emit(_uiState.value.copy(isLoading = false))
                        _uiEvent.emit("정보를 불러오는데 실패했습니다. 잠시 후 다시 시도해 주세요.")
                    }

                    is ApiResult.Loading -> {
                        _uiState.emit(_uiState.value.copy(isLoading = true))
                    }
                }
            }
        }
    }

    fun onKeywordChanged(keyword: String) {
        _keyword.value = keyword.trim().takeIf { it.isNotEmpty() }
    }

    fun onFilterChanged(filter: BookShelfFilter) {
        _uiState.value = _uiState.value.copy(filter = filter)
    }

    enum class BookShelfFilter(val value: String?) {
        ALL(null), PROGRESS("in-progress"), COMPLETE("completed")
    }

    data class BookShelfUiState(
        val isLoading: Boolean = false,
        val books: List<BookShelfItem> = emptyList(),
        val filter: BookShelfFilter = BookShelfFilter.ALL,
        val totalPage: Int = 0,
        val nowPage: Int = 0,
    )

    companion object {
        const val PAGE_SIZE = 15
        const val DEBOUNCE_TIME = 300L
    }
}