package com.vikingsen.cheesedemo.model.webservice

import com.vikingsen.cheesedemo.model.NetworkBoundResource
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.HttpURLConnection


@Suppress("MemberVisibilityCanPrivate", "unused")
class ApiResponse<T> : NetworkBoundResource.NetworkResponse<T> {
    override val data: T?
    val code: Int
    override val error: Throwable?
    override val errorResponse: String?

    override val successful: Boolean
        get() = code in 200..299

    constructor(error: Throwable?) {
        code = HttpURLConnection.HTTP_INTERNAL_ERROR
        data = null
        this.error = error
        this.errorResponse = error?.message
    }

    constructor(code: Int, body: T?, error: Throwable? = null) {
        this.code = code
        this.data = body
        this.error = error
        this.errorResponse = error?.message
    }

    constructor(response: Response<T>?) {
        code = response?.code() ?: HttpURLConnection.HTTP_INTERNAL_ERROR
        if (response?.isSuccessful == true) {
            data = response.body()
            error = null
            errorResponse = null
        } else {
            error = getWebServiceError(response)
            errorResponse = error.message
            data = null
        }
    }

    companion object {
        fun getWebServiceError(response: Response<*>?): WebServiceError {
            var message: String? = null
            response?.errorBody()?.let {
                try {
                    message = it.string()
                } catch (ignored: IOException) {
                    Timber.e(ignored, "error while parsing response")
                }
            }
            if (message.isNullOrBlank()) {
                message = response?.message()
            }
            return WebServiceError(message)
        }
    }

    class WebServiceError(message: String?) : Throwable(message)
}