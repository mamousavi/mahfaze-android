package com.miramir.mahfaze.ui.main.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.findNavController
import com.google.android.material.snackbar.Snackbar
import com.miramir.mahfaze.R
import com.miramir.mahfaze.data.model.Resource
import dagger.android.support.DaggerFragment
import kotlinx.android.synthetic.main.fragment_notes.*
import retrofit2.HttpException
import javax.inject.Inject

class NotesFragment : DaggerFragment() {
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var viewModel: NotesViewModel

    private val adapter = NoteAdapter { v, note ->
        val action = NotesFragmentDirections.actionNotesToNoteDetail()
        action.setNoteId(note.id)
        v.findNavController().navigate(action)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_notes, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        swipeRefreshLayout.setOnRefreshListener { viewModel.sync() }
        recyclerView.adapter = adapter
        add.setOnClickListener { it.findNavController().navigate(R.id.action_notes_to_note_detail) }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel = ViewModelProviders.of(this, viewModelFactory)[NotesViewModel::class.java]
        viewModel.notes.observe(this, Observer { adapter.submitList(it) })
        viewModel.syncResult.observe(this, Observer {
            when (it.status) {
                Resource.Status.SUCCESS ->
                    if (!it.isHandled) {
                        it.isHandled = true
                        swipeRefreshLayout.isRefreshing = false
                    }
                Resource.Status.ERROR ->
                    if (!it.isHandled) {
                        it.isHandled = true
                        swipeRefreshLayout.isRefreshing = false
                        when (it.error) {
                            is HttpException ->
                                Snackbar.make(swipeRefreshLayout, R.string.error_network, Snackbar.LENGTH_LONG).show()
                            else ->
                                Snackbar.make(swipeRefreshLayout, R.string.error_connection, Snackbar.LENGTH_LONG).show()
                        }
                    }
                Resource.Status.LOADING -> swipeRefreshLayout.isRefreshing = true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_notes, menu)

        val searchItem = menu?.findItem(R.id.action_search)?.apply {
            setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
                override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                    return true
                }

                override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                    viewModel.search("")
                    return true
                }
            })
        }

        val searchView = searchItem?.actionView as SearchView?
        searchView?.apply {
            // For some reason the layout direction attribute of this view is LOCALE, therefore
            // not conforming to the root view's layout direction property.
            ViewCompat.setLayoutDirection(findViewById(R.id.search_edit_frame), ViewCompat.LAYOUT_DIRECTION_INHERIT)
            queryHint = getString(R.string.hint_search)
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    viewModel.search(query)
                    return true
                }

                override fun onQueryTextChange(newText: String): Boolean {
                    return false
                }
            })
        }
    }
}