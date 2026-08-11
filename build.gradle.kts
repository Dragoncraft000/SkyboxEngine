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
    mavenCentral()
    mavenLocal()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
   // maven("https://maven.evokegames.gg/snapshots")
    maven("https://jitpack.io")

}

dependencies {
    compileOnly(libs.paper.api)
    implementation(libs.commandapi.shade)
    implementation(libs.configlib.yaml)
    implementation(libs.configlib.paper)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
    implementation("org.bstats:bstats-bukkit:3.2.1")
}


group = "de.vectorflare"
version = "1.7"
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
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
        relocate("org.bstats", "de.vectorflare.skyboxengine.shaded.bstats")
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
    val version = "26.2"
    val javaVersion = JavaLanguageVersion.of(25)

    val jvmArgsExternal = listOf(
            "-Dcom.mojang.eula.agree=true"
    )

    runServer {
        minecraftVersion(version)
        runDirectory = rootDir.resolve("run/$version/")

        javaLauncher = project.javaToolchains.launcherFor {
            languageVersion = javaVersion
        }

        downloadPlugins {
           // url("https://download.luckperms.net/1556/bukkit/loader/LuckPerms-Bukkit-5.4.141.jar")
        }

        jvmArgs = jvmArgsExternal
    }
}
