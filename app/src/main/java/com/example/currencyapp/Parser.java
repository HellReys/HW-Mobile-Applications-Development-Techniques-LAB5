package com.example.currencyapp;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Parser {


    public static List<String> parseXML(String xmlString) {
        List<String> list = new ArrayList<>();
        if (xmlString == null || xmlString.isEmpty()) return list;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            ByteArrayInputStream input = new ByteArrayInputStream(xmlString.getBytes(StandardCharsets.UTF_8));
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();

            NodeList items = doc.getElementsByTagName("item");

            for (int i = 0; i < items.getLength(); i++) {
                Node node = items.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element e = (Element) node;

                    String currencyCode = getTagValue("targetCurrency", e);
                    String exchangeRate = getTagValue("exchangeRate", e);

                    if (currencyCode != null && exchangeRate != null) {
                        list.add(currencyCode + " - " + exchangeRate);
                    }
                }
            }


            Collections.sort(list);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nlList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node nValue = nlList.item(0);
        return nValue != null ? nValue.getNodeValue() : null;
    }
}
