package ru.qa.interceptor.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.ShareActionProvider
import androidx.core.view.MenuItemCompat
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import ru.qa.interceptor.R
import ru.qa.interceptor.databinding.FragmentDetailBinding
import ru.qa.interceptor.db.RequestResponse
import ru.qa.interceptor.viewmodel.RequestViewModel

class RequestDetailFragment: FragmentBinding() {

    private var actionBar: ActionBar? = null
    private var strToIntent: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)

        setHasOptionsMenu(true)
        actionBar = (activity as AppCompatActivity).supportActionBar

        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val model: RequestViewModel by activityViewModels()
        model.selected
            .onEach {
                initUI(it)
                actionBar?.title = it.requestMethod + " " + it.requestUrl
                strToIntent = it.toString()
            }
            .launchIn(lifecycleScope)
    }

    private fun initUI(item: RequestResponse) {
        (binding as? FragmentDetailBinding)?.apply {
            textMethod.text = item.requestMethod
            textRequestDate.text = item.requestDate
            textRequestUrl.text = item.requestUrl

            textRequestHeaders.text = item.requestHeaders.ifEmpty { "-" }
            textRequestBody.text = if (item.requestBody.isNullOrEmpty()) "-" else item.requestBody
            textRequestContentType.text =
                if (item.requestContentType == "null") "-" else item.requestContentType
            textContentLength.text =
                if (item.requestContentLength == null) "0" else item.requestContentLength.toString()

            textResponceCode.text = item.responseCode.toString()
            textResponseTime.text = item.responseTime.toString()
            textResponseMessage.text = item.responseMessage.ifEmpty { "-" }
            textResponseProtocol.text = item.responseProtocol
            textResponseHeaders.text = item.responseHeaders
            textResponseBody.text = item.responseBody
        }
    }


    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.searchAction).isVisible = false
        val shareItem =
            MenuItemCompat.getActionProvider(menu.findItem(R.id.shareAction)) as ShareActionProvider
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_TEXT, strToIntent)
        shareItem.setShareIntent(mIntent)
    }
}