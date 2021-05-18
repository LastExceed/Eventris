import org.gradle.internal.os.OperatingSystem

plugins {
	kotlin("jvm") version "1.5.0"
}

repositories {
	maven("https://dl.bintray.com/dominaezzz/kotlin-native")
}

val host: OperatingSystem = OperatingSystem.current()

val kglVersion = "0.1.10"
val lwjglVersion = "3.2.3"

val lwjglNatives = when {
	host.isLinux -> "natives-linux"
	host.isMacOsX -> "natives-macos"
	host.isWindows -> "natives-windows"
	else -> error("Unrecognized or unsupported Operating system. Please set \"lwjglNatives\" manually")
}

dependencies {
	implementation("com.kgl", "kgl-opengl", kglVersion)
	implementation("com.kgl", "kgl-glfw", kglVersion)

	runtimeOnly("org.lwjgl", "lwjgl", lwjglVersion, classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-glfw", lwjglVersion, classifier = lwjglNatives)
	runtimeOnly("org.lwjgl", "lwjgl-opengl", lwjglVersion, classifier = lwjglNatives)
}


