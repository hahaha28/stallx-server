package `fun`.inaction.db

import com.mongodb.client.MongoCollection
import com.mongodb.client.result.DeleteResult
import org.bson.Document

fun MongoCollection<Document>.deleteOne(key:String,value:Any):DeleteResult{
    return this.deleteOne(Document(key,value))
}

fun MongoCollection<Document>.deleteOne(map:Map<String,Any>):DeleteResult{
    val doc = Document()
    map.forEach { t, u ->
        doc.append(t,u)
    }
    return this.deleteOne(doc)
}