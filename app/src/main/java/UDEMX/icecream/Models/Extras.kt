package UDEMX.icecream.Models

data class Extras(
    val type: String,
    val required: Boolean,
    val items: List<Item>
)
