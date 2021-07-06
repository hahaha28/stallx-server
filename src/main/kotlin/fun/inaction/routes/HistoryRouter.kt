package `fun`.inaction.routes

import `fun`.inaction.bean.History
import `fun`.inaction.db.findAll
import `fun`.inaction.db.historyTable
import `fun`.inaction.db.insert
import `fun`.inaction.utils.respondSuccess
import io.ktor.application.*
import io.ktor.routing.*
import org.bson.types.ObjectId

fun Application.historyRoute(){

    routing {

        // 添加历史
        post("/history/add"){
            val userID = call.request.cookies["userID"]!!
            val parkID = call.request.queryParameters["parkID"]!!
            val parkName = call.request.queryParameters["parkName"]!!

            historyTable.insert(mapOf(
                "userID" to userID,
                "parkID" to parkID,
                "parkName" to parkName,
                "timestamp" to System.currentTimeMillis()
            ))
            call.respondSuccess()
        }

        // 获取历史
        get("/history/get"){
            val userID = call.request.cookies["userID"]!!
            val historys = historyTable.findAll<History>("userID",userID).sortedByDescending {
                it.timestamp
            }
            call.respondSuccess(mapOf("historyData" to historys))
        }

    }
}