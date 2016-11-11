# 使い方

Blackmagic Design 社製キャプチャデバイス（Intensity Shuttle）で DJI Phantom の出力映像から画像を取得する場合。

1. [キャプチャデバイスのドライバインストール](#driver)
2. [Java のインストール](#java)
3. [画像取得部分の動作確認](#capture)
4. [画像アップロード部分の動作確認](#uploader)
5. [終了方法](#shutdown)
6. [画像が更新されないときは](#trouble)


## <div id="driver">キャプチャデバイスのドライバインストール</div>

「Desktop Video Installer v10.8.2.msi」を実行し、インストールを行います。


## <div id="java">Java のインストール</div>

「jre-8u112-windows-x64.exe」を実行し、インストールを行います。


## <div id="capture">画像取得部分の動作確認</div>

Intensity Shuttle を PC に接続し、Phantom の HDMI 出力を Intensity Shuttle に接続します。
「capture-decklink_中画質_表示無し_phantom50fps.bat」を実行します。
「image」フォルダを開き、10 秒ごとに画像が保存されていくことを確認します。
画像がカラーバーになる場合は、一旦[終了](#shutdown)させてから「capture-decklink_中画質_表示無し_phantom60fps.bat」を実行して確認します。


## <div id="uploader">画像アップロード部分の動作確認</div>

インターネットに接続し、画像を取得しながら、「uploader_リアルグローブ5号.bat」を実行し、[表示サイト](http://http://13.78.122.199/)で画像が更新されることを確認します。


## <div id="shutdown">終了方法</div>

映像やコマンドプロンプトのウィンドウを閉じることで終了します。


## <div id="trouble">画像が更新されないときは</div>

まず、画像取得部分が動作しているか[確認](#capture)します。
動作していない場合、スタートメニューを右クリックし、「デバイスマネージャ」を開きます。
デバイスマネージャで Intensity Shuttle に「？」が付加されている場合、PC を再起動します。
それでも直らない場合、HDMI から映像を出力しているか確認します。

画像取得部分が動作している場合、インターネットに接続しているか確認します。
接続している場合、画像アップロード部分を再起動します。
