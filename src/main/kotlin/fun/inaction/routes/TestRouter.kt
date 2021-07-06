package `fun`.inaction.routes

import `fun`.inaction.utils.FileUtil
import `fun`.inaction.utils.respondSuccess
import io.ktor.application.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.utils.io.core.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception

fun Application.testRoute(){
    routing {
        post("/test"){
            call.receiveMultipart().forEachPart { part->
                when(part){
                    is PartData.FormItem ->{
                        println("formItem")
                        println(part.value)
                    }
                    is PartData.FileItem -> {
                        println("fileItem")
                        val type = part.contentType?.contentSubtype?:"null"
                        val fileName = part.originalFileName as String
                        println("fileName=${fileName}")
                        val path = FileUtil.writeImageFile(part.streamProvider(),type)
                        println("imagePath=${path}")
                    }
                }
            }
            call.respondSuccess()
        }

        get("/ttt"){
            val file = File("images")
            val files = file.listFiles()
            println("${files?.size}")
            call.respondText("ok")
        }

    }
}