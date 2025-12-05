package com.example.moontrade.di

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class NetworkLoggingInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val startNs = System.nanoTime()

        val response = chain.proceed(request)

        val tookMs = (System.nanoTime() - startNs) / 1_000_000
        val encoding = response.header("Content-Encoding") ?: "none"
        val contentLength = response.header("Content-Length") ?: "unknown"

        val peek = response.peekBody(Long.MAX_VALUE).string()

        Log.d("HTTP_RAW", """
            📡 RAW NETWORK RESPONSE:
            URL: ${request.url}
            Time: ${tookMs}ms
            Content-Encoding: $encoding
            Content-Length: $contentLength
            Body: $peek
        """.trimIndent())

        return response
    }
}