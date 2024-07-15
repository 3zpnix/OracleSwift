package com.kin.easynotes.presentation.screens.settings.settings

import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.core.os.LocaleListCompat
import androidx.navigation.NavController
import com.kin.easynotes.R
import com.kin.easynotes.presentation.navigation.NavRoutes
import com.kin.easynotes.presentation.screens.settings.SettingsScaffold
import com.kin.easynotes.presentation.screens.settings.model.SettingsViewModel
import com.kin.easynotes.presentation.screens.settings.widgets.ActionType
import com.kin.easynotes.presentation.screens.settings.widgets.ListDialog
import com.kin.easynotes.presentation.screens.settings.widgets.SettingsBox

@Composable
fun LanguageScreen(navController: NavController, settingsViewModel: SettingsViewModel) {
    SettingsScaffold(
        settingsViewModel = settingsViewModel,
        title = stringResource(id = R.string.language),
        onBackNavClicked = { navController.navigateUp() }
    ) {
        LazyColumn {
            item {
                SettingsBox(
                    title = stringResource(id = R.string.language),
                    description = stringResource(id = R.string.language_description),
                    icon = Icons.Rounded.Translate,
                    radius = shapeManager(radius = settingsViewModel.settings.value.cornerRadius, isBoth = true),
                    actionType = ActionType.CUSTOM,
                    customAction = { onExit -> OnLanguageClicked(settingsViewModel) { onExit() }
                    }
                )
            }
        }
    }
}

@Composable
private fun OnLanguageClicked(settingsViewModel: SettingsViewModel, onExit: () -> Unit) {
    val context = LocalContext.current
    val languages = settingsViewModel.getSupportedLanguages(context).toList()
    val systemLanguage = context.getString(R.string.system_language)
    ListDialog(
        text = stringResource(R.string.language),
        list = languages,
        settingsViewModel = settingsViewModel,
        onExit = onExit,
        extractDisplayData = { it },
        initialItem = Pair(systemLanguage, second = ""),
        setting = { isFirstItem, isLastItem, displayData ->
            SettingsBox(
                isBig = false,
                title = displayData.first,
                radius = shapeManager(isFirst = isFirstItem, isLast = isLastItem, radius = settingsViewModel.settings.value.cornerRadius),
                actionType = ActionType.CHECKBOX,
                variable = if (displayData.first != systemLanguage) {
                    AppCompatDelegate.getApplicationLocales()[0]?.language == displayData.second
                } else {
                    AppCompatDelegate.getApplicationLocales().isEmpty
                },
                switchEnabled = { if (displayData.first != systemLanguage) {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(displayData.second))
                    } else {
                    AppCompatDelegate.setApplicationLocales(LocaleListCompat.getEmptyLocaleList())
                    }
                }
            )
        }
    )
}