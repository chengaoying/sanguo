package sanguo;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.lcdui.Image;

import cn.ohyeah.stb.res.UIResource;
import cn.ohyeah.stb.ui.ISprite;
import cn.ohyeah.stb.ui.PopupConfirm;
import cn.ohyeah.stb.ui.PopupIconText;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.ui.RegularSprite;

public class Resource {
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	
	/*Color ID*/
	public static final int COLOR_ID_LIST_ITEM_SELECTED_BG = 0X406190;
	public static final int COLRO_ID_LIST_ITEM_INDICATE_RECT = 0XFFFF00;
	public static final int COLOR_ID_LIST_ITEM_NORMAL_TEXT = 0Xfffbbf;
	public static final int COLOR_ID_LIST_ITEM_HILIGHT_TEXT = 0Xfffbbf;
	public static final int COLOR_ID_LIST_ITEM_DISABLE_TEXT = 0X8E726F;
	
	public static final int COLOR_ID_MENU_HILIGHT_TEXT = 0;
	public static final int COLOR_ID_MENU_NORMAL_TEXT = 0Xfffbbf;
	
	//public static final int COLOR_ID_MAP_CITY_NAME_TEXT = 0xFFED76;
	public static final int COLOR_ID_MAP_TOP_AGE_TEXT = 0xF0FFFD;
	public static final int COLOR_ID_MAP_CITY_INFO_TEXT = 0xD4D6D5;
	
	public static final int COLOR_ID_CITY_MENU_NORMAL_TEXT = 0xFFF1D2;
	public static final int COLOR_ID_CITY_MENU_DISABLE_TEXT = 0x8E726F;
	public static final int COLOR_ID_CITY_INTRO_TEXT = 0xFFFFE7;
	
	public static final int COLOR_ID_GENERAL_SELECTED_BG = 0x406190;
	public static final int COLOR_ID_GENERAL_NORMAL_TEXT = 0Xfffbbf;
	public static final int COLOR_ID_GENERAL_DISABLE_TEXT = 0x8E726F;
	
	public static final int COLOR_ID_FIGHTER_SELECTED_BG = 0x406190;
	public static final int COLOR_ID_FIGHTER_NORMAL_TEXT = 0Xfffbbf;
	public static final int COLOR_ID_FIGHTER_DISABLE_TEXT = 0x8E726F;
	
	public static final int COLOR_ID_CONFIRM_TEXT = 0XFFFFFF;
	public static final int COLOR_ID_CONFIRM_BTN_NORMAL = 0XFFFFFF;
	public static final int COLOR_ID_CONFIRM_BTN_HILIGHT = 0XFFFFFF;
	
	public static final int COLOR_ID_COMMON_BTN_TEXT = 0XF6CE37;
	
	/*Image ID*/
	public static short NUM_PICS = 0;
	
	public static final short PIC_ID_LOGO = NUM_PICS++;
	public static final short PIC_ID_CURSOR = NUM_PICS++;
	public static final short PIC_ID_WAIT = NUM_PICS++;
	public static final short PIC_ID_BOTTOM_INFO_BG = NUM_PICS++;
	public static final short PIC_ID_CONFIRM_TEXT_BG = NUM_PICS++;
	public static final short PIC_ID_CONFIRM_BTN_BG = NUM_PICS++;
	public static final short PIC_ID_SCROLL_BAR = NUM_PICS++;
	public static final short PIC_ID_NUMBER = NUM_PICS++;
	public static final short PIC_ID_PLUS = NUM_PICS++;
	
	public static final short PIC_ID_MAIN_BG = NUM_PICS++;
	public static final short PIC_ID_MAIN_MENU_BG = NUM_PICS++;
	public static final short PIC_ID_CHOICE_BG = NUM_PICS++;
	public static final short PIC_ID_RANK_TITLE = NUM_PICS++;
	public static final short PIC_ID_RANK_ITEM = NUM_PICS++;
	public static final short PIC_ID_RANK_FOOT = NUM_PICS++;
	public static final short PIC_ID_COLLECT = NUM_PICS++;

	public static final short PIC_ID_MAP_TOP_BG = NUM_PICS++;
	public static final short PIC_ID_MAP_BG = NUM_PICS++;
	public static final short PIC_ID_SYSTEM_MENU_BG = NUM_PICS++;
	public static final short PIC_ID_CITY_MENU_BG = NUM_PICS++;
	public static final short PIC_ID_CITY_SUB_MENU_BG = NUM_PICS++;
	public static final short PIC_ID_CITY_INTRO_BG = NUM_PICS++;
	public static final short PIC_ID_TRADE_BG = NUM_PICS++;
	public static final short PIC_ID_DRAFT_BG = NUM_PICS++;
	public static final short PIC_ID_DISTRIBUTE_BG = NUM_PICS++;
	public static final short PIC_ID_TRANSFER_BG = NUM_PICS++;
	public static final short PIC_ID_CITY_COMMERCE_ENHANCE_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_FARMING_ENHANCE_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_ATTACK_ENHANCE_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_DEFENSE_ENHANCE_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_COMMERCE_ENHANCE_INVALID_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_FARMING_ENHANCE_INVALID_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_ATTACK_ENHANCE_INVALID_ICON = NUM_PICS++;
	public static final short PIC_ID_CITY_DEFENSE_ENHANCE_INVALID_ICON = NUM_PICS++;
	public static final short PIC_ID_WIN = NUM_PICS++;
	public static final short PIC_ID_CITY_BORDER = NUM_PICS++;
	public static final short PIC_ID_MAIN_CITY_BORDER = NUM_PICS++;
	public static final short PIC_ID_USE_PROP_BG = NUM_PICS++;
	public static final short PIC_ID_RESOURCE_ICON = NUM_PICS++;
	
	public static final short PIC_ID_SEIGNEUR_HEAD = NUM_PICS++;
	public static final short PIC_ID_SEIGNEUR_FLAG = NUM_PICS++;
	
	public static final short PIC_ID_GENERAL_LIST_BG = NUM_PICS++;
	public static final short PIC_ID_AWARD_BG = NUM_PICS++;
	public static final short PIC_ID_AWARD_PROP_LIST_ARROW = NUM_PICS++;
	
	public static final short PIC_ID_BATTLE_FILED_BG = NUM_PICS++;
	public static final short PIC_ID_FIGHTER_LIST_BG = NUM_PICS++;
	public static final short PIC_ID_FIGHT_MENU_BG = NUM_PICS++;
	public static final short PIC_ID_FIGHTER_LIST_GENERAL_ATTRIBUTE_BG = NUM_PICS++;
	public static final short PIC_ID_FIGHTER_LIST_ARMY_ATTRIBUTE_BG = NUM_PICS++;
	public static final short PIC_ID_BATTLE_BTN_BG = NUM_PICS++;
	public static final short PIC_ID_FIGHT_TITLE = NUM_PICS++;
	public static final short PIC_ID_CD_BG = NUM_PICS++;
	public static final short PIC_ID_CD_PROGRESS = NUM_PICS++;
	public static final short PIC_ID_MAGIC_STAR = NUM_PICS++;
	public static final short PIC_ID_MAGIC_STAR_VALID = NUM_PICS++;
	public static final short PIC_ID_MAGIC = NUM_PICS++;
	
	public static final short PIC_ID_ARCHER_LEFT = NUM_PICS++;
	public static final short PIC_ID_ARCHER_RIGHT = NUM_PICS++;
	public static final short PIC_ID_CAVALRY_LEFT = NUM_PICS++;
	public static final short PIC_ID_CAVALRY_RIGHT = NUM_PICS++;
	public static final short PIC_ID_INFANTRY_LEFT = NUM_PICS++;
	public static final short PIC_ID_INFANTRY_RIGHT = NUM_PICS++;
	public static final short PIC_ID_ARCHER_LEFT_ATTACK = NUM_PICS++;
	public static final short PIC_ID_ARCHER_RIGHT_ATTACK = NUM_PICS++;
	public static final short PIC_ID_CAVALRY_LEFT_ATTACK = NUM_PICS++;
	public static final short PIC_ID_CAVALRY_RIGHT_ATTACK = NUM_PICS++;
	public static final short PIC_ID_INFANTRY_LEFT_ATTACK = NUM_PICS++;
	public static final short PIC_ID_INFANTRY_RIGHT_ATTACK = NUM_PICS++;
	
	public static final short PIC_ID_RECORD_SAVE_TITLE = NUM_PICS++;
	public static final short PIC_ID_RECORD_LOAD_TITLE = NUM_PICS++;
	public static final short PIC_ID_RECORD_BG_BODY = NUM_PICS++;
	public static final short PIC_ID_RECORD_ITEM_BG = NUM_PICS++;
	
	public static final short PIC_ID_MARKET_BG = NUM_PICS++;
	public static final short PIC_ID_HELP_BG = NUM_PICS++;
	
	/*Image Source*/
	private static final String[] imageSrc = {
		"/common/Logo.png",
		"/common/Cursor.png",
		"/common/Wait.png",
		"/common/BottomInfoBg.png",
		"/common/popup-bg.png",
		"/common/popup-btn.png",
		"/common/ScrollBar.png",
		"/common/Number.png",
		"/common/Plus.png",
		
		"/main/MainBg.jpg",
		"/main/MainMenuBg.png",
		"/main/ChoiceBg.jpg",
		"/main/RankTitle.png",
		"/main/RankItem.jpg",
		"/main/RankFoot.jpg",
		"/main/Collect.png",
		
		"/map/MapTopBg.jpg",
		"/map/MapBg.jpg",
		"/map/SystemMenuBg.png",
		"/map/CityMenuBg.png",
		"/map/CitySubMenuBg.png",
		"/map/CityIntroBg.png",
		"/map/TradeBg.png",
		"/map/DraftBg.png",
		"/map/DistributeBg.png",
		"/map/TransferBg.png",
		"/map/CityCommerceEnhance.png",
		"/map/CityFarmingEnhance.png",
		"/map/CityAttackEnhance.png",
		"/map/CityDefenseEnhance.png",
		null,
		null,
		null,
		null,
		"/map/Win.jpg",
		"/map/CityBorder.png",
		"/map/MainCityBorder.png",
		"/map/UseProp.png",
		"/map/Resource.png",
		
		"/seigneur/SeigneurHead.png",
		"/seigneur/SeigneurFlag.png",
		
		"/general/GeneralListBg.png",
		"/general/AwardBg.png",
		"/general/ArrowLR.png",
		
		"/battle/BattleFieldBg.jpg",
		"/battle/FighterListBg.png",
		"/battle/FightMenuBg.png",
		"/battle/GeneralAttributeBg.png",
		"/battle/ArmyAttributeBg.png",
		"/battle/ButtonBg.png",
		"/battle/FightTitle.png",
		"/battle/CdBg.png",
		"/battle/CdProgress.png",
		"/battle/MagicStar.png",
		"/battle/MagicStar.png",
		"/magic/Magic.png",
		
		"/soldier/ArcherLeft.png",
		"/soldier/ArcherRight.png",
		"/soldier/CavalryLeft.png",
		"/soldier/CavalryRight.png",
		"/soldier/InfantryLeft.png",
		"/soldier/InfantryRight.png",
		"/soldier/ArcherLeftAttack.png",
		"/soldier/ArcherRightAttack.png",
		"/soldier/CavalryLeftAttack.png",
		"/soldier/CavalryRightAttack.png",
		"/soldier/InfantryLeftAttack.png",
		"/soldier/InfantryRightAttack.png",
		
		"/record/RecordSaveTitle.png",
		"/record/RecordLoadTitle.png",
		"/record/RecordBgBody.png",
		"/record/RecordItemBg.png",
		
		"/market/MarketBg.png",
		"/help/HelpBg.jpg",
	};
	
	/*Image Loader*/
	private static final Image[] images = new Image[NUM_PICS];
	
	public static Image loadImage(int id) {
		if (images[id] == null) {
			try {
				images[id] = Image.createImage(imageSrc[id]);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return images[id];
	}
	
	public static Image loadImage(String path) {
		Image image = null;
		try {
			image = Image.createImage(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return image;
	}
	
	public static void freeImage(int id) {
		images[id] = null;
	}
	
	public static Image getImage(int id) {
		return images[id];
	}
	
	public static void setImage(int id, Image img) {
		images[id] = img;
	}
	
	/*common*/
	public static final short W_CONFIRM_BTN = 60;
	public static final short H_CONFIRM_BTN = 27;
	public static final short[][] REGION_SCROLL_BAR_FRAME = {
		{0, 0, 16, 8},
		{0, 8, 16, 87},
		{0, 95, 16, 8}
	};
	
	/*使用道具界面*/
	public static final short X_USE_PROP_ICON = 56;
	public static final short Y_USE_PROP_ICON = 65;
	public static final short XSPACE_USE_PROP_ICON = 92;
	public static final short X_USE_PROP_BTN = 35;
	public static final short Y_USE_PROP_BTN = 233;
	public static final short XSPACE_USE_PROP_BTN = 74;
	public static final short H_USE_PROP_TEXT = 22;
	
	/*主菜单*/
	public static final short POS_MAIN_MENU[][] = {
		{424, 203},
		{424, 262-5},
		{424, 321-5*2},
		{424, 380-5*3},
		{424, 439-5*4}
	};
	
	/*君主选择坐标*/
	public static final short POS_SEIGNEUR_CHOICE[][] = {
		{161, 144},	
		{299, 144}, 
		{442, 144},
		{161, 290},	
		{299, 290},	
		{442, 290}
	};
	
	public static final String[] STR_SEIGNEUR_NAME = {
		"袁绍", "刘备", "董卓", "刘璋", "曹操", "孙策"
	};
	
	public static final byte[] NUM_SEIGNEUR_CITYS = {
		2, 1, 3, 2, 1, 2
	};
	
	public static final byte[] NUM_SEIGNEUR_GENERALS = {
		7, 4, 9, 5, 4, 6
	};
	
	/*城池布局*/
	public static final short LAYOUT_CITY[][] = {
		{262, 112, -1, 4, -1, 1},		/*0*/
		{364, 130, -1, 6, 0, 2},		/*1*/
		{447, 109, -1, -1, 1, 7},		/*2*/
		{133, 160, -1, 8, -1, 4},		/*3*/
		{210, 201, 0, 8, 3, 5},			/*4*/
		{279, 217, -1, 12, 4, 9},		/*5(大城)*/
		{427, 197, 1, 10, 9, 7},		/*6*/
		{525, 155, 2, 11, 6, -1},		/*7*/
		{135, 226, 3, 12, -1, 4},		/*8*/
		{361, 244, 6, 13, 5, 10},		/*9*/
		{458, 237, 6, 13, 9, 11},		/*10(大城)*/
		{534, 218, 7, -1, 10, -1},		/*11*/
		{192, 263, 8, 15, 14, 5},		/*12*/
		{423, 281, 10, 17, 9, 18},		/*13*/
		{133, 302, 12, 20, 19, 15},		/*14(大城)*/
		{219, 324, 12, 20, 14, 22},		/*15*/
		{342, 320, -1, 23, 22, 17},		/*16*/
		{431, 333, 13, 24, 16, 18},		/*17*/
		{490, 304, 13, 24, 17, -1},		/*18(大城)*/
		{40, 372, 14, -1, -1, 20},		/*19*/
		{148, 365, 14, 21, 19, 15},		/*20*/
		{248, 411, -1, -1, 20, -1},		/*21*/
		{286, 354, 16, -1, 15, 23},		/*22*/
		{384, 376, 16, -1, 22, -1},		/*23*/
		{536, 347, 18, -1, 17, -1}		/*24*/
	};
	
	public static short getUpCityId(short cityId) {
		return LAYOUT_CITY[cityId][2];
	}
	
	public static City getUpCity(short cityId) {
		City city = null;
		int upCityId = getUpCityId(cityId);
		if (upCityId >= 0) {
			city = engine.cityList[upCityId];
		}
		return city;
	}
	
	public static short getDownCityId(short cityId) {
		return LAYOUT_CITY[cityId][3];
	}
	
	public static City getDownCity(short cityId) {
		City city = null;
		int upCityId = getDownCityId(cityId);
		if (upCityId >= 0) {
			city = engine.cityList[upCityId];
		}
		return city;
	}
	
	public static short getLeftCityId(short cityId) {
		return LAYOUT_CITY[cityId][4];
	}
	
	public static City getLeftCity(short cityId) {
		City city = null;
		int upCityId = getLeftCityId(cityId);
		if (upCityId >= 0) {
			city = engine.cityList[upCityId];
		}
		return city;
	}
	
	public static short getRightCityId(short cityId) {
		return LAYOUT_CITY[cityId][5];
	}
	
	public static City getRightCity(short cityId) {
		City city = null;
		int upCityId = getRightCityId(cityId);
		if (upCityId >= 0) {
			city = engine.cityList[upCityId];
		}
		return city;
	}
	
	public static boolean isConnected(short srcCityId, short targetCityId) {
		boolean connected = false;
		if (LAYOUT_CITY[srcCityId][2] == targetCityId) {
			connected = true;
		}
		else if (LAYOUT_CITY[srcCityId][3] == targetCityId) {
			connected = true;
		}
		else if (LAYOUT_CITY[srcCityId][4] == targetCityId) {
			connected = true;	
		}
		else if (LAYOUT_CITY[srcCityId][5] == targetCityId) {
			connected = true;
		}
		else {
			connected = false;
		}
		return connected;
	}
	
	/*系统菜单*/
	public static final short POS_SYSTEM_MENU[][] = {
		{235, 125+48-40},
		{235, 125+48*2-40},
		{235, 125+48*3-40},
		{235, 125+48*4-40},
		{235, 125+48*5-40},
		{235, 125+48*6-40},
		{235, 125+48*7-40}
	};
	
	/*游戏菜单*/
	public static final String[] STR_PLAYER_GAME_MENU = {"进入城池","道具商城","武将市场", "结束回合"};
	public static final String[] STR_OTHER_GAME_MENU = {"返回主城","道具商城","武将市场", "结束回合"};
	
	/*城池菜单*/
	public static final short W_CITY_MENU_BG = 86;
	public static final short H_CITY_MENU_BG = 40;
	public static final short W_CITY_SUB_MENU_BG = 107;
	public static final short H_CITY_SUB_MENU_BG = 35;
	
	/*城市菜单背景*/
	public static final short X_CITY_MENU_BG = 206;
	public static final short Y_CITY_MENU_BG = 171;
	public static final short YSPACE_CITY_MENU_BG = 50;
	
	public static final short X_CITY_INTRO_INFO = 63;
	public static final short Y_CITY_INTRO_INFO = 14;
	public static final short YSPACE_CITY_INTRO_INFO = 29;
	public static final short XSPACE_INTRO_PLUS = 75;
	
	public static final String STR_CITY_MENU[] = { "开发", "军事", "将领", "市场"};
	//public static final String STR_CITY_INTRO[] = { "农业", "商业", "人口", "民忠", "武将数", "士兵数"};
	public static final String STR_CITY_SUB_MENU[][] = {
		{ "农业", "商业", "救济"},
		{ "征兵", "分配", "出征", "输送"},
		{ "搜索", "修养", "调动", "授予"}, 
		{ "买粮", "卖粮"}
	};

	public static final String STR_CITY_SUB_MENU_HELP[][] = {
		{ "增加城池农业指数", "增加城池商业指数", "增加城池民忠"},
		{ "征集士兵", "重新分配士兵", "攻打城池", "在城池间输送资源"},
		{ "在城内搜索", "恢复武将体力和精力", "在己方城池之间移动", "使用道具"},
		{ "用金币购买粮食", "卖出粮食获得金币"}
	};
	
	public static final short XW_GENERAL_LIST_ITEM[][] = {
		{49+2+2, 86},	//将领
		{121+2+2, 50},	//体力
		{169+2+2, 46},	//精力
		{213+6+4, 47},	//武力
		{260+6+4, 46},	//智力
		{306+6+4, 46},	//魅力
		{352+6+4, 46},	//将技
		{406+4+2, 46},	//兵种
		{469+4, 67},	//士兵数
	};
	
	public static final short X_GENERAL_LIST_ITEM = 26+12;
	public static final short Y_GENERAL_LIST_ITEM = 53;
	public static final short W_GENERAL_LIST_ITEM = 497-12;
	public static final short H_GENERAL_LIST_ITEM = 26;
	
	public static final String STR_GENERAL_ITEM[] = { "将领", "体力", "精力", "武力", "智力", "魅力","将技", "兵种", "士兵数"};
	
	/*征兵界面*/
	public static final short X_DRAFT_SOLDIER_REDIF = 106;
	public static final short Y_DRAFT_SOLDIER_REDIF = 125;
	public static final short W_DRAFT_SOLDIER_REDIF = 97;
	public static final short H_DRAFT_SOLDIER_REDIF = 26;

	public static final short X_DRAFT_SOLDIER_COUNT = 261;
	public static final short Y_DRAFT_SOLDIER_COUNT = 125;
	public static final short W_DRAFT_SOLDIER_COUNT = 64;
	public static final short H_DRAFT_SOLDIER_COUNT = 26;
	public static final short YSPACE_DRAFT_SOLDIER_COUNT = 45;

	public static final short X_DRAFT_COIN_COMSUMPTION = 408;
	public static final short Y_DRAFT_COIN_COMSUMPTION = 125;
	public static final short W_DRAFT_COIN_COMSUMPTION = 64;
	public static final short H_DRAFT_COIN_COMSUMPTION = 26;

	public static final short X_DRAFT_LEFT_CONFIRM_BTN = 207;
	public static final short Y_DRAFT_LEFT_CONFIRM_BTN = 271;

	public static final short X_DRAFT_RIGHT_CONFIRM_BTN = 314;
	public static final short Y_DRAFT_RIGHT_CONFIRM_BTN = 271;
	
	/*兵力分配界面*/
	public static final short YSPACE_DISTRIBUTE_GENERAL_ATTRIBUTE = 30;
	public static final short W_DISTRIBUTE_GENERAL_SOLDIER = 95;
	public static final short H_DISTRIBUTE_GENERAL_SOLDIER = 22;
	public static final short X_DISTRIBUTE_GENERAL_SOLDIER = 139;
	public static final short Y_DISTRIBUTE_GENERAL_SOLDIER_NAME = 266;
	public static final short Y_DISTRIBUTE_GENERAL_SOLDIER_COUNT = 294;
	
	public static final short X_DISTRIBUTE_CITY_SOLDIER_COUNT = 417;
	public static final short Y_DISTRIBUTE_CITY_SOLDIER_COUNT = 120;
	public static final short W_DISTRIBUTE_CITY_SOLDIER_COUNT = 64;
	public static final short H_DISTRIBUTE_CITY_SOLDIER_COUNT = 26;
	
	public static final short YSPACE_DISTRIBUTE_CITY_SOLDIER_COUNT = 48;
	public static final short Y_DISTRIBUTE_CONFIRM_BTN = 334;
	public static final short X_DISTRIBUTE_LEFT_CONFIRM_BTN = 207;
	public static final short X_DISTRIBUTE_RIGHT_CONFIRM_BTN = 314;
	
	/*资源输送*/
	public static final short X_TRANSFER_RES = 97;
	public static final short Y_TRANSFER_RES = 133;
	public static final short W_TRANSFER_RES = 111;
	public static final short H_TRANSFER_RES = 31;
	public static final short XSPACE_TRANSFER_LEFT_RES = 165;
	public static final short YSPACE_TRANSFER_LEFT_RES = 68;
	public static final short XSPACE_TRANSFER_RES = 301;
	public static final short YSPACE_TRANSFER_RES = 68;
	public static final short X_TRANSFER_LEFT_BTN = 207;
	public static final short X_TRANSFER_RIGHT_BTN = 314;
	public static final short Y_TRANSFER_BTN = 271;
	
	/*粮食买卖*/
	public static final short X_TRADE_TYPE = 146;
	public static final short Y_TRADE_TYPE = 74;
	public static final short W_TRADE_TYPE = 64;
	public static final short H_TRADE_TYPE = 26;
	
	public static final short X_TRADE_GOODS_TYPE = 78;
	public static final short Y_TRADE_GOODS_TYPE = 112;
	public static final short W_TRADE_GOODS_TYPE = 61;
	public static final short H_TRADE_GOODS_TYPE = 39;
	
	public static final short XSPACE_TRADE_GOODS_TYPE = 70;
	public static final short W_TRADE_CITY_INFO = 110;
	public static final short H_TRADE_CITY_INFO = 26;

	public static final short X_TRADE_LEFT_BTN = 106;
	public static final short X_TRADE_RIGHT_BTN = 209;
	public static final short Y_TRADE_BTN = 261;
	
	/*授予*/
	public static final short X_AWARD_BG = 70;
	public static final short Y_AWARD_BG = 120;
	public static final short NUM_AWARD_GENERAL_LIST_ITEMS = 5;
	public static final short X_AWARD_GENERAL_LIST = 25;
	public static final short Y_AWARD_GENERAL_LIST = 103;
	public static final short W_AWARD_GENERAL_LIST = 83;
	public static final short H_AWARD_GENERAL_LIST = 25;
	public static final short YSPACE_AWARD_GENERAL_LIST = 33;
	
	public static final short H_AWARD_GENERAL_INFO = 22;
	public static final short YSPACE_AWARD_GENERAL_INFO = 32;
	public static final short XSPACE_AWARD_GENERAL_PLUS = 64;

	/*对战武将选择*/
	public static final short NUM_FIGHTER_LIST_PAGE_ITEMS = 3;
	public static final short X_ATTACK_FIGHTER_LIST_BG = 0;
	public static final short X_DEFENSE_FIGHTER_LIST_BG = 450;
	public static final short Y_FIGHTER_LIST_BG = 129;

	public static final short X_ATTACK_FIGHTER_LIST_ITEM = 7;
	public static final short X_DEFENSE_FIGHTER_LIST_ITEM = 34;
	public static final short Y_FIGHTER_LIST_ITEM = 47;
	
	public static final short X_FIGHTER_LIST_GENERAL_NAME = 1;
	public static final short Y_FIGHTER_LIST_GENERAL_NAME = 1;
	public static final short W_FIGHTER_LIST_GENERAL_NAME = 149;
	public static final short H_FIGHTER_LIST_GENERAL_NAME = 31;
	
	public static final short[][] POS_FIGHTER_LIST_GENERAL_ATTRIBUTE = {
		{36, 35},	//武力
		{107, 35},	//智力
		{36, 67},	//精力
		{107, 67}	//将技
	};

	public static final short X_FIGHT_ARMY_ATTRIBUTE = 53;
	public static final short Y_FIGHT_ARMY_ATTRIBUTE = 34;
	public static final short YSPACE_FIGHT_ARMY_ATTRIBUTE = 32;
	
	/*战斗*/
	public static final int SOLDIER_COUNT_PER_SPRITE = 100;		/*每个精灵代表的士兵数*/
	
	public static final short W_BATTLE_FIELD_ARMY_INFO = 72;
	public static final short H_BATTLE_FIELD_ARMY_INFO = 28;
	
	public static final short[][] POS_BATTLE_FIELD_ATTACK_ARMY_INFO = {
		{104, 7},	//攻
		{216, 7},	//防
		{104, 40},	//兵
		{216, 40}	//将
	};
	
	public static final short[][] POS_BATTLE_FIELD_DEFENSE_ARMY_INFO = {
		{494, 7},	//攻
		{388, 7},	//防
		{494, 40},	//兵
		{388, 40}	//将
	};
	
	/*士兵普通状态的帧信息*/
	public static final short [][] PIC_INFO_SOLDIERS = {
		{PIC_ID_INFANTRY_LEFT, PIC_ID_INFANTRY_RIGHT, 32, 54},	/*步兵*/
		{PIC_ID_CAVALRY_LEFT, PIC_ID_CAVALRY_RIGHT, 70, 72},	/*骑兵*/
		{PIC_ID_ARCHER_LEFT, PIC_ID_ARCHER_RIGHT, 54, 68}		/*弓兵*/
	};
	
	/*0:站立状态,1:移动状态,2:攻击状态,3:倒地状态*/
	public static final byte [][][]SEQ_SOLDIERS = {
		{{0,1,2,3},{0,0,1,1}},
		{{0,1,2,3},{0,1,2,3}},
		{{0,1,2,3},{0,0,1,1}}
	};
	
	/*士兵攻击状态的帧信息*/
	public static final short[][] PIC_INFO_SOLDIERS_ATTACK = {
		{PIC_ID_INFANTRY_LEFT_ATTACK, PIC_ID_INFANTRY_RIGHT_ATTACK, 46, 56},
		{PIC_ID_CAVALRY_LEFT_ATTACK, PIC_ID_CAVALRY_RIGHT_ATTACK, 84, 100},
		{PIC_ID_ARCHER_LEFT_ATTACK, PIC_ID_ARCHER_RIGHT_ATTACK, 54, 68}
	};
	
	/*攻击帧修正*/
	public static final short[][] PIC_CORRECTION_SOLDIERS_ATTACK = {
		{14, 2},
		{14, 28},
		{0, 0}
	};
	
	/*左边士兵坐标*/
	public static final short POS_ATTACK_SOLDIER[][] = { 
		{ 88, 194 + 30},
		{ 88+4, 194 + 35 + 30},
		{ 88, 194 + 35 * 2 + 30},
		{ 88+4, 194 + 35 * 3 + 30},
		
		{ 48, 179 + 30},
		{ 48+4, 179 + 35 * 1 + 30},
		{ 48, 179 + 35 * 2 + 30},
		{ 48+4, 179 + 35 * 3 + 30},
		
		{ 8, 194 + 30},
		{ 8+4, 194 + 35 * 1 + 30},
		{ 8, 194 + 35 * 2 + 30},
		{ 8+4, 194 + 35 * 3 + 30},
		
		{ 128, 179 + 30},
		{ 128+4, 179 + 35 * 1 + 30},
		{ 128, 179 + 35 * 2 + 30},
		{ 128+4, 179 + 35 * 3 + 30},
		
		{88, 194 + 35 * 4 + 30},
		{48+4, 179 +  35 * 4 + 30},
		{8, 194 + 35 * 4 + 30},
		{128+4, 179 + 35 * 4 + 30},
		
		{88, 194 - 35 + 30},
		{48+4, 179 - 35 + 30},
		{8, 194 - 35 + 30},
		{128+4, 179 - 35 + 30}
	};
	
	/*右边士兵坐标*/
	public static final short POS_DEFENSE_SOLDIER[][] = {
		{ 482, 194 + 30},
		{ 482+4, 194 + 35 * 1 + 30},
		{ 482, 194 + 35 * 2 + 30 },
		{ 482+4, 194 + 35 * 3 + 30 },

		{ 528, 179 + 30},
		{ 528+4, 179 + 35 * 1 + 30 },
		{ 528, 179 + 35 * 2 + 30 },
		{ 528+4, 179 + 35 * 3 + 30 },

		{ 562, 194 + 30},
		{ 562+4, 194 + 35 * 1 + 30 },
		{ 562, 194 + 35 * 2 + 30 },
		{ 562+4, 194 + 35 * 3 + 30 },
		
		{ 442, 179 + 30},
		{ 442+4, 179 + 35 * 1 + 30 },
		{ 442, 179 + 35 * 2 + 30 },
		{ 442+4, 179 + 35 * 3 + 30 },
		
		{482, 194 + 30},
		{528+4, 179 + 35 * 1 + 30},
		{562, 194 + 35 * 2 + 30},
		{442+4, 179 + 35 * 3 + 30},
		
		{482, 194 - 35 + 30 },
		{528+4, 179 - 35 + 30 },
		{562, 194 - 35 + 30 },
		{442+4, 179 - 35 + 30 }
	};
	
	/*战斗菜单*/
	public static final String[] STR_FIGHT_MENU = {"返回战场", "武将计", "原地待命", "全军冲锋", "全军撤退"};
	public static final short X_FIGHT_MENU_ITEM = 12;
	public static final short Y_FIGHT_MENU_ITEM = 17;
	public static final short W_FIGHT_MENU_ITEM = 166;
	public static final short H_FIGHT_MENU_ITEM = 28;
	
	
	/*武将计显示位置*/
	public static final short POS_MAGIC_SHOW[][] = {
		{ 124, 248 }, { 125,318 }, { 280, 251 }, { 418, 240 },
		{ 84, 358 }, { 223, 383 }, { 416, 389 }, { 473, 341 }
	};
	
	/*武将计帧信息*/
	public static final short REGION_MAGIC[][] = { //[0]x, [1]y, [2]w,[3]h
		{ 284, 228, 18, 20 }, // 落石
		{ 304, 223, 41, 41 },
		{ 304, 223, 41, 41 },
		{ 348, 205, 40, 79 }, 
		{ 348, 205, 40, 79 },
		
		{ 389, 202, 42, 82 }, // 风
		{ 0, 145, 96, 50 },
		{ 389, 202, 42, 82 },
		{ 0, 145, 96, 50 },
		{ 191, 202, 88, 82 },
		
		{ 107, 143, 56, 59 }, // 火
		{ 163, 140, 56, 62 },
		{ 107, 143, 56, 59 },
		{ 163, 140, 56, 62 },
		{ 219, 131, 57, 71 },
		
		{ 353, 123, 77, 79 }, // 冰
		{ 353, 123, 77, 79 },
		{ 353, 123, 77, 79 },
		{ 94, 202, 97, 85 },
		{ 0, 196, 94, 91 },

		{ 276, 154, 72, 66 }, // 光环
		{ 277, 83, 70, 70 },
		{ 271, 0, 82, 82 },
		{ 277, 83, 70, 70 },
		{ 276, 154, 72, 66 },
		
		{ 353, 69, 77, 51 }, // 死神
		{ 353, 23, 77, 78 },
		{ 353, 0, 77, 120 },
		{ 353, 23, 77, 78 },
		{ 353, 69, 77, 51 }
		
	};
	
	public static int getMagicPos(General general) {
		int power = general.getMagicPower();
		int pos = 0;
		if (power >= 90) {
			pos = 5*5;
		}
		else if (power >= 80) {
			pos = 4*5;
		}
		else if (power >= 70) {
			pos = 3*5;
		}
		else if (power >= 60) {
			pos = 2*5;
		}
		else if (power >= 40) {
			pos = 1*5;
		}
		else {
			pos = 0;
		}
		return pos;
	}
	
	/*存档*/
	public static final short X_RECORD_BG = 9;
	public static final short Y_RECORD_BG = 87;
	public static final short W_RECORD_ITEM_BG = 551;
	public static final short H_RECORD_ITEM_BG = 47;
	
	
	/*商城*/
	public static final short X_MARKET_PROP_LIST_ITEM = 221;
	public static final short Y_MARKET_PROP_LIST_ITEM = 80;
	public static final short W_MARKET_PROP_LIST_ITEM = 189;
	public static final short H_MARKET_PROP_LIST_ITEM = 99;
	
	public static final short X_MARKET_PROP_LIST_NUM = 302;
	public static final short Y_MARKET_PROP_LIST_NUM = 150;

	public static final short X_MARKET_PROP_BUY_BTN = 234;
	public static final short Y_MARKET_PROP_BUY_BTN = 147;
	
	public static final short XSPACE_MARKET_PROP_LIST_ITEM = 196;
	public static final short YSPACE_MARKET_PROP_LIST_ITEM = 107;
	
	public static final short X_MARKET_PROP_INTRO_ICON = 35-6;
	public static final short Y_MARKET_PROP_INTRO_ICON = 63;
	
	public static final short X_MARKET_PROP_INTRO_NAME = 91-6;
	public static final short Y_MARKET_PROP_INTRO_NAME = 68;
	
	public static final short X_MARKET_PROP_INTRO_PRICE = 91-6;
	public static final short Y_MARKET_PROP_INTRO_PRICE = 93;
	
	public static final short X_MARKET_PROP_INTRO_TEXT = 37-6;
	public static final short Y_MARKET_PROP_INTRO_TEXT = 123;
	public static final short W_MARKET_PROP_INTRO_TEXT = 145+12;
	public static final short H_MARKET_PROP_INTRO_TEXT = 145;
	
	public static final short X_MARKET_AMOUNT_TEXT = 103;
	public static final short Y_MARKET_AMOUNT_TEXT = 386;
	public static final short W_MARKET_AMOUNT_TEXT = 84;
	public static final short H_MARKET_AMOUNT_TEXT = 23;
	
	public static final short X_MARKET_BTN = 14;
	public static final short Y_MARKET_RECHARGE_BTN = 305;
	public static final short Y_MARKET_BACK_BTN = 341;
	public static final short W_MARKET_BTN = 193;
	public static final short H_MARKET_BTN = 27;
	
	
	/*帮助*/
	public static final String[][] STR_HELP_TEXT_PATH = {
		{"/txt/Help101.txt", "/txt/Help102.txt", "/txt/Help103.txt", "/txt/Help104.txt"},
		{"/txt/Help201.txt"},
		{"/txt/Help301.txt", "/txt/Help302.txt"},
		{"/txt/Help401.txt"}
	};
	
	/*弹出确认框*/
	public static final short X_POPUP_TEXT = 18;
	public static final short Y_POPUP_TEXT = 35;
	public static final short W_POPUP_TEXT = 245;
	public static final short H_POPUP_TEXT = 96;
	
	public static final short X_POPUP_ICON = 24;
	public static final short Y_POPUP_ICON = 35;
	public static final short Y_POPUP_ICON_DESC_TEXT = 94;
	public static final short X_POPUP_ICON_TEXT = 100;
	public static final short Y_POPUP_ICON_TEXT = 35;
	public static final short W_POPUP_ICON_TEXT = 162;
	public static final short H_POPUP_ICON_TEXT = 96;
	
	public static final short Y_POPUP_BTN = 110;
	
	public static final short[][] POS_CONFIRM_BTN_BG = {
		{50, 110},
		{178, 110}
	};
	public static final String[] STR_CONFIRM_BTN_TEXT = {"确定", "取消"};
	public static final String[] STR_CONFIRM_ADD_STAMINA = {"增加体力", "取消"};
	public static final String[] STR_CONFIRM_ADD_MAGIC = {"增加精力", "取消"};
	public static final String[] STR_CONFIRM_ADD_WEALTH = {"增加金币", "取消"};
	public static final String[] STR_CONFIRM_ADD_PROVISIONS = {"增加粮草", "取消"};
	public static final String[] STR_CONFIRM_ADD_LOYALTY = {"增加民忠", "取消"};
	public static final String[] STR_CONFIRM_ADD_GENERAL = {"聘用武将", "取消"};
	
	/*充值选择*/
	
	public static ISprite buildRotateCursor() {
		ISprite cursor = new RegularSprite();
		cursor.setImage(Resource.loadImage(PIC_ID_CURSOR), 42, 30);
		cursor.setFrameSequence(new byte[]{0,0, 1,1, 2,2, 3,3});
		return cursor;
	}
	
	public static void clearRotateCursor() {
		Resource.freeImage(PIC_ID_CURSOR);
	}
	
	public static String LoadString(String filePath) {
		InputStream ins = NewSanguoGameEngine.instance.getClass()
				.getResourceAsStream(filePath);
		StringBuffer strBuf = new StringBuffer();
		try {
			int low = ins.read();
			int high = ins.read();
			char temp;
			while ((low = ins.read()) >= 0) {
				high = ins.read();
				temp = (char) ((high << 8) | low);
				strBuf.append(temp);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {
				ins.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return strBuf.toString();
	}
	
	public static PopupConfirm buildPopupConfirm() {
		return UIResource.getInstance().buildDefaultPopupConfirm();
	}
	
	public static PopupText buildPopupText() {
		return UIResource.getInstance().buildDefaultPopupText();
	}
	
	public static PopupText buildPopupTextWithoutBtn() {
		return UIResource.getInstance().buildDefaultPopupTextWithoutBtn();
	}
	
	private static PopupIconText pitInstance;
	public static PopupIconText buildPopupIconText() {
		PopupIconText pit = null;
		if (pitInstance != null) {
			pit = pitInstance;
		}
		else {
			pit = new PopupIconText(NewSanguoGameEngine.instance);
			pitInstance = pit;
		}
		Image textBg = loadImage(PIC_ID_CONFIRM_TEXT_BG);
		pit.setTextBgImage(textBg);
		pit.setTextBgPos((NewSanguoGameEngine.instance.getScreenWidth()-textBg.getWidth())/2, 
				(NewSanguoGameEngine.instance.getScreenHeight()-textBg.getHeight())/2);
		pit.setTextColor(COLOR_ID_CONFIRM_TEXT);
		pit.setIconPos(X_POPUP_ICON, Y_POPUP_ICON);
		pit.setIconDescTextPos((short)0, Y_POPUP_ICON_DESC_TEXT);
		pit.setTextRegion(X_POPUP_ICON_TEXT, Y_POPUP_ICON_TEXT, W_POPUP_ICON_TEXT, H_POPUP_ICON_TEXT);
		
		pit.setButtonBgImage(loadImage(PIC_ID_CONFIRM_BTN_BG), W_CONFIRM_BTN, (short)0, W_CONFIRM_BTN, H_CONFIRM_BTN);
		pit.setButtonBorder((short)5);
		pit.setButtonText("确定");
		pit.setButtonPos((short)0, Y_POPUP_BTN);
		pit.setButtonTextColor(COLOR_ID_CONFIRM_BTN_NORMAL);
		pit.setWaitMillisSeconds(0);
		return pit;
	}
	
	public static void clearPopupIconText() {
		if (pitInstance != null) {
			pitInstance.setIcon(null);
		}
	}
}
