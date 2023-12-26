package org.aueb.tasks;

import org.apache.spark.sql.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.*;
import static org.aueb.constants.ApplicationConstants.OUTPUT_DIR;
import static org.aueb.constants.DatasetConstants.*;
import static org.aueb.spark.SparkUtils.createSparkSession;
import static org.aueb.utils.reader.CsvUtils.getDatasetFromCsv;

/**
 * This class represents the second task for the assignment.
 * Step 1: Creates a Spark session.
 * Step 2: Loads the CSVs for the criminal cases.
 * Step 3: Performs a group by crime id to perform a count as an aggregate function and then display the results in
 *         descending order and saves them under /output/task-2/.
 * USAGE:
 *       $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task2" --master local[1] target/p3312322.jar
 */
public class Task2 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task2.class);

    /**
     * Task Entry point. It will find the paths of the csv files for the criminal cases and add them to a
     * dataset which can be submitted to Spark.
     * @param args, not used.
     */
    public static void main(String[] args) {
        runTask2();
    }

    public static void runTask2(){
        LOGGER.info("runTask2() - Enter - Starting task #2.");
        SparkSession spark = createSparkSession();

        LOGGER.info("Parsing Criminal Cases CSV, path: {}", CRIMINAL_CASES_CSV_PATH);
        Dataset<Row> criminalCasesCsvData = getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH);

        LOGGER.info("Parsing Crimes CSV, path: {}", CRIMES_CSV_PATH);
        Dataset<Row> crimesCsvData = getDatasetFromCsv(spark, CRIMES_CSV_PATH);

        LOGGER.info("Joining the data of criminal cases with crimes");
        Dataset<Row> joinedData = criminalCasesCsvData
                .join(crimesCsvData, criminalCasesCsvData.col("crime_id")
                        .equalTo(crimesCsvData.col("crime_id")));

        LOGGER.info("Generating report.");
        Dataset<Row> crimeTypeCount = joinedData
                .groupBy(joinedData.col("crime_desc"))
                .count()
                .withColumnRenamed("count", "Number_of_Incidents")
                .orderBy(col("Number_of_Incidents").desc())
                .limit(10);

        crimeTypeCount.show();

        LOGGER.info("Saving report: {}task-2", OUTPUT_DIR);
        crimeTypeCount.write().mode(SaveMode.Overwrite).option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(OUTPUT_DIR + "task-2");
        spark.stop();

    }


}
