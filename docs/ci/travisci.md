# Travis CI
Travis CI is a free cloud based continuous integration service that integrates
nicely with Github projects to provide automated build and testing of changes
to the repository. Travis is able to report CI results back to Github.

# Using Travis CI
Travis CI has gained a lot of popularity because it is easy to use, setup and
maintain. Unlike other continuous integration systems were the configuration
lives on a remote server or on one of many build slaves, Travis CI puts nearly
all of the build configuration into a single ```.travis.yml``` file that lives
along side the code in the repository.
## Developer Use Case
Developers contributing to a project using Travis CI need only push changes or
submit pull requests as they normally would for the CI system to get involved.
Travis CI can provide results to the pull request for the developer to view.

## Maintainer Use Case
If are a project maintainer and want to setup your project to use Travis CI you
will need to complete the following steps:
1. Create a ```.travis.yml``` file in the root of your git repository.
Read the [getting started guide](https://docs.travis-ci.com/user/getting-started/)
for more help on this. I'll be discussing the specifics of the CoffeeNowApp
```.travis.yml``` file in a later section.
2. Sign into [https://travis-ci.com](https://travis-ci.com) with your Github
account.
3. Under your account profile you will see a listing of your Github projects,
select the projects you want to allow Travis CI to integrate with.

## Advanced Maintainer Use Case
The CoffeeNowApp uses more advanced features of Travis CI. You will need to
setup your machine for working with Travis CI from the command line interface
(CLI).

You will need to have ruby 1.9.3 or greater installed and then install the
Travis CLI.
```bash
gem install travis -v 1.8.2 --no-rdoc --no-ri
```
The Travis CI github page has good documentation on how to setup the CLI tool
if you run into problems.
[https://github.com/travis-ci/travis.rb#installation](https://github.com/travis-ci/travis.rb#installation)