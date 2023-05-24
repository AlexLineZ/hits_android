package com.example.hits_android

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.hits_android.blocks.FunctionClass
import com.example.hits_android.model.ReorderListViewModel

@Composable
fun FunctionSelection(vm: ReorderListViewModel) {
    LazyRow(
        modifier = Modifier
            .fillMaxHeight(0.08f)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(20.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        items(vm.functionsList) { item ->
            FunctionListItem(vm, item)
        }
        item {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary)
                    .clickable {
                        vm.addFunction()

                        vm.setCurrentScreenId(vm.functionsList.size - 1)

                        Log.d("a", "${vm.functionsList}")
                    },
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = "addButton",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun FunctionListItem(vm: ReorderListViewModel, item: FunctionClass) {

    when (item.id) {
        0 -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                    .clickable {
                        vm.setCurrentScreenId(item.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "main",
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
            }
        }

        else -> {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.8f)
                    .clip(shape = RoundedCornerShape(24.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.95f))
                    .clickable {
                        vm.setCurrentScreenId(item.id)
                    },
                contentAlignment = Alignment.Center
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(horizontal = 15.dp)
                ) {
                    Text(
                        text = "function",
                        color = MaterialTheme.colorScheme.onPrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_close),
                        contentDescription = "closeFunIcon",
                        tint = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .fillMaxHeight(0.7f)
                            .clickable {
                                vm.setCurrentScreenId(0)

                                vm.functionsList =
                                    vm.functionsList.toMutableList().apply {
                                        removeIf { it.id == item.id }
                                    }
                            }
                    )
                }
            }
        }
    }
}