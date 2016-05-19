build: gradlew build.gradle
	./gradlew assembleDebug

install: build
	adb install -rd target/dwim.apk

build.gradle: build.gradle.in
	cp build.gradle.in build.gradle

gradlew:
	rm -f build.gradle
	gradle wrapper --gradle-version 2.2
	cp build.gradle.in build.gradle

