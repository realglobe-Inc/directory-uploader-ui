rem キャプチャデバイス
set DEVICE="eMPIA HDMI Grabber"
set SIZE=1920x1080

rem 1 秒に何枚の画像を保存するか
rem 10 秒に 1 枚の画像を保存するなら 1/10 と指定する
set FPS=1/10

rem 画像を保存するフォルダ
set DIR=image
rem 画像ファイル名の接頭辞
set PREFIX=""
set FORMAT=jpg

set FFMPEG="ffmpeg-3.1.5-win64-static\bin\ffmpeg.exe"


%FFMPEG% -rtbufsize 128MB ^
         -f dshow ^
         -video_size %SIZE% ^
         -i video=%DEVICE% ^
         -vf fps=%FPS% ^
         %DIR%\\%PREFIX%%%05d.%FORMAT%
