echo "Copiando archivos execute_benchmark.py"
scp -prq execute_benchmark.py sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq execute_benchmark.py sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq execute_benchmark.py sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq execute_benchmark.py sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq execute_benchmark.py sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copiando archivos data benchmark"
scp -prq *.csv sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq *.csv sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq *.csv sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq *.csv sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq *.csv sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copia terminada"