package com.example.dam_proyecto_pablo_carbonero.lib.features.settings.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.CloudOff
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.composables.BackupWarningModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.composables.DeleteAccountModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.settings.viewsmodels.SettingsVM
import kotlinx.coroutines.launch


@Composable
fun SettingsView(navController: NavHostController, vm: SettingsVM = hiltViewModel()) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val latinNotes by vm.latinNotes.collectAsState()
    val loggedIn by vm.loggedIn.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val localDate by vm.localDate.collectAsState()
    val cloudDate by vm.cloudDate.collectAsState()
    var notations by remember { mutableStateOf(false) }
    val saveModal by vm.saveModal.collectAsState()
    val loadModal by vm.loadModal.collectAsState()
    val deleteModal by vm.deleteModal.collectAsState()
    val deleteAccountModal by vm.deleteAccountModal.collectAsState()
    val message by vm.messageManager.collectAsState()


    LaunchedEffect(message) {
        if (message.isSuccess == false){
            Toast.makeText(context, message.message, Toast.LENGTH_SHORT).show()
            vm.resetMessageManager()
        }
    }

    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .zIndex(1f),
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
            Text("Settings", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(3.dp))
            HorizontalDivider(thickness = 2.dp)

            LazyColumn(Modifier.fillMaxSize()) {
                if (!loggedIn)
                    item {
                        Box(
                            Modifier
                                .fillMaxWidth()
                                .padding(10.dp)
                                .background(
                                    color = MaterialTheme.colorScheme.primary,
                                    shape = RoundedCornerShape(12.dp)
                                )
                        ) {
                            Row(
                                Modifier.padding(5.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        navController.navigate("Register")
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        MaterialTheme.colorScheme.secondaryContainer
                                    )
                                ) {
                                    Text(
                                        "Register",
                                        style = TextStyle(color = MaterialTheme.colorScheme.onSecondaryContainer)
                                    )
                                }

                                Spacer(Modifier.width(3.dp))

                                Column {
                                    Text(
                                        "Create a free account",
                                        style = TextStyle(
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            fontWeight = FontWeight.Bold
                                        )
                                    )
                                    Text(
                                        "Save your data and recover it in a different device.",
                                        style = TextStyle(color = MaterialTheme.colorScheme.onPrimary)
                                    )
                                }
                            }
                        }
                    }

                item {
                    SettingsSection(
                        "General"
                    ) {
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
                        SettingsRow(
                            "My tunings",
                            { Icon(imageVector = Icons.Default.Tune, contentDescription = "") },
                            { navController.navigate("UserTunings") }
                        )
                    }
                }


                if(loggedIn)
                    item {
                        SettingsSection("Cloud data") {

                            SettingsRow(
                                "Save data",
                                {
                                    Icon(
                                        imageVector = Icons.Default.CloudUpload,
                                        contentDescription = ""
                                    )
                                },
                                {
                                    coroutineScope.launch {
                                        vm.loadBackUpInfo()
                                        vm.setSaveModal(true)
                                    }
                                }
                            )

                            SettingsRow(
                                "Load data",
                                {
                                    Icon(
                                        imageVector = Icons.Default.CloudDownload,
                                        contentDescription = ""
                                    )
                                },
                                {
                                    coroutineScope.launch {
                                        vm.loadBackUpInfo()
                                        vm.setLoadModal(true)
                                    }
                                }
                            )

                            SettingsRow(
                                "Delete data",
                                { Icon(imageVector = Icons.Default.CloudOff, contentDescription = "") },
                                {
                                    coroutineScope.launch {
                                        vm.loadBackUpInfo()
                                        vm.setDeleteModal(true)
                                    }
                                }
                            )
                        }
                    }


                item {
                    SettingsSection("User & Account") {
                        if (loggedIn){
                            SettingsRow(
                                title = "Delete Account",
                                composable = { Icons.Default.Delete },
                                onClick = {
                                    vm.setDeleteAccountModal(true)
                                }
                            )

                            Button(
                                onClick = { vm.logOut() },
                                Modifier.fillMaxWidth(),
                            ) { Text("Logout") }
                        }
                        else{
                            SettingsRow(
                                "Login",
                                {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.Login,
                                        contentDescription = ""
                                    )
                                },
                                { navController.navigate("Login") }
                            )
                        }

                    }
                }
            }

            if (saveModal)
                BackupWarningModal(
                    "Save data",
                    "Uploading your current data will overwrite the information in the cloud. Are you sure you want to confirm this action?",
                    localDate,
                    cloudDate,
                    Icons.Default.CloudUpload,
                    onSubmit = {
                        coroutineScope.launch {
                            val result = vm.uploadData()
                            if (!result)
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()

                            vm.setSaveModal(false)
                        }
                    },
                    onDismiss = { vm.setSaveModal(false) }
                )
            if (loadModal)
                BackupWarningModal(
                    "Download from the cloud",
                    "Downloading the data will overwrite the information in this device. This action is irreversible, are you sure you want to confirm this action?",
                    localDate,
                    cloudDate,
                    Icons.Default.CloudDownload,
                    onSubmit = {
                        coroutineScope.launch {
                            val result = vm.downloadData()
                            if(result)
                                vm.setLoadModal(false)
                        }
                    },
                    onDismiss = { vm.setLoadModal(false) }
                )
            if (deleteModal)
                BackupWarningModal(
                    "Delete data",
                    "Deleting your uploaded data is irreversible, are you sure you want to continue?",
                    localDate,
                    cloudDate,
                    Icons.Default.CloudOff,
                    onSubmit = {
                        coroutineScope.launch {
                            val result = vm.deleteData()
                            if(result)
                                vm.setDeleteModal(false)
                        }
                    },
                    onDismiss = { vm.setDeleteModal(false) }
                )

            if(deleteAccountModal)
                DeleteAccountModal(
                    title = "Delete Account",
                    body = "You cannot undo this action",
                    onDismiss = { vm.setDeleteAccountModal(false) },
                    onSubmit = {
                        coroutineScope.launch {
                            val result = vm.deleteAccount(it)
                            if (result){
                                vm.setDeleteAccountModal(false)
                                navController.navigate("Load"){
                                    popUpTo("Tuner"){ inclusive = true }
                                }
                            }
                        }
                    }
                )

        }
    }
}

@Composable
fun SettingsSection(title: String, content: @Composable () -> Unit) {
    Card(
        Modifier.padding(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )
    ) {
        Column(Modifier.padding(15.dp)) {
            Text(title, style = TextStyle(fontSize = 17.sp, fontWeight = FontWeight.Bold))
            content()
        }
    }
}

@Composable
fun SettingsRow(
    title: String,
    composable: @Composable () -> Unit,
    onClick: () -> Unit = {},
    noDivider: Boolean = false
) {
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
        if (!noDivider)
            HorizontalDivider(
                Modifier
                    .padding(top = 12.dp)
                    .alpha(0.1f),
                color = MaterialTheme.colorScheme.onSecondaryContainer
            )
    }
}
