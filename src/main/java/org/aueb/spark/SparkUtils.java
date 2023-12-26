package org.aueb.spark;

import org.apache.spark.sql.SparkSession;

public class SparkUtils {

    private static final String APP_NAME = "Spark Application";
    private static final String MASTER_LOCATION = "local[*]";

    public static SparkSession createSparkSession(){
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(MASTER_LOCATION)
                .getOrCreate();
    }
}
