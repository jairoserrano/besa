echo "Copiando archivos de configuración"
scp -prq config/ sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq config/ sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copiando config besa"
scp -prq confbesa.xml sistemas@10.195.20.22:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.27:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.31:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.34:/home/sistemas/dist/
scp -prq confbesa.xml sistemas@10.195.20.42:/home/sistemas/dist/

echo "Copia terminada"