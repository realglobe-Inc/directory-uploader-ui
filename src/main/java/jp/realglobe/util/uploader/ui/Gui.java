package jp.realglobe.util.uploader.ui;

import java.io.Closeable;

/**
 * UI
 */
interface Gui extends Closeable {

    @Override
    void close();

    /**
     * 終了ボタンを押されたときの動作を登録する
     * @param callback 終了ボタンを押されたときに実行する関数
     */
    void setOnExitPressed(Runnable callback);

    /**
     * 現状を表す文字列を設定する
     * @param text 現状を表す文字列
     */
    void setStatusText(String text);

}
