/**
 * Author: p3312322 - Iason Chatzopoulos - Dec 2023.
 */
package org.aueb.spark;

import org.apache.spark.sql.SparkSession;

import static org.aueb.constants.ApplicationConstants.MASTER_LOCATION;

public class SparkUtils {
    private SparkUtils() {
    }

    private static final String APP_NAME = "Spark Application";

    public static SparkSession createSparkSession(){
        return SparkSession.builder()
                .appName(APP_NAME)
                .master(MASTER_LOCATION)
                .getOrCreate();
    }
}
