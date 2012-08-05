package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class GeneralProp extends Prop {
	private short generalId;

	public void setGeneralId(short generalId) {
		this.generalId = generalId;
	}

	public short getGeneralId() {
		return generalId;
	}
	
	public void print() {
		super.print();
		System.out.print(generalId+" ");
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		super.serialize(out);
		out.writeShort(generalId);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		super.deserialize(in);
		generalId = in.readShort();
	}
}
