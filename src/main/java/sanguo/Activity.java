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
	 * ��ڼ����͵���
	 * @return
	 */
	private static boolean grantActivityGifts() {
		if (engine.pullProp() == 0) {
			/*����Կ��X3*/
			engine.addDepotPropById(0);
			engine.addDepotPropById(0);
			engine.addDepotPropById(0);
			/*���;��ʰ�X1*/
			engine.addDepotPropById(2);
			/*���;�����X1*/
			engine.addDepotPropById(4);
			/*���Ͱ���״X1*/
			engine.addDepotPropById(5);
			/*���ͽ�ҩX1*/
			engine.addDepotPropById(7);
			/*�������ӱ���X1*/
			engine.addDepotPropById(9);
			engine.pushProp();
			return true;
		}
		return false;
	}
	
	private static void showActivityGifts() {
		PopupText pt = Resource.buildPopupText();
		String text = "��ϲ����û����, ��������Ϊ: ���ӱ�����1, ��ҩ��3, ��������1, ���ʰ���1, Կ�ס�3";
		pt.setText(text);
		pt.popup();
		
		/*
		PopupIconText pit = Resource.buildPopupIconText();
		pit.setText("��ϲ�����Կ��X3");
		pit.setIcon(Resource.loadImage(engine.propList[0].getIcon()));
		pit.setIconDescText("X3");
		pit.popup();
		
		pit.setText("��ϲ����þ��ʰ�X1");
		pit.setIcon(Resource.loadImage(engine.propList[2].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("��ϲ����þ�����X1");
		pit.setIcon(Resource.loadImage(engine.propList[4].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("��ϲ����ð���״X1");
		pit.setIcon(Resource.loadImage(engine.propList[5].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("��ϲ����ý�ҩX1");
		pit.setIcon(Resource.loadImage(engine.propList[7].getIcon()));
		pit.setIconDescText("X1");
		pit.popup();
		
		pit.setText("��ϲ��������ӱ���X1");
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
	 * ������ȡ״̬
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
	 * �ж��Ƿ���ȡ�����
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
					if (data[0] == 1) {	/*��һ�ֽ�Ϊ�汾��*/
						if (data[1] == 1) {	/*�ڶ��ֽ�Ϊ1����ʾ��ȡ�����*/
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
	 * �ж��Ƿ��ڻ�ڼ�
	 * @return
	 */
	private static boolean isInActivityTime() {
		java.util.Date activityStartTime = DateUtil.createTime(2012, 5, 10);
		java.util.Date activityEndTime = DateUtil.createTime(2012, 5, 30, 23, 59, 59);
		return DateUtil.between(engineService.getCurrentTime(), activityStartTime, activityEndTime);
	}
}
