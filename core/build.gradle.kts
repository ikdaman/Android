import project.side.ikdaman.app.setNamespace

plugins {
    id("ikdaman.android.library")
    id("ikdaman.android.compose")
}

android {
    setNamespace("core")
}

dependencies{
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
}