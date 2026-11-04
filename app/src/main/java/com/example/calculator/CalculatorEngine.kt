package com.example.calculator

import org.mariuszgromada.math.mxparser.Expression
import org.mariuszgromada.math.mxparser.License

object CalculatorEngine {

    init {
        License.iConfirmNonCommercialUse("Uzytek dydaktyczny - Laboratorium PSM")
    }

    /**
     * Oblicza wartość wyrażenia matematycznego.
     * Zwraca sformatowany wynik, czytelny komunikat błędu lub pusty ciąg dla wyrażeń niedokończonych.
     */
    fun evaluate(expression: String): String {
        val trimmed = expression.trim()
        if (trimmed.isEmpty()) return ""

        // Zapamiętujemy, czy użytkownik użył przecinka (wymóg separatora z Zadania 3)
        val hasComma = trimmed.contains(",")

        // Normalizacja: zamiana przecinków na kropki oraz symboli UI na składnię mXparsera
        val normalized = trimmed
            .replace(",", ".")
            .replace("√", "sqrt")
            .replace("π", "pi")

        // Blokada wyrażeń niedokończonych (np. "2+", "5*", "sin(")
        if (normalized.endsWith("+") || normalized.endsWith("-") ||
            normalized.endsWith("*") || normalized.endsWith("/") ||
            normalized.endsWith("^") || normalized.endsWith("(")
        ) {
            return ""
        }

        val expr = Expression(normalized)

        // 1. Sprawdzenie składni (np. "2+", "((2+2)")
        // Zgodnie z tabelą konspektu: brak wyniku, bez awarii
        if (!expr.checkSyntax()) {
            return ""
        }

        val result = expr.calculate()

        // 2. Obsługa błędów matematycznych przy poprawnej składni
        if (result.isNaN() || result.isInfinite()) {
            return when {
                Regex("""/\s*0(\.0*)?([^\d]|$)""").containsMatchIn(normalized) || normalized.contains("/0") ->
                    "Błąd: dzielenie przez 0"
                normalized.contains("sqrt(-") || Regex("""sqrt\s*\(\s*-\d""").containsMatchIn(normalized) ->
                    "Błąd: poza dziedziną"
                normalized.contains("ln(-") || normalized.contains("ln(0") ->
                    "Błąd: poza dziedziną"
                normalized.contains("/") ->
                    "Błąd: dzielenie przez 0"
                else ->
                    "Błąd"
            }
        }

        // 3. Formatowanie wyniku (usunięcie zbędnego .0 dla liczb całkowitych)
        val formatted = if (result % 1.0 == 0.0 && !result.toString().contains("E") && !result.toString().contains("e")) {
            result.toLong().toString()
        } else {
            result.toString()
        }

        // Jeśli wejście miało przecinek, wynik również zwracamy z przecinkiem
        return if (hasComma) formatted.replace(".", ",") else formatted
    }
}