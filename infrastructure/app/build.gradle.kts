plugins {
    id("li.raphael.kokus.module")
    application
}

application {
    applicationName = "kokus"
    mainClass.set("li.raphael.kokus.MainKt")
}

version = System.getenv("APP_VERSION") ?: "dirty"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
