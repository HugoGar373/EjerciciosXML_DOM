package Ejercicio3;

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

public class Ejercicio3 {

    private static final String fichero_entrada = "src/Ejercicio3/alumnos.xml";
    private static final String fichero_salida = "src/Ejercicio3/alumnos_notas.xml";

    public static void main(String[] args) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setIgnoringComments(true);
            factory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder builder = factory.newDocumentBuilder();

            Document docEntrada = builder.parse(new File(fichero_entrada));
            Element rootEntrada = docEntrada.getDocumentElement();

            Element elemProfesor = (Element) rootEntrada.getElementsByTagName("profesor").item(0);
            String nombreModulo = elemProfesor.getElementsByTagName("modulo").item(0).getTextContent().trim();

            Document docSalida = builder.newDocument();
            Element rootSalida = docSalida.createElement("curso");
            rootSalida.setAttribute("id", rootEntrada.getAttribute("id"));
            docSalida.appendChild(rootSalida);

            Element elemModulo = docSalida.createElement("modulo");
            elemModulo.setTextContent(nombreModulo);
            rootSalida.appendChild(docSalida.createTextNode("\n  "));
            rootSalida.appendChild(elemModulo);

            NodeList listaAlumnos = rootEntrada.getElementsByTagName("alumno");
            double sumaNotasClase = 0;
            int totalAlumnos = listaAlumnos.getLength();

            for (int i = 0; i < totalAlumnos; i++) {
                Element elemAlumno = (Element) listaAlumnos.item(i);
                String dni = elemAlumno.getAttribute("dni");

                double p1 = Double.parseDouble(getTextoEtiqueta(elemAlumno, "practica1"));
                double p2 = Double.parseDouble(getTextoEtiqueta(elemAlumno, "practica2"));
                double examen = Double.parseDouble(getTextoEtiqueta(elemAlumno, "examen"));
                double pFinal = Double.parseDouble(getTextoEtiqueta(elemAlumno, "final"));

                double mediaPracticas = (p1 + p2) / 2.0;
                double notaMediaAlumno = (mediaPracticas * 0.30) + (examen * 0.40) + (pFinal * 0.30);

                notaMediaAlumno = Math.round(notaMediaAlumno * 100.0) / 100.0;
                sumaNotasClase += notaMediaAlumno;

                System.out.println(dni + " = " + notaMediaAlumno);

                Element nuevoAlumno = docSalida.createElement("alumno");
                nuevoAlumno.setAttribute("dni", dni);

                Element elemNota = docSalida.createElement("notaMedia");
                elemNota.setTextContent(String.valueOf(notaMediaAlumno));
                nuevoAlumno.appendChild(elemNota);

                rootSalida.appendChild(docSalida.createTextNode("\n  "));
                rootSalida.appendChild(nuevoAlumno);
            }

            double mediaTotalClase = 0;
            if (totalAlumnos > 0) {
                mediaTotalClase = sumaNotasClase / totalAlumnos;
                mediaTotalClase = Math.round(mediaTotalClase * 100.0) / 100.0;
            }

            System.out.println("Media total del módulo " + nombreModulo + "=" + mediaTotalClase);

            Element elemMediaClase = docSalida.createElement("mediaTotal");
            elemMediaClase.setTextContent(String.valueOf(mediaTotalClase));
            rootSalida.appendChild(docSalida.createTextNode("\n  "));
            rootSalida.appendChild(elemMediaClase);
            rootSalida.appendChild(docSalida.createTextNode("\n"));

            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.transform(
                    new DOMSource(docSalida),
                    new StreamResult(new File(fichero_salida))
            );

            System.out.println("\nFichero '" + fichero_salida + "' generado correctamente.");

        } catch (Exception e) {
            System.out.println("Error al procesar el archivo XML.");
            e.printStackTrace();
        }
    }

    private static String getTextoEtiqueta(Element elemPadre, String nombreEtiqueta) {
        NodeList lista = elemPadre.getElementsByTagName(nombreEtiqueta);
        if (lista.getLength() > 0) {
            return lista.item(0).getTextContent().trim();
        }
        return "0";
    }
}