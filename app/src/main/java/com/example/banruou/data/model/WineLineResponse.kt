package com.example.banruou.data.model

data class WineLineResponse(
    val MADONG: String,
    val TENDONG: String,
    val TRANGTHAI: String,
    val HINHANH: String,
    val MOTA: String,
    val CHITIET: String,
    val SOLUONGTON: Int,
    val MALOAI: String,
    val MATH: String,
    val reviews: List<Review>,
    val ct_phieudats: List<CtPhieudat>,
    val changeprices: List<ChangePrice>,
    val trademark: Trademark,
    val winetype: Winetype,
    val ct_khuyenmais: List<CtKhuyenmai>,
    val ct_phieunhaps: List<Any>, // Placeholder for a future data class
    val ct_orders: List<CtOrder>,
    val cungcaps: List<Cungcap>
)

data class Review(
    val customer: Any?, // Placeholder for a future data class
    val wineline: Any?, // Placeholder for a future data class
    val MAKH: String?,
    val MADONG: Any?, // Placeholder for a future data class
    val NGAYDANHGIA: String,
    val NOIDUNG: String,
    val RATING: Int
)

data class CtPhieudat(
    val phieudat: Any?, // Placeholder for a future data class
    val wineline: Any?, // Placeholder for a future data class
    val ct_phieutras: Any?, // Placeholder for a future data class
    val IDCTPD: Int,
    val MAPD: String,
    val MADONG: String,
    val SOLUONG: Int,
    val GIA: Double
)

data class ChangePrice(
    val wineline: Any?, // Placeholder for a future data class
    val staff: Any?, // Placeholder for a future data class
    val MADONG: String,
    val NGAYTHAYDOI: String,
    val MANV: String,
    val GIA: Double
)

data class Trademark(
    val winelines: Any?, // Placeholder for a future data class
    val MATH: String,
    val TENTH: String
)

data class Winetype(
    val winelines: Any?, // Placeholder for a future data class
    val MALOAI: String,
    val TENLOAI: String
)

data class CtKhuyenmai(
    val promotion: Any?, // Placeholder for a future data class
    val wineline: Any?, // Placeholder for a future data class
    val MAKM: String,
    val MADONG: String,
    val PHANTRAMGIAM: Double // Changed to Double for consistency in calculations
)

data class CtOrder(
    val wineline: Any?, // Placeholder for a future data class
    val order: Any?, // Placeholder for a future data class
    val MADONG: String,
    val MADDH: String,
    val SOLUONG: Int,
    val GIA: Double
)

data class Cungcap(
    val provider: Any?, // Placeholder for a future data class
    val wineline: Any?, // Placeholder for a future data class
    val MANCC: String,
    val MADONG: String,
    val GIA: Double
)
data class WineTypeDetailResponse(
    val winelines: List<WineLineResponse>,
    val MALOAI: String,
    val TENLOAI: String
)

