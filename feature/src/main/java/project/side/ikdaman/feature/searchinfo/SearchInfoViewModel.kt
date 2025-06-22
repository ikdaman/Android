package project.side.ikdaman.feature.searchinfo

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import javax.inject.Inject

@HiltViewModel
class SearchInfoViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase,
    private val paletteRepository: PaletteRepository
) : ViewModel() {
    val selectedColor = MutableStateFlow(Palette.first)

    private val _searchResult = MutableStateFlow<BookItem?>(null)
    val searchResult = _searchResult.asStateFlow()

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

    fun searchBookWithIsbn(isbn: String) {
        viewModelScope.launch {
            val result = searchBookWithIsbnUseCase(isbn)
            if (result.books.isNotEmpty()) {
                _searchResult.tryEmit(result.books[0])
            }
        }
    }
}