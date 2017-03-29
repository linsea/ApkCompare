## A tool for 2 version APK files Comparision
A tool for Comparision 2 version APK files to find out files size changes.

### Usage
[Download jar](https://github.com/linsea/ApkCompare/raw/master/release/apkcompare-1.0.jar) , then run:
```
java -jar apkcompare-1.0.jar app-1.0.apk app-2.0.apk v2_v1
```
argument `app-1.0.apk` is v1.0 APK,`app-2.0.apk` is v2.0 APK,`v2_v1` is output file name, which is a Markdown file(the size column is sorted), e.g.

-------------------------------------------


| Changed Files | Diff Size (byte) |
| --------- | ---------: |
| **[Added new file](#added)** | 3,395 |
| **[Increased file](#increased)** | 3,169 |
| **[Decreased file](#decreased)** | 3,069 |
| **[Removed file](#removed)** | 1,469 |

## Added New Files (新版本中新增的文件) <a name="added"></a>
| File Name | Size (byte)|
| --------- | ---------: |
| res/drawable/a01.png | 4,626 |
| res/drawable/a04.png | 3,395 |
| res/layout/bcd.xml | 1,469 |

## Size Increased Files (新版本中大小增加的文件) <a name="increased"></a>
| File Name | Increased Size (byte)|
| --------- | ---------: |
| assets/abc.mp3 | 12,335 |
| assets/d.txt | 7,100 |

## Size Decreased Files (新版本中大小减少的文件) <a name="decreased"></a>
| File Name | Decreased Size (byte)|
| --------- | ---------: |
| AndroidManifest.xml | -390 |
| META-INF/MANIFEST.MF | -115 |
| META-INF/AP_ANDRO.DSA | -110 |
| META-INF/AP_ANDRO.SF | -40 |

## Removed Files (新版本中删除的文件) <a name="removed"></a>
| File Name | Decreased Size (byte)|
| --------- | ---------: |
| res/layout/A.xml  | -42,471 |
| res/drawable/b.png | -32,755 |


-------------------------------------------





## License
    MIT License






