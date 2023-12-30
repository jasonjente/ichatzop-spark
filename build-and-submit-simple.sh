#!/bin/bash
#Author: p3312322 - Iason Chatzopoulos - Dec 2023.
mvn clean install

export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
export PYSPARK_PYTHON=/usr/bin/python3
export JAR_DIR="/mnt/c/projects/Mathimata/metaptyxiako/BigDataSystems/assignments/assignment_2/Project2/ichatzop-spark/target/p3312322.jar"
export OUTPUT_TASK_DIR="/mnt/c/projects/Mathimata/metaptyxiako/BigDataSystems/assignments/assignment_2/Project2/ichatzop-spark/src/main/resources/output/"
export CLASS="--class org.aueb.tasks.Task"
export ARGS="--master spark://JasonJenteDesktop.localdomain:7077  --deploy-mode cluster"
export SUBMIT_CMD="/bin/spark-submit"

SECONDS=0

echo "Cleaning up previous executions."
for i in {1..5}; do
  rm -f $OUTPUT_TASK_DIR${i}/*
done

for i in {1..5}; do
  echo "Submitting task $i to Spark."
  $SPARK_HOME$SUBMIT_CMD $CLASS"$i" $ARGS $JAR_DIR >> "task_$i.out"
done

echo "Total processing time: $SECONDS seconds."

python3 scripts/create-pie-chart.py  $OUTPUT_TASK_DIR"task-2" "Top 10 Types of crime."
python3 scripts/create-histogram.py  $OUTPUT_TASK_DIR"task-2" "Top 10 Types of crime." "frequency" "Type of crime"
python3 scripts/create-line-chart.py $OUTPUT_TASK_DIR"task-3"
python3 scripts/create-bar-chart.py  $OUTPUT_TASK_DIR"task-3"



