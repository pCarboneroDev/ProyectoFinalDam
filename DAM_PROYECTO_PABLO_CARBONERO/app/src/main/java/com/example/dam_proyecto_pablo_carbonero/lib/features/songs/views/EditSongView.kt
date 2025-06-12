package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.CreateHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.DeleteModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.AddTabsModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.TransparentTextField
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.EditSongVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun EditSongView(navController: NavHostController, vm: EditSongVM = hiltViewModel()){
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val maxChars = 25

    val song by vm.selectedSong.collectAsState()
    val tuningList by vm.tuningList.collectAsState()
    val selectedTuning by vm.selectedTuning.collectAsState()
    val songName by vm.songName.collectAsState("")
    val bandName by vm.bandName.collectAsState("")
    val bmp by vm.bpm.collectAsState("")
    val key by vm.tabs.collectAsState("")

    val tabsModal by vm.tabsModal.collectAsState()
    val deleteModal by vm.deleteModal.collectAsState()
    val messageManager by vm.messageManager.collectAsState()



    LaunchedEffect(messageManager) {
        if(!messageManager.isSuccess){
            Toast.makeText(context, messageManager.message, Toast.LENGTH_SHORT).show()
            vm.resetMessageManager()
        }
    }

    Column(Modifier
        .fillMaxSize()
        .systemBarsPadding()
        .padding(horizontal = 10.dp)) {
        CreateHeader(
            title = "Edit Song",
            subtitle = "${song?.name}",
            navController = navController,
            saveMethod = {
                CoroutineScope(Dispatchers.Main).launch {
                    var saved = vm.saveSong()

                    if (saved){
                        navController.navigate("SongDetails/${song?.id}/${song?.tuningId}"){
                            popUpTo("SongTuning"){ inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            }
        )
        // Botón para seleccionar la afinación de la canción
        OutlinedButton(
            onClick = {expanded = !expanded},
            Modifier.fillMaxWidth()
        ) {
            Column {
                Row {
                    Text(text = selectedTuning?.name ?: "Select a tuning for the song")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    Modifier.background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    tuningList.forEach { tuning ->
                        DropdownMenuItem(
                            text = {
                                Text(tuning.name)
                            },
                            onClick = {
                                vm.setSelectedTuning(tuning)
                            }
                        )
                    }
                }
            }

        }


        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = songName,
            label = "Song name",
            trailingIcon = {Text("${song?.name?.length}/$maxChars")},

            onValueChange = { vm.setSongName(it) },

            isError = vm.isSongNameValid()
        )

        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bandName,
            label = "Band name",
            trailingIcon = {Text("${bandName.length}/$maxChars")},

            onValueChange = { vm.setBandName(it) },

            isError = vm.isBandNameValid()
        )


        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bmp,
            label = "BPM",

            onValueChange = { vm.setBpm(it) },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            isError = vm.isBpmValid()
        )


        // load tabs
        Button(
            modifier = Modifier.fillMaxWidth(),

            onClick = {
                vm.setTabsModal(true)
            }
        ) {
            Text("Add tabs")
        }

        if (tabsModal) {
            AddTabsModal(saveMethod = { vm.setKey(it) }, dismissFunction = { vm.setTabsModal(false) }, currentTabs = key, context)
        }


        if (deleteModal)
            DeleteModal(
                dismissFunction = { vm.setDeleteModal(false) }, onDeletePressed = {
                    CoroutineScope(Dispatchers.Main).launch {
                        vm.deleteSong()
                    }
                    navController.navigate("SongTuning"){
                        popUpTo("Tuner") { inclusive = false }
                    }
                }
            )

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                 vm.setDeleteModal(true)
            }
        ) {
            Text("Delete")
        }
    }
}
