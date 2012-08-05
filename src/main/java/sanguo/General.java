package sanguo;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class General {
	public static final short MAX_FORCE_VALUE = 999;
	public static final short MAX_WIT_VALUE = 999;
	public static final short MAX_CHARM_VALUE = 999;
	public static final short MAX_MAGICPOWER_VALUE = 999;
	
	public static final byte TASK_FARMING = 1;		/*农业*/
	public static final byte TASK_COMMERCE = 2;		/*商业*/
	public static final byte TASK_RELIEF = 3;		/*救济*/
	public static final byte TASK_SEARCH = 4;		/*搜索*/
	public static final byte TASK_CONVALESCE = 5;	/*修养*/
	public static final byte TASK_MOVE = 6;			/*移动*/
	public static final byte TASK_EXPEDITION = 7;	/*出征*/
	
	public static final byte IDENTITY_NORMAL = 0;	/*普通*/
	public static final byte IDENTITY_PROP = 1;		/*道具*/
	public static final byte IDENTITY_FREE = 2;		/*在野（未出现）*/
	public static final byte IDENTITY_CAPTIVE = 3;	/*俘虏*/
	public static final byte IDENTITY_DEATH = 4;	/*死亡*/
	
	private String name;		/*武将名称*/
	private short id;
	private short stamina;		/*体力值*/
	private short magic;		/*精力(魔法值)*/
	private short force;		/*武力*/
	private short wit;			/*智力*/
	private short charm; 		/*魅力*/
	private short magicPower;	/*魔力(将技)*/
	private short seigneurId;	/*君主*/
	private short state;		/*状态*/
	private short identity;		/*身份*/
	private short lead;			/*统帅值*/
	private short cityId;		/*所在城市*/
	private short soldierId;	/*士兵类型*/
	private short soldierCount;	/*士兵数量*/
	private short appearTime;	/*出现时间*/
	
	public boolean canUseMagic() {
		return magic >= Rule.MAGIC_CONSUME_FOR_SKILL;
	}
	
	public int getMagicNum() {
		int n = 0;
		int m = magic;
		while (m >= Rule.MAGIC_CONSUME_FOR_SKILL) {
			m -= Rule.MAGIC_CONSUME_FOR_SKILL;
			++n;
		}
		return n;
	}
	
	public void useMagic() {
		magic -= Rule.MAGIC_CONSUME_FOR_SKILL;
		if (magic < 0) {
			magic = 0;
		}
	}
	
	public boolean isAppear() {
		return true;
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

	public short getMagic() {
		return magic;
	}

	public void setMagic(short magic) {
		this.magic = magic;
	}
	
	public void incMagic (int inc) {
		int value = magic+inc;
		if (value > Rule.INITIAL_MAGIC) {
			value = Rule.INITIAL_MAGIC;
		}
		else {
			value = (short)value;
		}
	}
	
	public void decMagic(int dec) {
		this.magic -= dec;
		if (this.magic < 0) {
			this.magic = 0;
		}
	}

	public void setSeigneurId(short seigneurId) {
		this.seigneurId = seigneurId;
	}

	public short getSeigneurId() {
		return seigneurId;
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
		return this.seigneurId == seigneur.getId();
	}
	
	public boolean belongToPlayer() {
		return this.seigneurId == NewSanguoGameEngine.instance.playerSeigneur;
	}
	
	public short getState() {
		return state;
	}
	
	public void setState(short state) {
		this.state = state;
	}
	
	public boolean isStateMove() {
		return state == TASK_MOVE;
	}
	
	public boolean isStateNormal() {
		return state == 0;
	}
	
	public void setStateNormal() {
		state = 0;
	}
	
	public boolean canAssignTask() {
		return state == 0;
	}
	
	public void assignTaskFarming() {
		state = TASK_FARMING;
	}
	
	public boolean hasTaskFarming() {
		return state==TASK_FARMING;
	}
	
	public void assignTaskCommerce() {
		state = TASK_COMMERCE;
	}
	
	public boolean hasTaskCommerce() {
		return state==TASK_COMMERCE;
	}
	
	public void assignTaskRelief() {
		state = TASK_RELIEF;
	}
	
	public boolean hasTaskRelief() {
		return state==TASK_RELIEF;
	}
	
	public void assignTaskSearch() {
		state = TASK_SEARCH;
	}
	
	public boolean hasTaskSearch() {
		return state==TASK_SEARCH;
	}
	
	public void assignTaskConvalesce() {
		state = TASK_CONVALESCE;
	}
	
	public boolean hasTaskConvalesce() {
		return state==TASK_CONVALESCE;
	}
	
	public void assignTaskMove() {
		state = TASK_MOVE;
	}
	
	public boolean hasTaskMove() {
		return state==TASK_MOVE;
	}
	
	public void assignTaskExpedition() {
		state = TASK_EXPEDITION;
	}
	
	public boolean hasTaskExpedition() {
		return state==TASK_EXPEDITION;
	}

	public short getIdentity() {
		return identity;
	}

	public void setIdentity(short identity) {
		this.identity = identity;
	}
	
	public boolean isIdentityNormal() {
		return identity == IDENTITY_NORMAL;
	}
	
	public void setIdentityNormal()  {
		identity = IDENTITY_NORMAL;
	}
	
	public boolean isIdentityFree() {
		return identity == IDENTITY_FREE;
	}
	
	public void setIdentityFree() {
		identity = IDENTITY_FREE;
	}
	
	public boolean isIdentityProp() {
		return identity == IDENTITY_PROP;
	}
	
	public void setIdentityProp() {
		identity = IDENTITY_PROP;
	}

	public boolean isIdentityDeath() {
		return identity == IDENTITY_DEATH;
	}
	
	public void setIdentityDeath() {
		identity = IDENTITY_DEATH;
	}
	
	public boolean isIdentityCaptive() {
		return identity == IDENTITY_CAPTIVE;
	}
	
	public void setIdentityCaptive() {
		identity = IDENTITY_CAPTIVE;
	}
	
	public short getCityId() {
		return cityId;
	}

	public void setCityId(short cityId) {
		this.cityId = cityId;
	}
	
	public void setCity(City city) {
		if (city != null) {
			this.cityId = city.getId();
		}
		else {
			this.cityId = -1;
		}
	}
	
	public City getCity() {
		if (cityId >= 0) {
			return NewSanguoGameEngine.instance.cityList[cityId];
		}
		return null;
	}
	
	public boolean belongToAnyCity() {
		return this.cityId >= 0;
	}
	
	public boolean belongToCity(short cityId) {
		return this.cityId == cityId;
	}
	
	public boolean belongToCity(City city) {
		return this.cityId == city.getId();
	}

	public short getSoldierId() {
		return soldierId;
	}
	
	public Soldier getSoldier() {
		return NewSanguoGameEngine.instance.soldierList[soldierId];
	}
	
	public void setSoldier(Soldier soldier) {
		soldierId = soldier.getId();
	}

	public void setSoldierId(short soldierId) {
		this.soldierId = soldierId;
	}

	public short getSoldierCount() {
		return soldierCount;
	}

	public void setSoldierCount(short soldierCount) {
		this.soldierCount = soldierCount;
	}
	
	public void incSoldierCount(int incCount) {
		this.soldierCount += incCount;
		if (this.soldierCount > this.lead) {
			this.soldierCount = this.lead;
		}
	}
	
	public void decSoldierCount(int decCount) {
		this.soldierCount -= decCount;
		if (this.soldierCount < 0) {
			this.soldierCount = 0;
		}
	}

	public void setLead(short lead) {
		this.lead = lead;
	}

	public short getLead() {
		return lead;
	}
	
	public void incLead(short inc) {
		lead += inc;
	}
	
	public short getForce() {
		return force;
	}

	public void setForce(short force) {
		this.force = force;
	}
	
	public void incForce(short inc) {
		int value = force+inc;
		if (value > MAX_FORCE_VALUE) {
			force = MAX_FORCE_VALUE;
		}
		else {
			force = (short)value;
		}
	}

	public short getWit() {
		return wit;
	}

	public void setWit(short wit) {
		this.wit = wit;
	}
	
	public void incWit(short inc) {
		int value = wit+inc;
		if (value > MAX_WIT_VALUE) {
			wit = MAX_WIT_VALUE;
		}
		else {
			wit = (short)value;
		}
	}

	public short getCharm() {
		return charm;
	}

	public void setCharm(short charm) {
		this.charm = charm;
	}
	
	public void incCharm(short inc) {
		int value = charm+inc;
		if (value > MAX_CHARM_VALUE) {
			charm = MAX_CHARM_VALUE;
		}
		else {
			charm = (short)value;
		}
	}

	public short getStamina() {
		return stamina;
	}

	public void setStamina(short stamina) {
		this.stamina = stamina;
	}
	
	public void incStamina(int inc) {
		int value = stamina +inc;
		if (value > Rule.INITIAL_STAMINA) {
			stamina = Rule.INITIAL_STAMINA;
		}
		else {
			stamina = (short)value;
		}
	}
	public void decStamina(int dec) {
		this.stamina -= dec;
		if (this.stamina < 0) {
			this.stamina = 0;
		}
	}
	
	public short getMagicPower() {
		return magicPower;
	}

	public void setMagicPower(short magicPower) {
		this.magicPower = magicPower;
	}
	
	public void incMagicPower(short inc) {
		int value = magicPower+inc;
		if (value >= MAX_MAGICPOWER_VALUE) {
			magicPower = MAX_MAGICPOWER_VALUE;
		}
		else {
			magicPower = (short)magicPower;
		}
	}
	
	public int getMagicPowerAttack() {
		return magicPower*250+5000;
	}

	public int getSoldierAttack() {
		int soldierAttack = NewSanguoGameEngine.instance.soldierList[soldierId].getAttack();
		int armyAttack = soldierAttack*Resource.SOLDIER_COUNT_PER_SPRITE
				+soldierAttack*force*Resource.SOLDIER_COUNT_PER_SPRITE/100;
		if (getCity().getAttackEnhance() > 0) {
			armyAttack = (short)(12*armyAttack/10);
		}
		return armyAttack;
	}
	
	public int getSoldierDefense() {
		int soldierDefense = NewSanguoGameEngine.instance.soldierList[soldierId].getDefense();
		int armyDefense = soldierDefense*Resource.SOLDIER_COUNT_PER_SPRITE
				+soldierDefense*wit*Resource.SOLDIER_COUNT_PER_SPRITE/100;
		if (getCity().getDefenseEnhance() > 0) {
			armyDefense = (short)(12*armyDefense/10);
		}
		return armyDefense;
	}
	
	public int getSoldierLife() {
		return NewSanguoGameEngine.instance.soldierList[soldierId].getLife();
	}
	
	public short getAppearTime() {
		return appearTime;
	}

	public void setAppearTime(short appearTime) {
		this.appearTime = appearTime;
	}
	
	/**
	 * 按武将智力排序，提供玩家开发农业商业优先选择
	 * @param other
	 * @return
	 */
	public int compareWit(General other) {
		if (state != 0) {
			return -1;
		}
		else if (other.state != 0) {
			return 1;
		}
		else if (stamina < Rule.STAMINA_CONSUME_FOR_COMMERCE) {
			return -1;
		}
		else if (other.stamina < Rule.STAMINA_CONSUME_FOR_COMMERCE) {
			return 1;
		}
		else {
			if (wit > other.wit) {
				return 1;
			}
			else if (wit < other.wit) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
	/**
	 * 按照武将魅力排序，提供玩家救济搜索优先选择
	 * @param other
	 * @return
	 */
	public int compareCharm(General other) {
		if (state != 0) {
			return -1;
		}
		else if (other.state != 0) {
			return 1;
		}
		else if (stamina < Rule.STAMINA_CONSUME_FOR_RELIEF) {
			return -1;
		}
		else if (other.stamina < Rule.STAMINA_CONSUME_FOR_RELIEF) {
			return 1;
		}
		else {
			if (charm > other.charm) {
				return 1;
			}
			else if (charm < other.charm) {
				return -1;
			}
			else {
				return 0;
			}
		}
	}
	
	/**
	 * 按照武将体力精力排序，提供玩家休养优先选择
	 * @param other
	 * @return
	 */
	public int compareStaminaForConvalesce(General other) {
		if (state != 0) {
			return -1;
		}
		else if (other.state != 0) {
			return 1;
		}
		else if (stamina < Rule.STAMINA_CONSUME_FOR_COMMERCE) {
			return 1;
		}
		else if (other.stamina < Rule.STAMINA_CONSUME_FOR_COMMERCE) {
			return -1;
		}
		else {
			if (getMagicNum() < 1) {
				return 1;
			}
			else {
				if (other.getMagicNum() < 1) {
					return -1;
				}
				else {
					return 0;
				}
			}
		}
	}
	
	public int compareStaminaForMove(General other) {
		if (state != 0) {
			return -1;
		}
		else if (other.state != 0) {
			return 1;
		}
		else if (stamina >= Rule.STAMINA_CONSUME_FOR_MOVE
				&& other.stamina < Rule.STAMINA_CONSUME_FOR_MOVE) {
			return 1;
		}
		else if (stamina < Rule.STAMINA_CONSUME_FOR_MOVE
				&& other.stamina >= Rule.STAMINA_CONSUME_FOR_MOVE) {
			return -1;
		}
		else {
			return 0;
		}
	}
	
	/**
	 * 按照(兵力>将技可施放数>将技>武力>智力)顺序排列，提供玩家出征优先选择
	 * @param other
	 * @return
	 */
	public int compareArmyPowerForPlayer(General other) {
		if (state != 0) {
			return -1;
		}
		else if (other.state != 0) {
			return 1;
		}
		else if (stamina < Rule.STAMINA_CONSUME_FOR_EXPEDITION) {
			return -1;
		}
		else if (other.stamina < Rule.STAMINA_CONSUME_FOR_EXPEDITION) {
			return 1;
		}
		if (soldierCount > other.soldierCount) {
			return 1;
		}
		else if (soldierCount < other.soldierCount) {
			return -1;
		}
		else {
			int mn1 = getMagicNum();
			int mn2 = other.getMagicNum();
			if (mn1 > mn2) {
				return 1;
			}
			else if (mn1 < mn2) {
				return -1;
			}
			else {
				if (magicPower > other.magicPower) {
					return 1;
				}
				else if (magicPower < other.magicPower) {
					return -1;
				}
				else {
					if (force > other.force) {
						return 1;
					}
					else if (force < other.force) {
						return -1;
					}
					else {
						if (wit > other.wit) {
							return 1;
						}
						else if (wit < other.wit) {
							return -1;
						}
						else {
							return 0;
						}
					}
				}
			}
		}
	}
	
	
	/**
	 * 按照(兵力>将技可施放数>将技>武力>智力)顺序排列，提供AI出征优先选择
	 * @param other
	 * @return 
	 */
	public int compareArmyPowerForAI(General other) {
		if (soldierCount > other.soldierCount) {
			return 1;
		}
		else if (soldierCount < other.soldierCount) {
			return -1;
		}
		else {
			int mn1 = getMagicNum();
			int mn2 = other.getMagicNum();
			if (mn1 > mn2) {
				return 1;
			}
			else if (mn1 < mn2) {
				return -1;
			}
			else {
				if (magicPower > other.magicPower) {
					return 1;
				}
				else if (magicPower < other.magicPower) {
					return -1;
				}
				else {
					if (force > other.force) {
						return 1;
					}
					else if (force < other.force) {
						return -1;
					}
					else {
						if (wit > other.wit) {
							return 1;
						}
						else if (wit < other.wit) {
							return -1;
						}
						else {
							return 0;
						}
					}
				}
			}
		}
	}
	
	public void printBattleGeneralAttribute() {
		System.out.println(name+"\t"
				+"兵"+soldierCount+"\t"
				+"精"+magic+"\t"
				+"技"+magicPower+"\t"
				+"武"+force+"\t"
				+"智"+wit
				);
	}

	public void print() {
		System.out.print(id+" "
				+name+" "
				+stamina+" "
				+magic+" "
				+force+" "
				+wit+" "
				+charm+" "
				+magicPower+" "
				+lead+" "
				+soldierId+" "
				+soldierCount+" "
				+state+" "
				+identity+" "
				+cityId+" "
				+seigneurId+" "
				+appearTime+" "
				);
	}
	
	public void serialize(DataOutputStream out) throws IOException {
		out.writeShort(id);
		out.writeUTF(name);
		out.writeShort(stamina);
		out.writeShort(magic);
		out.writeShort(force);
		out.writeShort(wit);
		out.writeShort(charm);
		out.writeShort(magicPower);
		out.writeShort(lead);
		out.writeShort(soldierId);
		out.writeShort(soldierCount);
		out.writeShort(state);
		out.writeShort(identity);
		out.writeShort(cityId);
		out.writeShort(seigneurId);
		out.writeShort(appearTime);
	}
	
	public void deserialize(DataInputStream in) throws IOException {
		id = in.readShort();
		name = in.readUTF();
		stamina = in.readShort();
		magic = in.readShort();
		force = in.readShort();
		wit = in.readShort();
		charm = in.readShort();
		magicPower = in.readShort();
		lead = in.readShort();
		soldierId = in.readShort();
		soldierCount = in.readShort();
		state = in.readShort();
		identity = in.readShort();
		cityId = in.readShort();
		seigneurId = in.readShort();
		appearTime = in.readShort();
	}
}
