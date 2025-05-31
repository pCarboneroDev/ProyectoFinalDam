package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.CreateHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM


@Composable
fun SettingsView(navController: NavHostController, vm: SettingsVM = hiltViewModel()){
    val latinNotes by vm.latinNotes.collectAsState()

    var notations by remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = { BottomNavBar(navController) }
    )
    { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                .padding(horizontal = 10.dp),
            // = Arrangement.Center
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                HorizontalDivider(thickness = 2.dp)

                /*Text("${latinNotes}")

                val sw = Switch(
                    checked = latinNotes ?: false,
                    onCheckedChange = {
                        vm.setNotationValue(it)
                    }
                )*/

                LazyColumn(Modifier.fillMaxSize()) {
                    item {
                        SettingsRow(
                            "Notation system",
                            {
                                TextButton(
                                    onClick = { notations = !notations }
                                ) {
                                    Text(if (latinNotes) "Latin" else "English")
                                    DropdownMenu(
                                        expanded = notations,
                                        onDismissRequest = { notations = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Latin") },
                                            onClick = {
                                                vm.setNotationValue(true)
                                                notations = false
                                            }
                                        )
                                        DropdownMenuItem(
                                            text = { Text("English") },
                                            onClick = {
                                                vm.setNotationValue(false)
                                                notations = false
                                            }
                                        )
                                    }
                                }
                                /*Switch(
                                    checked = latinNotes,
                                    onCheckedChange = {
                                        vm.setNotationValue(it)
                                    }
                                )*/
                            }
                        )
                    }

                    item {
                        SettingsRow(
                            "My tunings",
                            { Icon(imageVector = Icons.Default.Tune, contentDescription = "") },
                            { navController.navigate("UserTunings") }
                        )
                    }
                }



            }
        }
    }
}

@Composable
fun SettingsRow(title: String, composable: @Composable () -> Unit, onClick: () -> Unit = {}){
    Column(Modifier.clickable(onClick = onClick)) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(title)
            Spacer(Modifier.weight(1f))
            composable()
        }
        HorizontalDivider(Modifier.padding(top = 12.dp))
    }
}
