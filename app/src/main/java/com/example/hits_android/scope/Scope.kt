package com.example.hits_android.scope

import com.example.hits_android.expressionParser.scopes
import com.example.hits_android.expressionParser.variables

// Область видимости переменных
class Scope() {
    // Список всех областей видимости
    private var scopesList = mutableListOf<MutableList<String>>()

    // Добавление текущей области видимости
    fun addScope(scope: MutableList<String>) {
        scopesList.add(scope)
    }

    // Уничтожение текущей области видимости
    fun destroyScope() {
        // Уничтожение переменных, объявленных в текущем блоке кода
        for (name in variables.keys) {
            if (name !in scopes.getScope()) {
                variables.remove(name)
            }
        }

        scopesList.removeAt(scopesList.size - 1)
    }

    // Возврат текущей области видимости
    fun getScope(): MutableList<String> {
        if (scopesList.size == 0) {
            return MutableList<String>(0) { "" }
        }
        return scopesList[scopesList.size - 1]
    }
}