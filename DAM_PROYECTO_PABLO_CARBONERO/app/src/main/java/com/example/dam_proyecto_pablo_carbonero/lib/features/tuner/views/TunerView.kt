package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.lib.data.local.entities.MusicNote
import com.example.dam_proyecto_pablo_carbonero.lib.features.global.composables.BottomNavBar
import com.example.dam_proyecto_pablo_carbonero.lib.domain.model.TuningWithNotesModel
import com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.viewmodels.TunerVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.exp


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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(vm: TunerVM, startingTuning: TuningWithNotesModel, navController: NavHostController){
    val mainColor = MaterialTheme.colorScheme.primary
    var expanded by remember { mutableStateOf(false) }

    var i by remember { mutableStateOf(-1) }

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


    Row(Modifier.fillMaxWidth()) {
        // composable que contiene el header para seleccionar la afinación
        TuningSelector(
            selectedTuning = selectedTuning,
            tunings = tunings,
            latinNotes = latinNotes,
            onTuningSelected = { vm.setSelectedTuning(it); vm.setSelectedNote(null) },
            navController
        )
    }

    HorizontalDivider()


    Text("$graphValue")

    TuningAnimation(graphValue.toFloat(), Modifier, colorGraph)

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


@Composable
fun TuningAnimation(
    graphValue: Float,  // Value from 0 (too low) to 1 (too high), 0.5 is centered
    modifier: Modifier = Modifier,
    color: Color
) {
    val animatedValue by animateFloatAsState(
        targetValue = graphValue,
        animationSpec = spring(dampingRatio = 0.5f, stiffness = 100f),
        label = "tuningAnimation"
    )

    //if (min == null) min = 0f

    var containerWidth by remember { mutableStateOf(0) }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(100.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .onSizeChanged { containerWidth = it.width },
                //.background(Color.LightGray.copy(alpha = 0.3f)),
            contentAlignment = Alignment.Center
        ) {
            // Center line
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(Color.Gray)
            )

            // Range indicator
            Box(
                modifier = Modifier
                    .width(80.dp)
                    .height(4.dp)
                    .background(Color.Green.copy(alpha = 0.5f))
            )

            // Moving sphere
            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = ((animatedValue - 0.5f) * containerWidth * 0.8f).toInt(), //Function invocation 'size(...)' expected. // Function invocation 'width(...)' expected.
                            y = 0
                        )
                    }
                    .size(40.dp)
                    .background(
                        color = when {
                            animatedValue in 0.45f..0.55f -> color
                            animatedValue < 0.45f -> color
                            else -> color
                        },
                        shape = CircleShape
                    )
                    .border(2.dp, Color.White, CircleShape)
            )
        }
    }
}

@Composable
fun TuningSelector(
    selectedTuning: TuningWithNotesModel?,
    tunings: List<TuningWithNotesModel>,
    latinNotes: Boolean,
    onTuningSelected: (TuningWithNotesModel) -> Unit,
    navController: NavHostController,
){
    var expanded by remember { mutableStateOf(false) }
    Box(
        Modifier
            .height(60.dp)
            .width(270.dp)
            .clickable(
                onClick = {expanded = !expanded}
            ),
        contentAlignment = Alignment.CenterStart,
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
            tunings.forEach { tuning ->
                DropdownMenuItem(
                    text = {
                        Text(tuning.tuning.name)
                    },
                    onClick = { onTuningSelected(tuning); expanded = false }
                )
            }
        }
    }

    if(selectedTuning?.tuning?.name != "Standard Tuning"){
        IconButton(onClick = {
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