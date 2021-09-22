#!/usr/bin/python3
import subprocess
import threading
import os
import sys
import time

from datetime import datetime

# Cantidad de parametros
if len(sys.argv) != 3:
    print("Error al ejecutar, debes usar la siguiente forma:")
    print("./execute_benchmark.py <ArchivoBenchmark> <localhost/lab>")
    exit(1)

# Servidores para ejecución
if sys.argv[2] == "lab":
    server = {"00": "sistemas@10.195.20.22", "01": "sistemas@10.195.20.27",
              "02": "sistemas@10.195.20.31", "03": "sistemas@10.195.20.34", "04": "sistemas@10.195.20.42"}
    command_client = "cd /home/sistemas/dist/; java -cp BenchmarkBesa.jar:lib/ Benchmark.Main MAS_01_"
    command_worker = "cd /home/sistemas/dist/; java -cp BenchmarkBesa.jar:lib/ Benchmark.WorkerMain MAS_01_"
else:
    localhost = "jairo@127.0.0.1"
    server = {"00": localhost, "01": localhost,
              "02": localhost, "03": localhost, "04": localhost}
    command_client = "cd /home/jairo/BESA/besa/test/BenchmarkBesa/; java -cp dist/BenchmarkBesa.jar:lib/ Benchmark.Main MAS_00_"
    command_worker = "cd /home/jairo/BESA/besa/test/BenchmarkBesa/; java -cp dist/BenchmarkBesa.jar:lib/ Benchmark.WorkerMain MAS_00_"

# Filename of data
filename = sys.argv[1]


def print_header(header):
    print("\n************************************")
    print(header)
    print("************************************\n")


# Función de lanzamiento del server
def launch_main(server_id, parametros):
    print("Lanzando experimento", "ssh",
          server[server_id], command_client + server_id + ' ' + parametros)
    p = subprocess.Popen(["ssh", server[server_id], command_client +
                         server_id + ' ' + parametros], stdout=subprocess.PIPE)
    out = p.stdout.read()

    results_folder = "./results/"
    try:
        os.mkdir(results_folder)
        print("Results folder created")
    except OSError:
        pass

    file2write = open(results_folder + parametros + "-" + datetime.today().strftime('%Y%m%d%H%M%S') +
                      "-" + server_id + ".txt", 'wb')
    file2write.write(out)
    file2write.close()


def launch_worker(server_id, parametros):
    print("Lanzando experimento", "ssh",
          server[server_id], command_worker + server_id + ' ' + parametros)
    p = subprocess.Popen(["ssh", server[server_id], command_worker +
                         server_id + ' ' + parametros], stdout=subprocess.PIPE)
    out = p.stdout.read()


if __name__ == "__main__":
    # Revisando el experimento
    print("Revisando el experimento")
    with open(filename) as f:
        experiments = f.read().splitlines()

    # Recorriendo los experimentos
    print("Recorriendo los experimentos")
    contador = 1
    for line in experiments:
        besa00, besa01, besa02, besa03, besa04 = None, None, None, None, None
        parametros = line.split(",")
        print("\n\n************************************")
        print("experimento", contador, line)
        contador += 1
        for i in range(10):
            time.sleep(1)
            print(". ", i)
        if parametros[1] == "1":
            print_header("Lanzando experimento en un nodo ")
            besa00 = threading.Thread(
                target=launch_main, args=("00", line))
            besa01 = threading.Thread(
                target=launch_worker, args=("01", line))
            besa01.start()
            besa00.start()
            besa01.join()
            besa00.join()
        elif parametros[1] == "2":
            print_header("Lanzando experimento en dos nodos ")
            besa00 = threading.Thread(
                target=launch_main, args=("00", line))
            besa01 = threading.Thread(
                target=launch_worker, args=("01", line))
            besa02 = threading.Thread(
                target=launch_worker, args=("02", line))
            besa01.start()
            besa02.start()
            besa00.start()
            besa01.join()
            besa02.join()
            besa00.join()
        elif parametros[1] == "4":
            print_header("Lanzando experimento en cuatro nodos ")
            besa00 = threading.Thread(
                target=launch_main, args=("00", line))
            besa01 = threading.Thread(
                target=launch_worker, args=("01", line))
            besa02 = threading.Thread(
                target=launch_worker, args=("02", line))
            besa03 = threading.Thread(
                target=launch_worker, args=("03", line))
            besa04 = threading.Thread(
                target=launch_worker, args=("04", line))
            besa01.start()
            besa02.start()
            besa03.start()
            besa04.start()
            besa00.start()
            besa01.join()
            besa02.join()
            besa03.join()
            besa04.join()
            besa00.join()
