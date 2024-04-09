--------
Overview
--------
Given the 2 sample files: "first.html" and "second.csv" under the "data" directory,
write a Java program that performs the following:

1. In first.html it contains a table called "directory". Read and extract the table.
2. Read and extract the table from second.csv.
3. Combine and merge these 2 tables into 1 by consolidating duplicated columns and write the results in a file named "combined.csv".
   - For the merge operation, assume "ID" is unique.
   - The "ID" column in the output file "combined.csv" should be sorted in ascending order.
   - The column headers do not need to be in multiple languages.

-----------------
Expected Outcome
-----------------
- Your program must generate "combined.csv"
- "combined.csv" must contain a header row and no duplicate IDs.
- the IDs in "combined.csv" must be sorted in ascending order.

Program works with:
    a. any number of input files.
    b. any number of columns in the csv or html files.
    c. the data can be in any language (Chinese, French, English, etc).

--------------------------
To compile and run
--------------------------
- You need JDK for Java 8 or above
- This exercise does not assume an IDE or any build tool except for a command line shell.  It includes simple scripts for typical dev environments.
  You can make minor modifications to these files if necessary.
 * Unix-like:
   * build.sh: cleans, compiles and creates cantest.jar file, and a veeva_solution.zip file for submission.
   * run.sh: runs cantest.jar
 * Windows:
   * build.bat: cleans, compiles and creates cantest.jar file, and a veeva_solution.zip file for submission.
   * run.bat: runs cantest.jar
