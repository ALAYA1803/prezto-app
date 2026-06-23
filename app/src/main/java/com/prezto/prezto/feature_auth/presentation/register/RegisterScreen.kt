package com.prezto.prezto.feature_auth.presentation.register

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.prezto.prezto.R
import com.prezto.prezto.core.designsystem.components.PreztoPasswordField
import com.prezto.prezto.core.designsystem.components.PreztoTextField

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    onNavigateBack: () -> Unit,
    onNavigateToVerification: (phone: String) -> Unit,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess) onNavigateToVerification(state.phone)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (state.currentStep == 2) viewModel.onBackToPersonalData()
                            else onNavigateBack()
                        }
                    ) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
            )
        },
        bottomBar = {
            Surface(color = MaterialTheme.colorScheme.background, shadowElevation = 16.dp) {
                Button(
                    onClick = {
                        if (state.currentStep == 1) viewModel.onContinueToSecurity()
                        else viewModel.register()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    enabled = !state.isLoading
                ) {
                    Crossfade(targetState = state.isLoading, label = "registerBtn") { loading ->
                        if (loading) {
                            CircularProgressIndicator(
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(24.dp)
                            )
                        } else {
                            Text(
                                text = if (state.currentStep == 1) "Continuar" else "Crear mi cuenta",
                                color = MaterialTheme.colorScheme.onPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo Prezto",
                modifier = Modifier.size(56.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Crear Cuenta",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(modifier = Modifier.height(8.dp))
            StepIndicator(currentStep = state.currentStep, totalSteps = 2)

            Spacer(modifier = Modifier.height(28.dp))

            AnimatedContent(
                targetState = state.currentStep,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally(tween(300)) { it } + fadeIn()) togetherWith
                                (slideOutHorizontally(tween(300)) { -it } + fadeOut())
                    } else {
                        (slideInHorizontally(tween(300)) { -it } + fadeIn()) togetherWith
                                (slideOutHorizontally(tween(300)) { it } + fadeOut())
                    }
                },
                label = "registerStep"
            ) { step ->
                when (step) {
                    1 -> PersonalDataStep(state = state, viewModel = viewModel)
                    else -> SecurityStep(state = state, viewModel = viewModel)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (state.currentStep == 1) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "¿Ya tienes una cuenta? ",
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "Inicia Sesión",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clickable { onNavigateBack() }
                            .padding(4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun StepIndicator(currentStep: Int, totalSteps: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Paso $currentStep de $totalSteps",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(8.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            repeat(totalSteps) { index ->
                val active = index < currentStep
                Box(
                    modifier = Modifier
                        .height(6.dp)
                        .width(if (active) 32.dp else 16.dp)
                        .clip(RoundedCornerShape(50))
                        .background(
                            if (active) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.25f)
                        )
                )
            }
        }
    }
}

@Composable
private fun PersonalDataStep(
    state: RegisterState,
    viewModel: RegisterViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Cuéntanos sobre ti",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(16.dp))

        PreztoTextField(
            value = state.fullName,
            onValueChange = viewModel::onFullNameChanged,
            label = "Nombre Completo",
            leadingIcon = Icons.Default.Person,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            isError = state.fullNameError != null,
            errorMessage = state.fullNameError
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            PreztoTextField(
                value = state.dni,
                onValueChange = viewModel::onDniChanged,
                label = "DNI",
                leadingIcon = Icons.Default.Badge,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
                isError = state.dniError != null,
                errorMessage = state.dniError,
                modifier = Modifier.weight(1f)
            )
            PreztoTextField(
                value = state.phone,
                onValueChange = viewModel::onPhoneChanged,
                label = "Celular",
                leadingIcon = Icons.Default.Phone,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone, imeAction = ImeAction.Next),
                isError = state.phoneError != null,
                errorMessage = state.phoneError,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        PreztoTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChanged,
            label = "Correo Electrónico",
            leadingIcon = Icons.Default.Email,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { viewModel.onContinueToSecurity() }),
            isError = state.emailError != null,
            errorMessage = state.emailError
        )
    }
}

@Composable
private fun SecurityStep(
    state: RegisterState,
    viewModel: RegisterViewModel
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Asegura tu cuenta",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "Crea una contraseña de al menos 8 caracteres, con un número y un carácter especial.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Start
        )
        Spacer(modifier = Modifier.height(16.dp))

        PreztoPasswordField(
            value = state.password,
            onValueChange = viewModel::onPasswordChanged,
            isError = state.passwordError != null,
            errorMessage = state.passwordError,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { viewModel.register() })
        )
    }
}
