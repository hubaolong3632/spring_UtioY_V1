package com.example.UtioyV1.utio;

import java.io.File;
import java.net.URISyntaxException;

public class FileMain {
    public static void main(String[] args) {
        String packageAbsolutePath = getPackageAbsolutePath(FileMain.class);

        // 打印结果
        System.out.println("com.example.UtioyV1.utio 包的绝对路径：");
        System.out.println(packageAbsolutePath);
    }






    /**
     * 直接获取指定类所属包的绝对路径
     * @param clazz 包内任意类（此处用当前类FileMain）
     * @return 包的绝对路径（null表示获取失败）
     */
    private static String getPackageAbsolutePath(Class<?> clazz) {
        try {
            // 1. 获取类的绝对路径
            String classPath = clazz.getProtectionDomain().getCodeSource().getLocation().toURI().getPath();
            // 2. 拼接包路径（包名.替换为/）
            String packagePath = clazz.getPackage().getName().replace(".", File.separator);
            // 3. 拼接为完整绝对路径
            String absolutePath = new File(classPath, packagePath).getAbsolutePath();

            return absolutePath;
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return null;
        }
    }
}
