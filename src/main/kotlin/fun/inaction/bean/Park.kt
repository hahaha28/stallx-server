package `fun`.inaction.bean

import org.bson.types.ObjectId

data class Park(
    var _id:ObjectId? = null,
    var name:String,
    var longitude:Double,
    var latitude:Double,
    var city:String,
    var imgUrl:String,
    /**
     * 总车位，0代表未知
     */
    var totalStallNum:Int,
    /**
     * 当前剩余车位，0代表未知
     */
    var curStallNum:Int,
    /**
     * 是否是合作停车场
     */
    var isCollaborated:Boolean,
    /**
     * 停车场类型
     */
    var type:Int,
    /**
     * 是否收费，0（未知），1（不收费），2（收费）
     */
    var isCharged:Int = 0,
    /**
     * 收费规则
     */
    var chargingRules:String = "",
    /**
     * 评价星级，1~5
     */
    var rate:Float,
    /**
     * 评价人数
     */
    var commentsNum:Int,
    /**
     * 评论
     */
    var comments:MutableList<Comment>


){
    data class Comment(
        var userID:String,
        var userName:String,
        /**
         * 评论
         */
        var comment:String,
        var timestamp:Long,
        /**
         * 星级，1~5
         */
        var rate:Float
    )
}

fun main(){
}