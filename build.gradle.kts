import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar


plugins {
    kotlin("jvm") version "1.5.10"
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

group = "me.pljr"
version = "1.2.0"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://papermc.io/repo/repository/maven-public/")
    }
    maven {
        name = "sonatype-oss"
        url = uri("https://oss.sonatype.org/content/repositories/snapshots/")
    }
    maven {
        name = "codemc-repo"
        url = uri("https://repo.codemc.io/repository/maven-public/")
    }
}

dependencies {
    testImplementation(kotlin("test"))

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    implementation("com.github.cryptomorin:XSeries:7.9.1.1")

    // Kyori
    implementation("net.kyori:adventure-text-minimessage:4.1.0-SNAPSHOT")
    implementation("net.kyori:adventure-platform-bukkit:4.0.1")

    compileOnly("io.papermc.paper:paper-api:1.18.1-R0.1-SNAPSHOT")
    compileOnly(files("libs/CMIAPI8.7.8.2.jar"))
}

tasks.named<ShadowJar>("shadowJar") {
    relocate("net.kyori", "me.pljr.destroyblock.kyori")
    relocate("com.cryptomorin.xseries", "me.pljr.pljrapispigot.xseries")
}

tasks.build {
    dependsOn("shadowJar")
}

tasks.test {
    useJUnit()
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}