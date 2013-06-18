package sanguo;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.microedition.midlet.MIDlet;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.OwnProp;
import cn.ohyeah.stb.game.GameCanvasEngine;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.util.DateUtil;

public class NewSanguoGameEngine extends GameCanvasEngine{
	static final boolean RELEASE_VERSION = true;
	
	static final byte STATE_START = 0;
	static final byte STATE_MAIN = 1;
	static final byte STATE_MAP = 2;
	
	static NewSanguoGameEngine instance = buildGameEngine();
	
	StateMain stateMain;
	StateMap stateMap;
	
	byte state;
	boolean loadPropInfoSuccess = false;
	
	int playDuration;
	long gameStartMillis;
	long gameStartTime;
	int month;
	int playerSeigneur;
	General[] generalList;
	short[] generalCache;
	short[] cityCache;
	Seigneur[] seigneurList;
	City[] cityList;
	Soldier[] soldierList;
	Prop[] propList;
	GeneralProp[] generalPropList;
	short[] depotChanges;
	short[] propDepot;
	short[] generalDepot;
	
	private static NewSanguoGameEngine buildGameEngine() {
		return new NewSanguoGameEngine(NewSanguoMIDlet.getInstance());
	}
	
	private NewSanguoGameEngine(MIDlet midlet) {
		super(midlet);
		setRelease(RELEASE_VERSION);
	}
	
	/*得到道具数量*/
	public int getDepotPropCount(Prop prop) {
		return getDepotPropCountById(prop.getId());
	}
	
	public int getDepotPropCountById(int propId) {
		return propDepot[propId];
	}
	
	public int getPrizeBoxCount() {
		return propDepot[18];
	}
	
	public void setPrizeBoxCount(int count) {
		propDepot[18] = (short)count;
		System.out.println("设置宝箱数："+count);
	}
	
	public void addPrizeBox(int inc) {
		int value = propDepot[18]+inc;
		if (value > 99) {
			propDepot[18] = (short)99;
		}
		else {
			propDepot[18] = (short)value;
		}
		System.out.println("增加宝箱，数目变为"+propDepot[18]);
	}
	
	public void usePrizeBox(int dec) {
		int value = propDepot[18]-dec;
		if (value < 0) {
			propDepot[18] = 0;
		}
		else {
			propDepot[18] = (short)value;
		}
		System.out.println("使用宝箱，数目变为"+propDepot[18]);
	}
	/*查询武将*/
	public int getDepotGeneralPropCount(Prop prop) {
		return getDepotGeneralPropCountById(prop.getId());
	}
	
	public int getDepotGeneralPropCountById(int propId) {
		return generalDepot[propId];
	}
	
	/*增加道具*/
	public void addDepotProp(Prop prop) {
		addDepotPropById(prop.getId());
	}
	
	public void addDepotPropById(int propId) {
		++propDepot[propId];
		if (propDepot[propId] > 99) {
			propDepot[propId] = 99;
		}
		if (propId < 18) {
			++depotChanges[propId];
		}
		System.out.println("增加"+propList[propId].getName()+"，数目变为"+propDepot[propId]);
	}
	
	/*增加武将*/
	public void addDepotGeneralProp(Prop prop) {
		addDepotGeneralPropById(prop.getId());
	}
	
	public void addDepotGeneralPropById(int propId) {
		++generalDepot[propId];
		++depotChanges[propId+propList.length];
		System.out.println("增加"+generalPropList[propId].getName()+"，数目变为"+generalDepot[propId]);
	}
	
	/*使用道具*/
	public void useDepotProp(Prop prop) {
		useDepotPropById(prop.getId());
	}
	
	public void useDepotPropById(int propId) {
		--propDepot[propId];
		if (propId < 18) {
			++depotChanges[propId];
		}
		System.out.println("使用"+propList[propId].getName()+"，数目变为"+propDepot[propId]);
	}
	
	/*使用武将*/
	public void useDepotGeneralProp(Prop prop) {
		useDepotGeneralPropById(prop.getId());
	}
	
	public void useDepotGeneralPropById(int propId) {
		--generalDepot[propId];
		++depotChanges[propId+propList.length];
		System.out.println("派遣"+generalPropList[propId].getName()+"，数目变为"+generalDepot[propId]);
	}
	
	/*当前年代*/
	public int getCurrentYear() {
		return 189+month/12;
	}
	
	/*游戏时间字符串*/
	public String getGameTimeStr(int month) {
		int y = 189+month/12;
		int m = (month%12)+1;
		return "公元"+y+"年"+m+"月";
	}
	
	public byte[] getSaveData() throws IOException {
		ByteArrayOutputStream baos = null;
		DataOutputStream dos = null;
		try {
			baos = new ByteArrayOutputStream();
			dos = new DataOutputStream(baos);
			dos.writeLong(gameStartTime);
			dos.writeInt(playerSeigneur);
			dos.writeInt(month);
			dos.writeShort(getPrizeBoxCount());
			serializeGeneralList(dos);
			serializeCityList(dos);
			serializeSeigneurList(dos);
			return baos.toByteArray();
		}
		finally {
			try {
				if (dos != null) {
					dos.close();
				}
			}
			finally {
				if (baos != null) {
					baos.close();
				}
			}
		}
	}
	
	public int setSaveData(byte[] data) throws IOException {
		ByteArrayInputStream bais = null;
		DataInputStream dis = null;
		try {
			bais = new ByteArrayInputStream(data);
			dis = new DataInputStream(bais);
			long gameStartTime = dis.readLong();
			int playerSeigneur = dis.readInt();
			int month = dis.readInt();
			short prizeBoxCount = dis.readShort();
			General[] generalList = deserializeGeneralList(dis);
			City[] cityList = deserializeCityList(dis);
			Seigneur[] seigneurList = deserializeSeigneurList(dis);
			
			/*如果读取的数据没有错，则使用*/
			this.gameStartTime = gameStartTime;
			this.playerSeigneur = playerSeigneur;
			this.month = month;
			setPrizeBoxCount(prizeBoxCount);
			this.generalList = generalList;
			this.cityList = cityList;
			this.seigneurList = seigneurList;
			
			if (generalCache == null) {
				generalCache = new short[generalList.length];
			}
			if (cityCache == null) {
				cityCache = new short[cityList.length];
			}
			return 0;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
		finally {
			try {
				if (dis != null) {
					dis.close();
				}
			}
			finally {
				if (bais != null) {
					bais.close();
				}
			}
		}
	}
	
	public short getPlayerSeigneurCityId() {
		return generalList[seigneurList[playerSeigneur].getGeneralId()].getCityId();
	}
	
	public City getPlayerSeigneurCity() {
		return cityList[getPlayerSeigneurCityId()];
	}
	
	public short getSeigneurIdByGeneralId(int generalId) {
		return cityList[generalList[generalId].getCityId()].getSeigneurId();
	}
	
	public Seigneur getSeigneur(General g) {
		return seigneurList[cityList[g.getCityId()].getSeigneurId()];
	}
	
	public Seigneur getSeigneur(City c) {
		return seigneurList[c.getSeigneurId()];
	}
	
	/*查找最近的城池，广度优先搜索*/
	public City getSeigneryNearestCity(City city, int seigeurId) {
		
		short cityId = city.getId();
		City nearestCity = null;
		
		/*用cityCache记录带扫描的cityId*/
		cityCache[0] = cityId;
		/*用generalCache记录扫描过的cityId，避免回环*/
		for (int i = 0; i < generalCache.length; ++i) {
			generalCache[i] = 0;
		}
		generalCache[cityId] = 1;
		int cacheLen = 1;
		int cachePos = 0;
		while (cachePos < cacheLen) {
			cityId = cityCache[cachePos];
			++cachePos;
			
			City tc = Resource.getUpCity(cityId);
			if (tc != null && generalCache[tc.getId()]!=1) {
				if (tc.getSeigneurId() == seigeurId) {
					nearestCity = tc;
					break;
				}
				cityCache[cacheLen] = tc.getId();
				++cacheLen;
				generalCache[tc.getId()] = 1;
			}
			
			tc = Resource.getDownCity(cityId);
			if (tc != null&& generalCache[tc.getId()]!=1) {
				if (tc.getSeigneurId() == seigeurId) {
					nearestCity = tc;
					break;
				}
				cityCache[cacheLen] = tc.getId();
				++cacheLen;
				generalCache[tc.getId()] = 1;
			}
			
			tc = Resource.getLeftCity(cityId);
			if (tc != null&& generalCache[tc.getId()]!=1) {
				if (tc.getSeigneurId() == seigeurId) {
					nearestCity = tc;
					break;
				}
				cityCache[cacheLen] = tc.getId();
				++cacheLen;
				generalCache[tc.getId()] = 1;
			}
			
			tc = Resource.getRightCity(cityId);
			if (tc != null&& generalCache[tc.getId()]!=1) {
				if (tc.getSeigneurId() == seigeurId) {
					nearestCity = tc;
					break;
				}
				cityCache[cacheLen] = tc.getId();
				++cacheLen;
				generalCache[tc.getId()] = 1;
			}
		}
		return nearestCity;
	}
	
	public int getSeigneurCityCount(int seigneurId) {
		int count = 0;
		for (int i = 0; i < cityList.length; ++i) {
			if (cityList[i].belongToSeigneur((short)seigneurId)) {
				++count;
			}
		}
		return count;
	}
	
	public int getSeigneurGeneralCount(int seigneurId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].belongToSeigneur((short)seigneurId)
					&& generalList[i].isIdentityNormal()) {
				++count;
			}
		}
		return count;
	}
	
	public int getSeigneurSoldierCount(int seigneurId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].belongToSeigneur((short)seigneurId) 
					&& generalList[i].isIdentityNormal()) {
				count += generalList[i].getSoldierCount();
			}
		}
		return count;
	}
	
	public int getSeigneurWealth(int seigneurId) {
		int wealth = 0;
		for (int i = 0; i < cityList.length; ++i) {
			if (cityList[i].belongToSeigneur((short)seigneurId)) {
				wealth += cityList[i].getWealth();
			}
		}
		return wealth; 
	}
	
	public int getSeigneurProvisions(int seigneurId) {
		int provisions = 0;
		for (int i = 0; i < cityList.length; ++i) {
			if (cityList[i].getSeigneurId() == seigneurId) {
				provisions += cityList[i].getProvisions();
			}
		}
		return provisions;
	}
	
	public int getSeigneurPopulation(int seigeurId) {
		int population = 0;
		for (int i = 0; i < cityList.length; ++i) {
			if (cityList[i].getSeigneurId() == seigeurId) {
				population += cityList[i].getPopulation();
			}
		}
		return population;
	}
	
	public int getCityGeneralCount(City city) {
		return getCityGeneralCount(city.getId());
	}
	
	public int getCityGeneralCount(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					&&!generalList[i].isStateMove()) {
				++count;
			}
		}
		return count;
	}
	
	public int getCityAllGeneralCount(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()) {
				++count;
			}
		}
		return count;
	}
	
	public int getFirstGeneralId(int cityId) {
		int firstId = -1;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					/*&&!generalList[i].isStateMove()*/) {
				firstId = i;
				break;
			}
		}
		return firstId;
	}
	
	public int getCityGeneralIdByOrder(int cityId, int order) {
		int generalId = -1;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					/*&&!generalList[i].isStateMove()*/) {
				--order;
				if (order < 0) {
					generalId = i;
					break;
				}
			}
		}
		return generalId;
	}
	
	public int getCityNextGeneralId(int generalId) {
		int nextId = -1;
		short cityId = generalList[generalId].getCityId();
		for (int i = generalId+1; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					/*&&!generalList[i].isStateMove()*/) {
				nextId = i;
				break;
			}
		}
		return nextId;
	}
	
	public int getCityPrevGeneralId(int generalId) {
		int prevId = -1;
		short cityId = generalList[generalId].getCityId();
		for (int i = generalId-1; i >= 0; --i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					/*&&!generalList[i].isStateMove()*/) {
				prevId = i;
				break;
			}
		}
		return prevId;
	}
	
	public short []getCityAllGeneralList(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()) {
				generalCache[count++] = (short)i;
			}
		}
		short[] genList = null;
		if (count != 0) {
			genList = new short[count];
			System.arraycopy(generalCache, 0, genList, 0, count);
		}
		return genList;
	}
	
	public short []getCityAllDefenseGeneralList(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					&&!generalList[i].isStateMove()) {
				generalCache[count++] = (short)i;
			}
		}
		short[] genList = null;
		if (count != 0) {
			genList = new short[count];
			System.arraycopy(generalCache, 0, genList, 0, count);
		}
		return genList;
	}
	
	public short[] getCityBattleGeneralList(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()
					&&!generalList[i].isStateMove()
					&&generalList[i].getSoldierCount()>0) {
				generalCache[count++] = (short)i;
			}
		}
		short[] genList = null;
		if (count != 0) {
			genList = new short[count];
			System.arraycopy(generalCache, 0, genList, 0, count);
		}
		return genList;
	}
	
	public short[] getCityGeneralList(int cityId, int count) {
		short []list = new short[count];
		int n = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (n < count) {
				if (generalList[i].getCityId() == cityId
						&&generalList[i].isIdentityNormal()
						&&!generalList[i].isStateMove()) {
					list[n++] = (short)i;
				}
			}
			else {
				break;
			}
		}
		return list;
	}
	
	public int getCitySoldierCount(int cityId) {
		int count = 0;
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId
					&&generalList[i].isIdentityNormal()) {
				count += generalList[i].getSoldierCount();
			}
		}
		return count;
	}
	
	public int getCitySoldierCount(City city) {
		return getCitySoldierCount(city.getId());
	}
	
	public int getGeneralListSoldierCount(short[] genList) {
		int count = 0;
		if (genList != null) {
			for (int i = 0; i < genList.length; ++i) {
				count += generalList[genList[i]].getSoldierCount();
			}
		}
		return count;
	}
	
	private void serializeGeneralList(DataOutputStream dos) throws IOException {
		dos.writeShort(generalList.length);
		for (int i = 0; i < generalList.length; ++i) {
			generalList[i].serialize(dos);
		}
	}
	
	
	private General[] deserializeGeneralList(DataInputStream dis) throws IOException {
		General[] generalList = null;
		short count = dis.readShort();
		if (count > 0) {
			generalList = new General[count];
			for (int i = 0; i < count; ++i) {
				generalList[i] = new General();
				generalList[i].deserialize(dis);
			}
		}
		return generalList;
	}
	
	public void printGeneralList() {
		for (int i = 0; i < generalList.length; ++i) {
			generalList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	public void printCityGeneralList(short cityId) {
		for (int i = 0; i < generalList.length; ++i) {
			if (generalList[i].getCityId() == cityId) {
				generalList[i].print();
				System.out.println();
			}
		}
		System.out.println();
	}
	
	private void serializeSeigneurList(DataOutputStream dos) throws IOException {
		dos.writeShort(seigneurList.length);
		for (int i = 0; i < seigneurList.length; ++i) {
			seigneurList[i].serialize(dos);
		}
	}
	
	
	private Seigneur[] deserializeSeigneurList(DataInputStream dis) throws IOException {
		Seigneur[] seigneurList = null;
		int count = dis.readShort();
		if (count > 0) {
			seigneurList = new Seigneur[count];
			for (int i = 0; i < count; ++i) {
				seigneurList[i] = new Seigneur();
				seigneurList[i].deserialize(dis);
			}
		}
		return seigneurList;
	}
	
	public void printSeigneurList() {
		for (int i = 0; i < seigneurList.length; ++i) {
			seigneurList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	private void serializeCityList(DataOutputStream dos) throws IOException {
		dos.writeShort(cityList.length);
		for (int i = 0; i < cityList.length; ++i) {
			cityList[i].serialize(dos);
		}
	}
	
	
	private City[] deserializeCityList(DataInputStream dis) throws IOException {
		City[] cityList = null;
		short count = dis.readShort();
		if (count > 0) {
			cityList = new City[count];
			for (int i = 0; i < count; ++i) {
				cityList[i] = new City();
				cityList[i].deserialize(dis);
			}
		}
		return cityList;
	}
	
	public void printCityList() {
		for (int i = 0; i < cityList.length; ++i) {
			cityList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	private void deserializeSoldierList(DataInputStream dis) throws IOException {
		short count = dis.readShort();
		if (count > 0) {
			soldierList = new Soldier[count];
			for (int i = 0; i < count; ++i) {
				soldierList[i] = new Soldier();
				soldierList[i].deserialize(dis);
			}
		}
	}
	
	public void printSoldierList() {
		for (int i = 0; i < soldierList.length; ++i) {
			soldierList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	private void deserializePropList(DataInputStream dis) throws IOException {
		short num = dis.readShort();
		if (num > 0) {
			propList = new Prop[num];
			for (int i = 0; i < num; ++i) {
				propList[i] = new Prop();
				propList[i].deserialize(dis);
			}
		}
		Prop.setEngineService(engineService);
		if (propDepot == null) {
			propDepot = new short[propList.length];
		}
		for (int i = 0; i < propDepot.length; ++i) {
			propDepot[i] = 0;
		}
		printPropList();
	}
	
	public void printPropList() {
		for (int i = 0; i < propList.length; ++i) {
			propList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	private void deserializeGeneralPropList(DataInputStream dis) throws IOException {
		short num = dis.readShort();
		if (num > 0) {
			generalPropList = new GeneralProp[num];
			for (int i = 0; i < num; ++i) {
				generalPropList[i] = new GeneralProp();
				generalPropList[i].deserialize(dis);
			}
		}
		
		if (generalDepot == null) {
			generalDepot = new short[generalPropList.length];
		}
		for (int i = 0; i < generalDepot.length; ++i) {
			generalDepot[i] = 0;
		}
	}
	
	public void printGeneralPropList() {
		for (int i = 0; i < generalPropList.length; ++i) {
			generalPropList[i].print();
			System.out.println();
		}
		System.out.println();
	}
	
	/**
	 * 判断游戏结局
	 * @return -1游戏失败 0游戏进行中  1游戏胜利
	 */
	public int checkGameResult() {
		int result = 0;
		boolean failed = true;
		boolean success = true;
		for (int i = 0; i < cityList.length; ++i) {
			short sid = cityList[i].getSeigneurId();
			if (sid >= 0) {
				if (success && sid!=playerSeigneur) {
					success = false;
				}
				if (failed && sid==playerSeigneur){
					failed = false;
				}
				if (!failed && !success) {
					break;
				}
			}
		}
		if (failed) result = -1;
		else if (success) result = 1;
		return result;
	}
	
	private void closeStream(InputStream is) {
		if (is != null) {
			try {
				is.close();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private void closeResourceStream() {
		closeStream(dis);
		closeStream(is);
		dis = null;
		is = null;
	}
	
	InputStream is = null;
	DataInputStream dis = null;
	public int stateStartLoading(int progress) {
		//InputStream is = null;
		//DataInputStream dis = null;
		try {
			if (progress == 0) {
				is = this.getClass().getResourceAsStream("/data/constant.bin");
				dis = new DataInputStream(is);
				String magic = dis.readUTF();
				if (!"sanguo".equals(magic)) {
					throw new RuntimeException("数据文件标示错误");
				}
				short version = dis.readShort();
				if (version != 1)  {
					throw new RuntimeException("数据文件版本错误");
				}
				progress=20;
			}
			if (progress == 20) {
				deserializeSoldierList(dis);
				progress=50;
			}
			if (progress == 50) {
				System.out.println("加载道具信息");
				deserializePropList(dis);
				progress=70;
			}
			if (progress == 70) {
				deserializeGeneralPropList(dis);
				if (depotChanges == null) {
					depotChanges = new short[propList.length+generalPropList.length];
				}
				closeResourceStream();
				progress=100;
			}
		} catch (IOException e) {
			e.printStackTrace();
			closeResourceStream();
			throw new RuntimeException(e.getMessage());
		}
		return 100;
	}
	
	public int loading(int progress) {
		int prog = 0;
		switch (state) {
		case STATE_START: 
			prog = stateStartLoading(progress);
			break;
		case STATE_MAIN: 
			prog = 100;
			break;
		case STATE_MAP: 
			prog = 100;
			break;
		default: 
			throw new RuntimeException("未知的状态, state="+state);
		}
		return prog;
	}

	public boolean loadConstantData() {
		InputStream is = this.getClass().getResourceAsStream("/data/constant.bin");
		DataInputStream dis = new DataInputStream(is);
		try {
			String magic = dis.readUTF();
			if (!"sanguo".equals(magic)) {
				throw new RuntimeException("数据文件标示错误");
			}
			short version = dis.readShort();
			if (version != 1)  {
				throw new RuntimeException("数据文件版本错误");
			}
			deserializeSoldierList(dis);
			deserializePropList(dis);
			deserializeGeneralPropList(dis);
			if (depotChanges == null) {
				depotChanges = new short[propList.length+generalPropList.length];
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}

	public boolean loadVariableData() {
		InputStream is = this.getClass().getResourceAsStream("/data/variable.bin");
		DataInputStream dis = new DataInputStream(is);
		try {
			String magic = dis.readUTF();
			if (!"sanguo".equals(magic)) {
				throw new RuntimeException("数据文件标示错误");
			}
			short version = dis.readShort();
			if (version != 1)  {
				throw new RuntimeException("数据文件版本错误");
			}
			generalList = null;
			seigneurList = null;
			cityList = null;
			generalList = deserializeGeneralList(dis);
			seigneurList = deserializeSeigneurList(dis);
			cityList = deserializeCityList(dis);
			if (generalCache == null) {
				generalCache = new short[generalList.length];
			}
			if (cityCache == null) {
				cityCache = new short[cityList.length];
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		finally {
			try {
				dis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return true;
	}
	
	public int pushProp() {
		try {
			int result = 0;
			int count = 0;
			for (int i = 0; i < depotChanges.length; ++i) {
				if (depotChanges[i] > 0) {
					++count;
				}
			}
			
			if (!isReleaseVersion()) {
				System.out.println("拥有道具信息");
				for (int i = 0; i < propDepot.length-1; ++i) {
					if (propDepot[i] > 0) {
						System.out.println(propList[i].getName()+": "+propDepot[i]);
					}
				}
				for (int i = 0; i < generalDepot.length; ++i) {
					if (generalDepot[i] > 0) {
						System.out.println(generalPropList[i].getName()+": "+generalDepot[i]);
					}
				}
			}
			
			if (count > 0) {
				int[] propIds = new int[count];
				int[] counts = new int[count];
				int j = 0;
				for (int i = 0; i < depotChanges.length; ++i) {
					if (depotChanges[i] > 0) {
						if (i < propList.length) {
							propIds[j] = propList[i].getPropId();
							counts[j] = propDepot[i];
						}
						else {
							propIds[j] = generalPropList[i-propDepot.length].getPropId();
							counts[j] = generalDepot[i-propDepot.length];
						}
						++j;
					}
				}
				if (!isReleaseVersion()) {
					System.out.println("同步道具信息");
					for (int i = 0; i < propIds.length; ++i) {
						if (propIds[i] < generalPropList[0].getPropId()) {
							int pid = propIds[i]-propList[0].getPropId();
							System.out.println(propList[pid].getName()+": "+propDepot[pid]+"==>"+counts[i]);
						}
						else {
							int pid = propIds[i]-generalPropList[0].getPropId();
							System.out.println(generalPropList[pid].getName()+": "+generalDepot[pid]+"==>"+counts[i]);
						}
					}
				}
				ServiceWrapper sw = getServiceWrapper();
				sw.synProps(propIds, counts);
				if (sw.isServiceSuccessful()) {
					for (int i = 0; i < depotChanges.length; ++i) {
						depotChanges[i] = 0;
					}
				}
			}
			
			return result;
		}
		catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}
	
	public int pullProp() {
		if (!loadPropInfoSuccess) {
			try {
				if (propDepot == null) {
					propDepot = new short[propList.length];
				}
				for (int i = 0; i < propDepot.length; ++i) {
					propDepot[i] = 0;
				}
				
				if (generalDepot == null) {
					generalDepot = new short[generalPropList.length];
				}
				for (int i = 0; i < generalDepot.length; ++i) {
					generalDepot[i] = 0;
				}
				
				ServiceWrapper sw = getServiceWrapper();
				OwnProp[] ops = sw.queryOwnPropList();
				if (sw.isServiceSuccessful()) {
					loadPropInfoSuccess = true;
					if (ops != null) {
						int propStartId = propList[0].getPropId();
						int genPropStartId = generalPropList[0].getPropId();
						for (int i = 0; i < ops.length; ++i) {
							int propId = ops[i].getPropId();
							if (propId < genPropStartId) {
								propDepot[propId-propStartId] = (short)ops[i].getCount();
							}
							else {
								generalDepot[propId-genPropStartId] = (short)ops[i].getCount();
							}
						}
					}
				}
				if (!isReleaseVersion()) {
					if (ops != null) {
						System.out.println("读取道具信息，道具种类: "+ops.length);
						for (int i = 0; i < ops.length; ++i) {
							int propId = ops[i].getPropId();
							if (propId < generalPropList[0].getPropId()) {
								int pid = propId-propList[0].getPropId();
								System.out.println(propList[pid].getName()+": "+propDepot[pid]);
							}
							else {
								int pid = propId-generalPropList[0].getPropId();
								System.out.println(generalPropList[pid].getName()+": "+generalDepot[pid]);
							}
						}
					}
					else {
						System.out.println("读取道具信息，道具种类: 0");
					}
				}
				return sw.getServiceResult();
			}
			catch (Exception e) {
				e.printStackTrace();
				return -2;
			}
		}
		return 0;
	}
	
	public int calcAttainmentId(java.util.Date t) {
		int year = DateUtil.getYear(t);
		int month = DateUtil.getMonth(t);
		return year*100+(month);
	}
	
	public int pushAttainment(int scores, String remark) {
		try {
			/*必须是本月开始的游戏，才会纳入排行*/
			java.util.Date gst = new java.util.Date(gameStartTime);
			boolean needPush = DateUtil.isSameMonth(engineService.getCurrentTime(), gst);
			if (!needPush) {
				return -1;
			}
			
			int attainmentId = calcAttainmentId(gst);
			ServiceWrapper sw = getServiceWrapper();
			GameAttainment ga = sw.readAttainment(attainmentId);
			/**
			 * //TODO something need to do
			 * 由于中兴V1.0的机顶盒游戏时间计算不准，所以去掉按游戏时间排名的规则，
			 * 目前排名规则以每个月最早统一的时间为准
			 */
			if (ga != null && ga.getScores() >= scores) {
				needPush = false;
			}
			if (needPush) {
				GameAttainment attainment = new GameAttainment();
				attainment.setAttainmentId(attainmentId);
				attainment.setScores(scores);
				attainment.setPlayDuration(playDuration);
				attainment.setRemark(remark);
				sw.saveAttainment(attainment);
				return sw.getServiceResult();
			}
			else {
				return -1;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			return -2;
		}
	}

	public void initLoop() {
		if (stateMain == null) {
			stateMain = new StateMain();
		}
		if (stateMap == null) {
			stateMap = new StateMap();
		}
	}

	private int subState = 0;
	public void loop() {
		initLoop();
		switch (state) {
		case STATE_START: 
			//stateStart.handle(keyState);
			break;
		case STATE_MAIN: 
			stateMain.handle(keyState);
			break;
		case STATE_MAP: 
			stateMap.handle(keyState);
			break;
		default: 
			throw new RuntimeException("未知的状态, state="+state);
		}
		
		switch (state) {
		case STATE_START: 
			//stateStart.show(g);
			break;
		case STATE_MAIN: 
			stateMain.show(g);
			break;
		case STATE_MAP: 
			stateMap.show(g);
			break;
		default: 
			throw new RuntimeException("未知的状态, state="+state);
		}
		
		switch (state) {
		case STATE_START: 
			//stateStart.execute();
			if (subState == 0) {
				//gotoLoading();
				loading(0);
				subState = 1;
			}
			else {
				gotoStateMain();
			}
			break;
		case STATE_MAIN: 
			stateMain.execute();
			break;
		case STATE_MAP: 
			stateMap.execute();
			break;
		default: 
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	public void gotoStateMain() {
		stateMain.init();
		state = STATE_MAIN;
	}
	
	public void gotoStateMap() {
		stateMap.init();
		state = STATE_MAP;
	}
	
	public void initNewGame() {
		loadVariableData();
		month = 0;
		playDuration = 0;
		gameStartMillis = System.currentTimeMillis();
		gameStartTime = engineService.getCurrentTime().getTime();
		try {
			pullProp();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
