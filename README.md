# osrank-java
Java implementation of Osrank


## How to get set up with IntelliJ

Right now IntelliJ is the main IDE that we're using to develop. This will seem quite heavyweight to some developers, but is really nice and very powerful once it's set up. An alternative would be to use a lighter-weight editor like Emacs or Sublime and then simply build using Maven, or manually. If you'd like to do that but don't know how, reach out to @andrewpdickson for help.

1. Install Java JDK. Any reasonably recent version:

https://www.oracle.com/technetwork/java/javase/downloads/index.html

2. Install IntelliJ IDEA. The community edition is fine. Any reasonably recent version:

https://www.jetbrains.com/idea/download/#section=mac

3. Clone the repo into your local environment.

`git clone https://github.com/oscoin/osrank-java.git`

4. Open IntelliJ IDEA. Choose "Import Project".

5. Select the pom.xml in the base project directory.

6. A project import detail screen appears. Make sure "Import Maven projects automatically" is checked, and click "Next".

7. Click "Next" two more times and then "Finish".

8. The project should now be open. You can try running OsrankNaiveSimpleGraphApp by finding that file in a sub-folder of
src/ and then right-clicking on it and choosing to run or debug it. To change the parameters, open that file and change
the values of the various constants.

9. You're all set up -- enjoy!

## How To Run One Of The Apps From The Command line

1. Add a JAR as a "Build Artifact" to your Project Structure
https://stackoverflow.com/questions/1082580/how-to-build-jars-from-intellij-properly/45303637#45303637

2. Build the JAR
Build -> BuildProject or Build -> Artifacts

3. Run from command line:
`java -jar out/artifacts/osrank_jar/osrank.jar metadataFilePath=./metadata.csv dependenciesFilePath=./dependencies.csv contributionsFilePath=./contributions.csv`





