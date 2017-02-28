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

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

/**
 * GUI 用アイコン
 */
final class IconImages {

    /**
     * アイコン画像を返す
     * @return アイコン画像
     * @throws IOException 入出力エラー
     */
    static Image get() throws IOException {
        try (InputStream input = IconImages.class.getResource("/icon.png").openStream()) {
            return ImageIO.read(input);
        }
    }

}
