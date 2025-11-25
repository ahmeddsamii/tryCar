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
                HttpStatusCode.Unauthorized -> {
                    logError(
                        HANDLE_ERROR_STATUS_TAG,
                        "Unauthorized",
                        "Not authorized to do this action"
                    )
                    throw Exception()
                }

                HttpStatusCode.NotFound -> {
                    logError(
                        HANDLE_ERROR_STATUS_TAG,
                        "Not found",
                        "the resource you requested could not be found"
                    )
                    throw Exception()
                }

                else -> {
                    logError(
                        "HANDLE_ERROR_STATUS_TAG",
                        "Unknown 400s status code ${result.status.value}",
                        "An error with status code ${result.status.value} happened"
                    )
                    throw Exception()
                }
            }
        }

        in 500..599 -> {
            logError(
                HANDLE_ERROR_STATUS_TAG,
                "Server error",
                "An error occurred on the server side"
            )
            throw Exception()
        }

        else -> {
            logError(
                HANDLE_ERROR_STATUS_TAG,
                "Unknown status code ${result.status.value}",
                "An error with status code ${result.status.value} happened"
            )
            throw Exception()
        }
    }
}

fun logError(
    tag: String,
    type: String,
    message: String
) {
    Log.e(tag, "$type: $message" )
}

const val HANDLE_ERROR_STATUS_TAG = "handleErrorStatus"
const val SAFE_API_CALL_TAG = "handleErrorStatus"
