package com.shanlin.sxf;

import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 * Created by Sxf on 2017/9/14.
 */

public class Miss {

    public void initView(){
        try {
            //字节流
            InputStream inputStream=new FileInputStream("");
            //字符流---字节流和字符流的转换
            InputStreamReader inputStreamReader=new InputStreamReader(inputStream);
            //对象流--从文件中读对象
            ObjectInputStream objectInputStream=new ObjectInputStream(new FileInputStream("对象存在的Object"));
            Object object = objectInputStream.readObject();
            objectInputStream.close();
            //对象流--将对象输出到文件中
            ObjectOutputStream objectOutputStream=new ObjectOutputStream(new FileOutputStream("对象要存的Object"));
            objectOutputStream.writeObject(new Object());
            objectOutputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void useCollection(){
        HashMap<Integer,User> userHashMap=new HashMap<>();
        userHashMap.put(1,new User("张三",25));
        userHashMap.put(2,new User("李四",22));
        userHashMap.put(3,new User("王五",28));
        sortHashMap(userHashMap);

    }

    public void sortHashMap(HashMap<Integer,User> map){
        //entrySet 遍历
        Set<Map.Entry<Integer, User>> entries = map.entrySet();
        for (Map.Entry<Integer,User> entry:entries) {
            Integer key = entry.getKey();
            User value = entry.getValue();
        }
        //Iterator 遍历
        Iterator<Map.Entry<Integer, User>> iterator = entries.iterator();
        while (iterator.hasNext()){
            Map.Entry<Integer, User> next = iterator.next();
            Integer key = next.getKey();
            User value = next.getValue();
        }

        List<Map.Entry<Integer,User>> list=new ArrayList<Map.Entry<Integer, User>>(entries);
        Collections.sort(list, new Comparator<Map.Entry<Integer, User>>() {
            @Override
            public int compare(Map.Entry<Integer, User> o1, Map.Entry<Integer, User> o2) {
                return o1.getValue().age-o2.getValue().age;//>0--表示排序
            }
        });
        //list即是已经排序好的按照年龄倒叙排序--小--大
        for (int i=0;i<list.size();i++){
            Map.Entry<Integer, User> integerUserEntry = list.get(i);
            Integer key = integerUserEntry.getKey();
            User value = integerUserEntry.getValue();
            Log.e("aa","age:"+value.age);
        }

    }

}
