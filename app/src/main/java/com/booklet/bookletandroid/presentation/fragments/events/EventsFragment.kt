package com.booklet.bookletandroid.presentation.fragments.events

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.data.model.booklet.events.Event
import com.booklet.bookletandroid.databinding.FragmentEventsBinding
import com.booklet.bookletandroid.domain.Preferences
import com.booklet.bookletandroid.presentation.ViewPagerFragment
import com.booklet.bookletandroid.presentation.model.Filter
import kotlinx.android.synthetic.main.fragment_events.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.support.v4.onRefresh

class EventsFragment : ViewPagerFragment() {
    private lateinit var binding: FragmentEventsBinding

    val allItems = arrayListOf<Event>()
    var filteredItems = arrayListOf<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false)
        binding.viewModel = ViewModelProviders.of(this).get(EventsViewModel::class.java)
        binding.executePendingBindings()
        val view = binding.root

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun fetchData() {
        initRecyclerView(filteredItems)
        val prefs = Preferences.getInstance(activity!!)

        binding.viewModel!!.getEvents(prefs.userPid!!, prefs.userSecret!!)

        binding.viewModel!!.eventsLiveData.observe(this, Observer {
            eventsRefresher.isRefreshing = false
            allItems.clear()
            allItems.addAll(it!!.body()!!)
            paintItems()
        })

        eventsRefresher.onRefresh {
            binding.viewModel!!.getEvents(prefs.userPid!!, prefs.userSecret!!)
        }
    }

    private fun paintItems() {
        filteredItems.addAll(allItems)
        onFilter(Filter(Filter.State.CLOSED))
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    private fun initRecyclerView(events: List<Event>) {
        eventsRecyclerView.layoutManager = LinearLayoutManager(activity!!)
        eventsRecyclerView.setHasFixedSize(true)
        eventsRecyclerView.adapter = EventsAdapter(events)
        eventsRecyclerView.setItemViewCacheSize(30)
    }

    @Subscribe
    fun onFilter(filter: Filter) {
        val prefs = Preferences.getInstance(activity!!)
        val newItems = allItems.filter {
            (prefs.filterGrade || (it.type != "CLASS_EVENT" && it.type != "NEW_USER")) &&
                    (prefs.filterNewMarks || it.type != "NEW_MARK") &&
                    (prefs.filterTests || it.type != "TEST") &&
                    (prefs.filterHolidays || it.type != "HOLIDAYS")
        }
        val productDiffUtilCallback = EventDiffUtilCallback(filteredItems, newItems)
        val productDiffResult = DiffUtil.calculateDiff(productDiffUtilCallback)
        filteredItems.clear()
        filteredItems.addAll(newItems)
        productDiffResult.dispatchUpdatesTo(eventsRecyclerView.adapter!!)
    }
}