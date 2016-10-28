rem キャプチャデバイス
set DEVICE="eMPIA HDMI Grabber"
set SIZE=1920x1080

rem 何フレームごとに画像を保存するか
rem 30fps の映像で 10 秒おきに画像を保存するなら 300 にする
set RATIO=300

rem 画像を保存するフォルダ
set DIR=image
rem 画像ファイル名の接頭辞
set PREFIX=""
set FORMAT=jpg

set VLC="VLCPortable\VLCPortable.exe"


%VLC% --video-filter=scene ^
      --scene-format=%FORMAT% ^
      --scene-prefix=%PREFIX% ^
      --scene-path=%DIR% ^
      --scene-ratio=%RATIO% ^
      dshow:// ^
      :dshow-vdev=%DEVICE% ^
      :dshow-size=%SIZE% ^
      :live-caching=300
