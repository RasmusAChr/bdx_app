package com.rasmusac.bdx

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Preview
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyActivity() {
    var activeBase by remember { mutableIntStateOf(0) } // 0=decimal, 1=hex, 2=binary, 3=octal
    var currentValue by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Parse current value based on active base and convert to other bases
    val intValue = if (currentValue.isNotEmpty()) {
        currentValue.toIntOrNull(
            when (activeBase) {
                0 -> 10  // Decimal
                1 -> 16  // Hex
                2 -> 2   // Binary
                3 -> 8   // Octal
                else -> 10
            }
        )
    } else null

    val decimal = if (activeBase == 0) currentValue else (intValue?.toString(10) ?: "")
    val hex = if (activeBase == 1) currentValue else (intValue?.toString(16)?.uppercase() ?: "")
    val octal = if (activeBase == 3) currentValue else (intValue?.toString(8) ?: "")
    val binary = if (activeBase == 2) currentValue else (intValue?.toString(2) ?: "")

    // Get enabled keys based on active base
    val enabledKeys = when (activeBase) {
        0 -> listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9") // Decimal
        1 -> listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F") // Hex
        2 -> listOf("0", "1") // Binary
        3 -> listOf("0", "1", "2", "3", "4", "5", "6", "7") // Octal
        else -> listOf("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Base Converter",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Base input/output sections - centered with weight
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Decimal
                    BaseInputOutputCard(
                        label = "Decimal",
                        prefix = "0d",
                        value = decimal,
                        isActive = activeBase == 0,
                        isValid = intValue != null || currentValue.isEmpty(),
                        onClick = {
                            if (activeBase != 0) {
                                activeBase = 0
                                currentValue = decimal
                            }
                        }
                    )

                    // Hexadecimal
                    BaseInputOutputCard(
                        label = "Hexadecimal",
                        prefix = "0x",
                        value = hex,
                        isActive = activeBase == 1,
                        isValid = intValue != null || currentValue.isEmpty(),
                        onClick = {
                            if (activeBase != 1) {
                                activeBase = 1
                                currentValue = hex
                            }
                        }
                    )

                    // Octal
                    BaseInputOutputCard(
                        label = "Octal",
                        prefix = "0o",
                        value = octal,
                        isActive = activeBase == 3,
                        isValid = intValue != null || currentValue.isEmpty(),
                        onClick = {
                            if (activeBase != 3) {
                                activeBase = 3
                                currentValue = octal
                            }
                        }
                    )

                    // Binary
                    BaseInputOutputCard(
                        label = "Binary",
                        prefix = "0b",
                        value = binary,
                        isActive = activeBase == 2,
                        isValid = intValue != null || currentValue.isEmpty(),
                        onClick = {
                            if (activeBase != 2) {
                                activeBase = 2
                                currentValue = binary
                            }
                        }
                    )
                }
            }

            // Custom Keyboard - stays at bottom
            CustomKeyboard(
                enabledKeys = enabledKeys,
                onKeyClick = { key ->
                    // Validate input based on active base
                    val newValue = currentValue + key
                    val isValidInput = newValue.toIntOrNull(
                        when (activeBase) {
                            0 -> 10
                            1 -> 16
                            2 -> 2
                            3-> 8
                            else -> 10
                        }
                    ) != null

                    if (isValidInput) {
                        currentValue = newValue
                    }
                },
                onDeleteClick = {
                    if (currentValue.isNotEmpty()) {
                        currentValue = currentValue.dropLast(1)
                    }
                },
                onClearClick = {
                    currentValue = ""
                }
            )
        }
    }
}

@Composable
fun BaseInputOutputCard(
    label: String,
    prefix: String,
    value: String,
    isActive: Boolean,
    isValid: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isActive) 8.dp else 2.dp
        ),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                !isValid -> MaterialTheme.colorScheme.errorContainer
                isActive -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.surface
            }
        ),
        border = if (isActive) {
            androidx.compose.foundation.BorderStroke(
                2.dp,
                MaterialTheme.colorScheme.primary
            )
        } else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = when {
                    !isValid -> MaterialTheme.colorScheme.onErrorContainer
                    isActive -> MaterialTheme.colorScheme.onPrimaryContainer
                    else -> MaterialTheme.colorScheme.onSurface
                },
                modifier = Modifier.weight(1f)
            )

            SelectionContainer {
                Text(
                    text = if (value.isEmpty()) prefix else "$prefix$value",
                    fontSize = 18.sp,
                    fontFamily = FontFamily.Monospace,
                    color = when {
                        !isValid -> MaterialTheme.colorScheme.onErrorContainer
                        isActive -> MaterialTheme.colorScheme.primary
                        else -> MaterialTheme.colorScheme.onSurface
                    },
                    fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                    modifier = Modifier.weight(1.5f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
}

@Composable
fun CustomKeyboard(
    enabledKeys: List<String>,
    onKeyClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    onClearClick: () -> Unit
) {
    // Define the keyboard layout matching the image
    val keyLayout = listOf(
        listOf("A", "1", "2", "3", "AC"),
        listOf("B", "4", "5", "6", "⌫"),
        listOf("C", "7", "8", "9", "E"),
        listOf("D", "", "0", "", "F")
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onPrimary
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            keyLayout.forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    row.forEach { key ->
                        KeyboardButton(
                            key = key,
                            modifier = Modifier.weight(1f),
                            enabled = key.isEmpty() || enabledKeys.contains(key) || key == "AC" || key == "⌫",
                            onClick = {
                                when (key) {
                                    "AC" -> onClearClick()
                                    "⌫" -> onDeleteClick()
                                    else -> if (key.isNotEmpty()) onKeyClick(key)
                                }
                            }
                        )
                    }
                }
                if (row != keyLayout.last()) {
                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}

@Composable
fun KeyboardButton(
    key: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val isVisible = key.isNotEmpty()
    val isSpecialKey = key == "AC" || key == "⌫"

    Box(
        modifier = modifier.height(56.dp),
        contentAlignment = Alignment.Center
    ) {
        if (isVisible) {
            Button(
                onClick = onClick,
                enabled = enabled,
                modifier = Modifier.fillMaxSize(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isSpecialKey) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.primary
                    },
                    disabledContainerColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = key,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    color = if (enabled) {
                        MaterialTheme.colorScheme.onPrimary
                    } else {
                        MaterialTheme.colorScheme.outline
                    }
                )
            }
        }
    }
}