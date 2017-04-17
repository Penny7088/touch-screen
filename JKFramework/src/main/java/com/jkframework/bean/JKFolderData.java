package com.jkframework.bean;

import java.util.List;

/**
 * 文件夹
 * Created by Nereo on 2015/4/7.
 */
public class JKFolderData {
    public String name;
    public String path;
    public JKImageData cover;
    public List<JKImageData> images;

    @Override
    public boolean equals(Object o) {
        try {
            JKFolderData other = (JKFolderData) o;
            return this.path.equalsIgnoreCase(other.path);
        }catch (ClassCastException e){
            e.printStackTrace();
        }
        return super.equals(o);
    }
}
