plugins {
    id("java-library")
}

group = "fr.shyrogan"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://papermc.io/repo/repository/maven-public/")
}

dependencies {
    implementation("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
}