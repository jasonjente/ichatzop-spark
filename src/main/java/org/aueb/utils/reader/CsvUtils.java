package org.aueb.utils.reader;

import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

/**
 * This utility class provides static methods in order to access csv documents in a spark context.
 */
public class CsvUtils {

    /**
     * Reads a CSV file into a Dataset<Row> (DataFrame) using Apache Spark.
     *
     * @param spark The SparkSession instance.
     * @param filePath The path to the CSV file.
     * @param hasHeader if the CSV file has a header.
     * @return A Dataset<Row> representing the CSV data.
     */
    public static Dataset<Row> getDatasetFromCsv(SparkSession spark,
                                                 String filePath,
                                                 boolean hasHeader,
                                                 final String delimiter) {
        return spark.read()
                .option("header", String.valueOf(hasHeader))
                .option("delimiter", "|")
                //.option("inferSchema", "true")
                .csv(filePath);
    }
}
