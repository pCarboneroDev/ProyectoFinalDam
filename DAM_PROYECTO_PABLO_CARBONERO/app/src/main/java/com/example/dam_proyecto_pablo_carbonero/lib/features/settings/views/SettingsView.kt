package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Input
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.CloudDone
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Input
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.CreateHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM


@Composable
fun SettingsView(navController: NavHostController, vm: SettingsVM = hiltViewModel()){
    val latinNotes by vm.latinNotes.collectAsState()
    val loggedIn by vm.loggedIn.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    var notations by remember { mutableStateOf(false) }


    if (isLoading) {
        // Capa de carga encima
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .zIndex(1f), // se asegura de que esté por encima
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }

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
        ) {
            Column(
                Modifier.fillMaxSize()
            ) {
                Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
                Spacer(Modifier.height(3.dp))
                HorizontalDivider(thickness = 2.dp)

                LazyColumn(Modifier.fillMaxSize()) {
                    if(!loggedIn)
                        item {
                            Box(
                                Modifier
                                    .fillMaxWidth()
                                    .padding(10.dp)
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(12.dp)
                                    )
                            ){
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Button(
                                        onClick = {
                                            navController.navigate("Register")
                                        },
                                        colors = ButtonDefaults.buttonColors(
                                            MaterialTheme.colorScheme.secondaryContainer
                                        )
                                    ) { Text("Register", style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)) }

                                    Column {
                                        Text("Create a free account", style = TextStyle(color = MaterialTheme.colorScheme.onPrimary))
                                        Text("For saving all your data and recover it in a different device.", style = TextStyle(color = MaterialTheme.colorScheme.onPrimary))
                                    }
                                }
                            }
                        }

                    item {
                        Text("General", style = TextStyle(fontSize = 35.sp))
                    }

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

                    item {
                        Text("Account", style = TextStyle(fontSize = 35.sp))
                    }
                    if(loggedIn){
                        item {
                            SettingsRow(
                                "SaveData",
                                { Icon(imageVector = Icons.Default.CloudUpload, contentDescription = "") },
                                { vm.subirDatos() }
                            )
                        }

                        item {
                            SettingsRow(
                                "Load data",
                                { Icon(imageVector = Icons.Default.CloudDownload, contentDescription = "") },
                                { vm.downloadData() }
                            )
                        }

                        item {
                            SettingsRow(
                                "Delete data",
                                { Icon(imageVector = Icons.Default.CloudOff, contentDescription = "") },
                                {  }
                            )
                        }


                        item {
                            Button(
                                onClick = { vm.logOut() },
                                Modifier.fillMaxSize(),
                            ) { Text("Logout") }
                        }
                    }
                    else{
                        item {
                            SettingsRow(
                                "Login",
                                { Icon(imageVector = Icons.AutoMirrored.Default.Login, contentDescription = "") },
                                { navController.navigate("Login") }
                            )
                        }
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
