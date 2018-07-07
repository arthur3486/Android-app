package com.stocksexchange.android.utils.handlers

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager

/**
 * A helper class used for providing preferences functionality.
 */
class PreferenceHandler(context: Context) {


    companion object {

        private const val KEY_PUBLIC_KEY = "public_key"
        private const val KEY_SECRET_KEY = "secret_key"
        private const val KEY_USER_NAME = "user_name"

    }


    private val sharedPreferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)




    /**
     * Saves a public authentication key.
     *
     * @param key The key to save
     */
    fun savePublicAuthKey(key: String) {
        put(KEY_PUBLIC_KEY, key)
    }


    /**
     * Gets a public authentication key.
     *
     * @return The public authentication key
     */
    fun getPublicAuthKey(): String {
        return get(KEY_PUBLIC_KEY, "")
    }


    /**
     * Removes a public authentication key.
     */
    fun removePublicAuthKey() {
        remove(KEY_PUBLIC_KEY)
    }


    /**
     * Saves a secret authentication key.
     *
     * @param key The key to save
     */
    fun saveSecretAuthKey(key: String) {
        put(KEY_SECRET_KEY, key)
    }


    /**
     * Gets a secret authentication key.
     *
     * @return The secret authentication key
     */
    fun getSecretAuthKey(): String {
        return get(KEY_SECRET_KEY, "")
    }


    /**
     * Removes a secret authentication key.
     */
    fun removeSecretAuthKey() {
        remove(KEY_SECRET_KEY)
    }


    /**
     * Saves a user name.
     *
     * @param userName The user name to save
     */
    fun saveUserName(userName: String) {
        put(KEY_USER_NAME, userName)
    }


    /**
     * Gets a user name.
     *
     * @return The user name
     */
    fun getUserName(): String {
        return get(KEY_USER_NAME, "")
    }


    /**
     * Removes a user name.
     */
    fun removeUserName() {
        remove(KEY_USER_NAME)
    }


    fun put(key: String, value: Any) {
        put(key to value)
    }


    /**
     * Puts pairs of data inside shared preferences.
     *
     * @param pairs The pairs of data to put
     */
    fun put(vararg pairs: Pair<String, *>) {
        sharedPreferences.edit {
            pairs.forEach { put(it) }
        }
    }


    private fun SharedPreferences.Editor.put(pair: Pair<String, *>) {
        val (key, data) = pair

        when (data) {
            is Boolean -> putBoolean(key, data)
            is Int -> putInt(key, data)
            is Long -> putLong(key, data)
            is Float -> putFloat(key, data)
            is String -> putString(key, data)

            else -> throw IllegalArgumentException("Class ${data?.let { it::class }} is not supported")
        }
    }


    /**
     * Removes data from the preferences specified by the passed in keys.
     *
     * @param keys The keys of data to remove
     */
    fun remove(vararg keys: String) {
        sharedPreferences.edit {
            keys.forEach { remove(it) }
        }
    }


    /**
     * Gets data from the shared preferences.
     *
     * @param key The key to get the data for
     * @param defaultValue The default value to return
     * if the key is absent in the preferences
     */
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> get(key: String, defaultValue: T) : T {
        return with(sharedPreferences) {
            when(defaultValue) {
                is Int -> getInt(key, defaultValue) as T
                is Long -> getLong(key, defaultValue) as T
                is Float -> getFloat(key, defaultValue) as T
                is String -> getString(key, defaultValue) as T
                is Boolean -> getBoolean(key, defaultValue) as T

                else -> throw IllegalArgumentException("Class ${defaultValue.let { it::class }} is not supported")
            }
        }
    }


    @SuppressLint("ApplySharedPref")
    private fun SharedPreferences.edit(editAction: SharedPreferences.Editor.() -> Unit) {
        val editor = edit()
        editor.editAction()
        editor.commit()
    }


}