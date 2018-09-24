package test;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;
import java.security.SecureRandom;
import java.security.Security;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import org.apache.commons.io.IOUtils;

public class TestPost {

    public static void main(String[] args) {
        
       try{
                //se selecciona el archivo
                File file = new File("UrlArchivo");
                // 
                InputStream input = new FileInputStream(file);
                //documento convertido a string
                String archivo = FileUtils.readFileToString(file);
                //url del web service
                HttpsURLConnection connection = (HttpsURLConnection) new URL("https://develop.ingefactura.cl/wsRest/web/com/dte").openConnection(); 
                                   
                //parametros de envio
                connection.setDoOutput(true);
                connection.setDoInput(true);
                //metodo de envio
                connection.setRequestMethod("POST");
                connection.addRequestProperty("Content-Type", "application/xml");
                connection.addRequestProperty("rut", "Rut Empresa");
                //envio del token
                connection.addRequestProperty("Authorization","Bearer "+"Token");
                //tipo de archivo a enviar
                connection.addRequestProperty("tipo", "Tipo de archivo");
                DataOutputStream os = new DataOutputStream(connection.getOutputStream());
                os.writeBytes(archivo);
                os.close();
                 
                //respuesta xml del web service 
                InputStream salida = connection.getInputStream();
                String xmlRespuesta = IOUtils.toString(salida,"UTF-8");
                System.out.println(xmlRespuesta);
       
       } catch (FileNotFoundException ex) {
            ex.printStackTrace();
       } catch (Exception ex) {
            ex.printStackTrace();
        }
       
    }
    