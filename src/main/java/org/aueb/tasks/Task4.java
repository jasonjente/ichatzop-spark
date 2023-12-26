package org.aueb.tasks;

import org.apache.spark.sql.*;
import org.aueb.constants.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.aueb.constants.ApplicationConstants.OUTPUT_DIR;
import static org.aueb.constants.DatasetConstants.*;
import static org.aueb.spark.SparkUtils.createSparkSession;
import static org.aueb.utils.reader.CsvUtils.getDatasetFromCsv;

/**
 * This class represents the fourth task for the assignment.
 * Step 1: Creates a Spark session.
 * Step 2: Loads the CSVs for the criminal cases and any necessary datasets for crime types and incident status.
 * Step 3: Joins the criminal cases data with the crime types data to map crime ids to names. It then performs a group
 *         by operation on crime type and incident status. It performs a count as an aggregate function and orders the
 *         results alphabetically by crime type and incident status. Finally, it displays the results and saves them
 *         under /output/task-4/.
 * USAGE:
 *       $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task4" --master local[1] target/p3312322.jar
 */
public class Task4 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task4.class);

    /**
     * Task Entry point. It will find the paths of the csv files for the criminal cases and crimes and add them to a
     * dataset which can be submitted to Spark.
     * @param args, not used.
     */
    public static void main(String[] args) {
        runTask4();
    }

    public static void runTask4() {
        LOGGER.info("runTask4() - Enter - Starting task #4.");
        var spark = createSparkSession();

        LOGGER.info("Parsing Criminal Cases CSV, path: {}", CRIMINAL_CASES_CSV_PATH);
        var criminalCasesCsvData =
                getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH);

        LOGGER.info("Parsing Crimes CSV, path: {}", CRIMES_CSV_PATH);
        var crimesCsvData =
                getDatasetFromCsv(spark, CRIMES_CSV_PATH);

        LOGGER.info("Parsing Case Status CSV, path: {}", CASE_STATUS_CSV_PATH);
        var caseStatusCsvData =
                getDatasetFromCsv(spark, CASE_STATUS_CSV_PATH);

        LOGGER.info("Joining the data of criminal cases with crimes");
        var joinedData = criminalCasesCsvData
                .join(crimesCsvData, criminalCasesCsvData.col("crime_id")
                        .equalTo(crimesCsvData.col("crime_id")))
                .join(caseStatusCsvData, criminalCasesCsvData.col("case_status_id")
                        .equalTo(caseStatusCsvData.col("status_id")));

        LOGGER.info("Generating report.");
        var incidentStatusReport = joinedData
                .groupBy(crimesCsvData.col("crime_desc"), joinedData.col("status_desc"))
                .count()
                .withColumnRenamed("count", "Number_of_Incidents")
                .orderBy("crime_desc", "status_desc");

        incidentStatusReport.show();

        LOGGER.info("Saving report: {}task-4", OUTPUT_DIR);
        incidentStatusReport.write().mode(SaveMode.Overwrite)
                .option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(ApplicationConstants.OUTPUT_DIR + "task-4");

        spark.stop();
    }

}
