package com.example.hits_android.expressionParser

class TokenType(
    val name: Name,
    val identifier: Identifiers,
    val regex: Regex,
    val priority: Int = -1
)

class Token(val type: TokenType, val text: String)

val tokensList = mutableMapOf<String, TokenType>(
    "rand()" to TokenType(Name.RAND, Identifiers.RESERVED, Regex("^rand\\(\\)")),

    "space" to TokenType(Name.SPACE, Identifiers.SPACES, Regex("^\\s+")),
    "\n" to TokenType(Name.NEW_STRING, Identifiers.SPACES, Regex("^\\n")),

    "[" to TokenType(Name.L_SQUARE_BRACKET, Identifiers.LEFT_BRACKETS, Regex("^\\[")),
    "]" to TokenType(Name.R_SQUARE_BRACKET, Identifiers.RIGHT_BRACKETS, Regex("^]")),
    "(" to TokenType(Name.L_BRACKET, Identifiers.LEFT_BRACKETS, Regex("^\\(")),
    ")" to TokenType(Name.R_BRACKET, Identifiers.RIGHT_BRACKETS, Regex("^\\)")),

    ">=" to TokenType(Name.GREATER_OR_EQUALS, Identifiers.BOOLEAN, Regex("^>="), 3),
    "<=" to TokenType(Name.LESS_OR_EQUALS, Identifiers.BOOLEAN, Regex("^<="), 3),
    ">" to TokenType(Name.GREATER, Identifiers.BOOLEAN, Regex("^>"), 3),
    "<" to TokenType(Name.LESS, Identifiers.BOOLEAN, Regex("^<"), 3),
    "&&" to TokenType(Name.AND, Identifiers.BOOLEAN, Regex("^&&"), 1),
    "||" to TokenType(Name.OR, Identifiers.BOOLEAN, Regex("^\\|\\|"), 2),
    "!=" to TokenType(Name.NOT_EQUALS, Identifiers.BOOLEAN, Regex("^!="), 4),
    "==" to TokenType(Name.EQUALS, Identifiers.BOOLEAN, Regex("^=="), 4),

    "+" to TokenType(Name.PLUS, Identifiers.OPERATORS, Regex("^\\+"), 5),
    "-" to TokenType(Name.MINUS, Identifiers.OPERATORS, Regex("^-(?!\\d)"), 5),
    "*" to TokenType(Name.MULTIPLY, Identifiers.OPERATORS, Regex("^\\*"), 6),
    "/" to TokenType(Name.DIVIDE, Identifiers.OPERATORS, Regex("^/"), 6),
    "%" to TokenType(Name.MOD, Identifiers.OPERATORS, Regex("^%"), 6),

    "double" to TokenType(
        Name.DOUBLE,
        Identifiers.LITERAL,
        Regex("^(((-?[1-9]\\d*)|(-?0))(\\.\\d*))")
    ),
    "number" to TokenType(Name.NUMBER, Identifiers.LITERAL, Regex("^(-?0|-?[1-9]\\d*)")),
    "string" to TokenType(
        Name.STRING,
        Identifiers.VARIABLE,
        Regex("^\"[A-Za-z0-9!\\\"#\$%&'()*+,-./:;\\\\\\\\<=>?@\\\\[\\\\]^_`{|}~ ]*\"")
    ),
    "variable" to TokenType(Name.VARIABLE, Identifiers.VARIABLE, Regex("^(?!true|false)\\w+")),
    "bool" to TokenType(Name.BOOL, Identifiers.LITERAL, Regex("^(true)|(false)|0|1")),

    ";" to TokenType(Name.SEMICOLON, Identifiers.PUNCTUATION, Regex("^;")),
    ":" to TokenType(Name.COLON, Identifiers.PUNCTUATION, Regex("^:")),
    "," to TokenType(Name.COMMA, Identifiers.PUNCTUATION, Regex("^,")),

    "=" to TokenType(Name.ASSIGN, Identifiers.ASSIGN_OPERATORS, Regex("^="))
)
