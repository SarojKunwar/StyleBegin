package com.example.stylebegin.ui.api

import com.example.stylebegin.Utils.SECRET_KEY
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {
    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("v1/customers")
    suspend fun createCustomer(

    ): Response<CustomerResponse>

    @Headers(
        "Authorization: Bearer $SECRET_KEY",
        "Stripe-Version: 2024-04-10"
    )
    @POST("v1/ephemeral_keys")
    suspend fun getEphemeralKey(
        @Query("customer")
        customer: String
    ): Response<CustomerResponse>

    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("v1/payment_intents")
    suspend fun getPaymentIntent(
        @Query("customer")
        customer: String,
        @Query("amount")
        amount: String,
        @Query("currency")
        currency: String="CAD",
        @Query("automatic_payment_methods[enabled]")
        automatePay: Boolean = true
    ): Response<CustomerResponse>

    @Headers("Authorization: Bearer $SECRET_KEY")
    @POST("v1/payment_intents/{paymentIntentId}/confirm")
    suspend fun getPaymentConfirm(
        @Path("paymentIntentId") paymentIntentId: String,
        @Field("payment_method") paymentMethod: String="pm_card_visa"
    ): Response<CustomerResponse>

}


data class CustomerResponse(
    val id: String,

    )


data class ChargeRequest(
    val receipt_email: String,
    val amount: Int,
    val currency: String = "CAD",
    val card: String,
    val customer: String
)

data class ChargeResponse(
    val id: String,
    val client_secret: String
)