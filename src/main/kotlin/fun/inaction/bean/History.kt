package `fun`.inaction.bean

import org.bson.types.ObjectId

data class History(
    val _id:ObjectId,
    val userID:String,
    val parkID:String,
    val parkName:String,
    val timestamp:Long
)
