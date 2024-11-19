package edu.farmingdale.threadsexample

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

//@Composable
//fun FibonacciDemoNoBgThrd() {
//    var answer by remember { mutableStateOf("") }
//    var textInput by remember { mutableStateOf("40") }
//
//    Column (
//        modifier = Modifier
//            .padding(40.dp)
//    ) {
//        Row {
//            TextField(
//                value = textInput,
//                onValueChange = { textInput = it },
//                label = { Text("Number?") },
//                singleLine = true,
//                keyboardOptions = KeyboardOptions(
//                    keyboardType = KeyboardType.Number
//                )
//            )
//            Button(onClick = {
//                val num = textInput.toLongOrNull() ?: 0
//                val fibNumber = fibonacci(num)
//                answer = NumberFormat.getNumberInstance(Locale.US).format(fibNumber)
//            }) {
//                Text("Fibonacci")
//            }
//        }
//
//        Text("Result: $answer")
//    }
//}

// Time comp is O(2^n) = no good
// Okay for small calculations
suspend fun fibonacciSuspend(n: Long): Long {
    delay(10)
    return if (n <= 1) n else fibonacciSuspend(n - 1) + fibonacciSuspend(n - 2)
}


// Faster time comp and space comp O(n)
// Iterative method
// Bottom-up dynamic prog

//suspend fun fibonacciSuspend(n: Long): Long {
//    delay(10)
//    if (n <= 1) return n
//    var a = 0L
//    var b = 1L
//    var c = 0L
//    for (i in 2..n) {
//        c = a + b
//        a = b
//        b = c
//    }
//    return c
//}

@Preview(showBackground = true)
@Composable
fun FibonacciDemoWithCoroutine(
    modifier: Modifier = Modifier
) {
    var answer by remember { mutableStateOf("") }
    var textInput by remember { mutableStateOf("") }
    var fibonnaciJob: Job by remember { mutableStateOf(Job()) }
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .padding(top = 50.dp)
    ) {
        Row {
            TextField(
                value = textInput,
                onValueChange = { textInput = it },
                label = { Text("Number?") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
            Button(modifier = Modifier.padding(1.dp),
            onClick = {
                fibonnaciJob = coroutineScope.launch(Dispatchers.Default) {
                    val fibNumber = fibonacciSuspend(textInput.toLong())
                    answer = NumberFormat.getNumberInstance(Locale.US).format((fibNumber))
                }
            }
        ) {
                Text("Fibonacci")
            }
        }

        Text("Result: $answer")
    }
}