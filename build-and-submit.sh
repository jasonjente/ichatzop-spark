#!/bin/bash
## EN
#  This script accepts the following 3 or 4 arguments based on the type of execution:
#    --standalone <project.base.dir>, where <project.base.dir> is the same path as the 'pom.xml' file
#    --clustered <spark_host_url> <project.base.dir>, where <spark_host_url> corresponds to the URL of the Spark master
#  and <project.base.dir> is the same path as the 'pom.xml' file.
#  Next, the script will clean the '/opt/spark/conf/application.properties' file which contains the variables output.location, master.location, and project.base.location so that the script and Spark applications are aligned and looking at the same paths, and it will rebuild it with the values of the execution. Then, the command 'mvn clean install' is executed to produce the 'p33122322' jar, and the Java classes are submitted to Spark using the spark-submit command. Each submission is made in a separate thread for better time performance. Once the execution is completed and the CSV files with the results have been generated.

#  For the execution, the following should be installed on the machine:
#   sudo apt install default-jdk scala maven git python3 python3-pip -y
#   pip3 install pandas matplotlib

#Execution examples:

#    Clustered:
#    $ /opt/spark/sbin/start-master.sh --webui-port 8088
#    $ /opt/spark/sbin/start-worker.sh -c 10 -m 12G spark://JasonJenteDesktop:7077
#    $ ./build-and-submit-parallel.sh --clustered "spark://JasonJenteDesktop:7077" "/home/jasonjente/test/"
#    Standalone:
#    $ ./build-and-submit.sh --standalone /home/jasonjente/test/

#==================================================================================================#
#                                   FUNCTIONS SECTION                                              #
#==================================================================================================#

print_lines(){
  echo "=================================================================================================="
}

usage() {
    echo "Usage: $0 [--standalone | --clustered <spark://PathToSparkCluster>] <project.base.dir> "
    echo "  --standalone: To execute spark applications in standalone mode."
    echo "  --clustered <spark://PathToSparkCluster>: To execute spark applications in clusters."
    exit 1
}

#==================================================================================================#
#                                       SETUP SECTION                                              #
#==================================================================================================#
rm -f /opt/spark/conf/application.properties

case $1 in
    --standalone)
        echo "master.location=local[*]" >> /opt/spark/conf/application.properties
        export SPARK_HOST="local[*]"
        export ARGS="--master ${SPARK_HOST}"
        export BASE_DIR="$2"
        echo "project.base.dir=$BASE_DIR" >> /opt/spark/conf/application.properties
        ;;
    --clustered)
        if [ -z "$2" ]; then
            echo "Error: --clustered requires a master URL."
            usage
        else
            export SPARK_HOST=$2
            echo "master.location=$SPARK_HOST" >> /opt/spark/conf/application.properties
            export ARGS="--master ${SPARK_HOST} --deploy-mode cluster"
            export BASE_DIR="$3"
            echo "project.base.dir=$BASE_DIR" >> /opt/spark/conf/application.properties
        fi
        ;;
    *)
        usage
        ;;
esac

#Adjust only if Spark is installed in another directory.
export SUBMIT_CMD="/bin/spark-submit"

# Please do not change these.
export SPARK_HOME=/opt/spark
export PATH=$PATH:$SPARK_HOME/bin:$SPARK_HOME/sbin:.
export PYSPARK_PYTHON=/usr/bin/python3
export JAR_DIR="${BASE_DIR}target/p3312322.jar"
export OUTPUT_TASK_DIR="${BASE_DIR}output/task-"
export CLASS="--class org.aueb.tasks.Task"

echo "output.location=${BASE_DIR}output/" >> /opt/spark/conf/application.properties

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