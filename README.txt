comandos:
send mensaje                          envia un mensaje
list_user                             lista los nombres de todos los usuarios conectados
list_topic                            lista los topics (salas) y la cantidad de usarios
create_topic nombreSala               crea una sala
join_topic nombreSala                 se une a una sala existente
leave_topic                           abandona la sala (si queda vacia se borra automaticamente)
quit                                  se desconecta pero no cierra la ventana

como abrir el programa 
servidor:
cd hatsa la carpeta contenedora del jar
java -jar servidor.jar
ingresar los datos que le valla pidiendo (port)
cd hatsa la carpeta contenedora del jar
java -jar cliente.jar
ingresar los datos que le valla pidiendo (ip,port, nick)