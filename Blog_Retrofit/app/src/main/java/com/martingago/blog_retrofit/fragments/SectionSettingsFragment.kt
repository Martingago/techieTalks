package com.martingago.blog_retrofit.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import com.martingago.blog_retrofit.MainActivity
import com.martingago.blog_retrofit.R
import com.martingago.blog_retrofit.network.SharedPreferencesManager
import java.util.*

class SectionSettingsFragment : Fragment() {

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private lateinit var languageSpinner: Spinner
    private lateinit var themeSpinner: Spinner
    private lateinit var saveButton: Button

    private var selectedLanguage: String? = null
    private var selectedTheme: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_section_settings, container, false)

        languageSpinner = view.findViewById(R.id.language_spinner)
        themeSpinner = view.findViewById(R.id.theme_spinner)
        saveButton = view.findViewById(R.id.save_button)

        setupLanguageSpinner()
        setupThemeSpinner()

        saveButton.setOnClickListener {
            applyPreferences()
        }

        return view
    }

    private fun setupLanguageSpinner() {
        val languages = resources.getStringArray(R.array.language_options)
        languageSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            languages
        )
        val currentLanguage = sharedPreferencesManager.getLanguagePreference()
        languageSpinner.setSelection(languages.indexOf(currentLanguage))

        // Especificar tipos explícitamente en el listener
        languageSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedLanguage = languages[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona un ítem
            }
        }
    }

    private fun setupThemeSpinner() {
        val themes = resources.getStringArray(R.array.theme_options)
        themeSpinner.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_dropdown_item,
            themes
        )
        val currentTheme = sharedPreferencesManager.getThemePreference()

        // Buscar el índice del tema actual, considerando variantes
        val themeIndex = themes.indexOfFirst { it == currentTheme ||
                (currentTheme == "Light" && it == "Modo claro") ||
                (currentTheme == "Dark" && it == "Modo oscuro") ||
                (currentTheme == "Device Default" && it == "Predeterminado del dispositivo")
        }

        if (themeIndex != -1) {
            themeSpinner.setSelection(themeIndex)
        }

        themeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedTheme = themes[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No hacer nada si no se selecciona un ítem
            }
        }
    }


    private fun applyPreferences() {
        // Guardar preferencias en SharedPreferences
        selectedLanguage?.let {
            sharedPreferencesManager.saveLanguagePreference(it)
            updateLanguage(it)
        }
        selectedTheme?.let {
            sharedPreferencesManager.saveThemePreference(it)
            updateTheme(it)
        }

        // Reiniciar la actividad para aplicar cambios
        (activity as? MainActivity)?.restartActivity()
    }

    private fun updateLanguage(language: String) {
        val localeCode = when (language) {
            "English" -> "en"
            "Español" -> "es"
            else -> Locale.getDefault().language
        }
        val locale = Locale(localeCode)
        Locale.setDefault(locale)
        val config = Configuration(requireContext().resources.configuration)
        Log.i("opt", "idioma seleccionado: " + locale.toString())
        config.setLocale(locale)
        requireContext().resources.updateConfiguration(config, requireContext().resources.displayMetrics)
    }

    private fun updateTheme(theme: String) {
        when (theme) {
            "Light", "Modo claro" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                activity?.recreate()
            }
            "Dark", "Modo oscuro" -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                activity?.recreate()}
            else -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                activity?.recreate()
            }
        }
    }
}
