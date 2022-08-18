package com.example.composeweatherapp.data.remote

import com.example.composeweatherapp.data.model.ServerError
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.io.IOException


suspend inline fun <T> safeApiCall(
    crossinline apiCall: suspend () -> Response<T>
) = flow {
    emit(ResultWrapper.Loading)
    try {
        val response = apiCall()
        val responseBody = response.body()
        if (response.isSuccessful && responseBody != null) {
            emit(ResultWrapper.Success(responseBody))
        } else {
            val errorBody = response.errorBody()
            if (errorBody != null) {
                val type = object : TypeToken<ServerError>() {}.type
                val responseError = Gson().fromJson<ServerError>(errorBody.charStream(), type)
                emit(ResultWrapper.Error(responseError.message))
            } else {
                emit(ResultWrapper.Error("Unknown error!"))
            }
        }
    } catch (e: IOException) {
        emit(ResultWrapper.Error("We couldn't make a connection: ${e.message}"))
    } catch (e: Throwable) {
        emit(ResultWrapper.Error("Unknown error! ${e.message}"))
    }
}
