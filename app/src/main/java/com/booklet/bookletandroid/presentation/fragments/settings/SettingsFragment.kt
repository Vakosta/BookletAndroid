package com.booklet.bookletandroid.presentation.fragments.settings

import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SwitchPreference
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.domain.Preferences
import com.booklet.bookletandroid.domain.attribute
import com.booklet.bookletandroid.domain.model.PurchaseUpdate
import com.booklet.bookletandroid.domain.model.Refresh
import com.booklet.bookletandroid.domain.model.RestartActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.noButton
import org.jetbrains.anko.support.v4.alert
import org.jetbrains.anko.support.v4.browse
import org.jetbrains.anko.yesButton

class SettingsFragment : PreferenceFragmentCompat(),
        SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreatePreferences(p0: Bundle?, p1: String?) {
        addPreferencesFromResource(R.xml.preferences)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.backgroundColor = requireActivity().attribute(R.attr.colorBackground).data

        val main = (findPreference<SwitchPreference>(this.getString(R.string.preference_notification_main)))
        val newMarks = (findPreference<SwitchPreference>(this.getString(R.string.preference_notification_new_mark)))
        if (Preferences.getInstance(requireActivity()).notificationsSubscription) {
            main?.isEnabled = true
            newMarks?.isEnabled = true
        } else {
            main?.isEnabled = false
            main?.isChecked = false
            newMarks?.isEnabled = false
            newMarks?.isChecked = false
        }

        initMarkPurposePreference()
        initDarkThemePreference()

        findPreference<Preference>("button_vk")?.setOnPreferenceClickListener {
            browse(getString(R.string.url_vk_page))
            true
        }

        findPreference<Preference>("button_exit")?.setOnPreferenceClickListener {
            alert("Вы уверены?") {
                yesButton {
                    Preferences.getInstance(requireActivity()).userLogin = ""
                    Preferences.getInstance(requireActivity()).userPassword = ""
                    Preferences.getInstance(requireActivity()).userSecret = ""
                    Preferences.getInstance(requireActivity()).userPid = 0
                    Preferences.getInstance(requireActivity()).userStudentProfileId = ""
                    Preferences.getInstance(requireActivity()).botCode = ""
                    Preferences.getInstance(requireActivity()).markPurpose = 5
                    Preferences.getInstance(requireActivity()).saturdayLessons = false
                    Preferences.getInstance(requireActivity()).notificationMain = false
                    Preferences.getInstance(requireActivity()).notificationNewMark = false
                    Preferences.getInstance(requireActivity()).notificationNews = false
                    Preferences.getInstance(requireActivity()).notificationsSubscription = false
                    Preferences.getInstance(requireActivity()).isDarkTheme = false
                    EventBus.getDefault().post(RestartActivity(false))
                }
                noButton {}
            }.show()
            true
        }

        setStudents()
    }

    private fun initMarkPurposePreference() {
        val markPurposeIcon = when (Preferences.getInstance(requireActivity()).markPurpose) {
            5 -> R.drawable.prefs_five
            4 -> R.drawable.prefs_four
            else -> R.drawable.prefs_three
        }
        findPreference<ListPreference>(getString(R.string.preference_mark_purpose))?.setIcon(markPurposeIcon)
    }

    private fun initDarkThemePreference() {
        val darkThemeIcon = when (Preferences.getInstance(requireActivity()).isDarkTheme) {
            false -> R.drawable.prefs_moon
            true -> R.drawable.prefs_sun
        }
        findPreference<SwitchPreference>(getString(R.string.preference_dark_theme))?.setIcon(darkThemeIcon)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (activity != null && isAdded) {
            when (key) {
                getString(R.string.preference_mark_purpose) -> {
                    initMarkPurposePreference()
                    EventBus.getDefault().post(Refresh())
                }
                getString(R.string.preference_saturday_lessons) ->
                    EventBus.getDefault().post(Refresh())
                getString(R.string.preference_dark_theme) -> {
                    Preferences.getInstance(requireActivity()).isShowedDarkThemePopup = true
                    EventBus.getDefault().post(RestartActivity(true))
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onStop() {
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
        EventBus.getDefault().unregister(this)
        super.onStop()
    }

    @Subscribe
    fun onRefresh(purchaseUpdate: PurchaseUpdate) {
        preferenceScreen.removeAll()
        addPreferencesFromResource(R.xml.preferences)
    }

    fun setStudents() {
        val prefs = Preferences.getInstance(requireActivity())
        val names = prefs.userStudentProfiles.map { it.name.toString() }
        val ids = prefs.userStudentProfiles.map { it.id.toString() }
        val students = findPreference<ListPreference>(requireContext().getString(R.string.preference_student_profile_id))
        students?.entries = names.toTypedArray()
        students?.setDefaultValue(prefs.userStudentProfileId)
        students?.entryValues = ids.toTypedArray()
        EventBus.getDefault().post(Refresh())
    }
}