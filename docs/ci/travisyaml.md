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
a message will be displayed to the build logs.

## Running Android UI Tests
With an AVD running the Android UI test execution can start. Add a script
configuration after the before_script:
```yaml
script:
- ./gradlew installDebugAndroidTest
- adb shell input keyevent 82 &
- ./gradlew connectedDebugAndroidTest
```
The first step is to install the Android debug test apk. If multiple emulators
were running then the apk would be installed on all of them. After the install
is completed the adb shell command sends the menu press keyevent to the
emulator to wake the device up. This is useful as the install process may take
long enough that the device will go back to sleep. Now that the device is awake
the connectedDebugAndroidTest task can start to run the Android UI tests on all
connected emulators. This task will download the test results from the devices
it runs on.

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
went from 2:15 to 1:50 with these caches enabled.

# Publish Artifacts to Amazon S3
Builds in Travis CI can produce a lot of useful information. The build is now
generating an installable apk, unit test results, Android UI test results and
lint check results. If a build failure occurs it would be nice to be able to
see these results somewhere. Additionally if a build successes it would be nice
to simploy download the build apk. Eventually this process could be automated
and the apk could be signed and sent to the Google Play Store for release.

Based on the earlier guide for settings up an Amazon S3 bucket, the Travis CI
configuration can now be updated to push changes to S3.

## Setup Environment Variables
To setup Amazon S3 we need to configure a few environment variables to identify
our bucket and provide the credentials to upload artifacts.
After the android config and before the install config add an env section like
so:
```yaml
env:
  global:
  - ARTIFACTS_BUCKET=sunshineartifacts
```
The Amazon S3 credentials are needed too but they should not be added to the
```.travis.yml``` file in plain text. This is were the earlier installed travis
cli tool comes to play. Open a terminal and navigate to the root of the git
repository where the ```.travis.yml``` file is located. Run the following
commands to add encrypted environment variables to the travis configuration.
Replace the values for your specific configuration:
```bash
travis encrypt ARTIFACTS_KEY=YOUR_AWS_KEY -r YOUR_GITHUB_USERID/YOUR_GITHUB_PROJECT --add
travis encrypt ARTIFACTS_SECRET=YOUR_AWS_KEY_SECRET -r YOUR_GITHUB_USERID/YOUR_GITHUB_PROJECT --add
```
The ```--add``` flag will write the encrypted values to your ```.travis.yml```
file for you.

## Configure Artifacts Addon
The final step to getting artifacts pushed to S3 is to include the rules for
what to push to S3. At the very end of your travis config file add the
following rules:
```yaml
addons:
  artifacts:
    debug: false
    s3_region: us-west-2
    paths:
    - ./app/build/outputs/apk/app-debug.apk
    - ./app/build/reports/androidTests/connected
    - ./app/build/outputs/lint-results-debug_files
    - ./app/build/outputs/lint-results-debug.html
    - ./app/build/outputs/androidTest-results
    - ./app/build/test-results/debug
```
The s3_region may need to change depending on where the S3 bucket was setup.
This bucket was setup in the Oregon (us-west-2) region.

Now build artifacts will be published to Amazon S3 and can be viewed online.
The process of posting the artifact url back to Github is complex and not
covered by this tutorial.