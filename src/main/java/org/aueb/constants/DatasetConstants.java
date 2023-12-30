package org.aueb.constants;

import org.aueb.utils.reader.PropertyFileReader;

public class DatasetConstants {
    public static final String PROJECT_BASE_DIR = PropertyFileReader.getProperty("project.base.dir", "/home/");
    private DatasetConstants() {
    }

    public static final String DATA_SET_BASE_PATH = PROJECT_BASE_DIR + "src/main/resources/dataset/";
    public static final String AREAS_CSV_PATH = DATA_SET_BASE_PATH + "areas.csv";
    public static final String CASE_STATUS_CSV_PATH =  DATA_SET_BASE_PATH +"case_status.csv";
    public static final String CRIMES_CSV_PATH =  DATA_SET_BASE_PATH +"crimes.csv";
    public static final String CRIMINAL_CASES_CSV_PATH =  DATA_SET_BASE_PATH +"criminal_cases.csv";
    public static final String PREMISES_CSV_PATH =  DATA_SET_BASE_PATH +"premises.csv";
    public static final String VICTIM_DESCENT_CSV_PATH =  DATA_SET_BASE_PATH +"victim_descent.csv";
    public static final String WEAPONS_CSV_PATH =  DATA_SET_BASE_PATH +"weapons.csv";
    public static final String PIPE_DELIMITER = "|";

    public static final String NUMBER_OF_INCIDENTS = "Number_of_Incidents";
    public static final String COUNT = "count";

    public static final String DESCENT = "descent";
    public static final String VICTIM_SEX = "victim_sex";
    public static final String VICTIM_AGE = "victim_age";
    public static final String CRIMES_DESC = "crime_desc";
    public static final String STATUS_DESC = "status_desc";

    public static final String CASE_STATUS_ID = "case_status_id";
    public static final String STATUS_ID = "status_id";
    public static final String VICTIM_DESCENT_ID = "victim_descent_id";
    public static final String DESCENT_ID = "descent_id";
    public static final String CRIME_ID = "crime_id";

    public static final String ALL = "ALL";
}
