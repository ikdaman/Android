import java.util.Properties

plugins {
    id("ikdaman.android.application")
    id("com.google.android.gms.oss-licenses-plugin")
}

android {
    namespace = "project.side.ikdaman"

    val properties = Properties()
    properties.load(project.rootProject.file("key.properties").inputStream())

    compileSdk = 36

    defaultConfig {
        applicationId = "project.side.ikdaman"
        versionCode = 8
        versionName = "1.0.8"

        val kakaoAppKey = properties.getProperty("KAKAO_APP_KEY")
        buildConfigField("String", "KAKAO_APP_KEY", "\"$kakaoAppKey\"")
        manifestPlaceholders["KAKAO_APP_KEY"] = kakaoAppKey

        buildConfigField("String", "NAVER_CLIENT_ID", "\"${properties.getProperty("NAVER_CLIENT_ID")}\"")
        buildConfigField("String", "NAVER_CLIENT_SECRET", "\"${properties.getProperty("NAVER_CLIENT_SECRET")}\"")

        targetSdk = 36
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }

    signingConfigs {
        create("release") {
            storeFile = project.rootProject.file("release.keystore")
            storePassword = properties.getProperty("KEYSTORE_PASSWORD")
            keyAlias = properties.getProperty("KEY_ALIAS")
            keyPassword = properties.getProperty("KEY_PASSWORD")
        }
    }

    buildTypes {
        getByName("release") {
            signingConfig = signingConfigs.getByName("release")
        }
    }
}

dependencies {
    implementation(projects.feature)
    implementation(projects.core)
    implementation(projects.domain)
    implementation(projects.data)

    implementation(libs.androidx.core.ktx)

    implementation(libs.kakao.login)
    implementation(libs.naver.login)
}