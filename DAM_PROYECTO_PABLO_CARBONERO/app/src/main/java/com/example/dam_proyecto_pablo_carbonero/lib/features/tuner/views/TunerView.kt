package com.example.dam_proyecto_pablo_carbonero.lib.features.tuner.views

import android.Manifest
import android.content.pm.PackageManager
import android.transition.Slide
import androidx.activity.compose.LocalActivity
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.snapping.SnapPosition.Center
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
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
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.roundToInt
import kotlin.math.sin


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
    var sliderPosition by remember { mutableFloatStateOf(0f) }


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
                tunings.forEach { tuning ->
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

    Slider(
        value = graphValue.toFloat(),
        onValueChange = { sliderPosition = it },
        thumb = {
            Box(
                modifier = Modifier
                    .size(50.dp)  // Thumb size
                    .background(
                        color = Color.Transparent,
                        shape = CircleShape  // Makes it circular
                    )
                    .border(2.dp, colorGraph, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "Thumb",
                    tint = Color.White,
                    modifier = Modifier.size(16.dp)
                )
            }
        },
        colors = SliderDefaults.colors(
            activeTrackColor = Color.Transparent, // Hide active track
            inactiveTrackColor = Color.Transparent
        )
    )

    Text("$graphValue")

    //TuningNeedleIndicator(graphValue)
    //TuningBarIndicator(graphValue)
    CircularTopFillIndicator(graphValue)

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
fun TuningNeedleIndicator(normalizedValue: Double) {
    // Animación suave con Animatable
    val animatedValue = remember { Animatable(0.5f) }

    // Lanzar animación cuando cambia normalizedValue
    LaunchedEffect(normalizedValue) {
        // Limita entre 0f y 1f
        val target = normalizedValue.coerceIn(0.0, 1.0).toFloat()
        animatedValue.animateTo(
            target,
            animationSpec = tween(durationMillis = 300, easing = LinearOutSlowInEasing)
        )
    }

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .padding(16.dp)
    ) {
        val centerX = size.width / 2
        val centerY = size.height * 0.9f
        val radius = size.height * 0.6f

        // Fondo con guía central
        drawLine(
            color = Color.LightGray,
            start = Offset(centerX, centerY),
            end = Offset(centerX, centerY - radius),
            strokeWidth = 4f
        )

        // Calcula ángulo de la aguja según el valor animado
        val offsetFromCenter = (animatedValue.value - 0.5f) * 2f // -1 to 1
        val maxAngle = 45f  // grados
        val angleRad = Math.toRadians((offsetFromCenter * maxAngle).toDouble()).toFloat()

        val needleX = centerX + radius * sin(angleRad)
        val needleY = centerY - radius * cos(angleRad)

        // Aguja
        drawLine(
            color = Color.Red,
            start = Offset(centerX, centerY),
            end = Offset(needleX, needleY),
            strokeWidth = 6f,
            cap = StrokeCap.Round
        )
    }
}

@Composable
fun TuningBarIndicator(
    normalizedValue: Double,
    totalBars: Int = 11
) {
    val animatedValue = remember { Animatable(0.5f) }

    LaunchedEffect(normalizedValue) {
        val target = normalizedValue.coerceIn(0.0, 1.0).toFloat()
        animatedValue.animateTo(
            target,
            animationSpec = tween(durationMillis = 200)
        )
    }

    val filledBars = ((animatedValue.value - 0.5f) * (totalBars / 2)).roundToInt()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.Bottom
    ) {
        for (i in -(totalBars / 2)..(totalBars / 2)) {
            val isFilled = if (filledBars > 0) i in 0..filledBars else i in filledBars..0
            val color = when {
                i == 0 -> Color.Green
                isFilled -> if (abs(i) < 3) Color.Yellow else Color.Red
                else -> Color.LightGray
            }

            Box(
                modifier = Modifier
                    .padding(horizontal = 2.dp)
                    .width(6.dp)
                    .height((40 + 20 * (1 - abs(i).toFloat() / (totalBars / 2))).dp)
                    .background(color, shape = RoundedCornerShape(2.dp))
            )
        }
    }
}

@Composable
fun CircularTopFillIndicator(
    normalizedValue: Double,
    totalLines: Int = 41  // impar para tener línea central
) {
    val animatedValue = remember { Animatable(0.5f) }

    LaunchedEffect(normalizedValue) {
        animatedValue.animateTo(
            normalizedValue.coerceIn(0.0, 1.0).toFloat(),
            animationSpec = tween(durationMillis = 300)
        )
    }

    Canvas(
        modifier = Modifier
            .size(250.dp)
            .padding(16.dp)
    ) {
        val center = size.center
        val radius = size.minDimension / 2.5f
        val barLength = 30f

        val halfLines = totalLines / 2
        val targetIndex = ((animatedValue.value - 0.5f) * totalLines).roundToInt()

        for (i in -halfLines..halfLines) {
            val angleDeg = -90f + (i * (180f / totalLines))
            val angleRad = Math.toRadians(angleDeg.toDouble()).toFloat()

            val isFilled = if (targetIndex == 0) {
                i == 0
            } else if (targetIndex > 0) {
                i in 0..targetIndex
            } else {
                i in targetIndex..0
            }

            val color = when {
                i == 0 -> Color.Green
                isFilled && abs(i) < 3 -> Color.Yellow
                isFilled -> Color.Red
                else -> Color.LightGray
            }

            val start = Offset(
                x = center.x + cos(angleRad) * (radius - barLength),
                y = center.y + sin(angleRad) * (radius - barLength)
            )
            val end = Offset(
                x = center.x + cos(angleRad) * radius,
                y = center.y + sin(angleRad) * radius
            )

            drawLine(
                color = color,
                start = start,
                end = end,
                strokeWidth = 4f,
                cap = StrokeCap.Round
            )
        }
    }
}

