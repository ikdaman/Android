import project.side.ikdaman.app.setNamespace

plugins {
    id("ikdaman.android.feature")
}

android {
    setNamespace("feature")
}

dependencies {
    implementation(projects.core)
}