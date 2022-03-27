package ru.qa.interceptor.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "list_of_request")
data class RequestResponse(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    // Request
    @ColumnInfo(name = "request_date")
    val requestDate: String = "",

    @ColumnInfo(name = "request_method")
    val requestMethod: String = "",

    @ColumnInfo(name = "request_url")
    val requestUrl: String = "",

    @ColumnInfo(name = "request_headers")
    val requestHeaders: String = "",

    @ColumnInfo(name = "request_body")
    val requestBody: String? = null,

    @ColumnInfo(name = "request_content_type")
    val requestContentType: String = "",

    @ColumnInfo(name = "request_content_length")
    val requestContentLength: Long? = 0L,

    // Response
    @ColumnInfo(name = "response_time")
    val responseTime: Long = 0L,

    @ColumnInfo(name = "response_headers")
    val responseHeaders: String = "",

    @ColumnInfo(name = "response_code")
    val responseCode: Int = 0,

    @ColumnInfo(name = "response_message")
    val responseMessage: String = "",

    @ColumnInfo(name = "response_protocol")
    val responseProtocol: String = "",

    @ColumnInfo(name = "response_body")
    val responseBody: String = ""
) {
    override fun toString() = "${id}\n" + "$requestDate\n" + "$requestMethod\n" + requestUrl
}