package com.example.banruou.data.model
import com.google.gson.annotations.SerializedName
data class UserInfoGetMeResponse(
    val userId: String,
    val role: String,
    val username: String
)

data class UserResponse(
    @SerializedName("role")
    val role: Role?,
    @SerializedName("phieudats")
    val phieuDatList: List<PhieuDat>?,
    @SerializedName("MAKH")
    val maKH: String?,
    @SerializedName("HO")
    val ho: String?,
    @SerializedName("TEN")
    val ten: String?,
    @SerializedName("GIOITINH")
    val gioiTinh: String?,
    @SerializedName("NGAYSINH")
    val ngaySinh: String?,
    @SerializedName("DIACHI")
    val diaChi: String?,
    @SerializedName("SDT")
    val sdt: String?,
    @SerializedName("EMAIL")
    val email: String?,
    @SerializedName("USERNAME")
    val userName: String?,
    @SerializedName("PASSWORD")
    val password: String?,
    @SerializedName("MANQ")
    val maNQ: String?
)

data class Role(
    @SerializedName("customers")
    val customers: Any?,
    @SerializedName("staffs")
    val staffs: Any?,
    @SerializedName("manq")
    val manq: String?,
    @SerializedName("tennq")
    val tennq: String?
)

data class PhieuDat(
    @SerializedName("customer")
    val customer: Any?,
    @SerializedName("staff")
    val staff: Any?,
    @SerializedName("ct_phieudats")
    val ctPhieuDats: Any?,
    @SerializedName("bill")
    val bill: Any?,
    @SerializedName("MAPD")
    val maPD: String?,
    @SerializedName("NGAYDAT")
    val ngayDat: String?,
    @SerializedName("HONN")
    val hoNN: String?,
    @SerializedName("TENNN")
    val tenNN: String?,
    @SerializedName("DIACHINN")
    val diaChiNN: String?,
    @SerializedName("SDTNN")
    val sdtNN: String?,
    @SerializedName("GHICHU")
    val ghiChu: String?,
    @SerializedName("TRANGTHAI")
    val trangThai: String?,
    @SerializedName("MANVD")
    val maNVD: String?,
    @SerializedName("MANVGH")
    val maNVGH: String?,
    @SerializedName("MAKH")
    val maKH: String?
)
