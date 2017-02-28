/*----------------------------------------------------------------------
 * Copyright 2017 realglobe Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *----------------------------------------------------------------------*/

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
