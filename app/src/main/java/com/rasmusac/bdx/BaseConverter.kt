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

private val BASE_OPTIONS = listOf("Decimal (0d)", "Hexadecimal (0x)", "Binary (0b)")
private val BASE_SHORT = listOf("Decimal", "Hexadecimal", "Binary")
private val BASE_PREFIXES = listOf("0d", "0x", "0b")
private val BASE_RADIXES = listOf(10, 16, 2)
private val BASE_KEYBOARD_TYPES = listOf(
    KeyboardType.Number,
    KeyboardType.Text,
    KeyboardType.Number
)

private data class ConversionResult(
    val decimal: String,
    val hex: String,
    val binary: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BaseConverter() {
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionIndex by remember { mutableIntStateOf(0) }
    var input by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    val radix = remember(selectedOptionIndex) { BASE_RADIXES[selectedOptionIndex] }

    val intValue = remember(input, radix) { input.toIntOrNull(radix) }

    val conversionResult = remember(intValue) {
        intValue?.let {
            ConversionResult(
                decimal = it.toString(10),
                hex = it.toString(16).uppercase(),
                binary = it.toString(2)
            )
        }
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
                                value = BASE_SHORT[selectedOptionIndex],
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
                                BASE_OPTIONS.forEachIndexed { index, option ->
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
                            modifier = Modifier
                                .weight(1.5f)
                                .height(56.dp),
                            shape = RoundedCornerShape(12.dp),
                            textStyle = androidx.compose.ui.text.TextStyle(fontSize = 16.sp),
                            colors = OutlinedTextFieldDefaults.colors(),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = BASE_KEYBOARD_TYPES[selectedOptionIndex]
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Results Section
            if (conversionResult != null) {
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

                        listOf(
                            Triple("Decimal:", conversionResult.decimal, BASE_PREFIXES[0]),
                            Triple("Hexadecimal:", conversionResult.hex, BASE_PREFIXES[1]),
                            Triple("Binary:", conversionResult.binary, BASE_PREFIXES[2])
                        ).forEachIndexed { index, (label, value, prefix) ->
                            if (index > 0) Spacer(modifier = Modifier.height(12.dp))
                            ResultRow(label = label, value = value, prefix = prefix)
                        }
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