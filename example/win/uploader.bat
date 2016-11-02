rem アップロード先サーバー
if "%SERVER%"=="" set SERVER=http://13.78.122.199/rest
rem サーバーに名乗る ID
if "%ID%"=="" set ID=b03c5702-1cf1-48fd-b00b-bf8ec0577c2a
rem サーバーの認証トークン
if "%TOKEN%"=="" set TOKEN=a2fc62e5-4a5e-4aa9-a267-87d3c75a55ae
rem サーバーで紐付くユーザー
if "%USER%"=="" set USER=realglobe

rem 監視するフォルダ
if "%DIR%"=="" set DIR=image
rem アップロードするファイルの種類
if "%EXTENSION%"=="" set EXTENSION=jpg,jpeg,png,bmp,gif,tiff
rem アップロードするファイルの最小サイズ
if "%MIN%"=="" set MIN=1024
rem アップロードするファイルの最大サイズ
if "%MAX%"=="" set MAX=8388608

if "%JAVA%"=="" set JAVA=java
if "%JAR%"=="" set JAR=directory-uploader-ui-1.3.0-jar-with-dependencies.jar


%JAVA% -jar %JAR% ^
       --server %SERVER% ^
       --id %ID% ^
       --token %TOKEN% ^
       --user %USER% ^
       --watchDir %DIR% ^
       --ext %EXTENSION% ^
       --min %MIN% ^
       --max %MAX%
