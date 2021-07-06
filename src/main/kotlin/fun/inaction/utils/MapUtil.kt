package `fun`.inaction.utils

import com.squareup.okhttp.Callback
import com.squareup.okhttp.OkHttpClient
import com.squareup.okhttp.Request
import com.squareup.okhttp.Response
import java.io.IOException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.io.UnsupportedEncodingException
import java.net.URLEncoder
import java.util.LinkedHashMap





object MapUtil {

    /**
     * 地球半径,单位 km
     */
    private const val EARTH_RADIUS = 6378.137

    private const val SK = "8rU49ThNGMagBTO0KM6TKdHdK5UsoX9k"

    private const val AK = "pi6Qu5m6OYHrrNjOudK1rEDfjID0Ctj9"

    // 对Map内所有value作utf8编码，拼接返回结果
    @Throws(UnsupportedEncodingException::class)
    fun toQueryString(data: Map<*, *>): String {
        val queryString = StringBuffer()
        for ((key, value) in data) {
            queryString.append(key.toString() + "=")
            queryString.append(
                URLEncoder.encode(
                    value as String?,
                    "UTF-8"
                ).toString() + "&"
            )
        }
        if (queryString.length > 0) {
            queryString.deleteCharAt(queryString.length - 1)
        }
        return queryString.toString()
    }

    fun MD5(md5: String): String? {
        try {
            val md = MessageDigest
                .getInstance("MD5")
            val array = md.digest(md5.toByteArray())
            val sb = StringBuffer()
            for (i in array.indices) {
                sb.append(
                    Integer.toHexString(array[i].toInt() and 0xFF or 0x100)
                        .substring(1, 3)
                )
            }
            return sb.toString()
        } catch (e: NoSuchAlgorithmException) {
        }
        return null
    }

    fun getWalkTime(fromLongitude:Double,fromLatitude:Double,toLongitude:Double,toLatitude:Double,toUid:String? = null,callback:(Int?)->Unit){
        val paramsMap = LinkedHashMap<String,String>()
        paramsMap["origin"] = "${fromLatitude},${fromLongitude}"
        paramsMap["destination"] = "${toLatitude},${toLongitude}"
        paramsMap["ak"] = AK
        toUid?.let { paramsMap["destination_uid"] = "$toUid" }
        paramsMap["timestamp"] = "${System.currentTimeMillis()}"

        var paramStr = toQueryString(paramsMap)
        val wholeStr = "/directionlite/v1/walking?${paramStr}${SK}"
        val tempStr = URLEncoder.encode(wholeStr,"UTF-8")
        val sn = MD5(tempStr)!!


        paramsMap["sn"] = sn
        paramStr = toQueryString(paramsMap)
        val url = "http://api.map.baidu.com/directionlite/v1/walking?${paramStr}"
        println("url=${url}")

        // 发送网络请求
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request?, e: IOException?) {
                callback(null)
            }

            override fun onResponse(response: Response?) {
                response?.let {
                    val jsonStr = it.body().string()
                    val result = jsonStr.toObj<WalkPlanResult>()
                    if(result.status == 0){
                        callback(result.result.routes[0].duration)
                    }else{
                        callback(null)
                    }
                    return
                }
                callback(null)
                return
            }

        })
    }

    fun getDistance(longitude1: Double, latitude1: Double, longitude2: Double, latitude2: Double): Double {
        // 纬度
        val lat1 = Math.toRadians(latitude1)
        val lat2 = Math.toRadians(latitude2)
        // 经度
        val lng1 = Math.toRadians(longitude1)
        val lng2 = Math.toRadians(longitude2)
        // 纬度之差
        val a = lat1 - lat2
        // 经度之差
        val b = lng1 - lng2
        // 计算两点距离的公式
        var s = 2 * Math.asin(
            Math.sqrt(
                Math.pow(Math.sin(a / 2), 2.0) +
                        Math.cos(lat1) * Math.cos(lat2) * Math.pow(Math.sin(b / 2), 2.0)
            )
        )
        // 弧长乘地球半径, 返回单位: 千米
        s *= EARTH_RADIUS
        return s
    }

}

data class WalkPlanResult(
    var status:Int,
    var message: String,
    var result:Result
){
    data class Result(
        var routes:List<Route>
    )

    data class Route(
        /**
         * 耗时，单位 秒
         */
        var duration:Int
    )
}

fun main(){
    val dis = MapUtil.getDistance(121.819145,39.089943,121.819682,39.089912)
    println(MapUtil.getDistance(100.0,100.0,100.1,100.0))
}