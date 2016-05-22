# Travis YAML
The Travis CI build system monitors git repositories for changes. When a change
is detected the build system looks in the root of the git repository for a
```.travis.yml``` file configure the build. This configuration file can
instruct Travis to build, test, deploy and report results.

# Travis YAML For Android
The CoffeeNowApp uses a fairly standard ```.travis.yml``` file for Android with
the addition of both testing and S3 artifact deploy capabilities. Below is the
basic Android YAML file.
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
# Secure Environment Variables
```bash
travis encrypt ARTIFACTS_AWS_ACCESS_KEY_ID=abc123 -r slmolloy/CoffeeNowApp
travis encrypt ARTIFACTS_AWS_SECRET_ACCESS_KEY=abc123 -r slmolloy/CoffeeNowApp
```