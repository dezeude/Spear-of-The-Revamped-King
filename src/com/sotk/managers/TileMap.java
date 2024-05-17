package com.sotk.managers;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.sotk.levels.Level;
import com.sotk.main.GamePanel;

public class TileMap {
	public static final int TILELENGTH = 32;
	//class instance starts here
	public static int[][]map;
	public static int width,height; //for CollisionsMap
	public int collisionID;
	BufferedImage TileSet;
	Object[] TileTypes;
	Level level;
	Document doc;
	private ArrayList<int[][]> layersData = new ArrayList();
	

	//The integer in the 2D map array represents the ID of the tile.
	//The array above stores all the different types of tiles. 
	//The ID of the tile can give information about the image of the tile and whether it is solid or not.
	
	public TileMap(String levelPath, Level level) {
		this.level = level;
		try {
			loadMap(levelPath + "Level 1.tmx");
			loadTileSet();
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void loadTileSet() throws IOException {
		ArrayList<BufferedImage> tileList = new ArrayList<>();
		NodeList tilesets = doc.getElementsByTagName("tileset");
		for(int i = 0; i < tilesets.getLength(); i++) {
			Element tilesetElement = (Element) tilesets.item(i);
			String tsPath = "/" + tilesetElement.getAttribute("source");
			Tileset ts = new Tileset(tsPath);
			//add the tiles to the TileTypes
//			System.out.println(ts.getTileTypes());
			tileList.addAll(Arrays.asList(ts.getTileTypes()));
		}
		TileTypes = tileList.toArray();
		
	}
	
	private int[] ToIntArray(String[] str) {
		int[] arr = new int[str.length];
		for(int i = 0; i < str.length; i++) {
			arr[i] = Integer.parseInt(str[i]);
		}
		return arr;
	}
	
	//read csv file
	private void loadMap(String path) throws NumberFormatException, IOException, SAXException, ParserConfigurationException {
		//load the Map
//		System.out.println(path);
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		doc = builder.parse(AssetsManager.class.getResourceAsStream(path));
		//XML document Object
		
		//process layer data
		NodeList layers = doc.getElementsByTagName("layer");
		for(int i = 0; i < layers.getLength(); i++) { //go through each layer
			Node l = layers.item(i);
			Element layer = (Element) l;
			
			width = Integer.parseInt(layer.getAttribute("width"));
			height = Integer.parseInt(layer.getAttribute("height"));
			String name = layer.getAttribute("name");
			
			
			if(name.equals("Collision")) {
				map = processLayer(layer);
				Level.setMap(map);
			}
			else
				layersData.add(processLayer(layer));
			
			
		}
		
		
		NodeList objGrpList = doc.getElementsByTagName("objectgroup");
		
		for(int i = 0; i < objGrpList.getLength(); i++) {
			Node o = objGrpList.item(i);
			if(o.getNodeType() == Node.ELEMENT_NODE) {
				Element objGrp = (Element) o; //object group element.
				String grpName = objGrp.getAttribute("name");
				if(grpName.equals("Mobs")) { //group of enemies
					NodeList objects = objGrp.getChildNodes();
					for(int j = 0; j < objects.getLength(); j++) {
						Node object = objects.item(j);
						if(object.getNodeType() == Node.ELEMENT_NODE) {
							Element obj = (Element) object; 
							String name = obj.getAttribute("name");
							int mobX = Integer.parseInt(obj.getAttribute("x"));
							int mobY = Integer.parseInt(obj.getAttribute("y"));
							Element property = (Element)(object.getChildNodes().item(1).getChildNodes().item(1));
							String[] extras = property.getTextContent().split("\\r?\\n");
//							System.out.println(Arrays.toString(extras));
							level.addMob(name, mobX, mobY, extras);
						}
						
					}
				}
				
			}
		}
			
		
		
	}
	
	private int[][] processLayer(Element layer){//returns the 2D Array inside the Layer Element.
		width = Integer.parseInt(layer.getAttribute("width"));
		height = Integer.parseInt(layer.getAttribute("height"));
		
		Element data = (Element) layer.getChildNodes().item(1);
		String[] dataArr = data.getTextContent().replaceAll("\n", "").split(",");
		ArrayList<int[]> SmapData = new ArrayList<>();

		
		for(int i = 0; i < dataArr.length; i += width) {
			SmapData.add(ToIntArray(Arrays.copyOfRange(dataArr, i, width + i)));
		}
		int[][] mapData = new int[SmapData.size()][];
		SmapData.toArray(mapData);
		
		return mapData;
	}
	
	
	public void loadTMXMap() {
		System.out.println(TileMap.class.getResource("/").getPath());
	} 
	
	
	public static int[][] getMap() {
		return map;
	}
	
	public void render(Graphics g) {
			for(int y = Camera.getYOffset() / TILELENGTH; y < (Camera.getYOffset() + GamePanel.getGraphicsHeight()) / TILELENGTH + 1; y++) {//only render the tiles on the screen
				for(int x = Camera.getXOffset() / TILELENGTH; x < (Camera.getXOffset() + GamePanel.getGraphicsWidth()) / TILELENGTH + 1; x++) {
					for(int i = 0; i < layersData.size(); i++) {
						if(y >= 0 && y < map.length && x >= 0 && x < map[y].length) {
							int ID = layersData.get(i)[y][x] - 1;
							if(ID == -1)
								continue;
							g.drawImage((BufferedImage)TileTypes[ID], x * TILELENGTH - Camera.getXOffset(), y * TILELENGTH - Camera.getYOffset(), TILELENGTH, TILELENGTH, null);
						}	
					
				}
			}
		}
	}
	
	
	
}
