package Ejercicio1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Ejercicio1C {

    public static void main(String[] args) {
        try {
            File inputFile = new File("src/Ejercicio1/Zoo.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList listFelinos = doc.getElementsByTagName("Felino");

            for (int i = 0; i < listFelinos.getLength(); i++) {
                Node node = listFelinos.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    Node pesoNode = element.getElementsByTagName("Peso").item(0);

                    int pesoActual = Integer.parseInt(pesoNode.getTextContent());
                    int nuevoPeso = pesoActual - 5;

                    pesoNode.setTextContent(String.valueOf(nuevoPeso));
                }
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/Ejercicio1/Zoo2.xml"));
            transformer.transform(source, result);

            System.out.println("Fichero Zoo2.xml generado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
