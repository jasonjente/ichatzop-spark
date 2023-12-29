/**
 * Author: p3312322 - Iason Chatzopoulos - Dec 2023.
 */
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
 * This class represents the third task for the assignment.
 * Step 1: Creates a Spark session.
 * Step 2: Loads the CSVs for the criminal cases.
 * Step 3: Extracts the year and month from the date of incidents and performs a group by operation on them.
 *         It then performs a count as an aggregate function and orders the results by year and month in ascending order.
 *         It also displays the results and saves them under /output/task-3/.
 * USAGE:
 *       $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task3" --master local[1] target/p3312322.jar
 */
public class Task3 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task3.class);
    private static final String YEAR = "year";
    private static final String MONTH = "month";
    private static final String DATE_OCCURRED = "date_occurred";

    /**
     * Task Entry point. It will find the paths of the csv files for the criminal cases and add them to a
     * dataset which can be submitted to Spark.
     * @param args, not used.
     */
    public static void main(String[] args) {
        runTask3();
    }

    public static void runTask3() {
        LOGGER.info("runTask3() - Enter - Starting task #3.");
        var spark = createSparkSession();

        LOGGER.info("Parsing Criminal Cases CSV, path: {}", CRIMINAL_CASES_CSV_PATH);
        var criminalCasesCsvData =
                getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH);

        LOGGER.info("Generating report.");
        var monthlyReport = criminalCasesCsvData
                .withColumn(YEAR, year(col(DATE_OCCURRED)))
                .withColumn(MONTH, month(col(DATE_OCCURRED)))
                .groupBy(YEAR, MONTH)
                .count()
                .withColumnRenamed(COUNT, NUMBER_OF_INCIDENTS)
                .orderBy(YEAR, MONTH);

        monthlyReport.show();

        LOGGER.info("Saving report: {}task-3", OUTPUT_DIR);
        monthlyReport.write().mode(SaveMode.Overwrite)
                .option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(OUTPUT_DIR + "task-3");

        LOGGER.info("Stopping Spark Session.");
        spark.stop();
    }

}
