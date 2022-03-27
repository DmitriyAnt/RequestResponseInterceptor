package ru.qa.interceptor.recyclerview

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import ru.qa.interceptor.databinding.ItemRecyclerBinding
import ru.qa.interceptor.db.RequestResponse

class RequestRecyclerAdapter(private val uiListener: UiListener) :
    ListAdapter<RequestResponse, RequestRecyclerAdapter.UniversalBindingViewHolder>(ReqDiff) {

    override fun getItemCount() = currentList.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversalBindingViewHolder {
        val viewBinding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        viewBinding.root.setOnClickListener {
            uiListener.onClickDetail(it)
        }

        return UniversalBindingViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: UniversalBindingViewHolder, position: Int) {
        val current = currentList[position]

        val universalBindingViewHolder = holder as? UniversalBindingViewHolder
        (universalBindingViewHolder?.binding as? ItemRecyclerBinding)?.apply {
            textRequestMethod.text = current.requestMethod
            textRequestDate.text = current.requestDate
            textRequestUrl.text = current.requestUrl
            textResponseCode.text = if (current.responseCode == 0) "-" else current.responseCode.toString()
            textResponseTime.text = current.responseTime.toString()

            deleteReq.setOnClickListener { uiListener.onDelete(current) }
        }
    }


    private object ReqDiff : DiffUtil.ItemCallback<RequestResponse>() {
        override fun areItemsTheSame(oldItem: RequestResponse, newItem: RequestResponse): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: RequestResponse,
            newItem: RequestResponse
        ): Boolean {
            return oldItem.requestBody.equals(newItem.requestBody) &&
                    oldItem.requestContentLength == newItem.requestContentLength &&
                    oldItem.requestContentType == newItem.requestContentType &&
                    oldItem.requestDate == newItem.requestDate &&
                    oldItem.requestHeaders == newItem.requestHeaders &&
                    oldItem.requestMethod == newItem.requestMethod &&
                    oldItem.requestUrl == newItem.requestUrl &&
                    oldItem.responseCode == newItem.responseCode &&
                    oldItem.responseBody == newItem.responseBody &&
                    oldItem.responseHeaders == newItem.responseHeaders &&
                    oldItem.responseMessage == newItem.responseMessage &&
                    oldItem.responseProtocol == newItem.responseProtocol &&
                    oldItem.responseTime == newItem.responseTime
        }
    }

    class UniversalBindingViewHolder (fragmentBinding: ViewBinding) : RecyclerView.ViewHolder(fragmentBinding.root) {
        val binding = fragmentBinding
    }

    /*inner class RequestRecyclerHolder(holder: RecyclerView.ViewHolder) : UniversalBindingViewHolder {

        val universalBindingViewHolder = holder as? UniversalBindingViewHolder
        (universalBindingViewHolder?.binding as? ItemFormButtonBinding)?.apply
        {
            val requestMethod: TextView = view.textRequestMethod
            val requestDate: TextView = view.textRequestDate
            val requestURL: TextView = view.textRequestURL
            val responseCode: TextView = view.textResponseCode
            val responseTime: TextView = view.textResponseTime

            val deleteButton: Button = view.deleteReq
        }
    }*/
}