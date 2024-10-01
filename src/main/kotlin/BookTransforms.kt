package com.youtube

import com.youtube.transformation.SortType
import com.youtube.transformation.TransformOption

enum class BookOthers {
    IsBestSeller,
    IsComedy,
    IsPersonal,
    Before2009
}

sealed class BookTransforms {
    data class TitleSort(private val sortType: SortType = SortType.ASC) : BookTransforms(),
        TransformOption.Sorter<Book> {
        override fun sort(models: List<Book>) = when (sortType) {
            SortType.ASC -> models.sortedBy { it.title }
            SortType.DESC -> models.sortedByDescending { it.title }
        }
    }

    data class IdSort(private val sortType: SortType) : BookTransforms(), TransformOption.Sorter<Book> {
        override fun sort(models: List<Book>) = when (sortType) {
            SortType.ASC -> models.sortedBy { it.id }
            SortType.DESC -> models.sortedByDescending { it.id }
        }
    }

    data class TitleIdSort(private val sortType: SortType) : BookTransforms(), TransformOption.Sorter<Book> {
        override fun sort(models: List<Book>) = when (sortType) {
            SortType.ASC -> models.sortedWith(compareBy({ it.title }, { it.id }))
            SortType.DESC -> models.sortedWith(compareByDescending<Book> { it.title }.thenByDescending { it.id })
        }
    }

    class GenreFilter private constructor(
        private val genres: List<Genre> = emptyList()
    ) : BookTransforms(), TransformOption.Filterer<Book> {

        constructor(genre: Genre) : this(listOf(genre))

        constructor(vararg genres: Genre) : this(genres.toList())

        override fun filter(models: List<Book>): List<Book> {
            if (genres.isEmpty()) return models
            return models.filter { genres.contains(it.genre) }
        }
    }

    data object IsBestSellerFilter : BookTransforms(), TransformOption.Filterer<Book> {
        override fun filter(models: List<Book>) = models.filter { it.isBestSeller }
    }

    data object NonEmptyStatus : BookTransforms(), TransformOption.Filterer<Book> {
        override fun filter(models: List<Book>): List<Book> {
            return models.filter { it.status.isNotEmpty() }
        }
    }

    data class OtherFilter(val others: List<BookOthers> = emptyList()) : BookTransforms(),
        TransformOption.Filterer<Book> {

        constructor(vararg others: BookOthers) : this(others.toList())

        override fun filter(models: List<Book>): List<Book> {
            val result = mutableListOf<Book>()
            for (other in others) {
                when (other) {
                    BookOthers.IsBestSeller -> result.addAll(models.filter { it.isBestSeller && !result.contains(it) })
                    BookOthers.IsComedy -> result.addAll(models.filter { it.genre == Genre.Comedy && !result.contains(it) })
                    BookOthers.IsPersonal -> result.addAll(models.filter {
                        it.genre == Genre.Personal && !result.contains(
                            it
                        )
                    })

                    BookOthers.Before2009 -> result.addAll(models.filter {
                        it.publishDate.year < 2009 && !result.contains(it)
                    })
                }
            }
            return result
        }

    }
}