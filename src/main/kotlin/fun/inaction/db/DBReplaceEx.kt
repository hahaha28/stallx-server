package `fun`.inaction.db

import `fun`.inaction.utils.objectIdGson
import com.google.gson.Gson
import com.mongodb.client.MongoCollection
import org.bson.Document
import org.bson.types.ObjectId

inline fun <reified T> MongoCollection<Document>.findOneAndReplace(document: Document, modifyObj: (T) -> Unit) {
    val obj = this.findOne<T>(document)!!
    modifyObj(obj)
    val newDoc = Document.parse(objectIdGson.toJson(obj))
    newDoc.remove("_id")
    this.replaceOne(document, newDoc)
}

inline fun <reified T> MongoCollection<Document>.findOneAndReplace(_id: String, modifyObj: (T) -> Unit) =
    this.findOneAndReplace<T>(Document("_id", ObjectId(_id)), modifyObj)

inline fun <reified T> MongoCollection<Document>.findOneAndReplace(key: String, value: Any, modifyObj: (T) -> Unit) =
    this.findOneAndReplace<T>(Document(key, value), modifyObj)
