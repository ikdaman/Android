import project.side.ikdaman.app.setNamespace
import java.util.Properties

plugins {
    id("ikdaman.android.library")
}

android {
    setNamespace("data")
    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("key.properties").inputStream())

        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL")}\"")
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies{
    implementation(projects.domain)

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.androidx.datastore.preferences.core)
    implementation(libs.retrofit)
    implementation(libs.okhttp3.logging)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
}