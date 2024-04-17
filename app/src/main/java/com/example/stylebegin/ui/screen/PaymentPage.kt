package com.example.stylebegin.ui.screen

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.stylebegin.Utils
import com.example.stylebegin.ui.api.ApiUtilities
import com.example.stylebegin.ui.components.PaymentTextField
import com.stripe.android.model.ConfirmSetupIntentParams
import com.stripe.android.model.PaymentMethod
import com.stripe.android.model.PaymentMethodCreateParams
import com.stripe.android.payments.paymentlauncher.PaymentLauncher
import com.stripe.android.payments.paymentlauncher.PaymentResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

lateinit var customerid:String
lateinit var ephemeralKey:String
lateinit var paymentIntentId:String

@Composable
fun PaymentPage(price:String) {
    val context= LocalContext.current
    var cardName by remember { mutableStateOf("") }
    var cardNumber by remember { mutableStateOf("") }
    var expiryMonth by remember { mutableStateOf("") }
    var expiryYear by remember { mutableStateOf("") }
    var cvc by remember { mutableStateOf("") }
    val paymentLauncher =PaymentLauncher.Companion.createForCompose(
        publishableKey = Utils.PUBLISHABLE_KEY,

    ){paymentResult ->
        when(paymentResult){
            is PaymentResult.Completed -> {
                Toast.makeText(context,"Successful",Toast.LENGTH_SHORT).show()
            }
            is PaymentResult.Canceled -> {
                Toast.makeText(context,"Cancelled",Toast.LENGTH_SHORT).show()
            }

            is PaymentResult.Failed ->{
                Toast.makeText(context,paymentResult.throwable.message,Toast.LENGTH_SHORT).show()
            }
        }

    }

    Surface(modifier = Modifier.padding(16.dp)) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Payment Information",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(bottom = 16.dp)
            )

            PaymentTextField(
                value = cardName,
                onValueChange = { cardName = it },
                label = "Cardholder Name",
                placeholder = "John"
            )

            Spacer(modifier = Modifier.height(16.dp))

            PaymentTextField(
                value = cardNumber,
                onValueChange = { cardNumber = it },
                label = "Enter your card number",
                placeholder = "XXXX XXXX XXXX XXXX",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))



                PaymentTextField(
                    value = expiryMonth,
                    onValueChange = { expiryMonth = it },
                    label = "Exp. Month",
                    placeholder = "MM",
                    keyboardType = KeyboardType.Number,
                    maxLength = 2,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PaymentTextField(
                    value = expiryYear,
                    onValueChange = { expiryYear = it },
                    label = "Exp. Year",
                    placeholder = "YYYY",
                    keyboardType = KeyboardType.Number,
                    maxLength = 4 ,
                    modifier = Modifier.fillMaxWidth(0.5f)
                )


            Spacer(modifier = Modifier.height(16.dp))


            PaymentTextField(
                value = cvc,
                onValueChange = { cvc = it },
                label = "CVV/CVC",
                placeholder = "123",
                keyboardType = KeyboardType.Number
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val name= PaymentMethod.BillingDetails(
                        name = cardName
                    )
                    val paymentMethod=PaymentMethodCreateParams.create(
                        PaymentMethodCreateParams.Card.Builder()
                            .setNumber(cardNumber)
                            .setExpiryMonth(expiryMonth.toInt())
                            .setExpiryYear(expiryYear.toInt())
                            .setCvc(cvc)
                            .build(),
                        name
                    )
                    val confirmParam= ConfirmSetupIntentParams.create(
                        paymentMethod,
                        "pi_3P6SbwBJft6JSmTu0f4tdv5o_secret_OluRjdzvGjcFWOjeH78SOcvBv"
                    )
                    CoroutineScope(Dispatchers.IO).launch {
                        paymentLauncher.confirm(confirmParam)
                    }
//                    getCusomterId(price,context)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Pay Now")
            }
        }
    }
}

fun getCusomterId(price: String, context: Context) {
    val apiInterface = ApiUtilities.getApiInterface()
    CoroutineScope(Dispatchers.IO).launch {
        try {

            val response = apiInterface.createCustomer()

            if (response.isSuccessful&&response.body()!=null) {
                Toast.makeText(context, "Customer created!", Toast.LENGTH_SHORT).show()
                customerid=response.body()!!.id
                getEphermeralKey(customerid,price,context)

            } else {

                val message = response.errorBody()?.string() ?: "Failed to create customer"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}

fun getEphermeralKey(customerid: String, price: String, context: Context) {
    val apiInterface = ApiUtilities.getApiInterface()
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiInterface.getEphemeralKey(customerid)

            if (response.isSuccessful&&response.body()!=null) {
                Toast.makeText(context, "Ephemeral Confirmed!", Toast.LENGTH_SHORT).show()
                ephemeralKey=response.body()!!.id
                getPaymentIntent(customerid, ephemeralKey,price ,context)

            } else {

                val message = response.errorBody()?.string() ?: "Failed to create ephemeral key"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}

fun getPaymentIntent(customerid: String, ephemeralKey: String, amount: String, context: Context) {
    val apiInterface = ApiUtilities.getApiInterface()
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiInterface.getPaymentIntent(customer = customerid,amount=amount)

            if (response.isSuccessful&&response.body()!=null) {
                Toast.makeText(context, "Payment intent Confirmed!", Toast.LENGTH_SHORT).show()
                paymentIntentId=response.body()!!.id
                paymentConfirmed(paymentIntentId,context)

            } else {

                val message = response.errorBody()?.string() ?: "Failed to create payment id"
                Toast.makeText(context, message, Toast.LENGTH_SHORT).show()

            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}

fun paymentConfirmed(paymentIntentId: String, context: Context) {
    val apiInterface = ApiUtilities.getApiInterface()
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiInterface.getPaymentConfirm(paymentIntentId)

            if (response.isSuccessful&&response.body()!=null) {
                Toast.makeText(context, "Payment Confirmed!", Toast.LENGTH_SHORT).show()

            } else {

                val message = response.errorBody()?.string() ?: "Failed to confrim payment"
                Toast.makeText(context, message, Toast.LENGTH_SHORT)

            }
        } catch (e: Exception) {

            e.printStackTrace()
        }
    }
}
