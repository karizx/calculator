package com.example.calculator

// Rola przycisku posłuży w Zadaniu 4 do przypisania kolorów Material 3
enum class ButtonRole {
    DIGIT,      // Cyfry oraz kropka
    OPERATOR,   // Działania: +, -, *, /
    ACTION,     // C, ⌫, nawiasy
    EQUALS,     // Znak =
    SCIENTIFIC  // Funkcje naukowe (do Zadania 3)
}

data class CalcButton(
    val symbol: String,
    val role: ButtonRole,
    val onClickValue: String = symbol
)

// 20 przycisków bloku podstawowego (5 wierszy x 4 kolumny)
val basicButtons = listOf(
    listOf(
        CalcButton("C", ButtonRole.ACTION),
        CalcButton("⌫", ButtonRole.ACTION),
        CalcButton("(", ButtonRole.ACTION),
        CalcButton(")", ButtonRole.ACTION)
    ),
    listOf(
        CalcButton("7", ButtonRole.DIGIT),
        CalcButton("8", ButtonRole.DIGIT),
        CalcButton("9", ButtonRole.DIGIT),
        CalcButton("/", ButtonRole.OPERATOR)
    ),
    listOf(
        CalcButton("4", ButtonRole.DIGIT),
        CalcButton("5", ButtonRole.DIGIT),
        CalcButton("6", ButtonRole.DIGIT),
        CalcButton("*", ButtonRole.OPERATOR)
    ),
    listOf(
        CalcButton("1", ButtonRole.DIGIT),
        CalcButton("2", ButtonRole.DIGIT),
        CalcButton("3", ButtonRole.DIGIT),
        CalcButton("-", ButtonRole.OPERATOR)
    ),
    listOf(
        CalcButton("0", ButtonRole.DIGIT),
        CalcButton(".", ButtonRole.DIGIT),
        CalcButton("+", ButtonRole.OPERATOR),
        CalcButton("=", ButtonRole.EQUALS)
    )
)
// Blok funkcji naukowych (pojawia się tylko w trybie poziomym)
val scientificButtons = listOf(
    listOf(
        CalcButton("sin", ButtonRole.SCIENTIFIC, onClickValue = "sin("),
        CalcButton("cos", ButtonRole.SCIENTIFIC, onClickValue = "cos("),
        CalcButton("tan", ButtonRole.SCIENTIFIC, onClickValue = "tan("),
        CalcButton("π", ButtonRole.SCIENTIFIC, onClickValue = "π")
    ),
    listOf(
        CalcButton("√", ButtonRole.SCIENTIFIC, onClickValue = "sqrt("),
        CalcButton("xʸ", ButtonRole.SCIENTIFIC, onClickValue = "^"),
        CalcButton("ln", ButtonRole.SCIENTIFIC, onClickValue = "ln("),
        CalcButton("e", ButtonRole.SCIENTIFIC, onClickValue = "e")
    )
)