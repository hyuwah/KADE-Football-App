package com.muhammadwahyudin.kadefootballapp.data.model

sealed class ResourceState<T>

class LoadingState<T> : ResourceState<T>() {
    override fun hashCode(): Int = javaClass.hashCode()
    override fun equals(other: Any?) = equalz(other)
}

class EmptyState<T> : ResourceState<T>() {
    override fun hashCode(): Int = javaClass.hashCode()
    override fun equals(other: Any?) = equalz(other)
}

data class NoResultState<T>(val data: String) : ResourceState<T>()
data class PopulatedState<T>(val data: T) : ResourceState<T>()
data class ErrorState<T>(val message: String?) : ResourceState<T>()

// Make non-data class instances equals pas di compare
inline fun <reified T : ResourceState<*>> T.equalz(other: Any?) = when {
    this === other -> true
    other !is T -> false
    else -> true
}