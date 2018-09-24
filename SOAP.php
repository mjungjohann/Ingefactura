<?php 

$url= "https://develop.ingefactura.cl/wsDte/igf?wsdl"; 
            
            //Envio del rut
            $soapRut = "76518460";
            //envio de dv
            $soapDv = "6";
            //contraseña (Para obtener la contraseña debe mandar un correo a soporte@ingefactura.cl)
            $soapPassword = "12334";
            //numero de pedido
            $soapnumPedido = "111111";
            //dirección del archivo
            $file_name = realpath('/Users/admin/Desktop/pruebas.xml');
            //tomo el contenido del archivo
            $xmlGetContent = file_get_contents($file_name);
            //ingreso el archivo a una variable
            $xml = simplexml_load_string($xmlGetContent);
            //no tocar este dato
            $user = "user";
            //no tocar este dato
            $password = "pass";

            try{
            //se envia la estructura del xml
            $xml_post_string = '<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:ser="http://servicios/">
                           <soapenv:Header/>
                           <soapenv:Body>
                              <ser:sendDte>
                                 <rut>'.$soapRut.'</rut>
                                 <dv>'.$soapDv.'</dv>
                                 <pass>'.$soapPassword.'</pass>
                                 <numPedido>'.$soapnumPedido.'</numPedido>
                                 <sxml>'.$xmlGetContent.'</sxml>
                              </ser:sendDte>
                           </soapenv:Body>
                        </soapenv:Envelope>';  
                //
                $soap_do = curl_init(); 
                curl_setopt($soap_do, CURLOPT_URL,            $url );   
                curl_setopt($soap_do, CURLOPT_CONNECTTIMEOUT, 10); 
                curl_setopt($soap_do, CURLOPT_TIMEOUT,        10); 
                curl_setopt($soap_do, CURLOPT_RETURNTRANSFER, true );
                curl_setopt($soap_do, CURLOPT_SSL_VERIFYPEER, false);  
                curl_setopt($soap_do, CURLOPT_SSL_VERIFYHOST, false); 
                curl_setopt($soap_do, CURLOPT_POST,           true ); 
                curl_setopt($soap_do, CURLOPT_POSTFIELDS,    $xml_post_string); 
                curl_setopt($soap_do, CURLOPT_HTTPHEADER,     array('Content-Type: text/xml; charset=ISO-8859-1', 'Content-Length: '.strlen($xml_post_string))); 
                curl_setopt($soap_do, CURLOPT_USERPWD, $user . ":" . $password);

                $result = curl_exec($soap_do);

                echo $result;

            }catch(SoapFault $e){
                echo $e->getMessage();
            }

?>