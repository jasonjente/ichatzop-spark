/**
 * Author: p3312322 - Iason Chatzopoulos - Dec 2023.
 */
package org.aueb.constants;

import static org.aueb.constants.DatasetConstants.PROJECT_BASE_DIR;

public class ApplicationConstants {
    private ApplicationConstants(){
    }
    //Adjust accordingly to your spark environment.
    //public static final String MASTER_LOCATION = "spark://JasonJenteDesktop:7077";
    public static final String MASTER_LOCATION = "local[*]";
    public static final String OUTPUT_DIR = PROJECT_BASE_DIR + "src/main/resources/output/";
}
