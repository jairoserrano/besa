#!/usr/bin/python3
import os

def extract_time(data):
    return data.split(" ")[-1]

experiments = "Nodes,Agents,SmallTasks,MedTasks,BigTasks,Order,Cooperation,Balanced,Backup,Time\n"
with os.scandir("./results/") as entries:
    for entry in entries:
        if entry.is_file() and entry.name.endswith("00.txt"):
                with open(entry.name, 'r') as f:
                    last_line = f.readlines()[-1]
                experiments += entry.name.split("-")[0] + "," + extract_time(last_line)

with open('experiments_results.txt', 'a') as the_file:
    the_file.write(experiments)