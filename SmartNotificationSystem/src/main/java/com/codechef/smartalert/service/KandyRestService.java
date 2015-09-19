package com.codechef.smartalert.service;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codechef.smartalert.constant.ApplicationConstant;
import com.codechef.smartalert.constant.KandyApiConstant;

@Service
public class KandyRestService {

	@Autowired
	private HttpServletRequest request;
	
	final ObjectMapper objectMapper = new ObjectMapper();
	
	public Map<String, Object> getDomainTokenAccess() {
		return getResponse(KandyApiConstant.GET_DOMAIN_ACCESS_TOKEN_API, ApplicationConstant.GET, null);
	}

	public void createUserById() {

	}

	public Map<String, Object> sendValidationViaSms(final String user_phone_number) {
		final String api = KandyApiConstant.SEND_VALIDATION_CODE_VIA_SMS_API + "key=" + request.getSession().getAttribute("ACCESS_TOKEN") + "&user_phone_number=" + user_phone_number + "&user_country_code=IN";
		return getResponse(api, ApplicationConstant.POST, null);
	}

	public Map<String, Object> verifyValidationSmsCode(final String validation_code) {
		final String api = KandyApiConstant.VERIFY_VALIDATION_CODE_API + "key=" + request.getSession().getAttribute("ACCESS_TOKEN") + "&validation_code=" + validation_code;
		return getResponse(api, ApplicationConstant.GET, null);
	}

	public Map<String, Object> getResponse(final String api, final String method, final String requestBody) {
		try {
			final HttpURLConnection httpURLConnection = (HttpURLConnection) new URL(api).openConnection();
			httpURLConnection.setRequestMethod(method);
			httpURLConnection.setRequestProperty(ApplicationConstant.ACCEPT, ApplicationConstant.APP_JSON);
			httpURLConnection.setDoOutput (true);
			if(requestBody != null)
			{
				httpURLConnection.setRequestProperty(ApplicationConstant.CONTENT_TYPE, "application/json; charset=UTF-8");
				final OutputStream outputStream = httpURLConnection.getOutputStream();
				outputStream.write(requestBody.getBytes("UTF-8"));
				outputStream.close();
			}
			else {
				httpURLConnection.setRequestProperty(ApplicationConstant.CONTENT_TYPE, ApplicationConstant.APP_JSON);
			}
			if (httpURLConnection.getResponseCode() != 200) {
				return null;
			}
			final ObjectMapper objectMapper = new ObjectMapper();
			@SuppressWarnings("unchecked")
			final Map<String, Object> responseData = objectMapper.readValue(httpURLConnection.getInputStream(), Map.class);
			httpURLConnection.disconnect();
			return responseData;
		} catch (MalformedURLException malformedURLException) {
			malformedURLException.printStackTrace();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		}
		return null;
	}

	public Map<String, Object> registerUsersToDomain(final String userId, final String domainToken, final String countryCode) {
		final String api = KandyApiConstant.CREATE_USER_API +"key="+ domainToken +"&user_id="+ userId +"&user_country_code="+ countryCode;
		return getResponse(api, ApplicationConstant.POST, null);
	}

	public int deleteUser(final String userId, final String userPassword, final String domainToken) {
		final String api = KandyApiConstant.DELETE_USER_API +"key="+ domainToken +"&user_id="+ userId+"&user_password="+userPassword;
		System.out.println(api);
		return getResponse(api, ApplicationConstant.DELETE, null).get("status").toString().equalsIgnoreCase("0")? 1 : 0;
	}

	@SuppressWarnings({ "unchecked" })
	public Map<String, String> configureAddressBook(final String customerUsername, final String agentUsername) throws JsonGenerationException, JsonMappingException, IOException {
		System.out.println("USER API URL----->"+KandyApiConstant.GET_USER_ACCESS_TOKEN_API +"user_id="+ customerUsername);
		System.out.println("EMPLOYEE API URL----->"+KandyApiConstant.GET_USER_ACCESS_TOKEN_API +"user_id="+ agentUsername);
		
		final Map<String, Object> customerTokenResponse = (Map<String, Object>) getResponse(KandyApiConstant.GET_USER_ACCESS_TOKEN_API +"user_id="+ customerUsername, ApplicationConstant.GET, null).get("result");
		final Map<String, Object> agentTokenResponse = (Map<String, Object>) getResponse(KandyApiConstant.GET_USER_ACCESS_TOKEN_API +"user_id="+ agentUsername, ApplicationConstant.GET, null).get("result");
		
		Map<String, Object> contactDetails = new HashMap<String, Object>();
		contactDetails.put("contact_user_name", agentUsername.concat("@smartbank.techmahindra.com"));
		contactDetails.put("contact_first_name", agentUsername);
		contactDetails.put("contact_last_name", agentUsername);
		Map<String, Object> requestBody = new HashMap<String, Object>();
		requestBody.put("contact", contactDetails);
		System.out.println("eeeeeeeee----->"+objectMapper.writeValueAsString(customerTokenResponse));
		
		final Map<String, Object> customerAddressBookDetails = (Map<String, Object>) addGetDeleteAddressBookDetails(KandyApiConstant.ADD_DELETE_GET_TO_ADDRESS_API +customerTokenResponse.get("user_access_token").toString(), objectMapper.writeValueAsString(requestBody), ApplicationConstant.POST).get("result");
		
		contactDetails = new HashMap<String, Object>();
		contactDetails.put("contact_user_name", customerUsername.concat("@smartbank.techmahindra.com"));
		contactDetails.put("contact_first_name", customerUsername);
		contactDetails.put("contact_last_name", customerUsername);
		requestBody = new HashMap<String, Object>();
		requestBody.put("contact", contactDetails);
		final Map<String, Object> agentAddressBookDetails = (Map<String, Object>) addGetDeleteAddressBookDetails(KandyApiConstant.ADD_DELETE_GET_TO_ADDRESS_API +agentTokenResponse.get("user_access_token").toString(), objectMapper.writeValueAsString(requestBody), ApplicationConstant.POST).get("result");
		
		final Map<String, String> responseBody = new HashMap<String, String>();
		responseBody.put("customerAddedAgentContactId", customerAddressBookDetails.get("contact_id").toString());
		responseBody.put("agentAddedCustomerContactId", agentAddressBookDetails.get("contact_id").toString());
		return responseBody;
	}

	private Map<String, Object> addGetDeleteAddressBookDetails(final String addDeleteGetToAddressApi, final String requestBody, final String method) {
		System.out.println("addDeleteGetToAddressApi------>"+addDeleteGetToAddressApi);
		System.out.println("requestBody------>"+requestBody);
		if(method == ApplicationConstant.POST)
		{
			System.out.println("addDeleteGetToAddressApi------>"+addDeleteGetToAddressApi);
			System.out.println("requestBody------>"+requestBody);
			return getResponse(addDeleteGetToAddressApi, ApplicationConstant.POST, requestBody);
		}
		return null;
	}
	
	public static void main(String[] args) throws JsonGenerationException, JsonMappingException, IOException {
	  
	  System.out.println(new KandyRestService().configureAddressBook("ni309915", "gt300622"));
	}
}
