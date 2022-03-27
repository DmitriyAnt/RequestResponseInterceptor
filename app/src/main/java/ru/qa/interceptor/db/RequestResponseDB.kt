package ru.qa.interceptor.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import ru.qa.interceptor.db.forsearch.RequestResponseFTS4

@Database(
    entities = [RequestResponse::class, RequestResponseFTS4::class],
    version = 1,
    exportSchema = false
)
abstract class RequestResponseDB : RoomDatabase() {

    abstract fun getDao(): RequestResponseDAO

    companion object {
        @Volatile
        private var INSTANCE: RequestResponseDB? = null

        fun getDataBase(context: Context, dbName: String): RequestResponseDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RequestResponseDB::class.java,
                    dbName
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}