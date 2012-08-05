package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import cn.ohyeah.stb.ui.PopupText;

public class City {
	public static final int ASSIGN_TASK_FARMING = 1;		/*农业*/
	public static final int ASSIGN_TASK_COMMERCE = 1<<1;	/*商业*/
	public static final int ASSIGN_TASK_RELIEF = 1<<2;		/*救济*/
	public static final int ASSIGN_TASK_SEARCH = 1<<3;		/*搜索*/
	
	public static final int ASSIGN_TASK_DRAFT = 1<<4;		/**/
	
	public static final short MAX_FARMING_VALUE = 5000;		/*农业值上限*/
	public static final short MAX_COMMERCE_VALUE = 5000;	/*商业值上限*/
	public static final short MAX_POPULATION_VALUE = 2000;	/*人口数上限*/
	public static final short MAX_WEALTH_VALUE = 20000;		/*金钱数上限*/
	public static final short MAX_PROVISIONS_VALUE = 20000;	/*粮草上限*/
	public static final short MAX_LOYALTY_VALUE = 2000;		/*民忠上限*/
	
	public static final short MAX_FREE_SOLDIER_COUNT = 3600;
	
	private String name;
	private short id;
	
	private short farming;			/*农业值*/
	private short commerce;			/*商业值*/
	private short population;		/*人口数*/
	private short wealth;			/*金钱数*/
	private short provisions;		/*粮草数*/
	private short loyalty;			/*民忠*/
	private short seigneurId;		/*君主*/
	private short flag;				/*状态*/
	private short[] freeSoldier;
	private short farmingEnhance;	/*城市农业加成*/
	private short commerceEnhance;	/*城市商业加成*/
	private short attackEnhance;	/*城市部队攻击加成*/
	private short defenseEnhance;	/*城市部队防御加成*/
	
	private static short[][] transferTask = new short[5][5];
	
	public static int addTransferTask(short srcCityId, short wealth, short provisions, short destCityId) {
		int pos = -1;
		for (int i = 0; i < transferTask.length; ++i) {
			if (transferTask[i][0] == 0) {
				pos = i;
				transferTask[i][0] = 1;
				transferTask[i][1] = srcCityId;
				transferTask[i][2] = wealth;
				transferTask[i][3] = provisions;
				transferTask[i][4] = destCityId;
				break;
			}
		}
		return pos;
	}
	
	public static int getLeftTransferTaskCount() {
		int count = 0;
		for (int i = 0; i < transferTask.length; ++i) {
			if (transferTask[i][0] == 1) {
				count++;
			}
		}
		return transferTask.length-count;
	}
	
	public static void execTransferTask() {
		NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
		PopupText pt = Resource.buildPopupText();
		for (int i = 0; i < transferTask.length; ++i) {
			if (transferTask[i][0] == 1) {
				transferTask[i][0] = 0;
				City srcCity = engine.cityList[transferTask[i][1]];
				City destCity = engine.cityList[transferTask[i][4]];
				if (destCity.belongToPlayer()) {
					destCity.incWealth(transferTask[i][2]);
					destCity.incProvisions(transferTask[i][3]);
					pt.setText("从"+srcCity.getName()+"输送的"+"金币"+transferTask[i][2]+", 粮草"+transferTask[i][3]+", 已经成功到达"+destCity.getName());
					pt.setWaitMillisSeconds(3000);
					pt.popup();
				}
			}
		}
	}
	public static short[][] getTransferTask() {
		return transferTask;
	}
	
	public void addTaskFlag(int flag) {
		this.flag |= flag;
	}
	
	public boolean isTaskFlagValid(int flag) {
		return (this.flag & flag) != 0;
	}
	
	public void removeTaskFlag(int flag) {
		this.flag &= (~flag);
	}
	
	public void clearTaskFlag() {
		this.flag = 0;
	}
	
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
	public short getFarming() {
		return farming;
	}
	public void setFarming(short farming) {
		this.farming = farming;
	}
	public void incFarming(int inc) {
		int value = farming+inc;
		if (value > City.MAX_FARMING_VALUE) {
			farming = City.MAX_FARMING_VALUE;
		}
		else {
			farming = (short)value;
		}
	}
	
	public void decFarming(int dec) {
		this.farming -= dec;
		if (this.farming < 0) {
			this.farming = 0;
		}
	}
	
	public short getCommerce() {
		return commerce;
	}
	public void setCommerce(short commerce) {
		this.commerce = commerce;
	}
	public void incCommerce(int inc) {
		int value = commerce+inc;
		if (value > City.MAX_COMMERCE_VALUE) {
			commerce = City.MAX_COMMERCE_VALUE;
		}
		else {
			commerce = (short)value;
		}
	}
	
	public  void decCommerce(int dec) {
		this.commerce -= dec;
		if (this.commerce < 0) {
			this.commerce = 0;
		}
	}
	
	public short getPopulation() {
		return population;
	}
	public void setPopulation(short population) {
		this.population = population;
	}
	
	public void incPopulation(int inc) {
		int value = population+inc;
		if (value > City.MAX_POPULATION_VALUE) {
			population = City.MAX_POPULATION_VALUE;
		}
		else {
			population = (short)value;
		}
	}
	
	public void decPopulation(int dec) {
		this.population -= dec;
		if (this.population < 0) {
			this.population = 0;
		}
	}
	public short getWealth() {
		return wealth;
	}
	public void setWealth(short wealth) {
		this.wealth = wealth;
	}
	
	public void incWealth(int inc) {
		int value = wealth+inc;
		if (value > City.MAX_WEALTH_VALUE) {
			wealth = City.MAX_WEALTH_VALUE;
		}
		else {
			wealth = (short)value;
		}
	}
	
	public void decWealth(int dec) {
		this.wealth -= dec;
		if (this.wealth < 0) {
			this.wealth = 0;
		}
	}
	public short getProvisions() {
		return provisions;
	}
	public void setProvisions(short provisions) {
		this.provisions = provisions;
	}
	
	public void incProvisions(int inc) {
		int value = provisions+inc;
		if (value > City.MAX_PROVISIONS_VALUE) {
			provisions = City.MAX_PROVISIONS_VALUE;
		}
		else {
			provisions = (short)value;
		}
	}
	
	public void decProvisions(int dec) {
		this.provisions -= dec;
		if (this.provisions < 0) {
			this.provisions = 0;
		}
	}
	public short getLoyalty() {
		return loyalty;
	}
	public void setLoyalty(short loyalty) {
		this.loyalty = loyalty;
	}
	
	public void incLoyalty(int inc) {
		int value = loyalty+inc;
		if (value > City.MAX_LOYALTY_VALUE) {
			loyalty = City.MAX_LOYALTY_VALUE;
		}
		else {
			loyalty = (short)value;
		}
	}
	
	public void decLoyalty(int dec) {
		this.loyalty -= dec;
		if (this.loyalty < 0) {
			this.loyalty = 0;
		}
	}
	
	public short getSeigneurId() {
		return seigneurId;
	}
	public void setSeigneurId(short seigneurId) {
		this.seigneurId = seigneurId;
	}
	
	public void setSeigneur(Seigneur seigneur) {
		if (seigneur != null) {
			this.seigneurId = seigneur.getId();
		}
		else {
			this.seigneurId = -1;
		}
	}
	
	public Seigneur getSeigneur() {
		if (seigneurId >= 0) {
			return NewSanguoGameEngine.instance.seigneurList[seigneurId];
		}
		return null;
	}
	
	public boolean belongToAnySeigneur() {
		return this.seigneurId >= 0;
	}
	
	public boolean belongToSeigneur(short seigneurId) {
		return this.seigneurId == seigneurId;
	}
	
	public boolean belongToSeigneur(Seigneur seigneur) {
		return seigneurId == seigneur.getId();
	}
	
	public boolean belongToPlayer() {
		return seigneurId == NewSanguoGameEngine.instance.playerSeigneur;
	}
	
	public short getFreeSoldierCount(short soldierId) {
		return freeSoldier[soldierId];
	}
	
	public void setFreeSoldierCount(short soldierID, short count) {
		freeSoldier[soldierID] = count;
	}
	
	public void incFreeSoldierCount(short soldierID, short incCount) {
		freeSoldier[soldierID] += incCount;
		if (freeSoldier[soldierID] > MAX_FREE_SOLDIER_COUNT) {
			freeSoldier[soldierID] = MAX_FREE_SOLDIER_COUNT;
		}
	}
	
	public void decFreeSoldierCount(short soldierID, short decCount) {
		freeSoldier[soldierID] -= decCount;
		if (freeSoldier[soldierID] < 0) {
			freeSoldier[soldierID] = 0;
		}
	}
	
	public boolean isFarmingEnhance() {
		return farmingEnhance > 0;
	}
	
	public short getFarmingEnhance() {
		return farmingEnhance;
	}
	public void setFarmingEnhance(short farmingEnhance) {
		this.farmingEnhance = farmingEnhance;
	}
	public void incFarmingEnhance(short inc) {
		this.farmingEnhance += inc;
	}
	public void decFarmingEnhance(short dec) {
		this.farmingEnhance -= dec;
		if (this.farmingEnhance < 0) {
			this.farmingEnhance = 0;
		}
	}
	
	public boolean isCommerceEnhance() {
		return commerceEnhance > 0;
	}
	
	public short getCommerceEnhance() {
		return commerceEnhance;
	}
	public void setCommerceEnhance(short commerceEnhance) {
		this.commerceEnhance = commerceEnhance;
	}
	
	public void incCommerceEnhance(short inc) {
		this.commerceEnhance += inc;
	}
	
	public void decCommerceEnhance(short dec) {
		this.commerceEnhance -= dec;
		if (this.commerceEnhance < 0) {
			this.commerceEnhance = 0;
		}
	}
	
	public boolean isAttackEnhance() {
		return attackEnhance > 0;
	}
	
	public short getAttackEnhance() {
		return attackEnhance;
	}
	public void setAttackEnhance(short attackEnhance) {
		this.attackEnhance = attackEnhance;
	}
	
	public void incAttackEnhance(short inc) {
		attackEnhance += inc;
	}
	
	public void decAttackEnhance(short dec) {
		attackEnhance -=  dec;
		if (attackEnhance < 0) {
			attackEnhance = 0;
		}
	}
	
	public boolean isDefenseEnhance() {
		return defenseEnhance > 0;
	}
	
	public short getDefenseEnhance() {
		return defenseEnhance;
	}
	public void setDefenseEnhance(short defenseEnhance) {
		this.defenseEnhance = defenseEnhance;
	}
	
	public void incDefenseEnhance(short inc) {
		defenseEnhance += inc;
	}
	
	public void decDefenseEnhance(short dec) {
		defenseEnhance -= dec;
		if (defenseEnhance < 0) {
			defenseEnhance = 0;
		}
	}
	
	public void print() {
		System.out.print(id+" "+name+" "+farming+" "+commerce+" "+population+" "+wealth+" "+provisions+" "+loyalty+" "+seigneurId+" ");
	}
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(id);
		out.writeUTF(name);
		out.writeShort(farming);
		out.writeShort(commerce);
		out.writeShort(population);
		out.writeShort(wealth);
		out.writeShort(provisions);
		out.writeShort(loyalty);
		out.writeShort(seigneurId);
		out.writeShort(flag);
		out.writeShort(freeSoldier.length);
		for (int i = 0; i < freeSoldier.length; ++i) {
			out.writeShort(freeSoldier[i]);
		}
		out.writeShort(farmingEnhance);
		out.writeShort(commerceEnhance);
		out.writeShort(attackEnhance);
		out.writeShort(defenseEnhance);
	}
	public void deserialize(DataInputStream in) throws IOException {
		id = in.readShort();
		name = in.readUTF();
		farming = in.readShort();
		commerce = in.readShort();
		population = in.readShort();
		wealth = in.readShort();
		provisions = in.readShort();
		loyalty = in.readShort();
		seigneurId = in.readShort();
		flag = in.readShort();
		freeSoldier = new short[in.readShort()];
		for (int i = 0; i < freeSoldier.length; ++i) {
			freeSoldier[i] = in.readShort();
		}
		farmingEnhance = in.readShort();
		commerceEnhance = in.readShort();
		attackEnhance = in.readShort();
		defenseEnhance = in.readShort();
	}

	public void decEnhance() {
		if (isAttackEnhance()) {
			decAttackEnhance((short)1);
		}
		if (isDefenseEnhance()) {
			decDefenseEnhance((short)1);
		}
		if (isCommerceEnhance()) {
			decCommerceEnhance((short)1);
		}
		if (isFarmingEnhance()) {
			decFarmingEnhance((short)1);
		}
	}
}
