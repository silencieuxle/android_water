package me.gndev.water.core.model

sealed class DataResult<out R> {
    data class Success<out T>(val data: T) : DataResult<T>()

    object SuccessNoData : DataResult<Nothing>()

    data class Error(val errMsg: String?) : DataResult<Nothing>()

    object UnexpectedError : DataResult<Nothing>()

}
