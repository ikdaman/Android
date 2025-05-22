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
import project.side.ikdaman.domain.repository.PinningBookRepository
import project.side.ikdaman.domain.usecase.GetReadingBooksUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val pinningBookRepository: PinningBookRepository,
    private val getReadingBooksUseCase: GetReadingBooksUseCase,
    private val paletteRepository: PaletteRepository
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

    // TODO 나중에 책 추가되고 화면 업데이트 할 때도 이 함수를 호출해야 함
    fun getBooks() {
        viewModelScope.launch {
            getReadingBooksUseCase().collect { result ->
                when (result) {
                    is ApiResult.Success -> {
                        val bookList = dummy// result.data
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

    @Suppress("UNUSED_PARAMETER")
    fun deleteItem(item: HomeBookItem) {

    }

    fun saveSelectedColor(color: Color) {
        viewModelScope.launch {
            paletteRepository.setPalette(Palette.fromColor(color))
        }
    }
}



val dummy = mutableListOf(
    HomeBookItem(
        id = "0",
        imageUrl = "https://picsum.photos/250/284?random=1",
        lastEditedDateTime = System.currentTimeMillis(),
        title = "소년이 온다1",
        author = "한강1",
        firstImpression = "sample",
        progress = 1f
    ),
    HomeBookItem(
        id = "1",
        imageUrl = "https://picsum.photos/199/284?random=9",
        lastEditedDateTime = System.currentTimeMillis() - (24 * 60 * 60 * 1000),
        title = "소년이 온다2",
        author = "한강2",
        firstImpression = "네가 죽은 뒤 장례식을 치르지 못해, 내 삶이 장례식이 되었다.\n" +
                "네가 방수 모포에 싸여 청소차에 실려간 뒤에.\n" +
                "용서할 수 없는 물줄기가 번쩍이며 분수대에서 뿜어져나온 뒤에. 뿜어져나온 뒤에. 뿜어져나온 뒤에.\n" +
                "한강의 『소년이 온다』를 처음 읽었을 때, 마음이 정말 무거웠어. 광주 5·18 민주화운동을 배경으로 하고 있다 보니 첫 장부터 숨이 턱 막히는 느낌이 들더라고. 특히 주인공 동호라는 소년의 시선을 따라가다 보면, 어린 나이에 감당하기엔 너무나 참혹한 현실이 계속 펼쳐져서 읽는 내내 마음이 아팠어. 하지만 그런 상황 속에서도 사람답게 살고 싶어 하는 그 마음이 너무 짠하게 다가왔고, 그래서 더 오래 기억에 남더라. 한강 작가의 절제된 문장은 감정을 과하게 드러내지 않는데도 오히려 더 깊이 파고들었어. 이 책은 단순한 역사 소설이 아니라, 우리가 외면해선 안 되는 아픈 진실을 마주하게 하고, 그 기억을 어떻게 품고 살아갈지 진지하게 고민하게 만드는 그런 작품이었어.",
        progress = 0.8f
    ),
    HomeBookItem(
        id = "2",
        imageUrl = "https://picsum.photos/250/284?random=3",
        lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
        title = "소년이 온다3",
        author = "한강1",
        progress = 0.4f
    ),
    HomeBookItem(
        id = "3",
        imageUrl = "null",
        lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
        title = "소년이 온다4",
        author = "한강1"
    ),
    HomeBookItem(
        id = "4",
        imageUrl = "https://picsum.photos/250/284?random=8",
        lastEditedDateTime = System.currentTimeMillis(),
        title = "소년이 온다5",
        author = "한강1",
        firstImpression = "테스트 테스트",
        progress = 1f
    ),
    HomeBookItem(
        id = "5",
        imageUrl = "https://picsum.photos/199/284?random=6",
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
        lastEditedDateTime = System.currentTimeMillis() - (48 * 60 * 60 * 1000),
        title = "소년이 온다7",
        author = "한강1",
        progress = 0.4f
    ),
    HomeBookItem(
        id = "7",
        imageUrl = "null",
        lastEditedDateTime = System.currentTimeMillis() - (72 * 60 * 60 * 1000),
        title = "소년이 온다8",
        author = "한강1"
    ),
)