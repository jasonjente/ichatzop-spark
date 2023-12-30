package org.aueb.constants;

import org.aueb.utils.reader.PropertyFileReader;

public class ApplicationConstants {
    private ApplicationConstants(){
    }

    public static final String MASTER_LOCATION = PropertyFileReader.getProperty("master.location", "local[*]");
    public static final String OUTPUT_DIR = PropertyFileReader.getProperty("output.location", "/home/output/");
}
