import project.side.ikdaman.app.setNamespace

plugins {
    id("ikdaman.android.feature")
}

android {
    setNamespace("main")
}

dependencies {
    implementation(projects.core)
    implementation(projects.feature)
}