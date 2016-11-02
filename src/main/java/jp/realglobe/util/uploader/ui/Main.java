package jp.realglobe.util.uploader.ui;

import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
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

    private static final String OPTION_DELAY = "delay";
    private static final String OPTION_NOT_LATEST_ONLY = "noSkip";
    private static final String OPTION_TARGET_EXTENSIONS = "ext";
    private static final String OPTION_MIN_SIZE = "min";
    private static final String OPTION_MAX_SIZE = "max";
    private static final String OPTION_NAME = "name";
    private static final String OPTION_FILE_DIRECTORY_PATH = "fileDir";

    private static final String OPTION_ID = "id";
    private static final String OPTION_TOKEN = "token";

    private static final String DEFAULT_DELAY = "100";
    private static final String DEFAULT_NAME = "directory uploader ui";
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
                .addOption(OPTION_DELAY.substring(0, 1), OPTION_DELAY, true, "detection delay to avoid uploading same file")
                .addOption("S" /* OPTION_NOT_LATEST_ONLY.substring(0, 1) */, OPTION_NOT_LATEST_ONLY, false, "upload all files when many files are detected")
                .addOption(OPTION_TARGET_EXTENSIONS.substring(0, 1), OPTION_TARGET_EXTENSIONS, true, "upload file extensions separated by ','")
                .addOption(OPTION_MIN_SIZE.substring(0, 1), OPTION_MIN_SIZE, true, "upload file size lower limit")
                .addOption("M" /* OPTION_MAX_SIZE.substring(0, 1) */, OPTION_MAX_SIZE, true, "upload file size upper limit")
                .addOption(OPTION_NAME.substring(0, 1), OPTION_NAME, true, "display name")
                .addOption(OPTION_FILE_DIRECTORY_PATH.substring(0, 1), OPTION_FILE_DIRECTORY_PATH, true, "file directry path")
                .addOption(OPTION_ID.substring(0, 1), OPTION_ID, true, "(for debug) id")
                .addOption(OPTION_TOKEN.substring(0, 1), OPTION_TOKEN, true, "(for debug) token");
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
        final long delay = Long.parseLong(parameters.getOptionValue(OPTION_DELAY, DEFAULT_DELAY));
        final boolean latestOnly = !parameters.hasOption(OPTION_NOT_LATEST_ONLY);
        final Set<String> targetExtensions = parseExtensions(parameters.getOptionValue(OPTION_TARGET_EXTENSIONS, null));
        final long minSize = Long.parseLong(parameters.getOptionValue(OPTION_MIN_SIZE, "0"));
        final long maxSize = Long.parseLong(parameters.getOptionValue(OPTION_MAX_SIZE, "0"));
        final String name = parameters.getOptionValue(OPTION_NAME, DEFAULT_NAME);
        final Path fileDirectoryPath = Paths.get(parameters.getOptionValue(OPTION_FILE_DIRECTORY_PATH, Paths.get(System.getProperty("user.home"), DEFAULT_FILE_DIRECTORY_NAME).toString()));
        final String id = parameters.getOptionValue(OPTION_ID, null);
        final String token = parameters.getOptionValue(OPTION_TOKEN, null);

        LOG.info("Watch directory: " + watchDirectoryPath + "\n"
                + "Server: " + urlBase + "\n"
                + "User ID: " + userId + "\n"
                + "Delay: " + delay + "\n"
                + "Latest only: " + latestOnly + "\n"
                + "Extensions: " + targetExtensions + "\n"
                + "Min size: " + minSize + "\n"
                + "Max size: " + maxSize + "\n"
                + "File directory: " + fileDirectoryPath + "\n"
                + "ID: " + id + "\n"
                + "Token: " + token);

        final ExecutorService executor = Executors.newCachedThreadPool();
        final CountDownLatch stopper = new CountDownLatch(1);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            executor.shutdownNow();
            try {
                stopper.await();
            } catch (final InterruptedException e) {
                // せっかちさん
            }
        }));
        try (final Gui gui = new TrayGui(name, watchDirectoryPath, new URL(urlBase))) {

            gui.setOnExitPressed(() -> {
                executor.shutdownNow();
            });

            executor.submit((Callable<?>) () -> {
                run(watchDirectoryPath, delay, latestOnly, targetExtensions, minSize, maxSize, urlBase, userId, name, fileDirectoryPath, id, token, gui, executor);
                return null;
            }).get();
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
            stopper.countDown();
        }
        System.out.println("End");
    }

    private static void run(final Path watchDirectoryPath, final long delay, final boolean latestOnly, final Collection<String> targetExtensions, final long minSize, final long maxSize,
            final String urlBase, final String userId, final String name, final Path fileDirectoryPath, final String id, final String token, final Gui gui, final ExecutorService executor) throws Exception {

        final DirectoryUploader uploader;
        if (id != null && token != null) {
            uploader = new DirectoryUploader(watchDirectoryPath, delay, latestOnly, targetExtensions, minSize, maxSize, urlBase, userId, name, id, token);
        } else {
            uploader = new DirectoryUploader(watchDirectoryPath, delay, latestOnly, targetExtensions, minSize, maxSize, urlBase, userId, name, new FileStore(fileDirectoryPath));

            LOG.info("Prepare token");
            gui.setStatusText("トークンを取得中です");

            while (true) {
                try {
                    uploader.prepareToken();
                    break;
                } catch (final InterruptedException e) {
                    throw e;
                } catch (final Exception e) {
                    gui.setStatusText("トークン取得中にエラーが発生しました: " + e);
                    LOG.warning(e.toString());
                    LOG.fine(StackTraces.getString(e));
                }
                Thread.sleep(TRY_INTERVAL);
            }
        }

        final Future<?> task = executor.submit(uploader);

        LOG.info("Started");
        gui.setStatusText("ディレクトリ監視中です");
        task.get();
    }

    /**
     * , 区切りの拡張子を読み取る
     * @param text , 区切りの拡張子
     * @return 拡張子の集合
     */
    private static Set<String> parseExtensions(final String text) {
        if (text == null) {
            return Collections.emptySet();
        }
        final String[] tokens = text.split(",");
        final Set<String> set = new HashSet<>();
        for (final String token : tokens) {
            final String trimmed = token.trim();
            if (!trimmed.isEmpty()) {
                set.add(trimmed);
            }
        }
        return set;
    }

}
