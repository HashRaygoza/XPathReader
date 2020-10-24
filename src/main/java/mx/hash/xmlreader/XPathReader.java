package mx.hash.xmlreader;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author david
 */
public class XPathReader {

    static public void main(String[] args) {
        try {
            // Ruta del archivo XML
            String nombreArchivo = "cfdi.xml";
            File archivo = new File(nombreArchivo);
            
            // Generador de constructor de objetos XML
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // Esto es para agilizar la lectura de archivos grandes
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            // constructor de objetos XML
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            
            // Objeto Documento XML
            Document documento = documentBuilder.parse(archivo);

            // Esto ayuda al procesamiento
            documento.getDocumentElement().normalize();

            // XPath nos permite seleccionar objetos via su ubicacion en la estructura del XML
            XPath xPath = XPathFactory.newInstance().newXPath();
            
            // La ruta del elemento que deseamos, para este omitir el prefijo cfdi:
            String expresionTranslados = "/Comprobante/Impuestos/Traslados/Traslado";
            
            // Obtenemos todos los nodos que empatan con la ruta que indicamos
            NodeList nodeListTranslados = (NodeList) xPath.compile(expresionTranslados).evaluate(documento, XPathConstants.NODESET);
            
            
            System.out.println("Cantidad de elementos que empatan con la ruta" + nodeListTranslados.getLength());
            
            // Obtenemos el primer elemento de esa lista
            Element translado = (Element) nodeListTranslados.item(0);
            
            // Presentamos los atributos de ese elemento
            System.out.println("Importe\t\t: " + translado.getAttribute("Importe"));
            System.out.println("TasaOCuota\t\t: " + translado.getAttribute("TasaOCuota"));
            System.out.println("TipoFactor\t\t: " + translado.getAttribute("TipoFactor"));
            System.out.println("Impuesto\t\t: " + translado.getAttribute("Impuesto"));

            // Ruta de los conceptos d ela factura
            String expresionConceptos = "/Comprobante/Conceptos/Concepto";
            
            // Lista de nodos de conceptos
            NodeList nodeListConceptos = (NodeList) xPath.compile(expresionConceptos).evaluate(documento, XPathConstants.NODESET);
            
            System.out.println("Cantidad de conceptos de la factura");
            System.out.println(nodeListConceptos.getLength());
            System.out.println("");
            
            // Avanzamos por la lista para presentar los conceptos
            for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {

                // Obtenemos un nodo
                Node nodoConcepto = nodeListConceptos.item(temp);

                // Verificamos que el nodo sea un elemento, para prevenir errores
                if (nodoConcepto.getNodeType() == Node.ELEMENT_NODE) {

                    // Convertimos de Node a elemento
                    Element elementoConcepto = (Element) nodoConcepto;

                    // Presentamos los datos de cada elemento de la factura
                    System.out.println("ClaveProdServ\t\t: " + elementoConcepto.getAttribute("ClaveProdServ"));
                    System.out.println("ClaveUnidad\t\t: " + elementoConcepto.getAttribute("ClaveUnidad"));
                    System.out.println("Descripciont\t\t: " + elementoConcepto.getAttribute("Descripcion"));
                    System.out.println("ValorUnitario\t\t: " + elementoConcepto.getAttribute("ValorUnitario"));
                    System.out.println("NoIdentificacion\t: " + elementoConcepto.getAttribute("NoIdentificacion"));
                    System.out.println("\n");

                }
            }
            

        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException ex) {
            Logger.getLogger(XPathReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
