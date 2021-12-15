#!/usr/bin/python3
import subprocess
import threading
import os
import sys
import time
import trace

from datetime import datetime
print(sys.argv)

besa = []
server_exec = {}


class KThread(threading.Thread):
    """A subclass of threading.Thread, with a kill() method."""

    def __init__(self, *args, **keywords):
        threading.Thread.__init__(self, *args, **keywords)
        self.killed = False

    def start(self):
        """Start the thread."""
        self.__run_backup = self.run
        self.run = self.__run
        threading.Thread.start(self)

    def __run(self):
        """Hacked run function, which installs the trace."""
        sys.settrace(self.globaltrace)
        self.__run_backup()
        self.run = self.__run_backup

    def globaltrace(self, frame, why, arg):
        if why == 'call':
            return self.localtrace
        else:
            return None

    def localtrace(self, frame, why, arg):
        if self.killed:
            if why == 'line':
                raise SystemExit()
        return self.localtrace

    def kill(self):
        self.killed = True


# Cantidad de parametros
if len(sys.argv) != 4:
    print("Error al ejecutar, debes usar la siguiente forma:")
    print("./execute_benchmark.py <ArchivoBenchmark> <localhost/lab> <Folder/RunName>")
    exit(1)

# Servidores para ejecución
if sys.argv[2] == "lab":
    server = {"00": "sistemas@10.195.20.22", "01": "sistemas@10.195.20.27",
              "02": "sistemas@10.195.20.31", "03": "sistemas@10.195.20.34", "04": "sistemas@10.195.20.42"}
    command_client = "cd /home/sistemas/dist/; java -cp BenchmarkBesa.jar:lib/ Benchmark.Main MAS_01_"
    command_worker = "cd /home/sistemas/dist/; java -cp BenchmarkBesa.jar:lib/ Benchmark.WorkerMain MAS_01_"
else:
    localhost = "jairos@127.0.0.1"
    server = {"00": localhost, "01": localhost,
              "02": localhost, "03": localhost, "04": localhost}
    command_client = "cd /home/jairos/BESA/besa/test/BenchmarkBesa/; java -cp dist/BenchmarkBesa.jar:lib/ Benchmark.Main MAS_00_"
    command_worker = "cd /home/jairos/BESA/besa/test/BenchmarkBesa/; java -cp dist/BenchmarkBesa.jar:lib/ Benchmark.WorkerMain MAS_00_"

# Filename of data
filename = sys.argv[1]
experiment_run_name = sys.argv[3]


def check_and_clean(output=""):

    global besa
    global server_exec

    errors = ["Exception", "REPORT:", "BESA.Kernel.System.SystemExceptionBESA:",
              "java.lang.OutOfMemoryError:", "java.lang.IndexOutOfBoundsException:", "java.lang.NullPointerException:"]

    output = str(output)

    print(output)

    check = any(ele in output for ele in errors)
    if check:
        print("Terminó " + command_client)
        for b in besa:
            b.kill()
        for se in server_exec:
            se.terminate()
        sp = subprocess.Popen(["killall", "-KILL", "java"],
                              shell=False, stdout=subprocess.PIPE)
    return check


def print_header(header):
    print("\n************************************")
    print(header)
    print("************************************\n")


# Función de lanzamiento del server
def launch_main(server_id, parametros):

    global server_exec
    global errors

    print("Lanzando experimento", "ssh",
          server[server_id], command_client + server_id + ' ' + parametros)

    try:
        results_folder = "./results/" + experiment_run_name + "/"
        os.mkdir(results_folder)
        print("Results folder created")
    except OSError:
        pass

    file2write = open(results_folder + parametros + "-" +
                      datetime.today().strftime('%Y%m%d%H%M%S') + "-" + server_id + ".txt", 'wb')

    server_exec[server_id] = subprocess.Popen(["ssh", server[server_id], command_client +
                                               server_id + ' ' + parametros], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    while True:
        output = server_exec[server_id].stdout.readline()
        if server_exec[server_id].poll() is not None:
            break
        if output:
            # print(output.strip())
            if check_and_clean(output):
                file2write.write(output)

    rc = server_exec[server_id].poll()
    file2write.close()
    print("Finaliza ", server[server_id],
          command_client + server_id + ' ' + parametros)
    check_and_clean()


def launch_worker(server_id, parametros):

    global server_exec
    global errors

    print("Lanzando experimento", "ssh",
          server[server_id], command_worker + server_id + ' ' + parametros)

    server_exec[server_id] = subprocess.Popen(["ssh", server[server_id], command_worker +
                                               server_id + ' ' + parametros], shell=False, stdout=subprocess.PIPE, stderr=subprocess.PIPE)

    while True:
        output = server_exec[server_id].stdout.readline()
        if server_exec[server_id].poll() is not None:
            break
        if output:
            # print(output.strip())
            check_and_clean(output)

    rc = server_exec[server_id].poll()

    print("Finaliza ", server[server_id],
          command_client + server_id + ' ' + parametros)


if __name__ == "__main__":

    # Revisando el experimento
    print("Revisando el experimento")

    with open(filename) as f:
        experiments = f.read().splitlines()

    # Recorriendo los experimentos
    print("Recorriendo los experimentos")

    for line in experiments:
        besa.clear()
        server_exec.clear()
        parametros = line.split(",")
        print("\n\n************************************")
        besa.append(KThread(target=launch_main, args=("00", line)))
        for n in range(1, int(parametros[1])+1):
            print("0" + str(n), line)
            besa.append(KThread(target=launch_worker,
                        args=("0" + str(n), line)))
        for b in besa:
            b.start()
        for b in besa:
            b.join()

        for i in range(10):
            time.sleep(1)
            print(". ", i, end=None)
