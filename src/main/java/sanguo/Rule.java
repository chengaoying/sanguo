package sanguo;

import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.util.RandomValue;

public class Rule {
	public static short INITIAL_STAMINA = 100;				/*������ʼֵ*/
	public static short INITIAL_MAGIC = 100;				/*������ʼֵ*/
	
	public static short INITIAL_CITY_FARMING = 900;
	public static short INITIAL_CITY_COMMERCE = 900;
	public static short INITIAL_CITY_POPULATION = 1000;
	public static short INITIAL_CITY_LOYALTY = 500;
	
	public static short STAMINA_CONSUME_FOR_FARMING = 32;		/*����ũҵ���ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_COMMERCE = 32;		/*������ҵ���ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_RELIEF = 32;		/*�ȼ����ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_SEARCH = 32;		/*�������ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_MOVE = 32;			/*�ƶ����ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_CONVALESCE = 0;		/*�������ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_DISTRIBUTE = 0;		/*�������ĵ�����ֵ*/
	public static short STAMINA_CONSUME_FOR_EXPEDITION = 32;	/*�������ĵ�����ֵ*/
	
	public static short MAGIC_CONSUME_FOR_SKILL = 50;			/*ʩ���佫�����ĵľ���*/
	
	public static short LOYALTY_CONSUME_FOR_DRAFT = 20;			/*�������ĵ�����*/
	public static short LOYALTY_CONSUME_FOR_EXPEDITION = 50;	/*�������ĵ�����*/
	public static short LOYALTY_LIMIT_FOR_EXPEDITION = 300;		/*������Ҫ���������ֵ*/
	public static short LOYALTY_LIMIT_FOR_REVOLT = 300;			/*���������������ҵ����ֵ*/
	
	public static final byte CODE_GENERAL_STAMINA_NOT_ENOUGH = -1;
	public static final byte CODE_GENERAL_MAGIC_NOT_ENOUGH = -2;
	public static final byte CODE_GENERAL_SOLDIER_NOT_ENOUGH = -3;
	public static final byte CODE_CITY_WEALTH_NOT_ENOUGH = -11;
	public static final byte CODE_CITY_PROVISIONS_NOT_ENOUGH = -12;
	public static final byte CODE_CITY_LOYALTY_NOT_ENOUTH = -13;
	
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private static Permission permission = new Permission();
	
	/**
	 * ��ȡ��Ӧͳ��ֵ�Ĺ�ְ
	 * @param lead
	 * @return
	 */
	public static String getMilitaryTitle(int lead) {
		String title = "";
		if (lead == 2400) {
			title = "�󽫾�";
		}
		else if (lead == 1800) {
			title = "���ɽ�";
		}
		else if (lead == 1500) {
			title = "Уξ";
		}
		else {
			title = "����";
		}
		return title;
	}
	
	/**
	 * ���ּ��������100Сʱ������Ϊ��׼�֣�ÿ���佫��1200�֣�ÿ��ʿ����1�֣�
	 * ÿ10��Ҽ�1�֣�ÿ10���ݼ�1�֣�ÿ��Ϸ1���10��
	 * @param playDuration ʵ����Ϸʱ����������
	 * @param generals ���ͳһʱӵ���佫��
	 * @param soldiers ���ͳһʱӵ��ʿ����
	 * @param wealth ���ͳһʱӵ�н����
	 * @param provisions ���ͳһʱӵ��������
	 * @return
	 */
	public static int calcScores(int playDuration, int generals, int soldiers, int wealth, int provisions) {
		int baseScores = 100*3600;
		int scores = baseScores-playDuration*10;
		if (scores < 0) {
			scores = 0;
		}
		scores += generals*1200;
		scores += soldiers;
		scores += (wealth+provisions)/10;
		scores /= 10;
		return scores;
	}
	
	/**
	 * �����䣬���ػ�õĵ���ID
	 * @return
	 */
	public static int luckyDrawForProp() {
		int propId = -1;
		Prop[] propList = engine.propList;
		int v = RandomValue.getRandInt(100);
		for (int i = 0; i < propList.length; ++i) {
			v -= propList[i].getProbability();
			if (v < 0) {
				propId = propList[i].getId();
				break;
			}
		}
		return propId;
	}
	
	/*�ж�ִ�б���*/
	public static void judgeForRevolt() {
		City[] cityList = engine.cityList;
		City city = null;
		PopupText pt = Resource.buildPopupText();
		for (int i = 0; i < cityList.length; ++i) {
			city = cityList[i];
			if (city.belongToPlayer() && city.getLoyalty() < LOYALTY_LIMIT_FOR_REVOLT) {
				if (RandomValue.getRandInt(100) > 75) {
					int wealthLost = city.getWealth()>>2;
					int provisionsLost = city.getProvisions()>>2;
					int populationLost = city.getPopulation()>>2;
					city.decWealth(wealthLost);
					city.decProvisions(provisionsLost);
					city.decPopulation(populationLost);
					if (city.getPopulation() < INITIAL_CITY_POPULATION) {
						city.setPopulation(INITIAL_CITY_POPULATION);
					}
					city.decCommerce(city.getCommerce()>>2);
					if (city.getCommerce() < INITIAL_CITY_COMMERCE) {
						city.setCommerce(INITIAL_CITY_COMMERCE);
					}
					city.decFarming(city.getFarming()>>2);
					if (city.getFarming() < INITIAL_CITY_FARMING) {
						city.setFarming(INITIAL_CITY_FARMING);
					}
					city.setLoyalty(LOYALTY_LIMIT_FOR_REVOLT);
					
					pt.setText(city.getName()+"�����Ҳ���"+LOYALTY_LIMIT_FOR_REVOLT+", ��������"
							+", ��Ǯ��ʧ"+wealthLost+", ������ʧ"+provisionsLost+", �˿���ʧ"+populationLost);
					pt.setWaitMillisSeconds(5000);
					pt.popup();
				}
			}
		}
	}
	
	public static void freeGeneralMove() {
		General[] genList = engine.generalList;
		int randSeed = engine.cityList.length;
		for (int i = 0; i < genList.length; ++i) {
			General gen = genList[i];
			/*�佫��30%�����ƶ��������ǳ�*/
			if (gen.isIdentityFree() 
					&& gen.getAppearTime() < engine.getCurrentYear() 
					&& (RandomValue.getRandInt(100) < 30)) {
				gen.setCityId((short)RandomValue.getRandInt(randSeed));
			}
		}
	}
	
	public static void resumeGeneralStatus() {
		General[] genList = engine.generalList;
		for (int i = 0; i < genList.length; ++i) {
			General gen = genList[i];
			if (gen.isIdentityNormal()) {
				int stResume = gen.getForce()/12;
				if (stResume > 10) {
					stResume = 10;
				}
				int mgResume = gen.getWit()/12;
				if (mgResume > 10) {
					mgResume = 10;
				}
				gen.incStamina(stResume);
				gen.incMagic(mgResume);
			}
		}
	}
	
	/*�ǳ�����������ʽ*/
	public static int calcProvisionsRaiseValue(City city) {
		return (int)(((long)city.getFarming()*city.getPopulation()*city.getLoyalty())/2000000);
	}
	
	public static int raiseProvisions(City city) {
		int inc = calcProvisionsRaiseValue(city);
		if (city.belongToPlayer() && city.getFarmingEnhance()>0) {
			inc <<= 1;
		}
		city.incProvisions(inc);
		return inc;
	}
	
	/*�ǳؽ��������ʽ*/
	public static int calcWealthRaiseValue(City city) {
		return (int)(((long)city.getCommerce()*city.getPopulation()*city.getLoyalty())/2000000);
	}
	
	public static int raiseWealth(City city) {
		int inc = calcWealthRaiseValue(city);
		if (city.belongToPlayer() && city.getCommerceEnhance()>0) {
			inc <<= 1;
		}
		city.incWealth(inc);
		return inc;
	}
	
	/*�ǳ��˿�������ʽ*/
	public static int calcPopulationRaiseValue(City city) {
		return (city.getLoyalty()/10+RandomValue.getRandInt(city.getPopulation()/80));
	}
	
	public static int raisePopulation(City city) {
		int inc = calcPopulationRaiseValue(city);
		city.incPopulation(inc);
		return inc;
	}
	
	public static Permission getFarmingPermission(General general) {
		permission.reset();
		if (general.isStateNormal()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_FARMING) {
				permission.setHasPermission(true);
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_FARMING+",�޷�ִ��ũҵ����");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	/**
	 * ũҵ������ʽ(wit/5+wit/8+wit/10)
	 * @param wit �佫������ֵ
	 */
	public static int calcFarmingRailseValue(int wit) {
		int tmp = wit/5;
		return tmp+(wit>>3)+(tmp>>1);
	}
	
	public static void executeFarming(General general) {
		City city = general.getCity();
		int inc = calcFarmingRailseValue(general.getWit());
		city.incFarming(inc);
		general.decStamina(STAMINA_CONSUME_FOR_FARMING);
		general.setStateNormal();
	}
	
	public static Permission getCommercePermission(General general) {
		permission.reset();
		if (general.isStateNormal()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_COMMERCE) {
				permission.setHasPermission(true);
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_COMMERCE+",�޷�ִ����ҵ����");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	/**
	 * ��ҵ������ʽ(wit/5+wit/8+wit/10)
	 * @param wit �佫������ֵ
	 */
	public static int calcCommerceRaiseValue(int wit) {
		int tmp = wit/5;
		return tmp+(wit>>3)+(tmp>>1);
	}
	
	public static void executeCommerce(General general) {
		City city = general.getCity();
		int inc = calcCommerceRaiseValue(general.getWit());
		city.incCommerce(inc);
		general.decStamina(STAMINA_CONSUME_FOR_COMMERCE);
		general.setStateNormal();
	}
	
	public static Permission getReliefPermission(General general) {
		permission.reset();
		if (general.isStateNormal()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_RELIEF) {
				permission.setHasPermission(true);
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_RELIEF+",�޷����оȼ�");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	/**
	 * ����������ʽ(charm/8+charm/10)
	 * @param charm �佫������ֵ
	 */
	public static int calcLoyaltyRaiseValue(int charm) {
		return (charm>>3)+(charm/10);
	}
	
	public static void executeRelief(General general) {
		City city = general.getCity();
		int inc = calcLoyaltyRaiseValue(general.getCharm());
		city.incLoyalty(inc);
		general.decStamina(STAMINA_CONSUME_FOR_RELIEF);
		general.setStateNormal();
	}

	
	public static Permission getDraftPermission(City city) {
		permission.reset();
		if (city.getLoyalty() >= LOYALTY_CONSUME_FOR_DRAFT) {
			permission.setHasPermission(true);
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(true);
			permission.setCode(CODE_CITY_LOYALTY_NOT_ENOUTH);
			permission.setUseProp(true);
			permission.setMessage("�ǳ����Ҳ���"+LOYALTY_CONSUME_FOR_DRAFT+",�޷�����");
		}
		return permission;
	}
	
	public static Permission getExpeditionPermission(General general) {
		permission.reset();
		if (general.canAssignTask()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_EXPEDITION) {
				if (general.getSoldierCount() > 0) {
					permission.setHasPermission(true);
				}
				else {
					permission.setHasPermission(false);
					permission.setNeedTip(true);
					permission.setCode(CODE_GENERAL_SOLDIER_NOT_ENOUGH);
					permission.setMessage("�佫�������㣬�޷�����");
				}
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_RELIEF+",�޷�����");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	public static Permission getExpeditionPermission(City city) {
		if (city.getLoyalty() >= LOYALTY_LIMIT_FOR_EXPEDITION) {
			permission.setHasPermission(true);
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(true);
			permission.setCode(CODE_CITY_LOYALTY_NOT_ENOUTH);
			permission.setUseProp(true);
			permission.setMessage("�ǳ����Ҳ���"+LOYALTY_LIMIT_FOR_EXPEDITION+",�޷�����ս��");
		}
		return permission;
	}
	
	public static Permission getExpeditionPermission(City city, short []genList) {
		int soldierCount = engine.getGeneralListSoldierCount(genList);
		int requiredProvisions = soldierCount>>2;
		if (city.getProvisions() >= requiredProvisions) {
			permission.setValue(requiredProvisions);
			permission.setHasPermission(true);
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(true);
			permission.setCode(CODE_CITY_PROVISIONS_NOT_ENOUGH);
			permission.setUseProp(true);
			permission.setMessage("���ݲ��������25%���޷�����");
			permission.setValue(requiredProvisions);
		}
		return permission;
	}
	
	public static void executeExpedition(General general) {
		general.setStateNormal();
	}
	
	public static Permission getDistributePermission(General general) {
		permission.setHasPermission(true);
		return permission;
	}
	
	public static Permission getSearchPermission(General general) {
		if (general.isStateNormal()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_SEARCH) {
				permission.setHasPermission(true);
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_SEARCH+",�޷�ִ������");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	public static void executeSearch(General general) {
		City city = general.getCity();
		for (int i = 0; i < engine.generalList.length; ++i) {
			General g1 =engine.generalList[i];
			if (g1.isIdentityFree() 
					&& g1.getAppearTime() <= engine.getCurrentYear()
					&& g1.belongToCity(city)) {
				if (RandomValue.getRandInt(100) < 25) {	//25%�ĸ����ҵ�
					g1.setStamina(INITIAL_STAMINA);
					g1.setMagic(INITIAL_MAGIC);
					g1.setIdentityNormal();
					g1.setStateNormal();
					g1.setSeigneur(city.getSeigneur());
					
					PopupText pt = Resource.buildPopupText();
					pt.setText(general.getName()+"��"+city.getName()+"�����佫"+g1.getName()+"\n"
							+g1.getName()+"��פ�Ҿ�"+city.getName());
					pt.setWaitMillisSeconds(3000);
					pt.popup();
				}
				break;
			}
		}
		general.decStamina(STAMINA_CONSUME_FOR_SEARCH);
		general.setStateNormal();
	}

	public static Permission getMovePermission(General general) {
		if (general.isStateNormal()) {
			if (general.getStamina() >= STAMINA_CONSUME_FOR_MOVE) {
				permission.setHasPermission(true);
			}
			else {
				permission.setHasPermission(false);
				permission.setNeedTip(true);
				permission.setCode(CODE_GENERAL_STAMINA_NOT_ENOUGH);
				permission.setUseProp(true);
				permission.setMessage("�佫��������"+STAMINA_CONSUME_FOR_MOVE+",�޷������������ǳ�");
			}
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	public static void executeMove(General general) {
		general.decStamina(STAMINA_CONSUME_FOR_MOVE);
		general.setStateNormal();
	}
	
	public static Permission getConvalescePermission(General general) {
		if (general.isStateNormal()) {
			permission.setHasPermission(true);
		}
		else {
			permission.setHasPermission(false);
			permission.setNeedTip(false);
		}
		return permission;
	}
	
	public static void executeConvalesce(General general) {
		general.setStamina(INITIAL_STAMINA);
		general.setMagic(INITIAL_MAGIC);
		general.setStateNormal();
	}
	
	public static void aiTransferResource(Seigneur seigneur) {
		//TODO ai transfer resource
	}
	
	public static void aiExecuteFarming(City city, int genCount, int witAvg) {
		int wit = 60;
		if (witAvg > wit) {
			wit = witAvg;
		}
		int inc = calcFarmingRailseValue(wit);
		city.incFarming(genCount*inc);
	}
	
	public static void aiExecuteCommerce(City city, int genCount, int witAvg) {
		int wit = 60;
		if (witAvg > wit) {
			wit = witAvg;
		}
		int inc = calcCommerceRaiseValue(wit);
		city.incCommerce(genCount*inc);
	}
	
	public static void aiExecuteRelief(City city, int genCount, int charmAvg) {
		int charm = 60;
		if (charmAvg > charm) {
			charm = charmAvg;
		}
		int inc = calcLoyaltyRaiseValue(charm);
		city.incLoyalty(genCount*inc);
	}
	
	public static void aiExecuteConvalesce(General general) {
		if (general.getStamina()<=STAMINA_CONSUME_FOR_EXPEDITION || !general.canUseMagic()) {
			general.setStamina(INITIAL_STAMINA);
			general.setMagic(INITIAL_MAGIC);
		}
	}
	
	public static void aiExecuteSearch(City city, int genCount, int charmAvg) {
		//30%�ĸ����ҵ�
		int charm = 60;
		if (charmAvg > charm) {
			charm = charmAvg;
		}
		int genPos = 0;
		for (int i = 0; i < genCount; ++i) {
			boolean canFind = RandomValue.getRandInt(100)<30;
			while (!canFind && i<genCount) {
				canFind = RandomValue.getRandInt(100)<30;
				++i;
			}
			if (canFind) {
				while (genPos < engine.generalList.length) {
					General general =engine.generalList[genPos];
					++genPos;
					if (general.isIdentityFree() && general.belongToCity(city)
							&& general.getAppearTime() <= engine.getCurrentYear()) {
						general.setStamina(INITIAL_STAMINA);
						general.setMagic(INITIAL_MAGIC);
						general.setIdentityNormal();
						general.setStateNormal();
						general.setSeigneur(city.getSeigneur());
						break;
					}
				}
			}
			if (genPos >= engine.generalList.length) {
				break;
			}
		}
	}
	
	public static void aiExecuteDistribute(City city) {
		//TODO ai distribute
	}
	
	public static void aiExecuteDraft(General general) {
		City city = general.getCity();	
		int cityWealth = city.getWealth();
		int draftCount = general.getLead()-general.getSoldierCount();
		if (draftCount > 0) {
			int soldierAmount = general.getSoldier().getAmount();
			int consume = ((draftCount+100-1)/100)*soldierAmount;
			if (consume < cityWealth) {
				general.setSoldierCount(general.getLead());
				city.decWealth(consume);
			}
			else {
				draftCount = (cityWealth/soldierAmount)*100;
				general.incSoldierCount(draftCount);
				city.setWealth((short)(cityWealth%soldierAmount));
			}
		}
	}
	
	/**
	 * 
	 * @param city
	 * @param defenseList
	 * @return �����佫ǰ�����³��б�ţ�С��0��ʾ���
	 */
	public static int cityDefeated(City city) {
		int seigeurId = city.getSeigneurId();
		city.setSeigneur(null);
		City firstCity = engine.getSeigneryNearestCity(city, seigeurId);
		int newCityId = -1;
		if (firstCity != null) {
			newCityId = firstCity.getId();
			/*�з��佫ȫ���˻ص�����һ���ǳ�*/
			General[] genList = engine.generalList;
			for (int i = 0; i < genList.length; ++i) {
				General gen = genList[i];
				if (gen.isIdentityNormal()&&gen.belongToCity(city)) {
					gen.setCity(firstCity);
					gen.setStamina((short)0);
				}
			}
		}
		else {
			/*���û�������ĳǳأ���������佫ȫ����Ұ*/
			General[] genList = engine.generalList;
			for (int i = 0; i < genList.length; ++i) {
				General gen = genList[i];
				if (gen.isIdentityNormal()&&gen.belongToCity(city)) {
					gen.setIdentityFree();
					gen.setStamina((short)0);
				}
			}
		}
		/*���ս�ܣ�����ǳ�״̬*/
		if (city.belongToPlayer()) {
			city.setFarmingEnhance((short)0);
			city.setCommerceEnhance((short)0);
			city.setAttackEnhance((short)0);
			city.setDefenseEnhance((short)0);
		}
		return newCityId;
	}
	
	/*��ȡ�����ǳص�Ȩ��*/
	public static int getWeightForAttack(City city) {
		if (engine.isDebugMode() && city.belongToPlayer()) {
			return Integer.MAX_VALUE;
		}
		else {
			int soldiers = engine.getCitySoldierCount(city);
			return -(soldiers+engine.getCityGeneralCount(city)*300);
		}
	}
	
	public static void calcWeightForDefenseInCache(int seigneurId) {
		City[] cityList = engine.cityList;
		for (short i = 0; i < cityList.length; ++i) {
			if (cityList[i].getSeigneurId() == seigneurId) {
				int weight = 0;
				City tc = Resource.getUpCity(i);
				if (tc!=null && tc.getSeigneurId()!=seigneurId) {
					weight += engine.getCitySoldierCount(tc);
				}
				tc = Resource.getDownCity(i);
				if (tc!=null && tc.getSeigneurId()!=seigneurId) {
					weight += engine.getCitySoldierCount(tc);
				}
				tc = Resource.getLeftCity(i);
				if (tc!=null && tc.getSeigneurId()!=seigneurId) {
					weight += engine.getCitySoldierCount(tc);
				}
				tc = Resource.getRightCity(i);
				if (tc!=null && tc.getSeigneurId()!=seigneurId) {
					weight += engine.getCitySoldierCount(tc);
				}
			}
		}
	}
	
	/*����ռ��ճ�*/
	public static void captureEmptyCity(City src, City target) {
		//�ճǣ���һ���佫ȥռ��
		int generalId = engine.getCityGeneralIdByOrder(src.getId(), 0);
		if (generalId >= 0) {
			General general = engine.generalList[generalId];
			general.assignTaskExpedition();
			general.setCity(target);
			target.setSeigneur(general.getSeigneur());
		}
	}
	
	/*����ռ��ճ�*/
	public static void captureEmptyCity(short[] attackList, City target) {
		target.setSeigneur(engine.generalList[attackList[0]].getSeigneur());
		for (int i = 0; i < attackList.length; ++i) {
			engine.generalList[attackList[i]].setCity(target);
		}
		
		target.setCommerce(Rule.INITIAL_CITY_COMMERCE);
		target.setFarming(Rule.INITIAL_CITY_FARMING);
		target.setPopulation(Rule.INITIAL_CITY_POPULATION);
		target.setLoyalty(Rule.INITIAL_CITY_LOYALTY);
		target.setWealth((short)(target.getWealth()>>1));
		target.setProvisions((short)(target.getProvisions()>>1));
	}
	
	public static void attackPlayerCity(City src, short[] attackList, City target, short[] defenseList) {
		engine.stateMap.clear();
		
		Seigneur targetSeigneur = target.getSeigneur();
		StateBattle battle = new StateBattle();
		battle.setAttackList(attackList);
		battle.setDefenseList(defenseList);
		battle.setAttackedByPlayer(false);
		
		String tipText = "";
		int ncid = 0;
		if (battle.battle() == 0) {
			ncid = cityDefeated(target);
			captureEmptyCity(attackList, target);
			target.setCommerceEnhance((short)0);
			target.setFarmingEnhance((short)0);
			target.setAttackEnhance((short)0);
			target.setDefenseEnhance((short)0);
			tipText = "ս������\n�Ҿ�ս��\n";
			if (ncid < 0) {
				tipText = targetSeigneur.getName()+"������";
			}
			else {
				tipText = "�Ҿ���"+target.getName()+"���˵�"+engine.cityList[ncid].getName();
			}
		}
		else {
			/*ս���佫������Ϊ0*/
			for (int i = 0; i < attackList.length; ++i) {
				engine.generalList[attackList[i]].setStamina((short)0);
			}
			tipText = "ս������\n�Ҿ�ʤ��\n"+src.getSeigneur().getName()+"����"+target.getName()+"����";
		}
		engine.getSGraphics().setColor(0);
		engine.getSGraphics().fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
		PopupText pt = Resource.buildPopupText();
		pt.setText(tipText);
		pt.popup();
	}
	
	public static void attackPlayerCity(City src, City target) {
		/*���ж������Ƿ��㹻*/
		short[] attackList = getExpeditionGeneralList(src);
		if (attackList==null || attackList.length<=0) {
			return;
		}
		
		int attackSoldiers = engine.getGeneralListSoldierCount(attackList);
		if (src.getProvisions() < (attackSoldiers>>2)) {
			if (!engine.isReleaseVersion()) {
			System.out.println(src.getName()+"��������, ����ʧ��");
			}
			return;
		}
		
		PopupText pt = Resource.buildPopupText();
		pt.setText(src.getSeigneur().getName()+"�������Ҿ�"+target.getName());
		pt.popup();
		
		short[] defenseList = engine.getCityAllDefenseGeneralList(target.getId());
		if (defenseList!=null && defenseList.length>0) {
			/*ȷ���������ӣ�������*/
			src.decProvisions(attackSoldiers>>2);
			
			int defenseSoldiers = engine.getGeneralListSoldierCount(defenseList);
			if (defenseSoldiers <= (attackSoldiers>>1)) {
				PopupConfirm pc = Resource.buildPopupConfirm();
				pc.setText(target.getName()+"���ڱ������㣬�Ƿ�Ƹ���佫��ս?");
				pc.setButtonText(Resource.STR_CONFIRM_ADD_GENERAL);
				if (pc.popup() == 0) {
					engine.stateMap.clear();
					StateMarket market = new StateMarket(StateMarket.TYPE_GENERAL_MARKET);
					market.setTargetCity(target);
					market.popup();
					
					defenseList = engine.getCityAllDefenseGeneralList(target.getId());
					attackPlayerCity(src, attackList, target, defenseList);
				}
				else {
					attackPlayerCity(src, attackList, target, defenseList);
				}
			}
			else {
				attackPlayerCity(src, attackList, target, defenseList);
			}
		}
		else {
			PopupConfirm pc = Resource.buildPopupConfirm();
			pc.setText(target.getName()+"����û���佫���Ƿ�Ƹ���佫��ս?");
			pc.setButtonText(Resource.STR_CONFIRM_ADD_GENERAL);
			if (pc.popup() == 0) {
				engine.stateMap.clear();
				StateMarket market = new StateMarket(StateMarket.TYPE_GENERAL_MARKET);
				market.setTargetCity(target);
				market.popup();
				
				defenseList = engine.getCityAllDefenseGeneralList(target.getId());
				if (defenseList!=null && defenseList.length>0) {
					/*ȷ���������Ӻ�ſ�����*/
					src.decProvisions(attackSoldiers>>2);
					
					attackPlayerCity(src, attackList, target, defenseList);
				}
				else {
					captureEmptyCity(src, target);
				}
			}
			else {
				captureEmptyCity(src, target);
			}
		}
	}
	
	public static void qsortGeneralListByWitForPlayer(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareWit(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareWit(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }
	}
	
	public static void qsortGeneralListByCharmForPlayer(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareCharm(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareCharm(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }
	}
	
	public static void qsortGeneralListByStaminaForPlayerConvalesce(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareStaminaForConvalesce(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareStaminaForConvalesce(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }
	}
	
	public static void qsortGeneralListByStaminaForPlayerMove(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareStaminaForMove(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareStaminaForMove(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }
	}
	
	public static void qsortGeneralListByArmyPowerForPlayer(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareArmyPowerForPlayer(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareArmyPowerForPlayer(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }	
	}
	
	public static void qsortGeneralListByArmyPowerForAI(short[] genList) {
		if (genList==null || genList.length<2) {
			return;
		}
	    //int []stack = new int[genList.length];
	    short[] stack = engine.generalCache;
	    int top = 0;
	    int low,high,i,j;
	    short pivot;
		//������������ջ
	    stack[top++] = (short)(genList.length-1);
	    stack[top++] = 0;
	    while(top != 0)
	    {
	        low = stack[--top];
	        high = stack[--top];
	        pivot = genList[low];
	        i = low;
	        j = high;
	        while (i<j) {
		        while (j>i && engine.generalList[genList[j]].compareArmyPowerForAI(engine.generalList[pivot])<=0) {
		        	--j;
		        }
		        if (i != j) {
		        	genList[i++] = genList[j];
		        }
		        while (i<j && engine.generalList[genList[i]].compareArmyPowerForAI(engine.generalList[pivot])>=0) {
		        	++i;
		        }
		        if (i != j) {
		        	genList[j--] = genList[i];
		        }
	        }
	        genList[i] = pivot;

	        if(i-low > 1)
	        {
	            stack[top++] = (short)(i-1);
	            stack[top++] = (short)(low);
	        }
	        if(high-i > 1)
	        {
	            stack[top++] = (short)(high);
	            stack[top++] = (short)(i+1);
	        }
	    }
	}
	
	public static short[] getExpeditionGeneralList(City src) {
		short[] genList = engine.getCityBattleGeneralList(src.getId());
		if (genList!=null && genList.length > 0) {
			qsortGeneralListByArmyPowerForAI(genList);
			int count = genList.length;
			if (count > 5) {
				count = 5;
			}
			else {
				--count;
			}
			short[] attackList = new short[count];
			for (int i = 0; i < attackList.length; ++i) {
				attackList[i] = genList[i];
			}
			return attackList;
		}
		else {
			return null;
		}
	}
	
	public static void attackOtherCityFailed(City src, short[] attackList, City target, int targetSoldiers) {
		General g1 = null;
		int soldiers = 0;
		General[] generalList = engine.generalList;
		/*�������ӱ���ȫ��*/
		for (int i = 0; i < attackList.length; ++i) {
			g1 = generalList[attackList[i]];
			soldiers += g1.getSoldierCount();
			g1.setSoldierCount((short)(0));
			g1.setStamina((short)0);
		}
		
		/*���ط��������ٽ������ӱ�����50%*/
		soldiers >>= 1;
		for (int i = 0; i < generalList.length; ++i) {
			g1 = generalList[i];
			if (g1.belongToCity(target) && g1.isIdentityNormal()) {
				if (g1.getSoldierCount() >= soldiers) {
					g1.decSoldierCount(soldiers);
					soldiers ^= soldiers;
				}
				else {
					soldiers -= g1.getSoldierCount();
					g1.setSoldierCount((short)(0));
				}
			}
			if (soldiers <= 0) {
				break;
			}
		}
	}
	
	public static void attackOtherCitySuccessful(City src, short[] attackList, City target, int targetSoldiers) {
		processAttackArmyLose(attackList, target, targetSoldiers);
		processAttackArmyCapture(attackList, target, targetSoldiers);
	}

	private static void processAttackArmyCapture(short[] attackList,
			City target, int targetSoldiers) {
		Seigneur targetSeigneur = target.getSeigneur();
		int ncid = cityDefeated(target);
		captureEmptyCity(attackList, target);
		if (ncid < 0) {
			/*����Ĺ��Ҿ���������*/
			General targetGeneral = engine.generalList[targetSeigneur.getGeneralId()];
			targetGeneral.setIdentityNormal();
			targetGeneral.setSeigneur(target.getSeigneur());
			//engine.generalList[targetSeigneur.getGeneralId()].setSeigneur(target.getSeigneur());
			engine.getSGraphics().setColor(0);
			engine.getSGraphics().fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
			PopupText pt = Resource.buildPopupText();
			pt.setText(targetSeigneur.getName()+"������");
			pt.setWaitMillisSeconds(3000);
			pt.popup();
		}
	}

	private static void processAttackArmyLose(short[] attackList,
			City target, int targetSoldies) {
		General g1 = null;
		int soldiers = targetSoldies;
		General[] generalList = engine.generalList;
		/*���ز��ӱ���ȫ��*/
		for (int i = 0; i < generalList.length; ++i) {
			g1 = generalList[i];
			if (g1.belongToCity(target) && g1.isStateNormal()) {
				g1.setSoldierCount((short)(0));
			}
		}
		
		/*�������������ٷ��ز��ӱ�����75%*/
		soldiers -= (soldiers>>2);
		for (int i = 0; i < attackList.length; ++i) {
			g1 = generalList[attackList[i]];
			if (g1.getSoldierCount() >= soldiers) {
				g1.decSoldierCount(soldiers);
				soldiers ^= soldiers;
			}
			else {
				soldiers -= g1.getSoldierCount();
				g1.setSoldierCount((short)(0));
			}
			if (soldiers <= 0) {
				break;
			}
		}
	}
	
	public static void attackOtherCitySuccessfulWithoutLoss(City src, short[] attackList, City target, int targetSoldiers) {
		processAttackArmyCapture(attackList, target, targetSoldiers);
	}
	
	public static boolean isSuccessForAiAttackOtherCity(short[] attackList, int targetSoldiers) {
		boolean success = false;
		if (targetSoldiers > 0) {
			int srcSoldiers = engine.getGeneralListSoldierCount(attackList);
			int leftRange = targetSoldiers>>1;
			int rightRange = targetSoldiers<<1;
			int prob = RandomValue.getRandInt(rightRange-leftRange)+leftRange;
			if (srcSoldiers >= prob) {
				return true;
			}
		}
		return success;
	}
	
	public static void attackOtherCity(City src, City target) {
		/*����ǰ�ж������Ƿ��㹻*/
		short[] attackList = getExpeditionGeneralList(src);
		if (attackList==null || attackList.length<=0) {
			return;
		}
		int attackSoldiers = engine.getGeneralListSoldierCount(attackList);
		if (src.getProvisions() >= (attackSoldiers>>2)) {
			src.decProvisions(attackSoldiers>>2);
		}
		else {
			if (!engine.isReleaseVersion()) {
				System.out.println(src.getName()+"��������, ����ʧ��");
			}
			return;
		}
		
		if (attackList!=null && attackList.length>0) {
			int targetSoldiers = engine.getCitySoldierCount(target);
			
			if (!engine.isReleaseVersion()) {
				System.out.println(src.getSeigneur().getName()+"��"+src.getName()+"�ǽ���"+target.getSeigneur().getName()+"��"+target.getName()+"��");
				System.out.println("��������:");
				for (int i = 0; i < attackList.length; ++i) {
					engine.generalList[attackList[i]].printBattleGeneralAttribute();
				}
				System.out.println("���ر���: "+targetSoldiers);
			}
			if (targetSoldiers > 0) {
				if (isSuccessForAiAttackOtherCity(attackList, targetSoldiers)) {
					if (!engine.isReleaseVersion()) {
						System.out.println(src.getSeigneur().getName()+"��ռ��"+target.getName());
					}
					attackOtherCitySuccessful(src, attackList, target, targetSoldiers);
				}
				else {
					if (!engine.isReleaseVersion()) {
						System.out.println(src.getSeigneur().getName()+"��ս��");
					}
					attackOtherCityFailed(src, attackList, target, targetSoldiers);
				}
			}
			else {
				if (!engine.isReleaseVersion()) {
					System.out.println(src.getSeigneur().getName()+"��ռ��"+target.getName());
				}
				attackOtherCitySuccessfulWithoutLoss(src, attackList, target, targetSoldiers);
			}
		}
	}
	
	public static void aiExecuteExpedition(City city) {
		
		if (engine.getCityGeneralCount(city) >= 2/* && engine.month%3 == 2*/) {
			int weight = getWeightForAttack(city);
			City targetCity = null;
			
			City tc = Resource.getUpCity(city.getId());
			if (tc != null && !tc.belongToSeigneur(city.getSeigneur())) {
				int tw = getWeightForAttack(tc);
				if (tw > weight) {
					targetCity = tc;
					weight = tw;
				}
			}
			
			tc = Resource.getLeftCity(city.getId());
			if (tc!=null && !tc.belongToSeigneur(city.getSeigneur())) {
				int tw = getWeightForAttack(tc);
				if (tw > weight) {
					targetCity = tc;
					weight = tw;
				}
			}
			
			tc = Resource.getDownCity(city.getId());
			if (tc!=null && !tc.belongToSeigneur(city.getSeigneur())) {
				int tw = getWeightForAttack(tc);
				if (tw > weight) {
					targetCity = tc;
					weight = tw;
				}
			}
			
			tc = Resource.getRightCity(city.getId());
			if (tc!=null && !tc.belongToSeigneur(city.getSeigneur())) {
				int tw = getWeightForAttack(tc);
				if (tw > weight) {
					targetCity = tc;
					weight = tw;
				}
			}
			
			if (targetCity != null) {
				boolean canAttack = false;
				if (engine.month < 36) {
					if (RandomValue.getRandInt(100) > 80) {
						canAttack = true;
					}
				}
				else if (engine.month < 72){
					if (RandomValue.getRandInt(100) > 50) {
						canAttack = true;
					}
				}
				else {
					if (RandomValue.getRandInt(100) > 20) {
						canAttack = true;
					}
				}
				if (canAttack) {
					if (targetCity.belongToAnySeigneur()) {
						if (targetCity.belongToPlayer()) {
							attackPlayerCity(city, targetCity);
						}
						else {
							attackOtherCity(city, targetCity);
						}
					}
					else {
						if (!engine.isReleaseVersion()) {
							System.out.println(city.getSeigneur().getName()+"��ռ��ճ�"+targetCity.getName());
						}
						captureEmptyCity(city, targetCity);
					}
				}
			}
		}
	}

}
