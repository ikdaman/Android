package project.side.ikdaman.feature.barcode

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.BookSearch
import project.side.ikdaman.domain.usecase.SearchBookWithIsbnUseCase
import project.side.ikdaman.domain.usecase.SearchBookWithTitleUseCase
import javax.inject.Inject

private val TAG = "BarcodeViewModel"

@HiltViewModel
class BarcodeViewModel @Inject constructor(
    private val searchBookWithIsbnUseCase: SearchBookWithIsbnUseCase
) : ViewModel() {

    private val _isbn = MutableStateFlow<String?>(null)
    val isbn = _isbn.asStateFlow()

    private val _searchResult = MutableStateFlow<BookSearch?>(null)
    val searchResult = _searchResult.asStateFlow()

    fun searchBookWithIsbn(isbn: String?) {
        Log.d(TAG, "searchBookWithIsbn: $isbn")
        viewModelScope.launch {
            if (isbn != null) {
                val result = searchBookWithIsbnUseCase(isbn)
                _searchResult.tryEmit(result)
            }
        }
    }

    fun updateIsbn(newIsbn: String) {
        Log.d(TAG, "updateIsbn: $newIsbn")
        _isbn.update { newIsbn }
    }

    fun resetIsbn(){
        Log.d(TAG, "resetIsbn: ")
        _isbn.update { null }
    }
}