#ProcesJsonFiles

This application takes the code from book's chapter 5 and develops it to resemble a _fully_ fledged Java application that can be run from OS's CLI to perform 3 different transformations over the input data. 

It also contains code to generate different json files from the IDE. **Notice** that changing those json files will result in unit tests failing. That part of the application could be refined to allow the user decide where to output the json files. 

##Prerequisites to run

To be able to run this application using gradle in Windows, you need:

- git bash (or equivalent) and
- gradle wrapper installed in **ProcesJsonFiles** directory


##Run application

###On Windows

1. Open `git bash` (In Windows) or a terminal (any Unix OS) in your local copy of repo's **ProcesJsonFiles** directory
2. Run `./gradlew run <inputFilename> <outputDirectory>`

where  
`<inputFilename>` is the file where you want to count words and  
`<outputDirectory>` is where you want Spark to save the results. 

Example:
`./gradlew run -Dexec.args="src/test/resources/minimal_people_singleLine.json out/results`

This example uses apps's json `minimal_people_singleLine.json` file containing data about a single person (in a json file having all of its content in a **one-line** format (ie. not so human friendly readable) and outputs the results in the `out/results` directory that will be recreated if it already exists.

Once run, the application will request the user to input an option to decide what kind of data analysis is to be done as follows:

    What do you want to do with people's data set?
    ----------------------------------------------
    1. Extract number of people per nationality
    2. Extract number of siblings each person has
    3. Calculate average of siblings

If no valid option (i.e. 1, 2 or 3) is input, the application will repeat this text until a valid option is selected or the application is killed.

##Output

###Console output
After running it, in between all the INFO that Sparks prints in the console, you should something like this:

    2018-01-22 12:31:08,223 [main] INFO  app.internal.PeopleRdd - Spark will process src/test/resources/minimal_people_singleLine.json file
    ...
    18/01/22 12:31:09 INFO SparkContext: Created broadcast 0 from textFile at PeopleRdd.java:24
    18/01/22 12:31:09 INFO FileInputFormat: Total input paths to process : 1
    2018-01-22 12:31:09,693 [main] INFO  app.utils.filesystem.FileSystemOperations - Directory C:\SparkHadoopTutorials\ProcessJsonFiles\out\results does not exist. Nothing to delete
    2018-01-22 12:31:09,693 [main] INFO  app.utils.filesystem.FileSystemOperations - Processing results will be output in out/results directory

###Spark files
Again, assuming you run the example above you should have a new directory `out/results` inside your local `ProcesJsonFiles` directory containing 4 files. The count of people per nationality is in the `part-00000` file. 

The output should be this:

    (Elbonian,1)

## Unit testing

Unit tests for each one of the transformations offered by the application are included. They can be run from a gradle task.

If you run them like this:

    ./gradlew clean test

You will be get an output like this:

    :clean
    :compileJava
    :processResources
    :classes
    :compileTestJava
    :processTestResources
    :testClasses
    :junitPlatformTest
    2018-01-22 12:43:17,207 [main] INFO  app.PeopleRddTransformationTest - Starting and setting up Spark: default context, localhost...
    18/01/22 12:43:18 WARN NativeCodeLoader: Unable to load native-hadoop library for your platform... using builtin-java classes where applicable
    2018-01-22 12:43:20,776 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\minimal_people_singleLine.json file
    2018-01-22 12:43:23,570 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\simple_people_singleLine.json file
    2018-01-22 12:43:23,827 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\minimal_people_singleLine.json file
    2018-01-22 12:43:23,962 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\simple_people_singleLine.json file
    2018-01-22 12:43:24,082 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\minimal_people_singleLine.json file
    2018-01-22 12:43:24,430 [main] INFO  app.internal.PeopleRdd - Spark will process C:\SparkHadoopTutorials\ProcessJsonFiles\build\resources\test\simple_people_singleLine.json file
    2018-01-22 12:43:24,667 [main] INFO  app.PeopleRddTransformationTest - Stopping Spark...

    Test run finished after 8760 ms
    [         5 containers found      ]
    [         0 containers skipped    ]
    [         5 containers started    ]
    [         0 containers aborted    ]
    [         5 containers successful ]
    [         0 containers failed     ]
    [         6 tests found           ]
    [         0 tests skipped         ]
    [         6 tests started         ]
    [         0 tests aborted         ]
    [         6 tests successful      ]
    [         0 tests failed          ]

    :test SKIPPED

    BUILD SUCCESSFUL in 16s

## Json files creation

TBC