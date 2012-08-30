package sanguo;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.ui.DrawUtil;
import cn.ohyeah.stb.ui.ScrollBar;
import cn.ohyeah.stb.game.SGraphics;
import cn.ohyeah.stb.key.KeyCode;
import cn.ohyeah.stb.key.KeyState;


public class GeneralListUI {
	public static final byte TYPE_FARMING = 1;
	public static final byte TYPE_COMMERCE = 2;
	public static final byte TYPE_RELIEF = 3;
	public static final byte TYPE_DISTRIBUTE = 4;
	public static final byte TYPE_EXPEDITION = 5;
	public static final byte TYPE_SEARCH = 6;
	public static final byte TYPE_CONVALESCE = 7;
	public static final byte TYPE_MOVE = 8;
	
	private static final byte NUM_GENERAL_ATTRIBUTE = 9;
	private static final byte NUM_GENERAL_LIST_PAGE_ITEMS = 10; 
	/*当前页序号*/
	private byte curPageIndex;
	/*武将在当前页中的序号*/
	private byte curPageGeneralIndex;
	private byte curGroupIndex;
	private byte curBtnIndex;
	
	private int confirmIndex;
	
	private int type;
	private short selectedLen;
	private short selectedLimit;
	private short []generalIdList;
	private short []selectedList;
	private String[][] viewModel;
	private ScrollBar scroll;
	private Image listBg;
	private short bgX, bgY;
	private boolean considerGeneralState = true;
	private boolean isConfirmed;
	public void clearResource() {
		Resource.freeImage(Resource.PIC_ID_GENERAL_LIST_BG);
		Resource.freeImage(Resource.PIC_ID_SCROLL_BAR);
		listBg = null;
		generalIdList = null;
		selectedList = null;
		scroll = null;
		if (viewModel != null) {
			for (int i = 0; i < viewModel.length; ++i) {
				viewModel[i] = null;
			}
			viewModel = null;
		}
	}
	
	public void generalToViewModel(General general, String []model) {
		int pos = 0;
		/*武将名*/
		model[pos++] = general.getName();
		/*体力值*/
		model[pos++] = Integer.toString(general.getStamina());
		/*精力值*/
		model[pos++] = Integer.toString(general.getMagic());
		/*武力*/
		model[pos++] = Integer.toString(general.getForce());
		/*智力*/
		model[pos++] = Integer.toString(general.getWit());
		/*魅力*/
		model[pos++] = Integer.toString(general.getCharm());
		/*将技*/
		model[pos++] = Integer.toString(general.getMagicPower());
		/*兵种*/
		model[pos++] = NewSanguoGameEngine.instance.soldierList[general.getSoldierId()].getName();
		/*士兵数*/
		model[pos++] = Integer.toString(general.getSoldierCount());
	}
	
	public void updateIndicateGeneralAttribute() {
		General general = getIndicateGeneral();
		generalToViewModel(general, viewModel[curPageGeneralIndex]);
	}

	public void updateCurPageGeneralAttribute() {
		int loopCount = 0;
		if (generalIdList.length >= NUM_GENERAL_LIST_PAGE_ITEMS) {
			loopCount = NUM_GENERAL_LIST_PAGE_ITEMS;
		}
		else {
			loopCount = generalIdList.length;
		}
		for (int i = 0; i < loopCount; ++i) {
			General general = NewSanguoGameEngine.instance.generalList[generalIdList[curPageIndex+i]];
			generalToViewModel(general, viewModel[i]);
		}
	}

	public GeneralListUI(short []generalIdList, int selectedLimit, int type) {
		this.type = type;
		listBg = Resource.loadImage(Resource.PIC_ID_GENERAL_LIST_BG);
		bgX = (short)((NewSanguoGameEngine.instance.getScreenWidth()-listBg.getWidth())>>1);
		bgY = (short)((NewSanguoGameEngine.instance.getScreenHeight()-listBg.getHeight())>>1);
		bgY += 20;
		
		curGroupIndex = 0;
		curBtnIndex = 0;
		this.generalIdList = generalIdList;
		initSelectedList(generalIdList, selectedLimit);
		initViewModel(generalIdList);
		initScrollBar(generalIdList);
		
		initAutoSelect();
	}
	
	private void initAutoSelect() {
		if (type != TYPE_DISTRIBUTE) { 
			autoSelect();
			curGroupIndex = 1;
			if (selectedLen > 0) {
				curBtnIndex = 0;
			}
			else {
				curBtnIndex = 1;
			}
		}
	}

	private void initSelectedList(short[] generalIdList, int selectedLimit) {
		if (selectedLimit > 0) {
			this.selectedLimit = (short)selectedLimit;
		}
		else if(selectedLimit < 0){
			this.selectedLimit = (short)generalIdList.length;
		}
		else {
			throw new RuntimeException("武将选择上限不可以为0");
		}
		this.selectedList = new short[generalIdList.length];
		for (int i = 0; i < selectedList.length; ++i) {
			selectedList[i] = -1;
		}
		this.selectedLen = 0;
	}

	private void initViewModel(short[] generalIdList) {
		viewModel = new String[NUM_GENERAL_LIST_PAGE_ITEMS][];
		int loopCount = 0;
		if (generalIdList.length >= NUM_GENERAL_LIST_PAGE_ITEMS) {
			loopCount = NUM_GENERAL_LIST_PAGE_ITEMS;
		}
		else {
			loopCount = generalIdList.length;
		}
		for (int i = 0; i < loopCount; ++i) {
			viewModel[i] = new String[NUM_GENERAL_ATTRIBUTE];
			generalToViewModel(NewSanguoGameEngine.instance.generalList[generalIdList[i]], viewModel[i]);
		}
	}

	private void initScrollBar(short[] generalIdList) {
		scroll = new ScrollBar();
		scroll.setImage(Resource.loadImage(Resource.PIC_ID_SCROLL_BAR));
		scroll.setViewLen(NUM_GENERAL_LIST_PAGE_ITEMS);
		scroll.setContentLen((short)(generalIdList.length));
		scroll.setCurPage(curPageIndex);
		if (generalIdList.length > NUM_GENERAL_LIST_PAGE_ITEMS) {
			scroll.setTotalPage((short)(generalIdList.length-NUM_GENERAL_LIST_PAGE_ITEMS+1));
		}
		else {
			scroll.setTotalPage((short)(1));
		}
		scroll.setScrollType(ScrollBar.SCROLL_UP_DOWN);
		scroll.setFrameRegion(Resource.REGION_SCROLL_BAR_FRAME);
		scroll.setPosition(13+bgX, 51+bgY);
		scroll.setSize(16, 287);
	}
	
	public void resetGeneralListUI(short []generalIdList, int selectedLimit, int type) {
		this.type = type;
		this.generalIdList = generalIdList;
		initSelectedList(generalIdList, selectedLimit);
		initViewModel(generalIdList);
		initScrollBar(generalIdList);
		curGroupIndex = 0;
		curBtnIndex = 0;
		curPageIndex = 0;
		curPageGeneralIndex = 0;
		considerGeneralState = true;
		
		initAutoSelect();
	}
	
	public General getIndicateGeneral() {
		return NewSanguoGameEngine.instance.generalList[generalIdList[curPageIndex+curPageGeneralIndex]];
	}
	
	public int getSelectedCount() {
		return selectedLen;
	}
	
	public short[] getSelectedList() {
		short[] list = null;
		if (selectedLen > 0) {
			list = new short[selectedLen];
			int pos = 0;
			for (int i = 0; i < selectedList.length; ++i) {
				if (selectedList[i] > 0) {
					list[pos] = generalIdList[i];
					++pos;
				}
			}
		}
		return list;
	}
	
	public void show(SGraphics g) {
		g.drawImage(listBg, bgX, bgY, 20);
		
		Font font = NewSanguoGameEngine.instance.getFont();
		int deltaH = (Resource.H_GENERAL_LIST_ITEM-font.getHeight())>>1;
		String ss = null;
		int sx = bgX;
		int sy = bgY+Resource.Y_GENERAL_LIST_ITEM;
		int loopCount = 0;
		if (generalIdList.length >= NUM_GENERAL_LIST_PAGE_ITEMS) {
			loopCount = NUM_GENERAL_LIST_PAGE_ITEMS;
		}
		else {
			loopCount = generalIdList.length;
		}
		
		int indicatorY = 0;
		for (int i = 0; i < loopCount; ++i, sy+=Resource.H_GENERAL_LIST_ITEM) {
			if (selectedList[curPageIndex+i] > 0) {
				g.setColor(Resource.COLOR_ID_GENERAL_SELECTED_BG);
				g.fillRect(bgX+Resource.X_GENERAL_LIST_ITEM, sy, 
						Resource.W_GENERAL_LIST_ITEM, Resource.H_GENERAL_LIST_ITEM);
			}
			
			int num = 0;
			General general = NewSanguoGameEngine.instance.generalList[generalIdList[curPageIndex+i]];
			if (!considerGeneralState||general.canAssignTask()) {
				g.setColor(Resource.COLOR_ID_GENERAL_NORMAL_TEXT);
			}
			else {
				g.setColor(Resource.COLOR_ID_GENERAL_DISABLE_TEXT);
			}
			
			//将领
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			ss = viewModel[i][num];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//体力
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//精力
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//武力
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//智力
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//魅力
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//将技
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//兵种
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			//兵数
			ss = viewModel[i][num];
			sx = bgX + Resource.XW_GENERAL_LIST_ITEM[num][0];
			g.drawString(ss, sx, sy+deltaH, 20);
			++num;
			
			if (i == curPageGeneralIndex) {
				indicatorY = sy;
			}
		}
		
		if (curGroupIndex == 0) {
			sy = indicatorY;
			g.setColor(0XFFFF00);
			g.drawRect(bgX+Resource.X_GENERAL_LIST_ITEM, sy, 
					Resource.W_GENERAL_LIST_ITEM, Resource.H_GENERAL_LIST_ITEM);
		}
		else {
			sy = indicatorY;
			g.setColor(0XFF00FF);
			g.drawRect(bgX+Resource.X_GENERAL_LIST_ITEM, sy, 
					Resource.W_GENERAL_LIST_ITEM, Resource.H_GENERAL_LIST_ITEM);
			
			int btnX = 528, btnY;
			int btnW = 38, btnH = 74;
			if (curBtnIndex == 0) {
				btnY = 172;
			}
			else {
				btnY = 258;
			}
			DrawUtil.drawRect(g, bgX+btnX, bgY+btnY, btnW, btnH, 3, 0XFFFF00);
		}
		
		scroll.show(g);
	}
	
	public void handle(KeyState key) {
		if (key.containsAndRemove(KeyCode.UP)) {
			if (curGroupIndex == 0) {
				prevItem();
			}
			else {
				if (curBtnIndex > 0) {
					--curBtnIndex;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.DOWN)) {
			if (curGroupIndex == 0) {
				nextItem();
			}
			else {
				if (curBtnIndex < 1) {
					++curBtnIndex;
				}
			}
		}
		
		if (key.containsAndRemove(KeyCode.LEFT)) {
			if (curGroupIndex > 0) {
				--curGroupIndex;
			}
			else {
				isConfirmed = true;
				confirmIndex = 2;
			}
		}
		
		if (key.containsAndRemove(KeyCode.RIGHT)) {
			if (curGroupIndex < 1) {
				++curGroupIndex;
				curBtnIndex = 0;
			}
		}
		
		if (key.containsAndRemove(KeyCode.OK)) {
			if (curGroupIndex == 0) {
				confirmIndex = 0;
				isConfirmed = true;
			}
			else {
				if (curBtnIndex == 0) {
					confirmIndex = 1;
					isConfirmed = true;
				}
				else {
					confirmIndex = 2;
					isConfirmed = true;
				}
			}
		}
		
		/*自动选择*/
		if (key.containsAndRemove(KeyCode.NUM1)) {
			if (selectedLen <= 0) {
				cancelAll();
				initAutoSelect();
			}
		}
		
		/*取消选择*/
		if (key.containsAndRemove(KeyCode.NUM2)) {
			if (selectedLen > 0) {
				cancelAll();
			}
		}
		
		if (key.containsAndRemove(KeyCode.NUM0|KeyCode.BACK)) {
			key.clear();
			confirmIndex = 2;
			isConfirmed = true;
		}
	}
	
	public void nextItem() {
		if (curPageIndex+curPageGeneralIndex < generalIdList.length-1) {
			if (curPageGeneralIndex < NUM_GENERAL_LIST_PAGE_ITEMS-1) {
				++curPageGeneralIndex;
			}
			else {
				++curPageIndex;
				String[] view = viewModel[0];
				for (int i = 0; i < viewModel.length-1; ++i) {
					viewModel[i] = viewModel[i+1];
				}
				viewModel[viewModel.length-1] = view;
				generalToViewModel(NewSanguoGameEngine.instance.generalList[generalIdList[curPageIndex+curPageGeneralIndex]], view);
			}
			scroll.setCurPage(curPageIndex);
		}
	}
	
	public void prevItem() {
		if (curPageIndex+curPageGeneralIndex > 0) {
			if (curPageGeneralIndex > 0) {
				--curPageGeneralIndex;
			}
			else {
				--curPageIndex;
				String[] view = viewModel[viewModel.length-1];
				for (int i = viewModel.length-1; i > 0; --i) {
					viewModel[i] = viewModel[i-1];
				}
				viewModel[0] = view;
				generalToViewModel(NewSanguoGameEngine.instance.generalList[generalIdList[curPageIndex+curPageGeneralIndex]], view);
			}
			scroll.setCurPage(curPageIndex);
		}
	}
	
	public void selecteItem() {
		if (selectedList[curPageIndex+curPageGeneralIndex] > 0) {
			selectedList[curPageIndex+curPageGeneralIndex] = -1;
			--selectedLen;
		}
		else {
			selectedList[curPageIndex+curPageGeneralIndex] = 1;
			++selectedLen;
		}
	}
	
	public void autoSelect() {
		if (type == TYPE_CONVALESCE) {
			General gg = NewSanguoGameEngine.instance.generalList[generalIdList[0]];
			if (gg.isStateNormal()
					&& (gg.getStamina()<Rule.STAMINA_CONSUME_FOR_COMMERCE || gg.getMagic()<Rule.MAGIC_CONSUME_FOR_SKILL)) {
				selectedList[0] = 1;
				++selectedLen;
				for (int i = 1; i < generalIdList.length; ++i) {
					gg = NewSanguoGameEngine.instance.generalList[generalIdList[i]];
					if (gg.isStateNormal() &&(gg.getStamina() < Rule.STAMINA_CONSUME_FOR_COMMERCE
							|| gg.getMagic() < Rule.MAGIC_CONSUME_FOR_SKILL)) {
						selectedList[i] = 1;
						++selectedLen;
					}
					else {
						break;
					}
					if (selectedLen >= selectedLimit) {
						break;
					}
				}
			}
		}
		else if (type == TYPE_MOVE) {
			General gg = NewSanguoGameEngine.instance.generalList[generalIdList[0]];
			if (gg.isStateNormal()&& gg.getStamina() > Rule.STAMINA_CONSUME_FOR_MOVE) {
				selectedList[0] = 1;
				++selectedLen;
				for (int i = 1; i < generalIdList.length; ++i) {
					gg = NewSanguoGameEngine.instance.generalList[generalIdList[i]];
					if (gg.isStateNormal()&&
							gg.getStamina() > Rule.STAMINA_CONSUME_FOR_MOVE) {
						selectedList[i] = 1;
						++selectedLen;
					}
					else {
						break;
					}
					if (selectedLen >= selectedLimit) {
						break;
					}
				}
			}
		}
		else if (type == TYPE_EXPEDITION) {
			General gg = NewSanguoGameEngine.instance.generalList[generalIdList[0]];
			if (gg.isStateNormal()&& gg.getStamina() > Rule.STAMINA_CONSUME_FOR_EXPEDITION
					&& gg.getSoldierCount() > 0) {
				selectedList[0] = 1;
				++selectedLen;
				for (int i = 1; i < generalIdList.length; ++i) {
					gg = NewSanguoGameEngine.instance.generalList[generalIdList[i]];
					if (gg.isStateNormal()&& gg.getStamina() > Rule.STAMINA_CONSUME_FOR_MOVE
							&& gg.getSoldierCount() > 0) {
						selectedList[i] = 1;
						++selectedLen;
					}
					else {
						break;
					}
					if (selectedLen >= selectedLimit) {
						break;
					}
				}
			}
		}
		else if (type == TYPE_DISTRIBUTE) {
			//什么也不做
		}
		else {
			General gg = NewSanguoGameEngine.instance.generalList[generalIdList[0]];
			if (gg.isStateNormal()&& gg.getStamina() > Rule.STAMINA_CONSUME_FOR_COMMERCE) {
				selectedList[0] = 1;
				++selectedLen;
				for (int i = 1; i < generalIdList.length; ++i) {
					gg = NewSanguoGameEngine.instance.generalList[generalIdList[i]];
					if (gg.isStateNormal()&&
							gg.getStamina() > Rule.STAMINA_CONSUME_FOR_COMMERCE) {
						selectedList[i] = 1;
						++selectedLen;
					}
					else {
						break;
					}
					if (selectedLen >= selectedLimit) {
						break;
					}
				}
			}
			
		}
	}
	
	public void cancelAll() {
		if (type != TYPE_DISTRIBUTE) {
			for (int i = 0; i < selectedList.length; ++i) {
				selectedList[i] = -1;
			}
		}
		selectedLen = 0;
		curGroupIndex = 0;
	}

	public void setConsiderGeneralState(boolean considerGeneralState) {
		this.considerGeneralState = considerGeneralState;
	}

	public boolean isConsiderGeneralState() {
		return considerGeneralState;
	}

	public int getConfirmIndex() {
		return confirmIndex;
	}

	public void setConfirmed(boolean isConfirmed) {
		this.isConfirmed = isConfirmed;
	}

	public boolean isConfirmed() {
		return isConfirmed;
	}
	
	public boolean isConfirmItem() {
		return isConfirmed&&(confirmIndex==0);
	}
	
	public boolean isConfirmOk() {
		return isConfirmed&&(confirmIndex==1);
	}
	
	public boolean isConfirmBack() {
		return isConfirmed&&(confirmIndex == 2);
	}
	
	public void resetConfirmFlag() {
		isConfirmed = false;
	}

}
