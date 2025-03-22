package project.side.ikdaman.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.side.ikdaman.core.data.repository.BookRepository
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val bookRepository: BookRepository):ViewModel() {
    fun searchBookWithTitle(title: String) {
        viewModelScope.launch {
            bookRepository.searchBookWithTitle(title)
        }
    }
}