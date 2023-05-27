package com.example.hits_android.expressionParser

enum class Identifiers(val operator: String) {

    RESERVED("reserved"),
    VARIABLE("variable"),
    SPACES("spaces"),
    OPERATORS("operators"),
    BOOLEAN("boolean"),
    LITERAL("literal"),
    PUNCTUATION("grammar"),
    ASSIGN_OPERATORS("assignOperators"),
    LEFT_BRACKETS("leftbrackets"),
    RIGHT_BRACKETS("rightbrackets")

}