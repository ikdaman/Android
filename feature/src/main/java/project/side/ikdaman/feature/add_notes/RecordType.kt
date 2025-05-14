package project.side.ikdaman.feature.add_notes

enum class RecordType {
    FIRST,
    MIDDLE,
    FINAL, ;

    companion object {
        fun from(recordType: String?): RecordType {
            return when (recordType) {
                FIRST.name -> FIRST
                MIDDLE.name -> MIDDLE
                FINAL.name -> FINAL
                else -> FIRST
            }
        }
    }
}