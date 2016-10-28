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
