data class Customer(
    val role: String? = null,
    val phieudats: List<PhieuDat>? = null,
    val MAKH: String,
    val HO: String,
    val TEN: String,
    val GIOITINH: String,
    val NGAYSINH: String,
    val DIACHI: String,
    val SDT: String,
    val EMAIL: String,
    val USERNAME: String,
    val PASSWORD: String,
    val MANQ: String
)

data class Staff(
    val role: String? = null,
    val phieudats: List<PhieuDat>? = null,
//    val changeprices: List<ChangePrice>? = null,
    val bills: List<Bill>? = null,
    val MANV: String,
    val HO: String,
    val TEN: String,
    val GIOITINH: String,
    val NGAYSINH: String,
    val DIACHI: String,
    val SDT: String,
    val EMAIL: String,
    val USERNAME: String,
    val PASSWORD: String,
    val MANQ: String
)

data class Ct_PhieuDat(
    val phieudat: PhieuDat? = null,
    val IDCTPD: Int,
    val MAPD: String,
    val MADONG: String,
    val SOLUONG: Int,
    val GIA: Double
)

data class Bill(
    val phieudat: PhieuDat? = null,
    val staff: Staff? = null,
    val MAHD: String,
    val NGAY: String,
    val THANHTIEN: Double,
    val MASOTHUE: String,
    val MANV: String,
    val MAPD: String
)

data class PhieuDat(
    val customer: Customer,
    val staff: Staff,
    val ct_phieudats: List<Ct_PhieuDat>,
    val bill: Bill,
    val MAPD: String,
    val NGAYDAT: String,
    val HONN: String,
    val TENNN: String,
    val DIACHINN: String,
    val SDTNN: String,
    val GHICHU: String? = null,
    val TRANGTHAI: String,
    val MANVD: String? = null,
    val MANVGH: String? = null,
    val MAKH: String
)