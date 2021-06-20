echo "Copiando archivos ejecutables"
scp -prq dist/ sistemas@10.195.20.22:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.27:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.31:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.34:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.42:/home/sistemas/

echo "Copiando archivos de configuración"
scp -prq config/ sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copiando archivos de librerias"
scp -prq lib/ sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq lib/ sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq lib/ sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq lib/ sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq lib/ sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copiando config besa"
scp -prq confbesa.xml sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copiando archivos execute_benchmark.py"
scp -prq benchmark_* sistemas@10.195.20.22:/home/sistemas/dist/

echo "Copiando archivos data benchmark"
scp -prq *.csv sistemas@10.195.20.22:/home/sistemas/dist/

echo "Copia terminada"