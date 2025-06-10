package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.Manifest
import android.content.pm.PackageManager
import android.widget.GridView
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.MicOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.R
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables.TuningAnimation
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables.TuningSelector
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.TunerVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


@Composable
fun TunerView(navController: NavHostController, vm: TunerVM = hiltViewModel()){
    val tunings by vm.tunings.collectAsState();

    LaunchedEffect(Unit) {
        vm.loadTunings()
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

        //val vm = vm

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(vm: TunerVM, startingTuning: TuningWithNotesModel, navController: NavHostController){
    val mainColor = MaterialTheme.colorScheme.primary
    val activity = LocalActivity.current

    val isRecording by vm.isRecording.collectAsState(initial = false)
    val freq by vm.freqFound.collectAsState(initial = 0.0)
    val selectedNote by vm.selectedNote.collectAsState();
    val guidetext by vm.guideText.collectAsState(initial = "");
    val tunings by vm.tunings.collectAsStateWithLifecycle();
    val selectedTuning by vm.selectedTuning.collectAsState(initial = startingTuning);
    val latinNotes by vm.latinNotes.collectAsState()

    val graphValue by vm.graphValue.collectAsState()
    val colorGraph by vm.colorGraph.collectAsState()



    // composable que contiene el header para seleccionar la afinación
    TuningSelector(
        selectedTuning = selectedTuning,
        tunings = tunings,
        latinNotes = latinNotes,
        onTuningSelected = { vm.setSelectedTuning(it); vm.setSelectedNote(null) },
        navController
    )

    HorizontalDivider(thickness = 2.dp)

    Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        if(selectedNote != null)
            Text("Tuning: ${selectedNote?.englishName}", style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary))
        Spacer(Modifier.weight(1f))

        Icon(
            if(isRecording)
                Icons.Default.Mic
            else
                Icons.Default.MicOff,
            ""
        )
        Spacer(Modifier.width(5.dp))
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

    // TEXTOS DE INFORMACION
    Text(text = if (isRecording) "${freq.roundToInt()} Hz" else "", color = mainColor)

    Text(
        text = guidetext, color = mainColor
    )

    TuningAnimation(graphValue.toFloat(), Modifier, colorGraph)

    if(isRecording && selectedNote == null)
        Box(
            Modifier
                .background(
                    color = MaterialTheme.colorScheme.primaryContainer,
                    shape = RoundedCornerShape(50)
                )
                //.alpha(0.5f)
                .padding(5.dp)
        ){ Text("Select a string to star tuning!", style = TextStyle(color = MaterialTheme.colorScheme.onPrimaryContainer)) }

    TuningNotesSelector(
        selectedTuning = selectedTuning,
        onNoteSelected = { vm.setSelectedNote(it) },
        latinNotes = latinNotes,
        selectedNote = selectedNote
    )
}

@Composable
fun TuningNotesSelector(
    selectedTuning: TuningWithNotesModel?,
    onNoteSelected: (MusicNote) -> Unit,
    latinNotes: Boolean,
    selectedNote: MusicNote?
){

    Column {
        selectedTuning?.noteList?.forEachIndexed { index, note ->
            val isSelected = note == selectedNote
            val guitarString = (5 - index * 0.5).toInt()

            Row(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .clickable { onNoteSelected(note) },
                //horizontalAlignment = Alignment.CenterHorizontally
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)//.size(60.dp) // Tamaño circular fijo
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        ),
                        //.clickable { onNoteSelected(note); i = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (latinNotes == true) note.latinName else note.englishName,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        //.fillMaxWidth()
                        .weight(5f)
                        .height(guitarString.dp)
                        .background(color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSecondary)
                )
            }
        }
    }
}



