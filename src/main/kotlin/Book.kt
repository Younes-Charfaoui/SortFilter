package com.youtube

import java.time.LocalDate

data class Book(
    val id: Int,
    val title: String,
    val publishDate: LocalDate = LocalDate.now(),
    val genre: Genre = Genre.NonClassified,
    val isBestSeller: Boolean = false,
    val status: String = "Bought"
)

enum class Genre(val title: String) {
    NonClassified("N/A"), Comedy("Comedy"), Business("Business"),
    Software("Software"), Personal("Personal Development"), Manga("Manga")
}