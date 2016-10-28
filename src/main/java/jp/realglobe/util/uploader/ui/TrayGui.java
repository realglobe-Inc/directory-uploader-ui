package jp.realglobe.util.uploader.ui;

import java.awt.AWTException;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

/**
 * システムトレイ UI
 */
final class TrayGui implements Gui {

    private static final Logger LOG = Logger.getLogger(TrayGui.class.getName());

    private final TrayIcon icon;

    private final AtomicReference<Runnable> exitCallback;

    private final String name;
    private final Path watchDirectryPath;
    private final URL uploadUrl;

    private String status;

    TrayGui(final String name, final Path watchDirectoryPath, final URL uploadUrl) throws AWTException, IOException {
        if (!SystemTray.isSupported()) {
            throw new UnsupportedOperationException("System tray is not supported");
        }

        this.exitCallback = new AtomicReference<>();

        this.name = name;
        this.watchDirectryPath = watchDirectoryPath;
        this.uploadUrl = uploadUrl;

        // 右クリックメニュー
        final JPopupMenu popup = new JPopupMenu();

        this.icon = new TrayIcon(IconImages.get(), this.name, null);
        this.icon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(final MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON3) {
                    // 右クリックでメニュー表示
                    LOG.info("Caught right click");
                    popup.setLocation(e.getX(), e.getY());
                    popup.setInvoker(popup);
                    popup.setVisible(true);
                } else {
                    // クリックで状態表示
                    LOG.info("Caught click");
                    TrayGui.this.icon.displayMessage("状態", getDisplayText(), TrayIcon.MessageType.NONE);
                }
            }
        });

        // メニューの「メニューを閉じる」項目
        final JMenuItem closeItem = new JMenuItem("メニューを閉じる");
        closeItem.addActionListener(e -> {
            popup.setVisible(false);
        });
        popup.add(closeItem);

        // メニューの「終了」項目
        final JMenuItem exitItem = new JMenuItem("終了");
        exitItem.addActionListener(e -> {
            LOG.info("Exit button was pressed");
            final Runnable callback = this.exitCallback.get();
            if (callback != null) {
                callback.run();
            }
        });
        popup.add(exitItem);

        SystemTray.getSystemTray().add(this.icon);
    }

    private synchronized String getDisplayText() {
        return this.status + System.lineSeparator() +
                "表示名:" + this.name + System.lineSeparator() +
                "監視場所:" + this.watchDirectryPath + System.lineSeparator() +
                "アップロード先" + this.uploadUrl;
    }

    @Override
    public void close() {
        SystemTray.getSystemTray().remove(this.icon);
    }

    @Override
    public void setOnExitPressed(final Runnable callback) {
        this.exitCallback.set(callback);
    }

    @Override
    public void setStatusText(final String text) {
        this.status = text;
    }

}
