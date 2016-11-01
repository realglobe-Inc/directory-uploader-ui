rem アップロード先サーバー
set SERVER=http://localhost:3000/rest
rem サーバーにおけるユーザー
set USER=demo

rem 監視するフォルダ
set DIR=image
rem アップロードするファイルの種類
set EXTENSION=jpg,jpeg,png,bmp,gif,tiff
rem アップロードするファイルの最小サイズ
set MIN=1024
rem アップロードするファイルの最大サイズ
set MAX=8388608

set JAVA=java
set JAR=directory-uploader-ui-1.2.0-jar-with-dependencies.jar


%JAVA% -jar %JAR% ^
       -s %SERVER% ^
       -u %USER% ^
       -w %DIR% ^
       -ext %EXTENSION% ^
       -min %MIN% ^
       -max %MAX%
