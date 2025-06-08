package project.side.ikdaman.feature.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.HomeBookItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.repository.PinningBookRepository
import project.side.ikdaman.domain.usecase.DeleteBookUseCase
import project.side.ikdaman.domain.usecase.GetBookDetailUseCase
import project.side.ikdaman.domain.usecase.GetReadingBooksUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pinningBookRepository: PinningBookRepository,
    private val getReadingBooksUseCase: GetReadingBooksUseCase,
    private val paletteRepository: PaletteRepository,
    private val deleteBookUseCase: DeleteBookUseCase,
    private val getBookDetailUseCase: GetBookDetailUseCase
) : ViewModel() {

    val books = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val pinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val unpinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val selectedColor = MutableStateFlow(Palette.first)

    val isLoading = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    init {
        getBooks()
        getPalette()
    }

    private fun getPalette() {
        viewModelScope.launch {
            paletteRepository.getPalette().collect { color ->
                selectedColor.emit(Palette.getColor(color))
            }
        }
    }

    fun getBooks() {
        viewModelScope.launch {
            getReadingBooksUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val bookList = result.data
                        books.emit(bookList)
                        pinningBookRepository.getPinningBook().collect { pinnedBookList ->
                            val pinned = bookList.filter { book -> book.id in pinnedBookList }
                            val unpinned = bookList.filter { book -> book.id !in pinnedBookList }
                            pinnedItems.emit(pinned)
                            unpinnedItems.emit(unpinned)
                        }
                    }

                    is ApiResult.Error -> {
                        errorMessage.emit(result.message)
                    }

                    else -> {
                        isLoading.emit(true)
                    }
                }
            }
        }
    }

    fun pinItem(id: String) {
        viewModelScope.launch {
            val item = unpinnedItems.value.find { it.id == id }
            if (item != null) {
                pinningBookRepository.setPinningBook(id).collect {
                    if (!it) {
                        errorMessage.emit("일시적인 오류입니다. 잠시 후 다시 시도해주세요.")
                    }
                }
            } else {
                val pinnedItem = pinnedItems.value.find { it.id == id }
                if (pinnedItem != null) {
                    pinningBookRepository.removePinningBook(id).collect {
                        if (!it) {
                            errorMessage.emit("일시적인 오류입니다. 잠시 후 다시 시도해주세요.")
                        }
                    }
                }
            }
        }
    }

    fun deleteItem(item: HomeBookItem) {
        viewModelScope.launch {
            deleteBookUseCase(item.id).take(1).collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        // 성공적으로 삭제됨
                        isLoading.emit(false)
                        getBooks() // 책 목록을 다시 가져와서 UI 업데이트
                    }
                    is ApiResult.Error -> {
                        isLoading.emit(false)
                        errorMessage.emit(result.message)
                    }

                    ApiResult.Loading -> isLoading.emit(true)
                }
            }
        }
    }

    fun saveSelectedColor(color: Color) {
        viewModelScope.launch {
            paletteRepository.setPalette(Palette.fromColor(color))
        }
    }
}
