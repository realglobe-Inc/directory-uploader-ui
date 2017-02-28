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

import org.junit.Assert;
import org.junit.Test;

/**
 * アイコン画像周りのテスト
 */
public class IconImagesTest {

    /**
     * エラー落ちしないことの検査
     * @throws Exception エラー
     */
    @Test
    public void testGet() throws Exception {
        Assert.assertNotNull(IconImages.get());
    }

}
