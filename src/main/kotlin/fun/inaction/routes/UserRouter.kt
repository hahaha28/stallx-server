package `fun`.inaction.routes

import `fun`.inaction.bean.User
import `fun`.inaction.db.findOne
import `fun`.inaction.db.insert
import `fun`.inaction.db.userTable
import `fun`.inaction.utils.SMSUtil
import `fun`.inaction.utils.respond
import `fun`.inaction.utils.respondSuccess
import ch.qos.logback.core.db.dialect.DBUtil
import com.google.gson.Gson
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import kotlin.random.Random
import kotlin.reflect.KType

/**
 * 用户相关的路由
 */
fun Application.userRoute() {
    routing {

        /**
         * 登录/注册
         */
        post("/user/login") {
            val formData = call.receiveParameters()
            val tel = formData["tel"]!!
            // 首先检查验证码
            if (SMSUtil.checkCode(tel, formData["verifyCode"]!!)) {
                // 验证码正确，查找数据库
                val user = userTable.findOne<User>("tel", tel)
                if (user == null) {
                    // 如果用户不存在，注册
                    // 生成随机用户名
                    val userName = "用户${Random.nextInt(1000, 9999)}"
                    val userID = userTable.insert(User(tel, userName))!!.toHexString()
                    call.respondSuccess(mapOf("userID" to userID,"userName" to userName))
                } else {
                    // 用户存在
                    call.respondSuccess(mapOf("userID" to user._id!!))
                }
            } else {
                // 验证码错误
                call.respond(-1, "验证码错误")
            }

        }

        /**
         * 发送验证码
         */
        get("/captcha/send") {
            val tel = call.request.queryParameters["tel"]!!
            val (success, msg) = SMSUtil.sendSMS(tel)
            if (success) {
                call.respondSuccess()
            } else {
                call.respond(-1, msg)
            }
        }


    }
}