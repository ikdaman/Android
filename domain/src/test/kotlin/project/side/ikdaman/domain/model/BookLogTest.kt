package project.side.ikdaman.domain.model

import org.junit.Assert.assertEquals
import org.junit.Test

class BookLogTest {
    @Test
    fun getLogStringTest() {
        val bookLogItem = BookLogItem(
            booklogId = 19,
            loggedDate = "2025-06-09T11:02:35.584396",
            page = 123,
            content = "Sample content",
            type = "THINK"
        )

        val logString = bookLogItem.getLogString()
        assertEquals("25/06/09 11:12", logString)
    }
}