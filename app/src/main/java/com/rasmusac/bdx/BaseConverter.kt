import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverter() {
    val options = listOf("Decimal (0d)", "Hexadecimal (0x)", "Binary (0b)")
    val shortOptions = listOf("Decimal", "Hexadecimal", "Binary")
    val prefixes = listOf("0d", "0x", "0b")
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableIntStateOf(0) }
    var input by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Parse input based on selected base
    val intValue = input.toIntOrNull(
        when (selectedOptionIndex) {
            0 -> 10  // Decimal
            1 -> 16  // Hex
            2 -> 2   // Binary
            else -> 10
        }
    )

    val decimal = intValue?.toString(10) ?: ""
    val binary = intValue?.toString(2) ?: ""
    val hex = intValue?.toString(16)?.uppercase() ?: ""

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
                .padding(24.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusManager.clearFocus()
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Input Section
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Input",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Dropdown for base selection
                        ExposedDropdownMenuBox(
                            expanded = expanded,
                            onExpandedChange = { expanded = !expanded },
                            modifier = Modifier.weight(1.5f)
                        ) {
                            OutlinedTextField(
                                value = shortOptions[selectedOptionIndex],
                                onValueChange = { },
                                readOnly = true,
                                label = { Text("Base", fontSize = 14.sp) },
                                trailingIcon = {
                                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(56.dp)
                                    .menuAnchor(MenuAnchorType.PrimaryNotEditable),
                                shape = RoundedCornerShape(12.dp),
                                textStyle = androidx.compose.ui.text.TextStyle(fontSize = 14.sp),
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors()
                            )
                            ExposedDropdownMenu(
                                expanded = expanded,
                                onDismissRequest = { expanded = false },
                                modifier = Modifier
                                    .wrapContentWidth()
                                    .widthIn(min = 180.dp)
                            ) {
                                options.forEachIndexed { index, option ->
                                    DropdownMenuItem(
                                        text = {
                                            Text(
                                                text = option,
                                                fontSize = 14.sp,
                                                softWrap = false,
                                                maxLines = 1,
                                                modifier = Modifier.padding(end = 8.dp)
                                            )
                                        },
                                        onClick = {
                                            selectedOptionIndex = index
                                            expanded = false
                                        },
                                        modifier = Modifier.wrapContentWidth()
                                    )
                                }
                            }
                        }

                        // Input field
                        OutlinedTextField(
                            value = input,
                            onValueChange = { input = it },
                            label = { Text("Enter value", fontSize = 14.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1.5f).height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                            colors = OutlinedTextFieldDefaults.colors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = when (selectedOptionIndex) {
                                    0 -> KeyboardType.Number      // Decimal
                                    1 -> KeyboardType.Text        // Hexadecimal
                                    2 -> KeyboardType.Number      // Binary
                                    else -> KeyboardType.Number
                                }
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Results Section
            if (intValue != null) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {
                        Text(
                            text = "Conversions",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp),
                            textAlign = TextAlign.Center
                        )

                        // Decimal result
                        ResultRow(
                            label = "Decimal:",
                            value = decimal,
                            prefix = "0d"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Hexadecimal result
                        ResultRow(
                            label = "Hexadecimal:",
                            value = hex,
                            prefix = "0x"
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Binary result
                        ResultRow(
                            label = "Binary:",
                            value = binary,
                            prefix = "0b"
                        )
                    }
                }
            } else if (input.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = "Invalid input for selected base",
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            // Info text at bottom
            Text(
                text = "Enter a number in the selected base to see conversions",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
private fun ResultRow(
    label: String,
    value: String,
    prefix: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f)
        )

        SelectionContainer {
            Text(
                text = "$prefix$value",
                fontSize = 16.sp,
                fontFamily = FontFamily.Monospace,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1.5f),
                textAlign = TextAlign.End
            )
        }
    }
}