package org.aueb.utils.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.aueb.constants.ApplicationConstants;
import org.aueb.constants.DatasetConstants;

import static org.aueb.constants.DatasetConstants.PIPE_DELIMITER;

/**
 * This utility class provides static methods in order to access csv documents in a spark context.
 */
public class CsvUtils {

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
                .option("delimiter", PIPE_DELIMITER)
                .csv(filePath);
    }
}
