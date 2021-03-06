package com.booklet.bookletandroid.domain

import android.content.Context
import android.content.SharedPreferences
import androidx.preference.PreferenceManager
import com.booklet.bookletandroid.R
import com.booklet.bookletandroid.data.model.booklet.auth.Student
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Preferences private constructor(context: Context) {
    private val KEY_BOT_CODE = context.getString(R.string.preference_bot_code)
    private val KEY_INVITE_CODE = context.getString(R.string.preference_invite_code)
    private val KEY_INVITATIONS = context.getString(R.string.preference_invitations)
    private val KEY_LOGIN = context.getString(R.string.preference_login)
    private val KEY_PASSWORD = context.getString(R.string.preference_password)
    private val KEY_SECRET = context.getString(R.string.preference_secret)
    private val KEY_TOKEN_LAST_UPDATE = context.getString(R.string.preference_token_last_update)
    private val KEY_PID = context.getString(R.string.preference_pid)
    private val KEY_STUDENT_PROFILE_ID = context.getString(R.string.preference_student_profile_id)
    private val KEY_STUDENT_PROFILES = context.getString(R.string.preference_student_profiles)
    private val KEY_SATURDAY_LESSONS = context.getString(R.string.preference_saturday_lessons)
    private val KEY_MARK_PURPOSE = context.getString(R.string.preference_mark_purpose)
    private val KEY_NOTIFICATION_MAIN = context.getString(R.string.preference_notification_main)
    private val KEY_NOTIFICATION_NEW_MARK = context.getString(R.string.preference_notification_new_mark)
    private val KEY_NOTIFICATION_NEWS = context.getString(R.string.preference_notification_news)
    private val KEY_FILTER_GRADE = context.getString(R.string.preference_filter_grade)
    private val KEY_FILTER_NEW_MARKS = context.getString(R.string.preference_filter_new_marks)
    private val KEY_FILTER_TESTS = context.getString(R.string.preference_filter_tests)
    private val KEY_FILTER_HOLIDAYS = context.getString(R.string.preference_filter_holidays)
    private val KEY_BOUGHT_NOTIFICATIONS = context.getString(R.string.preference_bought_notifications)
    private val KEY_CLEARED_CACHE = "key_cache"
    private val KEY_IS_DARK_THEME = context.getString(R.string.preference_dark_theme)
    private val KEY_IS_SHOWED_DARK_THEME_POPUP = context.getString(R.string.preference_is_showed_dark_theme_popup)

    private val prefs: SharedPreferences = PreferenceManager
            .getDefaultSharedPreferences(context)

    var botCode: String
        get() = prefs.getString(KEY_BOT_CODE, "loading...")!!
        set(value) = prefs.edit().putString(KEY_BOT_CODE, value).apply()

    var inviteCode: String
        get() = prefs.getString(KEY_INVITE_CODE, "loading...")!!
        set(value) = prefs.edit().putString(KEY_INVITE_CODE, value).apply()

    var invitations: Int
        get() = prefs.getInt(KEY_INVITATIONS, 0)
        set(value) = prefs.edit().putInt(KEY_INVITATIONS, value).apply()

    var userLogin: String
        get() = prefs.getString(KEY_LOGIN, "")!!
        set(value) = prefs.edit().putString(KEY_LOGIN, value).apply()

    var userPassword: String
        get() = prefs.getString(KEY_PASSWORD, "")!!
        set(value) = prefs.edit().putString(KEY_PASSWORD, value).apply()

    var userSecret: String?
        get() = prefs.getString(KEY_SECRET, "")
        set(value) = prefs.edit().putString(KEY_SECRET, value ?: "").apply()

    var userTokenLastUpdate: Long
        get() {
            var result = prefs.getString(KEY_TOKEN_LAST_UPDATE, "0")!!
            if (result.isEmpty()) result = "0"
            return result.toLong()
        }
        set(value) = prefs.edit().putString(KEY_TOKEN_LAST_UPDATE, value.toString()).apply()

    var userPid: Long?
        get() = prefs.getLong(KEY_PID, 0)
        set(value) = prefs.edit().putLong(KEY_PID, value ?: 0).apply()

    var userStudentProfileId: String
        get() = prefs.getString(KEY_STUDENT_PROFILE_ID, "")!!
        set(value) = prefs.edit().putString(KEY_STUDENT_PROFILE_ID, value).apply()

    var userStudentProfiles: List<Student>
        get() {
            val savedString = prefs.getString(KEY_STUDENT_PROFILES, "")
            val listType = object :
                    TypeToken<List<Student>>() {}
                    .type
            return try {
                Gson().fromJson(savedString, listType)
            } catch (e: IllegalStateException) {
                listOf()
            }
        }
        set(value) = prefs.edit().putString(KEY_STUDENT_PROFILES, Gson().toJson(value)).apply()

    var saturdayLessons: Boolean
        get() = prefs.getBoolean(KEY_SATURDAY_LESSONS, false)
        set(value) = prefs.edit().putBoolean(KEY_SATURDAY_LESSONS, value).apply()

    var markPurpose: Int
        get() {
            val result = prefs.getString(KEY_MARK_PURPOSE, "5")
            return if (result == null || result == "")
                5
            else
                result.toInt()
        }
        set(value) = prefs.edit().putString(KEY_MARK_PURPOSE, value.toString()).apply()

    var notificationMain: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION_MAIN, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION_MAIN, value).apply()

    var notificationNewMark: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION_NEW_MARK, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION_NEW_MARK, value).apply()

    var notificationNews: Boolean
        get() = prefs.getBoolean(KEY_NOTIFICATION_NEWS, false)
        set(value) = prefs.edit().putBoolean(KEY_NOTIFICATION_NEWS, value).apply()

    var filterGrade: Boolean
        get() = prefs.getBoolean(KEY_FILTER_GRADE, true)
        set(value) = prefs.edit().putBoolean(KEY_FILTER_GRADE, value).apply()

    var filterNewMarks: Boolean
        get() = prefs.getBoolean(KEY_FILTER_NEW_MARKS, true)
        set(value) = prefs.edit().putBoolean(KEY_FILTER_NEW_MARKS, value).apply()

    var filterTests: Boolean
        get() = prefs.getBoolean(KEY_FILTER_TESTS, true)
        set(value) = prefs.edit().putBoolean(KEY_FILTER_TESTS, value).apply()

    var filterHolidays: Boolean
        get() = prefs.getBoolean(KEY_FILTER_HOLIDAYS, true)
        set(value) = prefs.edit().putBoolean(KEY_FILTER_HOLIDAYS, value).apply()

    var notificationsSubscription: Boolean
        get() = prefs.getBoolean(KEY_BOUGHT_NOTIFICATIONS, false)
        set(value) = prefs.edit().putBoolean(KEY_BOUGHT_NOTIFICATIONS, value).apply()

    var clearedCache: Boolean
        get() = prefs.getBoolean(KEY_CLEARED_CACHE, false)
        set(value) = prefs.edit().putBoolean(KEY_CLEARED_CACHE, value).apply()

    var isDarkTheme: Boolean
        get() = prefs.getBoolean(KEY_IS_DARK_THEME, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_DARK_THEME, value).apply()

    var isShowedDarkThemePopup: Boolean
        get() = prefs.getBoolean(KEY_IS_SHOWED_DARK_THEME_POPUP, false)
        set(value) = prefs.edit().putBoolean(KEY_IS_SHOWED_DARK_THEME_POPUP, value).apply()

    fun getWorkdayCount(): Int {
        return if (saturdayLessons)
            6
        else
            5
    }

    companion object : SingletonHolder<Preferences, Context>(::Preferences)
}