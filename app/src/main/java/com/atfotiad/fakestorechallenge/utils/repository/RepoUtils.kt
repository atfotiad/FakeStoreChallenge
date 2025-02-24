package com.atfotiad.fakestorechallenge.utils.repository

import android.content.Context
import androidx.appcompat.app.AlertDialog
import retrofit2.Response

object RepoUtils {
    suspend fun <T : Any> (suspend () -> Response<T>).toResult(): Result<T> {
        return try {
            val response = invoke()
            response.toResult()
        } catch (t: Throwable) {
            Result.Error(Exception(t))
        }
    }

    fun <T : Any> Response<T>.toResult(): Result<T> {
        return if (isSuccessful) {
            val body = body()
            if (body != null) {
                Result.Success(body)
            } else {
                Result.Error(Exception("Response body is null"))
            }
        } else {
            Result.Error(Exception("Network request failed with code: ${code()}"))
        }
    }

    fun <T : Any> Result<T>.getOrError(context: Context): T? {
        return when (this) {
            is Result.Success -> data
            is Result.Error -> {
                AlertDialog.Builder(context)
                    .setTitle("Error")
                    .setMessage(exception.message ?: "An unknown error occurred")
                    .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
                    .show()
                null
            }
        }
    }
}