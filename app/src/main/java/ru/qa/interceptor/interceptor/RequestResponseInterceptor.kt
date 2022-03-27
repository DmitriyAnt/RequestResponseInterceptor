package ru.qa.interceptor.interceptor

import kotlinx.coroutines.*
import okhttp3.*
import okio.Buffer
import ru.qa.interceptor.db.RequestResponse
import ru.qa.interceptor.db.RequestResponseRepo
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class RequestResponseInterceptor @Inject constructor(
    private val requestRepo: RequestResponseRepo,
    private val coroutineContext: CoroutineContext
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val mRequestBody: RequestBody? = request.body
        val mRequestContentBuffer = Buffer()
        mRequestBody?.writeTo(mRequestContentBuffer)

        val dateFormat = SimpleDateFormat("dd.MM.yyyy_HH:mm:ss_z", Locale.getDefault())

        val requestResponse = RequestResponse(
            requestMethod = request.method,
            requestUrl = request.url.toString(),
            requestHeaders = request.headers.toString(),
            requestBody = mRequestContentBuffer.readUtf8(),
            requestContentType = mRequestBody?.contentType().toString(),
            requestContentLength = mRequestBody?.contentLength(),
            requestDate = dateFormat.format(Date())
        )

        var id: Long = -1
        CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
            id = requestRepo.insertReq(requestResponse)
        }

        val t1 = System.nanoTime()
        val response = chain.proceed(request)
        val t2 = System.nanoTime()

        val responseBodyCopy: ResponseBody = response.peekBody(Long.MAX_VALUE)

        val fullRequestResponse = requestResponse.copy(
            id = id,
            responseTime = TimeUnit.NANOSECONDS.toMillis((t2 - t1)),
            responseCode = response.code,
            responseProtocol = response.protocol.name,
            responseHeaders = response.headers.toString(),
            responseMessage = response.message,
            responseBody = "Content Type: ${
                response.body?.contentType()
                    .toString()
            }\n ${responseBodyCopy.string()}"
        )

        CoroutineScope(coroutineContext).launch(Dispatchers.IO) {
            requestRepo.updateReq(fullRequestResponse)
        }

        return response
    }
}