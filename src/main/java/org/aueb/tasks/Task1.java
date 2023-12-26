package org.aueb.tasks;

import org.apache.spark.sql.SaveMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.col;
import static org.aueb.constants.ApplicationConstants.OUTPUT_DIR;
import static org.aueb.constants.DatasetConstants.*;
import static org.aueb.spark.SparkUtils.createSparkSession;
import static org.aueb.utils.reader.CsvUtils.getDatasetFromCsv;

/**
 * This class represents the first task for the assignment.
 * Step 1: Creates a Spark session.
 * Step 2: Loads the CSVs for the areas, premises and crimes into datasets.
 * Step 3: Joins the datasets by corresponding each crime with its area and type of premise.
 * USAGE:
 *       $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task1" --master local[1] target/p3312322.jar
 */
public class Task1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task1.class);

    /**
     * Task Entry point. It will find the paths of the csv files for the crimes, areas and premises and add them to a
     * dataset which can be submitted to Spark.
     * @param args, not used.
     */
    public static void main(String[] args) {
        runTask1();
    }

    public static void runTask1(){
        LOGGER.info("runTask1() - Enter - Starting task #1.");
        var spark = createSparkSession();

        LOGGER.info("Parsing Areas CSV, path: {}", AREAS_CSV_PATH);
        var areasCsvData = getDatasetFromCsv(spark, AREAS_CSV_PATH);

        LOGGER.info("Parsing Premises CSV, path: {}", PREMISES_CSV_PATH);
        var premisesCsvData = getDatasetFromCsv(spark, PREMISES_CSV_PATH);

        LOGGER.info("Parsing Criminal Cases CSV, path: {}", CRIMINAL_CASES_CSV_PATH);
        var criminalCasesCsvData = getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH);

        LOGGER.info("Joining the data of criminal cases with areas and premises");
        var joinedData = criminalCasesCsvData
                .join(areasCsvData, criminalCasesCsvData.col("area_id")
                        .equalTo(areasCsvData.col("area_id")))
                .join(premisesCsvData, criminalCasesCsvData.col("premis_id")
                        .equalTo(premisesCsvData.col("premis_id")));

        LOGGER.info("Generating report.");
        var report = joinedData.groupBy("area", "premis_desc")
                .count()
                .withColumnRenamed("count", "Number_of_Incidents")
                .orderBy(col("area").asc(), col("Number_of_Incidents")
                        .desc());

        report.show();
        
        LOGGER.info("Saving report: {}task-1", OUTPUT_DIR);
        report.write().mode(SaveMode.Overwrite)
                .option("header", "true")
                .option("delimiter", PIPE_DELIMITER)
                .csv(OUTPUT_DIR + "task-1");
        
        spark.stop();
    }

}
