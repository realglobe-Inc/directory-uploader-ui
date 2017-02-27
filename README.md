# directory-uploader-ui

directory-uploader のシステムトレイ UI。


## 実行可能 Jar の作成

依存ライブラリを取得するために Maven の設定ファイル（~/.m2/settings.xml 等）に以下のような記述を加える。

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<settings xsi:schemaLocation='http://maven.apache.org/SETTINGS/1.0.0 http://maven.apache.org/xsd/settings-1.0.0.xsd'
          xmlns='http://maven.apache.org/SETTINGS/1.0.0' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>
  <profiles>
    <profile>
      <repositories>
        <repository>
          <snapshots>
            <enabled>false</enabled>
          </snapshots>
          <id>jcenter</id>
          <name>jcenter</name>
          <url>https://jcenter.bintray.com/</url>
        </repository>
      </repositories>
      <id>jcenter</id>
    </profile>
  </profiles>
  <activeProfiles>
    <activeProfile>jcenter</activeProfile>
  </activeProfiles>
</settings>
```

で、

```
$ mvn clean compile assembly:single
```


## ライセンス

Apache License, Version 2.0
