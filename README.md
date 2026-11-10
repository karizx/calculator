# Programowanie Systemów Mobilnych – Laboratorium 4
## Kalkulator (Część 1) – Responsywny interfejs użytkownika i Material 3

## Informacje ogólne
* **Autor:** Karol Budzyń
* **Uczelnia:** Akademia Tarnowska
* **Kierunek:** Informatyka, Inżynieria Systemów Inteligentnych
* **Technologia:** Jetpack Compose, Material 3, Kotlin

---

## Opis projektu
Celem pierwszej części projektu kalkulatora było zbudowanie w pełni responsywnego interfejsu działającego bezbłędnie w orientacji pionowej i poziomej oraz opracowanie autorskiej palety barw Material 3. Aplikacja nie posiada sztucznych blokad orientacji w manifeście, skaluje się do dostępnej przestrzeni ekranu za pomocą wag (`Modifier.weight`) i nie duplikuje kodu siatki przycisków pomiędzy widokami.

---

## Realizacja zadań

### Zadanie 1: Konfiguracja projektu i brak blokady orientacji
* Projekt założony na czystym szablonie Empty Activity.
* Zweryfikowano brak atrybutu `android:screenOrientation` w pliku `AndroidManifest.xml` – aplikacja płynnie reaguje na obrót urządzenia.

### Zadanie 2: Układ pionowy i elastyczna siatka
* Układ bazowy 4x5 zawierający cyfry, cztery działania arytmetyczne, nawiasy, kropkę dziesiętną, czyszczenie (`C`) oraz kasowanie ostatniego znaku (`⌫`).
* Przyciski zostały zamodelowane strukturą danych (`CalcButton`, `ButtonRole`), dzięki czemu interfejs iteruje po liście, eliminując powielanie kodu komponentów `Button`.
* Przestrzeń ekranu dzielona jest proporcjonalnie wagami (`Modifier.weight(1f)`), co gwarantuje prawidłowe skalowanie na ekranach o dowolnej przekątnej i gęstości pikseli.
* Pole działania obsługuje długie wyrażenia za pomocą poziomego przewijania (`Modifier.horizontalScroll`), eliminując problem ucinania tekstu.

### Zadanie 3: Układ poziomy i tryb naukowy
* Wykrywanie bieżącego trybu realizowane dynamicznie przez `LocalConfiguration.current.orientation`.
* W trybie poziomym obok bloku podstawowego odsłaniany jest blok funkcji naukowych (`sin`, `cos`, `tan`, `π`, `√`, `xʸ`, `ln`, `e`).
* **Zasada pojedynczej odpowiedzialności i brak duplikacji:** Obie siatki renderowane są przez dokładnie ten sam komponent `ButtonGrid`, wywoływany z odrębnymi zestawami danych.
* Przyciski funkcji wprowadzają automatycznie nawias otwierający (np. `sin(`), przygotowując strukturę pod analizator składniowy w części 2.

### Zadanie 4: Autorska paleta barw Material 3
* Wdrożono dedykowaną, autorską paletę barw wygenerowaną w oparciu o barwę bazową (`#00658B`).
* Wyłączono kolor dynamiczny systemu (`dynamicColor = false`), zapewniając powtarzalność i spójność wizualną na każdym urządzeniu.
* Przypisano semantyczne role kolorystyczne rozróżniające hierarchię elementów:
    * Przycisk `=` -> `primary` / `onPrimary`
    * Operatory arytmetyczne -> `primaryContainer` / `onPrimaryContainer`
    * Przyciski funkcyjne i akcje -> `tertiaryContainer` / `onTertiaryContainer`
    * Funkcje naukowe -> `secondaryContainer` / `onSecondaryContainer`
    * Cyfry -> `surfaceVariant` / `onSurfaceVariant`
* Wszystkie pary kolorów spełniają kryteria kontrastu tekstu do tła WCAG 2.2 na poziomie co najmniej 4.5:1.

---

## Odpowiedzi na pytania z konspektu

### 1. Zapis stałej $\pi$ w polu działania (Zadanie 3)
> **Pytanie:** Dlaczego przycisk $\pi$ powinien wpisywać nazwę stałej, a nie liczbę 3,14159?
>
> **Odpowiedź:** Wpisanie nazwy stałej zachowuje jej symboliczną tożsamość aż do momentu ewaluacji wyrażenia przez silnik obliczeniowy. Pozwala to na podstawienie wartości z maksymalną precyzją zmiennoprzecinkową udostępnianą przez środowisko wykonawcze (np. `Math.PI` o precyzji `Double` lub `BigDecimal`). Wpisanie sztywnego zaokrąglenia (np. `3.14159`) trwale obcina dokładność obliczeń już na etapie wprowadzania danych, co przy operacjach wyższego rzędu (np. funkcje trygonometryczne, potęgowanie) prowadzi do kumulacji błędów numerycznych.

### 2. Uzasadnienie wyboru koloru bazowego (Zadanie 4)
> **Pytanie:** Dlaczego wybrany został ten kolor bazowy?
>
> **Odpowiedź:** Jako kolor bazowy wybrano odcień morskiego błękitu (`#00658B`), ponieważ nawiązuje do estetyki precyzyjnych przyrządów pomiarowych i oprogramowania technicznego, a wyprowadzone z niego tonacje Material 3 zapewniają naturalny kontrast oraz doskonałą czytelność zarówno w trybie jasnym, jak i ciemnym.

---

## Zrzuty ekranu

| Tryb jasny (Pion) | Tryb ciemny (Pion) |
|:---:|:---:|
| ![Jasny](screenshots/portrait_light.png) | ![Ciemny](screenshots/portrait_dark.png) |

| Tryb poziomy (Naukowy) |
|:---:|
| ![Poziomy](screenshots/landscape.png) |

---

## Część 2: Logika obliczeń, testy i dopracowanie (Laboratorium 5)

Druga część projektu zrealizowała pełną obsługę obliczeń z zachowaniem kolejności działań, odporność na błędy, separację logiki od interfejsu oraz szlify wizualne i lokalizację.

### Zrealizowane zadania

1. **Zachowanie stanu przy zmianie orientacji (Zadanie 1):**
  - Zastąpiono `remember` wywołaniem `rememberSaveable`, co zapobiega utracie wpisanego działania po zniszczeniu i ponownym utworzeniu aktywności.
  - W `MainActivity` nadpisano metodę `onSaveInstanceState(outState: Bundle)` i zweryfikowano w dzienniku Logcat (`CalcState`), że stan jest poprawnie przekazywany przez obiekt `Bundle`.
  - *Inne sytuacje niszczenia aktywności:* poza obrotem ekranu aktywność jest niszczona i odtwarzana m.in. przy zmianie rozmiaru okna w trybie wielookienkowym (split-screen), zmianie motywu systemowego (jasny/ciemny) w trakcie działania aplikacji, zmianie języka urządzenia oraz decyzji systemu o zwolnieniu pamięci RAM (process death).

2. **Silnik obliczeniowy i obsługa biblioteki mXparser (Zadanie 2):**
  - Do projektu dołączono bibliotekę `org.mariuszgromada.math:MathParser.org-mXparser:6.1.1` za pośrednictwem katalogu wersji (`libs.versions.toml`).
  - Zadeklarowano niekomercyjny/dydaktyczny charakter użycia biblioteki wywołaniem `License.iConfirmNonCommercialUse(...)`.
  - Zapewniono obsługę złożonych wyrażeń (kolejność działań, potęgowanie, nawiasy, funkcje trygonometryczne i logarytmiczne).

3. **Oddzielenie logiki i testy jednostkowe (Zadanie 3):**
  - Obliczenia wydzielono do niezależnego obiektu `CalculatorEngine`, całkowicie odseparowanego od kontekstu Androida (`android.*`), co umożliwia błyskawiczne uruchamianie testów na maszynie JVM.
  - W pliku `CalculatorTest.kt` zaimplementowano testy JUnit pokrywające tabelę przypadków brzegowych oraz separator dziesiętny:
    - Kolejność działań (`2+2*2` $\rightarrow$ `6`)
    - Obsługa nawiasów (`(2+2)*2` $\rightarrow$ `8`)
    - Dzielenie przez zero (`1/0` $\rightarrow$ czytelny komunikat błędu)
    - Wyrażenia niedokończone (`2+` $\rightarrow$ brak wyniku, bez awarii)
    - Nawiasy niezamknięte (`((2+2)` $\rightarrow$ brak wyniku, bez awarii)
    - Stan początkowy (puste pole $\rightarrow$ brak wyniku i błędu)
    - Dziedzina funkcji (`sqrt(-1)` $\rightarrow$ czytelny komunikat błędu)
    - Duże liczby (`1234567890*1234567890` $\rightarrow$ notacja naukowa, stabilny układ)
    - Separator dziesiętny (obsługa kropki `2.5+1` i przecinka `2,5+1`)

4. **Dopracowanie szczegółów (Zadanie 4):**
  - **Typografia:** Wdrożono cyfry tabelaryczne (`fontFeatureSettings = "tnum"`), eliminując drganie szerokości tekstu przy wprowadzaniu kolejnych cyfr.
  - **Ikona adaptacyjna:** Przygotowano autorską ikonę kalkulatora spójną z paletą barw Material 3 (`#001F2A` / `#78D1FF`), uwzględniającą bezpieczną strefę (*safe zone*) oraz wariant monochromatyczny dla Androida 13+.
  - **Lokalizacja:** Nazwę aplikacji przeniesiono do zasobów (`values/strings.xml` $\rightarrow$ `Calculator`, `values-pl/strings.xml` $\rightarrow$ `Kalkulator`).