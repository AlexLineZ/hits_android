package com.example.hits_android.blocks

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
    fun destoryScope() {
        scopesList.removeAt(scopesList.size - 1)
    }

    // Возврат текущей области видимости
    fun getScope(): MutableList<String> {
        if (isEmpty()) {
            return MutableList<String>(0){""}
        }

        return scopesList[scopesList.size - 1]
    }

    // Проверка списка областей видимости на пустоту
    fun isEmpty(): Boolean {
        return scopesList.size == 0
    }
}