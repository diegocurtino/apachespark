# LogStreamProcessing #

This is the same trivial example presented in Ch. 10 of the book. It only differs from it in 

-  It allows to specify what word to look for (if desired) instead of searching for the word **error** only.
-  It includes the fake logs file as _resources_ of the application
-  It includes the fakelogs.sh script send the logs to the server (provided that _nmap's location_ is updated in it.

## Prerequisites to run ##

To be able to run this application using gradle in Windows, you need:

- git bash (or equivalent) and
- gradle wrapper installed in LogStreamProcessing directory
- nmap installed.


## Run application  ##

1. Open `git bash` (In Windows) or a terminal (any Unix OS) in your local copy of repo's LogStreamProcessing directory
2. Run `./gradlew build`

#### On Windows ####

##### Using manual input (interactively) #####

1. Build the app.
2. Open a git bash terminal anywhere and run:  
 `$ <nmap_directory>/ncat.exe -lk 7777`  
  Example:  
  `/nmap-7.60/ncat.exe -lk 7777`  
 **Failing to do this, will result in ConnectionRefused RuntimeException due to non-existent server.**

3. Open a Windows terminal (it doesn't work with git bash! or I couldn't get it working) and run:  
  `<spark_bin_dir>\spark-submit --verbose --class app.LogStreamProcessing build\libs\LogStreamProcessing.jar [word_searched]`  
  if `word_searched` is not specified, the default word (error) will be used (which is what the fake logs example uses in the book)
4. Go to the git bash's terminal open in Step 2 and type any stuff. 
5. See the results in:
   - The Windows terminal running in Step 3 or
   - The **Streaming** tab in [Spark's UI](https://localhost:4040)

##### Using _real_ (constant flow from fake logs files) input #####

1. Build the app.
2. Go to <LogStreamProcessing dir>\src\main\resources
3. Open git bash (or similar)
4. Run ./fakelogs.sh
5. Open a Windows CMD (it doesn't work with git bash!) and run:  
  `<spark_bin_dir>\spark-submit --verbose --class app.LogStreamProcessing build\libs\LogStreamProcessing.jar [word_searched]`  
  if `word_searched` is not specified, the default word (error) will be used (which is what the fake logs example uses in the book)
6. Go to [Spark's UI](https://localhost:4040) and see the results in the **Streaming** tab.