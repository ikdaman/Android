package project.side.ikdaman.feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import project.side.ikdaman.domain.repository.PinningBookRepository
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pinningBookRepository: PinningBookRepository
) : ViewModel() {

    val books = mutableListOf(
        HomeBookItem(
            id = "0",
            imageUrl = "https://picsum.photos/250/284?random=1",
            addedDateTime = System.currentTimeMillis(),
            lastEditedDateTime = System.currentTimeMillis(),
            title = "소년이 온다1",
            author = "한강1",
            firstImpression = "테스트 테스트",
            progress = 1f
        ),
        HomeBookItem(
            id = "1",
            imageUrl = "https://picsum.photos/199/284?random=9",
            addedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
            title = "소년이 온다2",
            author = "한강2",
            firstImpression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                    "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                    "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
            progress = 0.8f
        ),
        HomeBookItem(
            id = "2",
            imageUrl = "https://picsum.photos/250/284?random=3",
            addedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
            title = "소년이 온다3",
            author = "한강1",
            progress = 0.4f
        ),
        HomeBookItem(
            id = "3",
            imageUrl = "null",
            addedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
            title = "소년이 온다4",
            author = "한강1"
        ),
        HomeBookItem(
            id = "4",
            imageUrl = "https://picsum.photos/250/284?random=8",
            addedDateTime = System.currentTimeMillis(),
            lastEditedDateTime = System.currentTimeMillis(),
            title = "소년이 온다5",
            author = "한강1",
            firstImpression = "테스트 테스트",
            progress = 1f
        ),
        HomeBookItem(
            id = "5",
            imageUrl = "https://picsum.photos/199/284?random=6",
            addedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
            title = "소년이 온다6",
            author = "한강2",
            firstImpression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                    "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                    "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에.",
            progress = 0.8f
        ),
        HomeBookItem(
            id = "6",
            imageUrl = "https://picsum.photos/250/284?random=7",
            addedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
            title = "소년이 온다7",
            author = "한강1",
            progress = 0.4f
        ),
        HomeBookItem(
            id = "7",
            imageUrl = "null",
            addedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
            lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
            title = "소년이 온다8",
            author = "한강1"
        ),
    )

    val pinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())
    val unpinnedItems = MutableStateFlow<List<HomeBookItem>>(emptyList())

    init {
        getPinnedBooks()
    }

    private fun getPinnedBooks() {
        viewModelScope.launch {
            pinningBookRepository.getPinningBook().collect { bookList ->
                val pinned = books.filter { book -> book.id in bookList }
                val unpinned = books.filter { book -> book.id !in bookList }
                pinnedItems.emit(pinned)
                unpinnedItems.emit(unpinned)
            }
        }
    }

    fun pinItem(id: String) {
        viewModelScope.launch {
            val item = unpinnedItems.value.find { it.id == id }
            if (item != null) {
                pinningBookRepository.setPinningBook(id).collect {
                    if (it) {
                        getPinnedBooks()
                    }
                }
            } else {
                val pinnedItem = pinnedItems.value.find { it.id == id }
                if (pinnedItem != null) {
                    pinningBookRepository.removePinningBook(id).collect {
                        if (it) {
                            getPinnedBooks()
                        }
                    }
                }
            }
        }
    }

    fun deleteItem(item: HomeBookItem) {

    }
}