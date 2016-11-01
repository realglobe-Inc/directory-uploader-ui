rem キャプチャデバイス
set DEVICE="eMPIA HDMI Grabber"
rem 入力のフレームレート
set /A FPS=30
rem 入力の横縦比率
set ASPECT=16:9

rem 何秒ごとに画像を保存するか
set INTERVAL=10
rem 出力サイズ
rem set OUTPUT_SIZE=1280x720
set OUTPUT_SIZE=1920x1080

rem 画像を保存するフォルダ
set DIR=image
rem 画像ファイル名の接頭辞
set PREFIX=""
set FORMAT=jpg

set VLC="VLCPortable\VLCPortable.exe"


for /F "delims=x tokens=1-2" %%1 in ("%OUTPUT_SIZE%") do (
    set WIDTH=%%1
    set HEIGHT=%%2
)
set /A RATIO=%FPS%*%INTERVAL%

%VLC% --video-filter=scene ^
      --scene-format=%FORMAT% ^
      --scene-width=%WIDTH% ^
      --scene-height=%HEIGHT% ^
      --scene-prefix=%PREFIX% ^
      --scene-path=%DIR% ^
      --scene-ratio=%RATIO% ^
      dshow:// ^
      :dshow-vdev=%DEVICE% ^
      :dshow-size=%INPUT_SIZE% ^
      :live-caching=300 ^
      --aspect-ratio=%ASPECT%
