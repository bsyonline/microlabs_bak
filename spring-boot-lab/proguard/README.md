# proguard

1. -keep 表示不混淆
2. spring boot 的启动类不要混淆，影响程序启动
3. mvn package 会自动替换混淆jar
4. proguard_map.txt 是混淆的映射文件