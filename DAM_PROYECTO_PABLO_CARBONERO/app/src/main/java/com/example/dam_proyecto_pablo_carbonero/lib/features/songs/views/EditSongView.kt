package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.systemGesturesPadding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewsmodels.EditSongVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun EditSongView(navController: NavHostController, vm: EditSongVM = hiltViewModel()){
    val song by vm.selectedSong.observeAsState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val maxChars = 25

    val tuningList by vm.tuningList.observeAsState()
    val selectedTuning by vm.selectedTuning.observeAsState()
    val songName by vm.songName.observeAsState("")
    val bandName by vm.bandName.observeAsState("")
    val bmp by vm.bpm.observeAsState("")
    val key by vm.key.observeAsState("")
    val formValid by vm.formValid.observeAsState(false)


    Column(Modifier.fillMaxSize().systemBarsPadding().padding(horizontal = 10.dp)) {
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
                    else{
                        withContext(Dispatchers.Main) {
                            Toast.makeText(context, "Error saving song", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        )
        // Botón para seleccionar la afinación de la canción
        OutlinedButton(
            onClick = {expanded = !expanded}
        ) {
            Column {
                Row() {
                    Text(text = selectedTuning?.name ?: "Select a tuning for the song")
                    Icon(Icons.Default.ArrowDropDown, contentDescription = "")
                }


                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    Modifier.background(color = MaterialTheme.colorScheme.secondary)
                ) {
                    tuningList?.forEach { tuning ->
                        DropdownMenuItem(
                            text = {
                                Text(tuning.name)
                            },
                            onClick = {
                                vm.setSelectedTuning(tuning)
                                vm.isFormValid()
                            }
                        )
                    }
                }
            }

        }


        OutlinedTextField(
            value = songName,
            label = {Text("Song name")},
            trailingIcon = {Text("${song?.name?.length}/$maxChars")},

            onValueChange = { vm.setSongName(it); vm.isFormValid() },

            isError = vm.isSongNameValid()
        )

        OutlinedTextField(
            value = bandName,
            label = {Text("Band name")},
            trailingIcon = {Text("${bandName.length}/$maxChars")},

            onValueChange = { vm.setBandName(it); vm.isFormValid() },

            isError = vm.isBandNameValid()
        )


        OutlinedTextField(
            value = bmp,
            label = {Text("BPM")},

            onValueChange = { vm.setBpm(it); vm.isFormValid() },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            isError = vm.isBpmValid()
        )

        OutlinedTextField(
            value = key,
            label = {Text("Key")},

            onValueChange = { vm.setKey(it) }
        )
    }
}