package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM
//import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM
//import com.example.dam_proyecto_pablo_carbonero.lib.repositories.repositories_impl.UserPreferencesRepositoryImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Composable
fun SettingsView(navController: NavHostController, vm: SettingsVM = hiltViewModel()){
    val latinNotes by vm.latinNotes.observeAsState()



    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    )
    { innerPadding ->
        Column(
            Modifier.fillMaxSize()
                .padding(innerPadding).systemBarsPadding(),
            horizontalAlignment = Alignment.CenterHorizontally,
            // = Arrangement.Center
        ) {
            Column(
                Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("SETTINGS")

                Text("${latinNotes}")

                Switch(
                    checked = latinNotes ?: false,
                    onCheckedChange = {
                        vm.setNotationValue(it)
                    }
                )
            }
        }
    }
}