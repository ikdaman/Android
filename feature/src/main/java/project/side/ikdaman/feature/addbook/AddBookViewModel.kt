package project.side.ikdaman.feature.addbook

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.AddBookItem
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.PostBookUseCase
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import javax.inject.Inject

@HiltViewModel
class AddBookViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase,
    private val postBookUseCase: PostBookUseCase,
    private val paletteRepository: PaletteRepository,
) : ViewModel() {
    val selectedColor = MutableStateFlow(Palette.first)

    private val _searchResult = MutableStateFlow<BookItem?>(null)
    val searchResult = _searchResult.asStateFlow()

    private val _initialImpression = MutableStateFlow("")
    val initialImpression = _initialImpression.asStateFlow()

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

    fun addBook() {
        viewModelScope.launch {
            val addBookItem = AddBookItem(
                bookItem = _searchResult.value ?: return@launch,
                impression = initialImpression.value
            )
            postBookUseCase(addBookItem).collect { result ->
                Log.d("hkhk", "addBook Result: $result")
            }
        }
    }

    fun updateInitialImpression(impression: String) {
        _initialImpression.value = impression
    }
}