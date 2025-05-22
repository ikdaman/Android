package project.side.ikdaman.feature.add_notes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.model.ApiResult
import project.side.ikdaman.domain.repository.MyBooksApiRepository
import javax.inject.Inject

@HiltViewModel
class AddRecordViewModel @Inject constructor(
    private val repository: MyBooksApiRepository
) : ViewModel() {
    fun addFirstImpression(
        bookId: String,
        firstImpression: String
    ) = viewModelScope.launch {
        repository.postImpression(bookId, firstImpression).collect {
            when (it) {
                is ApiResult.Loading -> {
                    // Handle loading state
                }

                is ApiResult.Success -> {
                    // Handle success
                }

                is ApiResult.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun addMiddleRecord(bookId: String, text: String, page: Int) = viewModelScope.launch {
        repository.addThink(bookId, text, page).collect {
            when (it) {
                is ApiResult.Loading -> {
                    // Handle loading state
                }

                is ApiResult.Success -> {
                    // Handle success
                }

                is ApiResult.Error -> {
                    // Handle error
                }
            }
        }
    }

    fun addCompletedRecord(bookId: String, content: String) = viewModelScope.launch {
            repository.addCompleted(bookId, content).collect {
                when (it) {
                    is ApiResult.Loading -> {
                        // Handle loading state
                    }

                    is ApiResult.Success -> {
                        // Handle success
                    }

                    is ApiResult.Error -> {
                        // Handle error
                    }
                }
            }
        }
}