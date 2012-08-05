package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Soldier {
	private String name;
	private short id;
	private short life;			/*生命值*/
	private short attack;		/*攻击力*/
	private short defense;		/*防御力*/
	private short attackSpeed;	/*攻击速度*/
	private short movingSpeed;	/*移动速度*/
	private short attackScope;	/*攻击范围*/
	private short amount;		/*价格*/

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

	public short getLife() {
		return life;
	}

	public void setLife(short life) {
		this.life = life;
	}

	public short getAttack() {
		return attack;
	}

	public void setAttack(short attack) {
		this.attack = attack;
	}

	public short getDefense() {
		return defense;
	}

	public void setDefense(short defense) {
		this.defense = defense;
	}

	public short getAttackSpeed() {
		return attackSpeed;
	}

	public void setAttackSpeed(short attackSpeed) {
		this.attackSpeed = attackSpeed;
	}

	public short getMovingSpeed() {
		return movingSpeed;
	}

	public void setMovingSpeed(short movingSpeed) {
		this.movingSpeed = movingSpeed;
	}

	public short getAttackScope() {
		return attackScope;
	}

	public void setAttackScope(short attackScope) {
		this.attackScope = attackScope;
	}

	public void setAmount(short amount) {
		this.amount = amount;
	}

	public short getAmount() {
		return amount;
	}
	
	public void print() {
		System.out.print(id+" "
				+name+" "
				+life+" "
				+attack+" "
				+defense+" "
				+attackSpeed+" "
				+attackScope+" "
				+amount+" ");
	}

	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(id);
		out.writeUTF(name);
		out.writeShort(life);
		out.writeShort(attack);
		out.writeShort(defense);
		out.writeShort(attackSpeed);
		out.writeShort(movingSpeed);
		out.writeShort(attackScope);
		out.writeShort(amount);
	}

	public void deserialize(DataInputStream in) throws IOException {
		id = in.readShort();
		name = in.readUTF();
		life = in.readShort();
		attack = in.readShort();
		defense = in.readShort();
		attackSpeed = in.readShort();
		movingSpeed = in.readShort();
		attackScope = in.readShort();
		amount = in.readShort();
	}
	
}
