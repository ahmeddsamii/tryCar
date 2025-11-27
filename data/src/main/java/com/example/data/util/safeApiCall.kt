package com.example.data.util

import android.util.Log
import com.example.domain.exception.NoInternetException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import io.ktor.http.HttpStatusCode
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.io.IOException

suspend inline fun <reified T> safeApiCall(
    execute: () -> HttpResponse
): T {
    val result = try {
        execute()
    } catch (exception: IOException) {
        Log.e(SAFE_API_CALL_TAG, "IOException: ${exception.message}")
        throw NoInternetException()
    } catch (exception: UnresolvedAddressException) {
        Log.e(SAFE_API_CALL_TAG, "UnresolvedAddressException: ${exception.message}")
        throw NoInternetException()
    } catch (exception: Exception) {
        Log.e(SAFE_API_CALL_TAG, "Unknown exception: ${exception.message}")
        throw exception
    }

    return handleResponseStatusCode(result)
}

suspend inline fun <reified T> handleResponseStatusCode(result: HttpResponse): T {
    return when (result.status.value) {
        in 200..299 -> {
            result.body<T>()
        }

        in 400..499 -> {
            when (result.status) {
                HttpStatusCode.NotFound -> {
                    Log.e(
                        HANDLE_ERROR_STATUS_TAG,
                        "Not found with status code:${result.status.value}"
                    )
                    throw Exception()
                }

                else -> {
                    Log.e(
                        HANDLE_ERROR_STATUS_TAG,
                        "Error with status code:${result.status.value}"
                    )
                    throw Exception()
                }
            }
        }

        in 500..599 -> {
            Log.e(
                HANDLE_ERROR_STATUS_TAG,
                "Server error with status code:${result.status.value}"
            )
            throw Exception()
        }

        else -> {
            Log.e(
                HANDLE_ERROR_STATUS_TAG,
                "server error with status code:${result.status.value}"
            )
            throw Exception()
        }
    }
}

const val HANDLE_ERROR_STATUS_TAG = "handleErrorStatus"
const val SAFE_API_CALL_TAG = "handleErrorStatus"
