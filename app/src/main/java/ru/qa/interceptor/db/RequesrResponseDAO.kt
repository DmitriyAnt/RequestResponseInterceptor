package ru.qa.interceptor.db

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RequestResponseDAO {

    @Query("SELECT * FROM list_of_request")
    fun getAll(): Flow<List<RequestResponse>>

    @Query("DELETE FROM list_of_request")
    fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(item: RequestResponse): Long

    @Update
    fun update(item: RequestResponse)

    @Delete
    fun delete(item: RequestResponse)

    @Query("SELECT * FROM list_of_request JOIN body_fts ON list_of_request.id == body_fts.rowid WHERE body_fts.response_body MATCH :body ORDER BY id")
    fun searchBody(body: String): Flow<List<RequestResponse>>
}