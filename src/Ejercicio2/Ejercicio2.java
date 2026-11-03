package Ejercicio2;

import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Ejercicio2 {

    private static final String ruta = "";
    private static final String fichero_bolsa = "src/Ejercicio2/Bolsa.xml";
    private static final String fichero_datos = "src/Ejercicio2/datos.xml";
    private static final String fichero_salida = "src/Ejercicio2/bolsaactualizada.xml";

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document docBolsa = builder.parse(new File(ruta + fichero_bolsa));
            Document docDatos = builder.parse(new File(ruta + fichero_datos));

            Element rootBolsa = docBolsa.getDocumentElement();
            NodeList operaciones = docDatos.getElementsByTagName("Operacion");

            for (int i = 0; i < operaciones.getLength(); i++) {
                Element op = (Element) operaciones.item(i);
                String tipoOp = op.getAttribute("id");

                if (tipoOp.equalsIgnoreCase("A")) {
                    darAlta(docBolsa, rootBolsa, op);
                } else if (tipoOp.equalsIgnoreCase("B")) {
                    darBaja(rootBolsa, op);
                } else if (tipoOp.equalsIgnoreCase("M")) {
                    modificarPrecio(rootBolsa, op);
                } else {
                    System.out.println("Error: Operación no válida (" + tipoOp + ").");
                }
            }

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(
                    new DOMSource(docBolsa),
                    new StreamResult(new File(ruta + fichero_salida))
            );

            System.out.println("\nProceso finalizado. Fichero 'bolsaactualizada.xml' generado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al procesar los documentos XML.");
        }
    }

    private static void darAlta(Document doc, Element root, Element op) {
        String nombre = getTextoEtiqueta(op, "Nombre");
        String indice = getTextoEtiqueta(op, "Indice");
        String simbolo = getTextoEtiqueta(op, "Simbolo");
        String precio = getTextoEtiqueta(op, "Precio");

        if (buscarEmpresa(root, nombre) != null) {
            System.out.println("Error [ALTA]: La empresa '" + nombre + "' ya existe.");
            return;
        }

        Element nuevaEmpresa = doc.createElement("Empresa");
        nuevaEmpresa.setAttribute("indice", indice.toLowerCase());

        Element elemNombre = doc.createElement("Nombre");
        elemNombre.setTextContent(nombre);

        Element elemSimbolo = doc.createElement("Simbolo");
        elemSimbolo.setTextContent(simbolo);

        Element elemPrecio = doc.createElement("Precio");
        elemPrecio.setTextContent(precio);

        nuevaEmpresa.appendChild(elemNombre);
        nuevaEmpresa.appendChild(elemSimbolo);
        nuevaEmpresa.appendChild(elemPrecio);

        root.appendChild(nuevaEmpresa);
        System.out.println("Alta realizada con éxito: " + nombre);
    }

    private static void darBaja(Element root, Element op) {
        String nombre = getTextoEtiqueta(op, "Nombre");
        Element empresa = buscarEmpresa(root, nombre);

        if (empresa == null) {
            System.out.println("Error [BAJA]: La empresa '" + nombre + "' no existe.");
            return;
        }

        root.removeChild(empresa);
        System.out.println("Baja realizada con éxito: " + nombre);
    }

    private static void modificarPrecio(Element root, Element op) {
        String nombre = getTextoEtiqueta(op, "Nombre");
        String nuevoPrecio = getTextoEtiqueta(op, "Precio");
        Element empresa = buscarEmpresa(root, nombre);

        if (empresa == null) {
            System.out.println("Error [MODIFICACIÓN]: La empresa '" + nombre + "' no existe.");
            return;
        }

        Element elemPrecio = (Element) empresa.getElementsByTagName("Precio").item(0);
        elemPrecio.setTextContent(nuevoPrecio);
        System.out.println("Modificación realizada con éxito: " + nombre + " -> Nuevo precio: " + nuevoPrecio);
    }

    private static Element buscarEmpresa(Element root, String nombreBuscado) {
        NodeList empresas = root.getElementsByTagName("Empresa");
        for (int i = 0; i < empresas.getLength(); i++) {
            Element elem = (Element) empresas.item(i);
            String nombre = getTextoEtiqueta(elem, "Nombre");
            if (nombre.equalsIgnoreCase(nombreBuscado)) {
                return elem;
            }
        }
        return null;
    }

    private static String getTextoEtiqueta(Element elemPadre, String nombreEtiqueta) {
        NodeList lista = elemPadre.getElementsByTagName(nombreEtiqueta);
        if (lista.getLength() > 0) {
            return lista.item(0).getTextContent().trim();
        }
        return "";
    }
}