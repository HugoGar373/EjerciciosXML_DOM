package Ejercicio1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Scanner;

public class Ejercicio1D {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el continente a buscar (ej. Africa, Asia, Europa, America): ");
        String continenteBuscado = scanner.nextLine().trim();

        try {
            File inputFile = new File("Zoo.xml");
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(inputFile);
            doc.getDocumentElement().normalize();

            NodeList listFelinos = doc.getElementsByTagName("Felino");
            boolean encontrado = false;

            System.out.println("\nAnimales de " + continenteBuscado + ":");
            for (int i = 0; i < listFelinos.getLength(); i++) {
                Node node = listFelinos.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    String origen = element.getElementsByTagName("Origen").item(0).getTextContent();

                    if (origen.equalsIgnoreCase(continenteBuscado)) {
                        String nombre = element.getElementsByTagName("Nombre").item(0).getTextContent();
                        String peso = element.getElementsByTagName("Peso").item(0).getTextContent();

                        System.out.println("- Nombre: " + nombre + ", Peso: " + peso + " kg");
                        encontrado = true;
                    }
                }
            }

            if (!encontrado) {
                System.out.println("No se encontraron animales procedentes de " + continenteBuscado + ".");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
