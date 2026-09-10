package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.i18n.Translations
import com.example.data.model.UserRole
import com.example.state.AppUiState
import com.example.state.AppViewModel
import com.example.ui.theme.*

@Composable
fun SmritiTopBar(
    state: AppUiState,
    viewModel: AppViewModel,
    onOpenRoleSelector: () -> Unit
) {
    var showLangMenu by remember { mutableStateOf(false) }

    Surface(
        color = if (state.isHighContrast) HighContrastSurface else Color.White,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp)
        ) {
            // Top Row: Logo, Brand Name & Controls
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Logo & Name
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(Emerald900, Teal800, Emerald950)
                                )
                            )
                            .border(1.dp, Amber400.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                            .clickable { viewModel.soundHelper.playChime("gentle") },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "স্মৃ",
                            color = Amber200,
                            fontWeight = FontWeight.Black,
                            fontSize = 18.sp
                        )
                    }

                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "SmritiNER",
                                fontWeight = FontWeight.Black,
                                fontSize = 16.sp,
                                color = if (state.isHighContrast) HighContrastYellow else TextDark
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "✨ NER",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Emerald700,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(Emerald50)
                                    .padding(horizontal = 4.dp, vertical = 1.dp)
                            )
                        }
                        Text(
                            text = "Cognitive Care & Memory",
                            fontSize = 10.sp,
                            color = if (state.isHighContrast) Color.LightGray else TextMuted
                        )
                    }
                }

                // Quick Action Controls (Sound, Offline, Language)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Language Dropdown Selector
                    Box {
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (state.isHighContrast) HighContrastBg else Color(0xFFF5F3EF),
                            border = BorderStroke(1.dp, if (state.isHighContrast) HighContrastYellow else BorderSubtle),
                            modifier = Modifier.clickable { showLangMenu = true }
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = "🌐 ${state.currentLanguage.uppercase()}",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (state.isHighContrast) HighContrastYellow else TextDark
                                )
                            }
                        }

                        DropdownMenu(
                            expanded = showLangMenu,
                            onDismissRequest = { showLangMenu = false }
                        ) {
                            Translations.languages.forEach { lang ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(lang.nativeName, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text(lang.englishName, fontSize = 10.sp, color = TextMuted)
                                        }
                                    },
                                    onClick = {
                                        viewModel.setLanguage(lang.code)
                                        showLangMenu = false
                                    },
                                    trailingIcon = {
                                        if (state.currentLanguage == lang.code) {
                                            Icon(Icons.Default.Check, contentDescription = "Active", tint = Emerald700)
                                        }
                                    }
                                )
                            }
                        }
                    }

                    // Sound Effects Toggle
                    IconButton(
                        onClick = { viewModel.toggleSound() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = if (state.soundEffects) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                            contentDescription = "Sound Toggle",
                            tint = if (state.soundEffects) Emerald700 else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // High Contrast Toggle
                    IconButton(
                        onClick = { viewModel.toggleHighContrast() },
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Brightness6,
                            contentDescription = "Contrast Toggle",
                            tint = if (state.isHighContrast) HighContrastYellow else TextMuted,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Online / Offline Toggle
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (state.isOffline) Amber50 else Emerald50,
                        border = BorderStroke(1.dp, if (state.isOffline) Amber400 else Emerald600.copy(alpha = 0.4f)),
                        modifier = Modifier.clickable { viewModel.toggleOffline() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = if (state.isOffline) Icons.Default.WifiOff else Icons.Default.Wifi,
                                contentDescription = "Network",
                                tint = if (state.isOffline) Amber600 else Emerald700,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = if (state.isOffline) "Offline" else "Online",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (state.isOffline) Amber600 else Emerald700
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Bottom Row: Active Role Indicator + Fast Role Switcher Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // Current User Tag
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (state.isHighContrast) HighContrastBg else Color(0xFFF5F3EF))
                        .clickable { onOpenRoleSelector() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = when (state.currentUser?.role) {
                            UserRole.PATIENT -> "👵 Patient"
                            UserRole.DOCTOR -> "🩺 Doctor"
                            UserRole.CAREGIVER -> "❤️ Caregiver"
                            else -> "👤 Select Role"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp,
                        color = if (state.isHighContrast) HighContrastYellow else Emerald800
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = state.currentUser?.name?.take(16) ?: "",
                        fontSize = 10.sp,
                        color = TextMuted
                    )
                }

                // 1-Tap Role Switch Buttons (Patient | Doctor | Caregiver)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val currentRole = state.currentUser?.role
                    RoleSwitchPill(
                        label = "Patient",
                        isSelected = currentRole == UserRole.PATIENT,
                        color = Emerald700,
                        onClick = { viewModel.switchRole(UserRole.PATIENT) }
                    )
                    RoleSwitchPill(
                        label = "Doctor",
                        isSelected = currentRole == UserRole.DOCTOR,
                        color = Sky700,
                        onClick = { viewModel.switchRole(UserRole.DOCTOR) }
                    )
                    RoleSwitchPill(
                        label = "Caregiver",
                        isSelected = currentRole == UserRole.CAREGIVER,
                        color = Rose700,
                        onClick = { viewModel.switchRole(UserRole.CAREGIVER) }
                    )
                }
            }
        }
    }
}

@Composable
fun RoleSwitchPill(
    label: String,
    isSelected: Boolean,
    color: Color,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isSelected) color else Color.Transparent,
        border = if (!isSelected) BorderStroke(1.dp, BorderSubtle) else null,
        modifier = Modifier.clickable { onClick() }
    ) {
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = if (isSelected) Color.White else TextMuted,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        )
    }
}

@Composable
fun CelebrationToast(message: String?) {
    AnimatedVisibility(
        visible = message != null,
        enter = slideInVertically { -it } + fadeIn(),
        exit = slideOutVertically { -it } + fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Emerald700,
                shadowElevation = 8.dp,
                border = BorderStroke(1.dp, Amber400)
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✨", fontSize = 16.sp)
                    Text(
                        text = message ?: "",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
            }
        }
    }
}
