package com.codechef.smartalert.constant;

public class KandyApiConstant {
	public static final String KANDY_API = "https://api.kandy.io/v1.2/";
	public static final String API_KEY = "DAK4baaa942fd894f8dba70ec68c29046b8";
	public static final String API_SECRET = "DASd6ede8d2d6db4acfb663676bf84f177c";
	public static final String GET_DOMAIN_ACCESS_TOKEN_API = KandyApiConstant.KANDY_API + "domains/accesstokens?key="+ KandyApiConstant.API_KEY +"&domain_api_secret="+ KandyApiConstant.API_SECRET;
	public static final String SEND_VALIDATION_CODE_VIA_SMS_API = KandyApiConstant.KANDY_API + "domains/verifications/smss?";
	public static final String VERIFY_VALIDATION_CODE_API = KandyApiConstant.KANDY_API + "domains/verifications/codes?";
	public static final String CREATE_USER_API = KandyApiConstant.KANDY_API + "domains/users/user_id?";
	public static final String GET_USER_ACCESS_TOKEN_API = KandyApiConstant.KANDY_API + "domains/users/accesstokens?key="+ KandyApiConstant.API_KEY +"&domain_api_secret="+ KandyApiConstant.API_SECRET+ "&";
	public static final String DELETE_USER_API = KandyApiConstant.KANDY_API + "domains/users?";
	public static final String ADD_DELETE_GET_TO_ADDRESS_API = KandyApiConstant.KANDY_API + "users/addressbooks/personal?key=";
}
