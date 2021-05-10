/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ContainersLauncher;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

/**
 *
 * @author Jairo Serrano <jaserrano@javeriana.edu.co>
 * 
 */
public final class BenchmarkConfig {

    private int NumberOfAgentsPerContainer;
    private int FiboToCalculate;
    private int NumberOfContainers;

    public BenchmarkConfig() {

        try {
            // https://www.javatpoint.com/how-to-read-xml-file-in-java
            File file = new File("config/BenchmarkConfig.xml");
            //an instance of factory that gives a document builder  
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //an instance of builder to parse the specified xml file  
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(file);
            doc.getDocumentElement().normalize();
            NodeList nodeList = doc.getElementsByTagName("container");
            // nodeList is not iterable, so we are using for loop  
            for (int itr = 0; itr < nodeList.getLength(); itr++) {
                Node node = nodeList.item(itr);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    // Configuración inicial desde el archivo XML
                    this.setNumberOfContainers(Integer.valueOf(eElement.getElementsByTagName("numberofcontainers").item(0).getTextContent()));
                    this.setFiboToCalculate(Integer.valueOf(eElement.getElementsByTagName("fibotocalculate").item(0).getTextContent()));
                    this.setNumberOfAgentsPerContainer(Integer.valueOf(eElement.getElementsByTagName("numberofagentspercontainer").item(0).getTextContent()));
                }
            }
        } catch (IOException | NumberFormatException | ParserConfigurationException | DOMException | SAXException e) {
            e.printStackTrace();
        }

    }

    public int getNumberOfContainers() {
        return NumberOfContainers;
    }

    public void setNumberOfContainers(int NumberOfContainers) {
        this.NumberOfContainers = NumberOfContainers;
    }

    public String getFiboToCalculate() {
        return String.valueOf(FiboToCalculate);
    }

    public void setFiboToCalculate(int FiboToCalculate) {
        this.FiboToCalculate = FiboToCalculate;
    }

    public int getNumberOfAgentsPerContainer() {
        return NumberOfAgentsPerContainer;
    }

    public void setNumberOfAgentsPerContainer(int NumberOfAgentsPerContainer) {
        this.NumberOfAgentsPerContainer = NumberOfAgentsPerContainer;
    }

}
