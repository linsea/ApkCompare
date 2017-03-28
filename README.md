## 比较两个版本APK中文件差异的工具
比较两个版本APK中文件的大小差异,以便分析APK增大的原因.

### 使用方法
[下载](https://github.com/linsea/ApkCompare/blob/master/release/apkcompare-1.0.jar)jar文件,然后运行:
```
java -jar apkcompare-1.0.jar app-1.0.apk app-2.0.apk v2_v1
```
其中参数`app-1.0.apk`是1.0版本的APK,`app-2.0.apk`是2.0版本的APK,`v2_v1`是输出差异结果的文件名,最终的输出是一个Markdown格式的文件,比如:

-------------------------------------------


| Changed Files | Diff Size (byte) |
| --------- | ---------: |
| **[Added new file](#added)** | 3,395 |
| **[Increased file](#increased)** | 3,469 |
| **[Decreased file](#decreased)** | 3,469 |
| **[Removed file](#removed)** | 3,469 |

## Added New Files (新版本中新增的文件) <a name="added"></a>
| File Name | Size (byte)|
| --------- | ---------: |
| res/drawable/a01.png | 1,626 |
| res/drawable/a04.png | 3,395 |
| res/layout/bcd.xml | 3,469 |

## Size Increased Files (新版本中大小增加的文件) <a name="increased"></a>
| File Name | Increased Size (byte)|
| --------- | ---------: |
| assets/abc.mp3 | 35 |
| assets/d.txt | 7,100 |

## Size Decreased Files (新版本中大小减少的文件) <a name="decreased"></a>
| File Name | Decreased Size (byte)|
| --------- | ---------: |
| AndroidManifest.xml | -390 |
| META-INF/MANIFEST.MF | -115 |
| META-INF/AP_ANDRO.DSA | -1 |
| META-INF/AP_ANDRO.SF | -40 |

## Removed Files (新版本中删除的文件) <a name="removed"></a>
| File Name | Decreased Size (byte)|
| --------- | ---------: |
| res/layout/A.xml  | -22,471 |
| res/drawable/b.png | -32,755 |


-------------------------------------------





## License
    MIT License






