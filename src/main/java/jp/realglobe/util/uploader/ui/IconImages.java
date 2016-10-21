package jp.realglobe.util.uploader.ui;

import java.awt.Image;
import java.awt.image.BufferedImage;

/**
 * GUI 用アイコン
 */
final class IconImages {

    /**
     * アイコン画像を返す
     * @return アイコン画像
     */
    static Image get() {
        final BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        // TODO 真っ白
        return image;
    }

}
