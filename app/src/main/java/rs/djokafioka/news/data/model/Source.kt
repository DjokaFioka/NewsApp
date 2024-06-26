package rs.djokafioka.news.data.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Source(
    @SerializedName("id")
    val id: String?,
    @SerializedName("name")
    val name: String?
) : Serializable