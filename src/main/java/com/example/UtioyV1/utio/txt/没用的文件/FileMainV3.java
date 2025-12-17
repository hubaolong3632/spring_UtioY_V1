//package com.example.UtioyV1.utio.txt.没用的文件;
//
//
//import java.io.*;
//import java.net.URISyntaxException;
//import java.nio.charset.StandardCharsets;
//import java.util.ArrayList;
//import java.util.List;
//
//public class FileMainV3 {
//    // 要被替换的旧包名（仅需改这个）
//    private static final String OLD_PACKAGE = "com.example.UtioyV1";
//    // 自动获取的新包名（运行时动态赋值）
//    private static String NEW_PACKAGE;
//    // 排除自身文件
//    private static final String EXCLUDE_FILE = "FileMainV3V3.java";
//    // 存储待处理的Java文件
//    private static final List<File> javaFileList = new ArrayList<>();
//    // 存储待处理的XML文件（resources/mapper下）
//    private static final List<File> xmlFileList = new ArrayList<>();
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
//            System.out.println("✅ 自动定位Java源码根路径：" + sourceRootDir.getAbsolutePath());
//
//            // 核心3：自动获取resources根路径（src/main/resources）
//            File resourcesRootDir = autoGetResourcesRootDir();
//            if (resourcesRootDir == null) {
//                System.err.println("❌ 自动获取Resources路径失败！");
//                return;
//            }
//            System.out.println("✅ 自动定位Resources根路径：" + resourcesRootDir.getAbsolutePath());
//
//            // 1. 收集并替换Java文件
//            collectAndReplaceJavaFiles(sourceRootDir);
//
//            // 2. 收集并替换XML文件（resources/mapper下）
//            collectAndReplaceXmlFiles(resourcesRootDir);
//
//            System.out.println("\n🎉 所有操作完成！");
//            System.out.println("📊 统计：Java文件处理 " + javaFileList.size() + " 个 | XML文件处理 " + xmlFileList.size() + " 个");
//        } catch (Exception e) {
//            System.err.println("❌ 执行异常：" + e.getMessage());
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * 自动获取当前项目的目标包名（com.example.UtioyV1）
//     */
//    private static String autoGetTargetPackageName() {
//        Class<FileMainV3> clazz = FileMainV3.class;
//        String currentPackage = clazz.getPackage().getName(); // com.example.UtioyV1.utio
//        // 截取掉最后一级（utio），得到目标包名：com.example.UtioyV1
//        return currentPackage.substring(0, currentPackage.lastIndexOf("."));
//    }
//
//    /**
//     * 自动获取Java源码根路径（src/main/java/com/example/UtioyV1）
//     */
//    private static File autoGetSourceRootDir() throws URISyntaxException {
//        String targetPackagePath = NEW_PACKAGE.replace(".", File.separator);
//        Class<FileMainV3> clazz = FileMainV3.class;
//
//        // 获取当前类的编译路径
//        String classPath = clazz.getResource("").toURI().getPath();
//        if (File.separatorChar == '\\' && classPath.startsWith("/")) {
//            classPath = classPath.substring(1);
//        }
//        File classDir = new File(classPath);
//
//        // 向上找项目根（包含src + pom.xml/build.gradle）
//        File projectRoot = classDir;
//        while (projectRoot != null) {
//            boolean isProjectRoot = new File(projectRoot, "src").exists()
//                    && (new File(projectRoot, "pom.xml").exists() || new File(projectRoot, "build.gradle").exists());
//            if (isProjectRoot) {
//                break;
//            }
//            projectRoot = projectRoot.getParentFile();
//            if (projectRoot == null || projectRoot.getAbsolutePath().length() < 3) {
//                break;
//            }
//        }
//
//        // 拼接Java源码路径
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
//        // 兜底：系统工作目录
//        String userDir = System.getProperty("user.dir");
//        File fallbackSourceRoot = new File(
//                userDir + File.separator + "src" + File.separator + "main" + File.separator + "java" + File.separator + targetPackagePath
//        );
//        return fallbackSourceRoot.exists() ? fallbackSourceRoot : null;
//    }
//
//    /**
//     * 自动获取Resources根路径（src/main/resources）
//     */
//    private static File autoGetResourcesRootDir() throws URISyntaxException {
//        Class<FileMainV3> clazz = FileMainV3.class;
//        String classPath = clazz.getResource("").toURI().getPath();
//        if (File.separatorChar == '\\' && classPath.startsWith("/")) {
//            classPath = classPath.substring(1);
//        }
//        File classDir = new File(classPath);
//
//        // 向上找项目根
//        File projectRoot = classDir;
//        while (projectRoot != null) {
//            boolean isProjectRoot = new File(projectRoot, "src").exists()
//                    && (new File(projectRoot, "pom.xml").exists() || new File(projectRoot, "build.gradle").exists());
//            if (isProjectRoot) {
//                break;
//            }
//            projectRoot = projectRoot.getParentFile();
//            if (projectRoot == null || projectRoot.getAbsolutePath().length() < 3) {
//                break;
//            }
//        }
//
//        // 拼接resources路径
//        if (projectRoot != null) {
//            File resourcesRoot = new File(
//                    projectRoot + File.separator + "src" + File.separator + "main" + File.separator + "resources"
//            );
//            if (resourcesRoot.exists()) {
//                return resourcesRoot;
//            }
//        }
//
//        // 兜底：系统工作目录
//        String userDir = System.getProperty("user.dir");
//        File fallbackResourcesRoot = new File(
//                userDir + File.separator + "src" + File.separator + "main" + File.separator + "resources"
//        );
//        return fallbackResourcesRoot.exists() ? fallbackResourcesRoot : null;
//    }
//
//    /**
//     * 收集并替换Java文件
//     */
//    private static void collectAndReplaceJavaFiles(File sourceRootDir) throws IOException {
//        // 收集Java文件
//        System.out.println("\n🔍 扫描Java文件（排除自身）...");
//        collectJavaFiles(sourceRootDir);
//        if (javaFileList.isEmpty()) {
//            System.out.println("⚠️ 无可用Java文件");
//        } else {
//            // 替换Java文件内容
//            System.out.println("\n📝 替换Java文件内容（" + OLD_PACKAGE + " → " + NEW_PACKAGE + "）...");
//            replaceJavaFileContent();
//        }
//    }
//
//    /**
//     * 收集并替换XML文件（resources/mapper下）
//     */
//    private static void collectAndReplaceXmlFiles(File resourcesRootDir) throws IOException {
//        // 定位mapper目录
//        File mapperDir = new File(resourcesRootDir + File.separator + "mapper");
//        if (!mapperDir.exists()) {
//            System.out.println("\n⚠️ 未找到resources/mapper目录：" + mapperDir.getAbsolutePath());
//            return;
//        }
//
//        // 收集XML文件
//        System.out.println("\n🔍 扫描XML文件（resources/mapper）...");
//        collectXmlFiles(mapperDir);
//        if (xmlFileList.isEmpty()) {
//            System.out.println("⚠️ mapper目录下无可用XML文件");
//        } else {
//            // 替换XML文件内容
//            System.out.println("\n📝 替换XML文件内容（" + OLD_PACKAGE + " → " + NEW_PACKAGE + "）...");
//            replaceXmlFileContent();
//        }
//    }
//
//    /**
//     * 递归收集Java文件（排除自身）
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
//                System.out.println("找到Java文件：" + file.getAbsolutePath());
//            }
//        }
//    }
//
//    /**
//     * 递归收集XML文件（resources/mapper下）
//     */
//    private static void collectXmlFiles(File dir) {
//        File[] files = dir.listFiles();
//        if (files == null) return;
//
//        for (File file : files) {
//            if (file.isDirectory()) {
//                collectXmlFiles(file); // 递归子目录
//            } else if (file.getName().endsWith(".xml")) {
//                xmlFileList.add(file);
//                System.out.println("找到XML文件：" + file.getAbsolutePath());
//            }
//        }
//    }
//
//    /**
//     * 替换Java文件内容中的包名
//     */
//    private static void replaceJavaFileContent() throws IOException {
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
//            System.out.println("✅ Java替换完成：" + file.getAbsolutePath());
//        }
//    }
//
//    /**
//     * 替换XML文件内容中的包名（兼容MyBatis XML格式）
//     */
//    private static void replaceXmlFileContent() throws IOException {
//        for (File file : xmlFileList) {
//            StringBuilder content = new StringBuilder();
//            try (BufferedReader br = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(file), StandardCharsets.UTF_8))) {
//                String line;
//                while ((line = br.readLine()) != null) {
//                    // 替换XML中的包名（比如namespace="com.example.UtioyV1.mapper.UtioMapper"）
//                    content.append(line.replace(OLD_PACKAGE, NEW_PACKAGE)).append(System.lineSeparator());
//                }
//            }
//
//            try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(
//                    new FileOutputStream(file), StandardCharsets.UTF_8))) {
//                bw.write(content.toString().trim());
//                bw.flush();
//            }
//            System.out.println("✅ XML替换完成：" + file.getAbsolutePath());
//        }
//    }
//
//    /**
//     * 打印诊断信息（定位问题用）
//     */
//    private static void printDiagnosticInfo() throws URISyntaxException {
//        System.out.println("\n===== 诊断信息 =====");
//        Class<FileMainV3> clazz = FileMainV3.class;
//        System.out.println("1. 当前类包名：" + clazz.getPackage().getName());
//        System.out.println("2. 自动截取的目标包名：" + NEW_PACKAGE);
//        System.out.println("3. 类编译路径：" + clazz.getResource("").toURI().getPath());
//        System.out.println("4. 系统工作目录：" + System.getProperty("user.dir"));
//        // 拼接的Resources路径
//        File classDir = new File(clazz.getResource("").toURI().getPath());
//        File projectRoot = classDir;
//        while (projectRoot != null && !new File(projectRoot, "src").exists()) {
//            projectRoot = projectRoot.getParentFile();
//        }
//        File resourcesRoot = new File(
//                (projectRoot == null ? System.getProperty("user.dir") : projectRoot)
//                        + File.separator + "src" + File.separator + "main" + File.separator + "resources"
//        );
//        System.out.println("5. 拼接的Resources路径：" + resourcesRoot.getAbsolutePath());
//        System.out.println("6. Resources路径是否存在：" + resourcesRoot.exists());
//        System.out.println("====================");
//    }
//}