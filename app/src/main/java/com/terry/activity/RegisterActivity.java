package com.terry.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.terry.BaseActivity;
import com.terry.R;
import com.terry.bean.Person;
import com.terry.util.ToastAlone;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.listener.SaveListener;

/**
 * 作者：Terry.Chen on 2015/12/101445.
 * 邮箱：herewinner@163.com
 * 描述：注册页面
 */
public class RegisterActivity extends BaseActivity {

    private ImageView back_img;
    private TextView regist_commit_text;
    private EditText username_edit, pass_edit, phone_edit, email_edit;
    @Override
    protected void initView() {
        setContentView(R.layout.regist_layout);
        back_img = (ImageView) findViewById(R.id.back_img);
        regist_commit_text = (TextView) findViewById(R.id.regist_commit_text);
        username_edit = (EditText) findViewById(R.id.username_edit);
        pass_edit = (EditText) findViewById(R.id.pass_edit);
        phone_edit = (EditText) findViewById(R.id.phone_edit);
        email_edit = (EditText) findViewById(R.id.email_edit);
    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListener() {
        back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        regist_commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = username_edit.getEditableText().toString();
                String password = pass_edit.getEditableText().toString();
                String phone = phone_edit.getEditableText().toString();
                String email = email_edit.getEditableText().toString();
                if (TextUtils.isEmpty(username)) {
                    ToastAlone.show("用户名不能为空");
                }else if (username.length() > 13) {
                    ToastAlone.show("用户名长度不能大于13位");
                }else if (TextUtils.isEmpty(password)) {
                    ToastAlone.show("密码不能为空");
                }else if (password.length() > 20) {
                    ToastAlone.show("密码不能超过20位");
                }else {
                    Person person = new Person();
                    person.setUsername(username);
                    person.setPassword(password);
                    person.setEmail(email);
                    person.setMobilePhoneNumber(phone);
                    commitRegistInfo(person);
                }
            }
        });
    }

    private void commitRegistInfo(final Person person) {
        person.signUp(mContext, new SaveListener() {
            @Override
            public void onSuccess() {
                spUtil.setPersonObjId(person.getObjectId());
                spUtil.setPersonName(person.getUsername());
                ToastAlone.show(R.string.regist_success);
            }

            @Override
            public void onFailure(int i, String s) {
                ToastAlone.show(R.string.regist_fail);
            }
        });
    }

    @Override
    protected void initToolbar() {

    }
}
