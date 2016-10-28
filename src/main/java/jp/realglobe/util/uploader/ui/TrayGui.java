package jp.realglobe.util.uploader.ui;

import java.awt.AWTException;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

/**
 * システムトレイ UI
 */
final class TrayGui implements Gui {

    private static final Logger LOG = Logger.getLogger(TrayGui.class.getName());

    private final TrayIcon icon;
    private final JDialog exitDialog;

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

        // メニューの「終了」項目
        this.exitDialog = makeExitDialog(this.exitCallback);
        final JMenuItem exitItem = new JMenuItem("終了");
        exitItem.addActionListener(e -> this.exitDialog.setVisible(true));
        popup.add(exitItem);

        SystemTray.getSystemTray().add(this.icon);
    }

    /**
     * 終了確認ダイアログをつくる
     * @param callback 終了ボタンが押されたときに実行される関数
     * @return 終了確認ダイアログ
     */
    private static JDialog makeExitDialog(final AtomicReference<Runnable> callback) {
        final JDialog dialog = new JDialog((JFrame) null, "確認");
        dialog.setLayout(new GridLayout(0, 1));
        dialog.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(final WindowEvent e) {
                // 閉じたら消える
                dialog.setVisible(false);
            }
        });

        final JLabel message = new JLabel("本当に終了させますか？");
        final JPanel messagePanel = new JPanel();
        messagePanel.setLayout(new FlowLayout());
        messagePanel.add(message);
        dialog.add(messagePanel);

        final JButton button = new JButton("はい");
        button.addActionListener(e -> {
            LOG.info("Exit button was pressed");
            dialog.setVisible(false);
            final Runnable callback0 = callback.get();
            if (callback0 != null) {
                callback0.run();
            }
        });
        final JPanel buttonPanel = new JPanel();
        buttonPanel.add(button);
        dialog.add(buttonPanel);

        dialog.setMinimumSize(dialog.getPreferredSize());
        return dialog;
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
        this.exitDialog.dispose();
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
