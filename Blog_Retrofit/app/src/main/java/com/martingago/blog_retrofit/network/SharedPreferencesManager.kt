package com.martingago.blog_retrofit.network

import android.content.Context

class SharedPreferencesManager(context: Context) {
    private val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

    fun saveUserCredentials(id:Long, username: String, password: String,name: String, roles: List<String>) {
        sharedPreferences.edit()
            .putLong("ID", id)
            .putString("USERNAME", username)
            .putString("PASSWORD", password)
            .putString("NAME", name)
            .putStringSet("ROLES", roles.toSet())
            .apply()
    }

    fun getId(): Long {
        return sharedPreferences.getLong("ID", -1L)
    }

    fun saveLoginState(isLoggedIn: Boolean) {
        sharedPreferences.edit()
            .putBoolean("IS_LOGGED_IN", isLoggedIn)
            .apply()
    }

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean("IS_LOGGED_IN", false) // Default is false
    }

    fun getUsername(): String? {
        return sharedPreferences.getString("USERNAME", null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString("PASSWORD", null)
    }

    fun updatePassword(password: String) {
        sharedPreferences.edit()
            .putString("PASSWORD", password)
            .apply()
    }

    fun getName(): String? {
        return sharedPreferences.getString("NAME", null)
    }

    fun updateName(name: String) {
        sharedPreferences.edit()
            .putString("NAME", name)
            .apply()
    }

    fun getRoles(): List<String> {
        return sharedPreferences.getStringSet("ROLES", emptySet())?.toList() ?: emptyList()
    }

    fun clearCredentials() {
        sharedPreferences.edit().clear().apply()
    }

    fun saveLanguagePreference(language: String) {
        sharedPreferences.edit()
            .putString("LANGUAGE", language)
            .apply()
    }

    fun getLanguagePreference(): String {
        return sharedPreferences.getString("LANGUAGE", "default") ?: "default"
    }

    fun saveThemePreference(theme: String) {
        sharedPreferences.edit()
            .putString("THEME", theme)
            .apply()
    }

    fun getThemePreference(): String {
        return sharedPreferences.getString("THEME", "default") ?: "default"
    }


}
