<?php 
     //tipo de archivo que se va a enviar
     $tipo = "xml";
     //rut de la empresa
     $rut = "11111111-1";
     //url del web service 
     $url='https://develop.ingefactura.cl/wsRest/web/com/dte';
     //token de acceso (Se debe pedir a soporte de ingefactura)
     $token='xxxxxxxxxxxxxxxxxxxxxx';
     // se toma la ruta del archivo
     $file_name = realpath('ruta del archivo');
     $fichero = file_get_contents($file_name);     

     $ch = curl_init();

     curl_setopt($ch, CURLOPT_URL, $url);
     curl_setopt($ch, CURLOPT_POST, true);
     curl_setopt($ch, CURLOPT_RETURNTRANSFER, true);
     curl_setopt($ch, CURLOPT_POSTFIELDS, $fichero);
     curl_setopt($ch, CURLOPT_HTTPHEADER, array('Authorization:  Bearer ' . $token ,
                                                     'Content-Type: application/xml' ,
                                                            'rut: ' .$rut   ,
                                                                 'tipo:'.$tipo));       
     //se ejecuta y trae respuesta 
     $response = curl_exec($ch);
     //se cierra 
     curl_close($ch);

     //se toma la respuesta 
     $data = htmlentities($response);
     //se imprime 
     echo $data;

     ?>