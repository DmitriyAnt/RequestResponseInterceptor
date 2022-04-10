package ru.qa.interceptor.fragments

import android.os.Bundle
import android.view.*
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.Lazy
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.qa.interceptor.R
import ru.qa.interceptor.TestApplication
import ru.qa.interceptor.databinding.FragmentListBinding
import ru.qa.interceptor.db.RequestResponse
import ru.qa.interceptor.examplenetworkservice.PlaceHolderApi
import ru.qa.interceptor.examplenetworkservice.PlaceHolderData
import ru.qa.interceptor.recyclerview.RequestRecyclerAdapter
import ru.qa.interceptor.recyclerview.UiListener
import ru.qa.interceptor.viewmodel.RequestViewModel
import java.util.*
import javax.inject.Inject

class RequestListFragment : FragmentBinding(), UiListener {

    private val viewModel: RequestViewModel by activityViewModels()
    private lateinit var recyclerView: RecyclerView
    private val recyclerAdapter = RequestRecyclerAdapter(this)

    @Inject
    lateinit var mRestApi: Lazy<PlaceHolderApi>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TestApplication.component.injectToFragment(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)
        initActionBar()
        initFunc()
        initRecycler()
        return binding?.root
    }

    private fun initActionBar() {
        val mActionBar = (activity as AppCompatActivity).supportActionBar
        mActionBar?.title = context?.getString(R.string.list)
    }

    private fun initFunc() {
        (binding as? FragmentListBinding)?.apply {
            sendButtonGet1.setOnClickListener { submitRequest(1) }
            sendButtonGet2.setOnClickListener { submitRequest(2) }
            sendButtonGet3.setOnClickListener { submitRequest(3) }
            viewModel.resultFTS
                .onEach {
                    recyclerAdapter.submitList(it)
                    textItemCount.text = it.size.toString()
                }
                .launchIn(lifecycleScope)
        }

    }

    private fun submitRequest(id: Int) {
        mRestApi.get().getPostById(id, "testGetHeader_$id").enqueue(object : Callback<PlaceHolderData> {
            override fun onFailure(call: Call<PlaceHolderData>, t: Throwable) {
            }

            override fun onResponse(call: Call<PlaceHolderData>, response: Response<PlaceHolderData>) {
            }
        })
    }

    private fun initRecycler() {
        (binding as? FragmentListBinding)?.apply {
            recyclerView = requestRecycler
            requestRecycler.adapter = recyclerAdapter
            requestRecycler.addItemDecoration(
                DividerItemDecoration(
                    activity,
                    (requestRecycler.layoutManager as LinearLayoutManager).orientation
                )
            )
            viewModel.allReq
                .onEach {
                    recyclerAdapter.submitList(it)
                    textItemCount.text = it.size.toString()
                }
                .launchIn(lifecycleScope)
        }
    }

    override fun onDelete(item: RequestResponse) {
        viewModel.delete(item)
    }

    override fun onClickDetail(item: View) {
        val childrenPosition = recyclerView.getChildAdapterPosition(item)
        val currentItem = recyclerAdapter.currentList[childrenPosition]
        viewModel.select(currentItem)
        findNavController().navigate(R.id.actionListReqToDetail)
    }


    @OptIn(ExperimentalCoroutinesApi::class, kotlinx.coroutines.FlowPreview::class)
    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)

        menu.findItem(R.id.shareAction).isVisible = false

        val menuSearchItem = menu.findItem(R.id.searchAction)
        val searchView = menuSearchItem.actionView as SearchView
        val searchClose =
            searchView.findViewById(androidx.appcompat.R.id.search_close_btn) as ImageView

        searchView.setIconifiedByDefault(false)

        (binding as? FragmentListBinding)?.apply {
            menuSearchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    recyclerAdapter.submitList(viewModel.allReq.value)
                    textItemCount.text = viewModel.allReq.value.size.toString()
                    return true
                }
            })

            searchClose.setOnClickListener {
                recyclerAdapter.submitList(viewModel.allReq.value)
                textItemCount.text = viewModel.allReq.value.size.toString()
                searchView.setQuery("", false)
            }
        }

        lifecycleScope.launchWhenResumed {
            callbackFlow {
                val listener = object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        trySend(query ?: "")
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        trySend(newText ?: "")
                        return false
                    }
                }

                searchView.setOnQueryTextListener(listener)
                awaitClose { }
            }
                .debounce(500)
                .map { text -> text.lowercase(Locale.getDefault()).trim() }
                .filter { text -> text.isNotBlank() }
                .onEach { viewModel.searchBody(it) }
                .launchIn(this)
        }

    }
}