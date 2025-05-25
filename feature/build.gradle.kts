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

    implementation(libs.kakao.login)

    implementation(libs.coil.compose)
    implementation(libs.barcode.scanning)

    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.ui.android)
    implementation(libs.androidx.ui.android)
}