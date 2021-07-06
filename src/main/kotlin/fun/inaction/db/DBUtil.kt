package `fun`.inaction.db

import `fun`.inaction.bean.Mark
import `fun`.inaction.bean.Park
import `fun`.inaction.bean.User
import `fun`.inaction.utils.toObj
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.mongodb.MongoClientSettings
import com.mongodb.client.MongoClients
import com.mongodb.client.MongoCollection
import io.ktor.http.*
import io.ktor.utils.io.core.*
import org.bson.Document
import org.bson.conversions.Bson
import org.bson.json.JsonWriterSettings
import org.bson.types.ObjectId
import java.io.FileReader
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.logging.LogManager
import javax.script.Invocable
import javax.script.ScriptEngineManager
import kotlin.text.toByteArray

private val userName = "hahaha28"
private val password = "S28dkdfcdmkmfcm"
private val url = "8.129.24.81:27017"

val dbClient =MongoClients.create("mongodb://${userName}:${password}@${url}/?authSource=admin")
val db = dbClient.getDatabase("stallX")

val userTable = db.getCollection("user")
val markTable = db.getCollection("mark")
val parkTable = db.getCollection("park")
val historyTable = db.getCollection("history")
val collectionTable = db.getCollection("collection")



fun main(){

    val str = "3_2.0+/api/v4/search_v3?t=general&q=python&correction=1&offset=0&limit=20&lc_idx=0&show_all_topics=0+\"AJAYEIt8qxKPTioQHjGcayyzESKzMIuqtkU=|1613525909\""
    val s = "你好".encodeURLPath()
    println(s)
}


fun String.md5(text: String): String {
    try {
        //获取md5加密对象
        val instance: MessageDigest = MessageDigest.getInstance("MD5")
        //对字符串加密，返回字节数组
        val digest:ByteArray = instance.digest(text.toByteArray())
        var sb : StringBuffer = StringBuffer()
        for (b in digest) {
            //获取低八位有效值
            var i :Int = b.toInt() and 0xff
            //将整数转化为16进制
            var hexString = Integer.toHexString(i)
            if (hexString.length < 2) {
                //如果是一位的话，补0
                hexString = "0" + hexString
            }
            sb.append(hexString)
        }
        return sb.toString()

    } catch (e: NoSuchAlgorithmException) {
        e.printStackTrace()
    }

    return ""
}