package proyectopruebasoap;

import java.io.File;
import java.io.StringWriter;
import java.nio.charset.Charset;
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
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConnection;
import javax.xml.soap.SOAPConnectionFactory;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import org.apache.commons.io.FileUtils;

public class ProyectoPruebaSOAP {

    public static void main(String[] args) {
        try {

            File file = new File("Ruta del archivo xml");

            String rut = "22222222";
            String dv = "1";
            String pass = "pass123";
            String numPedido = "111111";

            //convertimos el xml a string para poder enviarlo
            String xml = FileUtils.readFileToString(file);
            
            SOAPConnectionFactory soapConnectionFactory = SOAPConnectionFactory.newInstance();
            SOAPConnection soapConnection = soapConnectionFactory.createConnection();
            
            SOAPMessage mensaje;
            mensaje = createSOAPRequest(rut, dv, pass, numPedido, xml);

            if (mensaje != null) {

             String url = "https://develop.ingefactura.cl/wsDte/igf?wsdl";  
                
                //se envía response 
                SOAPMessage soapResponse = soapConnection.call(mensaje, url);
                //tomamos la salida de datos(XML) y la tomamos como byte[]
                byte[] xmlsalida = printResponse(soapResponse);
                //convertimos el arreglo de bytes a string 
                //respuesta tomada
                String xmlfinal = new String(xmlsalida,Charset.forName("UTF-8"));
                

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


//Metodo para retornar la respuesta como un arreglo de bytes
    private static byte[] printResponse(SOAPMessage soapResponse)
            throws Exception {
        TransformerFactory transformerFactory = TransformerFactory
                .newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        Source sourceContent = soapResponse.getSOAPPart().getContent();
  

        StringWriter writer = new StringWriter();
        StreamResult result = new StreamResult(writer);
  
        transformer.transform(sourceContent, result);
        
        byte[] res = writer.toString().getBytes("ISO-8859-1");
        return res;
    }

    //con ayuda de este metodo se crea el documento xml para poder comunicarnos con 
    //el servicio web service
    private static SOAPMessage createSOAPRequest(String rut, String dv, String pass, String numPedido, String xml) {
        try {

            //creacion de Request SOAP
            MessageFactory messageFactory = MessageFactory.newInstance();
            SOAPMessage soapMessage = messageFactory.createMessage();
            SOAPPart soapPart = soapMessage.getSOAPPart();

            // SOAP Envelope
            SOAPEnvelope envelope = soapPart.getEnvelope();
            //cambiamos prefijo del xml 
            envelope.removeNamespaceDeclaration("SOAP-ENV");
            envelope.addNamespaceDeclaration("soapenv", "http://schemas.xmlsoap.org/soap/envelope/");
            envelope.setPrefix("soapenv");
            
            SOAPHeader header = envelope.getHeader();
            SOAPBody body = envelope.getBody();
            header.setPrefix("soapenv");
            body.setPrefix("soapenv");
            
            
            //indicamos ruta de servicios
            envelope.addNamespaceDeclaration("ser", "http://servicios/");
            // SOAP Body
            SOAPBody soapBody = envelope.getBody();
            
            SOAPElement soapBodyElem = soapBody.addChildElement("sendDte", "ser");
            //agregamos elementos a enviar
            SOAPElement Rut = soapBodyElem.addChildElement("rut");
            SOAPElement Dv = soapBodyElem.addChildElement("dv");
            SOAPElement Pass = soapBodyElem.addChildElement("pass");
            SOAPElement NumPedido = soapBodyElem.addChildElement("numPedido");
            SOAPElement Xml = soapBodyElem.addChildElement("sxml");  

         // Agregamos a los elementos cada uno de sus atributos
            
            Rut.addTextNode(rut);
            Dv.addTextNode(dv);
            Pass.addTextNode(pass);
            NumPedido.addTextNode(numPedido);
            Xml.addTextNode(xml); 

            //guardamos los cambiaos hechos en el documento
            soapMessage.saveChanges()
            
            return soapMessage;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

 }