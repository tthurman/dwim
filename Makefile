build: gradlew build.gradle
	./gradlew assembleDebug

install:
	adb install -rd build/outputs/apk/dwim-debug.apk

build.gradle: build.gradle.in
	cp build.gradle.in build.gradle

gradlew:
	rm -f build.gradle
	gradle wrapper --gradle-version 2.2
	cp build.gradle.in build.gradle

test:
	./gradlew test
	xdg-open build/reports/tests/debug/index.html


