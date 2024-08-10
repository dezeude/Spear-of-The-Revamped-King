package com.sotk.managers;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;


public class Tileset {
	private int tileWidth, tileHeight, pixelWidth, pixelHeight, tileCount, columns;
	private String imgPath;
	private BufferedImage tileSet;
	private BufferedImage[] TileTypes;
	
	public Tileset(String path) {
		try {
			loadTileset(path);
		} catch (ParserConfigurationException | SAXException | IOException | URISyntaxException e) {
			// TODO Auto-generated catch block
			System.out.println("An error occurred while reading the tileset.");
			e.printStackTrace();
		}
	}
	private void loadTileset(String path) throws ParserConfigurationException, SAXException, IOException, URISyntaxException {
//		String url = AssetsManager.class.getResource(path).getPath();
//		File f = new File(url);
		
		//get name of TileSet file
		String tsName = path.substring(path.indexOf("/Tilesets"));
//		System.out.println("TileSet Name: " + tsName);
		//load the Map
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(AssetsManager.class.getResourceAsStream("/levels" + tsName));
		
		//breakpoint here!
		//error when loading document!
		Element ts = (Element) doc.getElementsByTagName("tileset").item(0);
		Element img = (Element) doc.getElementsByTagName("image").item(0);
		//data from tileset element
		tileWidth = Integer.parseInt(ts.getAttribute("tilewidth"));
		tileHeight = Integer.parseInt(ts.getAttribute("tileheight"));
		tileCount = Integer.parseInt(ts.getAttribute("tilecount"));
		columns = Integer.parseInt(ts.getAttribute("columns"));
		//data from img element
		pixelWidth = Integer.parseInt(img.getAttribute("width"));
		pixelHeight = Integer.parseInt(img.getAttribute("height"));
		imgPath = img.getAttribute("source");
		
		int rows = pixelHeight / tileHeight;
		
		//get Image File Name
		String imgName = imgPath.substring(imgPath.indexOf("/Assets"));
		
		//load tileSet image
		tileSet = AssetsManager.loadImage("/levels" + imgName);
		TileTypes = new BufferedImage[tileCount];
		int count = 0;
		
		for(int y = 0; y < rows; y++) {
			for(int x = 0; x < columns; x++) {
				TileTypes[count++] = tileSet.getSubimage(x * tileWidth, y * tileHeight, tileWidth, tileHeight);
			}
		}
		
	}
	
	public BufferedImage[] getTileTypes() {
		return TileTypes;
	}
	
}
