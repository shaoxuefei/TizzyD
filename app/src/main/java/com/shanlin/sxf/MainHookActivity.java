package com.shanlin.sxf;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.shanlin.sxf.bean.HookPersonBean;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class MainHookActivity extends AppCompatActivity {

    HookPersonBean hookPersonBean;
    Button btnClick, btnNotification;
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_hook);

        btnClick = findViewById(R.id.btn_click);
        btnNotification = findViewById(R.id.btn_notification);
        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i++;
                btnClick.setText(String.valueOf(i));
            }
        });
        checkOnClickListener(btnClick);
        final String channelId = "hook_notification_id";
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //发送通知
                NotificationManager systemService = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    NotificationChannel channel = new NotificationChannel(channelId, "hook_notification_name",
                            NotificationManager.IMPORTANCE_DEFAULT);
                    systemService.createNotificationChannel(channel);
                }

                NotificationCompat.Builder builder = new NotificationCompat.Builder(MainHookActivity.this, "hook_notification_id");
                builder.setTicker("Hook")
                        .setContentText("Hook-Notification")
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setChannelId(channelId)
                        .setSubText("Hook_SubTitle");
                Notification notification = builder.build();
                systemService.notify(0, notification);
            }
        });
        hookNotification();
    }

    private void getPersonConstructor() {
        try {
            //一般接口是这样声明Class.forName("xxx");
            Class<?> aClass = Class.forName("com.shanlin.sxf.bean.HookPersonBean");
//            other class method to get Class not include interfaces
//            Class<HookPersonBean> hookPersonBeanClass = HookPersonBean.class;
            Constructor<?> constructor = aClass.getConstructor();

            Constructor<?> constructor1 = aClass.getConstructor(String.class);

            Object objectInstance = constructor1.newInstance("张三");

            HookPersonBean hookPersonBean = (HookPersonBean) objectInstance;

            Constructor<?> constructor2 = aClass.getConstructor(String.class, String.class, Integer.class);
            //如果获取的方法或者是属性不是public的话需要设置setAccessible(true)；
            constructor2.setAccessible(true);

            Constructor<?>[] constructors = aClass.getConstructors();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPersonFiled() {

        try {
            Class<?> aClass = Class.forName("com.shanlin.sxf.bean.HookPersonBean");
            HookPersonBean hookPersonBean = new HookPersonBean("李四");
            Field name = aClass.getDeclaredField("name");
            name.setAccessible(true);

            String strName = (String) name.get(hookPersonBean);

            //更新对应的Field 的Value
            name.set(hookPersonBean, "王五");

            //获取修改后的Field
            String changeName = (String) name.get(hookPersonBean);

            Field[] fields = aClass.getFields();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void getPersonMethod() {
        try {
            Class<?> aClass = Class.forName("com.shanlin.sxf.bean.HookPersonBean");

            HookPersonBean hookPersonBean = new HookPersonBean();


            Method getUserInfo = aClass.getMethod("getUserInfo");

            Method getPersonSex = aClass.getDeclaredMethod("getPersonSex", boolean.class);

            Method getUserNameAge = aClass.getDeclaredMethod("getUserNameAge");

            getUserInfo.invoke(hookPersonBean);

            String invoke = (String) getPersonSex.invoke(hookPersonBean, false);

            String invoke1 = (String) getUserNameAge.invoke(hookPersonBean);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class DynamicProxyHandler implements InvocationHandler {
        private Object target;

        public DynamicProxyHandler(Object target) {
            this.target = target;
        }

        public <T> T getProxy() {
            //生产动态的接口代理类
            return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), this);
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return method.invoke(target, args);
        }
    }

    private void getProxyDynamicHandler() {
        RealBaseClick realBaseClick = new RealBaseClick();
        DynamicProxyHandler dynamicProxyHandler = new DynamicProxyHandler(realBaseClick);
        //动态生产代理对象--反射串改引动对象为代理对象
        IBaseClick proxyBaseClick = dynamicProxyHandler.getProxy();

    }


    //静态代理类声明
    public interface IBaseClick {
        void doAction();
    }

    private class RealBaseClick implements IBaseClick {

        @Override
        public void doAction() {
            //real  to  do
        }
    }

    //静态代理类
    private class ProxyBaseClick implements IBaseClick {
        private IBaseClick iBaseClick;

        public ProxyBaseClick(IBaseClick iBaseClick) {
            this.iBaseClick = iBaseClick;
        }

        @Override
        public void doAction() {
            //do other thing
            if (iBaseClick != null) {
                iBaseClick.doAction();
            }
        }
    }

    //修改系统的OnClickListener

    private void checkOnClickListener(final View view) {

        try {
            Method getListenerInfo = View.class.getDeclaredMethod("getListenerInfo");
            getListenerInfo.setAccessible(true);
            Object invoke = getListenerInfo.invoke(view);
            Class<?> aClass = Class.forName("android.view.View$ListenerInfo");
            Field mOnClickListener = aClass.getDeclaredField("mOnClickListener");
            mOnClickListener.setAccessible(true);
            final View.OnClickListener mClickListener = (View.OnClickListener) mOnClickListener.get(invoke);

            Class<?> aClass1 = Class.forName("android.view.View$OnClickListener");
            Object mDynamicClickListener = Proxy.newProxyInstance(mClickListener.getClass().getClassLoader(), new Class[]{aClass1}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Toast.makeText(MainHookActivity.this, "反射拦截点击事件", Toast.LENGTH_SHORT).show();
                    //此处的invoke第一个参数也就是:交给原先的系统方法执行，自己不拦截处理,所以一般这个变量就是你Proxy时设置进去的代理接口对象的实例,
                    //类似下边的HookedOnClickListenerProxy 手动定义的静态代理类：内部实现:onClickListener.onClick(v);
                    return method.invoke(mClickListener, args);
                }
            });

            HookedOnClickListenerProxy hookedOnClickListenerProxy = new HookedOnClickListenerProxy(mClickListener);
//            mOnClickListener.set(invoke, hookedOnClickListenerProxy);
            mOnClickListener.set(invoke, mDynamicClickListener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class HookedOnClickListenerProxy implements View.OnClickListener {

        private View.OnClickListener onClickListener;

        public HookedOnClickListenerProxy(View.OnClickListener onClickListener) {
            this.onClickListener = onClickListener;
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(MainHookActivity.this, "反射拦截点击事件", Toast.LENGTH_SHORT).show();
            if (onClickListener != null) {
                onClickListener.onClick(v);
            }
        }
    }

    public void hookNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        try {
            Method getService = NotificationManager.class.getDeclaredMethod("getService");
            getService.setAccessible(true);
            final Object invoke = getService.invoke(notificationManager);

            Class<?> aClass = Class.forName("android.app.INotificationManager");
            Object hookService = Proxy.newProxyInstance(invoke.getClass().getClassLoader(), new Class[]{aClass}, new InvocationHandler() {
                @Override
                public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                    Toast.makeText(MainHookActivity.this, "Hook反射拦截Notification", Toast.LENGTH_SHORT).show();
                    return method.invoke(invoke, args);
                }
            });
            Field sService = NotificationManager.class.getDeclaredField("sService");
            sService.setAccessible(true);
            sService.set(notificationManager, hookService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
