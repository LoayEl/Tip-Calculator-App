package com.example.tipcalculator

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.tipcalculator.ui.theme.TipCalculatorTheme
import java.text.NumberFormat

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TipCalculatorTheme {
                    TipCalcLayout()
                }
            }
        }
    }


@Composable
fun TipCalcLayout(){
    var amountInput by remember { mutableStateOf("") }
    var tipInput by remember{ mutableStateOf("")}
    var roundUp by remember{ mutableStateOf(false)}

    val amount = amountInput.toDoubleOrNull() ?: 0.0
    val tipPercent = tipInput.toDoubleOrNull() ?: 20.0
    val tip= calculateTip(amount, tipPercent, roundUp)
    Column(
        modifier= Modifier.statusBarsPadding().padding(horizontal = 40.dp).verticalScroll(rememberScrollState()).safeDrawingPadding(),
        horizontalAlignment= Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        Text(
            text= stringResource(R.string.calculate_tip),
            modifier = Modifier.padding(bottom=16.dp, top=40.dp).align(alignment= Alignment.Start)
        )
        EditNumberField(label =R.string.bill_amount,
            keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType=KeyboardType.Number,
                imeAction = ImeAction.Next),
            value = amountInput,
            onValueChange = {amountInput= it},
            Modifier.padding(bottom=32.dp).fillMaxWidth())

        EditNumberField(label =R.string.tip_percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType=KeyboardType.Number,
                imeAction = ImeAction.Next),
            value = tipInput,
            onValueChange = {tipInput= it},
            Modifier.padding(bottom=32.dp).fillMaxWidth())
        RoundTipRow(
            roundUp=roundUp,
            onRoundUpChanged = {roundUp=it},
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        Text(
            text = stringResource(R.string.tip_amount,tip),
            style = MaterialTheme.typography.displaySmall
        )
        Spacer(modifier = Modifier.height(150.dp))
    }
}

@Composable
fun EditNumberField(@StringRes label: Int,
                    keyboardOptions: KeyboardOptions,
                    value:String,
                    onValueChange: (String)-> Unit,
                    modifier: Modifier=Modifier
                    ){
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        label={ Text(stringResource(label))},
        singleLine = true,
        keyboardOptions = keyboardOptions  //KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

@Composable
fun RoundTipRow(
    roundUp:Boolean,
    onRoundUpChanged:(Boolean)-> Unit,
    modifier: Modifier=Modifier
)
{
    Row(
        modifier= modifier.fillMaxWidth().size(48.dp),
        verticalAlignment = Alignment.CenterVertically
    )
    {
        Text(text= stringResource(R.string.round_tip))
        Switch(
            checked=roundUp,
            onCheckedChange=onRoundUpChanged,
            modifier=modifier.fillMaxWidth().wrapContentWidth(Alignment.End)
        )
    }
}
private fun calculateTip(amount:Double, tipPercent:Double=20.0, roundUp:Boolean): String{
    var tip = tipPercent /100 * amount
    if(roundUp){
        tip= kotlin.math.ceil(tip)
    }
    return NumberFormat.getCurrencyInstance().format(tip)
}


@Preview(showBackground = true)
@Composable
fun TipCalcPreview() {
         TipCalculatorTheme {
             TipCalcLayout()
         }
    }
