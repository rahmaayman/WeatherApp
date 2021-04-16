package com.example.weatherapp.ui.fragments

import android.content.Intent
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.preference.ListPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import com.example.weatherapp.ui.activities.MainActivity
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentSettingBinding
import com.example.weatherapp.util.ContextUtils.Companion.setLocale
import com.example.weatherapp.util.Setting
class SettingFragment : PreferenceFragmentCompat() {
    private lateinit var binding: FragmentSettingBinding
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preferences, rootKey)
        preferenceManager.findPreference<Preference>("LANGUAGE_SYSTEM")!!
                .setOnPreferenceChangeListener(Preference.OnPreferenceChangeListener { preference, newValue ->
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    return@OnPreferenceChangeListener true
                })
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        val mapPreference: Preference? = findPreference("CUSTOM_LOCATION")
        mapPreference?.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (sharedPreferences.getBoolean("CUSTOM_LOCATION", true)) {
                view?.findNavController()
                        ?.navigate(R.id.action_settingsFragment_to_mapFragment)
            }
            true
        }
        val LP = findPreference("LANGUAGE_SYSTEM") as ListPreference?
        val lan = Setting.lang
        if ("en".equals(lan)) {
            setLocale(requireActivity(), "en")
            LP?.setSummary(LP?.getEntry())

        } else {
            setLocale(requireActivity(), "ar")
            LP?.setSummary(LP.getEntry())
        }
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }
    fun editSettings() {
        val LP = findPreference("LANGUAGE_SYSTEM") as ListPreference?
        val lang = Setting.lang
        if ("en".equals(lang)) {
            setLocale(requireActivity(), "en")
            LP?.setSummary(LP?.getEntry())

        } else {
            setLocale(requireActivity(), "ar")
            LP?.setSummary(LP.getEntry())
        }

        LP!!.setOnPreferenceChangeListener(androidx.preference.Preference.OnPreferenceChangeListener { prefs, obj ->
            val items = obj as String
            if (prefs.key == "LANGUAGE_SYSTEM") {
                when (items) {
                    "ar" -> {
                        setLocale(requireActivity(), "ar")
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                    "en" -> {
                        setLocale(requireActivity(), "en")
                        startActivity(Intent(requireContext(), MainActivity::class.java))
                    }
                }
                val UU = prefs as ListPreference
                UU.summary = UU.entries[UU.findIndexOfValue(items)]
            }
            true
        })
    }
}





