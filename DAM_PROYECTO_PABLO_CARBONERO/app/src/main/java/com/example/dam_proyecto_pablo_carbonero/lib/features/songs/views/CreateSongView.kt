package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
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
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.AddTabsModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.TransparentTextField
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.CreateSongVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CreateSongView(navController: NavHostController, vm: CreateSongVM = hiltViewModel()){
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val maxChars = 25

    val tuningList by vm.tuningList.collectAsState()
    val selectedTuning by vm.selectedTuning.collectAsState()
    val songName by vm.songName.collectAsState("")
    val bandName by vm.bandName.collectAsState("")
    val bmp by vm.bpm.collectAsState("")
    val tabs by vm.tabs.collectAsState("")
    val tabsModal by vm.tabsModal.collectAsState()
    val messageManager by vm.messageManager.collectAsState()



    LaunchedEffect(messageManager) {
        if(!messageManager.isSuccess){
            Toast.makeText(context, messageManager.message, Toast.LENGTH_SHORT).show()
            vm.resetMessageManager()
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .systemBarsPadding()
            .padding(horizontal = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        CreateHeader(
            title = "Create new song",
            saveMethod = {
                CoroutineScope(Dispatchers.Main).launch() {
                    val result = vm.saveSong()
                    if (result == true){
                        navController.navigate("SongTuning"){
                            popUpTo("CreateSong") { inclusive = true }
                        }
                    }
                }
            },
            navController = navController
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
                    onDismissRequest = { expanded = false }
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

        Spacer(Modifier.height(10.dp))


        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = songName,
            label = "Song name",
            trailingIcon = {Text("${songName.length}/$maxChars")},
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

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = {
                vm.setTabsModal(true)
            }
        ) {
            Text("Add tabs")
        }

        if (tabsModal) {
            AddTabsModal(
                saveMethod = {
                    vm.setTabs(it)
                    vm.setTabsModal(false)
                },
                dismissFunction = { vm.setTabsModal(false) },
                currentTabs = tabs, context)
        }
    }
}



