package am.andranik.inc.newsrss.presenter.view.fragment

import am.andranik.inc.newsrss.R
import am.andranik.inc.newsrss.presenter.viewmodel.SettingsViewModel
import am.andranik.inc.newsrss.util.NewsLanguage
import am.andranik.inc.newsrss.util.SyncInterval
import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : Fragment() {

    private val settingsViewModel: SettingsViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSettings()
        initListeners()
    }

    private fun initListeners() {
        ll_language_selection.setOnClickListener { openLanguageChooser() }
        ll_interval_selection.setOnClickListener { openIntervalChooser() }
    }

    private fun openIntervalChooser() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.settings_interval_title))

        val intervalList = SyncInterval.values().toList()

        val intervalsAdapter = object :
            ArrayAdapter<SyncInterval>(
                requireContext(),
                R.layout.list_item_single_choice,
                intervalList
            ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val title = view.findViewById(android.R.id.title) as TextView
                title.setText(intervalList[position].title)
                return view
            }
        }

        val selectedSyncInterval = settingsViewModel.interval.value
        val defaultCheckedItem =
            if (selectedSyncInterval == null) {
                0
            } else {
                intervalList.indexOfFirst { it.milis == selectedSyncInterval }
            }

        builder.setSingleChoiceItems(
            intervalsAdapter,
            defaultCheckedItem
        ) { dialog, pos ->
            val selectedInterval = intervalsAdapter.getItem(pos);
            selectedInterval?.milis?.let { settingsViewModel.setInterval(it) }
            dialog.dismiss()
        }

        builder.setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }
        val languageSelectionDialog: AlertDialog = builder.create()
        languageSelectionDialog.show()
    }

    private fun openLanguageChooser() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        builder.setTitle(getString(R.string.settings_language_title))

        val languagesList = NewsLanguage.values().toList()

        val languageAdapter = object :
            ArrayAdapter<NewsLanguage>(
                requireContext(),
                R.layout.list_item_single_choice,
                languagesList
            ) {
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = super.getView(position, convertView, parent)
                val title = view.findViewById(android.R.id.title) as TextView
                title.setText(languagesList[position].title)
                return view
            }
        }

        val selectedLanguage = settingsViewModel.language.value
        val defaultCheckedItem =
            if (selectedLanguage == null) {
                0
            } else {
                languagesList.indexOfFirst { it.key == selectedLanguage }
            }

        builder.setSingleChoiceItems(
            languageAdapter,
            defaultCheckedItem
        ) { dialog, pos ->
            val selectedNewsLanguage = languageAdapter.getItem(pos);
            selectedNewsLanguage?.let { settingsViewModel.setLanguage(selectedNewsLanguage) }
            dialog.dismiss()
        }
        builder.setNeutralButton(getString(R.string.cancel)) { dialog, _ -> dialog.dismiss() }

        val languageSelectionDialog: AlertDialog = builder.create()
        languageSelectionDialog.show()
    }

    private fun changeSyncUIState(enabled: Boolean) {
        ll_interval_selection.isEnabled = enabled

        context?.let {
            if (enabled) {
                tv_interval_title.setTextColor(ContextCompat.getColor(it, R.color.black_text_color))
            } else {
                tv_interval_title.setTextColor(ContextCompat.getColor(it, R.color.grey_text_color))
            }
        }
    }

    private fun loadSettings() {
        sc_interval_enable.setOnCheckedChangeListener { _, checked ->
            settingsViewModel.setSyncEnabled(checked)
            changeSyncUIState(checked)
        }
        val isSyncEnabled = settingsViewModel.isSyncEnabled
        sc_interval_enable.isChecked = isSyncEnabled
        changeSyncUIState(isSyncEnabled)

        settingsViewModel.language.observe(viewLifecycleOwner, Observer { language ->
            language?.let {
                val selectedLang = NewsLanguage.values().toList().find { it.key == language }
                selectedLang?.let {
                    tv_language_value.setText(it.title)
                }
            }
        })
        settingsViewModel.interval.observe(viewLifecycleOwner, Observer { interval ->
            interval?.let {
                val selectedInterval = SyncInterval.values().toList().find { it.milis == interval }
                selectedInterval?.let {
                    tv_interval_value.setText(it.title)
                }
            }
        })
    }
}
