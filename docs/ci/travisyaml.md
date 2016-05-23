# Travis YAML
The Travis CI build system monitors git repositories for changes. When a change
is detected the build system looks in the root of the git repository for a
```.travis.yml``` file configure the build. This configuration file can
instruct Travis to build, test, deploy and report results.

# Travis YAML For Android
The CoffeeNowApp uses a fairly standard ```.travis.yml``` file for Android with
the addition of both testing and S3 artifact deploy capabilities. Below is the
starting point for our Android yaml file.
```yaml
language: android
jdk: oraclejdk7
android:
  components:
  - platform-tools
  - tools
  - build-tools-23.0.3
  - android-23
  - extra-android-support
  - extra-android-m2repository
  licenses:
  - android-sdk-license-.+
script:
- ./gradlew assembleDebug
```
This file defines the build type of Android and specifies the jdk version to
use for the build. We list the necessary components for building our app on
API 23. The last step is the command that builds the app into a debuggable apk.

# Running Unit Tests
The next step in our setup is to run basic Android Unit Tests. Testing Android
applications is complex as you will soon learn so unit testing the Java code
outside of the Android environment can be very beneficial. To run Java unit
tests in Travis CI all we need to do is change our script command like so:
```yaml
script:
- ./gradlew testDebug
```
With this setup you'll run any unit tests in the project. We'll discuss writing
unit tests a little bit later.

# Running Android UI Tests
To run Android UI tests in Travis CI, we'll need to setup a few more things.
It is important to note that at this time, Android testing in Travis CI is
experimental and slow. It also seem to fail fairly regularly. For a larger team
building a production app, Travis CI is probably not ready for use. Hopefully
in the future things will change. Also realize that this documentation may be
out of date quickly.

## Create and Launch an Android Emulator
To run Android UI tests on a virtual environment like Travis CI, an Android
Virtual Device (AVD) will need to be setup. To do so, the following
configuration needs to be added to the ```.travis.yml``` file:
```yaml
android:
  components:
  # Add system image to the end of your components list
  - sys-img-armeabi-v7a-android-23
```
Rename the existing script configuration to install, it will follow directly
after the android.licenses configuration and look like:
```yaml
install:
- ./gradle testDebug
```
The install configuration gets run before the script configuration. We will now
create a before_script configuration that is reponsible for creating and
launching the AVD. Right after install add:
```yaml
before_script:
- export ANDROID_HOME=/usr/local/android-sdk
- export PATH=${PATH}:${ANDROID_HOME}/tools:${ANDROID_HOME}/platform-tools
- echo no | android create avd --force -n test -t android-23 --abi armeabi-v7a
- emulator -avd test -no-skin -no-audio -no-window &
- android-wait-for-emulator
- adb shell input keyevent 82 &
```
There is a lot going on here. The first two lines setup our PATH to include
the paths to the Android tools directories where commands like adb, android and
emulator live. The next line creates an AVD called test with the ARM API 23
system image we included above. The emulator command launches the emulator with
a series of special flags that are needed for launching emulators in a virtual
environment. The android-wait-for-emulator is a custom script that is include
on Travis CI build machines designed to wait for the AVD to boot. The source
can be found on [github](https://github.com/travis-ci/travis-cookbooks/blob/master/community-cookbooks/android-sdk/files/default/android-wait-for-emulator).
The android-wait-for-emulator command take a very long time to complete. At
this time the x86 emulators do not work and the ARM emulators are extremely
slow. It can take 2-4 minutes for the emulator to book. While it is booting
a message will be displayed to the build logs. The adb shell command sends a
menu key event to the device in order to wakeup the screen.

## Running Android UI Tests
With an AVD running and awake the Android UI test execution can start. Add a
script configuration after the before_script:
```yaml
script:
- ./gradlew connectedAndroidTest
```
The connectedAndroidTest task will build and install the apk on all connected
devices, in our case, the one AVD. The task then executes all Android UI tests
and downloads the results from the tests.

# Running Lint Check
Adding support for lint checks is quite simple. Lint is useful code checker
that should be used on all projects. To setup lint checks you can add it to the
install config like so:
```yaml
install:
- ./gradlew lintDebug
- ./gradlew testDebug
```

# Speeding Things Up
Travis CI allows builds to cache some data. Care needs to be taken when
deciding what to cache. More documentation about caching can be found in the
Travis CI documentation [here](https://docs.travis-ci.com/user/caching/). To
setup basic caching add a cache config after the android config.
```yaml
cache:
  directories:
  - $HOME/.gradle/caches
  - $HOME/.gradle/wrapper
```
For a simple build that only included build and unit tests the total build time
went from 2:15 to 1:50.

# Publish Artifacts to Amazon S3
Builds in Travis CI can produce a lot of useful information. The build is now
generating an installable apk, unit test results, Android UI test results and
lint check results. If a build failure occurs it would be nice to be able to
see these results somewhere. Additionally if a build successes it would be nice
to simploy download the build apk. Eventually this process could be automated
and the apk could be signed and sent to the Google Play Store for release.

Based on the earlier guide for settings up an Amazon S3 bucket, the Travis CI
configuration can now be updated to push changes to S3.
# Secure Environment Variables
```bash
travis encrypt ARTIFACTS_AWS_ACCESS_KEY_ID=abc123 -r slmolloy/CoffeeNowApp
travis encrypt ARTIFACTS_AWS_SECRET_ACCESS_KEY=abc123 -r slmolloy/CoffeeNowApp
```