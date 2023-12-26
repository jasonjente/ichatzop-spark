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
                .join(victimDescentCsvData, criminalCasesCsvData.col("victim_descent_id")
                        .equalTo(victimDescentCsvData.col("descent_id")));

        LOGGER.info("Generating data cube.");
        var dataCube = joinedData
                .cube("descent", "victim_sex", "victim_age")
                .count()
                .withColumnRenamed("count", "Number_of_Incidents");

        LOGGER.info("Converting null values from data cube to 'ALL'.");
        var adjustedDataCube = dataCube
                .withColumn("descent", when(col("descent").isNull(), "ALL")
                        .otherwise(col("descent")))
                .withColumn("victim_sex", when(col("victim_sex").isNull(), "ALL")
                        .otherwise(col("victim_sex")))
                .withColumn("victim_age", when(col("victim_age").isNull(), "ALL")
                        .otherwise(col("victim_age")));

        adjustedDataCube.show();

        LOGGER.info("Saving report: {}task-5", OUTPUT_DIR);
        adjustedDataCube.write().mode(SaveMode.Overwrite).option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(ApplicationConstants.OUTPUT_DIR + "task-5");

        spark.stop();
    }

}
