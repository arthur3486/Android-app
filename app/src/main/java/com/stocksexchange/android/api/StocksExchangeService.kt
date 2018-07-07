package com.stocksexchange.android.api

import com.stocksexchange.android.api.model.*
import retrofit2.Call
import retrofit2.http.*

interface StocksExchangeService {


    /**
     * Gets information about an account.
     */
    @FormUrlEncoded
    @POST("./")
    fun getUser(
        @Field("method") method: String,
        @Field("nonce") nonce: String
    ): Call<ApiResponse<User>>


    /**
     * Creates an order with the specified
     * parameters.
     */
    @FormUrlEncoded
    @POST("./")
    fun createOrder(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("pair") marketName: String,
        @Field("type") tradeType: String,
        @Field("amount") amount: String,
        @Field("rate") rate: String
    ): Call<ApiResponse<OrderResponse>>


    /**
     * Cancels a specific order specified by
     * order_id parameter.
     */
    @FormUrlEncoded
    @POST("./")
    fun cancelOrder(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("order_id") orderId: Long
    ): Call<ApiResponse<OrderResponse>>


    /**
     * Retrieves user's active orders.
     */
    @FormUrlEncoded
    @POST("./")
    fun getActiveOrders(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("pair") marketName: String,
        @Field("type") tradeType: String,
        @Field("owner") owner: String,
        @Field("order") order: String,
        @Field("count") count: Int
    ): Call<ApiResponse<Map<String, Order>>>


    /**
     * Retrieves user's history orders
     * (such as completed or cancelled orders).
     */
    @FormUrlEncoded
    @POST("./")
    fun getHistoryOrders(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("pair") marketName: String,
        @Field("status") statusId: Int,
        @Field("owner") owner: String,
        @Field("order") order: String,
        @Field("count") count: Int
    ): Call<ApiResponse<Map<String, Order>>>


    /**
     * Retrieves user's transactions (deposits
     * and withdrawals).
     */
    @FormUrlEncoded
    @POST("./")
    fun getTransactions(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("currency") currency: String,
        @Field("operation") operation: String,
        @Field("status") status: String,
        @Field("order") order: String,
        @Field("count") count: Int
    ): Call<ApiResponse<Map<String, Map<Long, Transaction>>>>


    /**
     * Retrieves detailed information about a specific currency deposit.
     */
    @FormUrlEncoded
    @POST("./")
    fun getDepositInfo(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("currency") currency: String
    ): Call<ApiResponse<Deposit>>


    /**
     * Generates a wallet address for a specific currency.
     */
    @FormUrlEncoded
    @POST("./")
    fun generateWalletAddress(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("currency") currency: String
    ): Call<ApiResponse<Deposit>>


    /**
     * Creates a ticket.
     */
    @FormUrlEncoded
    @POST("./")
    fun createTicket(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("ticket_category_id") categoryId: Int,
        @Field("currency_name") currency: String?,
        @Field("subject") subject: String,
        @Field("message") message: String
    ): Call<ApiResponse<TicketCreationResponse>>


    /**
     * Sends a reply to a ticket.
     */
    @FormUrlEncoded
    @POST("./")
    fun replyToTicket(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("ticket_id") ticketId: Long,
        @Field("message") message: String
    ): Call<ApiResponse<TicketReplyResponse>>


    /**
     * Retrieves user's tickets.
     */
    @FormUrlEncoded
    @POST("./")
    fun getTickets(
        @Field("method") method: String,
        @Field("nonce") nonce: String,
        @Field("ticket_id") ticketId: Long?,
        @Field("status") statusId: Int?,
        @Field("category") categoryId: Int?
    ): Call<ApiResponse<Map<Long, Ticket>>>


    /**
     * Retrieves the recommended retail exchange rates
     * for all currency pairs.
     */
    @GET("ticker")
    fun getCurrencyMarkets(): Call<List<CurrencyMarket>>


    /**
     * Retrieves detailed information about the specific
     * currency market.
     */
    @GET("market_summary/{currency}/{market}")
    fun getCurrencyMarketSummary(
        @Path("currency") currency: String,
        @Path("market") market: String
    ): Call<CurrencyMarketSummary>


    /**
     * Retrieves data for charts.
     */
    @GET("grafic_public")
    fun getChartData(
        @Query("pair") marketName: String,
        @Query("interval") interval: String,
        @Query("order") order: String,
        @Query("count") count: Int,
        @Query("page") page: Int
    ): Call<ApiResponse<ChartData>>


    /**
     * Retrieves public orders for a specific currency market.
     */
    @GET("trades")
    fun getPublicOrders(
        @Query("pair") marketName: String
    ): Call<ApiResponse<List<Order>>>


    /**
     * Retrieves all available currencies.
     */
    @GET("currencies")
    fun getCurrencies(): Call<List<Currency>>


}