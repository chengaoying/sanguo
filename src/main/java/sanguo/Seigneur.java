package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Seigneur {
	private String name;
	private short id;
	private short generalId;
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
	public short getGeneralId() {
		return generalId;
	}
	public void setGeneralId(short generalId) {
		this.generalId = generalId;
	}
	
	public void print() {
		System.out.print(id+" "+name+" "+generalId+" ");
	}
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(id);
		out.writeUTF(name);
		out.writeShort(generalId);
	}
	public void deserialize(DataInputStream in) throws IOException {
		id = in.readShort();
		name = in.readUTF();
		generalId = in.readShort();
	}
}
