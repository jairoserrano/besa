#!/usr/bin/python3
import subprocess
import threading

import sys

# Cantidad de parametros
if len(sys.argv) != 3:
    print("Error al ejecutar, debes usar la siguiente forma:")
    print("./execute_benchmark.py NODOS ArchivoBenchmark")
    exit(1)

# Servidores para ejecución
server = {"00": "sistemas@10.195.20.22", "01": "sistemas@10.195.20.27",
          "02": "sistemas@10.195.20.31", "03": "sistemas@10.195.20.34", "04": "sistemas@10.195.20.42"}

command = "java -cp LoadBenchAgentRemote.jar:lib/* ContainersLauncher.LoadContainer_"

filename = sys.argv[2]
nodos = sys.argv[1]

# Función de lanzamiento del server


def launch_server(server_id, parametros):
    print("Lanzando experimento", "ssh",
          server[server_id], "cd /home/sistemas/dist/;" + command + server_id + ' ' + parametros)
    p = subprocess.Popen(["ssh", server[server_id], "cd /home/sistemas/dist/;" + command +
                         server_id + ' ' + parametros], stdout=subprocess.PIPE)
    out = p.stdout.read()
    #print("Resultado", out)
    file2write = open(parametros + "-" + server_id + ".txt", 'wb')
    file2write.write(out)
    file2write.close()


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
        print("experimento", contador, line)
        contador += 1
        if parametros[0] == "1":
            print("Lanzando un nodo")
            besa00 = threading.Thread(
                target=launch_server, args=("00", line))
            besa01 = threading.Thread(
                target=launch_server, args=("01", line))
            besa01.start()
            besa00.start()
            besa01.join()
            besa00.join()
        elif parametros[0] == "2":
            print("Lanzando dos nodos")
            besa00 = threading.Thread(
                target=launch_server, args=("00", line))
            besa01 = threading.Thread(
                target=launch_server, args=("01", line))
            besa02 = threading.Thread(
                target=launch_server, args=("02", line))
            besa01.start()
            besa02.start()
            besa00.start()
            besa01.join()
            besa02.join()
            besa00.join()
        elif parametros[0] == "4":
            print("Lanzando cuatro nodos")
            besa00 = threading.Thread(
                target=launch_server, args=("00", line))
            besa01 = threading.Thread(
                target=launch_server, args=("01", line))
            besa02 = threading.Thread(
                target=launch_server, args=("02", line))
            besa03 = threading.Thread(
                target=launch_server, args=("03", line))
            besa04 = threading.Thread(
                target=launch_server, args=("04", line))
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
