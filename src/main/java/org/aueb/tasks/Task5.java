/**
 * Author: p3312322 - Iason Chatzopoulos - Dec 2023.
 */
package org.aueb.tasks;

import org.apache.spark.sql.SaveMode;
import org.aueb.constants.ApplicationConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.col;
import static org.apache.spark.sql.functions.when;
import static org.aueb.constants.ApplicationConstants.OUTPUT_DIR;
import static org.aueb.constants.DatasetConstants.*;
import static org.aueb.spark.SparkUtils.createSparkSession;
import static org.aueb.utils.reader.CsvUtils.getDatasetFromCsv;

public class Task5 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task5.class);

    /**
     * Task Entry point. It will find the paths of the csv files for the criminal cases and victim's descent and
     * add them to a dataset which can be submitted to Spark.
     * @param args, not used.
     */
    public static void main(String[] args) {
        runTask5();
    }

    public static void runTask5() {
        var spark = createSparkSession();

        LOGGER.info("Parsing Criminal Cases CSV, path: {}", CRIMINAL_CASES_CSV_PATH);
        var criminalCasesCsvData =
                getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH);

        LOGGER.info("Parsing Victim Descent CSV, path: {}", VICTIM_DESCENT_CSV_PATH);
        var victimDescentCsvData =
                getDatasetFromCsv(spark, VICTIM_DESCENT_CSV_PATH);

        LOGGER.info("Joining the data of criminal cases with victim descent.");
        var joinedData = criminalCasesCsvData
                .join(victimDescentCsvData, criminalCasesCsvData.col(VICTIM_DESCENT_ID)
                        .equalTo(victimDescentCsvData.col(DESCENT_ID)));

        LOGGER.info("Generating data cube.");
        var dataCube = joinedData
                .cube(DESCENT, VICTIM_SEX, VICTIM_AGE)
                .count()
                .withColumnRenamed(COUNT, NUMBER_OF_INCIDENTS);

        LOGGER.info("Converting null values from data cube to 'ALL'.");
        var adjustedDataCube = dataCube
                .withColumn(DESCENT, when(col(DESCENT).isNull(), ALL)
                        .otherwise(col(DESCENT)))
                .withColumn(VICTIM_SEX, when(col(VICTIM_SEX).isNull(), ALL)
                        .otherwise(col(VICTIM_SEX)))
                .withColumn(VICTIM_AGE, when(col(VICTIM_AGE).isNull(), ALL)
                        .otherwise(col(VICTIM_AGE)));

        adjustedDataCube.show();

        LOGGER.info("Saving report: {}task-5", OUTPUT_DIR);
        adjustedDataCube.write().mode(SaveMode.Overwrite).option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(ApplicationConstants.OUTPUT_DIR + "task-5");

        LOGGER.info("Stopping Spark Session.");
        spark.stop();
    }

}
