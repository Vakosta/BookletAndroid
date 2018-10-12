package me.annenkov.julistaandroid.presentation.activities.main

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentStatePagerAdapter
import me.annenkov.julistaandroid.presentation.fragments.account.AccountFragment
import me.annenkov.julistaandroid.presentation.fragments.marks.MarksFragment
import me.annenkov.julistaandroid.presentation.fragments.plus.PlusFragment
import me.annenkov.julistaandroid.presentation.fragments.schedule.ScheduleFragment
import me.annenkov.julistaandroid.presentation.fragments.settings.SettingsFragment

class MainPagerAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> AccountFragment()
            1 -> MarksFragment()
            2 -> ScheduleFragment()
            3 -> PlusFragment()
            else -> SettingsFragment()
        }
    }

    override fun getItemPosition(`object`: Any): Int {
        return FragmentStatePagerAdapter.POSITION_NONE
    }

    override fun getCount(): Int = 5
}