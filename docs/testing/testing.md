# Automated Testing
The goal of automated testing is to provide a quick and repeatable way to
verify changes to a code base will not cause regression. Good automated tests
help teams move quickly, make changes more confidently and improve the quality
of a project.

# CoffeeNowApp Testing
The CoffeeNowApp uses two styles of automated tests:
1. Unit tests
2. Integrated UI tests

The Espresso framework is used in the CoffeeNowApp for UI testing.

# Goals of Testing
The goal for unit tests is to provide nearly immediate feedback to a developer
about changes made to code. Unit tests should be focused on a small bit of code
and should not have a lot of dependencies.

The goal of integrated UI tests is to verify that the app as a whole behaves
as expected from the point of view of a user.