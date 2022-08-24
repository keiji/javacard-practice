import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.util.*

fun getProperty(file: String): Properties {
    val localProperties = File(file)
    if (!localProperties.exists() || !localProperties.isFile) {
        error("File ${file} not found")
    }

    val properties = Properties()
    InputStreamReader(FileInputStream(localProperties), Charsets.UTF_8).use { reader ->
        properties.load(reader)
    }

    return properties
}

val props = getProperty("local.properties")

plugins {
    id("java")
}

group = "dev.keiji.javacard.applet"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(
        fileTree(
            mapOf(
                "dir" to "$rootDir/libs/",
                "include" to listOf("*.jar"),
            )
        )
    )

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.withType(JavaCompile::class.java) {
    options.compilerArgs = mutableListOf("-g")
    targetCompatibility = JavaVersion.VERSION_1_7.toString()
    sourceCompatibility = JavaVersion.VERSION_1_7.toString()
}

val javaCardSdkLibPath = props["javacardsdk.path"]
val appletPackageName = "dev.keiji.javacard.applet"
val appletPackageAid = "0x0F:02:03:04:05"
val appletVersion = "1.0"
val appletAid = "0x0F:02:03:04:05:06:07:03:02"
val appletClassName = "dev.keiji.javacard.applet.App"
val appletTargetJavaCardOsVersion = "3.0.4"

tasks.create("convertToCap", JavaExec::class) {
    classpath = files(
        "${javaCardSdkLibPath}\\asm-8.0.1.jar",
        "${javaCardSdkLibPath}\\commons-cli-1.4.jar",
        "${javaCardSdkLibPath}\\commons-logging-1.2-9f99a00.jar",
        "${javaCardSdkLibPath}\\jctasks_tools.jar",
        "${javaCardSdkLibPath}\\json.jar",
        "${javaCardSdkLibPath}\\tools.jar",
        "${javaCardSdkLibPath}\\api_classic-3.0.4.jar",
        "${javaCardSdkLibPath}\\api_classic_annotations-3.0.4.jar",
    )
    main = "com.sun.javacard.converter.Main"
    args = listOf(
        appletPackageName, appletPackageAid, appletVersion,
        "-classdir", "${buildDir}/classes/java/main",
        "-applet", appletAid, appletClassName,
        "-d", "${buildDir}/applet",
        "-target", appletTargetJavaCardOsVersion,
        "-i"
    )
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
