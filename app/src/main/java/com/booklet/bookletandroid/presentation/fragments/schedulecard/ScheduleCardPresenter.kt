package com.booklet.bookletandroid.presentation.fragments.schedulecard

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.data.model.booklet.journal.MarksItem
import com.booklet.bookletandroid.data.model.booklet.journal.SubjectsItem
import com.booklet.bookletandroid.domain.ApiHelper
import com.booklet.bookletandroid.domain.DateHelper
import com.booklet.bookletandroid.domain.Utils
import com.booklet.bookletandroid.domain.model.Time
import com.booklet.bookletandroid.domain.px
import com.booklet.bookletandroid.presentation.InitContentPresenter
import org.jetbrains.anko.*

class ScheduleCardPresenter(
        private val view: ScheduleCardView,
        private val mContext: Context
) : InitContentPresenter(mContext) {
    var mPosition: Int = 0
    lateinit var mDate: String

    var scrollY = 0

    fun init(position: Int, date: String) {
        mPosition = position
        mDate = date
        initContent()
    }

    private fun paintSchedule(list: List<SubjectsItem?>) {
        doAsync {
            if (list.isEmpty())
                uiThread { view.setEmptyContentLayout() }

            val height = 12.px

            val addableView = LinearLayout(mContext)
            addableView.orientation = LinearLayout.VERTICAL

            var counter = 0
            var previous = list[0]
            val layoutInflater = LayoutInflater.from(mContext)
            val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
            for (i in list) {
                counter++
                layoutParams.setMargins(0, 0, 0, height)

                val local = layoutInflater
                        .inflate(R.layout.item_schedule, null, false)
                local.findViewById<TextView>(R.id.scheduleItemIndex).text = counter.toString()
                local.findViewById<TextView>(R.id.scheduleItemSubject).text = i?.name ?: ""

                val homeworkView = local.findViewById<TextView>(R.id.scheduleItemHomework)
                val homeworkDescription = if (i?.assignments != null) {
                    i.assignments[0]?.text ?: ""
                } else {
                    ""
                }
                prepareHomeworkView(homeworkView, homeworkDescription)

                val timeView = local.findViewById<TextView>(R.id.scheduleItemTime)
                val startTime = i!!.time!![0]!!.split(":").map { it.toInt() }
                val endTime = i.time!![1]!!.split(":").map { it.toInt() }
                val previousEndTime = previous!!.time!![1]!!
                        .split(":").map { it.toInt() }
                prepareTimeView(timeView, counter,
                        Time(startTime[0], startTime[1]),
                        Time(endTime[0], endTime[1]),
                        Time(previousEndTime[0], previousEndTime[1]))

                // TODO: Реализовать аттачменты
                /*val attachmentView = local.findViewById<ImageView>(R.id.scheduleItemAttachment)
                if (i.homework?.attachments != null && i.homework!!.attachments.isNotEmpty()) {
                    attachmentView.setOnClickListener {
                        mContext.browse("https://dnevnik.mos.ru${i.homework!!.attachments[0]}")
                    }
                    attachmentView.visibility = View.VISIBLE
                }*/

                val linkView = local.findViewById<ImageView>(R.id.scheduleItemLink)
                val urls = Utils.extractUrls(homeworkDescription)
                if (urls.isNotEmpty()) {
                    linkView.setOnClickListener {
                        mContext.browse(urls[0])
                    }
                    linkView.visibility = View.VISIBLE
                }

                val markView = local.findViewById<LinearLayout>(R.id.scheduleMarks)
                prepareMarksView(markView, i.marks ?: arrayListOf())

                previous = i

                local.setOnLongClickListener {
                    Utils.copyToClipboard(mContext, "Д/З", homeworkDescription)
                    mContext.toast("Д/З скопировано")
                    true
                }

                addableView.addView(local, layoutParams)
            }

            uiThread { view.setContentLayout(addableView) }
        }
    }

    private fun prepareHomeworkView(view: TextView, description: String) {
        if (description.isEmpty()) {
            view.visibility = View.GONE
        } else {
            view.text = description
        }
    }

    private fun prepareTimeView(view: TextView,
                                lessonNumber: Int,
                                beginTime: Time,
                                endTime: Time,
                                endPreviousTime: Time) {
        val timeText = "${beginTime.hour.toString().padStart(2, '0')}:" +
                "${beginTime.minute.toString().padStart(2, '0')}-" +
                "${endTime.hour.toString().padStart(2, '0')}:" +
                "${endTime.minute.toString().padStart(2, '0')}"
        val currentDate = DateHelper.getDate().format("DD.MM.YYYY")

        if (currentDate == mDate && DateHelper.isTimeInInterval(DateHelper.getCurrentTime(),
                        beginTime,
                        endTime)) {
            view.background = ContextCompat
                    .getDrawable(mContext, R.drawable.background_current_lesson)
            view.textColor = Color.WHITE
            val paddingDp = 6.px
            view.setPadding(paddingDp, 0, paddingDp, 0)
            view.text = "$timeText — ${mContext.getString(R.string.currentLesson)}"
        } else if (currentDate == mDate && lessonNumber > 1 &&
                DateHelper.isTimeInInterval(DateHelper.getCurrentTime(),
                        endPreviousTime,
                        beginTime)) {
            view.background = ContextCompat
                    .getDrawable(mContext, R.drawable.background_next_lesson)
            view.textColor = Color.WHITE
            val paddingDp = 6.px
            view.setPadding(paddingDp, 0, paddingDp, 0)
            view.text = "$timeText — ${mContext.getString(R.string.next_lesson)}"
        } else {
            view.text = timeText
        }
    }

    private fun prepareMarksView(ll: LinearLayout, marks: List<MarksItem?>) {
        var isCoefficients = false
        for ((index, mark) in marks.withIndex()) {
            val view = LayoutInflater.from(mContext)
                    .inflate(R.layout.layout_mark, null, false) as RelativeLayout
            val markView: TextView = view.find(R.id.scheduleItemMark)
            val weightView: TextView = view.find(R.id.scheduleItemMarkWeight)
            val param = RelativeLayout.LayoutParams(22.px, 22.px)
            if (index < marks.size) {
                param.marginEnd = 4.px
            }
            markView.layoutParams = param

            // TODO: Реализовать !mark.isPoint
            if (true) {
                markView.text = mark!!.score.toString()
                markView.background = when (mark.score) {
                    "5" -> ContextCompat.getDrawable(mContext, R.drawable.background_mark_five)
                    "4" -> ContextCompat.getDrawable(mContext, R.drawable.background_mark_four)
                    "3" -> ContextCompat.getDrawable(mContext, R.drawable.background_mark_three)
                    else -> ContextCompat.getDrawable(mContext, R.drawable.background_mark_two)
                }

                if (mark.weight!! > 1) {
                    weightView.text = mark.weight.toString()
                    param.marginEnd = (-6).px
                    isCoefficients = true
                } else {
                    weightView.visibility = View.GONE
                }
            } else {
                markView.background = ContextCompat.getDrawable(mContext, R.drawable.background_point)
                val params = RelativeLayout.LayoutParams(8.px, 8.px)
                params.marginEnd = 24.px
                markView.layoutParams = params
            }
            ll.addView(view)
        }
        if (marks.isEmpty())
            ll.visibility = View.GONE
        if (isCoefficients)
            ll.topPadding = 8.px
    }

    override fun executeMethod(): List<SubjectsItem?> = ApiHelper.getInstance(mContext)
            .getSchedule(prefs.userPid!!,
                    prefs.userSecret!!,
                    mDate, mDate)

    override fun onSuccessful(response: Any) {
        paintSchedule((response as List<SubjectsItem?>))
        view.stopRefreshing()
    }

    override fun onFailureResponse() {
        view.setHeaderText("Произошла ошибка")
        view.stopRefreshing()
    }

    override fun onFailureNetwork() {
        view.setHeaderText("Проверьте подключение к интернету")
        view.stopRefreshing()
    }
}