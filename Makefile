WRANGLER_TEST_GENERATED = src/test/java/org/marnanel/dwim/test/WranglerTest.java
WRANGLER_TEST_GENERATOR = src/test/python/generate-java.py
WRANGLER_TEST_DATA = src/test/python/*.xml

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

test: $(WRANGLER_TEST_GENERATED)
	./gradlew test

wrangler.test.rebuild: $(WRANGLER_TEST_GENERATOR) $(WRANGLER_TEST_DATA)
	python $(WRANGLER_TEST_GENERATOR) $(WRANGLER_TEST_DATA) > $(WRANGLER_TEST_GENERATED)

$(WRANGLER_TEST_GENERATED): wrangler.test.rebuild


showtest:
	xdg-open build/reports/tests/debug/index.html


