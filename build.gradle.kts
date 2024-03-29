plugins {
    java
    `maven-publish`
}

group = "dev.whips"
version = "1.1.3"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

dependencies {
    implementation("org.slf4j:slf4j-simple:1.7.36")

    // Data
    implementation("io.github.novacrypto:Base58:2022.01.17@jar")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("com.github.InstantWebP2P:tweetnacl-java:v1.1.2")

    // Web
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.13.2")
    implementation("org.java-websocket:Java-WebSocket:1.5.3")

    // Testing
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

java {
    sourceCompatibility = JavaVersion.VERSION_16
    targetCompatibility = JavaVersion.VERSION_16
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components["java"])
        }
    }
}