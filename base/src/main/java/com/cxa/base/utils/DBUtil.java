package com.cxa.base.utils;

import android.database.Cursor;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 把cursor转换为vo，
 * 注意：Cursor的字段名或者别名要和VO的成员名一致
 * Created by DefterosChen.
 */
public class DBUtil {

    /**
     * 通过Cursor转换成对应的VO。
     * 注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param c
     * @param clazz
     * @return
     */
    public static Object cursorToModel(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        Object obj;
        try {
            c.moveToNext();
            obj = setValuesToFields(c, clazz);
            return obj;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DBUtil","通过Cursor转换成对应的VO出错！！");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 通过Cursor转换成对应的VO集合。
     * 注意：Cursor里的字段名（可用别名）必须要和VO的属性名一致
     *
     * @param c
     * @param clazz
     * @return
     */
    public static List cursorToList(Cursor c, Class clazz) {
        if (c == null) {
            return null;
        }
        List list = new ArrayList();
        Object obj;
        try {
            while (c.moveToNext()) {
                obj = setValuesToFields(c, clazz);
                list.add(obj);
            }
            return list;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DBUtil","通过Cursor转换成对应的VO List出错！！");
            return null;
        } finally {
            c.close();
        }
    }

    /**
     * 把Cursor值设置进类属性里
     *
     * @param c
     * @param clazz
     * @return
     * @throws Exception
     */
    @SuppressWarnings("rawtypes")
    private static Object setValuesToFields(Cursor c, Class clazz) throws Exception {
        String[] columnNames = c.getColumnNames();// 字段数组
        Object obj = clazz.newInstance();
        Field[] fields = clazz.getDeclaredFields();
        for (Field _field : fields) {
            /**Android studio 2.1之后增加了Instant Run特性，
             * 导致fields多出了个属性$change
             */
            if (_field.isSynthetic()){//此处判断当前属性是否编译时插入的
                continue;
            }
            Class<? extends Object> typeClass = _field.getType();// 属性类型
            for (int j = 0; j < columnNames.length; j++) {
                String columnName = columnNames[j];
                typeClass = getBasicClass(typeClass);
                boolean isBasicType = isBaseType(typeClass);
                if (isBasicType) {
                    if (columnName.equalsIgnoreCase(_field.getName())) {// 是基本类型
                        String _str = c.getString(c.getColumnIndex(columnName));
                        if (_str == null) {
                            break;
                        }
                        _str = _str == null ? "" : _str;
                        Constructor<? extends Object> cons = typeClass
                                .getConstructor(String.class);
                        Object attribute = cons.newInstance(_str);
                        _field.setAccessible(true);
                        _field.set(obj, attribute);

                        break;
                    }
                } else {
                    Object obj2 = setValuesToFields(c, typeClass);// 递归
                    _field.set(obj, obj2);
                    break;
                }

            }
        }
        return obj;
    }

    /**
     * 判断是不是基本类型
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    private static boolean isBaseType(Class typeClass) {
        if (typeClass.equals(Integer.class) || typeClass.equals(Long.class)
                || typeClass.equals(Float.class)
                || typeClass.equals(Double.class)
                || typeClass.equals(Boolean.class)
                || typeClass.equals(Byte.class)
                || typeClass.equals(Short.class)
                || typeClass.equals(String.class)) {

            return true;

        } else {
            return false;
        }
    }

    /**
     * 获得包装类
     *
     * @param typeClass
     * @return
     */
    @SuppressWarnings("all")
    private static Class<? extends Object> getBasicClass(Class typeClass) {
        Class _class = basicMap.get(typeClass);
        if (_class == null)
            _class = typeClass;
        return _class;
    }

    @SuppressWarnings("rawtypes")
    private static Map<Class, Class> basicMap = new HashMap<Class, Class>();

    static {
        basicMap.put(int.class, Integer.class);
        basicMap.put(long.class, Long.class);
        basicMap.put(float.class, Float.class);
        basicMap.put(double.class, Double.class);
        basicMap.put(boolean.class, Boolean.class);
        basicMap.put(byte.class, Byte.class);
        basicMap.put(short.class, Short.class);
    }
}

