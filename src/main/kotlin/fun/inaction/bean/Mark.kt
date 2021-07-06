package `fun`.inaction.bean

import org.bson.types.ObjectId

data class Mark(
    var userID:String,
    var longitude:Double,
    var latitude:Double,
    var name:String,
    var radius:Float,
    var time:Long
)
