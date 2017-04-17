package com.jkframework.bean;

/**
 * 图片实体
 * Created by Nereo on 2015/4/7.
 */
public class JKImageData {
    public String path;
    public String name;
    public long time;

    public JKImageData(String path, String name, long time){
        this.path = path;
        this.name = name;
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        try {
            JKImageData other = (JKImageData) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
