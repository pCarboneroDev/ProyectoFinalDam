package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.TunerVM
//import com.example.dam_proyecto_pablo_carbonero.lib.data.local.repositories_impl.UserPreferencesRepositoryImpl
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.net.URLEncoder


@Composable
fun TunerView(navController: NavHostController, vm: TunerVM = hiltViewModel()){
    val tunings by vm.tunings.collectAsState();

    LaunchedEffect(Unit) {
        vm.loadPreferences()
    }


    if (tunings.isEmpty()){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    }
    else{
        val selectedTuning by vm.selectedTuning.collectAsState();

        if (selectedTuning == null) {
            vm.setSelectedTuning(tunings.find { it.tuning.id == 1.toLong() } ?: tunings[0])
            // todo poner objeto vacío o algo si no se cumple
        }

        val vm = vm

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            bottomBar = { BottomNavBar(navController) }
        )
        { innerPadding ->
            Column(
                Modifier.fillMaxSize()
                    .padding(innerPadding).systemBarsPadding(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                if (selectedTuning == null){
                    CircularProgressIndicator()
                }
                else{
                    MainContent(vm = vm, startingTuning = selectedTuning!!, navController)
                }
            }
        }
    }
}

@Composable
fun MainContent(vm: TunerVM, startingTuning: TuningWithNotesModel, navController: NavHostController){
    val mainColor = MaterialTheme.colorScheme.primary
    var expanded by remember { mutableStateOf(false) }

    var i by remember { mutableStateOf(-1) }

    val activity = LocalActivity.current

    val isRecording by vm.isRecording.collectAsState(initial = false)
    val freq by vm.freqFound.collectAsState(initial = 0.0)
    //val noteList by vm.noteList.observeAsState()
    val selectedNote by vm.selectedNote.collectAsState();
    val guidetext by vm.guideText.collectAsState(initial = "");
    val tunings by vm.tunings.collectAsStateWithLifecycle();
    val selectedTuning by vm.selectedTuning.collectAsState(initial = startingTuning);
    val latinNotes by vm.latinNotes.collectAsState()


    Row(Modifier.fillMaxWidth()) {
        Box(
            Modifier
                .height(60.dp)
                .width(270.dp)
                .clickable(
                    onClick = {expanded = !expanded}
                ),
            contentAlignment = Alignment.Center,
        ){
            Row(Modifier.fillMaxSize(), horizontalArrangement = Arrangement.Center) {
                Column() {
                    Text(selectedTuning?.tuning!!.name, fontSize = 20.sp, fontWeight = FontWeight.Bold)
                    Row() {
                        selectedTuning?.noteList?.forEach { note ->
                            Text(text = if (latinNotes == true) note.latinName + " " else note.englishName + " ",
                                fontSize = 15.sp, color = MaterialTheme.colorScheme.onBackground)
                        }
                    }
                }
                Icon(Icons.Default.ArrowDropDown, contentDescription = "More options", tint = MaterialTheme.colorScheme.primary)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                Modifier.background(color = MaterialTheme.colorScheme.secondary)
            ) {
                tunings?.forEach { tuning ->
                    DropdownMenuItem(
                        text = {
                            Text(tuning.tuning.name)
                        },
                        onClick = { vm.setSelectedTuning(tuning) }
                    )
                }
            }
        }

        if(selectedTuning?.tuning?.name != "Standard Tuning"){
            IconButton(onClick = {
                //val serializedTuning = URLEncoder.encode(Gson().toJson(selectedTuningId), "UTF-8")
                navController.navigate("EditTuning/${selectedTuning!!.tuning.id}")
            }) {
                Icon(Icons.Default.Create, contentDescription = "edit tunning", tint = Color.LightGray)
            }
        }


        IconButton(onClick = {
            navController.navigate("CreateTuning")
        }) {
            Icon(Icons.Default.Add, contentDescription = "add tunning", tint = Color.LightGray)
        }
    }

    HorizontalDivider()


    Row() {
        selectedTuning?.noteList?.forEachIndexed { index, note ->
            val isSelected = index == i

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .background(
                        color = if (isSelected) mainColor else MaterialTheme.colorScheme.secondary,
                        shape = CircleShape
                    )
                    .clickable() { vm.setSelectedNote(note); i = index }
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Text(
                    text = if (latinNotes == true) note.latinName else note.englishName,
                    color = MaterialTheme.colorScheme.onSecondary
                )
            }
        }
    }

    Text(
        text = if (selectedNote != null)
            if (latinNotes == true) selectedNote!!.latinName else selectedNote!!.englishName
        else "",
        color = mainColor
    )

    // TEXTOS DE INFORMACION
    Text(text = "Frequency: $freq Hz", color = mainColor)


    Text(
        text = guidetext, color = mainColor
    )

    // Switch

    Switch(
        checked = isRecording,
        onCheckedChange = {
            activity?.let {
                if (ActivityCompat.checkSelfPermission(it, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                    vm.initializeAudioRecord()
                    if (!isRecording) {
                        CoroutineScope(Dispatchers.Main).launch {
                            vm.startRecordingAudio()
                        }
                    }
                    vm.setIsRecording(!isRecording)
                } else {
                    // Solicitar permisos si no se tienen
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.RECORD_AUDIO), 1)
                }
            }
        }
    )
}
