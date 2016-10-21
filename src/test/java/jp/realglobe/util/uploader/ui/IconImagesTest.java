package jp.realglobe.util.uploader.ui;

import org.junit.Assert;
import org.junit.Test;

/**
 * アイコン画像周りのテスト
 */
public class IconImagesTest {

    /**
     * エラー落ちしないことの検査
     */
    @Test
    public void testGet() {
        Assert.assertNotNull(IconImages.get());
    }

}
