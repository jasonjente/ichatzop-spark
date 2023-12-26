#!/bin/bash

mvn clean install

$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task1" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/ichatzop-0.0.1-SNAPSHOT.jar"
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task2" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/ichatzop-0.0.1-SNAPSHOT.jar"
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task3" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/ichatzop-0.0.1-SNAPSHOT.jar"
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task4" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/ichatzop-0.0.1-SNAPSHOT.jar"
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task5" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/ichatzop-0.0.1-SNAPSHOT.jar"