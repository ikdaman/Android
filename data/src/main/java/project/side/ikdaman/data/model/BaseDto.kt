package project.side.ikdaman.data.model

interface BaseDto<T> {
    fun toEntity(): T
}