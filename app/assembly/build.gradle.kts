plugins {
    id("li.raphael.kokus.module")
    application
}

c4Container {
    description = "Bundles together all feature modules into a runnable artifact"
    hidden = true
}

application {
    applicationName = "kokus"
    mainClass.set("li.raphael.kokus.MainKt")
}

version = System.getenv("APP_VERSION") ?: "dirty"

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
