package ru.qa.interceptor.recyclerview

import android.view.View
import ru.qa.interceptor.db.RequestResponse

interface UiListener {
    fun onDelete(item: RequestResponse)
    fun onClickDetail(item: View)
}