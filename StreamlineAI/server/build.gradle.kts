plugins {
    alias(libs.plugins.kotlinJvm)
    alias(libs.plugins.ktor)
    application
}

group = "de.frinshhd.streamlineai"
version = "1.0.0"
application {
    mainClass.set("de.frinshhd.streamlineai.ApplicationKt")
    
    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

dependencies {
    implementation(projects.shared)
    implementation(libs.logback)
    implementation(libs.ktor.serverCore)
    implementation(libs.ktor.serverNetty)
    testImplementation(libs.ktor.serverTestHost)
    testImplementation(libs.kotlin.testJunit)
    implementation(libs.ktor.serverAuth)
    implementation(libs.ktor.clientCore)
    implementation(libs.ktor.clientCio)
    implementation(libs.ktor.clientContentNegotiation)
    implementation(libs.ktor.serverContentNegotiation)
    implementation(libs.ktor.clientApache)
    implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.http-client:google-http-client-gson:1.42.3")
    implementation("com.google.apis:google-api-services-oauth2:v2-rev20200213-2.0.0")
    implementation("com.google.auth:google-auth-library-oauth2-http:1.12.1")
    //implementation("com.google.http-client:google-http-client-javanet:1.42.3")
}