package ru.qa.interceptor.db.forsearch

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Fts4
import ru.qa.interceptor.db.RequestResponse

@Fts4(contentEntity = RequestResponse::class)
@Entity(tableName = "body_fts")
data class RequestResponseFTS4(
    @ColumnInfo(name = "response_body")
    val responseBody: String
)