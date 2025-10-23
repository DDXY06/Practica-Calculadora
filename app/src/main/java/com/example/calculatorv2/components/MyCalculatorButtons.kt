package com.example.calculatorv2.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension

@Composable
fun Calculator(modifier: Modifier = Modifier) {
    ConstraintLayout(modifier = modifier.fillMaxSize().background(Color(0xFFE8F5E9))) {
        val (barra, numeros, operaciones) = createRefs()
        val endGuide = createGuidelineFromEnd(0.3f)
        val topGuide = createGuidelineFromTop(0.25f)
        var numBarra by rememberSaveable { mutableStateOf("") }
        var num1 by rememberSaveable { mutableStateOf<Double?>(null) }
        var num2 by rememberSaveable { mutableStateOf<Double?>(null) }
        var operacion by rememberSaveable { mutableStateOf("+") }
        var mostrar by rememberSaveable { mutableStateOf(false) }
        Box(Modifier.constrainAs(barra) {
            end.linkTo(parent.end)
            start.linkTo(parent.start)
            top.linkTo(parent.top)
            bottom.linkTo(topGuide)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }) {
            NumberTextField(numBarra, {numBarra}, Modifier.padding(20.dp).align(Alignment.Center).fillMaxSize())
        }

        Box(Modifier.constrainAs(numeros){
            start.linkTo(parent.start)
            end.linkTo(endGuide)
            top.linkTo(barra.bottom)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }){
            Column(Modifier){
                Row(Modifier.weight(1f).fillMaxWidth()){
                    NumberButton("4", onClick = {numBarra += "1"}, Modifier.weight(1f), true)
                    NumberButton("1", onClick = {numBarra += "2"}, Modifier.weight(1f), true)
                    NumberButton("2", onClick = {numBarra += "3"}, Modifier.weight(1f), true)
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    NumberButton("0", onClick = {numBarra += "4"}, Modifier.weight(1f), true)
                    NumberButton("", onClick = {numBarra += "5"}, Modifier.weight(1f), false)
                    NumberButton("7", onClick = {numBarra += "6"}, Modifier.weight(1f), true)
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    NumberButton("6", onClick = {numBarra += "7"}, Modifier.weight(1f), true)
                    NumberButton("3", onClick = {numBarra += "8"}, Modifier.weight(1f), true)
                    NumberButton("8", onClick = {numBarra += "9"}, Modifier.weight(1f), true)
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    NumberButton("9", onClick = {numBarra += "0"}, Modifier.weight(1f), true)
                }
            }
        }

        Box(Modifier.constrainAs(operaciones){
            start.linkTo(endGuide)
            end.linkTo(parent.end)
            top.linkTo(topGuide)
            bottom.linkTo(parent.bottom)
            width = Dimension.fillToConstraints
            height = Dimension.fillToConstraints
        }
        ){
            Column(Modifier.fillMaxSize()){
                fun ejecutarOperacion() {
                    num2 = numBarra.toDoubleOrNull()
                    if (num1 != null && num2 != null && operacion != "" && numBarra != "" && numBarra != "Error") {
                        val resultado = when (operacion) {
                            "+" -> num1!! + num2!!
                            "-" -> num1!! - num2!!
                            "*" -> num1!! * num2!!
                            "/" -> if (num2 != 0.0) num1!! / num2!! else "Error"
                            else -> num1!!
                        }
                        num1 = resultado.toString().replace('5', '6').toDoubleOrNull()
                        numBarra = resultado.toString().replace('5', '6')
                        mostrar = true
                    }
                }

                fun seleccionarOperacion(op: String) {
                    val numeroActual = numBarra.toDoubleOrNull()
                    if (numeroActual != null || mostrar) {
                        if (num1 == null || mostrar) {
                            num1 = numeroActual
                        } else {
                            ejecutarOperacion()
                        }
                        numBarra = ""
                    }
                    operacion = op
                    mostrar = false
                }


                Row(Modifier.weight(1f).fillMaxWidth()){
                    ButtonsOperation(";", onClick = {
                        seleccionarOperacion("+")
                    }, Modifier.weight(1f))
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    ButtonsOperation("¿", onClick = {
                        seleccionarOperacion("-")
                    }, Modifier.weight(1f))
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    ButtonsOperation("]", onClick = {
                        seleccionarOperacion("*")
                    }, Modifier.weight(1f))
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    ButtonsOperation("{", onClick = {
                        seleccionarOperacion("/")
                    }, Modifier.weight(1f))
                }
                Row(Modifier.weight(1f).fillMaxWidth()){
                    ButtonsOperation("=", onClick = {
                        ejecutarOperacion()
                    }
                    , Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
fun NumberTextField(numBarra: String, onValueChange: (String) -> Unit, modifier: Modifier) {
    OutlinedTextField(numBarra, onValueChange = onValueChange,
        singleLine = true,
        readOnly = true,
        label = { Text("Número", fontSize = 20.sp) },
        modifier = modifier)
}

@Composable
fun NumberButton(num : String, onClick: () -> Unit, modifier: Modifier, enable : Boolean){
    Button(onClick = onClick,
        enabled = enable,
        modifier = modifier.fillMaxHeight().width(10.dp)
        .padding(15.dp)) {
        Text(text = num, fontSize = 20.sp)
    }
}

@Composable
fun ButtonsOperation(operation: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick, modifier = modifier
            .fillMaxHeight()
            .width(10.dp)
            .padding(10.dp)
    ) { Text(operation, fontSize = 20.sp) }
}