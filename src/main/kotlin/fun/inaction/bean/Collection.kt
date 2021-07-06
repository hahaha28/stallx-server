package `fun`.inaction.bean

import org.bson.types.ObjectId

data class Collection(
    val _id:ObjectId,
    val userID:String,
    val parkID:String,
    val parkName:String,
    val timestamp:Long
)
