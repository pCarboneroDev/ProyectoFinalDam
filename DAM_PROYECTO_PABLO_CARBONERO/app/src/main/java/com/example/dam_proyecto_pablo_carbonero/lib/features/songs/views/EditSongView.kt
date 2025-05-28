package com.example.dam_proyecto_pablo_carbonero.lib.features.songs.views

import android.R.attr.text
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.extensions.SortOption
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.CreateHeader
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.DeleteModal
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.composables.TransparentTextField
import com.example.dam_proyecto_pablo_carbonero.lib.features.songs.viewmodels.EditSongVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun EditSongView(navController: NavHostController, vm: EditSongVM = hiltViewModel()){
    val song by vm.selectedSong.collectAsState()
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val maxChars = 25

    val tuningList by vm.tuningList.collectAsState()
    val selectedTuning by vm.selectedTuning.collectAsState()
    val songName by vm.songName.collectAsState("")
    val bandName by vm.bandName.collectAsState("")
    val bmp by vm.bpm.collectAsState("")
    val key by vm.key.collectAsState("")
    val formValid by vm.formValid.collectAsState(false)

    var modal by remember { mutableStateOf(false) }
    var tabs by remember { mutableStateOf(false) }

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
            onClick = {expanded = !expanded},
            Modifier.fillMaxWidth()
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


        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = songName,
            label = "Song name",
            trailingIcon = {Text("${song?.name?.length}/$maxChars")},

            onValueChange = { vm.setSongName(it); vm.isFormValid() },

            isError = vm.isSongNameValid()
        )

        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bandName,
            label = "Band name",
            trailingIcon = {Text("${bandName.length}/$maxChars")},

            onValueChange = { vm.setBandName(it); vm.isFormValid() },

            isError = vm.isBandNameValid()
        )


        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = bmp,
            label = "BPM",

            onValueChange = { vm.setBpm(it); vm.isFormValid() },

            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),

            isError = vm.isBpmValid()
        )

        TransparentTextField(
            modifier = Modifier.fillMaxWidth(),
            value = key,
            label = "Key",

            onValueChange = { vm.setKey(it) }
        )

        // load tabs
        Button(
            modifier = Modifier.fillMaxWidth(),
            /*colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),*/
            onClick = {
                tabs = true
            }
        ) {
            Text("Add tabs")
        }

        if (tabs) {
            AddTabsModal(dismissFunction = { tabs = false })
        }


        if (modal) DeleteModal(
            dismissFunction = {modal = false}, onDeletePressed = {
                CoroutineScope(Dispatchers.Main).launch {
                    vm.deleteSong()
                }
                navController.navigate("Tuner"){
                    popUpTo("SongTuning") { inclusive = false }
                }
            }
        )

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error
            ),
            onClick = {
                modal = true
            }
        ) {
            Text("Delete")
        }
    }
}

@Composable
fun AddTabsModal(
    /*saveMethod: (() -> Unit),*/
    dismissFunction: (() -> Unit)
){
    var tabs = ""
    val textFieldState = rememberTextFieldState()
    val clipboardManager = LocalClipboardManager.current
    AlertDialog(
        title = { Text("Add Tabs")},

        text = {
            Column(Modifier.fillMaxSize()) {
                Row(Modifier.fillMaxWidth()) {
                    Spacer(Modifier.weight(1f))
                    Button(onClick = {
                        val annotatedString = clipboardManager.getText()
                        if(isGuitarTabFormatFlexible(annotatedString.toString())){
                            tabs = annotatedString.toString()
                            textFieldState.edit {
                                append(annotatedString.toString())
                            }
                        }

                    }) { Text("Copy from clipboard") }
                }
                OutlinedTextField(
                    value = tabs,
                    onValueChange = { tabs = it },
                    textStyle = TextStyle(
                        fontFamily = FontFamily.Monospace
                    ),
                )
                BasicTextField(
                    textStyle = TextStyle(fontFamily = FontFamily.Monospace), //Resolution to the classifier 'class TextStyle : Any' is not appropriate here.
                    state = textFieldState
                )
            }
        },

        onDismissRequest = { dismissFunction() },

        confirmButton = {
            TextButton(
                onClick = {
                    dismissFunction()
                }
            ) {
                Text("ok")
            }
        }
    )
}

fun isGuitarTabFormatFlexible(text: String): Boolean {
    val tabLineRegex = Regex("^[A-Ga-g#b0-9]{1,2}\\|[-\\d~hpbx/\\\\|()*\\s]+$")

    val lines = text.lines()
    val tabLines = lines.filter { tabLineRegex.matches(it.trim()) }

    return tabLines.size >= 4
}
