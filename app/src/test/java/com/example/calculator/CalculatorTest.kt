package com.example.calculator

import org.junit.Assert.assertEquals
import org.junit.Test

class CalculatorTest {

    // 1. Kolejność działań: 2+2*2 = 6, nie 8
    @Test
    fun testOperatorPrecedence() {
        assertEquals("6", CalculatorEngine.evaluate("2+2*2"))
    }

    // 2. Nawiasy: (2+2)*2 = 8
    @Test
    fun testParentheses() {
        assertEquals("8", CalculatorEngine.evaluate("(2+2)*2"))
    }

    // 3. Dzielenie przez zero: 1/0 -> czytelny komunikat, bez awarii
    @Test
    fun testDivisionByZero() {
        assertEquals("Błąd: dzielenie przez 0", CalculatorEngine.evaluate("1/0"))
    }

    // 4. Wyrażenie niedokończone: 2+ -> brak wyniku, bez awarii
    @Test
    fun testIncompleteExpression() {
        assertEquals("", CalculatorEngine.evaluate("2+"))
    }

    // 5. Nawias niezamknięty: ((2+2) -> brak wyniku, bez awarii
    @Test
    fun testUnclosedParenthesis() {
        assertEquals("", CalculatorEngine.evaluate("((2+2)"))
    }

    // 6. Puste pole -> brak wyniku i brak błędu
    @Test
    fun testEmptyExpression() {
        assertEquals("", CalculatorEngine.evaluate(""))
    }

    // 7. Dziedzina funkcji: sqrt(-1) -> czytelny komunikat
    @Test
    fun testDomainError() {
        assertEquals("Błąd: poza dziedziną", CalculatorEngine.evaluate("sqrt(-1)"))
    }

    // 8. Duże liczby: 1234567890*1234567890 -> wynik czytelny, bez awarii
    @Test
    fun testLargeNumbers() {
        val result = CalculatorEngine.evaluate("1234567890*1234567890")
        assertEquals(true, result.isNotEmpty())
        assertEquals(true, result.contains("1.524157875"))
    }

    // Testy separatora dziesiętnego: kropka vs przecinek
    @Test
    fun testDecimalSeparators() {
        assertEquals("3.5", CalculatorEngine.evaluate("2.5+1"))
        assertEquals("3,5", CalculatorEngine.evaluate("2,5+1"))
    }
}