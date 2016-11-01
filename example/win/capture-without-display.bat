rem キャプチャデバイス
set DEVICE="eMPIA HDMI Grabber"

rem 何秒ごとに画像を保存するか
set INTERVAL=10
rem 出力サイズ
if "%OUTPUT_SIZE%"=="" set OUTPUT_SIZE=1920x1080

rem 画像を保存するフォルダ
set DIR=image
rem 画像ファイル名の接頭辞
set PREFIX=""
set FORMAT=jpg

set FFMPEG="ffmpeg-3.1.5-win64-static\bin\ffmpeg.exe"


%FFMPEG% -rtbufsize 128MB ^
         -f dshow ^
         -i video=%DEVICE% ^
         -vf fps=1/%INTERVAL%,scale=%OUTPUT_SIZE% ^
         %DIR%\\%PREFIX%%%05d.%FORMAT%
