package com.youtube.transformation

class Transformer<T> {

    fun transform(
        models: List<T>,
        filters: List<TransformOption.Filterer<T>> = emptyList(),
        sorter: TransformOption.Sorter<T>? = null
    ): List<T> {
        var result = models
        filters.forEach { filter -> result = filter.filter(result) }
        result = sorter?.sort(result) ?: result
        return result
    }
}