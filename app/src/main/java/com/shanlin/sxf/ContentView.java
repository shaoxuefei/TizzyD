package com.shanlin.sxf;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Sxf on 2017/5/11.
 *
 * @project: Demo.
 * @detail:
 */

public class ContentView extends LinearLayout {
    private TextView textContent;
    private String tvContent="在此特别提醒您（用户）在注册成为\n" +
            "\n" +
            "***\n" +
            "\n" +
            "用户之前，请认真阅读本《\n" +
            "\n" +
            "***\n" +
            "\n" +
            "用户服务协议》\n" +
            "\n" +
            "（以下简称“协议”）\n" +
            "\n" +
            "，\n" +
            "\n" +
            "确保您充分理解本协议中各条款。\n" +
            "\n" +
            "请您审慎阅读并选择接受或不接\n" +
            "\n" +
            "受本协议。\n" +
            "\n" +
            "除非您接受本协议所有条款，\n" +
            "\n" +
            "否则您无权注册、登录或使用本协议所涉服务。您\n" +
            "\n" +
            "的注册、登录、使用等行为将视为对本协议的接受，并同意接受本协议各项条款的约束。\n" +
            "\n" +
            " \n" +
            "\n" +
            "本协议约定\n" +
            "\n" +
            "***\n" +
            "\n" +
            "与用户之间关于“\n" +
            "\n" +
            "***\n" +
            "\n" +
            "”服务\n" +
            "\n" +
            "（以下简称“服务”）\n" +
            "\n" +
            "的权利义务。\n" +
            "\n" +
            "“用户”\n" +
            "\n" +
            "是指注册、登录、使用本服务的个人。本协议可由\n" +
            "\n" +
            "***\n" +
            "\n" +
            "随时更新，更新后的协议条款一旦公\n" +
            "\n" +
            "布即代替原来的协议条款，\n" +
            "\n" +
            "恕不再另行通知，\n" +
            "\n" +
            "用户可在本\n" +
            "\n" +
            "APP\n" +
            "\n" +
            "中查阅最新版协议条款。\n" +
            "\n" +
            "在修\n" +
            "\n" +
            "改协议条款后，如果用户不接受修改后的条款，请立即停止使用\n" +
            "\n" +
            "***\n" +
            "\n" +
            "提供的服务，用户继续";
    public ContentView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.content_view,this,true);
        textContent= (TextView) findViewById(R.id.textContent);
        textContent.setText(tvContent);
    }
}
