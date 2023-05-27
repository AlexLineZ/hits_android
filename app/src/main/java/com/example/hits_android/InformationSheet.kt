package com.example.hits_android

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun InformationSheetComposable() {
    var showSheet by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(vertical = 15.dp)
    ) {
        Text(
            text = "Information:",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .height(70.dp)
                    .fillMaxWidth()
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        showSheet = true
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Help",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 30.sp,
                    maxLines = 1
                )
            }
        }
    }
    if (showSheet) {
        BottomSheet() {
            showSheet = false
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheet(onDismiss: () -> Unit) {
    val modalBottomSheetState = rememberModalBottomSheetState()

    ModalBottomSheet(
        onDismissRequest = { onDismiss() },
        sheetState = modalBottomSheetState,
        dragHandle = { BottomSheetDefaults.DragHandle() },
    ) {
        HelpList()
    }
}

@Composable
fun HelpList() {
    val helps = listOf(
        "DOCUMENTATION FOR OUR DEAR USERS: ",
        "To select a block, press the leftmost button in the bottom navigation bar:",
        "InitVar:\n" +
                "Select the variable type and specify its name and initial value, which can be cast to the selected type.",
        "InitArray:\n" +
                "Select the type of array elements and specify its name and non-negative integer size.",
        "Input:\n" +
                "Specify the name of the variable to which the value entered by the user through the console will be assigned.",
        "Print:\n" +
                "Specify a value or expression whose result will be printed to the console. You can list several values separated by commas, and they will be displayed in the console sequentially separated by a space.",
        "Assign:\n" +
                "Specify the name of the variable to which you want to assign a new value and the new value of the variable. When assigning one variable to another, the value of the assigned variable is copied.",
        "If:\n" +
                "Specify a condition (an expression that evaluates to true or false). The blocks between Begin and End following the If are executed if the condition evaluates to true.",
        "Else:\n" +
                "The block must come after the body of the If condition. The blocks between Begin and End following Else are executed if the If condition evaluates to false.",
        "While:\n" +
                "Specify a condition (an expression that evaluates to true or false). Blocks between Begin and End following While are executed as long as the condition evaluates to true.",
        "CallFun:\n" +
                "Specify the name of the called function and list the arguments passed to it in the format <value1>, <value2>.",
        "Return:\n" +
                "A block that terminates the execution of the current function.",
        "Break:\n" +
                "The block that ends the execution of the current loop.",
        "Continue:\n" +
                "Go to the next iteration of the loop.\n",

        "DATA TYPES:",

        "Int - integer, Double is cast to it implicitly.",

        "Double - real number, Int is implicitly cast into it.",

        "Bool - boolean type, takes values true or false.",

        "String - string, takes values in \"text\" format, it is implicitly converted to Char.",

        "Char - a character, takes values in the 'g' format, and also casts an Int to it (translated into a character according to the ASCII table).\n",

        "STRUCT\n" +
                "To create a structure, use initVar to create a structure field in the format <structure name>.<field name> = <value>. Field type is determined automatically.",
        "To add a new structure field, you must use Assign to assign some value to a structure field that does not yet exist: <struct name>.<new field name> = <value>.",
        "You can also copy a structure using Assign: <structure name1> = <structure name2>.\n",

        "ARRAY\n" +
                "The type of an array is the type of its element, with the \"Array\" suffix. For example IntArray, BoolArray, StringArray, StructArray",

        "An array element is accessed through an index inside square brackets arr[index]",

        "It is also possible to take a character from a string using square brackets with an index, " +
                "but the types of the string and character array are not equal and are not reducible. No arrays are reducible to each other.",

        "Example: Let there be an array of structures structArr, the first element of which is a structure with a field field of type StringArray, " +
                "whose first element is \"hello world\". Then structArr[0].field[0][0] " +
                "- taking the first character of a string that is in an array of strings, which is a field of a structure that is in an array of structures.\n",

        "FUNCTION CREATE\n" +
                "To create a new function, click on the (+) in the top bar of the development screen." +
                " In the first block, enter a unique function name, " +
                "and in the second block, list the accepted parameters in the format <parameter name1>:<parameter type1>, <parameter name2>:<parameter type2>.",

        "SCOPE\n" +
                "Functions only see variables created within themselves and their parameters." +
                " Outer blocks do not see variables created in inner blocks (blocks are allocated with start program and finish program, start function and finish function, begin and end).",

        "SAVE PROGRAM\n" +
                "To save, go to the settings window, click \"Save\" and enter a unique name for the file with the program.",

        "LOAD SAVE\n" +
                "To load the program, go to the settings window, click \"Load\" and select the file to load.",

        "THEME SELECT\n" +
                "In the settings window, select the lightness of the theme and its main colors, switching them by clicking on the top buttons.",

        "START PROGRAM\n" +
                "To launch, press the rightmost button on the bottom navigation bar."
    )

    LazyColumn {
        items(helps) { help ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 20.dp)
            ) {
                Text(
                    text = help,
                    modifier = Modifier.padding(end = 20.dp)
                )
            }
        }
    }
}