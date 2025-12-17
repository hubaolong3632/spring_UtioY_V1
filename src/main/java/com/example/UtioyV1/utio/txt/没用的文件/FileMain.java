//package com.example.UtioyV1.utio.txt.没用的文件;
//
//import java.io.*;
//import java.net.URISyntaxException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FileMain {
//    // 替换规则：把内容里的 V1 换成 UtioyV1
////    private static final String OLD_PACKAGE = "com.example.UtioyV1";
////    private static final String NEW_PACKAGE = "com.example.UtioyV1";
//
//
//    private static final String OLD_PACKAGE = "com.example.UtioyV1";
//    private static final String NEW_PACKAGE = "com.example.UtioyV1";
//    // 排除自身文件
//    private static final String EXCLUDE_FILE = "FileMain.java";
//    // 存储待处理的.java文件
//    private static final List<File> javaFileList = new ArrayList<>();
//
//    public static void main(String[] args) {
//        try {
//            // 核心：纯自动获取源码根路径（com/example/UtioyV1）
//            File sourceRootDir = autoGetSourceRootDir();
//            if (sourceRootDir == null) {
//                System.err.println("❌ 自动获取源码路径失败！");
//                printDiagnosticInfo(); // 打印诊断信息，帮助定位问题
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
//            // 替换内容
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
//     * 纯自动获取源码根路径（核心逻辑）
//     * 原理：
//     * 1. 获取当前类的包名（com.example.UtioyV1.utio）→ 截取到上一级（com.example.UtioyV1）
//     * 2. 从类加载路径反向推导项目根 → 拼接 src/main/java + 包路径
//     */
//    private static File autoGetSourceRootDir() throws URISyntaxException {
//        // 1. 获取当前类的包信息
//        Class<FileMain> clazz = FileMain.class;
//        String currentPackage = clazz.getPackage().getName(); // com.example.UtioyV1.utio
//        // 截取到目标包（去掉最后一级 utio）→ com.example.UtioyV1
//        String targetPackage = currentPackage.substring(0, currentPackage.lastIndexOf("."));
//        String targetPackagePath = targetPackage.replace(".", File.separator); // com/example/UtioyV1
//
//        // 2. 获取项目根目录（通过类编译路径反向推导）
//        String classPath = clazz.getResource("").toURI().getPath();
//        // 处理Windows路径开头的/
//        if (File.separatorChar == '\\' && classPath.startsWith("/")) {
//            classPath = classPath.substring(1);
//        }
//        File classDir = new File(classPath);
//
//        // 3. 向上找项目根（直到找到包含src的目录）
//        File projectRoot = classDir;
//        while (projectRoot != null) {
//            // 检查是否是项目根（包含src、pom.xml/ build.gradle）
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
//            // 兼容Gradle/非标准源码目录（src/java）
//            File sourceRootGradle = new File(
//                    projectRoot + File.separator + "src" + File.separator + "java" + File.separator + targetPackagePath
//            );
//            if (sourceRootGradle.exists()) {
//                return sourceRootGradle;
//            }
//        }
//
//        // 5. 终极兜底：通过系统属性+当前类包名拼接
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
//     * 打印诊断信息（帮助定位自动推导失败原因）
//     */
//    private static void printDiagnosticInfo() throws URISyntaxException {
//        System.out.println("\n===== 诊断信息 =====");
//        Class<FileMain> clazz = FileMain.class;
//        // 1. 当前类包名
//        System.out.println("1. 当前类包名：" + clazz.getPackage().getName());
//        // 2. 类编译路径
//        String classPath = clazz.getResource("").toURI().getPath();
//        if (File.separatorChar == '\\' && classPath.startsWith("/")) {
//            classPath = classPath.substring(1);
//        }
//        System.out.println("2. 类编译路径：" + classPath);
//        // 3. 系统工作目录
//        System.out.println("3. 系统工作目录：" + System.getProperty("user.dir"));
//        // 4. 项目根目录推导结果
//        File classDir = new File(classPath);
//        File projectRoot = classDir;
//        while (projectRoot != null && !new File(projectRoot, "src").exists()) {
//            projectRoot = projectRoot.getParentFile();
//        }
//        System.out.println("4. 推导的项目根：" + (projectRoot == null ? "null" : projectRoot.getAbsolutePath()));
//        // 5. 拼接的源码路径
//        String targetPackage = clazz.getPackage().getName().substring(0, clazz.getPackage().getName().lastIndexOf("."));
//        String targetPackagePath = targetPackage.replace(".", File.separator);
//        File sourceRoot = new File(
//                (projectRoot == null ? System.getProperty("user.dir") : projectRoot)
//                        + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + targetPackagePath
//        );
//        System.out.println("5. 拼接的源码路径：" + sourceRoot.getAbsolutePath());
//        System.out.println("6. 该路径是否存在：" + sourceRoot.exists());
//        System.out.println("====================");
//    }
//}