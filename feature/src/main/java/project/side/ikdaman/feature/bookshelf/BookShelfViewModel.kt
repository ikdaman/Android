package project.side.ikdaman.feature.bookshelf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.repository.PaletteRepository
import javax.inject.Inject

@HiltViewModel
class BookShelfViewModel @Inject constructor(
    private val paletteRepository: PaletteRepository
): ViewModel(){
    val selectedColor = MutableStateFlow(Palette.first)

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
}