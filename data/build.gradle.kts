import project.side.ikdaman.app.setNamespace

plugins {
    id("ikdaman.android.library")
}

android {
    setNamespace("data")
}

dependencies{
    implementation(projects.domain)

    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.converter.moshi)
    implementation(libs.moshi.kotlin)
}