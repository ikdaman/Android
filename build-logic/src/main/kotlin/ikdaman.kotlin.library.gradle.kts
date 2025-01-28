import project.side.ikdaman.app.configureKotest
import project.side.ikdaman.app.configureKotlin

plugins {
    kotlin("jvm")
    id("ikdaman.verify.detekt")
}

configureKotlin()
configureKotest()
