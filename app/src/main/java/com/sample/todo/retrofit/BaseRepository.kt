package com.sample.todo.retrofit

import android.util.Log
import com.sample.todo.retrofit.api.Result
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketTimeoutException


open class BaseRepository {

    suspend fun <T : Any> getSafeApiCall(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): T? {

        val result: Result<T> = getSafeApiResult(call, errorMessage)
        Timber.e("URL : ${result}")
        var data: T? = null
        when (result) {
            is Result.Success -> {
                data = result.data

            }
            is Result.Error -> {
                Log.d("1.DataRepository", "$errorMessage & Exception - ${result.exception}")
            }
        }

        return data

    }

    private suspend fun <T : Any> getSafeApiResult(
        call: suspend () -> Response<T>,
        errorMessage: String
    ): Result<T> {
        return try {
            val response = call.invoke()
            Timber.e("URL : ${response}")
            if (response.isSuccessful) {
                if (!response.body()!!.equals("")) {
                    Result.Success(response.body()!!)
                } else {
                    Result.Error(IOException("Response not received. Please try again"))
                }
            } else {
                Result.Error(IOException("An issue occurred while processing your request"))
            }
        } catch (ex: SocketTimeoutException) {
            return Result.Error(IOException("Timeout. Please try again later"))
        } catch (ex: IOException) {
            return Result.Error(IOException("API Error. Please try again later"))
        } catch (ex: Exception) {
            ex.printStackTrace()
            Result.Error(IOException("An issue occurred while processing your request, Custom ERROR - $errorMessage"))
        }
    }
}