plugins {
    `java-library`
    `maven-publish`
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
}

java {
    disableAutoTargetJvm()
    toolchain.languageVersion = JavaLanguageVersion.of(21)
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://maven.evokegames.gg/snapshots")
    maven("https://jitpack.io")
}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.commandapi)
    implementation(libs.configlib.yaml)
    implementation(libs.configlib.paper)
    implementation(libs.entitylib)
    compileOnly(libs.lombok)
    compileOnly(libs.packetevents)
    annotationProcessor(libs.lombok)
}


group = "de.vectorflare"
version = "1.1"
description = "Create custom skyboxes for each dimension"

tasks {
    jar {
        enabled = false
    }

    shadowJar {
        archiveFileName = "${rootProject.name}-${project.version}.jar"
        archiveClassifier = null

        manifest {
            attributes["Implementation-Version"] = rootProject.version
        }
        relocate("me.tofaa.entitylib", "de.vectorflare.skyboxengine.shaded.entitylib")
    }

    assemble {
        dependsOn(shadowJar)
    }

    withType<JavaCompile> {
        options.encoding = Charsets.UTF_8.name()
        options.release = 21
    }

    withType<Javadoc>() {
        options.encoding = Charsets.UTF_8.name()
    }

    processResources {
        inputs.property("version", project.version)

        filesMatching("plugin.yml") {
            expand(
                    "version" to rootProject.version,
            )
        }
    }

    defaultTasks("build")

    // 1.8.8 - 1.16.5 = Java 8
    // 1.17           = Java 16
    // 1.18 - 1.20.4  = Java 17
    // 1-20.5+        = Java 21
    val version = "1.21.4"
    val javaVersion = JavaLanguageVersion.of(21)

    val jvmArgsExternal = listOf(
            "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/test")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
            url("https://github.com/retrooper/packetevents/releases/download/v2.7.0/packetevents-spigot-2.7.0.jar")
           // url("https://download.luckperms.net/1556/bukkit/loader/LuckPerms-Bukkit-5.4.141.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}
