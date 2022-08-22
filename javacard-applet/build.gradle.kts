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

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}