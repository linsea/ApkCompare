package com.github.linsea.apk.compare;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class Main {

    static final String NA = "N/A";
    static final NumberFormat format = NumberFormat.getInstance();
    static final Comparator comparator = new Comparator<DiffItem>() {
        @Override
        public int compare(DiffItem o1, DiffItem o2) {
            return (int) (Math.abs(o2.diffSize) - Math.abs(o1.diffSize));
        }
    };

    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: ApkCompare oldApk newApk outputFilename");
            System.out.println("Example: ApkCompare App-1.0.apk App-2.0.apk changes");
            System.out.println("This would output a diff file named changes.md at current directory");
            return;
        }

        Map<String, Long> oldFilesInfo = getFilesInfo(args[0]);
        Map<String, Long> newFilesInfo = getFilesInfo(args[1]);
        List<DiffItem> outputDiffList = new ArrayList<DiffItem>();
        Set<Map.Entry<String, Long>> oldEntries = oldFilesInfo.entrySet();

        for (Map.Entry<String, Long> oldEntry : oldEntries) {
            DiffItem diffItem = new DiffItem();
            String keyOldFilename = oldEntry.getKey();
            diffItem.oldFilename = keyOldFilename;
            Long oldFileSize = oldEntry.getValue();
            Long newFileSize = newFilesInfo.get(keyOldFilename);
            if (newFileSize == null) { //新APK中删除了的文件
                diffItem.newFilename = NA;
                diffItem.diffSize = -oldFileSize;
            } else {
                diffItem.newFilename = diffItem.oldFilename;
                diffItem.diffSize = newFileSize - oldFileSize;
            }
            newFilesInfo.remove(keyOldFilename);
            if (diffItem.diffSize != 0L) { //仅统计文件大小有改变的情况
                outputDiffList.add(diffItem);
            }
        }

        //新版本中新增的文件
        Set<Map.Entry<String, Long>> newEntries = newFilesInfo.entrySet();
        for (Map.Entry<String, Long> newEntry : newEntries) {
            DiffItem diffItem = new DiffItem();
            diffItem.oldFilename = NA;
            diffItem.newFilename = newEntry.getKey();
            diffItem.diffSize = newEntry.getValue();
            outputDiffList.add(diffItem);
        }

        outputMarkdown(args, outputDiffList);

        System.out.println("APK compare done!");
        System.out.println("output file: " + new File(args[2]).getAbsolutePath() + ".md");
    }


    private static void outputMarkdown(String[] filenames, List<DiffItem> outputDiffList) {
        try {
            List<DiffItem> increased = new ArrayList<DiffItem>();
            List<DiffItem> decreased = new ArrayList<DiffItem>();
            List<DiffItem> added = new ArrayList<DiffItem>();
            List<DiffItem> removed = new ArrayList<DiffItem>();

            int increasedCnt = 0;
            int decreasedCnt = 0;
            int addedCnt = 0;
            int removedCnt = 0;

            for (DiffItem diffItem : outputDiffList) {
                if (NA.equals(diffItem.oldFilename)) { //新版本完全新增文件
                    added.add(diffItem);
                    addedCnt += diffItem.diffSize;
                } else if (NA.equals(diffItem.newFilename)) { //新版本删除了的文件
                    removed.add(diffItem);
                    removedCnt += diffItem.diffSize;
                } else if (diffItem.diffSize > 0) { //同一文件有差异,新版本中size增加了
                    increased.add(diffItem);
                    increasedCnt += diffItem.diffSize;
                } else {//同一文件有差异,新版本中size减小了
                    decreased.add(diffItem);
                    decreasedCnt += diffItem.diffSize;
                }
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(filenames[2] + ".md"));
            File oldApkFile = new File(filenames[0]);
            writer.write(oldApkFile.getName());
            writer.write(" VS ");
            File newApkFile = new File(filenames[1]);
            writer.write(newApkFile.getName());
            writer.write("\n\n");

            writer.write("### Diff Compressed Size : ");
            writer.write(format.format(newApkFile.length() - oldApkFile.length()));
            writer.write(" bytes\n");

            writer.write("### Diff Raw Size : ");
            writer.write(format.format(addedCnt + increasedCnt + decreasedCnt + removedCnt));
            writer.write(" bytes\n\n");

            writer.write("| Changed Files | Diff Raw Size (byte) |\n");
            writer.write("| --------- | ---------: |\n");
            writer.append("| **[Added new Files](#added)** | ").append(format.format(addedCnt)).append(" | \n");
            writer.append("| **[Increased Files](#increased)** | ").append(format.format(increasedCnt)).append(" | \n");
            writer.append("| **[Decreased Files](#decreased)** | ").append(format.format(decreasedCnt)).append(" | \n");
            writer.append("| **[Removed Files](#removed)** | ").append(format.format(removedCnt)).append(" | \n\n");

            outputMarkdownAddedList(writer, added);
            outputMarkdownIncreasedList(writer, increased);
            outputMarkdownDecreasedList(writer, decreased);
            outputMarkdownRemovedList(writer, removed);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void outputMarkdownAddedList(Writer writer, List<DiffItem> outputDiffList) {
        if (outputDiffList.size() > 0) {
            try {
                Collections.sort(outputDiffList, comparator);
                writer.append("## ").append("Added New Files (新版本中新增的文件)");
                writer.append("<a name=\"added\"></a>\n");
                writer.append("| File Name | Size (byte)|\n");
                writer.append("| --------- | ---------: |\n");
                for (DiffItem diffItem : outputDiffList) {
                    writer.append("| ").append(diffItem.newFilename).append(" | ")
                            .append(format.format(diffItem.diffSize))
                            .append(" |\n");
                    writer.flush();
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void outputMarkdownIncreasedList(Writer writer, List<DiffItem> outputDiffList) {
        if (outputDiffList.size() > 0) {
            try {
                Collections.sort(outputDiffList, comparator);
                writer.append("## ").append("Size Increased Files (新版本中大小增加的文件)");
                writer.append("<a name=\"increased\"></a>\n");
                writer.append("| File Name | Increased Size (byte)|\n");
                writer.append("| --------- | ---------: |\n");
                for (DiffItem diffItem : outputDiffList) {
                    writer.append("| ").append(diffItem.newFilename).append(" | ")
                            .append(format.format(diffItem.diffSize))
                            .append(" |\n");
                    writer.flush();
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void outputMarkdownDecreasedList(Writer writer, List<DiffItem> outputDiffList) {
        if (outputDiffList.size() > 0) {
            try {
                Collections.sort(outputDiffList, comparator);
                writer.append("## ").append("Size Decreased Files (新版本中大小减少的文件)");
                writer.append("<a name=\"decreased\"></a>\n");
                writer.append("| File Name | Decreased Size (byte)|\n");
                writer.append("| --------- | ---------: |\n");
                for (DiffItem diffItem : outputDiffList) {
                    writer.append("| ").append(diffItem.newFilename).append(" | ")
                            .append(format.format(diffItem.diffSize))
                            .append(" |\n");
                    writer.flush();
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static void outputMarkdownRemovedList(Writer writer, List<DiffItem> outputDiffList) {
        if (outputDiffList.size() > 0) {
            try {
                Collections.sort(outputDiffList, comparator);
                writer.append("## ").append("Removed Files (新版本中删除的文件)");
                writer.append("<a name=\"removed\"></a>\n");
                writer.append("| File Name | Decreased Size (byte)|\n");
                writer.append("| --------- | ---------: |\n");
                for (DiffItem diffItem : outputDiffList) {
                    writer.append("| ").append(diffItem.oldFilename).append(" | ")
                            .append(format.format(diffItem.diffSize))
                            .append(" |\n");
                    writer.flush();
                }
                writer.write("\n");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static Map<String, Long> getFilesInfo(String apkFilePath) {
        Map<String, Long> map = new LinkedHashMap<String, Long>();
        try {
            ZipFile apkFile = new ZipFile(apkFilePath);
            Enumeration<? extends ZipEntry> entries = apkFile.entries();
            while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                String filename = entry.getName();
                long size = entry.getSize();
                map.put(filename, size);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    private static class DiffItem {
        String oldFilename;
        String newFilename;
        Long diffSize;
    }

}


