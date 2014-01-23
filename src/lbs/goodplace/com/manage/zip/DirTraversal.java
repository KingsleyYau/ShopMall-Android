package lbs.goodplace.com.manage.zip;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
/**
 *  文件夹遍历
 * 
 *
 */
public class DirTraversal {
    
    //no recursion
    public static LinkedList<File> listLinkedFiles(String strPath) {
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(strPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else
                System.out.println(file[i].getAbsolutePath());
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);
                    else
                        System.out.println(file[i].getAbsolutePath());
                }
            } else {
                System.out.println(tmp.getAbsolutePath());
            }
        }
        return list;
    }

    
    //recursion
    public static Map<String,File> getAllFiles(String strPath) {
    	Map<String,File> fileMap = new HashMap<String,File>();
        refreshFileList(strPath,fileMap);
        return fileMap;
    }

    public static void refreshFileList(String strPath, Map<String,File> fileMap) {
        //ArrayList<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();

        if (files == null)
            return;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFileList(files[i].getAbsolutePath(), fileMap);
            } else {
                //if(files[i].getName().toLowerCase().endsWith("zip"))
                //fileList.add(files[i]);
            	fileMap.put(files[i].getAbsolutePath(), files[i]);
            }
        }      
    }
}