package com.shanlin.sxf.softkeybord;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.shanlin.sxf.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NewSoftInputActivity extends AppCompatActivity {
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.tv_write)
    TextView tvWrite;
    @BindView(R.id.softInputKeybord)
    NewSoftInputKeyBord softInputKeybord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_soft_input);
        ButterKnife.bind(this);
        softInputKeybord.setBaseActivity(this);
        tvContent.setText("Chris Wanstrath还向记者分享了关于GitHub的一些内幕信息︰\n" +
                "GitHub主要用Rails实现。我们在进行的post-commit集成小应用完全使用Merb编写。我们使用了Python的Pygments来做格式高亮显示，另外，还用了Ara T. Howard's Bj加上一些Ruby脚本来做我们的排队系统。当然，我们用了Ruby Grit库来和Git进行交互。\n" +
                "GitHub已经有了一组引人注目的特性，除了命令式的库浏览器和一个项目Wiki，GitHub甚至还包括了一个GitHub gem，以使通过shell方式使用GitHub更为方便。更多的未来特性已经在计划中︰\n" +
                "许多人都希望能有一个条目系统，因此一个简单的条目系统已经在开发中。此外，正如我前面所言，我们尚在进行RubyGems服务器和一些之前留出的post-commit钩子方面的工作。如果你不能或就是不想托管一个你自己的守护进程，你可以使用我们所提供的。\n" +
                "我们还在开发一些特性来帮助公司在使用Github时可以停留在sync之上。\n" +
                "最后，我们也在进行API发布方面的工作。我们很快就会发布一些只读性的API，随后是一些很强大的“写”集成。你可以使用API将新的事件发布到新闻feed中，发消息和做其他许多很酷的事情。\n" +
                "GitHub尚未设定官方版本的发布日期，不过估计在三月底（GitHub已经上线，但只能通过邀请注册）。更多关于GitHub的信息可以参见GitHub官方网站或GitHub博客。通过GitHub进行代码管理的开源项目列表也已经可以查阅。");
    }

    @OnClick(R.id.tv_write)
    public void onViewClicked() {
        softInputKeybord.showSoftKeyBord();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (softInputKeybord.linear_translate != null) {
            softInputKeybord.linear_translate.callOnClick();
        }
    }
}
