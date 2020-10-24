/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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
            //String nombreArchivo = "FAC-CONTADO-44838_ENVIADO_Timbrado.xml";
            String nombreArchivo = "cfdi.xml";
            File archivo = new File(nombreArchivo);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();

            // Esto es para agilizar la lectura de archivos grandes
            documentBuilderFactory.setNamespaceAware(false);
            documentBuilderFactory.setValidating(false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/namespaces", false);
            documentBuilderFactory.setFeature("http://xml.org/sax/features/validation", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false);
            documentBuilderFactory.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);

            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document documento = documentBuilder.parse(archivo);

            documento.getDocumentElement().normalize();

            XPath xPath = XPathFactory.newInstance().newXPath();
            // Se quita el prefijo cfdi
            String expression = "/Comprobante/Impuestos/Traslados/Traslado";
            NodeList nodeList = (NodeList) xPath.compile(expression).evaluate(documento, XPathConstants.NODESET);
            
            System.out.println(nodeList.getLength());
            
            Element elemento = (Element) nodeList.item(0);
            
            System.out.println("Importe\t\t: " + elemento.getAttribute("Importe"));
            System.out.println("TasaOCuota\t\t: " + elemento.getAttribute("TasaOCuota"));
            System.out.println("TipoFactor\t\t: " + elemento.getAttribute("TipoFactor"));
            System.out.println("Impuesto\t\t: " + elemento.getAttribute("Impuesto"));
            
            String conceptos = "Comprobante/Conceptos/Concepto";
            NodeList nodeListConceptos = (NodeList) xPath.compile(conceptos).evaluate(documento, XPathConstants.NODESET);
            
            System.out.println("");
            System.out.println(nodeListConceptos.getLength());
            System.out.println("");
            
            for (int temp = 0; temp < nodeListConceptos.getLength(); temp++) {

                Node nNode = nodeListConceptos.item(temp);

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element eElement = (Element) nNode;

                    System.out.println("ClaveProdServ\t\t: " + eElement.getAttribute("ClaveProdServ"));
                    System.out.println("ClaveUnidad\t\t: " + eElement.getAttribute("ClaveUnidad"));
                    System.out.println("Descripciont\t\t: " + eElement.getAttribute("Descripcion"));
                    System.out.println("ValorUnitario\t\t: " + eElement.getAttribute("ValorUnitario"));
                    System.out.println("NoIdentificacion\t: " + eElement.getAttribute("NoIdentificacion"));
                    System.out.println("\n");

                }
            }
            

        } catch (SAXException | IOException | ParserConfigurationException | XPathExpressionException ex) {
            Logger.getLogger(XPathReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
