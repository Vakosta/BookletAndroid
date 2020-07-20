package com.booklet.bookletandroid.presentation.fragments.newschedule

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.viewpager.widget.ViewPager
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.databinding.FragmentScheduleBinding
import com.booklet.bookletandroid.domain.Utils
import com.booklet.bookletandroid.domain.model.Date
import com.booklet.bookletandroid.domain.model.Result
import com.booklet.bookletandroid.presentation.ViewPagerFragment
import com.booklet.bookletandroid.presentation.customviews.RotateDownTransformer
import com.booklet.bookletandroid.presentation.fragments.newschedulecard.NewScheduleCardFragment
import com.booklet.bookletandroid.presentation.fragments.schedule.SchedulePagerAdapter
import kotlinx.android.synthetic.main.fragment_schedule.*
import kotlinx.android.synthetic.main.layout_week_days.*
import org.greenrobot.eventbus.EventBus
import kotlin.math.abs

class NewScheduleFragment : ViewPagerFragment(), View.OnClickListener, View.OnTouchListener,
        NewScheduleCardFragment.ScheduleDataListener {
    private lateinit var mViewModel: NewScheduleViewModel
    private lateinit var mBinding: FragmentScheduleBinding

    private var mIndex = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        mViewModel = ViewModelProvider(this).get(NewScheduleViewModel::class.java)
        mBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_schedule,
                container,
                false)
        mBinding.viewModel = mViewModel

        val view = mBinding.root
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        setupObservers()
        // setupListeners()
        initWeekdaysViewPager()
        initPager()
    }

    override fun fetchData() {
    }

    private fun setupObservers() {
        mViewModel.scheduleLiveData.observe(viewLifecycleOwner, Observer {
            it?.let { result ->
                when (result.status) {
                    Result.Status.SUCCESS -> {
                        EventBus.getDefault().post(result.data)
                    }

                    Result.Status.ERROR -> {
                    }

                    Result.Status.LOADING -> {
                    }
                }
            }
        })
    }

    private fun setupListeners() {
        weekdayMonday.setOnClickListener(this)
        weekdayTuesday.setOnClickListener(this)
        weekdayWednesday.setOnClickListener(this)
        weekdayThursday.setOnClickListener(this)
        weekdayFriday.setOnClickListener(this)
        weekdaySaturday.setOnClickListener(this)

        currentDay.setOnClickListener {
            setPagerPosition(5000)
        }
    }

    private fun initWeekdaysViewPager() {
        activityEnabled {
            val adapter = WeekdaysAdapter()
            val snapHelper = LinearSnapHelper()

            weekdaysRecyclerView.layoutManager = CenterLayoutManager(requireActivity(),
                    LinearLayoutManager.HORIZONTAL, false)
            snapHelper.attachToRecyclerView(weekdaysRecyclerView)
            weekdaysRecyclerView.attachSnapHelperWithListener(snapHelper,
                    SnapOnScrollListener.Behavior.NOTIFY_ON_SCROLL,
                    object : OnSnapPositionChangeListener {
                        override fun onSnapPositionChange(position: Int) {
                            EventBus.getDefault().post(Date() + (position -
                                    adapter.itemCount / 2))
                        }
                    })
            weekdaysRecyclerView.adapter = adapter
            weekdaysRecyclerView.scrollToPosition(adapter.itemCount / 2)
            weekdaysRecyclerView.smoothScrollToPosition(adapter.itemCount / 2)
            weekdaysRecyclerView.setHasFixedSize(true)
            weekdaysRecyclerView.setItemViewCacheSize(30)
        }
    }

    private fun initPager() {
        activityEnabled {
            setPagerPosition(5000)
            scheduleListPager.adapter = SchedulePagerAdapter(childFragmentManager, it,
                    this)
            scheduleListPager.setPageTransformer(false, RotateDownTransformer())
            scheduleListPager.offscreenPageLimit = 1
            scheduleListPager.setPadding(-11, 0, -15, 0)
            scheduleListPager.clipToPadding = false

            scheduleListPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageSelected(position: Int) {
                    // TODO: Implement (or refactor) this method.

                    // mViewModel.getSchedule("12.07.2020", "12.07.2020")
                }

                override fun onPageScrollStateChanged(state: Int) {
                }

                override fun onPageScrolled(position: Int,
                                            positionOffset: Float,
                                            positionOffsetPixels: Int) {
                }
            })
        }
    }

    private fun setPagerPosition(position: Int) {
        scheduleListPager.setCurrentItem(position, abs(mIndex - position) <= 8)
    }

    private fun setWeekPagerPosition(position: Int) {
        // TODO: Реализовать обновление Pager с неделями.
    }

    override fun onClick(view: View) {
        mViewModel.currentWeekday = when (view.id) {
            R.id.weekdayMonday ->
                NewScheduleViewModel.Weekday.MONDAY

            R.id.weekdayTuesday ->
                NewScheduleViewModel.Weekday.TUESDAY

            R.id.weekdayWednesday ->
                NewScheduleViewModel.Weekday.WEDNESDAY

            R.id.weekdayThursday ->
                NewScheduleViewModel.Weekday.THURSDAY

            R.id.weekdayFriday ->
                NewScheduleViewModel.Weekday.FRIDAY

            else ->
                NewScheduleViewModel.Weekday.SATURDAY
        }
    }

    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        return if (isAdded) Utils.convertPixelsToDp(requireActivity(), motionEvent.y) > 118
        else true
    }

    override fun onRequestScheduleData(date: Date) {
        mViewModel.getSchedule("12.07.2020", "12.07.2020")
    }
}