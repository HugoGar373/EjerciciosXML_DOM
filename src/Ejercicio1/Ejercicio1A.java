package Ejercicio1;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;

public class Ejercicio1A {
    public static void main(String[] args) {
        try {
            DocumentBuilderFactory dbF = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbF.newDocumentBuilder();
            Document doc = dBuilder.newDocument();

            Element rootElement = doc.createElement("Zoo");
            doc.appendChild(rootElement);

            Element felinosE = doc.createElement("Felinos");
            rootElement.appendChild(felinosE);

            String[][] datosFelinos = {
                    {"León", "África", "155"},
                    {"Tigre", "Asia", "215"},
                    {"Lince", "Europa", "20"},
                    {"PanteraTigris", "America", "95"}
            };

            for (String[] datos : datosFelinos) {
                Element felino = doc.createElement("Felino");

                Element nombre = doc.createElement("Nombre");
                nombre.appendChild(doc.createTextNode(datos[0]));
                felino.appendChild(nombre);

                Element origen = doc.createElement("Origen");
                origen.appendChild(doc.createTextNode(datos[1]));
                felino.appendChild(origen);

                Element peso = doc.createElement("Peso");
                peso.appendChild(doc.createTextNode(datos[2]));
                felino.appendChild(peso);

                felinosE.appendChild(felino);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File("src/Ejercicio1/Zoo.xml"));
            transformer.transform(source, result);

            System.out.println("Fichero Zoo.xml generado correctamente");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
