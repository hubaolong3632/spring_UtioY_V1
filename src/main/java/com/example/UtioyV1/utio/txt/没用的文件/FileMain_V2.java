//package com.example.UtioyV1.utio.txt.没用的文件;
//
//import java.io.*;
//import java.net.URISyntaxException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FileMain_V2 {
//    // 要被替换的旧包名（仅需改这个）
//    private static final String OLD_PACKAGE = "com.example.UtioyV1";
//    // 自动获取的新包名（运行时动态赋值）
//    private static String NEW_PACKAGE;
//    // 排除自身文件
//    private static final String EXCLUDE_FILE = "FileMain.java";
//    // 存储待处理的.java文件
//    private static final List<File> javaFileList = new ArrayList<>();
//
//    public static void main(String[] args) {
//        try {
//            // 核心1：自动获取当前项目的目标包名（com.example.UtioyV1）
//            NEW_PACKAGE = autoGetTargetPackageName();
//            if (NEW_PACKAGE == null) {
//                System.err.println("❌ 自动获取目标包名失败！");
//                return;
//            }
//            System.out.println("✅ 自动识别目标包名：" + NEW_PACKAGE);
//
//            // 核心2：自动获取源码根路径（com/example/UtioyV1）
//            File sourceRootDir = autoGetSourceRootDir();
//            if (sourceRootDir == null) {
//                System.err.println("❌ 自动获取源码路径失败！");
//                printDiagnosticInfo(); // 打印诊断信息
//                return;
//            }
//            System.out.println("✅ 自动定位源码根路径：" + sourceRootDir.getAbsolutePath());
//
//            // 校验目录
//            if (!sourceRootDir.exists()) {
//                System.err.println("❌ 源码目录不存在：" + sourceRootDir);
//                return;
//            }
//
//            // 收集文件
//            System.out.println("\n🔍 扫描.java文件（排除自身）...");
//            collectJavaFiles(sourceRootDir);
//            if (javaFileList.isEmpty()) {
//                System.err.println("⚠️ 无可用.java文件");
//                return;
//            }
//
//            // 替换内容（OLD_PACKAGE → 自动获取的NEW_PACKAGE）
//            System.out.println("\n📝 替换文件内容（" + OLD_PACKAGE + " → " + NEW_PACKAGE + "）...");
//            replacePackageInFiles();
//
//            System.out.println("\n🎉 操作完成！共处理 " + javaFileList.size() + " 个文件");
//        } catch (Exception e) {
//            System.err.println("❌ 执行异常：" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 核心：自动获取当前项目的目标包名（com.example.UtioyV1）
//     * 原理：从当前类的包名（com.example.UtioyV1.utio）截取到上一级
//     */
//    private static String autoGetTargetPackageName() {
//        Class<FileMain_V2> clazz = FileMain_V2.class;
//        String currentPackage = clazz.getPackage().getName(); // com.example.UtioyV1.utio
//        // 截取掉最后一级（utio），得到目标包名：com.example.UtioyV1
//        return currentPackage.substring(0, currentPackage.lastIndexOf("."));
//    }
//
//    /**
//     * 自动获取源码根路径（com/example/UtioyV1）
//     */
//    private static File autoGetSourceRootDir() throws URISyntaxException {
//        // 1. 获取目标包路径（com/example/UtioyV1）
//        String targetPackagePath = NEW_PACKAGE.replace(".", File.separator);
//
//        // 2. 获取当前类的编译路径
//        String classPath = FileMain_V2.class.getResource("").toURI().getPath();
//        // 处理Windows路径开头的/
//        if (File.separatorChar == '\\' && classPath.startsWith("/")) {
//            classPath = classPath.substring(1);
//        }
//        File classDir = new File(classPath);
//
//        // 3. 向上找项目根（包含src + pom.xml/build.gradle）
//        File projectRoot = classDir;
//        while (projectRoot != null) {
//            boolean isProjectRoot = new File(projectRoot, "src").exists()
//                    && (new File(projectRoot, "pom.xml").exists() || new File(projectRoot, "build.gradle").exists());
//            if (isProjectRoot) {
//                break;
//            }
//            projectRoot = projectRoot.getParentFile();
//            // 防止无限循环
//            if (projectRoot == null || projectRoot.getAbsolutePath().length() < 3) {
//                break;
//            }
//        }
//
//        // 4. 拼接源码路径（项目根 + src/main/java + 目标包路径）
//        if (projectRoot != null) {
//            File sourceRoot = new File(
//                    projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + targetPackagePath
//            );
//            if (sourceRoot.exists()) {
//                return sourceRoot;
//            }
//            // 兼容Gradle（src/java）
//            File sourceRootGradle = new File(
//                    projectRoot + File.separator + "src" + File.separator + "java" + File.separator + targetPackagePath
//            );
//            if (sourceRootGradle.exists()) {
//                return sourceRootGradle;
//            }
//        }
//
//        // 终极兜底：系统工作目录 + 目标包路径
//        String userDir = System.getProperty("user.dir");
//        File fallbackSourceRoot = new File(
//                userDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + targetPackagePath
//        );
//        return fallbackSourceRoot.exists() ? fallbackSourceRoot : null;
//    }
//
//    /**
//     * 收集.java文件（排除自身）
//     */
//    private static void collectJavaFiles(File dir) {
//        File[] files = dir.listFiles();
//        if (files == null) return;
//
//        for (File file : files) {
//            if (file.isDirectory()) {
//                collectJavaFiles(file);
//            } else if (file.getName().endsWith(".java")
//                    && !file.getName().equals(EXCLUDE_FILE)
//                    && !file.getName().contains("$")) {
//                javaFileList.add(file);
//                System.out.println("找到文件：" + file.getAbsolutePath());
//            }
//        }
//    }
//
//    /**
//     * 替换文件内容中的包名
//     */
//    private static void replacePackageInFiles() throws IOException {
//        for (File file : javaFileList) {
//            StringBuilder content = new StringBuilder();
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(file), StandardCharsets.UTF_8))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    // 替换：OLD_PACKAGE → 自动获取的NEW_PACKAGE
//                    content.append(line.replace(OLD_PACKAGE, NEW_PACKAGE)).append(System.lineSeparator());
//                }
//            }
//
//            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream(file), StandardCharsets.UTF_8))) {
//                bw.write(content.toString().trim());
//                bw.flush();
//            }
//            System.out.println("替换完成：" + file.getAbsolutePath());
//        }
//    }
//
//    /**
//     * 打印诊断信息（定位问题用）
//     */
//    private static void printDiagnosticInfo() throws URISyntaxException {
//        System.out.println("\n===== 诊断信息 =====");
//        Class<FileMain_V2> clazz = FileMain_V2.class;
//        System.out.println("1. 当前类包名：" + clazz.getPackage().getName());
//        System.out.println("2. 自动截取的目标包名：" + NEW_PACKAGE);
//        System.out.println("3. 类编译路径：" + clazz.getResource("").toURI().getPath());
//        System.out.println("4. 系统工作目录：" + System.getProperty("user.dir"));
//        System.out.println("====================");
//    }
//}