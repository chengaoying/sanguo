package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.itvgame.model.GameAttainment;
import cn.ohyeah.itvgame.model.GameRanking;
import cn.ohyeah.stb.game.Configurations;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.ui.ISprite;
import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.VerticalListMenu;
import cn.ohyeah.stb.util.DateUtil;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;

public class StateMain {
	private static final byte STATE_MENU = 0;
	private static final byte STATE_START = 1;
	private static final byte STATE_FETCH_RANK = 2;
	private static final byte STATE_RANK = 3;
	
	private static final byte STATE_START_INTRO = 0;
	private static final byte STATE_START_CHOICE =  1;
	
	public static final byte MAIN_MENU_START = 0;
	public static final byte MAIN_MENU_LOAD = 1;
	public static final byte MAIN_MENU_RANK = 2;
	public static final byte MAIN_MENU_HELP = 3;
	public static final byte MAIN_MENU_EXIT = 4;
	
	private NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	
	private byte groupIndex;
	private byte state;
	private byte startState;
	private byte choiceIndex;
	private byte hilightMainMenuIndex;
	
	private VerticalListMenu mainMenu;
	private ISprite choiceCursor;
	
	private GameRanking[] rankingList;
	private GameAttainment attainment;
	
	public void handle(KeyState key) {
		switch(state) {
		case STATE_MENU:
			handleMenu(key);
			break;
		case STATE_START: 
			handleStart(key);
			break;
		case STATE_FETCH_RANK:
			break;
		case STATE_RANK:
			handleRank(key);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void handleRank(KeyState key) {
		if (key.containsAndRemove(KeyCode.OK|KeyCode.NUM0)) {
			state = STATE_MENU;
			rankingList = null;
			attainment = null;
		}
	}

	private void handleMenu(KeyState key) {
		if (!Configurations.getInstance().isFavorWayNonsupport()) {
			if (key.containsAndRemove(KeyCode.LEFT)) {
				if (groupIndex == 0) {
					groupIndex = 1;
					hilightMainMenuIndex = (byte)mainMenu.getHilightIndex();
					mainMenu.setHilightIndex(-1);
				}
			}
			
			if (key.containsAndRemove(KeyCode.RIGHT)) {
				if (groupIndex == 1) {
					groupIndex = 0;
					mainMenu.setHilightIndex(hilightMainMenuIndex);
				}
			}
		}
		
		if (groupIndex == 0) {
			if (key.containsAndRemove(KeyCode.UP)) {
				if (mainMenu != null) {
					mainMenu.prevItem();
				}
			}
			if (key.containsAndRemove(KeyCode.DOWN)) {
				if (mainMenu != null) {
					mainMenu.nextItem();
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (groupIndex == 0) {
				if (mainMenu != null) {
					switch(mainMenu.getHilightIndex()) {
					case MAIN_MENU_START: 
						if (!prepareStart()) {
							return;
						}
						clearMenuRes();
						if (choiceCursor == null) {
							choiceCursor = Resource.buildRotateCursor();
						}
						choiceIndex = 0;
						state = STATE_START;
						startState = STATE_START_CHOICE;
						break;
					case MAIN_MENU_LOAD: 
						clearMenuRes();
						StateRecord stateRecord = new StateRecord();
						int result = stateRecord.popup(StateRecord.TYPE_LOAD);
						if (result == 0) {
							clearStartRes();
							engine.gotoStateMap();
						}
						break;
					case MAIN_MENU_RANK:
						state = STATE_FETCH_RANK;
						break;
					case MAIN_MENU_HELP: 
						clearMenuRes();
						StateHelp help = new StateHelp();
						help.popup();
						break;
					case MAIN_MENU_EXIT: 
						PopupConfirm confirm = Resource.buildPopupConfirm();
						confirm.setText("确定退出游戏吗？");
						switch(confirm.popup()) {
						case 0: 
							engine.setExit();
							break;
						case 1: 
							state = STATE_MENU;
							break;
						default:break;
						}
						break;
					default: break;
					}
				}
			}
			else {
				addfavorite();
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			PopupConfirm confirm = Resource.buildPopupConfirm();
			confirm.setText("确定退出游戏吗？");
			switch(confirm.popup()) {
			case 0: 
				engine.setExit();
				break;
			case 1: 
				state = STATE_MENU;
				break;
			default:break;
			}
		}
	}
	
	private void addfavorite() {
		PopupText pt = Resource.buildPopupText();
		try {
			ServiceWrapper sw = engine.getServiceWrapper();
			sw.addFavoritegd();
			if (sw.isServiceSuccessful()) {
				pt.setText("添加收藏夹成功");
			}
			else {
				pt.setText("添加收藏夹失败, 原因: "+sw.getServiceMessage());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			pt.setText("添加收藏夹失败，请稍后重试");
		}
		pt.popup();
	}

	private boolean prepareStart() {
		return true;
	}
	
	private void clearMenuRes() {
		Resource.freeImage(Resource.PIC_ID_MAIN_BG);
		Resource.freeImage(Resource.PIC_ID_MAIN_MENU_BG);
		if (mainMenu != null) {
			hilightMainMenuIndex = (byte)mainMenu.getHilightIndex();
			mainMenu.setMenuBgImage(null);
			mainMenu = null;
		}
		Resource.freeImage(Resource.PIC_ID_COLLECT);
	}
	
	private void initMenuRes() {
		if (mainMenu == null) {
			mainMenu = new VerticalListMenu();
			mainMenu.setMenuBgImage(Resource.loadImage(Resource.PIC_ID_MAIN_MENU_BG), 197, 51);
			mainMenu.setItemsCoordinate(Resource.POS_MAIN_MENU);
			mainMenu.setHilightIndex(hilightMainMenuIndex);
		}
	}

	private void handleStart(KeyState KeyState) {
		switch (startState) {
		case STATE_START_INTRO: break;
		case STATE_START_CHOICE: 
			handleStartChoice(KeyState);
			break;
		default:
			throw new RuntimeException("未知的状态, startState="+state);
		}
	}
	
	private void handleStartChoice(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (choiceIndex >= 3 && choiceIndex <= 5) {
				choiceIndex -= 3;
			}
		}
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (choiceIndex >= 0 && choiceIndex <= 2) {
				choiceIndex += 3;
			}
		}
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (choiceIndex > 0) {
				--choiceIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (choiceIndex < 5) {
				++choiceIndex;
			}
		}
		if (key.containsAndRemove(KeyCode.OK)) {
			clearStartRes();
			engine.playerSeigneur = choiceIndex;
			showLoad();
			engine.initNewGame();
			engine.gotoStateMap();
		}
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			clearStartRes();
			initMenuRes();
			state = STATE_MENU;
		}
	}
	
	private void showLoad() {
		int sx, sy;
		String ss = "正在加载资源， 请稍后......";
		SGraphics g = engine.getSGraphics();
		sx = (engine.getScreenWidth()-engine.getFont().stringWidth(ss))/2;
		sy = (engine.getScreenHeight()-engine.getFont().getHeight())/2;
		g.setClip(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
		g.setColor(0);
		g.fillRect(0, 0, engine.getScreenWidth(), engine.getScreenHeight());
		g.setColor(-1);
		g.drawString(ss, sx, sy, 0);
		engine.flushGraphics();
	}

	private void clearStartRes() {
		Resource.freeImage(Resource.PIC_ID_CHOICE_BG);
		Resource.clearRotateCursor();
		choiceCursor = null;
	}

	public void show(SGraphics g) {
		switch(state) {
		case STATE_MENU:
			showMenu(g);
			break;
		case STATE_START: 
			showStart(g);
			break;
		case STATE_FETCH_RANK:
			showFetchRank(g);
			break;
		case STATE_RANK:
			showRank(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void showRankBg(SGraphics g) {
		Image title = Resource.loadImage(Resource.PIC_ID_RANK_TITLE);
		Image item = Resource.loadImage(Resource.PIC_ID_RANK_ITEM);
		Image foot = Resource.loadImage(Resource.PIC_ID_RANK_FOOT);
		
		int titleX = (engine.getScreenWidth()-title.getWidth())>>1;
		int titleY = 30;
		g.drawImage(title, titleX, titleY, 20);
		
		int itemX = titleX + ((title.getWidth()-item.getWidth())>>1);
		int itemY = titleY+title.getHeight();
		for (int i = 0; i < 11; ++i) {
			g.drawImage(item, itemX, itemY, 20);
			itemY += item.getHeight();
		}
		
		int footX = titleX + ((title.getWidth()-foot.getWidth())>>1);
		int footY = itemY;
		g.drawImage(foot, footX, footY, 20);
	}
	
	private void showFetchRank(SGraphics g) {
		showRankBg(g);
		String info = "正在读取排行榜，请稍后......";
		PopupText pt = Resource.buildPopupTextWithoutBtn();
		pt.setText(info);
		pt.show(g);
		engine.flushGraphics();
	}

	private void showRank(SGraphics g) {
		
		Image title = Resource.loadImage(Resource.PIC_ID_RANK_TITLE);
		Image item = Resource.loadImage(Resource.PIC_ID_RANK_ITEM);
		Image foot = Resource.loadImage(Resource.PIC_ID_RANK_FOOT);
		
		int titleX = (engine.getScreenWidth()-title.getWidth())>>1;
		int titleY = 30;
		g.drawImage(title, titleX, titleY, 20);
		
		Font font = g.getFont();
		int itemX = titleX + ((title.getWidth()-item.getWidth())>>1);
		int itemY = titleY+title.getHeight();
		int deltaH = (item.getHeight()- font.getHeight())>>1;
		int sx = 0, sy = 0;
		String ss = null;
		int rankX = 43-20,  idX = 43+80, scoresX = 188+50, seiX = 309+30, timeX = 467+10;
		for (int i = 0; i < 11; ++i) {
			g.drawImage(item, itemX, itemY, 20);
			sy = itemY+deltaH;
			itemY += item.getHeight();
			if (i == 0) {
				/*显示表头*/
				rankX += itemX;
				idX += itemX;
				scoresX += itemX;
				seiX += itemX;
				timeX += itemX;
				
				g.setColor(165, 173, 64);
				g.drawString("游戏排名", rankX, sy, 20);
				g.drawString("用户ID", idX, sy, 20);
				g.drawString("积分", scoresX, sy, 20);
				g.drawString("所用主公", seiX, sy, 20);
				g.drawString("统一时间", timeX, sy, 20);
				
				/*计算表数据坐标*/
				rankX += font.stringWidth("游戏")-2;
				idX += ((font.stringWidth("用户ID")-font.stringWidth("0234**98"))>>1);
				scoresX += ((font.stringWidth("积分")-font.stringWidth("34567"))>>1);
				seiX += ((font.stringWidth("所用主公")-font.stringWidth("曹操"))>>1);
				timeX += ((font.stringWidth("统一时间")-font.stringWidth("2012-03-24 23:58"))>>1);
			}
			else {
				if (rankingList != null) {
					if (i <= rankingList.length) {
						g.setColor(0XFFFF00);
						g.drawString(Integer.toString(rankingList[i-1].getRanking()), rankX, sy, 20);
						g.setColor(0XFFFFFF);
						String userId = rankingList[i-1].getUserId();
						int userIdLen = userId.length();
						g.drawString(userId.substring(0, 4)+"**"+userId.substring(userIdLen-2), idX, sy, 20);
						g.drawString(Integer.toString(rankingList[i-1].getScores()), scoresX, sy, 20);
						String remark = rankingList[i-1].getRemark();
						g.drawString(remark.substring(0, remark.indexOf(";")), seiX, sy, 20);
						String time = rankingList[i-1].getTime();
						g.drawString(time.substring(0, time.length()-3), timeX, sy, 20);
					}
				}
			}
		}
		
		int footX = titleX + ((title.getWidth()-foot.getWidth())>>1);
		int footY = itemY;
		g.drawImage(foot, footX, footY, 20);
		g.setColor(0XFFFF00);
		if (attainment != null) {
			ss = Integer.toString(attainment.getRanking());
		}
		else {
			ss = "榜上无名";
		}
		sx = footX+175+((98-font.stringWidth(ss))>>1);
		sy = footY+9+((33-font.getHeight())>>1);
		g.drawString(ss, sx, sy, 20);
		
		sx = footX+508;
		sy = footY+6;
		DrawUtil.drawRect(g, sx, sy, 89, 39, 3);
		
	}

	private void showMenu(SGraphics g) {
		Image bgImg = Resource.loadImage(Resource.PIC_ID_MAIN_BG);
		g.drawImage(bgImg, 0, 0, 0);
		if (mainMenu == null) {
			initMenuRes();
		}
		mainMenu.show(g);
		
		if (!Configurations.getInstance().isFavorWayNonsupport()) {
			Image collect = Resource.loadImage(Resource.PIC_ID_COLLECT);
			int sx = 12, sy = 405;
			if (collect != null) {
				g.drawImage(collect, sx, sy, 20);
			}
			if (groupIndex == 1) {
				DrawUtil.drawRect(g, sx, sy, collect.getWidth(), collect.getHeight(), 3, 0XFFFF00);
			}
		}
	}
	
	private void showStart(SGraphics g) {
		switch (startState) {
		case STATE_START_INTRO: 
			break;
		case STATE_START_CHOICE: 
			showStartChoice(g);
			break;
		default:
			throw new RuntimeException("未知的状态, startState="+startState);
		}
	}
	
	private void showStartChoice(SGraphics g) {
		g.drawImage(Resource.loadImage(Resource.PIC_ID_CHOICE_BG), 0, 0, 0);
		choiceCursor.show(g, Resource.POS_SEIGNEUR_CHOICE[choiceIndex][0], 
				Resource.POS_SEIGNEUR_CHOICE[choiceIndex][1]);
		String info = Resource.STR_SEIGNEUR_NAME[choiceIndex]
		+"  城池X"+Resource.NUM_SEIGNEUR_CITYS[choiceIndex]
		 +"  武将X"+Resource.NUM_SEIGNEUR_GENERALS[choiceIndex];
		g.setColor(0XFFFFFF);
		Font font = g.getFont();
		g.drawString(info, (engine.getScreenWidth()-font.stringWidth(info))>>1, 456, 0);
	}

	public void execute() {
		switch(state) {
		case STATE_MENU:break;
		case STATE_START: 
			executeStart();
			break;
		case STATE_FETCH_RANK:
			executeFetchRank();
			break;
		case STATE_RANK:break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private boolean fetchRank() {
		PopupText pt = Resource.buildPopupText();
		try {
			java.util.Date loginTime = engine.getEngineService().getLoginTime();
			java.util.Date start = DateUtil.getMonthStartTime(loginTime);
			java.util.Date end = DateUtil.getNextMonthStartTime(loginTime);
			ServiceWrapper sw = engine.getServiceWrapper();
			rankingList = sw.queryRankingList(start, end, 0, 10);
			if (!sw.isServiceSuccessful()) {
				pt.setText("读取排行榜失败，原因："+sw.getServiceMessage());
				pt.popup();
				return false;
			}
			int attainmentId = engine.calcAttainmentId(loginTime);
			attainment = sw.readAttainment(attainmentId, start, end);
			return true;
		}
		catch (Exception e) {
			e.printStackTrace();
			pt.setText("读取排行榜失败，原因："+e.getMessage());
			pt.popup();
			return false;
		}
	}
	
	private void executeFetchRank() {
		if (fetchRank()) {
			state = STATE_RANK;
		}
		else {
			rankingList = null;
			attainment = null;
			state = STATE_MENU;
		}
		
	}

	private void executeStart() {
		switch (startState) {
		case STATE_START_INTRO: 
			break;
		case STATE_START_CHOICE: 
			executeStartChoice();
			break;
		default:
			throw new RuntimeException("未知的状态, startState="+startState);
		}
	}

	private void executeStartChoice() {
		choiceCursor.nextFrame();
	}

	public void init() {
		state = STATE_MENU;
		initMenuRes();
	}

	public void clear() {
		
	}

}
