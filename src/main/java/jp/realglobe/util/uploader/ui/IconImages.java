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
