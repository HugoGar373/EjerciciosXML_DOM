package Ejercicio1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class Ejercicio1B {

    public static void main(String[] args) {
        try {
            File inputFile = new File("src/Ejercicio1/Zoo.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList listFelinos = doc.getElementsByTagName("Felino");

            System.out.println("Felinos con peso mayor a 100 kg:");
            for (int i = 0; i < listFelinos.getLength(); i++) {
                Node node = listFelinos.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String nombre = element.getElementsByTagName("Nombre").item(0).getTextContent();
                    String origen = element.getElementsByTagName("Origen").item(0).getTextContent();
                    int peso = Integer.parseInt(element.getElementsByTagName("Peso").item(0).getTextContent());

                    if (peso > 100) {
                        System.out.println("Nombre: " + nombre + ", Origen: " + origen + ", Peso: " + peso + " kg");
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}