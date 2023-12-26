#!/bin/bash

mvn clean install

export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
export PYSPARK_PYTHON=/usr/bin/python3

$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task1" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar" >> task_1.out
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task2" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar" >> task_2.out
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task3" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar" >> task_3.out
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task4" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar" >> task_4.out
$SPARK_HOME/bin/spark-submit --class "org.aueb.tasks.Task5" --master local[1] "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar" >> task_5.out

python3 scripts/create-pie-chart.py  "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/src/main/resources/output/task-2" "Top 10 Types of crime."
python3 scripts/create-histogram.py  "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/src/main/resources/output/task-2" "Top 10 Types of crime." "frequency" "Type of crime"
python3 scripts/create-line-chart.py "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/src/main/resources/output/task-3"
python3 scripts/create-bar-chart.py  "/mnt/c/projects/Mathimata/metaptyxiako/Big Data Systems/assignments/assignment_2/Project2/ichatzop-spark/src/main/resources/output/task-3"



