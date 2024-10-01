package com.youtube.transformation

sealed interface TransformOption<T> {
    interface Filterer<T> : TransformOption<T> {
        fun filter(models: List<T>): List<T>
    }

    interface Sorter<T> : TransformOption<T> {
        fun sort(models: List<T>): List<T>
    }
}