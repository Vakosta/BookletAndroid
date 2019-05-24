package me.annenkov.julistaandroid.domain.model.mos

class ProgressResponse(val subjectName: String,
                       val avgFive: Float,
                       val avgHundred: Float,
                       val periods: List<PeriodResponse>,
                       val final: Int?)
