#!/bin/sh
# Title: pre-install script
# Description: the pre-install script is responsible for preparing the system
#  to install the Web application.  Steps include stopping Tomcat if started and
#  archiving out the photos directory if it exists.
#
echo ""
echo "Starting pre-install script..."


# Obtain the PID for the running Tomcat server, if it exists
TOMCAT_PID=`ps -ef  |grep tomcat | grep java | grep -v grep | awk -F" " '{print $2}'`

# Check PID for value, if no value then Tomcat must not be running now.
if [ "$TOMCAT_PID" == "" ]; then
   echo "    Tomcat not found to be running... continuing on."
else
   # stop the Tomcat process
   echo "    Tomcat found to be running... stopping the Tomcat service..."
   if [ -f /etc/init.d/tomcat6 ]
   then
   	service tomcat6 stop
   fi
   
   # sleeping 6 seconds to give the process a chance to terminate
   sleep 6

  # Loop for 12 turns waiting a maximum of 60 seconds for the process to exit
  for (( i = 1; i <= 12; i++ )); do
    RUNNING_TOMCAT_PROCESS=`ps --noheaders -p $TOMCAT_PID`
    if [ "$RUNNING_TOMCAT_PROCESS" == "" ]; then
      # found Tomcat process, clear out the PID and exit the loop
      TOMCAT_PID=""  
      break;          
    else
      echo "        Tomcat process still running... waiting 5 seconds for the process terminate."
      # sleep 5 seconds waiting for the process to finish
      sleep 5
    fi   
  done

  # Check if the TOMCAT_PID still has a value, this means the process is still running
  if [ "$TOMCAT_PID" == "" ]; then
    echo "    Tomcat process stopped... continuing on."
  else
    echo "    The Tomcat process has not stopped... halting the script."
    # Exit the pre-install script with an error code
    exit -2
  fi
fi

# Clear out the old Web application
if [ -d /opt/tomcat6/current/eurekawebapps/ROOT ]; then
  echo "    Found the ROOT Web application directory... removing it."
  rm -rf /opt/tomcat6/current/eurekawebapps/ROOT
else
  echo "    Did not find the ROOT Web application directory... nothing to remove."
fi

echo "Ending pre-install script."

# exit with a successful exit code
exit 0
