#启动

---
## 1.启动命令
````
java [0]|[1] [GBK] [.txt] -jar [jarName.jar] >log.file 2>&1 &
````
### (1) 参数解释
  >* **[0] |[1]** 可选,默认1,main启动无页面,0 springboot启动,页面模式. 
  --> java 0 -jar client.jar >log.file 2>&1 &
  >* **[GBK]** 可选,默认file.encoding系统编码,配置读取本地文件的编码格式.
  --> java GBK -jar client.jar >log.file 2>&1 &
  >* **[.txt]** 可选,默认读取没有文件类型的文件,配置读取Infile文件夹目录下的文件格式和写入OutFile的文件格式.
  --> java  .txt -jar client.jar >log.file 2>&1 &
## 2.现有功能
- [x] 监听InFile文件变化
- [x] 自定义配置(目前只支持页面和非页面启动,编码格式, 文件类型)
- [x] 监听事件:CREATE ,MODIFY (DELETE事件不触发操作)


