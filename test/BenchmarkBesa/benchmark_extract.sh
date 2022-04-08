#!/usr/bin/bash

grep "REPORT:" ./results/$1/* | awk -F ' ' '{print $3}' > ./results/$1/reporte_final.csv
