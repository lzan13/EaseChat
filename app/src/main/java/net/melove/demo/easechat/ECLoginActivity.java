package net.melove.demo.easechat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

public class ECLoginActivity extends AppCompatActivity {

    // 弹出框
    private ProgressDialog mDialog;

    // username 输入框
    private EditText mUsernameEdit;
    // 密码输入框
    private EditText mPasswordEdit;

    // 注册按钮
    private Button mSignUpBtn;
    // 登录按钮
    private Button mSignInBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initView();
    }

    /**
     * 初始化界面控件
     */
    private void initView() {
        mUsernameEdit = (EditText) findViewById(R.id.ec_edit_username);
        mPasswordEdit = (EditText) findViewById(R.id.ec_edit_password);

        mSignUpBtn = (Button) findViewById(R.id.ec_btn_sign_up);
        mSignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });

        mSignInBtn = (Button) findViewById(R.id.ec_btn_sign_in);
        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    /**
     * 注册方法
     */
    private void signUp() {
        // 注册是耗时过程，所以要显示一个dialog来提示下用户
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = mUsernameEdit.getText().toString().trim();
                    String password = mPasswordEdit.getText().toString().trim();
                    EMClient.getInstance().createAccount(username, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!ECLoginActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            Toast.makeText(ECLoginActivity.this, "注册成功", Toast.LENGTH_LONG).show();
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!ECLoginActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                            // 网络错误
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(ECLoginActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            // 用户已存在
                            case EMError.USER_ALREADY_EXIST:
                                Toast.makeText(ECLoginActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                            case EMError.USER_ILLEGAL_ARGUMENT:
                                Toast.makeText(ECLoginActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器未知错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(ECLoginActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            case EMError.USER_REG_FAILED:
                                Toast.makeText(ECLoginActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(ECLoginActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * 登录方法
     */
    private void signIn() {
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("正在登陆，请稍后...");
        mDialog.show();
        String username = mUsernameEdit.getText().toString().trim();
        String password = mPasswordEdit.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(ECLoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();

                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();

                        // 登录成功跳转界面
                        Intent intent = new Intent(ECLoginActivity.this, ECMainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mDialog.dismiss();
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                        // 网络异常 2
                        case EMError.NETWORK_ERROR:
                            Toast.makeText(ECLoginActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 无效的用户名 101
                        case EMError.INVALID_USER_NAME:
                            Toast.makeText(ECLoginActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 无效的密码 102
                        case EMError.INVALID_PASSWORD:
                            Toast.makeText(ECLoginActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 用户认证失败，用户名或密码错误 202
                        case EMError.USER_AUTHENTICATION_FAILED:
                            Toast.makeText(ECLoginActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 用户不存在 204
                        case EMError.USER_NOT_FOUND:
                            Toast.makeText(ECLoginActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 无法访问到服务器 300
                        case EMError.SERVER_NOT_REACHABLE:
                            Toast.makeText(ECLoginActivity.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 等待服务器响应超时 301
                        case EMError.SERVER_TIMEOUT:
                            Toast.makeText(ECLoginActivity.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 服务器繁忙 302
                        case EMError.SERVER_BUSY:
                            Toast.makeText(ECLoginActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        // 未知 Server 异常 303 一般断网会出现这个错误
                        case EMError.SERVER_UNKNOWN_ERROR:
                            Toast.makeText(ECLoginActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        default:
                            Toast.makeText(ECLoginActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                            break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
