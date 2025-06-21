import project.side.ikdaman.app.setNamespace
import java.util.Properties

plugins {
    id("ikdaman.android.feature")
}

android {
    setNamespace("feature")

    defaultConfig {
        val properties = Properties()
        properties.load(project.rootProject.file("key.properties").inputStream())

        buildConfigField(
            "String",
            "GOOGLE_CLIENT_ID",
            "\"${properties.getProperty("GOOGLE_CLIENT_ID")}\""
        )
    }

    buildFeatures {
        buildConfig = true
    }
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)

    implementation(libs.kakao.login)
    implementation(libs.naver.login)
    implementation(libs.google.credentials)
    implementation(libs.google.credentials.auth)
    implementation(libs.google.id)

    implementation(libs.converter.gson)

    implementation(libs.coil.compose)
    implementation(libs.barcode.scanning)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.android)
}