# Continuous Integration
When done properly Continuous Integration (CI) can provide a huge increase in
productivity for a team, speeding up deliveries while increasing code quality.

# CI for the CoffeeNowApp
The CoffeeNowApp's CI has the following components. Each component is documented
in this section.
1. The app is hosted on Github which allows developers to submit pull requests
for new changes. As feedback is provided developers can update their pull
request with new changes.
2. Travis CI monitors the Github project for new pull requests as well as
updates to existing pull requests and will start a new build when changes are
detected.
3. The Travis CI build process includes building the source code into an
Android app, executing a suite of tests against the source code including the
new changes and finally running a Lint check which looks for common issues in
code.
4. Travis CI will post results from the build process to Amazon S3 cloud
storage. If a build fails, developers can review the results stored on S3. When
a build is successful the apk file can be posted to the Google Play Store.
5. The final results of the build will be posted back to the pull request on
Github to notify developers and maintainers about the results of the build.