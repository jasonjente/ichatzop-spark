package org.aueb.tasks;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SaveMode;
import org.apache.spark.sql.SparkSession;
import org.aueb.constants.ApplicationConstants;
import org.aueb.constants.DatasetConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.apache.spark.sql.functions.col;
import static org.aueb.constants.DatasetConstants.*;
import static org.aueb.spark.SparkUtils.createSparkSession;
import static org.aueb.utils.reader.CsvUtils.getDatasetFromCsv;

/**
 * This class represents the first task for the assignment.
 * Step 1: Creates a spark session.
 * Step 2: Loads the CSVs for the areas, premises and crimes into datasets.
 * Step 3: Joins the data frames by corresponding each crime with its area and type of premise.
 * USAGE:
 *       $SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task1" --master local[1] target/ichatzop-0.0.1-SNAPSHOT.jar
 */
public class Task1 {
    private static final Logger LOGGER = LoggerFactory.getLogger(Task1.class);

    /**
     * Task Entry point. It will find the paths of the csv files for the crimes, areas and premises and add them to a
     * dataframe which can be submitted to spark.
     * @param args, not used/
     */
    public static void main(String[] args) {
        runTask1();
    }


    public static void runTask1(){
        SparkSession spark = createSparkSession();
        LOGGER.info("Parsing Areas CSV, path: {}, \n{}, \n{}", AREAS_CSV_PATH,
                DatasetConstants.CRIMES_CSV_PATH, PREMISES_CSV_PATH);

        Dataset<Row> areasCsvData = getDatasetFromCsv(spark, AREAS_CSV_PATH, true, DELIMITER);
        Dataset<Row> premisesCsvData = getDatasetFromCsv(spark, PREMISES_CSV_PATH, true, DELIMITER);
        Dataset<Row> criminalCasesCsvData = getDatasetFromCsv(spark, CRIMINAL_CASES_CSV_PATH, true, DELIMITER);

        //Data Preparation
        Dataset<Row> joinedData = criminalCasesCsvData
                .join(areasCsvData, criminalCasesCsvData.col("area_id").equalTo(areasCsvData.col("area_id")))
                .join(premisesCsvData, criminalCasesCsvData.col("premis_id").equalTo(premisesCsvData.col("premis_id")));


        Dataset<Row> report = joinedData.groupBy("area", "premis_desc")
                .count()
                .withColumnRenamed("count", "Number_of_Incidents")
                .orderBy(col("area").asc(), col("Number_of_Incidents").desc());

        // To show the result in the console
        report.show();

        // To save the result as a new CSV file
        report.write().mode(SaveMode.Overwrite).option("header", "true").csv(ApplicationConstants.OUTPUT_DIR);

        // Stop the Spark session
        spark.stop();
    }

}
