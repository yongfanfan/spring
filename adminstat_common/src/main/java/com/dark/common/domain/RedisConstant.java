package com.dark.common.domain;

/**
 * redis常量
 * @author lhj
 *
 */
public class RedisConstant {

	//手机校验码key
	public static final String VERIFY_CODE_KEY = "%s_verifycode";
	//发送短信，24小时10次限制key
	public static final String VERIFY_CODE_24_KEY = "%s_24";
	//手机校验码失效时间
	public static final int VERIFY_CODE_TIMEOUT = 130;
	//发送短信，24小时10次限制key
	public static final int VERIFY_CODE_24_TIMEOUT = 60 * 60 * 24;
	
	//advertiseTime活动时间段对象,adtime_${id}
	public static final String ADVERTISE_TIME_KEY = "adtime_%s";
	//奖品项product,product_${id}
	public static final String PRODUCT_KEY = "product_%s";
	//奖品项更新KEY,product_update_${id}
	public static final String PRODUCT_UPDATE_KEY = "product_update_%s";
	//奖品库存product_inventory_${id}
	public static final String PRODUCT_INVENTORY_KEY = "product_inventory_%s";
	//兑换商品库存锁
	public static final String EXCHANGE_PRODUCT = "exchange_product";
	
	//meger失效时间 2个月
	public static final int MERGE_TIME_OUT = 60 * 60 * 24 * 30 * 2;
	//积分是否需要merge mp_${id}
	public static final String MERGE_POINT_KEY = "mp_%s";
	//彩票是否需要merge mt_${id}
	public static final String MERGE_TICKET_KEY = "mt_%s";
	//订单是否需要merge mo_${id}
	public static final String MERGE_ORDER_KEY = "mo_%s";
	//微信授权Token key
	public static final String WEICHAT_TOKEN="WEICHAT_TOKEN";
	//微信js票据
	public static final String WEICHAT_JS_TIKET="WEICHAT_JS_TIKET";
	//微信公众平台授权码 失效时间 2小时
	public static final int WEICHAT_TOKEN_TIMEOUT=7200;
	
	public static final String ACTIVITY_START="JX_ACTIVITY_START_%s";//若开始 则为1
	
	public static final String REQUEST_COUNT="JX_REQUEST_COUNT_%s";
	
	public static final String FIRST_BINGO_PROBABILITY = "fisrt_bingo_%s";//第一次中奖概率
}
