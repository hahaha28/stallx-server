package `fun`.inaction.routes

import `fun`.inaction.bean.Collection
import `fun`.inaction.bean.Park
import `fun`.inaction.db.*
import `fun`.inaction.utils.respond
import `fun`.inaction.utils.respondSuccess
import io.ktor.application.*
import io.ktor.routing.*
import org.bson.types.ObjectId

fun Application.collectionRoute(){

    routing {

        // 添加收藏
        post("/collection/add"){
            val userID = call.request.cookies["userID"]!!
            val parkID = call.request.queryParameters["parkID"]!!
            println("parkID=${parkID}")
            val park = parkTable.findOne<Park>("_id",ObjectId(parkID))!!
            collectionTable.insert(mapOf(
                "userID" to userID,
                "parkID" to parkID,
                "parkName" to park.name,
                "timestamp" to System.currentTimeMillis()
            ))
            call.respondSuccess()
        }

        // 获取所有收藏
        get("/collection/get"){
            val userID = call.request.cookies["userID"]!!
            val collections = collectionTable.findAll<Collection>("userID",userID).sortedByDescending {
                it.timestamp
            }
            call.respondSuccess(mapOf("collectionData" to collections))
        }

        // 取消收藏
        post("/collection/delete"){
            val collectionID = call.request.queryParameters["collectionID"]
            collectionTable.deleteOne("_id",ObjectId(collectionID))
            call.respondSuccess()
        }

        // 取消收藏
        post("/collection/delete_indirect"){
            val userID = call.request.cookies["userID"]!!
            val parkID = call.request.queryParameters["parkID"]!!
            val deleteResult = collectionTable.deleteOne(mapOf(
                "userID" to userID,
                "parkID" to parkID
            ))
            if(deleteResult.deletedCount == 1L){
                call.respondSuccess()
            }else{
                call.respond(-1,"不存在")
            }
        }

        // 判断是否收藏
        post("/collection/judge"){
            val userID = call.request.cookies["userID"]!!
            val parkID = call.request.queryParameters["parkID"]!!
            val result = collectionTable.findOne<Collection>(mapOf(
                "userID" to userID,
                "parkID" to parkID
            ))
            if(result == null){
                call.respondSuccess(mapOf("isCollected" to false))
            }else{
                call.respondSuccess(mapOf("isCollected" to true))
            }
        }


    }

}