package com.example.banruou.data.network

import PhieuDat
import com.example.banruou.data.model.AccessTokenResponse
import com.example.banruou.data.model.LoginRequest
import com.example.banruou.data.model.UserInfoGetMeResponse
import com.example.banruou.data.model.UserResponse
import com.example.banruou.data.model.WineLineResponse
import com.example.banruou.data.model.WineTypeResponse
import com.example.banruou.ui.auth.UserRegister
import com.example.banruou.ui.cart.PhieuDatCart
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): AccessTokenResponse
    @POST("customers")
    suspend fun register(@Body userRegister: UserRegister): Response<Void>
    @GET("auth/me")
    suspend fun getUserInfo(@Header("Authorization") token: String): UserInfoGetMeResponse
    @GET("winelines")
    suspend fun getWineLines(): Response<List<WineLineResponse>>
    @GET("winelines/product/hot")
    suspend fun getWineLinesHot(): Response<List<WineLineResponse>>
    @GET("winelines/product/promo/top")
    suspend fun getWineLinesPromo(): Response<List<WineLineResponse>>
    @GET("winelines/product/name/{name}")
    suspend fun getWineLinesByName(@Path("name") name: String): Response<List<WineLineResponse>>
    @GET("winelines/{id}")
    suspend fun getWineLinesById(@Path("id") name: String): Response<WineLineResponse>
    @GET("winetype")
    suspend fun getWineTypes(): Response<List<WineTypeResponse>>
    @GET("winelines/product/{category}")
    suspend fun getWineLinesByCategory(@Path("category") category: String): Response<List<WineLineResponse>>
    @GET("customers/{id}")
    suspend fun getUserById(@Path("id") id: String): Response<UserResponse>
    @GET("phieudat/list/pd/KH/{id}")
    suspend fun getOrderByCustomer(@Path("id") id: String): Response<List<PhieuDat>>
    @GET("phieudat/{id}")
    suspend fun getOrderDetailByIdOrder(@Path("id") id: String): Response<PhieuDat>
    @POST("phieudat")
    suspend fun createPhieuDat(@Body phieuDat: PhieuDatCart): Response<Unit>
}
