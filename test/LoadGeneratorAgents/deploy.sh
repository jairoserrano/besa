echo "Copiando archivos ejecutables"
scp -prq dist/ ubuntu@34.223.103.71:/home/ubuntu/
scp -prq dist/ ubuntu@35.80.34.231:/home/ubuntu/
scp -prq dist/ ubuntu@35.81.83.60:/home/ubuntu/
scp -prq dist/ ubuntu@34.222.210.254:/home/ubuntu/
scp -prq dist/ ubuntu@35.80.21.154:/home/ubuntu/

echo "Copiando archivos de configuración"
scp -prq config/ ubuntu@34.223.103.71:/home/ubuntu/dist/
scp -prq config/ ubuntu@35.80.34.231:/home/ubuntu/dist/
scp -prq config/ ubuntu@35.81.83.60:/home/ubuntu/dist/
scp -prq config/ ubuntu@34.222.210.254:/home/ubuntu/dist/
scp -prq config/ ubuntu@35.80.21.154:/home/ubuntu/dist/

echo "Copiando config besa"
scp -prq confbesa.xml ubuntu@34.223.103.71:/home/ubuntu/dist/
scp -prq confbesa.xml ubuntu@35.80.34.231:/home/ubuntu/dist/
scp -prq confbesa.xml ubuntu@35.81.83.60:/home/ubuntu/dist/
scp -prq confbesa.xml ubuntu@34.222.210.254:/home/ubuntu/dist/
scp -prq confbesa.xml ubuntu@35.80.21.154:/home/ubuntu/dist/

echo "Copia terminada"