plugins {
    java
}

group = "dev.whips"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Data
    implementation("io.github.novacrypto:Base58:2022.01.17@jar")
    implementation("com.google.guava:guava:31.1-jre")

    // Web
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.12.4")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}