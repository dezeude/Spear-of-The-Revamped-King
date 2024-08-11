package com.sotk.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import com.sotk.levels.Level;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Tileset {
    private BufferedImage[] TileTypes;
    private Level level;

    /**
     * Constructs a tileset from a path containing the tileset file
     *
     * @param path the path of the file containing the tileset
     */
    public Tileset(String path, Level level) {
        this.level = level;
        try {
            loadTileset(path);
        } catch (ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
            // TODO Auto-generated catch block
            System.out.println("An error occurred while reading the tileset.");
            e.printStackTrace();
        }
    }

    /**
     * Constructs a tileset from an element/node
     *
     * @param el element/node containing the tileset data
     */
    public Tileset(Element el, Level level) {
        this.level = level;
        loadTileset(el);
    }

    private void loadTileset(String path) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
        //load the Map
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(AssetsManager.class.getResourceAsStream(level.getPath()) + path);

        //breakpoint here!
        //error when loading document!
        Element ts = (Element) doc.getElementsByTagName("tileset").item(0);
        Element img = (Element) doc.getElementsByTagName("image").item(0);
        //data from tileset element
        int tileWidth = Integer.parseInt(ts.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(ts.getAttribute("tileheight"));
        int tileCount = Integer.parseInt(ts.getAttribute("tilecount"));
        int columns = Integer.parseInt(ts.getAttribute("columns"));
        //data from img element
        int pixelWidth = Integer.parseInt(img.getAttribute("width"));
        int pixelHeight = Integer.parseInt(img.getAttribute("height"));
        String imgPath = img.getAttribute("source");

        int rows = pixelHeight / tileHeight;

        //get Image File Name
        String imgName = imgPath.substring(imgPath.indexOf("/Assets"));

        //load tileSet image
        BufferedImage tileSet = AssetsManager.loadImage("/levels" + imgName);
        TileTypes = new BufferedImage[tileCount];
        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                TileTypes[count++] = tileSet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }

    }

    private void loadTileset(Element ts) {
        Element img = null;
        NodeList children = ts.getChildNodes();
        for (int i = 0; i < children.getLength(); i++) {
            Node child = children.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE) {
                img = (Element) child;
                break;
            }
        }
        int tileWidth = Integer.parseInt(ts.getAttribute("tilewidth"));
        int tileHeight = Integer.parseInt(ts.getAttribute("tileheight"));
        int tileCount = Integer.parseInt(ts.getAttribute("tilecount"));
        int columns = Integer.parseInt(ts.getAttribute("columns"));
        //data from img element
        int pixelWidth = Integer.parseInt(img.getAttribute("width"));
        int pixelHeight = Integer.parseInt(img.getAttribute("height"));
        String imgPath = img.getAttribute("source");

        int rows = pixelHeight / tileHeight;

//		//get Image File Name
//		String imgName = imgPath.substring(imgPath.indexOf("/Assets"));
//
//		//load tileSet image
//		BufferedImage tileSet = AssetsManager.loadImage("/levels" + imgName);

        BufferedImage tileSet = AssetsManager.loadImage(this.level.getPath() + imgPath);
        TileTypes = new BufferedImage[tileCount];
        int count = 0;

        for (int y = 0; y < rows; y++) {
            for (int x = 0; x < columns; x++) {
                TileTypes[count++] = tileSet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
            }
        }
    }

    public BufferedImage[] getTileTypes() {
        return TileTypes;
    }

}
