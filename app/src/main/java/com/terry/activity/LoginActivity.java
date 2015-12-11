package com.terry.activity;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.bean.Person;
import com.terry.util.ToastAlone;
import com.terry.view.GifLoadingDialog;

import org.xutils.common.util.LogUtil;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 作者：Terry.Chen on 2015/12/81448.
 * 邮箱：herewinner@163.com
 * 描述：登录界面
 */
public class LoginActivity extends BaseActivity {
    private ImageView back_img;
    private TextView regist_text, login_text;
    private final static int REGIST_REQUEST_CODE = 100;
    private EditText username_edittext, password_edittext;

    private String username;
    private GifLoadingDialog dialog;
    @Override
    protected void initView() {
        setContentView(R.layout.login_layout);

        back_img = (ImageView) findViewById(R.id.back_img);
        regist_text = (TextView) findViewById(R.id.regist_text);
        username_edittext = (EditText) findViewById(R.id.username_edittext);
        password_edittext = (EditText) findViewById(R.id.password_edittext);
        login_text = (TextView) findViewById(R.id.login_text);
    }

    @Override
    protected void initData() {
        dialog = new GifLoadingDialog(mContext, -1);
    }

    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regist_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, RegisterActivity.class);
                startActivityForResult(intent, REGIST_REQUEST_CODE);
            }
        });

        login_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = username_edittext.getEditableText().toString();
                String password = password_edittext.getEditableText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastAlone.show("用户名不能为空");
                }else if (username.length() > 13) {
                    ToastAlone.show("用户名长度不能大于13位");
                }else if (TextUtils.isEmpty(password)) {
                    ToastAlone.show("密码不能为空");
                }else if (password.length() > 20) {
                    ToastAlone.show("密码不能超过20位");
                }else {
                    dialog.show();
                    Person person = new Person();
                    person.setUsername(username);
                    person.setPassword(password);
                    loginPerson(person);
                }
            }
        });
    }

    private void loginPerson(final Person person) {
        person.login(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                queryUserInfo();
            }

            @Override
            public void onFailure(int i, String s) {
                dialog.dismiss();
            }
        });
    }

    private void queryUserInfo() {
        BmobQuery<Person> persons = new BmobQuery<Person>();
        persons.addWhereEqualTo("username", username);
        persons.findObjects(mContext, new FindListener<Person>() {
            @Override
            public void onSuccess(List<Person> list) {
                dialog.dismiss();
                Person person = list.get(0);
                ToastAlone.show(R.string.login_success);
                spUtil.setPersonObjId(person.getObjectId());
                spUtil.setPersonName(person.getUsername());
                spUtil.setPersonHead(person.getHeadPic());
                spUtil.setPersonEmail(person.getEmail());
                spUtil.setPersonPhone(person.getMobilePhoneNumber());
                //登录成功，进行查询操作
                setResult(RESULT_OK);
                finish();
            }

            @Override
            public void onError(int i, String s) {
                dialog.dismiss();
                ToastAlone.show(s);
            }
        });

    }

    @Override
    protected void initToolbar() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REGIST_REQUEST_CODE) {
            //证明成功了
            setResult(RESULT_OK);
            finish();
        }
    }
}
