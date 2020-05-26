#!/bin/bash

helpFunction()
{
   echo ""
   echo "Usage: $0 -e <env file> -f <file list> -p <path to CDR git home>"
   echo -e "\t-e <environment file>"
   echo -e "\t-f <file list>"
   echo -e "\t-p <Path for CDR GIT home directory>"
   exit 1 # Exit script after printing help
}

while getopts "e:f:p:" opt
do
   case "$opt" in
      e ) ENV_FILE="$OPTARG" ;;
      f ) FILE_LIST="$OPTARG" ;;
      p ) PING_CDR_HOME="$OPTARG" ;;
      ? ) helpFunction ;; # Print helpFunction in case parameter is non-existent
   esac
done

# Print helpFunction in case parameters are empty
if [ -z "$ENV_FILE" ] || [ -z "$FILE_LIST" ] || [ -z "$PING_CDR_HOME" ]
then
   echo "Some or all of the parameters are empty";
   helpFunction
fi

while read f; 
do 
        file=${PING_CDR_HOME}/${f}
	if [ -e $file ];
	then
	 ./create_subst_file.pl -e ${ENV_FILE} -i ${file}; 
	fi

done<$FILE_LIST
