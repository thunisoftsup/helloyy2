package com.thunisoft.sswy.mobile.activity.auth;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Extra;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.thunisoft.sswy.mobile.Constants;
import com.thunisoft.dzfy.mobile.R;
import com.thunisoft.sswy.mobile.activity.BaseActivity;
import com.thunisoft.sswy.mobile.activity.WaittingDialog;
import com.thunisoft.sswy.mobile.activity.dialog.YzmDialogActivity_;
import com.thunisoft.sswy.mobile.cache.LoginCache;
import com.thunisoft.sswy.mobile.logic.AuthLogic;
import com.thunisoft.sswy.mobile.logic.response.BaseResponse;
import com.thunisoft.sswy.mobile.util.StringUtils;

/**
 * 登录
 * 
 * @author hzz
 * 
 */
@EActivity(R.layout.activity_login)
public class LoginActivity extends BaseActivity implements OnClickListener {

	String TAG = "susong51";
	/**
	 * 普通用户选择
	 */
	@ViewById(R.id.tv_normal)
	TextView tv_normal_user;

	/**
	 * 律师用户选择
	 */
	@ViewById(R.id.tv_lawyer)
	TextView tv_lawyer_user;

	/**
	 * 用普通用户登录时--用户名
	 */
	@ViewById(R.id.tv_username)
	EditText tvUserName;

	/**
	 * 用普通用户登录时--密码
	 */
	@ViewById(R.id.tv_password)
	EditText tvPassword;

	/**
	 * 律师用户--手机登录选择
	 */
	@ViewById(R.id.login_with_phonenumber)
	TextView loginWithPhoneNumber;

	/**
	 * 律师用户--令牌登录选择
	 */
	@ViewById(R.id.login_with_token)
	TextView loginWithToken;

	/**
	 * 律师用户--手机登录--用户名
	 */
	@ViewById(R.id.login_with_phone_userName)
	EditText loginWithPhoneUserName;

	/**
	 * 律师用户--手机登录--手机号
	 */
	@ViewById(R.id.login_with_phone_phonenumber)
	EditText loginWithPhonePhoneNumber;

	/**
	 * 律师用户--手机登录--验证码
	 */
	@ViewById(R.id.login_with_phone_code)
	EditText loginWithPhoneCode;

	/**
	 * 律师用户--手机登录--获取手机验证码按钮
	 */
	@ViewById(R.id.get_code)
	Button btGetCode;

	/**
	 * 律师用户--令牌登录--用户名
	 */
	@ViewById(R.id.login_with_token_userName)
	EditText loginWithTokenUserName;

	/**
	 * 律师用户--令牌登录--密码
	 */
	@ViewById(R.id.login_with_token_password)
	EditText loginWithTokenPassword;

	/**
	 * 律师用户--令牌登录--令牌
	 */
	@ViewById(R.id.login_with_token_token)
	EditText loginWithTokenToken;

	/**
	 * 登录按钮
	 */
	@ViewById(R.id.btn_login)
	Button btnLogin;

	/**
	 * 普通用户登录界面
	 */
	@ViewById(R.id.normal_user_layout)
	LinearLayout normalUserLayout;

	/**
	 * 律师用户登录界面
	 */
	@ViewById(R.id.lawyer_user_layout)
	LinearLayout laywerUserLayout;

	/**
	 * 律师用户--手机--输入区域
	 */
	@ViewById(R.id.login_with_phone_layout)
	LinearLayout loginWithPhoneLayout;

	/**
	 * 律师用户--令牌--输入区域
	 */
	@ViewById(R.id.login_with_token_layout)
	LinearLayout loginWithTokenLayout;

	/**
	 * “手机登录”下划线
	 */
	@ViewById(R.id.line_phone)
	View linePhone;

	/**
	 * “令牌登录下划线”
	 */
	@ViewById(R.id.line_token)
	View lineToken;
	
	/**
	 * 注册，找回密码总布局
	 */
	@ViewById(R.id.zhmmAndzeLayout)
	LinearLayout zhmmAndzeLayout;

	@Bean
	AuthLogic authLogic;
	@Bean
	LoginCache aLoginCache;

	String tempSid;

	String yzm;
	
	private Runnable countFDown;

	private int currentSeconds = 0;
	
	Handler handler = new Handler();

	private int userType = LoginCache.LOGIN_TYPE_NORMAL;
	
	private WaittingDialog waitDialog;

	@Extra("message")
	String message;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setResult(RESULT_OK);
	}

	@AfterViews
	public void initViews() {
		setBtnBack();
		findViewById(R.id.get_code).setOnClickListener(this);
		findViewById(R.id.btn_login).setOnClickListener(this);
		findViewById(R.id.tv_normal).setOnClickListener(this);
		findViewById(R.id.tv_lawyer).setOnClickListener(this);
		findViewById(R.id.login_with_phonenumber).setOnClickListener(this);
		findViewById(R.id.login_with_token).setOnClickListener(this);
		findViewById(R.id.get_code).setOnClickListener(this);
		findViewById(R.id.register).setOnClickListener(this);
		findViewById(R.id.reGetPassword).setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (aLoginCache.isLogined()) {
			finish();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_login:
			btnLogin.setFocusable(false);
			btnLogin.setClickable(false);
			waitDialog = new WaittingDialog(this, R.style.LoginDialogStyle, "登录中...");
	    	waitDialog.setIsCanclable(false);
			waitDialog.show();
			loginClick();
			break;
		case R.id.tv_normal:
			changeUserType(LoginCache.LOGIN_TYPE_NORMAL);
			break;
		case R.id.tv_lawyer:
			changeUserType(LoginCache.LOGIN_TYPE_LAYWER);
			break;
		case R.id.login_with_phonenumber:
			changeUserType(LoginCache.LOGIN_TYPE_LAYWER_PHONE);
			break;
		case R.id.login_with_token:
			changeUserType(LoginCache.LOHIN_TYPE_LAYWER_TOKEN);
			break;
		case R.id.get_code:
			if (checkDataGetPhoneCode()) {
				getPhoneCode(getHandledString(loginWithPhoneUserName),
						getHandledString(loginWithPhonePhoneNumber));
				btGetCode.setClickable(false);
				btGetCode.setEnabled(false);
				currentSeconds = 0;
				countFDown = new Runnable() {
					@Override
					public void run() {

						if (currentSeconds < 59) {
							currentSeconds++;
							btGetCode.setText((60 - currentSeconds)
									+ "s后重新获取");
							handler.postDelayed(countFDown, 1000);
						} else {
							currentSeconds = 0;
							btGetCode.setText("获取验证码");
							btGetCode.setClickable(true);
							btGetCode.setEnabled(true);
						}
					}
				};
				handler.post(countFDown);
			}
			break;
		case R.id.register:
			register();
			break;
		case R.id.reGetPassword:
			reGetPassword();
			break;

		}
		super.onClick(v);
	}

	private void changeUserType(int type) {
		Message message = new Message();
		if (type == LoginCache.LOGIN_TYPE_NORMAL) {
			userType = LoginCache.LOGIN_TYPE_NORMAL;
			message.what = 0;
			switchLayoutHandler.sendMessage(message);
		} else if (type == LoginCache.LOGIN_TYPE_LAYWER) {
			if (userType == LoginCache.LOGIN_TYPE_NORMAL) {
				userType = LoginCache.LOGIN_TYPE_LAYWER_PHONE;
				message.what = 1;
				switchLayoutHandler.sendMessage(message);
			}
		} else if (type == LoginCache.LOGIN_TYPE_LAYWER_PHONE) {
			userType = LoginCache.LOGIN_TYPE_LAYWER_PHONE;
			message.what = 2;
			switchLayoutHandler.sendMessage(message);
		} else if (type == LoginCache.LOHIN_TYPE_LAYWER_TOKEN) {
			userType = LoginCache.LOHIN_TYPE_LAYWER_TOKEN;
			message.what = 3;
			switchLayoutHandler.sendMessage(message);
		}
	}

	@SuppressLint("HandlerLeak")
	private Handler switchLayoutHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.what) {
			case 0:
				switchToNormalUserLayout();
				break;
			case 1:
				switchToLaywerUserLayout();
				break;
			case 2:
				switchToLaywerUserPhoneLayout();
				break;
			case 3:
				switchToLaywerUserTokenLayout();
				break;
			default:
				break;
			}
		}

	};

	/**
	 * 转到普通用户登录
	 */
	@SuppressLint("ResourceAsColor")
	private void switchToNormalUserLayout() {
		tv_normal_user.setBackgroundResource(R.color.white);
		// tv_normal_user.setTextColor(R.color.login_user_text_color_select);
		tv_normal_user.setTextColor(this.getResources().getColor(
				R.color.login_user_text_color_select));
		tv_lawyer_user
				.setBackgroundResource(R.color.login_user_switch_select_color);
		// tv_lawyer_user.setTextColor(R.color.white);
		tv_lawyer_user
				.setTextColor(this.getResources().getColor(R.color.white));
		laywerUserLayout.setVisibility(View.GONE);
		normalUserLayout.setVisibility(View.VISIBLE);
		zhmmAndzeLayout.setVisibility(View.VISIBLE);
		clearDataLaywerPhone();
		clearDataLaywerToken();
	}

	/**
	 * 转到律师用户登录
	 */
	@SuppressLint("ResourceAsColor")
	private void switchToLaywerUserLayout() {
		tv_normal_user
				.setBackgroundResource(R.color.login_user_switch_select_color);
		// tv_normal_user.setTextColor(R.color.white);
		tv_normal_user
				.setTextColor(this.getResources().getColor(R.color.white));
		tv_lawyer_user.setBackgroundResource(R.color.white);
		// tv_lawyer_user.setTextColor(R.color.login_user_text_color_select);
		tv_lawyer_user.setTextColor(this.getResources().getColor(
				R.color.login_user_text_color_select));
		// loginWithPhoneNumber.setTextColor(R.color.phone_or_token_select_color);
		loginWithPhoneNumber.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_select_color));
		// loginWithToken.setTextColor(R.color.phone_or_token_unselect_color);
		loginWithToken.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_unselect_color));
		linePhone.setVisibility(View.VISIBLE);
		lineToken.setVisibility(View.GONE);
		loginWithPhoneLayout.setVisibility(View.VISIBLE);
		loginWithTokenLayout.setVisibility(View.GONE);
		laywerUserLayout.setVisibility(View.VISIBLE);
		zhmmAndzeLayout.setVisibility(View.GONE);
		clearDataNormal();
	}

	/**
	 * 转到律师用户--手机登录
	 */
	@SuppressLint("ResourceAsColor")
	private void switchToLaywerUserPhoneLayout() {
		// loginWithToken.setTextColor(R.color.phone_or_token_unselect_color);
		loginWithToken.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_unselect_color));
		lineToken.setVisibility(View.GONE);
		loginWithTokenLayout.setVisibility(View.GONE);

		// loginWithPhoneNumber.setTextColor(R.color.phone_or_token_select_color);
		loginWithPhoneNumber.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_select_color));
		linePhone.setVisibility(View.VISIBLE);
		loginWithPhoneLayout.setVisibility(View.VISIBLE);
		clearDataLaywerToken();
	}

	/**
	 * 转到律师用户--令牌登录
	 */
	@SuppressLint("ResourceAsColor")
	private void switchToLaywerUserTokenLayout() {
		// loginWithPhoneNumber.setTextColor(R.color.phone_or_token_unselect_color);
		loginWithPhoneNumber.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_unselect_color));
		linePhone.setVisibility(View.GONE);
		loginWithPhoneLayout.setVisibility(View.GONE);

		// loginWithToken.setTextColor(R.color.phone_or_token_select_color);
		loginWithToken.setTextColor(this.getResources().getColor(
				R.color.phone_or_token_select_color));
		lineToken.setVisibility(View.VISIBLE);
		loginWithTokenLayout.setVisibility(View.VISIBLE);
		clearDataLaywerPhone();
	}

	/**
	 * 清空正常用户登录界面输入框的数据
	 */
	public void clearDataNormal() {
		tvUserName.setText("");
		tvPassword.setText("");
	}

	/**
	 * 清空律师用户手机登录界面输入框的数据
	 */
	public void clearDataLaywerPhone() {
		loginWithPhoneUserName.setText("");
		loginWithPhonePhoneNumber.setText("");
		loginWithPhoneCode.setText("");
	}

	/**
	 * 清空律师用户令牌登录界面输入框的数据
	 */
	public void clearDataLaywerToken() {
		loginWithTokenUserName.setText("");
		loginWithTokenPassword.setText("");
		loginWithTokenToken.setText("");
	}

	/**
	 * 登录
	 */
	public void loginClick() {
		switch (userType) {
		case LoginCache.LOGIN_TYPE_NORMAL:
			if (checkDataNormal()) {
				loginBackgroundNormal(tvUserName.getText().toString().trim(),
						tvPassword.getText().toString().trim());
			}
			break;
		case LoginCache.LOGIN_TYPE_LAYWER_PHONE:
			if (checkDataLaywerPhone()) {
				loginBackgroundLaywerPhone(
						getHandledString(loginWithPhoneUserName),
						getHandledString(loginWithPhonePhoneNumber),
						getHandledString(loginWithPhoneCode));
			}
			break;
		case LoginCache.LOHIN_TYPE_LAYWER_TOKEN:
			if (checkDataLaywerToken()) {
				loginBackgroundLaywerToken(
						getHandledString(loginWithTokenUserName),
						getHandledString(loginWithTokenPassword),
						getHandledString(loginWithTokenToken));
			}
			break;
		}
	}

	/**
	 * 普通用户登录时，核查数据
	 */
	public boolean checkDataNormal() {
		boolean flag = true;
		if (StringUtils.isBlank(tvUserName.getText().toString().trim())
				|| StringUtils.isBlank(tvPassword.getText().toString().trim())) {
			Toast.makeText(this, "用户名或密码不能为空!", Toast.LENGTH_SHORT).show();
			flag = false;
			changeBtnStatus();
		}
		return flag;
	}

	/**
	 * 律师用户手机登录时，核查数据
	 */
	public boolean checkDataLaywerPhone() {
		boolean flag = false;
		if (StringUtils.isBlank(loginWithPhoneUserName.getText().toString()
				.trim())
				|| StringUtils.isBlank(loginWithPhonePhoneNumber.getText()
						.toString().trim())
				|| StringUtils.isBlank(loginWithPhoneCode.getText().toString()
						.trim())) {
			Toast.makeText(this, "用户名、手机号或验证码不能为空！", Toast.LENGTH_SHORT).show();
			changeBtnStatus();
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 律师用户令牌登录时，核查数据
	 */
	public boolean checkDataLaywerToken() {
		boolean flag = false;
		if (StringUtils.isBlank(loginWithTokenUserName.getText().toString()
				.trim())
				|| StringUtils.isBlank(loginWithTokenPassword.getText()
						.toString().trim())
				|| StringUtils.isBlank(loginWithTokenToken.getText().toString()
						.trim())) {
			Toast.makeText(this, "用户名、密码或令牌不能为空！", Toast.LENGTH_SHORT).show();
			changeBtnStatus();
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 获取验证码时核查数据
	 */
	public boolean checkDataGetPhoneCode() {
		boolean flag = false;
		if (StringUtils.isBlank(loginWithPhoneUserName.getText().toString()
				.trim())
				|| StringUtils.isBlank(loginWithPhonePhoneNumber.getText()
						.toString().trim())) {

			Toast.makeText(this, "用户名、手机号不能为空！", Toast.LENGTH_SHORT).show();
		} else {
			flag = true;
		}
		return flag;
	}

	/**
	 * 普通用户登录时后台处理
	 */
	@Background
	public void loginBackgroundNormal(String userName, String userPassword) {
		if (StringUtils.isBlank(this.tempSid)) {
			Log.i(TAG, "点击登陆");
			BaseResponse br = authLogic.getTempSid();
			if (!br.isSuccess()) {
				Log.i(TAG, "loginBackground建立会话失败:" + br.getMessage());
				showToast("建立会话失败");
				changeBtnStatus();
			} else {
				this.tempSid = br.getTempSid();
				Log.i(TAG, "loginBackground建立会话成功:" + tempSid);
				loginNormal(userName, userPassword);
			}
			
		} else {
			Log.i(TAG, "loginBackground会话存在，直接登录:" + tempSid);
			loginNormal(userName, userPassword);
		}
	}

	/**
	 * 律师用户--手机登录--后台处理
	 */
	@Background
	public void loginBackgroundLaywerPhone(String userName, String phoneNumber,
			String phoneCode) {
		if (StringUtils.isBlank(this.tempSid)) {
			Log.i(TAG, "点击登陆");
			
			BaseResponse br = authLogic.getTempSid();
			if (!br.isSuccess()) {
				Log.i(TAG, "loginBackground建立会话失败:" + br.getMessage());
				showToast("建立会话失败");
				changeBtnStatus();
			} else {
				this.tempSid = br.getTempSid();
				Log.i(TAG, "loginBackground建立会话成功:" + tempSid);
				loginLaywerPhone(userName, phoneNumber, phoneCode);
			}
			
		} else {
			Log.i(TAG, "loginBackground会话存在，直接登录:" + tempSid);
			loginLaywerPhone(userName, phoneNumber, phoneCode);
		}
	}

	/**
	 * 律师用户--令牌登录--后台处理
	 */
	@Background
	public void loginBackgroundLaywerToken(String userName,
			String userPassword, String token) {
		if (StringUtils.isBlank(this.tempSid)) {
			Log.i(TAG, "点击登陆");
			BaseResponse br = authLogic.getTempSid();
			if (!br.isSuccess()) {
				Log.i(TAG, "loginBackground建立会话失败:" + br.getMessage());
				showToast("建立会话失败");
				changeBtnStatus();
			} else {
				this.tempSid = br.getTempSid();
				Log.i(TAG, "loginBackground建立会话成功:" + tempSid);
				loginLaywerToken(userName, userPassword, token);
			}
			
		} else {
			Log.i(TAG, "loginBackground会话存在，直接登录:" + tempSid);
			loginLaywerToken(userName, userPassword, token);
		}
	}
	
	private Handler mHandler = new Handler();
	
	public void changeBtnStatus(){
		mHandler.post(new Runnable() {
			public void run() {
				btnLogin.setFocusable(true);
				btnLogin.setClickable(true);
				if (null != waitDialog) {
		    		waitDialog.dismiss();
		    	}
			}
		});
		
	}

	private int loginTimes = 0;

	/**
	 * 普通用户登录
	 * 
	 * @param userName
	 * @param userPassword
	 */
	public void loginNormal(String userName, String userPassword) {
		loginTimes++;
		if (loginTimes > 4) {
			loginTimes = 0;
			return;
		}
		BaseResponse br = authLogic.login(tempSid, yzm, userName, userPassword,
				LoginCache.LOGIN_TYPE_NORMAL, null, null, null);
		changeBtnStatus();
		if (br.isSuccess()) {
			loginTimes = 0;
			Intent intent = getIntent();
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (br.isXtcw()) {
			loginTimes = 0;
			showToast(br.getMessage());
		} else {
			if (br.getErrorShowType() != null) {
				if (Constants.ERROR_SHOW_YZM == br.getErrorShowType()
						.intValue()) {
					Log.i(TAG, "重新获取验证码");
					loginTimes = 0;
					showYzm();
				} else if (Constants.ERROR_REQUEST_SID == br.getErrorShowType()
						.intValue()) {
					Log.e(TAG, "Tempsid不存在，重新请求。。。。");
					BaseResponse tempBr = authLogic.getTempSid();
					if (tempBr.isSuccess()) {
						Log.i(TAG, "重新获取会话成功，重新登录");
						tempSid = tempBr.getTempSid();
						loginNormal(userName, userPassword);
					} else {
						Log.i(TAG, "重新获取会话失败");
						showToast("建立会话失败");
					}
				}
			} else {
				showToast(br.getMessage());
			}
		}
	}

	/**
	 * 律师用户令牌登录
	 */
	public void loginLaywerToken(String userName, String userPassword,
			String token) {
		loginTimes++;
		if (loginTimes > 4) {
			loginTimes = 0;
			return;
		}
		BaseResponse br = authLogic.login(tempSid, yzm, userName, userPassword,
				LoginCache.LOHIN_TYPE_LAYWER_TOKEN, token, null, null);
		
		if (br.isSuccess()) {
			authLogic.getQRCode(userName, System.currentTimeMillis()); 
			loginTimes = 0;
			Intent intent = getIntent();
			setResult(Activity.RESULT_OK, intent);
			changeBtnStatus();
			finish();
		} else if (br.isXtcw()) {
			changeBtnStatus();
			loginTimes = 0;
			showToast(br.getMessage());
		} else {
			changeBtnStatus();
			if (br.getErrorShowType() != null) {
				if (Constants.ERROR_SHOW_YZM == br.getErrorShowType()
						.intValue()) {
					Log.i(TAG, "重新获取验证码");
					loginTimes = 0;
					showYzm();
				}
			} else {
				showToast(br.getMessage());
			}
		}
	}

	/**
	 * 律师用户手机登录
	 */
	public void loginLaywerPhone(String userName, String phoneNumber,
			String phoneCode) {
		loginTimes++;
		if (loginTimes > 4) {
			loginTimes = 0;
			return;
		}
		BaseResponse br = authLogic.login(tempSid, yzm, userName, null,
				LoginCache.LOGIN_TYPE_LAYWER_PHONE, null, phoneCode,
				phoneNumber);
		changeBtnStatus();
		if (br.isSuccess()) {
			loginTimes = 0;
			authLogic.getQRCode(userName, System.currentTimeMillis()); 
			Intent intent = getIntent();
			setResult(Activity.RESULT_OK, intent);
			finish();
		} else if (br.isXtcw()) {
			loginTimes = 0;
			showToast(br.getMessage());
		} else {
			if (br.getErrorShowType() != null) {
				if (Constants.ERROR_SHOW_YZM == br.getErrorShowType()
						.intValue()) {
					Log.i(TAG, "重新获取验证码");
					loginTimes = 0;
					showYzm();
				}
			} else {
				showToast(br.getMessage());
			}
		}
	}

	/**
	 * 显示验证码
	 */
	@UiThread
	public void showYzm() {
		Intent intent = new Intent(this, YzmDialogActivity_.class);
		startActivityForResult(intent, Constants.REQUEST_CODE_YZM);
	}

	/**
	 * 显示验证码
	 */
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == Constants.REQUEST_CODE_YZM
				&& resultCode == RESULT_OK) {
			yzm = data.getStringExtra("yzm");
		}
	}

	/**
	 * 找回密码
	 */
	public void reGetPassword() {
		Intent intent = new Intent(this, ZhmmActivity_.class);
		startActivity(intent);
	}

	/**
	 * 注册
	 */
	public void register() {
		Intent intent = new Intent(this, ZcActivity_.class);
		startActivity(intent);
	}

	/**
	 * 处理输入框中的字符串
	 * 
	 * @param et
	 * @return
	 */
	public String getHandledString(EditText et) {
		return et.getText().toString().trim();
	}
	
	
	@Override
    protected void onDestroy() {
    	if (null != waitDialog) {
    		waitDialog.dismiss();
    	}
    	super.onDestroy();
    }

	/**
	 * 获取验证码
	 */
	@Background
	public void getPhoneCode(String userName, String phoneNumber) {
		BaseResponse br = authLogic.getPhoneCode(userName, phoneNumber);
		if (br.isSuccess()) {
			showToast("验证码已发送");
		} else {
			message=br.getMessage();
			showToast(message);
			currentSeconds = 60;
		}
	}
}
