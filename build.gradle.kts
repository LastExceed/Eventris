import org.gradle.internal.os.OperatingSystem

plugins {
	kotlin("jvm") version "1.5.0"
}

repositories {
	maven("https://maven.pkg.github.com/Dominaezzz/matrix-kt") {
		credentials {
			username = System.getenv("GITHUB_USER") // Your GitHub username
			password = System.getenv("GITHUB_TOKEN") // A GitHub token with `read:packages`
		}
	}
	mavenCentral()
}

val host: OperatingSystem = OperatingSystem.current()

val kglVersion = "0.1.11"
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


