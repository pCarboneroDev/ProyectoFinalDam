package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables.TuningAnimation
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.composables.TuningSelector
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.TunerVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


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
                    .padding(innerPadding)
                    .padding(top = WindowInsets.systemBars.asPaddingValues().calculateTopPadding())
                    .padding(horizontal = 12.dp),
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

    HorizontalDivider()

    // TEXTOS DE INFORMACION
    Text(text = if (isRecording) "$freq Hz" else "", color = mainColor)

    Text(
        text = guidetext, color = mainColor
    )

    TuningAnimation(graphValue.toFloat(), Modifier, colorGraph)

    Text(
        text = if (selectedNote != null)
            if (latinNotes == true) selectedNote!!.latinName else selectedNote!!.englishName
        else "",
        color = mainColor
    )

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

    TuningNotesSelector(
        selectedTuning = selectedTuning,
        onNoteSelected = { vm.setSelectedNote(it) },
        latinNotes = latinNotes
    )
}

@Composable
fun TuningNotesSelector(
    selectedTuning: TuningWithNotesModel?,
    onNoteSelected: (MusicNote) -> Unit,
    latinNotes: Boolean
){
    var i by remember { mutableStateOf(-1) }
    Row(Modifier.fillMaxWidth()) {
        selectedTuning?.noteList?.forEachIndexed { index, note ->
            val isSelected = index == i
            val cuerda = (5 - index * 0.5).toInt()

            Column(
                modifier = Modifier
                    .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)//.size(60.dp) // Tamaño circular fijo
                        .background(
                            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary,
                            shape = CircleShape
                        )
                        .clickable { onNoteSelected(note); i = index },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (latinNotes == true) note.latinName else note.englishName,
                        color = MaterialTheme.colorScheme.onSecondary
                    )
                }
                Box(
                    modifier = Modifier
                        .width(cuerda.dp)
                        .height(300.dp)
                        .background(Color.Gray)
                )
            }
        }
    }
}



