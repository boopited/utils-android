package com.boopited.utils_android.network.graphql.model

class NoDataException: Exception(MESSAGE) {
    companion object {
        private const val MESSAGE = "Data received is null"
    }
}