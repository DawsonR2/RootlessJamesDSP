package me.timschneeberger.rootlessjamesdsp.fragment

import android.content.Intent
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.preference.*
import me.timschneeberger.rootlessjamesdsp.R
import me.timschneeberger.rootlessjamesdsp.preference.MaterialSeekbarPreference
import me.timschneeberger.rootlessjamesdsp.utils.Constants
import me.timschneeberger.rootlessjamesdsp.utils.ContextExtensions.sendLocalBroadcast

class SettingsAudioFormatFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        preferenceManager.sharedPreferencesName = Constants.PREF_APP
        setPreferencesFromResource(R.xml.app_audio_format_preferences, rootKey)

        findPreference<MaterialSeekbarPreference>(getString(R.string.key_audioformat_buffersize))?.setOnPreferenceChangeListener { _, newValue ->
            if((newValue as Float) <= 1024){
                Toast.makeText(requireContext(), getString(R.string.audio_format_buffer_size_warning_low_value), Toast.LENGTH_SHORT).show()
            }
            requireContext().sendLocalBroadcast(Intent(Constants.ACTION_SERVICE_HARD_REBOOT_CORE))
            true
        }
        findPreference<ListPreference>(getString(R.string.key_audioformat_encoding))?.setOnPreferenceChangeListener { _, _ ->
            requireContext().sendLocalBroadcast(Intent(Constants.ACTION_SERVICE_HARD_REBOOT_CORE))
            true
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val a = TypedValue()
        requireContext().theme.resolveAttribute(android.R.attr.windowBackground, a, true)
        if (a.isColorType) {
            view.setBackgroundColor(a.data)
        } else {
            view.background = requireContext().resources.getDrawable(a.resourceId, requireContext().theme)
        }
        return view
    }

    companion object {
        fun newInstance(): SettingsAudioFormatFragment {
            return SettingsAudioFormatFragment()
        }
    }
}