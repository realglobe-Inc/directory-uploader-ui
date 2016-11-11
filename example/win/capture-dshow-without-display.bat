rem キャプチャデバイス
rem 列挙する方法は ffmpeg.exe -list_devices true -f dshow -i dummy
if "%DEVICE%"=="" set DEVICE="eMPIA HDMI Grabber"

rem 何秒ごとに画像を保存するか
if "%INTERVAL%"=="" set INTERVAL=10
rem 出力サイズ
if "%OUTPUT_SIZE%"=="" set OUTPUT_SIZE=1920x1080

rem 画像を保存するフォルダ
if "%DIR%"=="" set DIR=image
rem 画像ファイル名の接頭辞
if "%PREFIX%"=="" set PREFIX=""
if "%FORMAT%"=="" set FORMAT=jpg

if "%FFMPEG%"=="" set FFMPEG="ffmpeg-3.2-win64-static\bin\ffmpeg.exe"


call backup.bat

%FFMPEG% -rtbufsize 128MB ^
         -f dshow ^
         -i video=%DEVICE% ^
         -vf fps=1/%INTERVAL%,scale=%OUTPUT_SIZE% ^
         %DIR%\\%PREFIX%%%05d.%FORMAT%
