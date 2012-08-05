package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.ui.ISprite;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupIconText;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.ImageUtil;
import cn.ohyeah.stb.ui.ScrollBar;
import cn.ohyeah.stb.ui.TextBox;
import cn.ohyeah.stb.ui.TextView;
import cn.ohyeah.stb.ui.VerticalListMenu;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.util.Wait;

public class StateMap {
	private static final byte CITY_ATTR_WEALTH = 0;
	private static final byte CITY_ATTR_PROVISIONS = 1;
	private static final byte CITY_ATTR_LOYALTY = 2;
	private static final byte CITY_ATTR_BOX = 3;
	
	private static final byte GENERAL_ATTR_STAMINA = 0;
	private static final byte GENERAL_ATTR_FORCE = 1;
	private static final byte GENERAL_ATTR_WIT = 2;
	private static final byte GENERAL_ATTR_CHARM = 3;
	
	private static final byte GENERAL_ATTR_TITLE = 0;
	private static final byte GENERAL_ATTR_ARMY_ATTACK = 1;
	private static final byte GENERAL_ATTR_ARMY_DEFENSE = 2;
	
	private static final byte STATE_START = 0;
	private static final byte STATE_NORMAL = 1;
	private static final byte STATE_SYSTEM_MENU = 2;
	private static final byte STATE_GAME_MENU = 3;
	private static final byte STATE_CITY_MENU = 4;
	private static final byte STATE_ROUND_END = 5;
	private static final byte STATE_GIFTS = 6;
	
	private static final byte STATE_DEVELOP_MENU = 10;
	private static final byte STATE_DEVELOP_FARMING = 11;		/*农业*/
	private static final byte STATE_DEVELOP_COMMERCE = 12;		/*商业*/
	private static final byte STATE_DEVELOP_RELIEF = 13;		/*救济*/
	
	private static final byte STATE_MILITARY_MENU = 20;
	private static final byte STATE_MILITARY_DRAFT = 21;		/*征兵*/
	private static final byte STATE_MILITARY_EXPEDITION = 22;	/*出征*/
	private static final byte STATE_MILITARY_DISTRIBUTE = 23;	/*分配*/
	private static final byte STATE_EXPEDITION_TARGET = 24;		/*进攻目标*/
	private static final byte STATE_DISTRIBUTE_DETAIL = 25;		/*分配详情*/
	private static final byte STATE_MILITRAY_TRANSFER = 26;		/*输送*/
	private static final byte STATE_TRANSFER_TARGET = 27;		/*输送目标*/
	
	private static final byte STATE_GENERAL_MENU = 30;
	private static final byte STATE_GENERAL_SEARCH = 31;		/*搜索*/
	private static final byte STATE_GENERAL_CONVALESCE = 32;	/*修养*/
	private static final byte STATE_GENERAL_MOVE = 33;			/*调动*/
	private static final byte STATE_GENERAL_AWARD = 34;			/*授予*/
	private static final byte STATE_GENERAL_MOVE_TARGE = 35;	/*移动目标*/
	
	private static final byte STATE_MARKET_MENU = 40;
	private static final byte STATE_MARKET_GRAIN_TRADE = 41;	/*粮食买卖*/
	
	private static final byte GAME_MENU_ENTRY_CITY = 0;			/*进入城池*/
	private static final byte GAME_MENU_PROP_MARKET = 1;		/*道具商城*/
	private static final byte GAME_MENU_GENERAL_MARKET = 2;		/*武将商城*/
	private static final byte GAME_MENU_ROUND_END = 3;			/*回合结束*/
	
	private static final byte CITY_MENU_DEVELOP = 0;			/*开发*/
	private static final byte CITY_MENU_MILITARY_AFFAIRS = 1;	/*军事*/
	private static final byte CITY_MENU_GENERAL = 2;			/*武将*/
	private static final byte CITY_MENU_MARKET = 3;				/*综合市场*/
	
	private static final byte CITY_SUB_MENU_FARMING = 0;		/*农业*/
	private static final byte CITY_SUB_MENU_COMMERCE = 1;		/*商业*/
	private static final byte CITY_SUB_MENU_RELIEF = 2;			/*救济*/
	
	private static final byte CITY_SUB_MENU_DRAFT = 0;			/*征兵*/
	private static final byte CITY_SUB_MENU_DISTRIBUTE = 1;		/*分配*/
	private static final byte CITY_SUB_MENU_EXPEDITION = 2;		/*出征*/
	private static final byte CITY_SUB_MENU_TRANSFER = 3;		/*输送*/
	
	private static final byte CITY_SUB_MENU_SEARCH = 0;			/*搜索*/
	private static final byte CITY_SUB_MENU_CONVALESCE = 1;		/*修养*/
	private static final byte CITY_SUB_MENU_MOVE = 2;			/*调动*/
	private static final byte CITY_SUB_MENU_AWARD = 3;			/*授予*/
	
	private static final byte CITY_SUB_MENU_BUY_GRAIN = 0;		/*买粮*/
	private static final byte CITY_SUB_MENU_SOLD_GRAIN = 1;		/*卖粮*/
	
	private static final byte SYSTEM_MENU_BACK = 0;				/*返回*/
	private static final byte SYSTEM_MENU_SAVE_RECORD = 1;		/*存档*/
	private static final byte SYSTEM_MENU_READ_RECORD = 2;		/*读档*/
	private static final byte SYSTEM_MENU_PROP_MARKET = 3;		/*道具商城*/
	private static final byte SYSTEM_MENU_GENERAL_MARKET = 4;	/*武将市场*/
	private static final byte SYSTEM_MENU_HELP = 5;				/*帮助*/
	private static final byte SYSTEM_MENU_QUIT = 6;				/*退出*/
	
	private static final byte NUM_FLAG_SEQUENCES = 8;
	private static final byte NUM_WAIT_CURSOR_SEQUENCES = 8;
	
	private NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private byte seigneurList[];
	private String bottomText;
	private VerticalListMenu systemMenu;
	private byte waitCursorFrame;
	private byte flagFrame;
	private byte cityMenuIndex;
	private byte citySubMenuIndex;
	private short curCityId;
	private short bakCityId;
	
	private ISprite cityCursor;
	private GeneralListUI generalListUI;
	private ScrollBar scroll;
	
	private short awardGeneralIndex;
	private short awardGeneralCount;
	private short awardGeneralCurPage;
	
	private byte currentTurn;		/*回合结束时步骤*/
	
	private byte curConfirmIndex;			/*当前按钮序号*/
	private byte curGroupIndex;				/*当前选框序号*/
	private byte curCityGroupIndex;			/*当前城池分组编号*/
	private byte curSoldierIndex;	
	
	private byte curCityAttrIndex;	/*当前城池属性(城池介绍页面使用)*/
	private byte curGeneralBaseAttrIndex;
	private byte curGeneralArmyAttrIndex;
	
	private byte slideFrame;	/*滑动帧*/
	private byte slideState;	/*滑动状态*/
	
	private byte showUseProp;
	/*资源绘制帧*/
	private short resourcePaintFrame;
	/*绘制数字时使用的临时空间*/
	
	private short soldierCount[];
	private General distributeGeneral;
	private short distributeSoldierCount;
	
	private short cityProvisions;
	private short cityWealth;
	private byte curGoodsType;
	private byte gameMenuIndex;		/*游戏菜单*/
	private String[] gameMenuStr;	/*游戏菜单字串*/
	private byte state;
	private byte stateStackPos;
	private byte []stateStack = new byte[8];
	private int requiredProvisionsForExpedition;	/*出征消耗的粮草值*/
	
	private TextBox wealthTb;
	private TextBox provisionsTb;
	private UsePropConfirm usePropConfirm = UsePropConfirm.getInstance();
	
	private boolean __DEBUG_WIN;
	private boolean __DEBUG_LOSE;
	private long recordTime;
	private boolean timePass(int millisSeconds) {
		long curTime = System.currentTimeMillis();
		if (recordTime <= 0) {
			recordTime = curTime;
		}
		else {
			if (curTime-recordTime >= millisSeconds) {
				recordTime = 0;
				return true;
			}
		}
		return false;
	}
	
	private void pushState(byte state) {
		stateStack[stateStackPos++] = state;
	}
	
	private byte popState() {
		return stateStack[--stateStackPos];
	}
	
	private byte popStateToNormal() {
		stateStackPos = 0;
		return stateStack[0];
	}
	
	private void bakCityId() {
		bakCityId = curCityId;
	}
	
	private void resumeCityId() {
		curCityId = bakCityId;
	}
	
	private short getBakCityId() {
		return bakCityId;
	}
	
	public void clearMapLargePic() {
		Resource.freeImage(Resource.PIC_ID_MAP_TOP_BG);
		Resource.freeImage(Resource.PIC_ID_MAP_BG);
		Resource.freeImage(Resource.PIC_ID_CITY_INTRO_BG);
		Resource.freeImage(Resource.PIC_ID_MAIN_CITY_BORDER);
		Resource.freeImage(Resource.PIC_ID_CITY_BORDER);
		Resource.freeImage(Resource.PIC_ID_GENERAL_LIST_BG);
		Resource.freeImage(Resource.PIC_ID_AWARD_BG);
		Resource.freeImage(Resource.PIC_ID_TRADE_BG);
		Resource.freeImage(Resource.PIC_ID_TRANSFER_BG);
		Resource.freeImage(Resource.PIC_ID_DISTRIBUTE_BG);
		Resource.freeImage(Resource.PIC_ID_DRAFT_BG);
	}

	public void handle(KeyState key) {
		switch(state) {
		case STATE_START:
			break;
		case STATE_GIFTS:
			break;
		case STATE_NORMAL: 
			handleNormal(key);
			break;
		case STATE_SYSTEM_MENU: 
			handleSystem(key);
			break;
		case STATE_GAME_MENU:
			handleGameMenu(key);
			break;
		case STATE_CITY_MENU: 
			if (showUseProp == 0) {
				handleCity(key);
			}
			else {
				handleCommonUseProp(key);
			}
			break;
		case STATE_ROUND_END:
			break;
		case STATE_DEVELOP_MENU: 
			handleDevelop(key);
			break;
		case STATE_DEVELOP_FARMING: 
			if (showUseProp == 0) {
				handleDevelopFarming(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_DEVELOP_COMMERCE: 
			if (showUseProp == 0) {
				handleDevelopCommerce(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_DEVELOP_RELIEF: 
			if (showUseProp == 0) {
				handleDevelopRelief(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_MILITARY_MENU: 
			handleMilitaryAffairs(key);
			break;
		case STATE_MILITARY_DRAFT: 
			handleMilitaryAffairsDraft(key);
			break;
		case STATE_MILITARY_EXPEDITION: 
			if (showUseProp == 0) {
				handleMilitaryAffairsExpedition(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_MILITARY_DISTRIBUTE: 
			handleMilitaryAffairsDistribute(key);
			break;
		case STATE_EXPEDITION_TARGET:
			handleExpeditionTarget(key);
			break;
		case STATE_DISTRIBUTE_DETAIL:
			handleDistributeDetail(key);
			break;
		case STATE_MILITRAY_TRANSFER:
			handleMilitaryTransfer(key);
			break;
		case STATE_TRANSFER_TARGET:
			handleTransferTarget(key);
			break;
		case STATE_GENERAL_MENU: 
			handleGeneral(key);
			break;
		case STATE_GENERAL_SEARCH: 
			if (showUseProp == 0) {
				handleGeneralSearch(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_GENERAL_CONVALESCE: 
			if (showUseProp == 0) {
				handleGeneralConvalesce(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_GENERAL_MOVE: 
			if (showUseProp == 0) {
				handleGeneralMove(key);
			}
			else {
				handleUsePropFromGeneralList(key);
			}
			break;
		case STATE_GENERAL_AWARD:
			if (showUseProp == 0) {
				handleGeneralAward(key);
			}
			else {
				handleCommonUseProp(key);
			}
			break;
		case STATE_GENERAL_MOVE_TARGE:
			handleGeneralMoveTarge(key);
			break;
		case STATE_MARKET_MENU: 
			handleMarket(key);
			break;
		case STATE_MARKET_GRAIN_TRADE:
			handleGrainTrade(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleTransferTarget(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (Resource.getUpCityId(curCityId) >= 0) {
				curCityId = Resource.getUpCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (Resource.getDownCityId(curCityId) >= 0) {
				curCityId = Resource.getDownCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (Resource.getLeftCityId(curCityId) >= 0) {
				curCityId = Resource.getLeftCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (Resource.getRightCityId(curCityId) >= 0) {
				curCityId = Resource.getRightCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curCityId == bakCityId) {
				PopupText pt = Resource.buildPopupText();
				pt.setText("不能输送到当前城池");
				pt.popup();
			}
			else if (!engine.cityList[curCityId].belongToPlayer()) {
				PopupText pt = Resource.buildPopupText();
				pt.setText("只能输送到己方城池");
				pt.popup();
			}
 			else {
 				City srcCity = engine.cityList[bakCityId];
 				City targetCity = engine.cityList[curCityId];
 				int wealth = wealthTb.getValue();
 				int provisions = provisionsTb.getValue();
 				
 				boolean transfer = true;
 				boolean overflow = false;
 				String dispearText = "";
 				if (wealth+targetCity.getWealth() > City.MAX_WEALTH_VALUE) {
 					overflow = true;
 					dispearText += "金币"+(wealth+targetCity.getWealth()-City.MAX_WEALTH_VALUE);
 				}
 				if (provisions+targetCity.getProvisions() > City.MAX_PROVISIONS_VALUE) {
 					overflow = true;
 					if (dispearText != null) {
 						dispearText += ", ";
 					}
 					dispearText += "粮草"+(provisions+targetCity.getProvisions()-City.MAX_PROVISIONS_VALUE);
 				}
 				if (overflow) {
 					String text = "输送量超过城池容纳上限, 若继续输送, "+dispearText+"将消失, 是否继续?";
 					PopupConfirm pc = Resource.buildPopupConfirm();
 					pc.setText(text);
 					if (pc.popup() == 1) {
 						transfer = false;
 					}
 				}
 				if (transfer) {
	 				PopupText pt = Resource.buildPopupText();
					pt.setText("金币:"+wealth+", 粮草:"+provisions
							+"\n输往"+targetCity.getName());
					pt.popup();
	 				
	 				City.addTransferTask(srcCity.getId(), (short)(wealth), (short)(provisions), targetCity.getId());
	 				srcCity.decWealth(wealth);
	 				srcCity.decProvisions(provisions);
	 				
					resumeCityId();
					Resource.freeImage(Resource.PIC_ID_TRANSFER_BG);
					popState();
					state = popState();
 				}
 			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popState();
			resumeCityId();
		}
	}

	private void handleMilitaryTransfer(KeyState key) {
		if (curGroupIndex == 0) {
			wealthTb.handle(key);
		}
		if (curGroupIndex == 1) {
			provisionsTb.handle(key);
		}
		
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex > 0) {
				if (curGroupIndex == 1) {
					provisionsTb.setDisabled(true);
					wealthTb.setDisabled(false);
				}
				else {
					provisionsTb.setDisabled(false);
				}
				--curGroupIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex < 2) {
				if (curGroupIndex == 0) {
					wealthTb.setDisabled(true);
					provisionsTb.setDisabled(false);
				}
				else {
					provisionsTb.setDisabled(true);
				}
				++curGroupIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex == 2) {
				if (curConfirmIndex == 0) {
					curConfirmIndex = 1;
				}
				else {
					curConfirmIndex = 0;
				}
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex == 2) {
				if (curConfirmIndex == 0) {
					curConfirmIndex = 1;
				}
				else {
					curConfirmIndex = 0;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			Resource.freeImage(Resource.PIC_ID_TRANSFER_BG);
			state = popState();
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curGroupIndex == 2) {
				if (curConfirmIndex == 0) {
					if (City.getLeftTransferTaskCount() > 0) {
						if (wealthTb.getValue()==0&&provisionsTb.getValue()==0) {
							PopupText pt = Resource.buildPopupText();
							pt.setText("没有指定输送数量");
							pt.popup();
						}
						else {
							bakCityId();
							pushState(state);
							state = STATE_TRANSFER_TARGET;
						}
					}
					else {
						PopupText pt = Resource.buildPopupText();
						pt.setText("已达到输送次数上限");
						pt.popup();
					}
				}
				else {
					Resource.freeImage(Resource.PIC_ID_TRANSFER_BG);
					state = popState();
				}
			}
		}
	}

	private void handleGrainTrade(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex > 0) {
				--curGroupIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex < 2) {
				++curGroupIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex == 0) {
				citySubMenuIndex = (byte)((citySubMenuIndex+1)%2);
				cityProvisions = engine.cityList[curCityId].getProvisions();
				cityWealth = engine.cityList[curCityId].getWealth();
			}
			else if (curGroupIndex == 1) {
				curGoodsType = (byte)((curGoodsType+3-1)%3);
			}
			else {
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex == 0) {
				citySubMenuIndex = (byte)((citySubMenuIndex+1)%2);
				cityProvisions = engine.cityList[curCityId].getProvisions();
				cityWealth = engine.cityList[curCityId].getWealth();
			}
			else if (curGroupIndex == 1) {
				curGoodsType = (byte)((curGoodsType+1)%3);
			}
			else {
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			PopupText pt = null;
			String tipText = "";
			if (curGroupIndex == 1) {	//光标聚焦在粮食选择
				if (citySubMenuIndex == CITY_SUB_MENU_BUY_GRAIN) {	//买粮
					if (cityProvisions < City.MAX_PROVISIONS_VALUE) {
						int needWealth = 0;
						int incProvisions = 0;
						if (curGoodsType == 0) {	//每次操作100粮食
							needWealth = 125;
							incProvisions = 100;
						}
						else if (curGoodsType == 1) {	//每次操作500粮食
							needWealth = 125*5;
							incProvisions = 500;
						}
						else if (curGoodsType == 2){	//每次操作1000粮食
							needWealth = 125*10;
							incProvisions = 1000;
						}
						else {
							//null
						}
						
						if (needWealth < cityWealth) {
							cityWealth -= needWealth;
							cityProvisions += incProvisions;
							if (cityProvisions > City.MAX_PROVISIONS_VALUE) {
								cityProvisions = City.MAX_PROVISIONS_VALUE;
							}
						}
						else {
							pt = Resource.buildPopupText();
							pt.setText("城池金币不足");
							pt.popup();
						}
					}
					else {
						pt = Resource.buildPopupText();
						pt.setText("城池粮食达到上限");
						pt.popup();
					}
				}
				else if (citySubMenuIndex == CITY_SUB_MENU_SOLD_GRAIN){	//卖粮
					if (cityWealth < City.MAX_WEALTH_VALUE) {
						int needProvisions = 0;
						int incWealth = 0;
						if (curGoodsType == 0) {	//每次操作100粮食
							needProvisions = 100;
							incWealth = 80;
						}
						else if (curGoodsType == 1) {	//每次操作500粮食
							needProvisions = 100*5;
							incWealth = 80*5;
						}
						else if (curGoodsType == 2){	//每次操作1000粮食
							needProvisions = 100*10;
							incWealth = 80*10;
						}
						else {
							//null
						}
						if (needProvisions < cityProvisions) {
							cityProvisions -= needProvisions;
							cityWealth += incWealth;
							if (cityWealth > City.MAX_WEALTH_VALUE) {
								cityWealth = City.MAX_WEALTH_VALUE;
							}
						}
						else {
							pt = Resource.buildPopupText();
							pt.setText("城池粮食不足");
							pt.popup();
						}
					}
					else {
						pt = Resource.buildPopupText();
						pt.setText("城池金币达到上限");
						pt.popup();
					}
				}
				else {
					//null
				}
			}
			else if (curGroupIndex == 2) {	//光标聚焦在按钮
				if (curConfirmIndex == 0) {	//确定
					engine.cityList[curCityId].setWealth(cityWealth);
					engine.cityList[curCityId].setProvisions(cityProvisions);
					if (citySubMenuIndex == CITY_SUB_MENU_BUY_GRAIN) {	//买粮
						tipText = "买粮完成\n";
					}
					else {
						tipText = "卖粮完成\n";
					}
					tipText += "城池粮食变为"+cityProvisions+"\n城池金币变为"+cityWealth;
					pt = Resource.buildPopupText();
					pt.setText(tipText);
					pt.popup();
					state = popState();
				}
				else {	//返回
					state = popState();
				}
				Resource.freeImage(Resource.PIC_ID_TRADE_BG);
			}
			else {
				//null
			}
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			Resource.freeImage(Resource.PIC_ID_TRADE_BG);
			state = popState();
		}
		
	}

	private void handleGameMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			gameMenuIndex = (byte)((gameMenuIndex+gameMenuStr.length-1)%gameMenuStr.length);
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			gameMenuIndex = (byte)((gameMenuIndex+1)%gameMenuStr.length);
		}
		
		if (key.containsAndRemove(KeyCode.LEFT|KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popState();
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			StateMarket stateMarket = null;
			switch(gameMenuIndex) {
			case GAME_MENU_ENTRY_CITY: 
				if (engine.cityList[curCityId].belongToPlayer()) {
					pushState(state);
					state = STATE_CITY_MENU;
					cityMenuIndex = 0;
					curCityAttrIndex = 0;
					curGroupIndex = 0;
				}
				else {
					curCityId = engine.getPlayerSeigneurCityId();
					state = popState();
				}
				break;
			case GAME_MENU_PROP_MARKET: 
				clear();
				stateMarket = new StateMarket(StateMarket.TYPE_PROP_MARKET);
				stateMarket.popup();
				state = popStateToNormal();
				break;
			case GAME_MENU_GENERAL_MARKET: 
				clear();
				stateMarket = new StateMarket(StateMarket.TYPE_GENERAL_MARKET);
				if (engine.cityList[curCityId].belongToPlayer()) {
					stateMarket.setTargetCity(engine.cityList[curCityId]);
				}
				stateMarket.popup();
				state = popStateToNormal();
				break;
			case GAME_MENU_ROUND_END: 
				PopupConfirm pc = Resource.buildPopupConfirm();
				pc.setText("确定结束本回合吗？");
				if (pc.popup() == 0) {
					resourcePaintFrame = 0;
					currentTurn = -1;
					state = STATE_ROUND_END;
				}
				break;
			default: break;
			}
		}
	}

	private void handleDistributeDetail(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex > 0) {
				--curGroupIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex < 2) {
				++curGroupIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex == 0) {	//兵种切换
				soldierCount[curSoldierIndex] += distributeSoldierCount;
				curSoldierIndex = (byte)((curSoldierIndex+soldierCount.length-1)%soldierCount.length);
				distributeSoldierCount = 0;
			}
			else if (curGroupIndex == 1){	//调整兵力
				if (distributeSoldierCount > 0) {
					if (distributeSoldierCount > 100) {
						soldierCount[curSoldierIndex] += 100;
						distributeSoldierCount -= 100;
					}
					else {
						soldierCount[curSoldierIndex] += distributeSoldierCount;
						distributeSoldierCount = 0;
					}
				}
			}
			else {
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex == 0) {	//兵种切换
				soldierCount[curSoldierIndex] += distributeSoldierCount;
				curSoldierIndex = (byte)((curSoldierIndex+1)%soldierCount.length);
				distributeSoldierCount = 0;
			}
			else if (curGroupIndex == 1){	//调整兵力
				if (soldierCount[curSoldierIndex] > 0) {
					int disCount = distributeGeneral.getLead()-distributeSoldierCount;
					if (disCount > 100) {
						disCount = 100;
					}
					if (soldierCount[curSoldierIndex] > disCount) {
						distributeSoldierCount += disCount;
						soldierCount[curSoldierIndex] -= disCount;
					}
					else {
						distributeSoldierCount += soldierCount[curSoldierIndex];
						soldierCount[curSoldierIndex] = 0;
					}
				}
			}
			else {
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curGroupIndex == 2) {
				if (curConfirmIndex == 0) {
					City curCity = engine.cityList[curCityId];
					boolean needConfirm = false;
					String tipText = "";
					for (int i = 0; i < soldierCount.length; ++i) {
						if (soldierCount[i] > City.MAX_FREE_SOLDIER_COUNT) {
							needConfirm = true;
							tipText = engine.soldierList[i].getName()+soldierCount[i]
							+"超出城市中最大容纳的数量"+City.MAX_FREE_SOLDIER_COUNT+"，多余的士兵将消失，是否继续？";
							break;
						}
					}
					if (needConfirm) {	//需要确认
						key.clear();
						PopupConfirm pc = Resource.buildPopupConfirm();
						pc.setText(tipText);
						if (pc.popup() == 0) {	//点击确定，继续
							distributeGeneral.setSoldierId(curSoldierIndex);
							distributeGeneral.setSoldierCount(distributeSoldierCount);
							for (int i = 0; i < soldierCount.length; ++i) {
								curCity.setFreeSoldierCount((short)i, soldierCount[i]);
							}
							updateGeneralListAttribute();
							clearDistributeResource();
							state = popState();
						}
					}
					else {	//不需要确认
						distributeGeneral.setSoldierId(curSoldierIndex);
						distributeGeneral.setSoldierCount(distributeSoldierCount);
						for (int i = 0; i < soldierCount.length; ++i) {
							curCity.setFreeSoldierCount((short)i, soldierCount[i]);
						}
						updateGeneralListAttribute();
						clearDistributeResource();
						state = popState();
					}
				}
				else {
					clearDistributeResource();
					state = popState();
				}
			}
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			clearDistributeResource();
			state = popState();
		}
	}

	private void updateGeneralListAttribute() {
		generalListUI.updateCurPageGeneralAttribute();
	}

	private void clearDistributeResource() {
		Resource.freeImage(Resource.PIC_ID_DISTRIBUTE_BG);
		soldierCount = null;
	}

	private void handleGeneralMoveTarge(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (Resource.getUpCityId(curCityId) >= 0) {
				curCityId = Resource.getUpCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (Resource.getDownCityId(curCityId) >= 0) {
				curCityId = Resource.getDownCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (Resource.getLeftCityId(curCityId) >= 0) {
				curCityId = Resource.getLeftCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (Resource.getRightCityId(curCityId) >= 0) {
				curCityId = Resource.getRightCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			String tipText = "";
			if (curCityId == getBakCityId()) {
				tipText = "武将已经在该城池中";
			}
			else {
				City targetCity = engine.cityList[curCityId];
				if (targetCity.belongToPlayer()) {
					popState();
					state = popState();
					resumeCityId();
					General general;
					short[] list = generalListUI.getSelectedList();
					int disCount = list.length;
					if (disCount > 3) disCount = 3;
					for (int i = 0; i < list.length; ++i) {
						general = engine.generalList[list[i]];
						general.assignTaskMove();
						general.setCity(targetCity);
						if (disCount > 0) {
							tipText += general.getName();
							if (disCount > 1) {
								tipText += "、";
							}
							else {
								if (list.length > 3) {
									tipText += "等";
								}
							}
							--disCount;
						}
					}
					tipText += "前往"+targetCity.getName();
					generalListUI.clearResource();
					generalListUI = null;
				}
				else {
					tipText = "城池"+engine.cityList[curCityId].getName()+"不属于我方势力";
				}
			}

			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
			
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popState();
			resumeCityId();
		}
	}
	
	private void handleDevelopCommerce(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getCommercePermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					clearMapLargePic();
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			String tipText = "";
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				state = popState();
				City srcCity = engine.cityList[curCityId];
				General general;
				short[] list = generalListUI.getSelectedList();
				int disCount = list.length;
				if (disCount > 3) disCount = 3;
				for (int i = 0; i < list.length; ++i) {
					general = engine.generalList[list[i]];
					general.assignTaskCommerce();
					srcCity.addTaskFlag(City.ASSIGN_TASK_COMMERCE);
					if (disCount > 0) {
						tipText +=general.getName();
						if (disCount > 1) {
							tipText += "、";
						}
						else {
							if (list.length > 3) {
								tipText += "等";
							}
						}
						--disCount;
					}
				}
				tipText += "执行商业开发";
				generalListUI.clearResource();
				generalListUI = null;
			}
			else {
				tipText = "未选择任何武将";
			}
			
			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
		
	}

	private void handleDevelopRelief(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getReliefPermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					clearMapLargePic();
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			City srcCity = engine.cityList[curCityId];
			String tipText = "";
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				state = popState();
				General general;
				short[] list = generalListUI.getSelectedList();
				int disCount = list.length;
				if (disCount > 3) disCount = 3;
				for (int i = 0; i < list.length; ++i) {
					general = engine.generalList[list[i]];
					general.assignTaskRelief();
					srcCity.addTaskFlag(City.ASSIGN_TASK_RELIEF);
					if (disCount > 0) {
						tipText += general.getName();
						if (disCount > 1) {
							tipText += "、";
						}
						else {
							if (list.length > 3) {
								tipText += "等";
							}
						}
						--disCount;
					}
				}
				tipText += "执行救济";
				generalListUI.clearResource();
				generalListUI = null;
			}
			else {
				tipText = "未选择任何武将";
			}
			
			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
		
	}

	private void handleGeneralAward(KeyState key) {
		if (engine.isDebugMode()) {
			General g1 = engine.generalList[engine.getCityGeneralIdByOrder(curCityId, awardGeneralCurPage+awardGeneralIndex)];
			/*加武力*/
			if (key.contains(KeyCode.NUM1)) {
				g1.incForce((short)(10));
			}
			/*加智力*/
			if (key.contains(KeyCode.NUM2)) {
				g1.incWit((short)(10));
			}
			/*加魅力*/
			if (key.contains(KeyCode.NUM3)) {
				g1.incCharm((short)(10));
			}
			/*加将技*/
			if (key.contains(KeyCode.NUM4)) {
				g1.incMagicPower((short)(10));
			}
			/*体加满*/
			if (key.contains(KeyCode.NUM5)) {
				g1.setStamina(Rule.INITIAL_STAMINA);
			}
			/*兵加满*/
			if (key.contains(KeyCode.NUM6)) {
				g1.setSoldierCount(g1.getLead());
			}
			
		}
		
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex == 0) {
				if (awardGeneralCurPage+awardGeneralIndex > 0) {
					if (awardGeneralIndex > 0) {
						--awardGeneralIndex;
					}
					else {
						--awardGeneralCurPage;
						scroll.setCurPage(awardGeneralCurPage);
					}
				}
			}
			else if (curGroupIndex == 1){
				if (curGeneralBaseAttrIndex > 0) {
					--curGeneralBaseAttrIndex;
				}
			}
			else if (curGroupIndex == 2) {
				if (curGeneralArmyAttrIndex > 0) {
					--curGeneralArmyAttrIndex;
				}
			}
			else {
				curGroupIndex = 2;
				curGeneralArmyAttrIndex = 2;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex == 0) {
				if (awardGeneralCurPage+awardGeneralIndex < awardGeneralCount-1) {
					if (awardGeneralIndex < Resource.NUM_AWARD_GENERAL_LIST_ITEMS-1) {
						++awardGeneralIndex;
					}
					else {
						++awardGeneralCurPage;
						scroll.setCurPage(awardGeneralCurPage);
					}
				}
			}
			else if (curGroupIndex == 1) {
				if (curGeneralBaseAttrIndex < 3) {
					++curGeneralBaseAttrIndex;
				}
				else {
					curGroupIndex = 3;
				}
			}
			else if (curGroupIndex == 2) {
				if (curGeneralArmyAttrIndex < 2) {
					++curGeneralArmyAttrIndex;
				}
				else {
					curGroupIndex = 3;
				}
			}
			else {
				//null
			}
				
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex == 1) {
				--curGroupIndex;
			}
			else if (curGroupIndex == 2) {
				--curGroupIndex;
				curGeneralBaseAttrIndex = (byte)(curGeneralArmyAttrIndex+1);
			}
			else {
				//null
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex == 0) {
				++curGroupIndex;
			}
			else if (curGroupIndex == 1) {
				++curGroupIndex;
				if (curGeneralBaseAttrIndex > 0) {
					curGeneralArmyAttrIndex = (byte)(curGeneralBaseAttrIndex-1);
				}
				else {
					curGeneralArmyAttrIndex = 0;
				}
			}
			else {
				//null
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {	//使用道具
			if (curGroupIndex == 0) {
				curGroupIndex = 3;
			}
			else if (curGroupIndex == 1){
				General general = engine.generalList[engine.getCityGeneralIdByOrder(curCityId, awardGeneralCurPage+awardGeneralIndex)];
				prepareGeneralBaseRaiseRes(engine.cityList[curCityId], general, curGeneralBaseAttrIndex);
				showUseProp = 1;
				clearMapLargePic();
			}
			else if (curGroupIndex == 2){
				General general = engine.generalList[engine.getCityGeneralIdByOrder(curCityId, awardGeneralCurPage+awardGeneralIndex)];
				prepareGeneralArmyRaiseRes(engine.cityList[curCityId], general, curGeneralArmyAttrIndex);
				showUseProp = 1;
				clearMapLargePic();
			}
			else {
				state = popState();
				Resource.freeImage(Resource.PIC_ID_AWARD_BG);
				Resource.freeImage(Resource.PIC_ID_AWARD_PROP_LIST_ARROW);
				scroll = null;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popState();
			Resource.freeImage(Resource.PIC_ID_AWARD_BG);
			Resource.freeImage(Resource.PIC_ID_AWARD_PROP_LIST_ARROW);
			scroll = null;
		}
	}

	private void prepareGeneralArmyRaiseRes(City city, General general, int type) {
		usePropConfirm.setCity(city);
		usePropConfirm.setGeneral(general);
		switch (type) {
		case GENERAL_ATTR_TITLE:
			usePropConfirm.addProp(engine.propList[15]);
			usePropConfirm.addProp(engine.propList[16]);
			usePropConfirm.addProp(engine.propList[17]);
			break;
		case GENERAL_ATTR_ARMY_ATTACK:
			usePropConfirm.addProp(engine.propList[13]);
			break;
		case GENERAL_ATTR_ARMY_DEFENSE:
			usePropConfirm.addProp(engine.propList[14]);
			break;
		default:
			break;
		}
	}

	private void prepareGeneralBaseRaiseRes(City city, General general, int type) {
		usePropConfirm.setCity(city);
		usePropConfirm.setGeneral(general);
		switch (type) {
		case GENERAL_ATTR_STAMINA:
			usePropConfirm.addProp(engine.propList[7]);
			usePropConfirm.addProp(engine.propList[8]);
			break;
		case GENERAL_ATTR_FORCE:
			usePropConfirm.addProp(engine.propList[9]);
			usePropConfirm.addProp(engine.propList[10]);
			break;
		case GENERAL_ATTR_WIT:
			usePropConfirm.addProp(engine.propList[9]);
			usePropConfirm.addProp(engine.propList[11]);
			break;
		case GENERAL_ATTR_CHARM:
			usePropConfirm.addProp(engine.propList[9]);
			usePropConfirm.addProp(engine.propList[12]);
			break;
		default:
			break;
		}
	}

	private void handleExpeditionTarget(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (Resource.getUpCityId(curCityId) >= 0) {
				curCityId = Resource.getUpCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (Resource.getDownCityId(curCityId) >= 0) {
				curCityId = Resource.getDownCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (Resource.getLeftCityId(curCityId) >= 0) {
				curCityId = Resource.getLeftCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (Resource.getRightCityId(curCityId) >= 0) {
				curCityId = Resource.getRightCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (engine.cityList[curCityId].belongToPlayer()) {
				PopupText pt = Resource.buildPopupText();
				pt.setText("不能攻击己方城池");
				pt.popup();
			}
			else if (Resource.isConnected(getBakCityId(), curCityId)) {
				City src = engine.cityList[getBakCityId()];
				City target = engine.cityList[curCityId];
				short[] attackList = generalListUI.getSelectedList();
				attackOtherCity(src, attackList, target);
			}
			else {
				PopupText pt = Resource.buildPopupText();
				pt.setText("目标城池不可到达");
				pt.popup();
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popState();
			resumeCityId();
		}
	}

	private void attackOtherCity(City src, short[] attackList, City target) {
		Seigneur targetSeigneur = target.getSeigneur();
		Seigneur srcSeigneur = src.getSeigneur();
		
		PopupText pt = Resource.buildPopupText();
		if (targetSeigneur == null) {
			pt.setText("我军进攻"+target.getName());
		}
		else {
			pt.setText("我军进攻"+targetSeigneur.getName()+"军"+target.getName());
		}
		pt.popup();
		
		src.decLoyalty(Rule.LOYALTY_CONSUME_FOR_EXPEDITION);
		src.decProvisions(requiredProvisionsForExpedition);
		for (int i = 0; i < attackList.length; ++i) {
			engine.generalList[attackList[i]].decStamina(Rule.STAMINA_CONSUME_FOR_EXPEDITION);
			engine.generalList[attackList[i]].assignTaskExpedition();
		}
		if (target.belongToAnySeigneur()) {	//目标不为空城
			clear();
			state = popStateToNormal();
			short[] defenseList = engine.getCityAllGeneralList(curCityId);
			/*如果武将士兵数为0,则+10个士兵*/
			for (int i = 0; i < defenseList.length; ++i) {
				General g1 = engine.generalList[defenseList[i]];
				if (g1.getSoldierCount() <= 0) {
					g1.incSoldierCount((short)10);
				}
			}
			StateBattle battle = new StateBattle();
			battle.setAttackList(attackList);
			battle.setDefenseList(defenseList);
			battle.setAttackedByPlayer(true);
			
			/*是否灭国*/
			int ncid = 0;
			String tipText = "";
			int battleResult = battle.battle();
			if (battleResult == 0) {
				ncid = Rule.cityDefeated(target);
				Rule.captureEmptyCity(attackList, target);
				tipText = "战争结束\n我军胜利, 进驻"+target.getName();
			}
			else {
				tipText = "战争结束\n我军战败, 从"+target.getName()+"退回"+src.getName();
			}
			engine.getGraphics().setColor(0);
			engine.getGraphics().fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
			pt.setText(tipText);
			pt.popup();
			
			if (battleResult == 0) {
				/*新占领的城池，增加状态*/
				target.setFarmingEnhance(src.getFarmingEnhance());
				target.setCommerceEnhance(src.getCommerceEnhance());
				target.setAttackEnhance(src.getAttackEnhance());
				target.setDefenseEnhance(src.getDefenseEnhance());
				
				PopupIconText pit = Resource.buildPopupIconText();
				pit.setIcon(Resource.loadImage(engine.propList[18].getIcon()));
				pit.setIconDescText("X 3");
				pit.setText("攻城胜利，系统奖励3个宝箱");
				pit.popup();
				Resource.clearPopupIconText();
				engine.addPrizeBox(3);
				
				if (ncid < 0) {
					pt.setText(targetSeigneur.getName()+"军灭亡了");
					pt.popup();
					
					General targetGeneral = engine.generalList[targetSeigneur.getGeneralId()];
					targetGeneral.setIdentityNormal();
					targetGeneral.setSeigneur(srcSeigneur);
					targetGeneral.assignTaskExpedition();
					
					if (defenseList[0] == targetSeigneur.getGeneralId()) {
						targetGeneral = engine.generalList[defenseList[1]];
					}
					else {
						targetGeneral = engine.generalList[defenseList[0]];
					}
					targetGeneral.setIdentityNormal();
					targetGeneral.setSeigneur(srcSeigneur);
					targetGeneral.assignTaskExpedition();
					
					tipText = "我军俘虏敌方君主"+targetSeigneur.getName()+", 我军俘虏敌方武将"+targetGeneral.getName();
					pt.setText(tipText);
					pt.popup();
				}
			}
		}
		else {	//目标为空城
			state = popStateToNormal();
			
			Rule.captureEmptyCity(attackList, target);
			
			engine.getGraphics().setColor(0);
			engine.getGraphics().fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
			pt.setText("我军成功占领"+target.getName());
			pt.popup();
			
			/*新占领的城池，增加状态*/
			target.setFarmingEnhance(src.getFarmingEnhance());
			target.setCommerceEnhance(src.getCommerceEnhance());
			target.setAttackEnhance(src.getAttackEnhance());
			target.setDefenseEnhance(src.getDefenseEnhance());
			
			PopupIconText pit = Resource.buildPopupIconText();
			pit.setIcon(Resource.loadImage(engine.propList[18].getIcon()));
			pit.setIconDescText("X 1");
			pit.setText("攻城胜利，系统奖励1个宝箱");
			pit.popup();
			Resource.clearPopupIconText();
			engine.addPrizeBox(1);
		}
		resumeCityId();                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
	}

	private void handleGeneralMove(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getMovePermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
					clearMapLargePic();
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				bakCityId();
				pushState(state);
				state = STATE_GENERAL_MOVE_TARGE;
			}
			else {
				String tipText = "未选择任何武将";
				PopupText pt = Resource.buildPopupText();
				pt.setText(tipText);
				pt.popup();
			}
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
	}

	private void handleGeneralConvalesce(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general  = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getConvalescePermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
					clearMapLargePic();
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			String tipText = "";
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				state = popState();
				General general;
				short[] list = generalListUI.getSelectedList();
				int disCount = list.length;
				if (disCount > 3) disCount = 3;
				for (int i = 0; i < list.length; ++i) {
					general = engine.generalList[list[i]];
					general.assignTaskConvalesce();
					if (disCount > 0) {
						tipText += general.getName();
						if (disCount > 1) {
							tipText += "、";
						}
						else {
							if (list.length > 3) {
								tipText += "等";
							}
						}
						--disCount;
					}
				}
				tipText += "进行修养";
				generalListUI.clearResource();
				generalListUI = null;
			}
			else {
				tipText = "未选择任何武将";
			}
			
			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
	}

	private void handleGeneralSearch(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getSearchPermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
					clearMapLargePic();
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			City srcCity = engine.cityList[curCityId];
			String tipText = "";
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				state = popState();
				General general;
				short[] list = generalListUI.getSelectedList();
				int disCount = list.length;
				if (disCount > 3) disCount = 3;
				for (int i = 0; i < list.length; ++i) {
					general = engine.generalList[list[i]];
					general.assignTaskSearch();
					srcCity.addTaskFlag(City.ASSIGN_TASK_SEARCH);
					if (disCount > 0) {
						tipText += general.getName();
						if (disCount > 1) {
							tipText += "、";
						}
						else {
							if (list.length > 3) {
								tipText += "等";
							}
						}
						--disCount;
					}
				}
				tipText += "在城中执行搜索。";
				generalListUI.clearResource();
				generalListUI = null;
			}
			else {
				tipText = "未选择任何武将";
			}
			
			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
	}

	private void handleMilitaryAffairsDistribute(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem() || generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			
			Permission permission = Rule.getDistributePermission(generalListUI.getIndicateGeneral());
			if (permission.hasPermission()) {
				distributeGeneral = generalListUI.getIndicateGeneral();
				curGroupIndex = 0;
				curSoldierIndex = (byte)distributeGeneral.getSoldierId();
				distributeSoldierCount = distributeGeneral.getSoldierCount();
				City curCity = engine.cityList[curCityId];
				soldierCount = new short[engine.soldierList.length];
				for (int i = 0; i < soldierCount.length; ++i) {
					soldierCount[i] = curCity.getFreeSoldierCount((short)i);
				}
				pushState(state);
				state = STATE_DISTRIBUTE_DETAIL;
			}
			else {
				if (permission.needTip()) {
					PopupText pt = Resource.buildPopupText();
					pt.setText(permission.getMessage());
					pt.popup();
				}
			}
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
			
		}
	}

	private void handleMilitaryAffairsExpedition(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getExpeditionPermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
					clearMapLargePic();
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			short[] genList = generalListUI.getSelectedList();
			if (genList!=null && genList.length> 0) {
				Permission permission = Rule.getExpeditionPermission(engine.cityList[curCityId], genList);
				requiredProvisionsForExpedition = permission.getValue();
				if (permission.hasPermission()) {
					bakCityId();
					pushState(state);
					state = STATE_EXPEDITION_TARGET;
				}
				else {
					if (showPermissionTip(permission) == 0) {
						showUseProp = 1;
						prepareUsePropRes(permission, engine.cityList[curCityId], null);
						clearMapLargePic();
					}
				}
			}
			else {
				String tipText = "未选择任何武将";
				PopupText pt = Resource.buildPopupText();
				pt.setText(tipText);
				pt.popup();
			}
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
	}

	private void handleMilitaryAffairsDraft(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex > 0) {
				--curGroupIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex < soldierCount.length-1+1) {
				++curGroupIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex >= soldierCount.length) {	//光标在确认按钮
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
			else {
				if (soldierCount[curGroupIndex] > 0) {
					soldierCount[curGroupIndex] -= 100;
					if (soldierCount[curGroupIndex] < 0) {
						soldierCount[curGroupIndex] = 0;
					}
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex >= soldierCount.length) {
				curConfirmIndex = (byte)((curConfirmIndex+1)%2);
			}
			else {
				City curCity = engine.cityList[curCityId];
				Soldier curSoldier = engine.soldierList[curGroupIndex];
				/*城市预备兵未满，可以继续征兵*/
				if (soldierCount[curGroupIndex]+ curCity.getFreeSoldierCount(curGroupIndex) < City.MAX_FREE_SOLDIER_COUNT) {
					int consume = 0;
					for (int i = 0; i < soldierCount.length; ++i) {
						consume += ((soldierCount[i]+100-1)/100)*engine.soldierList[i].getAmount();
					}
					int leftWealth = curCity.getWealth()-consume;
					if (leftWealth >= curSoldier.getAmount()) {
						soldierCount[curGroupIndex] += 100;
						if (soldierCount[curGroupIndex]+ curCity.getFreeSoldierCount(curGroupIndex) > City.MAX_FREE_SOLDIER_COUNT) {
							soldierCount[curGroupIndex] = (short)(City.MAX_FREE_SOLDIER_COUNT-curCity.getFreeSoldierCount(curGroupIndex));
						}
					}/*
					else {	//添加金币不足的提示
						PopupText pt = Resource.buildPopupText();
						pt.setText("城池中的金币不足，无法继续征兵");
						pt.popup();
						Resource.clearPopupText();
					}*/
				}
				/*
				else {	//预备兵达到上限时的提示
					PopupText pt = Resource.buildPopupText();
					pt.setText("城池中的"+curSoldier.getName()+"已达到上限，无法继续征募"+curSoldier.getName());
					pt.popup();
					Resource.clearPopupText();
				}*/
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curGroupIndex >= soldierCount.length) {	//光标在确认按钮
				if (curConfirmIndex == 0) {	//确定
					if (isDraftAnySoldier()) {
						City curCity = engine.cityList[curCityId];
						String tipText = "征兵结束!\n城池";
						int consume = 0;
						for (int i = 0; i < soldierCount.length; ++i) {
							if (soldierCount[i] > 0) {
								tipText += engine.soldierList[i].getName()+"增加"+soldierCount[i]+". ";
								curCity.incFreeSoldierCount((short)i, soldierCount[i]);
								consume += ((soldierCount[i]+100-1)/100)*engine.soldierList[i].getAmount();
							}
						}
						curCity.decWealth(consume);
						/*
						 * 显示文字出框，去掉金币和民忠减少的文字描述
						 * 
						tipText += "\n城池金币减少"+consume;
						curCity.decLoyalty(Rule.LOYALTY_CONSUME_FOR_DRAFT);
						tipText += ". 民忠减少"+Rule.LOYALTY_CONSUME_FOR_DRAFT;
						*/
						PopupText pt = Resource.buildPopupText();
						pt.setText(tipText);
						pt.popup();
					}
				}
				clearDraftResource();
				state = popState();
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			clearDraftResource();
			state = popState();
		}
	}
	
	private boolean isDraftAnySoldier() {
		boolean isDraft = false;
		for (int i = 0; i < soldierCount.length; ++i) {
			if (soldierCount[i] > 0) {
				isDraft = true;
			}
		}
		return isDraft;
	}

	private void clearDraftResource() {
		Resource.freeImage(Resource.PIC_ID_DRAFT_BG);
		soldierCount = null;
	}
	
	private void showGeneralListUI(Graphics g) {
		generalListUI.show(g);
	}

	private void handleGeneralListUI(KeyState key) {
		generalListUI.handle(key);
	}
	
	private void handleDevelopFarming(KeyState key) {
		handleGeneralListUI(key);
		
		if (generalListUI.isConfirmItem()) {
			generalListUI.resetConfirmFlag();
			General general = generalListUI.getIndicateGeneral();
			Permission permission = Rule.getFarmingPermission(general);
			if (permission.hasPermission()) {
				generalListUI.selecteItem();
			}
			else {
				if (showPermissionTip(permission) == 0) {
					showUseProp = 1;
					prepareUsePropRes(permission, engine.cityList[curCityId], general);
					clearMapLargePic();
				}
			}
		}
		else if (generalListUI.isConfirmOk()) {
			generalListUI.resetConfirmFlag();
			City srcCity = engine.cityList[curCityId];
			String tipText = "";
			int count = generalListUI.getSelectedCount();
			if (count > 0) {
				state = popState();
				General general;
				short[] list = generalListUI.getSelectedList();
				int disCount = list.length;
				if (disCount > 3) disCount = 3;
				for (int i = 0; i < list.length; ++i) {
					general = engine.generalList[list[i]];
					general.assignTaskFarming();
					srcCity.addTaskFlag(City.ASSIGN_TASK_FARMING);
					if (disCount > 0) {
						tipText += general.getName();
						if (disCount > 1) {
							tipText += "、";
						}
						else {
							if (list.length > 3) {
								tipText += "等";
							}
						}
						--disCount;
					}
				}
				tipText += "执行农业开发";
				generalListUI.clearResource();
				generalListUI = null;
			}
			else {
				tipText = "未选择任何武将";
			}
			PopupText pt = Resource.buildPopupText();
			pt.setText(tipText);
			pt.popup();
		}
		else if (generalListUI.isConfirmBack()) {
			generalListUI.resetConfirmFlag();
			generalListUI.clearResource();
			generalListUI = null;
			state = popState();
		}
	}

	private void handleMarket(KeyState key) {
		handleCitySubMenu(key);
		handleMarketSubMenu(key);
	}

	private void handleMarketSubMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.OK)) {
			showUseProp = 0;
			int genCount = engine.getCityAllGeneralCount(curCityId);
			switch(citySubMenuIndex) {
			case CITY_SUB_MENU_BUY_GRAIN: 
			case CITY_SUB_MENU_SOLD_GRAIN: 
				if (genCount > 0) {
					pushState(state);
					//Resource.freeImage(Resource.PIC_ID_MAP_BG);
					//Resource.freeImage(Resource.PIC_ID_CITY_MENU_BG);
					//Resource.freeImage(Resource.PIC_ID_CITY_INTRO_BG);
					//Resource.freeImage(Resource.PIC_ID_BOTTOM_INFO_BG);
					state = STATE_MARKET_GRAIN_TRADE;
					curGroupIndex = 1;
					curConfirmIndex = 0;
					curGoodsType = 0;
					cityProvisions = engine.cityList[curCityId].getProvisions();
					cityWealth = engine.cityList[curCityId].getWealth();
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行粮食买卖");
					pt.popup();
				}
				break;
			default: throw new RuntimeException("错误的城市子菜单号，cityMenuIndex="+cityMenuIndex+", citySubMenuIndex"+citySubMenuIndex);
			}
		}
	}

	private void handleGeneral(KeyState key) {
		handleCitySubMenu(key);
		handleGeneralSubMenu(key);
	}

	private void handleGeneralSubMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.OK)) {
			showUseProp = 0;
			short[] genList = null;
			switch(citySubMenuIndex) {
			case CITY_SUB_MENU_SEARCH: 
				genList = engine.getCityAllGeneralList(curCityId);
				if (genList != null) {
					pushState(state);
					state = STATE_GENERAL_SEARCH;
					Rule.qsortGeneralListByCharmForPlayer(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, -1, GeneralListUI.TYPE_SEARCH);
					}
					else {
						generalListUI.resetGeneralListUI(genList, -1, GeneralListUI.TYPE_SEARCH);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行搜索");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_CONVALESCE: 
				genList = engine.getCityAllGeneralList(curCityId);
				if (genList != null) {
					pushState(state);
					state = STATE_GENERAL_CONVALESCE;
					Rule.qsortGeneralListByStaminaForPlayerConvalesce(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, -1, GeneralListUI.TYPE_CONVALESCE);
					}
					else {
						generalListUI.resetGeneralListUI(genList, -1, GeneralListUI.TYPE_CONVALESCE);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行修养");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_MOVE: 
				genList = engine.getCityAllGeneralList(curCityId);
				if (genList != null) {
					pushState(state);
					state = STATE_GENERAL_MOVE;
					Rule.qsortGeneralListByStaminaForPlayerMove(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, -1, GeneralListUI.TYPE_MOVE);
					}
					else {
						generalListUI.resetGeneralListUI(genList, -1, GeneralListUI.TYPE_MOVE);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行调动");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_AWARD: 
				int genCount = engine.getCityAllGeneralCount(curCityId);
				if (genCount > 0) {
					pushState(state);
					initGeneralAward(genCount);
					state = STATE_GENERAL_AWARD;
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行授予");
					pt.popup();
				}
				break;
			default: throw new RuntimeException("错误的城市子菜单号，cityMenuIndex="+cityMenuIndex+", citySubMenuIndex"+citySubMenuIndex);
			}
		}
	}

	private void initGeneralAward(int genCount) {
		curGroupIndex = 0;
		curGeneralBaseAttrIndex = 0;
		curGeneralArmyAttrIndex = 0;
		
		awardGeneralCount = (short)(genCount);
		awardGeneralCurPage = 0;
		awardGeneralIndex = 0;
		scroll = new ScrollBar();
		scroll.setImage(Resource.loadImage(Resource.PIC_ID_SCROLL_BAR));
		scroll.setViewLen(Resource.NUM_AWARD_GENERAL_LIST_ITEMS);
		scroll.setContentLen((short)(awardGeneralCount));
		scroll.setCurPage(awardGeneralCurPage);
		if (awardGeneralCount > Resource.NUM_AWARD_GENERAL_LIST_ITEMS) {
			scroll.setTotalPage((short)(awardGeneralCount-Resource.NUM_AWARD_GENERAL_LIST_ITEMS+1));
		}
		else {
			scroll.setTotalPage((short)(1));
		}
		scroll.setScrollType(ScrollBar.SCROLL_UP_DOWN);
		scroll.setFrameRegion(Resource.REGION_SCROLL_BAR_FRAME);
		scroll.setPosition(Resource.X_AWARD_BG+119, Resource.Y_AWARD_BG+102);
		scroll.setSize(16, 157);
	}

	private void handleMilitaryAffairs(KeyState key) {
		handleCitySubMenu(key);
		handleMilitaryAffairsSubMenu(key);
	}

	private void handleMilitaryAffairsSubMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.OK)) {
			showUseProp = 0;
			short[] genList = engine.getCityAllGeneralList(curCityId);
			Permission permission;
			switch(citySubMenuIndex) {
			case CITY_SUB_MENU_DRAFT: 
				if (genList != null) {
					permission = Rule.getDraftPermission(engine.cityList[curCityId]);
					if (permission.hasPermission()) {
						pushState(state);
						state = STATE_MILITARY_DRAFT;
						soldierCount = new short[engine.soldierList.length];
						curGroupIndex = 0;
					}
					else {
						if (permission.needTip()) {
							PopupText pt = Resource.buildPopupText();
							pt.setText(permission.getMessage());
							pt.popup();
						}
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行征兵");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_EXPEDITION: 
				if (genList != null) {
					permission = Rule.getExpeditionPermission(engine.cityList[curCityId]);
					if (permission.hasPermission()) {
						pushState(state);
						state = STATE_MILITARY_EXPEDITION;
						Rule.qsortGeneralListByArmyPowerForPlayer(genList);
						if (generalListUI == null) {
							generalListUI = new GeneralListUI(genList, 5, GeneralListUI.TYPE_EXPEDITION);
						}
						else {
							generalListUI.resetGeneralListUI(genList, 5, GeneralListUI.TYPE_EXPEDITION);
						}
					}
					else {
						if (permission.needTip()) {
							PopupText pt = Resource.buildPopupText();
							pt.setText(permission.getMessage());
							pt.popup();
						}
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行出征");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_DISTRIBUTE: 
				if (genList != null) {
					pushState(state);
					state = STATE_MILITARY_DISTRIBUTE;
					Rule.qsortGeneralListByArmyPowerForPlayer(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, 1, GeneralListUI.TYPE_DISTRIBUTE);
						generalListUI.setConsiderGeneralState(false);
					}
					else {
						generalListUI.resetGeneralListUI(genList, 1, GeneralListUI.TYPE_DISTRIBUTE);
						generalListUI.setConsiderGeneralState(false);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行分配");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_TRANSFER:
				if (genList != null) {
					pushState(state);
					state = STATE_MILITRAY_TRANSFER;
					cityWealth = engine.cityList[curCityId].getWealth();
					cityProvisions = engine.cityList[curCityId].getProvisions();
					curGroupIndex = 0;
					curConfirmIndex = 0;
					initWealthTb(cityWealth);
					wealthTb.setDisabled(false);
					initProvisionsTb(cityProvisions);
					provisionsTb.setDisabled(true);
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行输送");
					pt.popup();
				}
				break;
			default: throw new RuntimeException("错误的城市子菜单号，cityMenuIndex="+cityMenuIndex+", citySubMenuIndex"+citySubMenuIndex);
			}
		}
	}
	
	private void initWealthTb(int wealth) {
		if (wealthTb == null) {
			wealthTb = new TextBox();
			wealthTb.setTextColor(0XFFFBBF);
			wealthTb.setOverideBgColor(0XFF);
			wealthTb.setOverideTextColor(0XFFFFFF);
			wealthTb.setCursorColor(0XFF00FF);
		}
		wealthTb.setMaxValue(wealth);
	}
	
	private void initProvisionsTb(int provisions) {
		if (provisionsTb == null) {
			provisionsTb = new TextBox();
			provisionsTb.setTextColor(0XFFFBBF);
			provisionsTb.setOverideBgColor(0XFF);
			provisionsTb.setOverideTextColor(0XFFFFFF);
			provisionsTb.setCursorColor(0XFF00FF);
		}
		provisionsTb.setMaxValue(provisions);
	}

	private void handleDevelop(KeyState KeyState) {
		handleCitySubMenu(KeyState);
		handleDevelopSubMenu(KeyState);
	}

	private void handleDevelopSubMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.OK)) {
			showUseProp = 0;
			short[] genList = engine.getCityAllGeneralList(curCityId);
			switch (citySubMenuIndex) {
			case CITY_SUB_MENU_FARMING:
				if (genList != null) {
					pushState(state);
					state = STATE_DEVELOP_FARMING;
					Rule.qsortGeneralListByWitForPlayer(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, 3, GeneralListUI.TYPE_FARMING);
					}
					else {
						generalListUI.resetGeneralListUI(genList, 3, GeneralListUI.TYPE_FARMING);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行农业开发");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_COMMERCE: 
				if (genList != null) {
					pushState(state);
					state = STATE_DEVELOP_COMMERCE;
					Rule.qsortGeneralListByWitForPlayer(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, 3, GeneralListUI.TYPE_COMMERCE);
					}
					else {
						generalListUI.resetGeneralListUI(genList, 3, GeneralListUI.TYPE_COMMERCE);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行商业开发");
					pt.popup();
				}
				break;
			case CITY_SUB_MENU_RELIEF: 
				if (genList != null) {
					pushState(state);
					state = STATE_DEVELOP_RELIEF;
					Rule.qsortGeneralListByCharmForPlayer(genList);
					if (generalListUI == null) {
						generalListUI = new GeneralListUI(genList, 3, GeneralListUI.TYPE_RELIEF);
					}
					else {
						generalListUI.resetGeneralListUI(genList, 3, GeneralListUI.TYPE_RELIEF);
					}
				}
				else {
					PopupText pt = Resource.buildPopupText();
					pt.setText("城内没有武将，无法执行救济");
					pt.popup();
				}
				break;
			default: throw new RuntimeException("错误的城市子菜单号，cityMenuIndex="+cityMenuIndex+", citySubMenuIndex"+citySubMenuIndex);
			}
		}
	}
	
	private void handleCitySubMenu(KeyState key) {
		int menuCount = Resource.STR_CITY_SUB_MENU[cityMenuIndex].length;
		if (key.containsAndRemove(KeyCode.UP)) {
			citySubMenuIndex = (byte)((citySubMenuIndex+menuCount-1)%menuCount);
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			citySubMenuIndex = (byte)((citySubMenuIndex+1)%menuCount);
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			state = popState();
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popStateToNormal();
		}
	}
	
	private byte getCitySubMenuState(byte index) {
		switch (index) {
		case CITY_MENU_DEVELOP:return STATE_DEVELOP_MENU;
		case CITY_MENU_MILITARY_AFFAIRS:return STATE_MILITARY_MENU;
		case CITY_MENU_GENERAL: return STATE_GENERAL_MENU;
		case CITY_MENU_MARKET:return STATE_MARKET_MENU;
		default:throw new RuntimeException("错误的城市菜单号，cityMenuIndex="+cityMenuIndex);
		}
	}

	private void handleCity(KeyState key) {
		if (engine.isDebugMode()) {
			/*加金*/
			if (key.containsAndRemove(KeyCode.NUM1)) {
				engine.cityList[curCityId].incWealth(1000);
			}
			/*加粮*/
			if (key.containsAndRemove(KeyCode.NUM2)) {
				engine.cityList[curCityId].incProvisions(1000);
			}
			/*加商业*/
			if (key.containsAndRemove(KeyCode.NUM3)) {
				engine.cityList[curCityId].incCommerce(100);
			}
			/*加农业*/
			if (key.containsAndRemove(KeyCode.NUM4)) {
				engine.cityList[curCityId].incFarming(100);
			}
			/*加人口*/
			if (key.containsAndRemove(KeyCode.NUM5)) {
				engine.cityList[curCityId].incPopulation(100);
			}
			/*加民忠*/
			if (key.containsAndRemove(KeyCode.NUM6)) {
				engine.cityList[curCityId].incLoyalty(100);
			}
		}
		
		int menuCount = Resource.STR_CITY_MENU.length;;
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curCityGroupIndex == 0) {
				cityMenuIndex = (byte)((cityMenuIndex+menuCount-1)%menuCount);
			}
			else {
				if (curCityAttrIndex > 0) {
					--curCityAttrIndex;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curCityGroupIndex == 0) {
				cityMenuIndex = (byte)((cityMenuIndex+1)%menuCount);
			}
			else {
				if (curCityAttrIndex < 3) {
					++curCityAttrIndex;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curCityGroupIndex > 0) {
				--curCityGroupIndex;
			}
			else {
				state = popStateToNormal();
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curCityGroupIndex < 1) {
				++curCityGroupIndex;
			}
		}

		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			state = popStateToNormal();
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curCityGroupIndex == 0) {
				pushState(state);
				state = getCitySubMenuState(cityMenuIndex);
				citySubMenuIndex = 0;
			}
			else {
				showUseProp = 1;
				prepareCityAttrRaiseRes(engine.cityList[curCityId], curCityAttrIndex);
				clearMapLargePic();
			}
		}
	}

	private void prepareCityAttrRaiseRes(City city, int type) {
		usePropConfirm.setCity(city);
		switch (type) {
		case CITY_ATTR_WEALTH:
			usePropConfirm.addProp(engine.propList[2]);
			usePropConfirm.addProp(engine.propList[1]);
			break;
		case CITY_ATTR_PROVISIONS:
			usePropConfirm.addProp(engine.propList[4]);
			usePropConfirm.addProp(engine.propList[3]);
			break;
		case CITY_ATTR_LOYALTY:
			usePropConfirm.addProp(engine.propList[5]);
			usePropConfirm.addProp(engine.propList[6]);
			break;
		case CITY_ATTR_BOX:
			usePropConfirm.addProp(engine.propList[18]);
			usePropConfirm.addProp(engine.propList[0]);
			break;
		default:
			break;
		}
	}

	private void handleSystem(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			systemMenu.prevItem();
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			systemMenu.nextItem();
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			StateRecord stateRecord = null;
			StateMarket stateMarket = null;
			StateHelp stateHelp = null;
			switch(systemMenu.getHilightIndex()) {
			case SYSTEM_MENU_BACK: 
				clearSystemMenu();
				state = popState();
				break;
			case SYSTEM_MENU_SAVE_RECORD: 
				clearSystemMenu();
				clear();
				stateRecord = new StateRecord();
				stateRecord.popup(StateRecord.TYPE_SAVE);
				state = popState();
				break;
			case SYSTEM_MENU_READ_RECORD: 
				clearSystemMenu();
				clear();
				stateRecord = new StateRecord();
				if (stateRecord.popup(StateRecord.TYPE_LOAD) == 0) {
					init();
				}
				else {
					state = popState();
				}
				break;
			case SYSTEM_MENU_PROP_MARKET: 
				clearSystemMenu();
				clear();
				stateMarket = new StateMarket(StateMarket.TYPE_PROP_MARKET);
				stateMarket.popup();
				state = popState();
				break;
			case SYSTEM_MENU_GENERAL_MARKET: 
				clearSystemMenu();
				clear();
				stateMarket = new StateMarket(StateMarket.TYPE_GENERAL_MARKET);
				stateMarket.popup();
				state = popState();
				break;
			case SYSTEM_MENU_HELP: 
				clearSystemMenu();
				clear();
				stateHelp = new StateHelp();
				stateHelp.popup();
				state = popState();
				break;
			case SYSTEM_MENU_QUIT: 
				clearSystemMenu();
				PopupConfirm pc = Resource.buildPopupConfirm();
				pc.setText("确定保存游戏并退出吗？");
				if (pc.popup() == 0) {
					clear();
					stateRecord = new StateRecord();
					if (stateRecord.popup(StateRecord.TYPE_SAVE) == 0) {
						engine.gotoStateMain();
					}
					else {
						pc.setText("您未成功保存游戏进度，确定退出吗？");
						if (pc.popup() == 0) {
							engine.gotoStateMain();
						}
						else {
							state = popState();
						}
					}
				}
				else {
					state = popState();
				}
				break;
			default: break;
			}
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.LEFT|KeyCode.BACK)) {
			key.clear();
			clearSystemMenu();
			state = popState();
		}
	}

	private void clearSystemMenu() {
		Resource.freeImage(Resource.PIC_ID_SYSTEM_MENU_BG);
		systemMenu = null;
	}
	
	private void handleNormal(KeyState key) {
		if (engine.isDebugMode()) {
			if (key.contains(KeyCode.NUM1)) {
				__DEBUG_WIN = true;
			}
			if (key.contains(KeyCode.NUM2)) {
				__DEBUG_LOSE = true;
			}
		}
		if (key.containsAndRemove(KeyCode.UP)) {
			if (Resource.getUpCityId(curCityId) >= 0) {
				curCityId = Resource.getUpCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (Resource.getDownCityId(curCityId) >= 0) {
				curCityId = Resource.getDownCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (Resource.getLeftCityId(curCityId) >= 0) {
				curCityId = Resource.getLeftCityId(curCityId);
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM7)) {
			clear();
			StateRecord stateRecord = new StateRecord();
			stateRecord.popup(StateRecord.TYPE_SAVE);
		}
		
		if (key.containsAndRemove(KeyCode.NUM8)) {
			clear();
			StateRecord stateRecord = new StateRecord();
			if (stateRecord.popup(StateRecord.TYPE_LOAD) == 0) {
				init();
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM9)) {
			clear();
			StateHelp stateHelp = new StateHelp();
			stateHelp.popup();
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (Resource.getRightCityId(curCityId) >= 0) {
				curCityId = Resource.getRightCityId(curCityId);
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			pushState(state);
			state = STATE_GAME_MENU;
			slideState = 1;
			gameMenuIndex = 0;
			if (engine.cityList[curCityId].belongToPlayer()) {
				gameMenuStr = Resource.STR_PLAYER_GAME_MENU;
			}
			else {
				gameMenuStr = Resource.STR_OTHER_GAME_MENU;
			}
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			pushState(state);
			if (systemMenu == null) {
				createSystemMenu();
			}
			state = STATE_SYSTEM_MENU;
		}
	}
	
	public void show(Graphics g) {
		switch(state) {
		case STATE_START:
			showStart(g);
			break;
		case STATE_GIFTS:
			showGifts(g);
			break;
		case STATE_NORMAL: 
			showNormal(g);
			break;
		case STATE_SYSTEM_MENU: 
			showSystem(g);
			break;
		case STATE_GAME_MENU:
			showGameMenu(g);
			break;
		case STATE_CITY_MENU: 
			if (showUseProp == 0) {
				showCity(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCity(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				showMapTopInfo(g);
				usePropConfirm.show(g);
			}
			break;
		case STATE_ROUND_END:
			showRoundEnd(g);
			break;
		case STATE_DEVELOP_MENU: 
			showDevelop(g);
			break;
		case STATE_DEVELOP_FARMING: 
			if (showUseProp == 0) {
				showDevelopFarming(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showDevelopFarming(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_DEVELOP_COMMERCE: 
			if (showUseProp == 0) {
				showDevelopCommerce(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showDevelopCommerce(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_DEVELOP_RELIEF: 
			if (showUseProp == 0) {
				showDevelopRelief(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showDevelopRelief(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_MILITARY_MENU: 
			showMilitaryAffairs(g);
			break;
		case STATE_MILITARY_DRAFT: 
			showMilitartAffairsDraft(g);
			break;
		case STATE_MILITARY_EXPEDITION: 
			if (showUseProp == 0) {
				showMilitartAffairsExpedition(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showMilitartAffairsExpedition(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_MILITARY_DISTRIBUTE: 
			showMilitartAffairsDistribute(g);
			break;
		case STATE_EXPEDITION_TARGET:
			showExpeditionTarget(g);
			break;
		case STATE_DISTRIBUTE_DETAIL:
			showDistributeDetail(g);
			break;
		case STATE_MILITRAY_TRANSFER:
			showMilitaryTransfer(g);
			break;
		case STATE_TRANSFER_TARGET:
			showTransferTarget(g);
			break;
		case STATE_GENERAL_MENU: 
			showGeneral(g);
			break;
		case STATE_GENERAL_SEARCH: 
			if (showUseProp == 0) {
				showGeneralSearch(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showGeneralSearch(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_GENERAL_CONVALESCE: 
			if (showUseProp == 0) {
				showGeneralConvalesce(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showGeneralConvalesce(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_GENERAL_MOVE: 
			if (showUseProp == 0) {
				showGeneralMove(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					updateGeneralListAttribute();
					showGeneralMove(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_GENERAL_AWARD:
			if (showUseProp == 0) {
				showGeneralAward(g);
			}
			else {
				if (usePropConfirm.isNeedPaintPreBg()) {
					showCommonMapBgInfo(g);
					showGeneralAward(g);
					clearMapLargePic();
					usePropConfirm.setNeedPaintPreBg(false);
				}
				usePropConfirm.show(g);
			}
			break;
		case STATE_GENERAL_MOVE_TARGE:
			showGeneralMoveTarge(g);
			break;
		case STATE_MARKET_MENU: 
			showMarket(g);
			break;
		case STATE_MARKET_GRAIN_TRADE:
			showGrainTrade(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void showGifts(Graphics g) {
		showNormal(g);
	}

	private void showStart(Graphics g) {
		showNormal(g);
		showSlideWindow(g);
	}

	void showSlideWindow(Graphics g) {
		int scrW = engine.getScreenWidth();
		int scrH = engine.getScreenHeight();
		int lastFrame = 16;
		int stepW = 22;
		int startOffset = -80;
		g.setColor(0);
		
		int curStepX = slideFrame*stepW+startOffset;
		for (int i = 0; i < lastFrame; i++) {
			g.fillRect(curStepX, 0, i<<1, scrH);
			curStepX += stepW;
		}
		g.fillRect(curStepX, 0, scrW + 50, scrH);
		++slideFrame;
		if (slideFrame > lastFrame) {
			slideFrame = 0;
			slideState = 0;
		} 
	}
	
	public void prepareUsePropRes(Permission permission, City city, General general) {
		usePropConfirm.setCity(city);
		usePropConfirm.setGeneral(general);
		switch (permission.getCode()) {
		case Rule.CODE_GENERAL_STAMINA_NOT_ENOUGH:
			usePropConfirm.addProp(engine.propList[7]);
			usePropConfirm.addProp(engine.propList[8]);
			break;
		case Rule.CODE_CITY_WEALTH_NOT_ENOUGH:
			usePropConfirm.addProp(engine.propList[2]);
			break;
		case Rule.CODE_CITY_PROVISIONS_NOT_ENOUGH:
			usePropConfirm.addProp(engine.propList[4]);
			break;
		case Rule.CODE_CITY_LOYALTY_NOT_ENOUTH:
			usePropConfirm.addProp(engine.propList[5]);
			usePropConfirm.addProp(engine.propList[6]);
			break;
		}
	}
	
	public void handleCommonUseProp(KeyState KeyState) {
		usePropConfirm.handle(KeyState);
		if (usePropConfirm.isBack()) {
			showUseProp = 0;
			usePropConfirm.clear();
		}
	}
	
	public void handleUsePropFromGeneralList(KeyState KeyState) {
		usePropConfirm.handle(KeyState);
		if (usePropConfirm.isBack()) {
			showUseProp = 0;
			usePropConfirm.clear();
			updateGeneralListAttribute();
		}
	}
	
	public int showPermissionTip(Permission permission) {
		int index = 1;
		if (permission.needTip()) {
			if (permission.isUseProp()) {
				PopupConfirm pc = Resource.buildPopupConfirm();
				pc.setText(permission.getMessage());
				switch (permission.getCode()) {
				case Rule.CODE_GENERAL_STAMINA_NOT_ENOUGH:
					pc.setButtonText(Resource.STR_CONFIRM_ADD_STAMINA);
					break;
				case Rule.CODE_GENERAL_MAGIC_NOT_ENOUGH:
					pc.setButtonText(Resource.STR_CONFIRM_ADD_MAGIC);
					break;
				case Rule.CODE_CITY_WEALTH_NOT_ENOUGH:
					pc.setButtonText(Resource.STR_CONFIRM_ADD_WEALTH);
					break;
				case Rule.CODE_CITY_PROVISIONS_NOT_ENOUGH:
					pc.setButtonText(Resource.STR_CONFIRM_ADD_PROVISIONS);
					break;
				case Rule.CODE_CITY_LOYALTY_NOT_ENOUTH:
					pc.setButtonText(Resource.STR_CONFIRM_ADD_LOYALTY);
					break;
				}
				index = pc.popup();
			}
			else {
				PopupText pt = Resource.buildPopupText();
				pt.setText(permission.getMessage());
				pt.popup();
			}
		}
		return index;
	}
	
	public void showCommonMapBgInfo(Graphics g) {
		//画背景图片
		g.drawImage(Resource.loadImage(Resource.PIC_ID_MAP_TOP_BG), 0, 0, 20);
		g.drawImage(Resource.loadImage(Resource.PIC_ID_MAP_BG), 0, 81, 20);
		showMapTopInfo(g);
	}
	
	public void clearCommonMapBgRes() {
		Resource.freeImage(Resource.PIC_ID_MAP_TOP_BG);
		Resource.freeImage(Resource.PIC_ID_MAP_BG);
	}

	private void showTransferTarget(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
	}

	private void showMilitaryTransfer(Graphics g) {
		Image tbg = Resource.loadImage(Resource.PIC_ID_TRANSFER_BG);
		int bgX = (engine.getScreenWidth()-tbg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-tbg.getHeight())>>1;
		g.drawImage(tbg, bgX, bgY, 20);
		
		int sx = bgX+Resource.X_TRANSFER_RES;
		int sy = bgY+Resource.Y_TRANSFER_RES;
		int sw = Resource.W_TRANSFER_RES;
		int sh = Resource.H_TRANSFER_RES;
		int deltaH = (sh-engine.getFont().getHeight())>>1;
		
		if (curGroupIndex < 2) {
			g.setColor(0xFFFF00);
			if (curGroupIndex == 0) {
				g.drawRect(sx+Resource.XSPACE_TRANSFER_RES, sy, sw, sh);
			}
			else {
				g.drawRect(sx+Resource.XSPACE_TRANSFER_RES, sy+Resource.YSPACE_TRANSFER_RES, sw, sh);
			}
		}
		
		/*城池金币*/
		g.setColor(0XFFFBBF);
		String text = Integer.toString(cityWealth);
		g.drawString(text, sx+((sw-engine.getFont().stringWidth(text))>>1), sy+deltaH, 20);
		/*剩余金币*/
		text = Integer.toString(cityWealth-wealthTb.getValue());
		g.drawString(text, sx+Resource.XSPACE_TRANSFER_LEFT_RES+((sw-engine.getFont().stringWidth(text))>>1), 
				sy+deltaH, 20);
		
		/*输送金币*/
		wealthTb.setTextBgPos(Resource.XSPACE_TRANSFER_RES+sx, sy);
		wealthTb.setTextBgSize(sw, sh);
		if (curGroupIndex == 0) {
			wealthTb.setDisabled(false);
			wealthTb.show(g);
		}
		else {
			wealthTb.setDisabled(true);
			wealthTb.show(g);
		}
		
		/*城池粮草*/
		g.setColor(0XFFFBBF);
		text = Integer.toString(cityProvisions);
		g.drawString(text, sx+((sw-engine.getFont().stringWidth(text))>>1), sy+deltaH+Resource.YSPACE_TRANSFER_RES, 20);
		/*剩余粮草*/
		text = Integer.toString(cityProvisions-provisionsTb.getValue()); 
		g.drawString(text, sx+Resource.XSPACE_TRANSFER_LEFT_RES+((sw-engine.getFont().stringWidth(text))>>1), 
				sy+deltaH+Resource.YSPACE_TRANSFER_LEFT_RES, 20);
		
		/*输送粮草*/
		provisionsTb.setTextBgPos(Resource.XSPACE_TRANSFER_RES+sx, sy+Resource.YSPACE_TRANSFER_RES);
		provisionsTb.setTextBgSize(sw, sh);
		if (curGroupIndex == 1) {
			provisionsTb.setDisabled(false);
			provisionsTb.show(g); 
		}
		else {
			provisionsTb.setDisabled(true);
			provisionsTb.show(g); 
		}
		
		Font font = g.getFont();
		int fontH = font.getHeight();
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_DRAFT_LEFT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DRAFT_LEFT_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		String ss = "确定";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-fontH)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		sx = bgX+Resource.X_DRAFT_RIGHT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DRAFT_RIGHT_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "返回";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-fontH)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		if (curGroupIndex == 2) {
			g.setColor(0XFFFF00);
			if (curConfirmIndex == 0) {
				DrawUtil.drawRect(g, bgX+Resource.X_TRANSFER_LEFT_BTN, bgY+Resource.Y_TRANSFER_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
			else {
				DrawUtil.drawRect(g, bgX+Resource.X_TRANSFER_RIGHT_BTN, bgY+Resource.Y_TRANSFER_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
		}
	}

	private void showGrainTrade(Graphics g) {
		Image tradeBg = Resource.loadImage(Resource.PIC_ID_TRADE_BG);
		int bgX = (engine.getScreenWidth()-tradeBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-tradeBg.getHeight())>>1;
		g.drawImage(tradeBg, bgX, bgY, 20);
		int sx = 0, sy = 0;
		String ss = "";
		Font font = engine.getFont();
		int strHeight = font.getHeight();
		
		if (curGroupIndex == 0) {
			g.setColor(0x406190);
			g.fillRect(bgX+Resource.X_TRADE_TYPE, 
					bgY+Resource.Y_TRADE_TYPE, 
					Resource.W_TRADE_TYPE, 
					Resource.H_TRADE_TYPE);
			g.setColor(0XFFFF00);
			g.drawRect(bgX+Resource.X_TRADE_TYPE, 
					bgY+Resource.Y_TRADE_TYPE, 
					Resource.W_TRADE_TYPE, 
					Resource.H_TRADE_TYPE);
		}
		else if (curGroupIndex == 1) {
			g.setColor(0XFFFF00);
			g.drawRect(bgX+Resource.X_TRADE_GOODS_TYPE+Resource.XSPACE_TRADE_GOODS_TYPE*curGoodsType, 
					bgY+Resource.Y_TRADE_GOODS_TYPE, 
					Resource.W_TRADE_GOODS_TYPE, 
					Resource.H_TRADE_GOODS_TYPE);
		}
		else {
			//null
		}
		
		//显示交易类型
		ss = (citySubMenuIndex == 0)?"买粮":"卖粮";
		sx = bgX+Resource.X_TRADE_TYPE+((Resource.W_TRADE_TYPE-font.stringWidth(ss))>>1);
		sy = bgY+Resource.Y_TRADE_TYPE+((Resource.H_TRADE_TYPE-strHeight)>>1);
		g.setColor(0XFFFFFF);
		g.drawString(ss, sx, sy, 20);
		
		//显示粮草
		ss = Integer.toString(engine.cityList[curCityId].getProvisions());
		sx = bgX+84+((Resource.W_TRADE_CITY_INFO-font.stringWidth(ss))>>1);
		sy = bgY+170+((Resource.H_TRADE_CITY_INFO-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		ss = Integer.toString(cityProvisions);
		sx = bgX+224+((Resource.W_TRADE_CITY_INFO-font.stringWidth(ss))>>1);
		sy = bgY+170+((Resource.H_TRADE_CITY_INFO-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		//显示金币
		ss = Integer.toString(engine.cityList[curCityId].getWealth());
		sx = bgX+84+((Resource.W_TRADE_CITY_INFO-font.stringWidth(ss))>>1);
		sy = bgY+214+((Resource.H_TRADE_CITY_INFO-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		ss = Integer.toString(cityWealth);
		sx = bgX+224+((Resource.W_TRADE_CITY_INFO-font.stringWidth(ss))>>1);
		sy = bgY+214+((Resource.H_TRADE_CITY_INFO-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		//显示按钮
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_TRADE_LEFT_BTN;
		sy = bgY+Resource.Y_TRADE_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "确定";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		sx = bgX+Resource.X_TRADE_RIGHT_BTN;
		sy = bgY+Resource.Y_TRADE_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "返回";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		if (curGroupIndex >= 2) {
			g.setColor(0XFFFF00);
			if (curConfirmIndex == 0) {
				DrawUtil.drawRect(g, bgX+Resource.X_TRADE_LEFT_BTN, bgY+Resource.Y_TRADE_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
			else {
				DrawUtil.drawRect(g, bgX+Resource.X_TRADE_RIGHT_BTN, bgY+Resource.Y_TRADE_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
		}
	}

	private void showGameMenu(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showGameMenuList(g);
	}

	private void showGameMenuList(Graphics g) {
		int sx = (engine.getScreenWidth()-Resource.W_CITY_SUB_MENU_BG)>>1;
		int sy = 200+2;
		Font font = g.getFont();
		int deltaH = (Resource.H_CITY_SUB_MENU_BG-font.getHeight())>>1;
		Image menuBg = Resource.loadImage(Resource.PIC_ID_CITY_SUB_MENU_BG);
		g.setColor(Resource.COLOR_ID_CITY_MENU_NORMAL_TEXT);
		int menuCount = gameMenuStr.length;
		int i = 0;
		while (i < menuCount) {
			if (i == gameMenuIndex) {
				g.drawRegion(menuBg, Resource.W_CITY_SUB_MENU_BG, 0, Resource.W_CITY_SUB_MENU_BG, Resource.H_CITY_SUB_MENU_BG, 0, 
						sx, sy, 20);
			}
			else {
				g.drawRegion(menuBg, 0, 0, Resource.W_CITY_SUB_MENU_BG, Resource.H_CITY_SUB_MENU_BG, 0, 
						sx, sy, 20);
			}
			g.drawString(gameMenuStr[i], 
					sx+((Resource.W_CITY_SUB_MENU_BG-font.stringWidth(gameMenuStr[i]))>>1), sy+deltaH, 20);
			sy += Resource.H_CITY_SUB_MENU_BG;
			++i;
		}
	}

	private void showDistributeDetail(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showDistributeInfo(g);
	}

	private void showDistributeInfo(Graphics g) {
		Image disBg = Resource.loadImage(Resource.PIC_ID_DISTRIBUTE_BG);
		int bgX = (engine.getScreenWidth()-disBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-disBg.getHeight())>>1;
		bgY += 30;
		g.drawImage(disBg, bgX, bgY, 20);
		
		Font font = engine.getFont();
		int strHeight = font.getHeight();
		int sx, sy;
		String ss;
		
		if (curGroupIndex < 2) {
			g.setColor(0x406190);
			if (curGroupIndex == 0) {
				g.fillRect(bgX+Resource.X_DISTRIBUTE_GENERAL_SOLDIER, bgY+Resource.Y_DISTRIBUTE_GENERAL_SOLDIER_NAME, 
						Resource.W_DISTRIBUTE_GENERAL_SOLDIER, Resource.H_DISTRIBUTE_GENERAL_SOLDIER);
			}
			else {
				g.fillRect(bgX+Resource.X_DISTRIBUTE_GENERAL_SOLDIER, bgY+Resource.Y_DISTRIBUTE_GENERAL_SOLDIER_COUNT, 
						Resource.W_DISTRIBUTE_GENERAL_SOLDIER, Resource.H_DISTRIBUTE_GENERAL_SOLDIER);
			}
		}
		
		/*显示武将姓名*/
		g.setColor(0XFFFBBF);
		ss = distributeGeneral.getName();
		sx = bgX+139+4;
		sy = bgY+85+((26-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		/*显示体力*/
		ss = Integer.toString(distributeGeneral.getStamina());
		sx = bgX+139+4;
		sy = bgY+116+((26-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		sy += Resource.YSPACE_DISTRIBUTE_GENERAL_ATTRIBUTE;
		
		/*显示精力*/
		ss = Integer.toString(distributeGeneral.getMagic());
		g.drawString(ss, sx, sy, 20);
		sy += Resource.YSPACE_DISTRIBUTE_GENERAL_ATTRIBUTE;
		
		/*显示武力*/
		ss = Integer.toString(distributeGeneral.getForce());
		g.drawString(ss, sx, sy, 20);
		sy += Resource.YSPACE_DISTRIBUTE_GENERAL_ATTRIBUTE;
		
		/*显示智力*/
		ss = Integer.toString(distributeGeneral.getWit());
		g.drawString(ss, sx, sy, 20);
		sy += Resource.YSPACE_DISTRIBUTE_GENERAL_ATTRIBUTE;
		
		/*显示将技*/
		ss = Integer.toString(distributeGeneral.getMagicPower());
		g.drawString(ss, sx, sy, 20);
		
		/*显示兵种*/
		ss = engine.soldierList[curSoldierIndex].getName();
		sx = bgX+Resource.X_DISTRIBUTE_GENERAL_SOLDIER;
		sy = bgY+Resource.Y_DISTRIBUTE_GENERAL_SOLDIER_NAME;
		sx += ((Resource.W_DISTRIBUTE_GENERAL_SOLDIER-font.stringWidth(ss))>>1);
		sy += ((Resource.H_DISTRIBUTE_GENERAL_SOLDIER-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		/*显示兵数*/
		ss = distributeSoldierCount+"/"+distributeGeneral.getLead();
		sx = bgX+Resource.X_DISTRIBUTE_GENERAL_SOLDIER;
		sy = bgY+Resource.Y_DISTRIBUTE_GENERAL_SOLDIER_COUNT;
		sx += ((Resource.W_DISTRIBUTE_GENERAL_SOLDIER-font.stringWidth(ss))>>1);
		sy += ((Resource.H_DISTRIBUTE_GENERAL_SOLDIER-strHeight)>>1);
		g.drawString(ss, sx, sy, 20);
		
		/*显示城市剩余兵力*/
		sx = bgX+Resource.X_DISTRIBUTE_CITY_SOLDIER_COUNT;
		sy = bgY+Resource.Y_DISTRIBUTE_CITY_SOLDIER_COUNT;
		for (int i = 0; i < soldierCount.length; ++i, sy+=Resource.YSPACE_DISTRIBUTE_CITY_SOLDIER_COUNT) {
			ss = soldierCount[i]+"/"+City.MAX_FREE_SOLDIER_COUNT;
			g.drawString(ss, sx+((Resource.W_DISTRIBUTE_CITY_SOLDIER_COUNT-font.stringWidth(ss))>>1), sy, 20);
		}
		
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_DISTRIBUTE_LEFT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DISTRIBUTE_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "确定";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		sx = bgX+Resource.X_DISTRIBUTE_RIGHT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DISTRIBUTE_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "返回";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		if (curGroupIndex >= 2) {
			g.setColor(0XFFFF00);
			if (curConfirmIndex == 0) {
				DrawUtil.drawRect(g, bgX+Resource.X_DISTRIBUTE_LEFT_CONFIRM_BTN, bgY+Resource.Y_DISTRIBUTE_CONFIRM_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
			else {
				DrawUtil.drawRect(g, bgX+Resource.X_DISTRIBUTE_RIGHT_CONFIRM_BTN, bgY+Resource.Y_DISTRIBUTE_CONFIRM_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
		}
	}

	private void showRoundEnd(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showRoundInfo(g);
	}

	private void showRoundInfo(Graphics g) {
		int waitCursorW = 74;
		int waitCursorH = 74;
		Image wait = Resource.loadImage(Resource.PIC_ID_WAIT);
		int sx = (engine.getScreenWidth()-waitCursorW)>>1;
		int sy = (engine.getScreenHeight()-waitCursorH)>>1;
		int cursorSequence;
		if (waitCursorFrame >= NUM_WAIT_CURSOR_SEQUENCES>>1) {
			cursorSequence = waitCursorFrame-(NUM_WAIT_CURSOR_SEQUENCES>>1);
		}
		else {
			cursorSequence = waitCursorFrame;
		}
		g.drawRegion(wait, cursorSequence*waitCursorW, 0, 
				waitCursorW, waitCursorH, 0, sx, sy, 20);
		
		Image bottom = Resource.loadImage(Resource.PIC_ID_BOTTOM_INFO_BG);
		sx = 0;
		sy = engine.getScreenHeight()-bottom.getHeight();
		g.drawImage(bottom, sx, sy, 20);
		
		if (currentTurn < 0) {
			bottomText = "城市金钱，粮食，人口增加";
		}
		else {
			while (currentTurn < seigneurList.length) {
				if (engine.getSeigneurCityCount(seigneurList[currentTurn]) > 0) {
					break;
				}
				++currentTurn;
			}
			
			if (currentTurn < engine.seigneurList.length) {
				bottomText = engine.generalList[engine.seigneurList[seigneurList[currentTurn]].getGeneralId()].getName()+"正在行动";
			}
			else {
				bottomText = "新的回合";
			}
		}
		
		if (bottomText != null) {
			sx += (bottom.getWidth()-engine.getFont().stringWidth(bottomText))/2;
			sy += (bottom.getHeight()-engine.getFont().getHeight())/2;
			g.setColor(0XFFFFFF);
			g.drawString(bottomText, sx, sy, 20);
		}
	}

	private void showGeneralMoveTarge(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
	}

	private void showDevelopRelief(Graphics g) {
		showGeneralListUI(g);
	}

	private void showDevelopCommerce(Graphics g) {
		showGeneralListUI(g);
	}

	private void showGeneralAward(Graphics g) {
		//showMapTop(g);
		if (engine.isDebugMode()) {
			engine.addDebugUserMessage("1:武+10; 2:智+10; 3:魅+10; 4:技+10; 5:体加满; 6:兵加满");
		}
		
		/*授予背景*/
		Image awardBg = Resource.loadImage(Resource.PIC_ID_AWARD_BG);
		int bgX = Resource.X_AWARD_BG;
		int bgY = Resource.Y_AWARD_BG;
		g.drawImage(awardBg, bgX, bgY, 20);
		
		int sx = 0, sy = 0;
		int loopCount = 0;
		/*显示武将列表*/
		if (awardGeneralCount >= Resource.NUM_AWARD_GENERAL_LIST_ITEMS) {
			loopCount = Resource.NUM_AWARD_GENERAL_LIST_ITEMS;
		}
		else {
			loopCount = awardGeneralCount;
		}
		sx = bgX+Resource.X_AWARD_GENERAL_LIST;
		sy = bgY+Resource.Y_AWARD_GENERAL_LIST;
		g.setColor(0XFFFFFF);
		General general = null;
		int loopi = 0;
		Font font = g.getFont();
		int fontH = font.getHeight();
		if (loopi < loopCount) {
			general = engine.generalList[engine.getCityGeneralIdByOrder(curCityId, awardGeneralCurPage+loopi)];
			g.setColor(0XFFFFFF);
			g.drawString(general.getName(), 
					sx+((Resource.W_AWARD_GENERAL_LIST-font.stringWidth(general.getName()))>>1), 
					sy+((Resource.H_AWARD_GENERAL_LIST-fontH)>>1), 
					20);
			sy += Resource.YSPACE_AWARD_GENERAL_LIST;
			++loopi;
		}
		
		while (loopi < loopCount) {
			general = engine.generalList[engine.getCityNextGeneralId(general.getId())];
			g.setColor(0XFFFFFF);
			g.drawString(general.getName(), 
					sx+((Resource.W_AWARD_GENERAL_LIST-font.stringWidth(general.getName()))>>1), 
					sy+((Resource.H_AWARD_GENERAL_LIST-fontH)>>1), 
					20);
			sy += Resource.YSPACE_AWARD_GENERAL_LIST;
			++loopi;
		}
		
		if (curGroupIndex == 0) {
			g.setColor(0XFFFF00);
			DrawUtil.drawRect(g, sx, bgY+Resource.Y_AWARD_GENERAL_LIST+awardGeneralIndex*Resource.YSPACE_AWARD_GENERAL_LIST, 
					Resource.W_AWARD_GENERAL_LIST, Resource.H_AWARD_GENERAL_LIST, 2);
		}
		scroll.show(g);

		Image plus = Resource.loadImage(Resource.PIC_ID_PLUS);
		int plusW = plus.getWidth();
		int plusH = plus.getHeight();
		
		/*显示武将详情*/
		general = engine.generalList[engine.getCityGeneralIdByOrder(curCityId, awardGeneralCurPage+awardGeneralIndex)];
		int ydelta = (Resource.H_AWARD_GENERAL_INFO-font.getHeight())>>1;
		g.setColor(0XFFFFFF);
		g.drawString(general.getName(), bgX+231, bgY+59+ydelta, 20);
		
		sx = bgX+219;
		sy = bgY+96;
		//体力
		g.drawString(Integer.toString(general.getStamina()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==1 && curGeneralBaseAttrIndex == GENERAL_ATTR_STAMINA) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//精力
		g.drawString(Integer.toString(general.getMagic()), sx, sy+ydelta, 20);
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//武力
		g.drawString(Integer.toString(general.getForce()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==1 && curGeneralBaseAttrIndex == GENERAL_ATTR_FORCE) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		
		//智力
		g.drawString(Integer.toString(general.getWit()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==1 && curGeneralBaseAttrIndex == GENERAL_ATTR_WIT) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		
		//魅力
		g.drawString(Integer.toString(general.getCharm()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==1 && curGeneralBaseAttrIndex == GENERAL_ATTR_CHARM) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//将技
		g.drawString(Integer.toString(general.getMagicPower()), sx, sy+ydelta, 20);
		
		sx = bgX+386;
		sy = bgY+96+ydelta;
		//统率
		g.drawString(Integer.toString(general.getLead()), sx, sy+ydelta, 20);
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//官职
		g.drawString(Rule.getMilitaryTitle(general.getLead()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==2 && curGeneralArmyAttrIndex == GENERAL_ATTR_TITLE) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//兵力
		g.drawString(Integer.toString(general.getSoldierCount()), sx, sy+ydelta, 20);
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//部队攻击
		g.drawString(Integer.toString(general.getSoldierAttack()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==2 && curGeneralArmyAttrIndex == GENERAL_ATTR_ARMY_ATTACK) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		sy += Resource.YSPACE_AWARD_GENERAL_INFO;
		
		//部队防御
		g.drawString(Integer.toString(general.getSoldierDefense()), sx, sy+ydelta, 20);
		g.drawImage(plus, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, 20);
		if (curGroupIndex==2 && curGeneralArmyAttrIndex == GENERAL_ATTR_ARMY_DEFENSE) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_AWARD_GENERAL_PLUS, sy-3, plusW, plusH, 2, 0XFFFF00);
			g.setColor(0XFFFFFF);
		}
		
		//攻击加成
		int iconX = 360;
		int iconY = 192;
		int iconW = 22;
		int iconH = 22;
		City city = general.getCity();
		Image icon = null;
		Image grayIcon = null;
		if (city.getAttackEnhance() > 0) {
			Resource.freeImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_INVALID_ICON);
			icon = Resource.loadImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_ICON);
			g.drawImage(icon, bgX+iconX, bgY+iconY, 20);
		}
		else {
			grayIcon = Resource.getImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_INVALID_ICON);
			if (grayIcon == null) {
				icon = Resource.loadImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_ICON);
				grayIcon = ImageUtil.createGray(icon, 0, 0, iconW, iconH);
				Resource.setImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_INVALID_ICON, grayIcon);
				Resource.freeImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_ICON);
			}
			g.drawImage(grayIcon, bgX+iconX, bgY+iconY, 20);
		}
		
		//防御加成
		iconY = 224;
		if (city.getDefenseEnhance() > 0) {
			Resource.freeImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_INVALID_ICON);
			icon = Resource.loadImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_ICON);
			g.drawImage(icon, bgX+iconX, bgY+iconY, 20);
		}
		else {
			grayIcon = Resource.getImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_INVALID_ICON);
			if (grayIcon == null) {
				icon = Resource.loadImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_ICON);
				grayIcon = ImageUtil.createGray(icon, 0, 0, iconW, iconH);
				Resource.setImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_INVALID_ICON, grayIcon);
				Resource.freeImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_ICON);
			}
			g.drawImage(grayIcon, bgX+iconX, bgY+iconY, 20);
		}

		sx = bgX+385;
		sy = bgY+307;
		Image btn = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		g.drawRegion(btn, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		
		String ss = "返回";
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, 
				sx+((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1), 
				sy+((Resource.H_CONFIRM_BTN-fontH)>>1), 
				20);
		
		if (curGroupIndex == 3) {
			DrawUtil.drawRect(g, sx, sy, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2, 0XFFFF00);
		}
	}

	private void showExpeditionTarget(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
	}

	private void showGeneralMove(Graphics g) {
		showGeneralListUI(g);
	}

	private void showGeneralConvalesce(Graphics g) {
		showGeneralListUI(g);
	}

	private void showGeneralSearch(Graphics g) {
		showGeneralListUI(g);
	}

	private void showMilitartAffairsDistribute(Graphics g) {
		showGeneralListUI(g);
	}

	private void showMilitartAffairsDraft(Graphics g) {
		Image draftBg = Resource.loadImage(Resource.PIC_ID_DRAFT_BG);
		int bgX = (engine.getScreenWidth()-draftBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-draftBg.getHeight())>>1;
		g.drawImage(draftBg, bgX, bgY, 20);

		City curCity = engine.cityList[curCityId];
		Font font = engine.getFont();
		int strHeight = font.getHeight();
		String ss;
		int sx, sy;
		sy = bgY+Resource.Y_DRAFT_SOLDIER_COUNT;
		for (int i = 0; i < engine.soldierList.length; i++, sy += Resource.YSPACE_DRAFT_SOLDIER_COUNT) {
			if (curGroupIndex == i) {
				g.setColor(0x406190);
				g.fillRect(bgX+Resource.X_DRAFT_SOLDIER_COUNT, sy, 
						Resource.W_DRAFT_SOLDIER_COUNT, Resource.H_DRAFT_SOLDIER_COUNT);
			}
			
			g.setColor(0XFFFBBF);
			/*显示城池兵力*/
			sx = bgX+Resource.X_DRAFT_SOLDIER_REDIF;
			ss = (curCity.getFreeSoldierCount((short)i)+soldierCount[i])+"/"+City.MAX_FREE_SOLDIER_COUNT;
			g.drawString(ss, 
					sx+((Resource.W_DRAFT_SOLDIER_REDIF-font.stringWidth(ss))>>1), 
					sy+((Resource.H_DRAFT_SOLDIER_REDIF-strHeight)>>1), 
					20);
			
			/*显示征集的兵力*/
			sx = bgX+Resource.X_DRAFT_SOLDIER_COUNT;
			ss = Integer.toString(soldierCount[i]);
			g.drawString(ss, 
					sx+((Resource.W_DRAFT_SOLDIER_COUNT-font.stringWidth(ss))>>1), 
					sy+((Resource.H_DRAFT_SOLDIER_COUNT-strHeight)>>1), 
					20);
			
			/*显示消耗的金币*/
			sx = bgX+Resource.X_DRAFT_COIN_COMSUMPTION;
			ss = Integer.toString((soldierCount[i]/100)*engine.soldierList[i].getAmount());
			g.drawString(ss, 
					sx+((Resource.W_DRAFT_COIN_COMSUMPTION-font.stringWidth(ss))>>1), 
					sy+((Resource.H_DRAFT_COIN_COMSUMPTION-strHeight)>>1), 
					20);
		}
		
		Image btnBg = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		sx = bgX+Resource.X_DRAFT_LEFT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DRAFT_LEFT_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "确定";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		sx = bgX+Resource.X_DRAFT_RIGHT_CONFIRM_BTN;
		sy = bgY+Resource.Y_DRAFT_RIGHT_CONFIRM_BTN;
		g.drawRegion(btnBg, 0, 0, Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 0, sx, sy, 20);
		ss = "返回";
		sx += ((Resource.W_CONFIRM_BTN-font.stringWidth(ss))>>1);
		sy += ((Resource.H_CONFIRM_BTN-strHeight)>>1);
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		if (curGroupIndex >= 3) {
			g.setColor(0XFFFF00);
			if (curConfirmIndex == 0) {
				DrawUtil.drawRect(g, bgX+Resource.X_DRAFT_LEFT_CONFIRM_BTN, bgY+Resource.Y_DRAFT_LEFT_CONFIRM_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
				
			}
			else {
				DrawUtil.drawRect(g, bgX+Resource.X_DRAFT_RIGHT_CONFIRM_BTN, bgY+Resource.Y_DRAFT_RIGHT_CONFIRM_BTN, 
						Resource.W_CONFIRM_BTN, Resource.H_CONFIRM_BTN, 2);
			}
		}
		
		int consume = 0;
		for (int i = 0; i < soldierCount.length; ++i) {
			consume += ((soldierCount[i]+100-1)/100)*engine.soldierList[i].getAmount();
		}
		int leftWealth = curCity.getWealth()-consume;
		
		ss = "城市剩余金币" + leftWealth;
		sx = bgX+261+((64-font.stringWidth(ss))>>1);
		sy = bgY+250;
		g.drawString(ss, sx, sy, 0);
		
	}

	private void showMilitartAffairsExpedition(Graphics g) {
		//showMapBg(g);
		//showMapTopInfo(g);
		showGeneralListUI(g);
	}

	private void showDevelopFarming(Graphics g) {
		showGeneralListUI(g);
	}

	private void showMarket(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showCitySubMenu(g);
	}

	private void showGeneral(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showCitySubMenu(g);
	}

	private void showMilitaryAffairs(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showCitySubMenu(g);
	}

	private void showDevelop(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showCitySubMenu(g);
	}

	private void showCitySubMenu(Graphics g) {
		int sx = 0, sy = 0;
		Image menuBg = Resource.loadImage(Resource.PIC_ID_CITY_MENU_BG);

		Font font = g.getFont();
		int fontH = font.getHeight();
		
		sx = Resource.X_CITY_MENU_BG;
		sy = Resource.Y_CITY_MENU_BG+cityMenuIndex*Resource.YSPACE_CITY_MENU_BG;
		g.drawRegion(menuBg, Resource.W_CITY_MENU_BG, 0, Resource.W_CITY_MENU_BG, Resource.H_CITY_MENU_BG, 0, sx, sy, 20);
		g.setColor(Resource.COLOR_ID_CITY_MENU_NORMAL_TEXT);
		g.drawString(Resource.STR_CITY_MENU[cityMenuIndex], 
				sx + ((Resource.W_CITY_MENU_BG-font.stringWidth(Resource.STR_CITY_MENU[cityMenuIndex]))>>1), 
				sy + ((Resource.H_CITY_MENU_BG-fontH)>>1), 
				20);
		
		Image subMenuBg = Resource.loadImage(Resource.PIC_ID_CITY_SUB_MENU_BG);
		int deltaH = (Resource.H_CITY_SUB_MENU_BG-fontH)>>1;
		int subMenuCount = Resource.STR_CITY_SUB_MENU[cityMenuIndex].length;
		int subMenuYInc = 0;
		sx += Resource.W_CITY_MENU_BG;
		for (int i = 0; i < subMenuCount; ++i) {
			g.drawRegion(subMenuBg, (citySubMenuIndex==i)?Resource.W_CITY_SUB_MENU_BG:0, 0, 
					Resource.W_CITY_SUB_MENU_BG, Resource.H_CITY_SUB_MENU_BG, 0, 
					sx, sy+subMenuYInc, 20);
			g.drawString(Resource.STR_CITY_SUB_MENU[cityMenuIndex][i], 
					sx+((Resource.W_CITY_SUB_MENU_BG-font.stringWidth(Resource.STR_CITY_SUB_MENU[cityMenuIndex][i]))>>1), 
					sy+subMenuYInc+deltaH, 20);
			subMenuYInc += Resource.H_CITY_SUB_MENU_BG;
		}
		
		Image bottom = Resource.loadImage(Resource.PIC_ID_BOTTOM_INFO_BG);
		String helpText = Resource.STR_CITY_SUB_MENU_HELP[cityMenuIndex][citySubMenuIndex];
		sx = 0;
		sy = engine.getScreenHeight()-bottom.getHeight();
		g.drawImage(bottom, sx, sy, 20);
		sx += ((bottom.getWidth()-font.stringWidth(helpText))>>1);
		sy += ((bottom.getHeight()-fontH)>>1);
		g.setColor(0XFFFFFF);
		g.drawString(helpText, sx, sy, 20);
	}

	private void showCity(Graphics g) {
		if (engine.isDebugMode()) {
			engine.addDebugUserMessage("1:金+1000; 2:粮+1000; 3:农+100; 4:商+100; 5:人+100; 6:忠+100");
		}
		showMapBg(g);
		showMapTopInfo(g);
		showCityMenu(g);
		showCityIntro(g);
	}

	private void showCityIntro(Graphics g) {
		Image introBg = Resource.loadImage(Resource.PIC_ID_CITY_INTRO_BG);
		int bgX = 307-20;
		int bgY = 171;
		if (introBg != null) {
			g.drawImage(introBg, bgX, bgY, 20);
		}
		Image plus = Resource.loadImage(Resource.PIC_ID_PLUS);
		int plusW = plus.getWidth();
		int plusH = plus.getHeight();
		City city = engine.cityList[curCityId];
		g.setColor(Resource.COLOR_ID_CITY_INTRO_TEXT);
		String ss = null;
		int sx = bgX+Resource.X_CITY_INTRO_INFO;
		int sy = bgY+Resource.Y_CITY_INTRO_INFO;
		Font font = g.getFont();
		int deltaH = (22-font.getHeight())>>1;
		
		//金币
		ss = Integer.toString(city.getWealth());
		g.drawString(ss, sx, sy+deltaH, 20);
		g.drawImage(plus, sx+Resource.XSPACE_INTRO_PLUS, sy, 20);
		if (curCityGroupIndex == 1 && curCityAttrIndex == CITY_ATTR_WEALTH) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_INTRO_PLUS, sy, plusW, plusH, 2, 0XFFFF00);
			g.setColor(Resource.COLOR_ID_CITY_INTRO_TEXT);
		}
		sy += Resource.YSPACE_CITY_INTRO_INFO;
		
		//粮草
		ss = Integer.toString(city.getProvisions());
		g.drawString(ss, sx, sy+deltaH, 20);
		g.drawImage(plus, sx+Resource.XSPACE_INTRO_PLUS, sy, 20);
		if (curCityGroupIndex == 1 && curCityAttrIndex == CITY_ATTR_PROVISIONS) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_INTRO_PLUS, sy, plusW, plusH, 2, 0XFFFF00);
			g.setColor(Resource.COLOR_ID_CITY_INTRO_TEXT);
		}
		sy += Resource.YSPACE_CITY_INTRO_INFO;
		
		//民忠
		ss = Integer.toString(city.getLoyalty());
		g.drawString(ss, sx, sy+deltaH, 20);
		g.drawImage(plus, sx+Resource.XSPACE_INTRO_PLUS, sy, 20);
		if (curCityGroupIndex == 1 && curCityAttrIndex == CITY_ATTR_LOYALTY) {
			DrawUtil.drawRect(g, sx+Resource.XSPACE_INTRO_PLUS, sy, plusW, plusH, 2, 0XFFFF00);
			g.setColor(Resource.COLOR_ID_CITY_INTRO_TEXT);
		}
		sy += Resource.YSPACE_CITY_INTRO_INFO;
		
		//武将数
		ss = Integer.toString(engine.getCityAllGeneralCount(curCityId));
		g.drawString(ss, sx, sy+deltaH, 20);
		sy += Resource.YSPACE_CITY_INTRO_INFO;
		
		//士兵数
		ss = Integer.toString(engine.getCitySoldierCount(curCityId));
		g.drawString(ss, sx, sy+deltaH, 20);
		sy += Resource.YSPACE_CITY_INTRO_INFO;
		
		//宝箱
		ss = Integer.toString(engine.getPrizeBoxCount());
		g.drawString(ss, sx, sy+deltaH, 20);
		
		Image btn = Resource.loadImage(Resource.PIC_ID_CONFIRM_BTN_BG);
		int btnOffset = 56;
		int btnW = 50;
		int halfBtnW = btnW>>1;
		int btnH = 27;
		g.drawRegion(btn, 0, 0, btnW>>1, btnH, 0, sx+btnOffset, sy, 20);
		g.drawRegion(btn, Resource.W_CONFIRM_BTN-halfBtnW, 0, halfBtnW, btnH, 0, sx+btnOffset+halfBtnW, sy, 20);
		ss = "打开";
		g.setColor(Resource.COLOR_ID_COMMON_BTN_TEXT);
		g.drawString(ss, 
				sx+btnOffset+((btnW-font.stringWidth(ss))>>1), 
				sy+((btnH-font.getHeight())>>1), 
				20);
		if (curCityGroupIndex == 1 && curCityAttrIndex == CITY_ATTR_BOX) {
			DrawUtil.drawRect(g, sx+btnOffset, sy, btnW, btnH, 2, 0XFFFF00);
			g.setColor(Resource.COLOR_ID_CITY_INTRO_TEXT);
		}
		
	}

	private void showCityMenu(Graphics g) {
		int sx = 0, sy = 0;
		Font font = g.getFont();
		Image menuBg = Resource.loadImage(Resource.PIC_ID_CITY_MENU_BG);
		g.setColor(Resource.COLOR_ID_CITY_MENU_NORMAL_TEXT);
		int menuCount = Resource.STR_CITY_MENU.length;
		int menuYInc = 0;
		int hilightMenuSrcX = Resource.W_CITY_MENU_BG;
		int hilightMenuXSpace = 16;
		if (curCityGroupIndex == 1) {
			hilightMenuSrcX = 0;
			hilightMenuXSpace = 0;
		}
		for (int i = 0; i < menuCount; ++i) {
			sx = Resource.X_CITY_MENU_BG-20;
			sy = Resource.Y_CITY_MENU_BG+menuYInc;
			menuYInc += Resource.YSPACE_CITY_MENU_BG;
			g.drawRegion(menuBg, (cityMenuIndex==i)?(hilightMenuSrcX):(0), 0, 
					Resource.W_CITY_MENU_BG, Resource.H_CITY_MENU_BG, 0, (cityMenuIndex==i)?sx-hilightMenuXSpace:sx, sy, 20);
			sx = sx + (Resource.W_CITY_MENU_BG-font.stringWidth(Resource.STR_CITY_MENU[i]))/2;
			sy = sy + (Resource.H_CITY_MENU_BG-font.getHeight())/2;
			g.drawString(Resource.STR_CITY_MENU[i], (cityMenuIndex==i)?sx-hilightMenuXSpace:sx, sy, 20);
		}
	}

	private void showSystem(Graphics g) {
		showMapBg(g);
		showMapTopInfo(g);
		showSystemMenu(g);
	}

	private void showSystemMenu(Graphics g) {
		systemMenu.show(g);
	}
	
	private void createSystemMenu() {
		systemMenu = new VerticalListMenu();
		systemMenu.setMenuBgImage(Resource.loadImage(Resource.PIC_ID_SYSTEM_MENU_BG), 197, 51);
		systemMenu.setItemsCoordinate(Resource.POS_SYSTEM_MENU);
	}
	
	private void showMapBg(Graphics g) {
		//画背景图片
		g.drawImage(Resource.loadImage(Resource.PIC_ID_MAP_BG), 0, 81, 20);
		//画旗帜
		drawFlag(g);
		//画光标
		drawCursor(g);
	}
	
	
	private void drawResStatusIcon(Graphics g) {
		City city = engine.cityList[curCityId];
		if (city.belongToPlayer()) {
			Image icon = null;
			Image grayIcon = null;
			//商业加成
			int iconX = 177;
			int iconY = 7;
			int iconW = 24;
			int iconH = 24;
			if (city.getCommerceEnhance() > 0) {
				Resource.freeImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_INVALID_ICON);
				icon = Resource.loadImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_ICON);
				g.drawImage(icon, iconX, iconY, 20);
			}
			else {
				grayIcon = Resource.getImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_INVALID_ICON);
				if (grayIcon == null) {
					icon = Resource.loadImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_ICON);
					grayIcon = ImageUtil.createGray(icon, 0, 0, iconW, iconH);
					Resource.setImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_INVALID_ICON, grayIcon);
					Resource.freeImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_ICON);
				}
				g.drawImage(grayIcon, iconX, iconY, 20);
			}
			
			//农业加成
			iconY = 38;
			if (city.getFarmingEnhance() > 0) {
				Resource.freeImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_INVALID_ICON);
				icon = Resource.loadImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_ICON);
				g.drawImage(icon, iconX, iconY, 20);
			}
			else {
				grayIcon = Resource.getImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_INVALID_ICON);
				if (grayIcon == null) {
					icon = Resource.loadImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_ICON);
					grayIcon = ImageUtil.createGray(icon, 0, 0, iconW, iconH);
					Resource.setImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_INVALID_ICON, grayIcon);
					Resource.freeImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_ICON);
				}
				g.drawImage(grayIcon, iconX, iconY, 20);
			}
		}
	}

	private void drawCursor(Graphics g) {
		if (cityCursor == null) {
			cityCursor = Resource.buildRotateCursor();
		}
		cityCursor.show(g, Resource.LAYOUT_CITY[curCityId][0]+4, Resource.LAYOUT_CITY[curCityId][1]-8);
	}

	private void drawFlag(Graphics g) {
		Image flag = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_FLAG);
		int flagSequence;
		if (flagFrame >= (NUM_FLAG_SEQUENCES>>1)) {
			flagSequence = flagFrame-(NUM_FLAG_SEQUENCES>>1);
		}
		else {
			flagSequence = flagFrame;
		}
		Image border = Resource.loadImage(Resource.PIC_ID_CITY_BORDER);
		Image mainBorder = Resource.loadImage(Resource.PIC_ID_MAIN_CITY_BORDER);
		int flagX, flagY;
		int flagW = 33;
		int flagH = 40;
		City[] cityList = engine.cityList;
		for (int i = 0; i < cityList.length; ++i) {
			int seigneurId = cityList[i].getSeigneurId();
			if (seigneurId >= 0) {
				flagX = Resource.LAYOUT_CITY[i][0]+8;
				flagY = Resource.LAYOUT_CITY[i][1]-4;
				if (cityList[i].belongToPlayer()) {
					if (i == 5 || i ==10 || i==14 || i==18) {
						g.drawImage(mainBorder, flagX-22, flagY+17, 20);
					}
					else {
						g.drawImage(border, flagX-18, flagY+19, 20);
					}
				}
				g.drawRegion(flag, flagSequence*flagW, seigneurId*flagH, 
						flagW, flagH, 0, flagX, flagY, 20);
			}
		}
	}

	private void showMapTopInfo(Graphics g) {
		//显示君主头像
		g.drawImage(Resource.loadImage(Resource.PIC_ID_MAP_TOP_BG), 0, 0, 20);
		Image head = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_HEAD);
		if (head != null) {
			int seigneurId = engine.cityList[curCityId].getSeigneurId();
			if (seigneurId >= 0) {
				g.drawRegion(head, 71*seigneurId, 0, 71, 71, 0, 0, 0, 20);
			}
		}
		
		City city = engine.cityList[curCityId];
		//显示君主
		int sx = 0, sy = 0;
		String ss = null;
		Font font = g.getFont();
		int fontH = font.getHeight();
		int infoX=120;
		int infoY=8;
		int infoW=52;
		int infoH=22;
		int deltaH = (infoH-fontH)>>1;
		if (city.belongToAnySeigneur()) {
			ss = city.getSeigneur().getName();
		}
		else {
			ss = "无";
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.setColor(Resource.COLOR_ID_MAP_CITY_INFO_TEXT);
		g.drawString(ss, sx, sy, 20);

		//显示城市名称
		infoY = 39;
		ss = city.getName();
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.setColor(Resource.COLOR_ID_MAP_CITY_INFO_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		//商业
		infoX = 242;
		infoY = 8;
		infoW = 54;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getCommerce());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.setColor(Resource.COLOR_ID_MAP_CITY_INFO_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		//农业
		infoY = 39;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getFarming());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.drawString(ss, sx, sy, 20);
		
		//金币
		infoX = 345;
		infoW = 57;
		infoY = 8;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getWealth());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.drawString(ss, sx, sy, 20);
		
		//粮草
		infoY = 39;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getProvisions());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.drawString(ss, sx, sy, 20);
		
		//人口
		infoX = 450;
		infoY = 8;
		infoW = 55;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getPopulation());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.drawString(ss, sx, sy, 20);
		
		//民忠
		infoY = 39;
		if (!city.belongToPlayer()) {
			ss = "????";
		}
		else {
			ss = Integer.toString(city.getLoyalty());
		}
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+deltaH;
		g.drawString(ss, sx, sy, 20);
		
		//显示年代
		infoX = 513;
		infoY = 5;
		infoW = 130;
		infoH = 27;
		ss = engine.getGameTimeStr(engine.month);
		sx = infoX+((infoW-font.stringWidth(ss))>>1);
		sy = infoY+((infoH-fontH)>>1);
		g.setColor(Resource.COLOR_ID_MAP_TOP_AGE_TEXT);
		g.drawString(ss, sx, sy, 20);
		
		//画状态图标
		drawResStatusIcon(g);
	}
	
	public void showMapTop(Graphics g) {
		//画背景图片
		g.drawImage(Resource.loadImage(Resource.PIC_ID_MAP_TOP_BG), 0, 0, 20);
		showMapTopInfo(g);
	}

	private void showNormal(Graphics g) {
		if (engine.isDebugMode()) {
			engine.addDebugUserMessage("1:胜利; 2:失败");
		}
		showMapBg(g);
		showMapTopInfo(g);
	}
	
	public void execute() {
		switch(state) {
		case STATE_START:
			executeStart();
			break;
		case STATE_GIFTS:
			executeGifts();
			break;
		case STATE_NORMAL: 
			executeNormal();
			break;
		case STATE_SYSTEM_MENU: 
			break;
		case STATE_GAME_MENU:
			executeGameMenu();
			break;
		case STATE_CITY_MENU: 
			executeCity();
			break;
		case STATE_ROUND_END:
			executeRoundEnd();
			break;
		case STATE_DEVELOP_MENU: 
			executeDevelop();
			break;
		case STATE_DEVELOP_FARMING: 
			break;
		case STATE_DEVELOP_COMMERCE: 
			break;
		case STATE_DEVELOP_RELIEF: 
			break;
		case STATE_MILITARY_MENU: 
			executeMilitaryAffairs();
			break;
		case STATE_MILITARY_DRAFT: 
			break;
		case STATE_MILITARY_EXPEDITION: 
			break;
		case STATE_MILITARY_DISTRIBUTE: 
			break;
		case STATE_EXPEDITION_TARGET:
			executeExpeditionTarget();
			break;
		case STATE_DISTRIBUTE_DETAIL:
			break;
		case STATE_MILITRAY_TRANSFER:
			break;
		case STATE_TRANSFER_TARGET:
			break;
		case STATE_GENERAL_MENU: 
			executeGeneral();
			break;
		case STATE_GENERAL_SEARCH: 
			break;
		case STATE_GENERAL_CONVALESCE: 
			break;
		case STATE_GENERAL_MOVE: 
			break;
		case STATE_GENERAL_AWARD:
			break;
		case STATE_GENERAL_MOVE_TARGE:
			break;
		case STATE_MARKET_MENU: 
			executeMarket();
			break;
		case STATE_MARKET_GRAIN_TRADE:
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void executeGifts() {
		Activity.processGrantGifts();
		state = STATE_NORMAL;
	}

	private void executeStart() {
		if (slideState == 0) {
			state = STATE_GIFTS;
		}
	}

	private void executeGameMenu() {
		cityCursor.nextFrame();
	}

	private void playerTurn() {
		for (int i = 0; i < engine.generalList.length; ++i) {
			General general = engine.generalList[i];
			if (general.belongToPlayer() && general.isIdentityNormal()) {
				if (!general.isStateNormal()) {
					if (general.hasTaskFarming()) {
						Rule.executeFarming(general);
					}
					else if (general.hasTaskCommerce()) {
						Rule.executeCommerce(general);
					}
					else if (general.hasTaskRelief()) {
						Rule.executeRelief(general);
					}
					else if (general.hasTaskExpedition()) {
						Rule.executeExpedition(general);
					}
					else if (general.hasTaskSearch()) {
						Rule.executeSearch(general);
					}
					else if (general.hasTaskConvalesce()) {
						Rule.executeConvalesce(general);
					}
					else if (general.hasTaskMove()) {
						Rule.executeMove(general);
					}
					else {
						//null
					}
				}
			}
		}
		Rule.judgeForRevolt();
		City.execTransferTask();
	}
	
	private void aiTurn() {
		int seigneurId = seigneurList[currentTurn];
		Seigneur seigneur = engine.seigneurList[seigneurId];
		
		General[] generalList = engine.generalList;
		City[] cityList = engine.cityList;
		short[] cityCache = engine.cityCache;
		short[] generalCache = engine.generalCache;
		General general = null;
		City city = null;
		
		/*清除缓存*/
		/*cityCache缓存用于存放城池中的武将个数*/
		for (int i = 0; i < cityCache.length; ++i) {
			cityCache[i] = 0;
		}
		/*generalCache缓存用于存放城池中武将的智力平均值和魅力平均值*/
		for (int i = 0; i < generalCache.length; ++i) {
			generalCache[i] = 0;
		}
		int cacheLen = cityList.length;
		/*统计城池武将数量, 为开发与资源调度做准备*/
		for (int i = 0; i < generalList.length; ++i) {
			general = generalList[i];
			if (general.getSeigneurId() == seigneurId
					&&general.isIdentityNormal() 
					&&general.canUseMagic()
					&&general.getStamina()>Rule.STAMINA_CONSUME_FOR_COMMERCE) {
				int cityId = general.getCityId();
				++cityCache[cityId];
				generalCache[cityId] += general.getWit();
				generalCache[cacheLen+cityId] += general.getCharm();
				general.decStamina(Rule.STAMINA_CONSUME_FOR_COMMERCE);
				general.assignTaskCommerce();
			}
		}
		
		/*计算城池武将的智力平均值和魅力平均值*/
		/*1：城池的属性增长，搜索，增长的值与城池中的武将个数及城池中武将的智力平均值，魅力平均值有关*/
		int dcacheLen = cacheLen<<1;
		for (int i = 0; i < cityList.length; ++i) {
			if (cityList[i].getSeigneurId()==seigneurId && cityCache[i]>0) {
				/*计算智力平均值*/
				generalCache[i] /= cityCache[i];
				/*计算魅力平均值*/
				generalCache[cacheLen+i] /= cityCache[i];
				/*计算可开发的武将个数*/
				generalCache[dcacheLen+i] = (short)((cityCache[i]+3)>>2);
				
				Rule.aiExecuteCommerce(cityList[i], generalCache[dcacheLen+i], generalCache[i]);
				Rule.aiExecuteFarming(cityList[i], generalCache[dcacheLen+i], generalCache[i]);
				Rule.aiExecuteRelief(cityList[i], generalCache[dcacheLen+i], generalCache[cacheLen+i]);
				Rule.aiExecuteSearch(cityList[i], generalCache[dcacheLen+i], generalCache[cacheLen+i]);
			}
		}
		
		/*2: 武将增兵及修养*/
		for (int i = 0; i < generalList.length; ++i) {
			general = generalList[i];
			if (general.getSeigneurId() == seigneurId) {
				Rule.aiExecuteConvalesce(general);
				Rule.aiExecuteDraft(general);
			}
		}
		
		/*3: 电脑进攻*/
		for (int i = 0; i < cityList.length; ++i) {
			city = cityList[i];
			if (city.getSeigneurId() == seigneurId) {
				Rule.aiExecuteDistribute(city);
				Rule.aiExecuteExpedition(city);
			}
		}
		
		/*4：战争后调动城池的资源，宏观调控*/
		Rule.aiTransferResource(seigneur);
	}
	
	/*
	private void drawNumber(Graphics g, int num, int x, int y, int gap) {
		Image img = Resource.loadImage(Resource.PIC_ID_NUMBER);
		int imgW = 13;
		int imgH = 17;
		int sx = x;
		int sy = y;
		if (num >= 0) {
			g.drawRegion(img, imgW*10, 0, imgW, imgH, 0, sx, sy, 20);
		}
		else {
			g.drawRegion(img, imgW*11, 0, imgW, imgH, 0, sx, sy, 20);
		}
		sx += imgW+1;
		
		int mod = num%10;
		int div = num/10;
		int pos = 0;
		tmpNumber[pos++] = (byte)mod;
		while (div > 0) {
			mod = div%10;
			div = div/10;
			tmpNumber[pos++] = (byte)mod;
		}
		--pos;
		while (pos >= 0) {
			g.drawRegion(img, imgW*tmpNumber[pos], 0, imgW, imgH, 0, sx, sy, 20);
			sx += imgW+1;
			--pos;
		}
	}*/

	private void executeRoundEnd() {
		flagFrame = (byte)((flagFrame+1)%NUM_FLAG_SEQUENCES);
		waitCursorFrame = (byte)((++waitCursorFrame)%NUM_WAIT_CURSOR_SEQUENCES);
		
		if (currentTurn == -1) {
			raiseCityResource();
		}
		
		if (currentTurn == -2) {
			currentTurn = 0;
		}
		
		if (currentTurn < 0) {
			return;
		}
		
		if (!timePass(1000)) {
			return;
		}
		
		if (currentTurn < seigneurList.length) {
			if (seigneurList[currentTurn] == engine.playerSeigneur) {
				playerTurn();
			}
			else {
				aiTurn();
			}
			++currentTurn;
		}
		
		if (currentTurn >= seigneurList.length) {
			/*城池增强状态减少*/
			if (currentTurn == seigneurList.length) {
				++currentTurn;
				return;
			}
			
			decPlayerCityEnhanceStatus();
			int result = engine.checkGameResult();
			if (result == -1) {	//游戏失败
				loseGame();
				clear();
				engine.gotoStateMain();
			}
			else if(result == 1) {//游戏胜利
				clear();
				winGame();
				engine.gotoStateMain();
			}
			else {
				Rule.freeGeneralMove();
				Rule.resumeGeneralStatus();
				Resource.freeImage(Resource.PIC_ID_WAIT);
				++engine.month;

				engine.pushProp();
				state = popState();
				String tipText = "新的回合开始了";
				PopupText pt = Resource.buildPopupText();
				pt.setText(tipText);
				pt.popup();
			}
		}
	}

	private void decPlayerCityEnhanceStatus() {
		City playerSeigneurCity = engine.getPlayerSeigneurCity();
		boolean attackEnhanceExist = playerSeigneurCity.isAttackEnhance();
		boolean defenseEnhanceExist = playerSeigneurCity.isDefenseEnhance();
		boolean farmingEnhanceExist = playerSeigneurCity.isFarmingEnhance();
		boolean commerceEnhanceExist = playerSeigneurCity.isCommerceEnhance();
		for (int i = 0; i < engine.cityList.length; ++i) {
			if (engine.cityList[i].belongToPlayer()) {
				engine.cityList[i].decEnhance();
			}
		}
		if (attackEnhanceExist && !playerSeigneurCity.isAttackEnhance()) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("您的武神旗效果已过期");
			pt.setWaitMillisSeconds(3000);
			pt.popup();
		}
		if (defenseEnhanceExist && !playerSeigneurCity.isDefenseEnhance()) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("您的武侯旗效果已过期");
			pt.setWaitMillisSeconds(3000);
			pt.popup();
		}
		if (farmingEnhanceExist && !playerSeigneurCity.isFarmingEnhance()) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("您的齐民要术效果已过期");
			pt.setWaitMillisSeconds(3000);
			pt.popup();
		}
		if (commerceEnhanceExist && !playerSeigneurCity.isCommerceEnhance()) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("您的聚宝盆效果已过期");
			pt.setWaitMillisSeconds(3000);
			pt.popup();
		}
	}

	private void raiseCityResource() {
		if (engine.month % 3 == 2) {
			short[] cache = engine.generalCache;
			City[] cityList = engine.cityList;
			int len2 = cityList.length;
			int inc, sx, sy;
			Image res = Resource.loadImage(Resource.PIC_ID_RESOURCE_ICON);
			int resW = res.getWidth()>>1;
			int resH = res.getHeight();
			for (int i = 0; i < cityList.length; ++i) {
				City city = cityList[i];
				if (city.getSeigneurId() >= 0) {
					if (resourcePaintFrame == 0) {
						inc = Rule.raiseWealth(city);
						cache[i] = (short)inc;
						inc = Rule.raiseProvisions(city);
						cache[len2+i] = (short)inc;
						Rule.raisePopulation(city);
					}
					if (city.belongToPlayer()) {
						sx = Resource.LAYOUT_CITY[i][0]-resW;
						sy = Resource.LAYOUT_CITY[i][1]+16;
						if (resourcePaintFrame >= 12) {
							sy -= (resourcePaintFrame-8)<20?(resourcePaintFrame-8):20;
						}
						Image img = Resource.loadImage(Resource.PIC_ID_NUMBER);
						/*显示金币增长*/
						engine.getGraphics().drawRegion(res, resW, 0, resW, resH, 0, sx, sy, 20);
						if (resourcePaintFrame >= 2) {
							DrawUtil.drawNumberWithSymbol(engine.getGraphics(), img, cache[i], sx+resW, sy, 1);
						}
						sy += 20;
						/*粮草金币增长*/
						engine.getGraphics().drawRegion(res, 0, 0, resW, resH, 0, sx, sy, 20);
						if (resourcePaintFrame >= 2) {
							DrawUtil.drawNumberWithSymbol(engine.getGraphics(), img, cache[len2+i], sx+resW, sy, 1);
						}
					}
				}
			}
		}
		else {
			resourcePaintFrame = 100;
		}
		
		if (resourcePaintFrame++ > 25) {
			currentTurn = -2;
		}
	}

	private void executeExpeditionTarget() {
		cityCursor.nextFrame();
	}

	private void executeMarket() {
		cityCursor.nextFrame();
	}

	private void executeGeneral() {
		cityCursor.nextFrame();
	}

	private void executeMilitaryAffairs() {
		cityCursor.nextFrame();
	}

	private void executeDevelop() {
		cityCursor.nextFrame();
	}

	private void executeCity() {
		cityCursor.nextFrame();
	}

	private void executeNormal() {
		flagFrame = (byte)((flagFrame+1)%NUM_FLAG_SEQUENCES);
		cityCursor.nextFrame();
		
		int result = engine.checkGameResult();
		if (result == -1) {	//游戏失败
			loseGame();
			clear();
			engine.gotoStateMain();
		}
		else if(result == 1) {//游戏胜利
			clear();
			winGame();
			engine.gotoStateMain();
		}
		else {
			if (engine.isDebugMode()) {
				if (__DEBUG_WIN) {
					clear();
					winGame();
					engine.gotoStateMain();
					__DEBUG_WIN = false;
				}
				else if (__DEBUG_LOSE) {
					loseGame();
					clear();
					engine.gotoStateMain();
					__DEBUG_LOSE = false;
				}
			}
		}
	}
	
	private String getPlayDurationStr(int seconds) {
		int hour = seconds/3600;
		int min = (seconds%3600)/60;
		return hour+"小时"+min+"分";
	}
	
	private void winGame() {
		Graphics g = engine.getGraphics();
		Image win = Resource.loadImage(Resource.PIC_ID_WIN);
		g.drawImage(win, 0, 0, 20);
		
		Image head = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_HEAD);
		g.drawRegion(head, 71*(engine.playerSeigneur), 0, 71, 71, 0, 20, 78, 20);
		
		/*君主名称*/
		int sx = 287;
		int sy = 89;
		String ss = null;
		String remark = null;
		ss = engine.seigneurList[engine.playerSeigneur].getName();
		remark = ss;
		g.drawString(ss, sx, sy, 20);
		sy += 25;

		ss = engine.getGameTimeStr(engine.month);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		sy += 25;
		
		int generals = engine.getSeigneurGeneralCount(engine.playerSeigneur);
		ss = Integer.toString(generals);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		sy += 25;
		
		int soldiers = engine.getSeigneurSoldierCount(engine.playerSeigneur);
		ss = Integer.toString(soldiers);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		
		sx += 217;
		sy = 89;
		int wealth = engine.getSeigneurWealth(engine.playerSeigneur);
		ss = Integer.toString(wealth);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		sy += 25;
		
		int provisions = engine.getSeigneurProvisions(engine.playerSeigneur);
		ss = Integer.toString(provisions);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		sy += 25;
		
		int curPlayDuration = (int)(System.currentTimeMillis()-engine.gameStartMillis)/1000;
		engine.playDuration += curPlayDuration;
		ss = getPlayDurationStr(engine.playDuration);
		remark += ";"+ss;
		g.drawString(ss, sx, sy, 20);
		
		String text = Resource.LoadString("/txt/Seigneur"+engine.playerSeigneur+".txt");
		if (text != null) {
			g.setColor(0XFFFFFF);
			TextView.showMultiLineText(g, text, 1, 40, 223, 562, 272);
		}
		engine.flushGraphics();
		Wait.waitForKey(engine.getKeyState());
		
		int scores = Rule.calcScores(engine.playDuration, generals, soldiers, wealth, provisions);
		if (engine.pushAttainment(scores, remark) == 0) {
			PopupText pt = Resource.buildPopupText();
			pt.setText("您的游戏成就已经加入数据库,欲知自己的排名,敬请关注风云榜!");
			pt.popup();
		}
		Resource.freeImage(Resource.PIC_ID_WIN);
	}
	
	private void loseGame() {
		PopupText pt = Resource.buildPopupText();
		pt.setText("在惨烈的战争中，你最终失败了，请不要泄气，再来一次！");
		pt.popup();
	}
	
	public void init() {
		__DEBUG_WIN = false;
		__DEBUG_LOSE = false;
		state = STATE_START;
		slideFrame = 0;
		slideState = 1;
		curCityId = engine.getPlayerSeigneurCityId();
		int seigneurNum = Resource.STR_SEIGNEUR_NAME.length;
		if (seigneurList == null) {
			seigneurList = new byte[seigneurNum];
		}
		seigneurList[0] = (byte)engine.playerSeigneur;
		int pos = 1;
		for (int i = 0; i < seigneurNum; ++i) {
			if (i != engine.playerSeigneur) {
				seigneurList[pos++] = (byte)i;
			}
		}
	}
	
	public void clear() {
		Resource.freeImage(Resource.PIC_ID_MAP_TOP_BG);
		Resource.freeImage(Resource.PIC_ID_MAP_BG);
		Resource.freeImage(Resource.PIC_ID_CITY_INTRO_BG);
		Resource.freeImage(Resource.PIC_ID_CITY_MENU_BG);
		Resource.freeImage(Resource.PIC_ID_CITY_SUB_MENU_BG);
		Resource.freeImage(Resource.PIC_ID_BOTTOM_INFO_BG);
		Resource.freeImage(Resource.PIC_ID_SEIGNEUR_FLAG);
		//Resource.freeImage(Resource.PIC_ID_SEIGNEUR_HEAD);
		Resource.freeImage(Resource.PIC_ID_SYSTEM_MENU_BG);
		Resource.freeImage(Resource.PIC_ID_CITY_BORDER);
		Resource.freeImage(Resource.PIC_ID_MAIN_CITY_BORDER);
		Resource.freeImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_COMMERCE_ENHANCE_INVALID_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_FARMING_ENHANCE_INVALID_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_ATTACK_ENHANCE_INVALID_ICON);
		Resource.freeImage(Resource.PIC_ID_CITY_DEFENSE_ENHANCE_INVALID_ICON);
		Resource.freeImage(Resource.PIC_ID_AWARD_BG);
		Resource.freeImage(Resource.PIC_ID_AWARD_PROP_LIST_ARROW);
		Resource.freeImage(Resource.PIC_ID_WAIT);
		Resource.freeImage(Resource.PIC_ID_DRAFT_BG);
		Resource.freeImage(Resource.PIC_ID_DISTRIBUTE_BG);
		Resource.freeImage(Resource.PIC_ID_TRADE_BG);
		Resource.freeImage(Resource.PIC_ID_TRANSFER_BG);
		if (cityCursor != null) {
			Resource.clearRotateCursor();
			cityCursor = null;
		}
		if (generalListUI != null) {
			generalListUI.clearResource();
			generalListUI = null;
		}
	}
	
}
