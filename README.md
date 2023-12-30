### Big Data analysis system using Apache Spark.
#### Part of an assignment for my masters degree.

### Dataset
The dataset consists of the criminal_cases.csv whose structure resembles that of a fact table and  the areas, 
case_Status, crimes, premises, victim_descent and weapons files acting as dimensions.

### Requirements
The requirements for this project were the following:

Write a spark application that will:
1. Generate a report with the total number of cases per area and type of premise.
2. Generate a report including the top 10 committed crimes.
3. Generate a report regarding the total number of cases for each year grouped on a monthly basis.
4. Generate a report regarding the type of crime, the status of the incident and the number of cases in some specified order.
5. Generate a data cube whose cells will contain the total number of cases per country of descent, sex and age of the victim.
6. For the reports of (2) and (3), present the data with the appropriate graphs like histograms or pie charts.

### Technical Stuff
- The project is executed on WSL with java 11.0.21 and maven 3.6.3 for the build.
- Windows 10 with WSL2 enabled running Ubuntu 22.04.3
- Spark 3.5.0, available from:
  - ```bash
    $ sudo apt install openjdk-11-jdk
    $ sudo apt install maven -y
    $ wget https://dlcdn.apache.org/spark/spark-3.5.0/spark-3.5.0-bin-hadoop3.tgz
    $ tar xvf spark-*
    $ sudo mv spark-3.5.0-bin-hadoop3 /opt/spark
    ```
- Init the master and the workers:

```bash
    $ export SPARK_HOME=/opt/spark
    $ export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
    $ export PYSPARK_PYTHON=/usr/bin/python3
    $ $SPARK_HOME/start-master.sh
    $ $SPARK_HOME/start-worker.sh -c 4 -m 512M spark://JasonJenteDesktop:7077
```
- Build Project:

```bash
    # Navigate to the directory of pom.xml and execute:
    $ mvn clean install
```

- Submit Tasks:
```bash
    # The following command will also output the result from spark.
    # Navigate to the directory of pom.xml and execute:
    $ export SPARK_HOME=/opt/spark
    $ export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
    $ export PYSPARK_PYTHON=/usr/bin/python3
    $ export JAR_DIR="target/p3312322.jar"

    $ $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task1" --master local[1] $JAR_DIR >> task_1.log
```

build-and-submit.sh:

This script accepts the following 3 or 4 arguments based on the type of execution:
- --standalone _<project.base.dir>_, where _<project.base.dir>_ is the same path as the 'pom.xml' file
- --clustered _<spark_host_url> <project.base.dir>_, where _<spark_host_url>_ corresponds to the URL of the Spark master
and _<project.base.dir>_ is the same path as the 'pom.xml' file.

Next, the script will clean the '_/opt/spark/conf/application.properties_' file which contains the variables:
- ```output.location```, 
- ```master.location ```and
- ```project.base.location```.

so that the script and Spark applications are aligned and looking at the same paths, and it will rebuild it with the 
values of the execution. Then, the command 'mvn clean install' is executed to produce the 'p33122322' jar, and the 
Java classes are submitted to Spark using the spark-submit command. Each submission is made in a separate thread for 
better time performance. 

Once the execution is completed and the CSV files with the results have been generated.

For the execution, the following binaries should be installed on the machine:
- ```sudo apt install default-jdk scala maven git python3 python3-pip -y```
- ```pip3 install pandas matplotlib```

Execution examples:

```bash
    # Clustered:
    $ ./build-and-submit-parallel.sh --clustered "spark://JasonJenteDesktop:7077 --deploy-mode cluster" "/home/user/project/"
    
    # Standalone:
    $ ./build-and-submit.sh --standalone "/home/user/project/"
```
### Java stuff
  - Generated the following method in a utility class creating a spark session which can be then used to submit the dataset csv files.
    ```java
    public static SparkSession createSparkSession(){
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(MASTER_LOCATION)
                .getOrCreate();
    }
    ```
    
    ```java
    /**
     * Reads a CSV file into a Dataset<Row> (dataset) using Apache Spark.
     *
     * @param spark The SparkSession instance.
     * @param filePath The path to the CSV file.
     * @return A Dataset<Row> representing the CSV data.
     */
      public static Dataset<Row> getDatasetFromCsv(SparkSession spark, String filePath) {
          return spark.read()
                .option("header", "true")
                .option("delimiter", DatasetConstants.PIPE_DELIMITER)
                .csv(filePath);
    }
    
    ```
  - To improve code readability and maintainability, I created a format of Tasks that can be easily generated and submitted 
   to spark even in a streamlined and automated fashion. Each task is responsible for creating and destroying the spark context,
  and load any CSV needed to spark and then perform join operations and group by's on the data.
  
### Performance

Spark can work in parallel allowing for the processing of the files to become extremely fast. 5 sequential submissions 
took ~34 seconds to  complete and as 5 separate processes, it managed to complete in 14 seconds.