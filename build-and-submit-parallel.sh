#!/bin/bash
#Author: p3312322 - Iason Chatzopoulos - Dec 2023.

print_lines(){
  echo "=================================================================================================="
}

#==================================================================================================#
#                                       SETUP SECTION                                              #
#==================================================================================================#

### ONLY CHANGE THESE TO MATCH YOUR SYSTEM!
# make sure this matches the value of the variable 'org.aueb.constants.ApplicationConstants.MASTER_LOCATION'.
# Spark cluster if it exists, otherwise use: --master local[*] for standalone mode.
export SPARK_HOST="--master spark://JasonJenteDesktop.localdomain:7077"
#export SPARK_HOST="--master local[*]"

## Change this and make sure is the same as org.aueb.constants.DatasetConstants.PROJECT_BASE_DIR
export BASE_DIR="/mnt/c/projects/Mathimata/metaptyxiako/BigDataSystems/assignments/assignment_2/Project2/ichatzop-spark/"

# Cluster mode is not compatible with local[*]
# export ARGS="${SPARK_HOST}"  # Standalone mode
export ARGS="${SPARK_HOST} --deploy-mode cluster"

#Adjust only if Spark is installed in another directory.
export SUBMIT_CMD="/bin/spark-submit"

# Please do not change these.
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
export PYSPARK_PYTHON=/usr/bin/python3
export JAR_DIR="${BASE_DIR}/target/p3312322.jar"
export OUTPUT_TASK_DIR="${BASE_DIR}/src/main/resources/output/task-"
export CLASS="--class org.aueb.tasks.Task"


#==================================================================================================#
#                                          Main SECTION                                            #
#==================================================================================================#

print_lines
echo " p3312322 - Iason Chatzopoulos - START "
print_lines
echo "Building project"

#==================================================================================================#
#                                         Build SECTION                                            #
#==================================================================================================#

mvn clean install
print_lines

SECONDS=0

#==================================================================================================#
#                                   Task Submission SECTION                                        #
#==================================================================================================#

echo "Cleaning up previous executions."
for i in {1..5}; do
  rm -f $OUTPUT_TASK_DIR${i}/*
done

for i in {1..5}; do
  echo "Submitting task$i to Spark."
  print_lines
  $SPARK_HOME$SUBMIT_CMD $CLASS"$i" $ARGS $JAR_DIR >> "task_$i.out" &
  pid[$i]=$!
done

for i in {1..5}; do
  echo "Waiting for task$i to finish."
  print_lines
  wait ${pid[$i]}
done

INTERVAL=1
TIMEOUT=60
START_TIME=$(date +%s)
TASK_2_COMPLETED=0
TASK_3_COMPLETED=0
SCRIPTS_COMPLETED=0
echo "Waiting for CSV files to be generated."
print_lines

#==================================================================================================#
#                                   Graph Generation SECTION                                       #
#==================================================================================================#

while true; do
  CURRENT_TIME=$(date +%s)
  ELAPSED_TIME=$(( CURRENT_TIME - START_TIME ))

  if [ $ELAPSED_TIME -ge $TIMEOUT ]; then
    break
  fi

  if ls "${OUTPUT_TASK_DIR}2"/*.csv 1> /dev/null 2>&1 && [ $TASK_2_COMPLETED -eq 0 ]; then
    python3 scripts/create-pie-chart.py  "${OUTPUT_TASK_DIR}2" "Top 10 Types of crime."
    python3 scripts/create-histogram.py  "${OUTPUT_TASK_DIR}2" "Top 10 Types of crime." "frequency" "Type of crime"
    TASK_2_COMPLETED=$((TASK_2_COMPLETED+1))
  else
    echo "Waiting for Spark to finish the tasks, ${OUTPUT_TASK_DIR}2, checking again in $INTERVAL seconds."
  fi

  if ls "${OUTPUT_TASK_DIR}3"/*.csv 1> /dev/null 2>&1 && [ $TASK_3_COMPLETED -eq 0 ]; then
   python3 scripts/create-line-chart.py "${OUTPUT_TASK_DIR}3"
   python3 scripts/create-bar-chart.py  "${OUTPUT_TASK_DIR}3"
   TASK_3_COMPLETED=$((TASK_3_COMPLETED+1))
  else
    echo "Waiting for Spark to finish the tasks, ${OUTPUT_TASK_DIR}3, checking again in $INTERVAL seconds."
  fi

  if [ $TASK_2_COMPLETED -eq 1 ] && [ $TASK_3_COMPLETED -eq 1 ]; then
    break
  fi
  sleep $INTERVAL
done

#==================================================================================================#
#                                      End Main SECTION                                            #
#==================================================================================================#

echo "Total processing time: $SECONDS seconds."
print_lines
echo " p3312322 - Iason Chatzopoulos - FINISH "
print_lines