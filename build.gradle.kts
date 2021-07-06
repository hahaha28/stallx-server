val ktor_version: String by project
val kotlin_version: String by project
val logback_version: String by project

plugins {
    application
    kotlin("jvm") version "1.4.32"
}

group = "fun.inaction"
version = "0.0.1"
application {
    mainClass.set("fun.inaction.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.ktor:ktor-server-core:$ktor_version")
    implementation("io.ktor:ktor-server-netty:$ktor_version")
    implementation("ch.qos.logback:logback-classic:$logback_version")
    testImplementation("io.ktor:ktor-server-tests:$ktor_version")
    implementation("io.ktor:ktor-gson:$ktor_version")


    implementation("org.mongodb:mongodb-driver-sync:4.2.3") // mongodb 驱动
    implementation("com.google.code.gson:gson:2.8.6") // gson
    implementation("com.tencentcloudapi:tencentcloud-sdk-java:3.1.239") // 短信
    implementation("com.squareup.okhttp3:okhttp:4.9.1") // okhttp


}