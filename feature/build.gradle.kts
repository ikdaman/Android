import project.side.ikdaman.app.setNamespace

plugins {
    id("ikdaman.android.feature")
}

android {
    setNamespace("feature")
}

dependencies {
    implementation(projects.core)
    implementation(projects.domain)
    implementation("io.coil-kt:coil-compose:2.4.0")
}