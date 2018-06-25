package com.dark.api.util;

import java.util.Map;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import com.alibaba.druid.support.json.JSONUtils;
import com.dark.common.domain.Constants;
import com.dark.common.domain.RedisConstant;
import com.dark.common.redis.JedisTemplate;

public class WeichatUtil {
	private static JedisTemplate jedisTemplate=new JedisTemplate();
	public final static String AUTH_URL="https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=%s&state=%s#wechat_redirect";
	public final static String ACCESSTOKEN_URL_WEB="https://api.weixin.qq.com/sns/oauth2/access_token?appid=%s&secret=%s&code=%s&grant_type=authorization_code";
	public final static String USERINFO_URL="https://api.weixin.qq.com/sns/userinfo?access_token=%s&openid=%s&lang=zh_CN";
	public final static String TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
	public final static String JS_TICKET_URL="https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=%s&type=jsapi";
	public final static Map<String,Object> parse(String json){
		Map<String,Object> result=(Map<String, Object>) JSONUtils.parse(json);
		return result;
	}
	/**
	 * 获取微信授权码 accessToken
	 * @return
	 */
	public static String getToken(){
		try {
			String token=jedisTemplate.get(RedisConstant.WEICHAT_TOKEN);
			if(StringUtils.isNotEmpty(token)){//首先从缓存里获取 
				return token;
			}
			else{//如果缓存中没有则重新获取
				String tokenInfo=Remote.GETMethod(String.format(TOKEN_URL,Constants.WEICHAT_APPID,Constants.WEICHAT_APPSECRET));
				Map<String,Object> tokenMap=parse(tokenInfo);
				if(MapUtils.isNotEmpty(tokenMap)&&tokenMap.containsKey("access_token")){//接口成功 
					jedisTemplate.setex(RedisConstant.WEICHAT_TOKEN, (String)tokenMap.get("access_token"), RedisConstant.WEICHAT_TOKEN_TIMEOUT);
					return (String)tokenMap.get("access_token");
				}
				else{//如果失败则再获取一次
					tokenInfo=Remote.GETMethod(String.format(TOKEN_URL,Constants.WEICHAT_APPID,Constants.WEICHAT_APPSECRET));
					tokenMap=parse(tokenInfo);
					if(MapUtils.isNotEmpty(tokenMap)&&tokenMap.containsKey("access_token")){
						jedisTemplate.setex(RedisConstant.WEICHAT_TOKEN, (String)tokenMap.get("access_token"), RedisConstant.WEICHAT_TOKEN_TIMEOUT);
						return (String)tokenMap.get("access_token");
					}
					else{//只有两次机会 第三次则返回null
						return null;
					}
				}	
			}
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 获取js票据 调用js方法使用
	 * @return
	 */
	public static String getJSTicket(){
		try {
			String ticket=jedisTemplate.get(RedisConstant.WEICHAT_JS_TIKET);
			if(StringUtils.isNotEmpty(ticket)){//首先从缓存中获取
				return ticket;
			}
			else{//缓存中没有则调用接口
				String token=getToken();
				if(StringUtils.isNotEmpty(token)){//如果存在授权码则调用
					return getJSTicketByToken(token);
					
				}
				else{//如果不存在则获取一次
					token=getToken();
					return getJSTicketByToken(token);
				}
			}
		} catch (Exception e) {
			return null;
		}
	}
	/**
	 * 通过微信授权码获取js票据
	 * @param accessToken
	 * @return
	 */
	public static String getJSTicketByToken(String accessToken){
		try {
			String ticketInfo=Remote.GETMethod(String.format(JS_TICKET_URL, accessToken));
			Map<String,Object> ticketMap=parse(ticketInfo);
			if(MapUtils.isEmpty(ticketMap)){//接口返回空 
				return null;
			}
			else if(ticketMap.containsKey("ticket")){//接口返回正常 存在票据信息
				jedisTemplate.setex(RedisConstant.WEICHAT_JS_TIKET, (String)ticketMap.get("ticket"), RedisConstant.WEICHAT_TOKEN_TIMEOUT);
				return (String)ticketMap.get("ticket");
			}
			else if((Integer)ticketMap.get("errcode")==40001){//accessToken失效 则首先获取accessToken再调用接口
				jedisTemplate.del(RedisConstant.WEICHAT_TOKEN);
				String token=getToken();
				ticketInfo=Remote.GETMethod(String.format(JS_TICKET_URL, token));
				ticketMap=parse(ticketInfo);
				if(MapUtils.isEmpty(ticketMap)){
					return null;
				}
				else if(ticketMap.containsKey("ticket")){//第二次机会
					jedisTemplate.setex(RedisConstant.WEICHAT_JS_TIKET, (String)ticketMap.get("ticket"), RedisConstant.WEICHAT_TOKEN_TIMEOUT);
					return (String)ticketMap.get("ticket");
				}
				else{
					return null;
				}
			}
			else{//第二次机会
				ticketInfo=Remote.GETMethod(String.format(JS_TICKET_URL, accessToken));
				ticketMap=parse(ticketInfo);
				if(MapUtils.isEmpty(ticketMap)){
					return null;
				}
				else if(ticketMap.containsKey("ticket")){
					jedisTemplate.setex(RedisConstant.WEICHAT_JS_TIKET, (String)ticketMap.get("ticket"), RedisConstant.WEICHAT_TOKEN_TIMEOUT);
					return (String)ticketMap.get("ticket");
				}
				else{
					return null;
				}
			}
		} catch (Exception e) {
			return null;
		}
	}
}