package jp.realglobe.util.uploader.ui;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import jp.realglobe.lib.util.StackTraces;
import jp.realglobe.util.uploader.DirectoryUploader;
import jp.realglobe.util.uploader.FileStore;

/**
 * 開始点
 */
public final class Main {

    private static final Logger LOG = Logger.getLogger(Main.class.getName());

    private static final long TRY_INTERVAL = 10_000L;
    private static final long SHUTDOWN_WAIT = 10_000L;

    private static final String OPTION_WATCH_DIRECTORY_PATH = "watchDir";
    private static final String OPTION_BASE_URL = "server";
    private static final String OPTION_USER_ID = "user";
    private static final String OPTION_NAME = "name";
    private static final String OPTION_FILE_DIRECTORY_PATH = "fileDir";

    private static final String DEFAULT_FILE_DIRECTORY_NAME = ".directory-uploader-ui";

    /**
     * 開始点
     * @param args コマンドライン引数
     * @throws Exception エラー
     */
    public static void main(final String[] args) throws Exception {

        final Option watchDirectoryPathOption = new Option(OPTION_WATCH_DIRECTORY_PATH.substring(0, 1), OPTION_WATCH_DIRECTORY_PATH, true, "watch directory path");
        watchDirectoryPathOption.setRequired(true);
        final Option urlBaseOption = new Option(OPTION_BASE_URL.substring(0, 1), OPTION_BASE_URL, true, "upload server url");
        urlBaseOption.setRequired(true);
        final Option userIdOption = new Option(OPTION_USER_ID.substring(0, 1), OPTION_USER_ID, true, "user ID");
        userIdOption.setRequired(true);

        final Options options = (new Options())
                .addOption(watchDirectoryPathOption)
                .addOption(urlBaseOption)
                .addOption(userIdOption)
                .addOption(OPTION_NAME.substring(0, 1), OPTION_NAME, true, "display name")
                .addOption(OPTION_FILE_DIRECTORY_PATH.substring(0, 1), OPTION_FILE_DIRECTORY_PATH, true, "file directry path");
        final CommandLine parameters;
        try {
            parameters = (new DefaultParser()).parse(options, args);
        } catch (final ParseException e) {
            LOG.severe(e.toString());
            (new HelpFormatter()).printHelp("directory uploader ui", options);
            System.exit(1);
            return;
        }

        final Path watchDirectoryPath = Paths.get(parameters.getOptionValue(OPTION_WATCH_DIRECTORY_PATH));
        final String urlBase = parameters.getOptionValue(OPTION_BASE_URL);
        final String userId = parameters.getOptionValue(OPTION_USER_ID);
        final String name = parameters.getOptionValue(OPTION_NAME);
        final Path fileDirectoryPath = Paths.get(parameters.getOptionValue(OPTION_FILE_DIRECTORY_PATH, Paths.get(System.getProperty("user.home"), DEFAULT_FILE_DIRECTORY_NAME).toString()));

        final ExecutorService executor = Executors.newCachedThreadPool();
        try (final Gui gui = new TrayGui(name, watchDirectoryPath, new URL(urlBase))) {

            gui.setOnExitPressed(() -> {
                executor.shutdownNow();
            });

            final DirectoryUploader uploader = new DirectoryUploader(watchDirectoryPath, urlBase, userId, name, new FileStore(fileDirectoryPath));

            LOG.info("Prepare token");
            gui.setStatusText("トークンを取得中です");

            while (true) {
                try {
                    uploader.prepareToken();
                    break;
                } catch (final Exception e) {
                    gui.setStatusText("トークン取得中にエラーが発生しました: " + e);
                    LOG.warning(e.toString());
                    LOG.fine(StackTraces.getString(e));
                }
                Thread.sleep(TRY_INTERVAL);
            }

            final Future<?> task = executor.submit(uploader);

            LOG.info("Started");
            gui.setStatusText("ディレクトリ監視中です");
            task.get();
        } catch (final InterruptedException e) {
            // 終了
            LOG.info("Shut down");
        } finally {
            executor.shutdownNow();
            try {
                executor.awaitTermination(SHUTDOWN_WAIT, TimeUnit.MILLISECONDS);
            } catch (final InterruptedException e) {
                LOG.warning("Shutdown is forced");
            }
        }
    }

}
