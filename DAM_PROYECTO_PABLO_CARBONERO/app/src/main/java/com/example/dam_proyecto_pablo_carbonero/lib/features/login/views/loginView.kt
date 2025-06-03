package com.example.dam_proyecto_pablo_carbonero.lib.features.login.views

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.dam_proyecto_pablo_carbonero.R
import com.example.dam_proyecto_pablo_carbonero.lib.features.login.viewmodels.LoginVM
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun LoginView(navController: NavHostController, vm: LoginVM = hiltViewModel()) {
    val email by vm.email.collectAsState()
    val password by vm.password.collectAsState()
    var passwordVisible by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                brush = Brush.verticalGradient(
                    colorStops = arrayOf(
                        0.0f to MaterialTheme.colorScheme.primary,
                        0.35f to MaterialTheme.colorScheme.background,
                    )
                )
            )
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Box(
            modifier = Modifier
                .size(110.dp)
                .padding(bottom = 16.dp)
                .aspectRatio(1f)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                ).padding(10.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "logo",
                modifier = Modifier.matchParentSize()//size(20.dp)
            )
        }

        // Title
        Text(
            text = "Welcome Back",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Subtitle
        Text(
            text = "Login to continue",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
            modifier = Modifier.padding(bottom = 32.dp)
        )

        // Email Field
        OutlinedTextField(
            value = email,
            onValueChange = { vm.setEmail(it) },
            label = { Text("Email") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(12.dp)
        )

        // Password Field
        OutlinedTextField(
            value = password,
            onValueChange = { vm.setpassword(it) },
            label = { Text("Password") },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(12.dp)
        )


        Button(
            onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val loggedIn = vm.signInWithEmailAndPassword()

                    if(loggedIn){
                        navController.navigate("Settings"){
                            popUpTo("Settings") { inclusive = true }
                        }
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Login", style = MaterialTheme.typography.labelLarge)
        }

        //todo Olvidé mi contraseá a ver que hago un html pedorro
        TextButton(
            onClick = { },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                "Forgot Password?",
                color = MaterialTheme.colorScheme.primary
            )
        }

        // Sign Up Prompt
        Row(
            modifier = Modifier.padding(top = 32.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Don't have an account?",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
            )
            TextButton(onClick = { navController.navigate("Register"){
                popUpTo("Settings") {  }
            } }) {
                Text("Sign Up", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}