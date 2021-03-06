package com.booklet.bookletandroid.presentation.fragments.schedule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.domain.model.Date
import com.booklet.bookletandroid.presentation.model.event.SelectWeekdayEvent
import org.greenrobot.eventbus.EventBus

class WeekdaysAdapter : RecyclerView.Adapter<WeekdayHolder>() {
    var activeElement = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeekdayHolder {
        return WeekdayHolder(LayoutInflater.from(parent.context)
                .inflate(R.layout.item_weekday, parent, false))
    }

    override fun onBindViewHolder(holder: WeekdayHolder, position: Int) {
        val date = Date() + (position - SchedulePagerAdapter.CENTRAL_POSITION)
        holder.bind(date, position == activeElement)
        holder.view.setOnClickListener {
            selectItem(holder.layoutPosition)

            EventBus.getDefault().post(SelectWeekdayEvent(position))
        }
    }

    fun selectItem(position: Int) {
        notifyItemChanged(activeElement)
        activeElement = position
        notifyItemChanged(activeElement)
    }

    override fun getItemCount(): Int = SchedulePagerAdapter.COUNT
}