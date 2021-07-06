package `fun`.inaction.bean

import org.bson.types.ObjectId

data class User(
    var tel:String,
    var name:String
){
    var _id:ObjectId? = null
}
