package ru.qa.interceptor.db

import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RequestResponseRepo @Inject constructor(private var requestDAO: RequestResponseDAO) {

    fun getAllRequest(): Flow<List<RequestResponse>> {
        return requestDAO.getAll()
    }

    fun insertReq(item: RequestResponse): Long {
        return requestDAO.insert(item)
    }

    fun updateReq(item: RequestResponse) {
        requestDAO.update(item)
    }

    fun deleteReq(item: RequestResponse) {
        requestDAO.delete(item)
    }

    fun searchBody(body: String): Flow<List<RequestResponse>> {
        return requestDAO.searchBody(body)
    }

    fun deleteAll() {
        requestDAO.deleteAll()
    }
}