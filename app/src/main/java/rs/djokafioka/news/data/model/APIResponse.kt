package rs.djokafioka.news.data.model


import com.google.gson.annotations.SerializedName

data class APIResponse(
    @SerializedName("articles")
    var articles: List<Article>, //Changed from val to var for manual pagination
    @SerializedName("status")
    val status: String,
    @SerializedName("totalResults")
    val totalResults: Int
)