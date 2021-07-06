package `fun`.inaction.routes

import `fun`.inaction.bean.Mark
import `fun`.inaction.db.findAll
import `fun`.inaction.db.insert
import `fun`.inaction.db.markTable
import `fun`.inaction.db.userTable
import `fun`.inaction.utils.respondSuccess
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.routing.*

/**
 * 标记相关路由
 */
fun Application.markRoute() {
    routing {

        // 添加标记
        post("/mark/add") {
            val userID = call.request.cookies["userID"]!!
            val mark = call.receive<Mark>()
            mark.userID = userID
            markTable.insert(mark)
            call.respondSuccess()
        }

        // 获取历史标记
        get("/mark/get/history") {
            val userID = call.request.cookies["userID"]!!
            val markList = markTable.findAll<Mark>("userID", userID)
                .sortedByDescending {
                    it.time
                }
            call.respondSuccess(mapOf("markList" to markList))
        }

    }
}