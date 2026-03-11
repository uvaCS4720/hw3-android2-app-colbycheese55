package edu.nd.pmcburne.hwapp.one

enum class GameState(val value: String) {
    FUTURE("future"),
    Q1("1st Quarter"),
    Q2("2nd Quarter"),
    Q3("3rd Quarter"),
    Q4("4th Quarter"),
    H1("1st Half"),
    H2("2nd Half"),
    DONE("FINAL"),
    ERROR("ERROR")
}