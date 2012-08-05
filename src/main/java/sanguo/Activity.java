package sanguo;

import cn.ohyeah.itvgame.model.GameRecord;
import cn.ohyeah.stb.game.EngineService;
import cn.ohyeah.stb.game.ServiceWrapper;
import cn.ohyeah.stb.ui.PopupText;
import cn.ohyeah.stb.util.DateUtil;

public class Activity {
	private static NewSanguoGameEngine engine = NewSanguoGameEngine.instance;
	private static EngineService engineService = engine.getEngineService();
	
	/**
	 * 活动期间赠送道具
	 * @return
	 */
	private static boolean grantActivityGifts() {
		if (engine.pullProp() == 0) {
			/*赠送钥匙X3*/
			engine.addDepotPropById(0);
			engine.addDepotPropById(0);
			engine.addDepotPropById(0);
			/*赠送军资包X1*/
			engine.addDepotPropById(2);
			/*赠送军粮包X1*/
			engine.addDepotPropById(4);
			/*赠送安民状X1*/
			engine.addDepotPropById(5);
			/*赠送金疮药X1*/
			engine.addDepotPropById(7);
			/*赠送孙子兵法X1*/
			engine.addDepotPropById(9);
			engine.pushProp();
			return true;
		}
		return false;
	}
	
	private static void showActivityGifts() {
		PopupText pt = Resource.buildPopupText();
		String text = "恭喜您获得活动奖励, 奖励内容为: 孙子兵法×1, 金疮药×3, 军粮包×1, 军资包×1, 钥匙×3";
		pt.setText(text);
		pt.popup();
		
		/*
		PopupIconText pit = Resource.buildPopupIconText();
		pit.setText("恭喜您获得钥匙X3");
		pit.setIcon(Resource.loadImage(engine.propList[0].getIcon()));
		pit.setIconDescText("X3");
		pit.popup();
		
		pit.setText("恭喜您获得军资包X1");
		pit.setIcon(Resource.loadImage(engine.propList[2].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("恭喜您获得军粮包X1");
		pit.setIcon(Resource.loadImage(engine.propList[4].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("恭喜您获得安民状X1");
		pit.setIcon(Resource.loadImage(engine.propList[5].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("恭喜您获得金疮药X1");
		pit.setIcon(Resource.loadImage(engine.propList[7].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("恭喜您获得孙子兵法X1");
		pit.setIcon(Resource.loadImage(engine.propList[9].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		*/
	}
	
	public static void processGrantGifts() {
		//if (isInActivityTime()) {
			if (!isReceivedGifts()) {
				if (grantActivityGifts()) {
					saveReceivedState();
					showActivityGifts();
				}
			}
		//}
	}
	
	/**
	 * 保存领取状态
	 * @return
	 */
	private static boolean saveReceivedState() {
		GameRecord record = new GameRecord();
		record.setRecordId(-1);
		byte[] data = new byte[2];
		data[0] = 1;
		data[1] = 1;
		record.setData(data);
		ServiceWrapper sw = engine.getServiceWrapper();
		sw.saveRecord(record);
		return sw.isServiceSuccessful();
	}
	
	/**
	 * 判断是否领取过礼包
	 * @return
	 */
	private static boolean isReceivedGifts() {
		boolean received = false;
		ServiceWrapper sw = engine.getServiceWrapper();
		GameRecord record = sw.readRecord(-1);
		if (sw.isServiceSuccessful()) {
			if (record != null) {
				byte[] data = record.getData();
				if (data != null) {
					if (data[0] == 1) {	/*第一字节为版本号*/
						if (data[1] == 1) {	/*第二字节为1，表示领取过礼包*/
							received = true;
						}
					}
				}
			}
		}
		else {
			received = true;
		}
		return received;
	}
	
	/**
	 * 判断是否在活动期间
	 * @return
	 */
	private static boolean isInActivityTime() {
		java.util.Date activityStartTime = DateUtil.createTime(2012, 5, 10);
		java.util.Date activityEndTime = DateUtil.createTime(2012, 5, 30, 23, 59, 59);
		return DateUtil.between(engineService.getCurrentTime(), activityStartTime, activityEndTime);
	}
}
