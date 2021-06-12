package exercise.android.nami.sandwiches

import java.io.Serializable
import java.util.*

data class Order (
    var id: String = UUID.randomUUID().toString(),
    var customerName: String = "",
    var pickles: Int = 0,
    var hummus: Boolean = false,
    var tahini: Boolean = false,
    var comment:  String = "",
    var status:  String = ""
) : Serializable

