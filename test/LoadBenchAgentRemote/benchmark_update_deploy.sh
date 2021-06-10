echo "Copiando archivos ejecutables"
scp -prq dist/ sistemas@10.195.20.22:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.27:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.31:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.34:/home/sistemas/
scp -prq dist/ sistemas@10.195.20.42:/home/sistemas/

echo "Copia terminada"