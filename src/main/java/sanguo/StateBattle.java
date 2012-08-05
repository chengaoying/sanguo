package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.game.Sprite;

import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.ImageUtil;
import cn.ohyeah.stb.ui.ScrollBar;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;
import cn.ohyeah.stb.util.RandomValue;

public class StateBattle {
	private static final byte STATE_FIGHTER_LIST = 0;
	private static final byte STATE_BATTLE_FIELD = 1;
	private static final byte STATE_FIGHT_MENU = 2;
	private static final byte STATE_FIGHT_END = 3;
	
	private static final byte FIGHT_MENU_BACK = 0;		//返回战场
	private static final byte FIGHT_MENU_SKILL = 1;		//武将计
	private static final byte FIGHT_MENU_WAIT = 2;		//待命
	private static final byte FIGHT_MENU_ASSAULT = 3;	//进攻
	private static final byte FIGHT_MENU_RETREAT = 4;	//撤退
	
	private static final byte MAGIC_CD_LIMIT = 72		/*48*/;
	
	private NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private byte state;
	private byte fightMenuIndex;
	private byte attackIndex;
	private byte defenseIndex;
	
	private short[] attackList;
	private short[] defenseList;
	private byte[] failedFlags;
	
	private boolean attackedByPlayer;
	private boolean magicAttack;
	private boolean canRetreat;
	
	private int battleFrame;
	private int magicFrame;
	private int perFrameHurtByMagic;
	
	private General magicGeneral;
	private General attackGeneral;
	private General defenseGeneral;
	
	private short attackArmyAttack;
	private short attackArmyDefense;
	private short attackArmySoldiers;

	private short defenseArmyAttack;
	private short defenseArmyDefense;
	private short defenseArmySoldiers;
	
	private SoldierSprite[] attackSprites;
	private SoldierSprite[] defenseSprites;
	private ScrollBar attackScroll;
	private ScrollBar defenseScroll;
	private byte curAttackListPage;
	private byte curAttackListItem;
	private byte curDefenseListPage;
	private byte curDefenseListItem;
	
	private byte attackCdLimit;
	private byte defenseCdLimit;
	private byte attackCd;
	private byte defenseCd;
	
	private byte retreatFlag;
	private byte focusType;
	private byte confirmIndex;
	private byte stateStackPos;
	private byte []stateStack = new byte[4];
	
	int victory;
	boolean run;
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
	
	public void setAttackList(short[] attackList) {
		this.attackList = attackList;
		refreshAttackArmyInfo();
		initAttackScrollBar();
	}
	
	private void refreshAttackArmyInfo() {
		attackGeneral = engine.generalList[attackList[attackIndex]];
		attackArmyAttack = (short)attackGeneral.getSoldierAttack();
		attackArmyDefense = (short)attackGeneral.getSoldierDefense();
		attackArmySoldiers = attackGeneral.getSoldierCount();
	}
	
	public short[] getAttackList() {
		return attackList;
	}

	public void setDefenseList(short[] defenseList) {
		this.defenseList = defenseList;
		refreshDefenseArmyInfo();
		initDefenseScrollBar();
	}
	
	private void refreshDefenseArmyInfo() {
		defenseGeneral = engine.generalList[defenseList[defenseIndex]];
		defenseArmyAttack = (short)defenseGeneral.getSoldierAttack();
		defenseArmyDefense = (short)defenseGeneral.getSoldierDefense();
		defenseArmySoldiers = defenseGeneral.getSoldierCount();
	}
	
	public short[] getDefenseList() {
		return defenseList;
	}

	public boolean isAttackedByPlayer() {
		return attackedByPlayer;
	}
	
	public void setAttackedByPlayer(boolean attackedByPlayer) {
		this.attackedByPlayer = attackedByPlayer;
	}
	
	public void initAttackScrollBar() {
		attackScroll = new ScrollBar();
		attackScroll.setImage(Resource.loadImage(Resource.PIC_ID_SCROLL_BAR));
		attackScroll.setViewLen(Resource.NUM_FIGHTER_LIST_PAGE_ITEMS);
		attackScroll.setContentLen(attackList.length);
		attackScroll.setCurPage(curAttackListPage);
		if (attackList.length > Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
			attackScroll.setTotalPage(attackList.length-Resource.NUM_FIGHTER_LIST_PAGE_ITEMS+1);
		}
		else {
			attackScroll.setTotalPage(1);
		}
		attackScroll.setScrollType(ScrollBar.SCROLL_UP_DOWN);
		attackScroll.setFrameRegion(Resource.REGION_SCROLL_BAR_FRAME);
		attackScroll.setPosition(Resource.X_ATTACK_FIGHTER_LIST_BG+165, Resource.Y_FIGHTER_LIST_BG+47);
		attackScroll.setSize(16, 311);
	}
	
	public void initDefenseScrollBar() {
		defenseScroll = new ScrollBar();
		defenseScroll.setImage(Resource.loadImage(Resource.PIC_ID_SCROLL_BAR));
		defenseScroll.setViewLen(Resource.NUM_FIGHTER_LIST_PAGE_ITEMS);
		defenseScroll.setContentLen(defenseList.length);
		defenseScroll.setCurPage(curDefenseListPage);
		if (defenseList.length > Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
			defenseScroll.setTotalPage(defenseList.length-Resource.NUM_FIGHTER_LIST_PAGE_ITEMS+1);
		}
		else {
			defenseScroll.setTotalPage(1);
		}
		defenseScroll.setScrollType(ScrollBar.SCROLL_UP_DOWN);
		defenseScroll.setFrameRegion(Resource.REGION_SCROLL_BAR_FRAME);
		defenseScroll.setPosition(Resource.X_DEFENSE_FIGHTER_LIST_BG+12, 
				Resource.Y_FIGHTER_LIST_BG+47);
		defenseScroll.setSize(16, 311);
	}
	
	public void handle(KeyState KeyState) {
		switch (state) {
		case STATE_FIGHTER_LIST: 
			handleFighterList(KeyState);
			break;
		case STATE_BATTLE_FIELD: 
			handleBattleField(KeyState);
			break;
		case STATE_FIGHT_MENU: 
			handleFightMenu(KeyState);
			break;
		case STATE_FIGHT_END:
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}
	
	private void handleFightMenu(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (fightMenuIndex > 0) {
				--fightMenuIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (fightMenuIndex < Resource.STR_FIGHT_MENU.length-1) {
				++fightMenuIndex;
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK|KeyCode.LEFT)) {
			key.clear();
			Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
			state = popState();
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			switch(fightMenuIndex) {
			case FIGHT_MENU_BACK: 
				Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
				state = popState();
				break;
			case FIGHT_MENU_WAIT: 
				if (retreatFlag != 1) {
					Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
					if (attackedByPlayer) {
						for (int i = 0; i < attackSprites.length; ++i) {
							attackSprites[i].setStandAction();
						}
					}
					else {
						for (int i = 0; i < defenseSprites.length; ++i) {
							defenseSprites[i].setStandAction();
						}
					}
					state = popState();
				}
				break;
			case FIGHT_MENU_ASSAULT: 
				if (retreatFlag != 1) {
					Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
					if (attackedByPlayer) {
						for (int i = 0; i < attackSprites.length; ++i) {
							attackSprites[i].setAssaultAction();
						}
					}
					else {
						for (int i = 0; i < defenseSprites.length; ++i) {
							defenseSprites[i].setAssaultAction();
						}
					}
					state = popState();
				}
				break;
			case FIGHT_MENU_SKILL:
				if (retreatFlag != 1) {
					if (!magicAttack) {
						boolean magicCdPass = false;
						if (attackedByPlayer) {
							magicGeneral = attackGeneral;
							if (attackCd >= attackCdLimit-1) {
								magicCdPass = true;
							}
						}
						else {
							magicGeneral = defenseGeneral;
							if (defenseCd >= defenseCdLimit-1) {
								magicCdPass = true;
							}
						}
						if (engine.isDebugMode() || (magicGeneral.canUseMagic()&&magicCdPass)) {
							Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
							if (attackedByPlayer) {
								attackCd = 0;
							}
							else {
								defenseCd = 0;
							}
							magicGeneral.useMagic();
							magicAttack = true;
							state = popState();
							perFrameHurtByMagic = magicGeneral.getMagicPowerAttack()/Resource.POS_MAGIC_SHOW.length;
						}
					}
				}
				break;
			case FIGHT_MENU_RETREAT:
				if (retreatFlag != 1) {
					retreatFlag = 1;
					if (attackedByPlayer) {
						failedFlags[attackIndex] = 1;
					}
					else {
						failedFlags[defenseIndex] = 1;
					}
					Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
					if (attackedByPlayer) {
						for (int i = 0; i < attackSprites.length; ++i) {
							attackSprites[i].setRetreatAction();
						}
					}
					else {
						for (int i = 0; i < defenseSprites.length; ++i) {
							defenseSprites[i].setRetreatAction();
						}
					}
					state = popState();
				}
				break;
			default: throw new RuntimeException("错误的菜单号，fightMenuIndex="+fightMenuIndex);
			}
		}
	}

	private void handleBattleField(KeyState KeyState) {
		if (KeyState.containsAndRemove(KeyCode.OK)) {
			pushState(state);
			fightMenuIndex = 0;
			state = STATE_FIGHT_MENU;
		}
	}

	private void handleFighterList(KeyState KeyState) {
		if (KeyState.containsAndRemove(KeyCode.UP)) {
			if (focusType == 0) {
				if (attackedByPlayer) {
					if (curAttackListPage+curAttackListItem > 0) {
						if (curAttackListItem > 0) {
							--curAttackListItem;
						}
						else {
							--curAttackListPage;
						}
						attackScroll.setCurPage(curAttackListPage);
					}
					attackIndex = (byte)(curAttackListPage+curAttackListItem);
					refreshAttackArmyInfo();
				}
				else {
					if (curDefenseListPage+curDefenseListItem > 0) {
						if (curDefenseListItem > 0) {
							--curDefenseListItem;
						}
						else {
							--curDefenseListPage;
						}
						defenseScroll.setCurPage(curDefenseListPage);
					}
					defenseIndex = (byte)(curDefenseListPage+curDefenseListItem);
					refreshDefenseArmyInfo();
				}
			}
			else {
				if (confirmIndex == 0) {
					confirmIndex = 1;
				}
				else {
					confirmIndex = 0;
				}
			}
		}
		if (KeyState.containsAndRemove(KeyCode.DOWN)) {
			if (focusType == 0) {
				if (attackedByPlayer) {
					if (curAttackListPage+curAttackListItem < attackList.length-1) {
						if (curAttackListItem < Resource.NUM_FIGHTER_LIST_PAGE_ITEMS-1) {
							++curAttackListItem;
						}
						else {
							++curAttackListPage;
						}
						attackScroll.setCurPage(curAttackListPage);
					}
					attackIndex = (byte)(curAttackListPage+curAttackListItem);
					refreshAttackArmyInfo();
				}
				else {
					if (curDefenseListPage+curDefenseListItem < defenseList.length-1) {
						if (curDefenseListItem < Resource.NUM_FIGHTER_LIST_PAGE_ITEMS-1) {
							++curDefenseListItem;
						}
						else {
							++curDefenseListPage;
						}
						defenseScroll.setCurPage(curDefenseListPage);
					}
					defenseIndex = (byte)(curDefenseListPage+curDefenseListItem);
					refreshDefenseArmyInfo();
				}
			}
			else {
				if (confirmIndex == 0) {
					confirmIndex = 1;
				}
				else {
					confirmIndex = 0;
				}
			}
		}
		
		if (KeyState.containsAndRemove(KeyCode.LEFT|KeyCode.RIGHT)) {
			if (focusType == 0) {
				focusType = 1;
				confirmIndex = 0;
			}
			else {
				focusType = 0;
			}
		}
		
		if (KeyState.containsAndRemove(KeyCode.OK)) {
			if (focusType == 0) {
				focusType = 1;
				confirmIndex = 0;
			}
			else {
				if (confirmIndex == 0) {
					boolean canFight = false;
					if (attackedByPlayer) {
						if (attackIndex>=0 && failedFlags[attackIndex]!=1&&attackGeneral.getSoldierCount() > 0) {
							canFight = true;
						}
					}
					else {
						if (attackIndex>=0 && failedFlags[defenseIndex]!=1&&defenseGeneral.getSoldierCount() > 0) {
							canFight = true;
						}
					}
					if (canFight) {
						Resource.freeImage(Resource.PIC_ID_FIGHTER_LIST_BG);
						buildBattleArmy();
						attackCd = 0;
						defenseCd = 0;
						retreatFlag = 0;
						battleFrame = 0;
						magicFrame = 0;
						magicAttack = false;
						pushState(state);
						state = STATE_BATTLE_FIELD;
					}
					else {
						PopupText pt = Resource.buildPopupText();
						pt.setText("武将"+attackGeneral.getName()+"兵力不足，无法开始战斗，请选择其他武将出战");
						pt.popup();
					}
				}
				else {
					if (canRetreat) {
						PopupConfirm pc = Resource.buildPopupConfirm();
						pc.setText("确定撤退吗？");
						if (pc.popup() == 0) {
							if (attackedByPlayer) {
								victory = -1;
							}
							else {
								victory = 0;
							}
							run = false;
						}
					}
					else {
						PopupText pt = Resource.buildPopupText();
						pt.setText("没有其他的城池可以撤退");
						pt.popup();
					}
				}
			}
		}
	}
	
	private void buildBattleArmy() {
		attackCdLimit = (byte)(MAGIC_CD_LIMIT-(attackGeneral.getMagicPower()>>2));
		int attackArmyHurt = attackArmyAttack*attackArmyAttack/(attackArmyAttack+defenseArmyDefense);
		attackArmyHurt = attackArmyHurt-(attackArmyHurt>>1);
		attackSprites = buildArmyByGeneral(attackGeneral, attackArmyHurt, 
				Resource.POS_ATTACK_SOLDIER, SoldierSprite.BELONG_TO_ATTACK);
		defenseCdLimit = (byte)(MAGIC_CD_LIMIT-(defenseGeneral.getMagicPower()>>2));
		int defenseArmyHurt = defenseArmyAttack*defenseArmyAttack/(defenseArmyAttack+attackArmyDefense);
		defenseArmyHurt = defenseArmyHurt-(defenseArmyHurt>>1);
		defenseSprites = buildArmyByGeneral(defenseGeneral, defenseArmyHurt, 
				Resource.POS_DEFENSE_SOLDIER, SoldierSprite.BELONG_TO_DEFENSE);
		for (int i = 0; i < attackSprites.length; ++i) {
			attackSprites[i].setSecurityArea((short)0);
		}
		for (int i = 0; i < defenseSprites.length; ++i) {
			defenseSprites[i].setSecurityArea((short)engine.getScreenWidth());
		}
		if (attackedByPlayer) {
			/*电脑兵力小于玩家兵力一半时，不冲锋*/
			if (allLife(defenseSprites)<(allLife(attackSprites)>>1)
					&& defenseGeneral.canUseMagic()) {
				for (int i = 0; i < defenseSprites.length; ++i) {
					defenseSprites[i].setStandAction();
				}
			}
			else {
				for (int i = 0; i < defenseSprites.length; ++i) {
					defenseSprites[i].setAssaultAction();
				}
			}
		}
		else {
			if (allLife(attackSprites)<(allLife(defenseSprites)>>1)
					&& attackGeneral.canUseMagic()) {
				for (int i = 0; i < attackSprites.length; ++i) {
					attackSprites[i].setStandAction();
				}
			}
			else {
				for (int i = 0; i < attackSprites.length; ++i) {
					attackSprites[i].setAssaultAction();
				}
			}
		}
	}

	private SoldierSprite[] buildArmyByGeneral(General general, int hurt, short[][] pos, byte belongTo) {
		short soldierId = general.getSoldierId();
		Soldier soldier = engine.soldierList[soldierId];
		int spriteNum = general.getSoldierCount()/Resource.SOLDIER_COUNT_PER_SPRITE;
		int delta = general.getSoldierCount()%Resource.SOLDIER_COUNT_PER_SPRITE;
		SoldierSprite[] sprites = new SoldierSprite[spriteNum+(delta>0?1:0)];
		int spriteIndex = 0;
		int spriteLife = soldier.getLife()*Resource.SOLDIER_COUNT_PER_SPRITE;
		
		for (spriteIndex = 0; spriteIndex < spriteNum; ++spriteIndex) {
			sprites[spriteIndex] = new SoldierSprite(soldier, belongTo);
			sprites[spriteIndex].setLife(spriteLife);
			sprites[spriteIndex].setHurt(hurt);
			sprites[spriteIndex].setSingleLife(general.getSoldierLife());
		}
		spriteNum = delta;
		if (spriteNum > 0) {
			sprites[spriteIndex] = new SoldierSprite(soldier, belongTo);
			sprites[spriteIndex].setLife(soldier.getLife()*spriteNum);
			sprites[spriteIndex].setHurt(hurt);
			sprites[spriteIndex].setSingleLife(general.getSoldierLife());
		}
		for (int i = 0; i < sprites.length; ++i) {
			sprites[i].setPosition(pos[i][0], pos[i][1]);
		}
		return sprites;
	}

	public void show(Graphics g) {
		switch (state) {
		case STATE_FIGHTER_LIST: 
			showFighterList(g);
			break;
		case STATE_BATTLE_FIELD: 
			showBattleField(g);
			break;
		case STATE_FIGHT_MENU: 
			showFightMenu(g);
			break;
		case STATE_FIGHT_END:
			showBattleField(g);
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
	}

	private void showFightMenu(Graphics g) {
		Image menuBg = Resource.loadImage(Resource.PIC_ID_FIGHT_MENU_BG);
		int bgX = (engine.getScreenWidth()-menuBg.getWidth())>>1;
		int bgY = (engine.getScreenHeight()-menuBg.getHeight())>>1;
		g.drawImage(menuBg, bgX, bgY, 20);
		
		Font font = engine.getFont();
		int deltaH = (Resource.H_FIGHT_MENU_ITEM-font.getHeight())>>1;
		int sx = bgX;
		int sy = bgY+Resource.Y_FIGHT_MENU_ITEM;
		sy += ((Resource.H_FIGHT_MENU_ITEM-font.getHeight())>>1);
		for (int i = 0; i < Resource.STR_FIGHT_MENU.length; ++i, sy+=Resource.H_FIGHT_MENU_ITEM) {
			if (i == fightMenuIndex) {
				g.setColor(Resource.COLOR_ID_LIST_ITEM_SELECTED_BG);
				g.fillRect(bgX+Resource.X_FIGHT_MENU_ITEM, sy, 
						Resource.W_FIGHT_MENU_ITEM, Resource.H_FIGHT_MENU_ITEM);
			}
			sx = bgX+Resource.X_FIGHT_MENU_ITEM+((Resource.W_FIGHT_MENU_ITEM-font.stringWidth(Resource.STR_FIGHT_MENU[i]))>>1);
			if (i > 0) {
				if (retreatFlag == 1) {
					g.setColor(Resource.COLOR_ID_LIST_ITEM_DISABLE_TEXT);
				}
				else {
					if (i == FIGHT_MENU_SKILL) {	//对武将计特殊处理
						boolean canUseMagic = false;
						if (attackedByPlayer) {
							canUseMagic = attackGeneral.canUseMagic();
							canUseMagic &= (attackCd >= attackCdLimit-1);
						}
						else {
							canUseMagic = defenseGeneral.canUseMagic();
							canUseMagic &= (defenseCd >= defenseCdLimit-1);
						}
						if (!magicAttack&&canUseMagic) {
							g.setColor(Resource.COLOR_ID_LIST_ITEM_NORMAL_TEXT);
						}
						else {
							g.setColor(Resource.COLOR_ID_LIST_ITEM_DISABLE_TEXT);
						}
					}
					else {
						g.setColor(Resource.COLOR_ID_LIST_ITEM_NORMAL_TEXT);
					}
				}
			}
			else {
				g.setColor(Resource.COLOR_ID_LIST_ITEM_NORMAL_TEXT);
			}
			g.drawString(Resource.STR_FIGHT_MENU[i], sx, sy+deltaH, 20);
		}
	}

	private int partitionSoldier(SoldierSprite []list, int low, int high) {
		SoldierSprite pivot = list[low];
		int pivotkey = pivot.getY();   
		while (low < high) {
			while (low < high && list[high].getY()>=pivotkey) --high;
			list[low] = list[high];
			while (low < high && list[low].getY()<=pivotkey) ++low;
			list[high] = list[low];
		}
		list[low] = pivot;
		return low;
	}
	
	private void qsortSoldier(SoldierSprite []list, int low, int high) {
		if (low < high) {
			int pivotpos = partitionSoldier(list, low, high);
			qsortSoldier(list, low, pivotpos-1);
			qsortSoldier(list, pivotpos+1, high);
		}
	}
	
	/*y升序排列*/
	private void sortSoldier(SoldierSprite []list) {
		qsortSoldier(list, 0, list.length-1);
	}
	
	private void showCdProgress(Graphics g) {
		Image cdBg = Resource.loadImage(Resource.PIC_ID_CD_BG);
		Image cdProgress = Resource.loadImage(Resource.PIC_ID_CD_PROGRESS);
		int cdBgW = 222;
		int cdBgH = 51;
		int[] progressArea = {84, 20, 112, 11};
		
		int sx = 0, sy = 90;
		int progressSw = 0;
		g.drawImage(cdBg, sx, sy, 20);
		boolean needShowCdProgress = true;
		if (attackCd >= attackCdLimit-1) {
			if (battleFrame%4 >= 2) {
				needShowCdProgress = false;
			}
		}
		if (needShowCdProgress) {
			progressSw = (attackCd+1)*progressArea[2]/attackCdLimit;
			g.drawRegion(cdProgress, progressArea[2]-progressSw, 0, progressSw, 11, 0, sx+progressArea[0], sy+progressArea[1], 20);
		}
		sx = engine.getScreenWidth()-cdBgW;
		g.drawRegion(cdBg, 0, 0, cdBgW, cdBgH, Sprite.TRANS_MIRROR, sx, sy, 20);
		
		needShowCdProgress = true;
		if (defenseCd >= defenseCdLimit-1) {
			if (battleFrame%4 >= 2) {
				needShowCdProgress = false;
			}
		}
		if (needShowCdProgress) {
			progressSw = (defenseCd+1)*progressArea[2]/defenseCdLimit;
			g.drawRegion(cdProgress, progressArea[2]-progressSw, 0, progressSw, 11, Sprite.TRANS_MIRROR, engine.getScreenWidth()-progressArea[0]-progressSw, sy+progressArea[1], 20);
		}
	}
	
	private void showBattleField(Graphics g) {
		Image battleBg = Resource.loadImage(Resource.PIC_ID_BATTLE_FILED_BG);
		g.drawImage(battleBg, 0, 0, 20);
		
		showCdProgress(g);
		showMagicStar(g);
		
		Image head = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_HEAD);
		g.drawRegion(head, 71*attackGeneral.getCity().getSeigneurId(), 0, 71, 71, 0, 
				2, 2, 20);
		g.drawRegion(head, 71*defenseGeneral.getCity().getSeigneurId(), 0, 71, 71, 0, 
				568, 2, 20);
		
		if (magicAttack) {
			g.setColor(0);
			g.fillRect(0, 84, engine.getScreenWidth(), engine.getScreenHeight()-84);
		}
		
		/*进攻部队攻击*/
		showBattleFieldInfo(g, 
				Resource.POS_BATTLE_FIELD_ATTACK_ARMY_INFO, 
				Integer.toString(attackArmyAttack),
				Integer.toString(attackArmyDefense),
				Integer.toString(attackArmySoldiers),
				attackGeneral.getName());
		
		
		/*防守部队攻击*/
		showBattleFieldInfo(g, 
				Resource.POS_BATTLE_FIELD_DEFENSE_ARMY_INFO, 
				Integer.toString(defenseArmyAttack),
				Integer.toString(defenseArmyDefense),
				Integer.toString(defenseArmySoldiers),
				defenseGeneral.getName());
		
		/*显示进攻方士兵*/
		sortSoldier(attackSprites);
		for (int i = 0; i < attackSprites.length; ++i) {
			if (attackSprites[i] != null) {
				attackSprites[i].show(g);
			}
		}
		
		/*显示防守方士兵*/
		sortSoldier(defenseSprites);
		for (int i = 0; i < defenseSprites.length; ++i) {
			if (defenseSprites[i] != null) {
				defenseSprites[i].show(g);
			}
		}
		
		/*显示武将计效果*/
		if (magicAttack) {
			int pos;
			for (int i = 0; i < Resource.POS_MAGIC_SHOW.length; i++) {
				if (magicFrame >= i) {
					pos = (magicFrame - i) % 5 + Resource.getMagicPos(magicGeneral);
					g.drawRegion(Resource.loadImage(Resource.PIC_ID_MAGIC), 
							Resource.REGION_MAGIC[pos][0], 
							Resource.REGION_MAGIC[pos][1], 
							Resource.REGION_MAGIC[pos][2], 
							Resource.REGION_MAGIC[pos][3], 
							0, 
							Resource.POS_MAGIC_SHOW[i][0], 
							Resource.POS_MAGIC_SHOW[i][1]-50, 
							20);
				}
			}
		}
	}

	private void showMagicStar(Graphics g) {
		Image star = Resource.loadImage(Resource.PIC_ID_MAGIC_STAR);
		Image grayStar = Resource.getImage(Resource.PIC_ID_MAGIC_STAR_VALID);
		if (grayStar == null) {
			grayStar = ImageUtil.createGray(star, 0, 0, 17, 18);
			Resource.setImage(Resource.PIC_ID_MAGIC_STAR_VALID, grayStar);
		}
		int starChangeFrames = 2;
		int starFrame = 0;
		if (battleFrame%9 < starChangeFrames) {
			starFrame = 0;
		}
		else if (battleFrame%9 < (starChangeFrames<<1)){
			starFrame = 1;
		}
		else {
			starFrame = 2;
		}
		
		int starX = 7;
		int starY = 90;
		int totalMagicNum = 2;
		int magicNum = attackGeneral.getMagicNum();
		int sn = 0;
		while (sn < magicNum) {
			g.drawRegion(star, 17*starFrame, 0, 17, 18, 0, starX, starY, 20);
			++sn;
			starX += 22;
		}
		while (sn < totalMagicNum) {
			g.drawImage(grayStar, starX, starY, 20);
			++sn;
			starX += 22;
		}
		
		starX = engine.getScreenWidth()-7-17;
		starY = 90;
		magicNum = defenseGeneral.getMagicNum();
		sn = 0;
		while (sn < magicNum) {
			g.drawRegion(star, 17*starFrame, 0, 17, 18, 0, starX, starY, 20);
			++sn;
			starX -= 22;
		}
		while (sn < totalMagicNum) {
			g.drawImage(grayStar, starX, starY, 20);
			++sn;
			starX -= 22;
		}
	}

	private void showFighterList(Graphics g) {
		/*显示战斗背景*/
		Image battleBg = Resource.loadImage(Resource.PIC_ID_BATTLE_FILED_BG);
		g.drawImage(battleBg, 0, 0, 20);
		
		/*显示君主头像*/
		Image head = Resource.loadImage(Resource.PIC_ID_SEIGNEUR_HEAD);
		g.drawRegion(head, 71*attackGeneral.getCity().getSeigneurId(), 0, 71, 71, 0, 
				2, 2, 20);
		g.drawRegion(head, 71*defenseGeneral.getCity().getSeigneurId(), 0, 71, 71, 0, 
				568, 2, 20);
		
		if (attackedByPlayer) {
			defenseIndex = (byte)getAttackFighter(defenseList, defenseIndex);
			curDefenseListItem = 0;
			curDefenseListPage = (byte)(defenseIndex-curDefenseListItem);
			int deltaPage = curDefenseListPage-(defenseScroll.getTotalPage()-1);
			if (deltaPage > 0) {
				curDefenseListPage  -= deltaPage;
				curDefenseListItem = (byte)(defenseIndex-curDefenseListPage);
			}
			defenseScroll.setCurPage(curDefenseListPage);
			refreshDefenseArmyInfo();
		}
		else {
			attackIndex = (byte)getAttackFighter(attackList, attackIndex);
			curAttackListItem = 0;
			curAttackListPage = (byte)(attackIndex-curAttackListItem);
			int deltaPage = curAttackListPage-(attackScroll.getTotalPage()-1);
			if (deltaPage > 0) {
				curAttackListPage  -= deltaPage;
				curAttackListItem = (byte)(attackIndex-curAttackListPage);
			}
			attackScroll.setCurPage(curAttackListPage);
			refreshAttackArmyInfo();
		}
		
		/*进攻部队*/
		showBattleFieldInfo(g, 
				Resource.POS_BATTLE_FIELD_ATTACK_ARMY_INFO, 
				Integer.toString(attackArmyAttack),
				Integer.toString(attackArmyDefense),
				Integer.toString(attackGeneral.getSoldierCount()),
				attackGeneral.getName());
		
		/*防守部队*/
		showBattleFieldInfo(g, 
				Resource.POS_BATTLE_FIELD_DEFENSE_ARMY_INFO, 
				Integer.toString(defenseArmyAttack),
				Integer.toString(defenseArmyDefense),
				Integer.toString(defenseGeneral.getSoldierCount()),
				defenseGeneral.getName());
		
		Image fighterListBg = Resource.loadImage(Resource.PIC_ID_FIGHTER_LIST_BG);
		Image generalAttributeBg = Resource.loadImage(Resource.PIC_ID_FIGHTER_LIST_GENERAL_ATTRIBUTE_BG);
		Image armyAttributeBg = Resource.loadImage(Resource.PIC_ID_FIGHTER_LIST_ARMY_ATTRIBUTE_BG);
		Image titleBg = Resource.loadImage(Resource.PIC_ID_FIGHT_TITLE);
		showAttackFighterList(g, fighterListBg, generalAttributeBg, armyAttributeBg, titleBg);
		showDefenseFighterList(g, fighterListBg, generalAttributeBg, armyAttributeBg, titleBg);
		showFightBtn(g);
	}
	
	private void showFightBtn(Graphics g) {
		Font font = engine.getFont();
		Image btnBg = Resource.loadImage(Resource.PIC_ID_BATTLE_BTN_BG);
		int btnW = btnBg.getWidth();
		int btnH = btnBg.getHeight();
		int deltaH = (btnH-font.getHeight())>>1;
		int sx = (engine.getScreenWidth()-btnW)>>1;
		int sy = 413;
		g.drawImage(btnBg, sx, sy, 20);
		String ss = "开始战斗";
		g.setColor(0XFFFFFF);
		g.drawString(ss, sx+((btnW-font.stringWidth(ss))>>1), sy+deltaH+3, 20);
		
		sy = 450;
		g.drawImage(btnBg, sx, sy, 20);
		ss = "全军撤退";
		g.setColor(0XFFFFFF);
		g.drawString(ss, sx+((btnW-font.stringWidth(ss))>>1), sy+deltaH+3, 20);
		
		if (focusType == 1) {
			if (confirmIndex == 0) {
				sy = 413;
			}
			else {
				sy = 450;
			}
			
			if (battleFrame == 0) {
				DrawUtil.drawRect(g, sx+5, sy+5, btnW-10, btnH-8, 3, 0XFFFF00);
			}
		}
		
	}
	
	private void showDefenseFighterList(Graphics g, Image listBg, Image generalBg, Image armyBg, Image titleBg) {
		Font font = engine.getFont();
		int deltaH = (Resource.H_BATTLE_FIELD_ARMY_INFO-font.getHeight())>>1;
		int num = 0;
		
		int yInc = generalBg.getHeight()+3;
		
		//显示防守方列表
		int bgX = Resource.X_DEFENSE_FIGHTER_LIST_BG;
		int bgY = Resource.Y_FIGHTER_LIST_BG;
		g.drawRegion(listBg, 0, 0, listBg.getWidth(), listBg.getHeight(), 
				Sprite.TRANS_MIRROR, bgX, bgY, 20);
		
		g.drawRegion(titleBg, 0, 24, 72, 24, 0, bgX+51, bgY+13, 20);
		
		int sx = bgX+Resource.X_DEFENSE_FIGHTER_LIST_ITEM;
		int sy = bgY+Resource.Y_FIGHTER_LIST_ITEM;
		int loopCount = 0;
		if (defenseList.length >= Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
			loopCount = Resource.NUM_FIGHTER_LIST_PAGE_ITEMS;
		}
		else {
			loopCount = defenseList.length;
		}
		short[][] pos = Resource.POS_FIGHTER_LIST_GENERAL_ATTRIBUTE;
		for (int i = 0; i < loopCount; ++i, sy+=yInc) {
			General general = engine.generalList[defenseList[curDefenseListPage+i]];
			g.drawImage(generalBg, sx, sy, 20);
			if (general.getSoldierCount() > 0) {
				if (!attackedByPlayer && failedFlags[curDefenseListPage+i] == 1) {
					g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
				}
				else {
					g.setColor(Resource.COLOR_ID_FIGHTER_NORMAL_TEXT);
				}
			}
			else {
				g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
			}
			g.drawString(general.getName(),
					sx+Resource.X_FIGHTER_LIST_GENERAL_NAME+(Resource.W_FIGHTER_LIST_GENERAL_NAME-font.stringWidth(general.getName())>>1),
					sy+Resource.Y_FIGHTER_LIST_GENERAL_NAME+deltaH, 20);
			num = 0;
			/*武力*/
			g.drawString(Integer.toString(general.getForce()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*智力*/
			g.drawString(Integer.toString(general.getWit()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*精力*/
			g.drawString(Integer.toString(general.getMagic()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*将技*/
			g.drawString(Integer.toString(general.getMagicPower()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);

			
			if (i == curDefenseListItem) {
				if (!attackedByPlayer && focusType == 0) {
					if (battleFrame == 0) {
						DrawUtil.drawRect(g, sx, sy, generalBg.getWidth(), generalBg.getHeight(), 2, 0XFFFF00);
					}
				}
				else {
					DrawUtil.drawRect(g, sx, sy, generalBg.getWidth(), generalBg.getHeight(), 2, 0XFFFF00);
				}
			}
		}
		defenseScroll.show(g);
		
		if (defenseGeneral.getSoldierCount() > 0) {
			g.setColor(Resource.COLOR_ID_FIGHTER_NORMAL_TEXT);
		}
		else {
			g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
		}
		
		bgX = 336;
		bgY = 152;
		g.drawImage(armyBg, bgX, bgY, 20);
		
		/*部队攻击*/
		sx = bgX+Resource.X_FIGHT_ARMY_ATTRIBUTE+4;
		sy = bgY+Resource.Y_FIGHT_ARMY_ATTRIBUTE+deltaH;
		g.drawString(Integer.toString(defenseArmyAttack), 
				sx, sy, 20);
		
		/*部队防御*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(Integer.toString(defenseArmyDefense), 
				sx, sy, 20);
		
		/*兵种*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(defenseGeneral.getSoldier().getName(), 
				sx, sy, 20);
		
		/*兵力*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(Integer.toString(defenseArmySoldiers), 
				sx, sy, 20);
	}
	
	private void showAttackFighterList(Graphics g, Image listBg, Image generalBg, Image armyBg, Image titleBg) {
		Font font = engine.getFont();
		int deltaH = (Resource.H_BATTLE_FIELD_ARMY_INFO-font.getHeight())>>1;
		int num = 0;
		
		int bgX = Resource.X_ATTACK_FIGHTER_LIST_BG;
		int bgY = Resource.Y_FIGHTER_LIST_BG;
		g.drawImage(listBg, bgX, bgY, 20);
		g.drawRegion(titleBg, 0, 0, 72, 24, 0, bgX+51, bgY+13, 20);
		
		int yInc = generalBg.getHeight()+3;
		
		int sx = bgX+Resource.X_ATTACK_FIGHTER_LIST_ITEM;
		int sy = bgY+Resource.Y_FIGHTER_LIST_ITEM;
		int loopCount = 0;
		if (attackList.length >= Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
			loopCount = Resource.NUM_FIGHTER_LIST_PAGE_ITEMS;
		}
		else {
			loopCount = attackList.length;
		}
		short[][] pos = Resource.POS_FIGHTER_LIST_GENERAL_ATTRIBUTE;
		for (int i = 0; i < loopCount; ++i, sy+=yInc) {
			General general = engine.generalList[attackList[curAttackListPage+i]];
			
			g.drawImage(generalBg, sx, sy, 20);
			if (general.getSoldierCount() > 0) {
				if (attackedByPlayer && failedFlags[curAttackListPage+i]==1) {
					g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
				}
				else {
					g.setColor(Resource.COLOR_ID_FIGHTER_NORMAL_TEXT);
				}
			}
			else {
				g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
			}
			
			g.drawString(general.getName(),
					sx+Resource.X_FIGHTER_LIST_GENERAL_NAME+(Resource.W_FIGHTER_LIST_GENERAL_NAME-font.stringWidth(general.getName())>>1),
					sy+Resource.Y_FIGHTER_LIST_GENERAL_NAME+deltaH, 20);
			
			num = 0;
			/*武力*/
			g.drawString(Integer.toString(general.getForce()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*智力*/
			g.drawString(Integer.toString(general.getWit()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*精力*/
			g.drawString(Integer.toString(general.getMagic()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			++num;
			
			/*将技*/
			g.drawString(Integer.toString(general.getMagicPower()), 
					sx+pos[num][0]+4, 
					sy+pos[num][1]+deltaH, 20);
			
			
			if (i == curAttackListItem) {
				if (attackedByPlayer && focusType == 0) {
					if (battleFrame == 0) {
						DrawUtil.drawRect(g, sx, sy, generalBg.getWidth(), generalBg.getHeight(), 2, 0XFFFF00);
					}
				}
				else {
					DrawUtil.drawRect(g, sx, sy, generalBg.getWidth(), generalBg.getHeight(), 2, 0XFFFF00);
				}
			}
			
		}
		attackScroll.show(g);
		
		if (attackGeneral.getSoldierCount() > 0) {
			g.setColor(Resource.COLOR_ID_FIGHTER_NORMAL_TEXT);
		}
		else {
			g.setColor(Resource.COLOR_ID_FIGHTER_DISABLE_TEXT);
		}
		
		bgX = 193;
		bgY = 152;
		g.drawImage(armyBg, bgX, bgY, 20);
		
		/*部队攻击*/
		sx = bgX+Resource.X_FIGHT_ARMY_ATTRIBUTE+4;
		sy = bgY+Resource.Y_FIGHT_ARMY_ATTRIBUTE+deltaH;
		g.drawString(Integer.toString(attackArmyAttack), 
				sx, sy, 20);
		
		/*部队防御*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(Integer.toString(attackArmyDefense), 
				sx, sy, 20);
		
		/*兵种*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(attackGeneral.getSoldier().getName(), 
				sx, sy, 20);
		
		/*兵力*/
		sy += Resource.YSPACE_FIGHT_ARMY_ATTRIBUTE;
		g.drawString(Integer.toString(attackArmySoldiers), 
				sx, sy, 20);
	}
	
	private void showBattleFieldInfo(Graphics g, short[][]pos, String attack, String defense, String soldiers, String name) {
		Font font = engine.getFont();
		int deltaH = (Resource.H_BATTLE_FIELD_ARMY_INFO-font.getHeight())>>1;
		int num = 0;
		
		g.setColor(0XFFFFFF);
		g.drawString(attack, 
				pos[num][0]+4, 
				pos[num][1]+deltaH, 20);
		++num;
		
		g.drawString(defense, 
				pos[num][0]+4, 
				pos[num][1]+deltaH, 20);
		++num;
		
		g.drawString(soldiers, 
				pos[num][0]+4, 
				pos[num][1]+deltaH, 20);
		++num;
		
		g.drawString(name, 
				pos[num][0]+4, 
				pos[num][1]+deltaH, 20);
	}

	public void execute() {
		switch (state) {
		case STATE_FIGHTER_LIST: 
			executeFighterList();
			break;
		case STATE_BATTLE_FIELD: 
			executeBattleField();
			break;
		case STATE_FIGHT_MENU: break;
		case STATE_FIGHT_END:
			executeFightEnd();
			break;
		default:
			throw new RuntimeException("未知的状态, state="+state);
		}
		
	}

	private void executeFightEnd() {
		
		for (int i = 0; i < attackSprites.length; ++i) {
			if (attackSprites[i] != null) {
				attackSprites[i].nextFrame();
			}
		}
		
		for (int i = 0; i < defenseSprites.length; ++i) {
			if (defenseSprites[i] != null) {
				defenseSprites[i].nextFrame();
			}
		}
		
		if (timePass(3000)) {
			General general = attackGeneral;

			if (attackArmySoldiers == 0) {
				general.setSoldierCount((short)0);
				general.setStamina((short)0);
			}
			else {
				general.setSoldierCount(attackArmySoldiers);
			}
			
			general = defenseGeneral;

			if (defenseArmySoldiers == 0) {
				general.setSoldierCount((short)0);
				general.setStamina((short)0);
			}
			else {
				general.setSoldierCount(defenseArmySoldiers);
			}
			
			for (int i = 0; i < attackSprites.length; ++i) {
				attackSprites[i].clearResource();
				attackSprites[i] = null;
			}
			attackSprites = null;
			
			for (int i = 0; i < defenseSprites.length; ++i) {
				defenseSprites[i].clearResource();
				defenseSprites[i] = null;
			}
			defenseSprites = null;
			
			if (attackedByPlayer) {
				defenseIndex = (byte)getAttackFighter(defenseList);
				if (defenseIndex >= 0) {
					if (defenseIndex < Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
						curDefenseListPage = 0;
					}
					else {
						curDefenseListPage = (byte)(defenseIndex-Resource.NUM_FIGHTER_LIST_PAGE_ITEMS+1);
					}
					curDefenseListItem = (byte)(defenseIndex-curDefenseListPage);
					defenseScroll.setCurPage(curDefenseListPage);
					refreshDefenseArmyInfo();
				}
			}
			else {
				attackIndex = (byte)getAttackFighter(attackList);
				if (attackIndex >= 0) {
					if (attackIndex < Resource.NUM_FIGHTER_LIST_PAGE_ITEMS) {
						curAttackListPage = 0;
					}
					else {
						curAttackListPage = (byte)(attackIndex-Resource.NUM_FIGHTER_LIST_PAGE_ITEMS+1);
					}
					curAttackListItem = (byte)(attackIndex-curAttackListPage);
					attackScroll.setCurPage(curAttackListPage);
					refreshAttackArmyInfo();
				}
			}
			
			Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
			state = popState();
			
			if (attackFailer()) {
				victory = -1;
				run = false;
			}
			else  {
				if (defenseFailer()) {
					victory = 0;
					run = false;
				}
			}
		}
		
	}


	private void executeFighterList() {
		if (battleFrame == 0) {
			battleFrame = 1;
		}
		else {
			battleFrame = 0;
		}
	}
	private void executeBattleField() {
		++battleFrame;
		
		if (attackCd < attackCdLimit-1) {
			++attackCd;
		}
		if (defenseCd < defenseCdLimit-1) {
			++defenseCd;
		}
		
		if (magicAttack) {
			/*武将计伤害*/
			int hurt = perFrameHurtByMagic;
			int life;
			SoldierSprite[] injuredSprites;
			if (magicGeneral == attackGeneral) {
				injuredSprites = defenseSprites;
			}
			else {
				injuredSprites = attackSprites;
			}
			for (int i = 0; i < injuredSprites.length; ++i) {
				if (hurt <= 0) {
					break;
				}
				if (injuredSprites[i] != null) {
					life = injuredSprites[i].getLife();
					if (life > 0) {
						if (life >= hurt) {
							injuredSprites[i].injured(hurt);
							hurt = 0;
						}
						else {
							injuredSprites[i].injured(life);
							hurt -= life;
						}
					}
				}
			}
			
			/*武将计释放完*/
			if (++magicFrame >= Resource.POS_MAGIC_SHOW.length) {
				Resource.freeImage(Resource.PIC_ID_MAGIC);
				magicAttack = false;
				magicFrame = 0;
			}
		}
		
		/*将地图上的所有士兵随机排列*/
		byte soldierList[] = new byte[attackSprites.length+defenseSprites.length];
		int pos = 0;
		while (pos < soldierList.length) {
			soldierList[pos] = (byte)pos;
			++pos;
		}
		
		int swapPos;
		byte tmp;
		pos = 0;
		while (pos < soldierList.length) {
			swapPos = RandomValue.getRandInt(soldierList.length);
			tmp = soldierList[pos];
			soldierList[pos] = soldierList[swapPos];
			soldierList[swapPos] = tmp;
			++pos;
		}
		
		int realPos;
		pos = 0;
		while (pos < soldierList.length) {
			if (pos >= attackSprites.length) {
				realPos = pos-attackSprites.length;
				if (defenseSprites[realPos] != null) {
					if (defenseSprites[realPos].canAction()) {
						defenseSprites[realPos].action(attackSprites);
					}
					defenseSprites[realPos].nextFrame();
				}
			}
			else {
				realPos = pos;
				if (attackSprites[realPos] != null) {
					if (attackSprites[realPos].canAction()) {
						attackSprites[realPos].action(defenseSprites);
					}
					attackSprites[realPos].nextFrame();
				}
			}
			++pos;
		}
		
		attackArmySoldiers = (short)((allLife(attackSprites)+attackGeneral.getSoldierLife()-1)/attackGeneral.getSoldierLife());
		defenseArmySoldiers = (short)((allLife(defenseSprites)+defenseGeneral.getSoldierLife()-1)/defenseGeneral.getSoldierLife());
	
		if (isfightEnd()) {
			state = STATE_FIGHT_END;
		}
		else {
			if (!magicAttack) {	//电脑施放武将计
				boolean magicCdPass = false;
				if (attackedByPlayer) {
					magicGeneral = defenseGeneral;
					if (defenseCd >= defenseCdLimit-1) {
						magicCdPass = true;
					}
				}
				else {
					magicGeneral = attackGeneral;
					if (attackCd >= attackCdLimit-1) {
						magicCdPass = true;
					}
				}
				if (magicGeneral.canUseMagic()&&magicCdPass) {
					if (aiNeedUserMagic()) {
						if (attackedByPlayer) {
							defenseCd = 0;
						}
						else {
							attackCd = 0;
						}
						magicGeneral.useMagic();
						magicAttack = true;
						perFrameHurtByMagic = magicGeneral.getMagicPowerAttack()/Resource.POS_MAGIC_SHOW.length;
					}
				}
			}
			
			if (!magicAttack) {
				if (attackedByPlayer) {
					if (!defenseGeneral.canUseMagic()) {
						for (int i = 0; i < defenseSprites.length; ++i) {
							defenseSprites[i].setAssaultAction();
						}
					}
				}
				else {
					if (!attackGeneral.canUseMagic()) {
						for (int i = 0; i < attackSprites.length; ++i) {
							attackSprites[i].setAssaultAction();
						}
					}
				}
			}
		}
	}
	
	public boolean aiNeedUserMagic() {
		if (retreatFlag == 1) {
			return false;
		}
		
		boolean use = true;
		General playerGeneral = null;
		General aiGeneral = null;
		int playerSoldierHurt = 0;
		int aiSoldierHurt = 0;
		int playerSoldiersLift = 0;
		int aiSoldiersLift = 0;
		int playerSprites = 0;
		int aiSprites = 0;
		if (attackedByPlayer) {
			playerGeneral = attackGeneral;
			aiGeneral = defenseGeneral;
			playerSoldiersLift = allLife(attackSprites);
			aiSoldiersLift = allLife(defenseSprites);
			playerSoldierHurt = attackSprites[0].getHurt();
			aiSoldierHurt = defenseSprites[0].getHurt();
			playerSprites = allSprites(attackSprites);
			aiSprites = allSprites(defenseSprites);
		}
		else {
			playerGeneral = defenseGeneral;
			aiGeneral = attackGeneral;
			playerSoldiersLift = allLife(defenseSprites);
			aiSoldiersLift = allLife(attackSprites);
			playerSoldierHurt = defenseSprites[0].getHurt();
			aiSoldierHurt = attackSprites[0].getHurt();
			playerSprites = allSprites(defenseSprites);
			aiSprites = allSprites(attackSprites);
		}
		
		if (aiSoldiersLift > playerGeneral.getMagicPowerAttack() && aiSprites > playerSprites) {
			if(aiSoldiersLift > (playerSoldiersLift>>1)) {
				if (!playerGeneral.canUseMagic()) {
					use = false;
				}
				else {
					if (aiGeneral.getMagicPowerAttack() > playerSoldiersLift) {
						use = false;
					}
				}
			}
			else if (aiSoldiersLift > playerSoldiersLift) {
				if (!playerGeneral.canUseMagic() 
						&& aiSoldierHurt > playerSoldierHurt) {
					use = false;
				}
			}
		}
		return use;
	}
	
	
	
	public boolean isfightEnd() {
		boolean fightEnd = true;
		for (int i = 0; i < attackSprites.length; ++i) {
			if (!attackSprites[i].isDisappear()) {
				fightEnd = false;
				break;
			}
		}
		if (!fightEnd) {
			fightEnd = true;
			for (int i = 0; i < defenseSprites.length; ++i) {
				if (!defenseSprites[i].isDisappear()) {
					fightEnd = false;
					break;
				}
			}
		}
		return fightEnd;
	}
	
	public boolean attackFailer() {
		if (attackedByPlayer) {
			for (int i = 0; i < attackList.length; ++i) {
				if (failedFlags[i]!=1 && engine.generalList[attackList[i]].getSoldierCount() > 0) {
					return false;
				}
			}
		}
		else {
			for (int i = 0; i < attackList.length; ++i) {
				if (engine.generalList[attackList[i]].getSoldierCount() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public boolean defenseFailer() {
		if (attackedByPlayer) {
			for (int i = 0; i < defenseList.length; ++i) {
				if (engine.generalList[defenseList[i]].getSoldierCount() > 0) {
					return false;
				}
			}
		}
		else {
			for (int i = 0; i < defenseList.length; ++i) {
				if (failedFlags[i]!=1 && engine.generalList[defenseList[i]].getSoldierCount() > 0) {
					return false;
				}
			}
		}
		return true;
	}
	
	public int getAttackFighter(short[] fighterList, int start) {
		int pos = -1;
		for (int i = start+1; i < fighterList.length; ++i) {
			if (engine.generalList[fighterList[i]].getSoldierCount() > 0) {
				pos = i;
				break;
			}
		}
		if (pos < 0) {
			for (int i = 0; i <= start; ++i) {
				if (engine.generalList[fighterList[i]].getSoldierCount() > 0) {
					pos = i;
					break;
				}
			}
		}
		return pos;
	}
	
	public int getAttackFighter(short[] fighterList) {
		int pos = -1;
		for (int i = 0; i < fighterList.length; ++i) {
			if (engine.generalList[fighterList[i]].getSoldierCount() > 0) {
				pos = i;
				break;
			}
		}
		return pos;
	}
	
	private int allLife(SoldierSprite[] list) {
		int life = 0;
		int slife = 0;
		for (int i = 0; i < list.length; ++i) {
			if (list[i]!=null && ((slife=list[i].getLife())>0)) {
				life += slife;
			}
		}
		return life;
	}
	
	private int allSprites(SoldierSprite[] list) {
		int count = 0;
		for (int i = 0; i < list.length; ++i) {
			if (list[i]!=null && list[i].getLife()>0) {
				++count;
			}
		}
		return count;
	}
	
	public int battle() {
		Graphics g = engine.getGraphics();
		KeyState key = engine.getKeyState();
		run = true;
		defenseIndex = (byte)getAttackFighter(defenseList);
		attackIndex = (byte)getAttackFighter(attackList);
		if (attackedByPlayer) {
			failedFlags = new byte[attackList.length];
			canRetreat = true;
		}
		else {
			failedFlags = new byte[defenseList.length];
			if (engine.getSeigneurCityCount(engine.playerSeigneur) > 1) {
				canRetreat = true;
			}
			else {
				canRetreat = false;
			}
		}
		
		try {
			while (run) {
				long t1 = System.currentTimeMillis();
				handle(key);
				//KeyState.clear();
				show(g);
				engine.flushGraphics();
				execute();
				System.gc();
				int sleepTime = (int)(125-(System.currentTimeMillis()-t1));
				if (sleepTime <= 0) {
					Thread.sleep(0);
				}
				else {
					Thread.sleep(sleepTime);
				}
			}
		}
		catch (Throwable t) {
			t.printStackTrace();
			victory = -1;
		}
		finally {
			clear();
		}
		return victory;
	}

	public void clear() {
		Resource.freeImage(Resource.PIC_ID_BATTLE_FILED_BG);
		Resource.freeImage(Resource.PIC_ID_FIGHTER_LIST_BG);
		Resource.freeImage(Resource.PIC_ID_FIGHT_MENU_BG);
		Resource.freeImage(Resource.PIC_ID_CD_BG);
		Resource.freeImage(Resource.PIC_ID_CD_PROGRESS);
		Resource.freeImage(Resource.PIC_ID_FIGHTER_LIST_GENERAL_ATTRIBUTE_BG);
		Resource.freeImage(Resource.PIC_ID_FIGHTER_LIST_ARMY_ATTRIBUTE_BG);
		Resource.freeImage(Resource.PIC_ID_FIGHT_TITLE);
		Resource.freeImage(Resource.PIC_ID_BATTLE_BTN_BG);
		Resource.freeImage(Resource.PIC_ID_MAGIC_STAR);
		Resource.freeImage(Resource.PIC_ID_MAGIC_STAR_VALID);
		Resource.freeImage(Resource.PIC_ID_MAGIC);
		attackList = null;
		defenseList = null;
		failedFlags = null;
	}
	
	public void init() {
		
	}
}
