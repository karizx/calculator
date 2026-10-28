package com.example.calculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.calculator.ui.theme.CalculatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CalculatorScreen()
                }
            }
        }
    }
}

@Composable
fun CalculatorScreen() {
    var expression by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    // Odczyt bieżącej orientacji ekranu z LocalConfiguration
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    LaunchedEffect(expression) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    // Wspólna funkcja obsługi kliknięcia przycisków
    val handleButtonClick: (CalcButton) -> Unit = { btn ->
        when (btn.symbol) {
            "C" -> expression = ""
            "⌫" -> if (expression.isNotEmpty()) expression = expression.dropLast(1)
            "=" -> { /* Obliczenia realizowane w części 2 */ }
            else -> expression += btn.onClickValue
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(if (isLandscape) 12.dp else 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // 1. Pole działania – w poziomie zajmuje proporcjonalnie mniej miejsca
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isLandscape) 0.8f else 1f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = expression.ifEmpty { "0" },
                fontSize = if (isLandscape) 34.sp else 44.sp,
                textAlign = TextAlign.End,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(scrollState)
            )
        }

        // 2. Blok przycisków: w poziomie układ Row z dwoma siatkami, w pionie pojedyncza siatka
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(if (isLandscape) 2.2f else 2f)
        ) {
            if (isLandscape) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Blok naukowy po lewej stronie
                    ButtonGrid(
                        buttons = scientificButtons,
                        onButtonClick = handleButtonClick,
                        modifier = Modifier.weight(1f)
                    )
                    // Blok podstawowy po prawej stronie
                    ButtonGrid(
                        buttons = basicButtons,
                        onButtonClick = handleButtonClick,
                        modifier = Modifier.weight(1.2f)
                    )
                }
            } else {
                // W pionie tylko blok podstawowy
                ButtonGrid(
                    buttons = basicButtons,
                    onButtonClick = handleButtonClick
                )
            }
        }
    }
}

@Composable
fun ButtonGrid(
    buttons: List<List<CalcButton>>,
    onButtonClick: (CalcButton) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        buttons.forEach { row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                row.forEach { button ->
                    // Rozróżnienie ról kolorystycznych zgodnie z Material 3
                    val (containerColor, contentColor) = when (button.role) {
                        ButtonRole.EQUALS ->
                            MaterialTheme.colorScheme.primary to MaterialTheme.colorScheme.onPrimary
                        ButtonRole.OPERATOR ->
                            MaterialTheme.colorScheme.primaryContainer to MaterialTheme.colorScheme.onPrimaryContainer
                        ButtonRole.ACTION ->
                            MaterialTheme.colorScheme.tertiaryContainer to MaterialTheme.colorScheme.onTertiaryContainer
                        ButtonRole.SCIENTIFIC ->
                            MaterialTheme.colorScheme.secondaryContainer to MaterialTheme.colorScheme.onSecondaryContainer
                        ButtonRole.DIGIT ->
                            MaterialTheme.colorScheme.surfaceVariant to MaterialTheme.colorScheme.onSurfaceVariant
                    }

                    Button(
                        onClick = { onButtonClick(button) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight(),
                        shape = MaterialTheme.shapes.medium,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = containerColor,
                            contentColor = contentColor
                        )
                    ) {
                        Text(
                            text = button.symbol,
                            fontSize = 18.sp
                        )
                    }
                }
            }
        }
    }
}