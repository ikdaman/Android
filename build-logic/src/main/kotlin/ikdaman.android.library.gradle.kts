import project.side.ikdaman.app.configureCoroutineAndroid
import project.side.ikdaman.app.configureHiltAndroid
import project.side.ikdaman.app.configureKotest
import project.side.ikdaman.app.configureKotlinAndroid

plugins {
    id("com.android.library")
    id("ikdaman.verify.detekt")
}

configureKotlinAndroid()
configureCoroutineAndroid()
configureHiltAndroid()
configureKotest()