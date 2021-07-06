package `fun`.inaction.routes

import `fun`.inaction.bean.Park
import `fun`.inaction.db.*
import `fun`.inaction.utils.FileUtil
import `fun`.inaction.utils.MapUtil
import `fun`.inaction.utils.respondSuccess
import `fun`.inaction.utils.toObj
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.util.*
import org.bson.Document
import org.bson.types.ObjectId
import kotlin.random.Random

fun Application.parkRoute() {
    routing {

        // 搜索附近停车场
        post("/park/search") {
            val formData = call.receiveParameters()
            val searchRadius = formData["searchRadius"]!!.toFloat()
            val curLatitude = formData["latitude"]!!.toDouble()
            val curLongitude = formData["longitude"]!!.toDouble()

            // 生成虚拟数据插入数据库
//            repeat(8) {
//                val park = generateRandomPark(curLongitude, curLatitude, formData["city"]!!)
//                parkTable.insert(park)
//            }

            // 搜索数据库中该城市所有停车场
            val parks = parkTable.findAll<Park>("city", formData["city"]!!)
            val result = mutableListOf<Park>()
            parks.forEach {
                val distance = MapUtil.getDistance(curLongitude, curLatitude, it.longitude, it.latitude)
                if (distance < searchRadius) {
                    result.add(it)
                }
            }
            call.respondSuccess(mapOf("parks" to result))
        }

        // 添加停车场
        post("/park/add") {
            var jsonStr: String? = null
            var urlPath: String? = null
            call.receiveMultipart().forEachPart { part ->
                when (part) {
                    is PartData.FormItem -> {
                        jsonStr = part.value
//                        val park = jsonStr.toObj<Park>()
                    }
                    is PartData.FileItem -> {
                        val type = part.contentType?.contentSubtype ?: "null"
                        val path = FileUtil.writeImageFile(part.streamProvider(), type)
                        urlPath = "/img/${path.substringAfter("/")}"
                        println("urlPath=${urlPath}")
                    }
                }
            }
            val doc = Document.parse(jsonStr!!)
                .append("imgUrl", urlPath!!)
            parkTable.insertOne(doc)
            call.respondSuccess()
        }

        // 获取某个id的停车场信息
        get("/park/get") {
            val parkId = call.request.queryParameters["parkID"]
            val park = parkTable.findOne<Park>("_id", ObjectId(parkId))!!
            call.respondSuccess(
                mapOf(
                    "parkData" to park
                )
            )
        }

        // 添加评论
        post("/park/comment/add") {
            val formData = call.receiveParameters()
            parkTable.findOneAndReplace<Park>(formData["parkID"]!!) {
                val rate = formData["rate"]!!.toFloat()

                it.comments.add(
                    Park.Comment(
                        formData["userID"]!!,
                        formData["userName"]!!,
                        formData["comment"]!!,
                        System.currentTimeMillis(),
                        rate
                    )
                )
                it.rate = (it.rate*it.commentsNum+rate)/(it.commentsNum+1)
                it.commentsNum += 1
            }
            call.respondSuccess()
        }

    }
}

fun generateRandomPark(longitude: Double, latitude: Double, city: String): Park {
    val name = "停车场"
    val deltar = 0.05
    val lde = Random.nextDouble(longitude - deltar, longitude + deltar)
    val lade = Random.nextDouble(latitude - deltar, latitude + deltar)
    val imgUrl = "/img/park_default.png"
    val totalStallNum = Random.nextInt(280)
    val curStallNum = Random.nextInt(totalStallNum)
    val isCollaborated = Random.nextInt() % 2 == 1
    val type = Random.nextInt(1, 5)
    val isCharged = Random.nextInt(0, 3)
    var rate = 0f
    val chargingRules = "每小时2.00元，一天20.00元。"

    val comments = mutableListOf<Park.Comment>()
    repeat(8) {
        val comment = generateRandomComment()
        comments.add(comment)
        rate += comment.rate
    }
    rate /= comments.size

    val park = Park(
        null,
        name,
        lde,
        lade,
        city,
        imgUrl,
        totalStallNum,
        curStallNum,
        isCollaborated,
        type,
        isCharged,
        chargingRules,
        rate,
        comments.size,
        comments
    )
    return park
}

fun generateRandomComment(): Park.Comment {
    val commentList = listOf(
        "默认好评",
        "这停车场位置挺多",
        "感觉空位有点少",
        "位置不太好找",
        "不错",
        "还行"
    )
    val randomInt = Random.nextInt(commentList.size)
    return Park.Comment(
        "随机生成id",
        "用户${Random.nextInt(65535)}",
        commentList[randomInt],
        Random.nextLong(System.currentTimeMillis() - 7 * 24 * 60 * 60 * 1000, System.currentTimeMillis()),
        rate = if (randomInt == 0) {
            5f
        } else {
            Random.nextDouble(4.0, 5.0).toFloat()
        }
    )
}