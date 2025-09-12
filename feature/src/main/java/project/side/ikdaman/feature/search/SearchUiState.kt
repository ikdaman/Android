package project.side.ikdaman.feature.search

import androidx.compose.ui.graphics.Color
import project.side.ikdaman.core.ui.Palette
import project.side.ikdaman.domain.model.BookItem
import project.side.ikdaman.domain.model.BookSearchResult

data class SearchUiState(
    val searchKeyword: String = "",
    val searchResult: List<BookItem> = listOf(),
    val selectedColor: Color = Palette.first,
    val startPage: Int = 1,
    val cachedSearchResult: BookSearchResult = BookSearchResult()
    )
