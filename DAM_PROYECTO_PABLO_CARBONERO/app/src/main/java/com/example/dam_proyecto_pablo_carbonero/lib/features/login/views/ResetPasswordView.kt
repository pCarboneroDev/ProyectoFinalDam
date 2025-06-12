package com.example.dam_proyecto_pablo_carbonero.lib.features.login.views

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.R
import com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels.ResetPasswordVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ResetPasswordView(navController: NavHostController, vm: ResetPasswordVM = hiltViewModel()){
    val context = LocalContext.current
    val email by vm.email.collectAsState()
    val wrongEmail by vm.wrongEmail.collectAsState()
    val isLoading by vm.isLoading.collectAsState()
    val mailSent by vm.mailSent.collectAsState()
    val messageManager by vm.messageManager.collectAsState()

    LaunchedEffect(messageManager) {
        if(!messageManager.isSuccess){
            Toast.makeText(context, messageManager.message, Toast.LENGTH_SHORT).show()
            vm.resetMessageManager()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp).systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier
                .size(110.dp)
                .padding(bottom = 16.dp)
                .aspectRatio(1f)
                .border(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary,
                    shape = CircleShape
                ).padding(10.dp)
        )

        // título
        Text(
            text = "Reset your password",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // subtititulo
        Text(
            text =
                if (!mailSent)
                    "Enter email and we will send you an email to reset your password"
                else
                    "Check your email inbox and spam section",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email
        OutlinedTextField(
            value = email,
            onValueChange = { vm.setEmail(it) },
            label = { Text("Email") },
            singleLine = true,
            supportingText = { if (wrongEmail) Text("Enter a valid email") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp),
            isError = wrongEmail
        )

        // resend
        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    vm.sendEmail()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),

            ) {
            Text(
                text = if (mailSent) "Re-send again" else "Send email",
                style = MaterialTheme.typography.labelLarge)
        }
    }
}