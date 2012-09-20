package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cn.ohyeah.stb.game.EngineService;

public class Prop {
	private static EngineService engineService;
	
	private String name;
	private short id;
	private short propId;
	private short price;
	private short probability;
	private String intro;
	private String icon;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public short getId() {
		return id;
	}
	
	public void setId(short id) {
		this.id = id;
	}
	
	public short getPrice() {
		return price;
	}
	
	public void setPrice(short price) {
		this.price = price;
	}
	
	public short getProbability() {
		return probability;
	}
	
	public void setProbability(short probability) {
		this.probability = probability;
	}
	
	public String getIntro() {
		return intro;
	}
	
	public void setIntro(String intro) {
		this.intro = intro;
	}
	
	public String getIcon() {
		return icon;
	}
	
	public void setIcon(String icon) {
		this.icon = icon;
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(id);
		out.writeUTF(name);
		out.writeUTF(intro);
		out.writeShort(propId);
		out.writeShort(price);
		out.writeShort(probability);
		out.writeUTF(icon);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		id = in.readShort();
		name = in.readUTF();
		intro = in.readUTF();
		propId = in.readShort();
		price = in.readShort();
		probability = in.readShort();
		icon = in.readUTF();
	}
	
	public void print() {
		System.out.print(id+" "+name+" "+intro+" "+price+" "+probability+" "+icon+" ");
	}
	
	public void setPropId(short propId) {
		this.propId = propId;
	}
	
	public short getPropId() {
		return propId;
	}
	
	public int getExpendPrice() {
		return engineService.calcExpendAmount(price);
	}

	public static void setEngineService(EngineService engineService) {
		Prop.engineService = engineService;
	}

	public static EngineService getEngineService() {
		return engineService;
	}
}
