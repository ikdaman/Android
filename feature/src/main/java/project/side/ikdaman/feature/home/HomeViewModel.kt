package project.side.ikdaman.feature.home

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.model.HomeBookItem
import project.side.ikdaman.domain.repository.PaletteRepository
import project.side.ikdaman.domain.usecase.DeleteBookUseCase
import project.side.ikdaman.domain.usecase.GetPinningBookUseCase
import project.side.ikdaman.domain.usecase.GetReadingBooksUseCase
import project.side.ikdaman.domain.usecase.RemovePinningBookUseCase
import project.side.ikdaman.domain.usecase.SetPinningBookUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPinningBookUseCase: GetPinningBookUseCase,
    private val getReadingBooksUseCase: GetReadingBooksUseCase,
    private val setPinningBookUseCase: SetPinningBookUseCase,
    private val removePinningBookUseCase: RemovePinningBookUseCase,
    private val paletteRepository: PaletteRepository,
    private val deleteBookUseCase: DeleteBookUseCase,
) : ViewModel() {

    val pinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val unpinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val selectedColor = MutableStateFlow(Palette.first)

    val isLoading = MutableStateFlow(false)
    val errorMessage = MutableStateFlow("")

    fun initialize() {
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
            isLoading.emit(true)
            getReadingBooksUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        getPinningBookUseCase(result.data).collect { pair ->
                            pinnedItems.emit(pair.first)
                            unpinnedItems.emit(pair.second)
                            isLoading.emit(false)
                        }
                    }

                    is ApiResult.Error -> {
                        errorMessage.emit(result.message)
                        isLoading.emit(false)
                    }

                    else -> {

                    }
                }
            }
        }
    }

    fun pinItem(id: String) {
        viewModelScope.launch {
            val item = unpinnedItems.value.find { it.id == id }
            if (item != null) {
                if (!setPinningBookUseCase(id).not()) {
                    errorMessage.emit("일시적인 오류입니다. 잠시 후 다시 시도해주세요.")
                }
            } else {
                val pinnedItem = pinnedItems.value.find { it.id == id }
                if (pinnedItem != null) {
                    if (removePinningBookUseCase(id).not()){
                        errorMessage.emit("일시적인 오류입니다. 잠시 후 다시 시도해주세요.")
                    }
                }
            }
        }
    }

    fun deleteItem(bookId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            val result = deleteBookUseCase(bookId)
            if (result is ApiResult.Success){
                isLoading.emit(false)
                onSuccess()
            } else {
                isLoading.emit(false)
                errorMessage.emit((result as ApiResult.Error).message)
            }
        }
    }

    fun saveSelectedColor(color: Color) {
        viewModelScope.launch {
            paletteRepository.setPalette(Palette.fromColor(color))
        }
    }
}
