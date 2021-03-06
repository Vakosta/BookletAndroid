package com.booklet.bookletandroid.presentation.model.event

class Event(
        val id: Int,
        val type: Type,
        val text: String,
        val date: String
) {
    enum class Type(val code: Int) {
        GRADE(1),
        TEST(2),
        HOLIDAYS(3),
        SUBSCRIPTION(4),
        NEW_MARK(5),
        NEW_USER(6),
    }
}