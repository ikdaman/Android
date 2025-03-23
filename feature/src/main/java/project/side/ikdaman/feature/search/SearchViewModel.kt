package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.BookSearch
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchBookWithTitleUseCase: SearchBookWithTitleUseCase):ViewModel() {
    private val totalCountFlow = MutableStateFlow(0)
    val totalCount = totalCountFlow.asStateFlow()

    fun searchBookWithTitle(title: String) {
        viewModelScope.launch {
            val result: BookSearch = searchBookWithTitleUseCase(title)
            println("searchBookWithTitle: $result")
            totalCountFlow.emit(result.totalBookCount)
        }
    }
}