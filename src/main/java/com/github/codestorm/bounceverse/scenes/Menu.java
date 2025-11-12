/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 *
 * Modified by Mai Thành (@thnhmai06), 2025.
 */
package com.github.codestorm.bounceverse.scenes;

// ... (Các import giữ nguyên)
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLMenu;
import com.almasb.fxgl.app.scene.MenuType;
import com.almasb.fxgl.core.math.FXGLMath;
import com.almasb.fxgl.core.util.InputPredicates;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.input.Input;
import com.almasb.fxgl.input.InputModifier;
import com.almasb.fxgl.input.Trigger;
import com.almasb.fxgl.input.UserAction;
import com.almasb.fxgl.input.view.TriggerView;
import com.almasb.fxgl.localization.Language;
import com.almasb.fxgl.logging.Logger;
import com.almasb.fxgl.particle.ParticleEmitters;
import com.almasb.fxgl.particle.ParticleSystem;
import com.almasb.fxgl.profile.SaveFile;
import com.almasb.fxgl.scene.SubScene;
import com.almasb.fxgl.ui.FXGLScrollPane;
import com.almasb.fxgl.ui.FontType;
import com.github.codestorm.bounceverse.systems.manager.metrics.LeaderboardManager;

import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import javafx.util.StringConverter;

public class Menu extends FXGLMenu {

    // ... (Các biến thành viên giữ nguyên)
    private static final Logger log = Logger.get(Menu.class);

    private final ParticleSystem particleSystem = new ParticleSystem();

    private final SimpleObjectProperty<Color> titleColor = new SimpleObjectProperty<>(Color.WHITE);
    private final Pane menuRoot = new Pane();
    private final Pane menuContentRoot = new Pane();
    private final MenuContent defaultContent = new MenuContent();
    private final PressAnyKeyState pressAnyKeyState = new PressAnyKeyState();
    private final MenuBox menu;
    private final List<Animation<?>> animations = new ArrayList<>();
    private double t = 0.0;

    public Menu(MenuType type) {
        super(type);

        if (getAppWidth() < 800 || getAppHeight() < 600) {
            log.warning("Menu is not designed for resolutions < 800x600");
        }

        if (type == MenuType.MAIN_MENU) {
            menu = createMenuBodyMainMenu();
        } else {
            menu = createMenuBodyGameMenu();
        }

        var menuX = 50.0;
        var menuY = getAppHeight() / 2.0 - menu.getLayoutHeight() / 2.0;

        menuRoot.setTranslateX(menuX);
        menuRoot.setTranslateY(menuY);

        menuContentRoot.setTranslateX(getAppWidth() - 500.0);
        menuContentRoot.setTranslateY(menuY);

        initParticles();

        menuRoot.getChildren().add(menu);
        menuContentRoot.getChildren().add(defaultContent);

        getContentRoot()
                .getChildren()
                .addAll(
                        createBackground(getAppWidth(), getAppHeight()),
                        createTitleView(FXGL.getSettings().getTitle()),
                        createVersionView(makeVersionString()),
                        particleSystem.getPane(),
                        menuRoot,
                        menuContentRoot);
    }

    // ... (Các phương thức khác giữ nguyên cho đến createMenuBodyMainMenu)
    private void initParticles() {
        // particle smoke
        var t = FXGL.texture("particles/smoke.png", 128.0, 128.0).brighter().brighter();

        var emitter = ParticleEmitters.newFireEmitter();
        emitter.setBlendMode(BlendMode.SRC_OVER);
        emitter.setSourceImage(t.getImage());
        emitter.setSize(150.0, 220.0);
        emitter.setNumParticles(10);
        emitter.setEmissionRate(0.01);
        emitter.setVelocityFunction(
                (v) -> new Point2D(FXGL.random() * 2.5, -FXGL.random() * FXGL.random(80, 120)));
        emitter.setExpireFunction((v) -> Duration.seconds(FXGL.random(4, 7)));
        emitter.setScaleFunction((v) -> new Point2D(0.15, 0.10));
        emitter.setSpawnPointFunction(
                (v) -> new Point2D(FXGL.random(0.0, getAppWidth() - 200.0), 120.0));

        particleSystem.addParticleEmitter(emitter, 0.0, FXGL.getAppHeight());
    }

    @Override
    public void onCreate() {
        animations.clear();

        var menuBox = (MenuBox) menuRoot.getChildren().getFirst();

        for (var i = 0; i < menuBox.getChildren().size(); i++) {
            var node = menuBox.getChildren().get(i);
            node.setTranslateX(-250.0);

            var animation
                    = FXGL.animationBuilder()
                            .delay(Duration.seconds(i * 0.07))
                            .interpolator(Interpolators.EXPONENTIAL.EASE_OUT())
                            .duration(Duration.seconds(0.66))
                            .translate(node)
                            .from(new Point2D(-250.0, 0.0))
                            .to(new Point2D(0.0, 0.0))
                            .build();

            animations.add(animation);

            animation.stop();
            animation.start();
        }
    }

    @Override
    public void onDestroy() {
        // the scene is no longer active so reset everything
        // so that next time scene is active everything is loaded properly
        switchMenuTo(menu);
        switchMenuContentTo(defaultContent);
    }

    @Override
    public void onUpdate(double tpf) {
        if (getType() == MenuType.MAIN_MENU
                && FXGL.getSettings().isUserProfileEnabled()
                && "DEFAULT".equals(FXGL.getSettings().getProfileName().get())) {
            showProfileDialog();
        }

        animations.forEach((a) -> a.onUpdate(tpf));

        var frequency = 1.7;

        t += tpf * frequency;

        particleSystem.onUpdate(tpf);

        var color = Color.color(1.0, 1.0, 1.0, FXGLMath.noise1D(t));
        titleColor.set(color);
    }

    private Rectangle createBackground(double width, double height) {
        var bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, getType() == MenuType.GAME_MENU ? 0.5 : 1.0));
        return bg;
    }

    private StackPane createTitleView(String title) {
        var text = FXGL.getUIFactoryService().newText(title.substring(0, 1), 50.0);
        text.setFill(null);
        text.strokeProperty().bind(titleColor);
        text.setStrokeWidth(1.5);

        var text2 = FXGL.getUIFactoryService().newText(title.substring(1), 50.0);
        text2.setFill(null);
        text2.setStroke(titleColor.get());
        text2.setStrokeWidth(1.5);

        var textWidth = text.getLayoutBounds().getWidth() + text2.getLayoutBounds().getWidth();

        var border = new Rectangle(textWidth + 30, 65.0, null);
        border.setStroke(Color.WHITE);
        border.setStrokeWidth(4.0);
        border.setArcWidth(25.0);
        border.setArcHeight(25.0);

        var emitter = ParticleEmitters.newExplosionEmitter(50);
        emitter.setBlendMode(BlendMode.ADD);
        emitter.setSourceImage(FXGL.image("particles/trace_horizontal.png", 64.0, 64.0));
        emitter.setMaxEmissions(Integer.MAX_VALUE);
        emitter.setSize(18.0, 22.0);
        emitter.setNumParticles(2);
        emitter.setEmissionRate(0.2);
        emitter.setVelocityFunction(
                i -> {
                    if (i % 2 == 0) {
                        return new Point2D(FXGL.random(-10.0, 0.0), 0.0);
                    } else {
                        return new Point2D(FXGL.random(0.0, 10.0), 0.0);
                    }
                });
        emitter.setExpireFunction((v) -> Duration.seconds(FXGL.random(4.0, 6.0)));
        emitter.setScaleFunction((v) -> new Point2D(-0.03, -0.03));
        emitter.setSpawnPointFunction((v) -> new Point2D(0, 0));

        var box = new HBox(text, text2);
        box.setAlignment(Pos.CENTER);

        var titleRoot = new StackPane(border, box);
        titleRoot.setTranslateX(getAppWidth() / 2.0 - (textWidth + 30) / 2.0);
        titleRoot.setTranslateY(50.0);

        if (!FXGL.getSettings().isNative()) {
            particleSystem.addParticleEmitter(
                    emitter,
                    getAppWidth() / 2.0 - 30,
                    titleRoot.getTranslateY() + border.getHeight() - 16);
        }

        return titleRoot;
    }

    private Text createVersionView(String version) {
        var view = FXGL.getUIFactoryService().newText(version);
        view.setTranslateY(getAppHeight() - 2.0);
        return view;
    }

    private MenuBox createMenuBodyMainMenu() {
        log.debug("createMenuBodyMainMenu()");

        var box = new MenuBox();

        Set<MenuItem> enabledItems = FXGL.getSettings().getEnabledMenuItems();

        var itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());
        box.add(itemNewGame);

        // << --- THÊM NÚT LOAD GAME --- >>
        if (enabledItems.contains(MenuItem.SAVE_LOAD)) {
            var itemLoad = new MenuButton("menu.load");
            itemLoad.setMenuContent(this::createContentLoad, false);
            box.add(itemLoad);
        }

        var itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        if (enabledItems.contains(MenuItem.EXTRA)) {
            var itemExtra = new MenuButton("menu.extra");
            itemExtra.setChild(createExtraMenu());
            box.add(itemExtra);
        }

        var itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());
        box.add(itemExit);

        return box;
    }

    private MenuBox createMenuBodyGameMenu() {
        log.debug("createMenuBodyGameMenu()");

        var box = new MenuBox();

        var enabledItems = FXGL.getSettings().getEnabledMenuItems();

        var itemResume = new MenuButton("menu.resume");
        itemResume.setOnAction(e -> fireResume());
        box.add(itemResume);

        if (enabledItems.contains(MenuItem.SAVE_LOAD)) {
            var itemSave = new MenuButton("menu.save");
            itemSave.setOnAction(e -> fireSave());

            var itemLoad = new MenuButton("menu.load");
            itemLoad.setMenuContent(this::createContentLoad, false);

            box.add(itemSave);
            box.add(itemLoad);
        }

        var itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        if (enabledItems.contains(MenuItem.EXTRA)) {
            var itemExtra = new MenuButton("menu.extra");
            itemExtra.setChild(createExtraMenu());
            box.add(itemExtra);
        }

        if (FXGL.getSettings().isMainMenuEnabled()) {
            var itemExit = new MenuButton("menu.mainMenu");
            itemExit.setOnAction(e -> fireExitToMainMenu());
            box.add(itemExit);
        } else {
            var itemExit = new MenuButton("menu.exit");
            itemExit.setOnAction(e -> fireExit());
            box.add(itemExit);
        }

        return box;
    }

    private MenuBox createOptionsMenu() {
        log.debug("createOptionsMenu()");

        // var itemGameplay = new MenuButton("menu.gameplay");
        // itemGameplay.setMenuContent(this::createContentGameplay);
        var itemControls = new MenuButton("menu.controls");
        itemControls.setMenuContent(this::createContentControls);

        var itemVideo = new MenuButton("menu.video");
        itemVideo.setMenuContent(this::createContentVideo);
        var itemAudio = new MenuButton("menu.audio");
        itemAudio.setMenuContent(this::createContentAudio);

        // var btnRestore = createRestoreButton();
        return new MenuBox(itemControls, itemVideo, itemAudio);
    }

    private MenuButton createRestoreButton() {
        var btnRestore = new MenuButton("menu.restore");
        btnRestore.setOnAction(
                e
                -> FXGL.getDialogService()
                        .showConfirmationBox(
                                FXGL.localize("menu.settingsRestore"),
                                yes -> {
                                    if (yes) {
                                        switchMenuContentTo(defaultContent);
                                        // TODO: Restore Default Settings
                                        restoreDefaultSettings();
                                    }
                                }));
        return btnRestore;
    }

    private MenuBox createExtraMenu() {
        log.debug("createExtraMenu()");

        // var itemAchievements = new MenuButton("menu.trophies");
        // itemAchievements.setMenuContent(this::createContentAchievements);
        var itemLeaderboard = new MenuButton("temp.key");
        itemLeaderboard.btn.textProperty().unbind();
        itemLeaderboard.btn.setText("LEADERBOARD");
        itemLeaderboard.setMenuContent(this::createContentLeaderboard);

        var itemCredits = new MenuButton("menu.credits");
        itemCredits.setMenuContent(this::createContentCredits);

        return new MenuBox(itemLeaderboard, itemCredits);
    }

    private void switchMenuTo(Node menuNode) {
        var oldMenu = menuRoot.getChildren().getFirst();

        var ft = new FadeTransition(Duration.seconds(0.33), oldMenu);
        ft.setToValue(0.0);
        ft.setOnFinished(
                e -> {
                    menuNode.setOpacity(0.0);
                    menuRoot.getChildren().set(0, menuNode);
                    oldMenu.setOpacity(1.0);

                    var ft2 = new FadeTransition(Duration.seconds(0.33), menuNode);
                    ft2.setToValue(1.0);
                    ft2.play();
                });
        ft.play();
    }

    private void switchMenuContentTo(Node content) {
        menuContentRoot.getChildren().set(0, content);
    }

    /**
     * @return full version string
     */
    private String makeVersionString() {
        return "v"
                + FXGL.getSettings().getVersion()
                + (FXGL.getSettings().getApplicationMode() == ApplicationMode.RELEASE
                ? ""
                : "-" + FXGL.getSettings().getApplicationMode());
    }

    protected MenuContent createContentLoad() {
        log.debug("createContentLoad()");

        ListView<SaveFile> list = FXGL.getUIFactoryService().newListView();

        final var FONT_SIZE = 16.0;

        list.setCellFactory(
                e
                -> new ListCell<>() {
            @Override
            protected void updateItem(SaveFile item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    var nameDate
                            = String.format(
                                    "%-25.25s %s",
                                    item.getName(),
                                    item.getDateTime()
                                            .format(
                                                    DateTimeFormatter.ofPattern(
                                                            "dd-MM-yyyy HH-mm")));

                    var text
                            = FXGL.getUIFactoryService()
                                    .newText(
                                            nameDate,
                                            Color.WHITE,
                                            FontType.MONO,
                                            FONT_SIZE);

                    setGraphic(text);
                }
            }
        });

        var task
                = getSaveLoadService()
                        .readSaveFilesTask("./", FXGL.getSettings().getSaveFileExt())
                        .onSuccess(files -> list.getItems().addAll(files));

        FXGL.getTaskService().runAsyncFXWithDialog(task, FXGL.localize("menu.load"));

        list.prefHeightProperty().bind(Bindings.size(list.getItems()).multiply(FONT_SIZE).add(16));

        var btnLoad
                = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.load"));
        btnLoad.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

        btnLoad.setOnAction(
                e -> {
                    var saveFile = list.getSelectionModel().getSelectedItem();
                    fireLoad(saveFile);
                });

        var btnDelete
                = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.delete"));
        btnDelete.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

        btnDelete.setOnAction(
                e -> {
                    var saveFile = list.getSelectionModel().getSelectedItem();
                    fireDelete(saveFile);
                });

        var hbox = new HBox(50.0, btnLoad, btnDelete);
        hbox.setAlignment(Pos.CENTER);

        return new MenuContent(list, hbox);
    }

    protected MenuContent createContentGameplay() {
        log.debug("createContentGameplay()");

        return new MenuContent();
    }

    protected MenuContent createContentControls() {
        log.debug("createContentControls()");

        var grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10.0);
        grid.setVgap(10.0);
        grid.setPadding(new Insets(10.0, 10.0, 10.0, 10.0));
        grid.getColumnConstraints()
                .add(new ColumnConstraints(200.0, 200.0, 200.0, Priority.ALWAYS, HPos.LEFT, true));
        grid.getRowConstraints()
                .add(new RowConstraints(40.0, 40.0, 40.0, Priority.ALWAYS, VPos.CENTER, true));

        grid.setUserData(0);

        for (var e : FXGL.getInput().getAllBindings().entrySet()) {
            addNewInputBinding(e.getKey(), e.getValue(), grid);
        }

        var scroll = new FXGLScrollPane(grid);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setMaxHeight(getAppHeight() / 2.5);

        var hbox = new HBox(scroll);
        hbox.setAlignment(Pos.CENTER);

        return new MenuContent(hbox);
    }

    private void addNewInputBinding(UserAction action, Trigger trigger, GridPane grid) {
        var actionName = FXGL.getUIFactoryService().newText(action.getName(), Color.WHITE, 18.0);

        var triggerView = new TriggerView(trigger);
        triggerView.triggerProperty().bind(FXGL.getInput().triggerProperty(action));

        triggerView.setOnMouseClicked(
                e -> {
                    if (pressAnyKeyState.isActive) {
                        return;
                    }

                    pressAnyKeyState.isActive = true;
                    pressAnyKeyState.actionContext = action;
                    FXGL.getSceneService().pushSubScene(pressAnyKeyState);
                });

        var hBox = new HBox();
        hBox.setPrefWidth(100.0);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(triggerView);

        var controlsRow = (int) grid.getUserData();
        grid.addRow(controlsRow++, actionName, hBox);
        grid.setUserData(controlsRow);
    }

    protected MenuContent createContentVideo() {
        log.debug("createContentVideo()");

        var languageBox
                = FXGL.getUIFactoryService()
                        .newChoiceBox(
                                FXCollections.observableArrayList(
                                        FXGL.getSettings().getSupportedLanguages()));
        languageBox.setValue(FXGL.getSettings().getLanguage().get());
        languageBox.setConverter(
                new StringConverter<>() {
            @Override
            public String toString(Language object) {
                return object.getNativeName();
            }

            @Override
            public Language fromString(String string) {
                return FXGL.getSettings().getSupportedLanguages().stream()
                        .filter(l -> l.getNativeName().equals(string))
                        .findFirst()
                        .orElse(Language.NONE);
            }
        });

        FXGL.getSettings().getLanguage().bindBidirectional(languageBox.valueProperty());

        var vbox = new VBox();

        if (FXGL.getSettings().isFullScreenAllowed()) {
            var cbFullScreen = FXGL.getUIFactoryService().newCheckBox();
            cbFullScreen.selectedProperty().bindBidirectional(FXGL.getSettings().getFullScreen());

            vbox.getChildren()
                    .add(
                            new HBox(
                                    25.0,
                                    FXGL.getUIFactoryService()
                                            .newText(FXGL.localize("menu.fullscreen") + ": "),
                                    cbFullScreen));
        }

        return new MenuContent(
                new HBox(
                        25.0,
                        FXGL.getUIFactoryService()
                                .newText(FXGL.localizedStringProperty("menu.language").concat(":")),
                        languageBox),
                vbox);
    }

    protected MenuContent createContentAudio() {
        log.debug("createContentAudio()");

        var sliderMusic = FXGL.getUIFactoryService().newSlider();
        sliderMusic.setMin(0.0);
        sliderMusic.setMax(1.0);
        sliderMusic
                .valueProperty()
                .bindBidirectional(FXGL.getSettings().globalMusicVolumeProperty());

        var textMusic
                = FXGL.getUIFactoryService()
                        .newText(FXGL.localizedStringProperty("menu.music.volume").concat(": "));
        var percentMusic = FXGL.getUIFactoryService().newText("");
        percentMusic
                .textProperty()
                .bind(sliderMusic.valueProperty().multiply(100).asString("%.0f"));

        var sliderSound = FXGL.getUIFactoryService().newSlider();
        sliderSound.setMin(0.0);
        sliderSound.setMax(1.0);
        sliderSound
                .valueProperty()
                .bindBidirectional(FXGL.getSettings().globalSoundVolumeProperty());

        var textSound
                = FXGL.getUIFactoryService()
                        .newText(FXGL.localizedStringProperty("menu.sound.volume").concat(": "));
        var percentSound = FXGL.getUIFactoryService().newText("");
        percentSound
                .textProperty()
                .bind(sliderSound.valueProperty().multiply(100).asString("%.0f"));

        var gridPane = new GridPane();
        gridPane.setHgap(15.0);
        gridPane.addRow(0, textMusic, sliderMusic, percentMusic);
        gridPane.addRow(1, textSound, sliderSound, percentSound);

        return new MenuContent(gridPane);
    }

    protected MenuContent createContentCredits() {
        log.debug("createContentCredits()");

        var pane = new FXGLScrollPane();
        pane.setPrefWidth(500.0);
        pane.setPrefHeight(getAppHeight() / 2.0);
        pane.setStyle("-fx-background:black;");

        var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefWidth(pane.getPrefWidth() - 15);

        var credits = new ArrayList<>(FXGL.getSettings().getCredits());
        credits.add("");
        credits.add("Powered by FXGL " + FXGL.getVersion());
        credits.add("Author: Almas Baimagambetov");
        credits.add("https://github.com/AlmasB/FXGL");
        credits.add("");

        for (var credit : credits) {
            if (credit.length() > 45) {
                log.warning("Credit color length > 45: " + credit);
            }

            vbox.getChildren().add(FXGL.getUIFactoryService().newText(credit));
        }

        pane.setContent(vbox);

        return new MenuContent(pane);
    }

    protected MenuContent createContentAchievements() {
        log.debug("createContentAchievements()");

        var content = new MenuContent();

        FXGL.getAchievementService()
                .getAchievementsCopy()
                .forEach(
                        a -> {
                            var checkBox = new CheckBox();
                            checkBox.setDisable(true);
                            checkBox.selectedProperty().bind(a.achievedProperty());

                            var text = FXGL.getUIFactoryService().newText(a.getName());
                            var tooltip = new Tooltip(a.getDescription());
                            tooltip.setShowDelay(Duration.seconds(0.1));

                            Tooltip.install(text, tooltip);

                            var box = new HBox(25.0, text, checkBox);
                            box.setAlignment(Pos.CENTER_RIGHT);

                            content.getChildren().add(box);
                        });

        return content;
    }

    protected MenuContent createContentLeaderboard() {
        log.debug("createContentLeaderboard()");

        var leaderboardManager = LeaderboardManager.getInstance();

        var endlessScores = leaderboardManager.getViewLeaderboard();

        var contentBox = new VBox(20);
        contentBox.setAlignment(Pos.TOP_CENTER);

        var title
                = FXGL.getUIFactoryService()
                        .newText("LEADERBOARD", Color.ORANGE, FontType.MONO, 27.0);

        var grid = new GridPane();
        grid.setAlignment(Pos.TOP_LEFT);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        grid.getColumnConstraints()
                .add(new ColumnConstraints(40, 40, 40, Priority.NEVER, HPos.CENTER, false)); // Rank
        grid.getColumnConstraints()
                .add(
                        new ColumnConstraints(
                                120, 120, 120, Priority.NEVER, HPos.LEFT, false)); // Name
        grid.getColumnConstraints()
                .add(new ColumnConstraints(70, 70, 70, Priority.NEVER, HPos.RIGHT, false)); // Score
        grid.getColumnConstraints()
                .add(
                        new ColumnConstraints(
                                50, 50, 50, Priority.NEVER, HPos.CENTER, false)); // Level
        grid.getColumnConstraints()
                .add(
                        new ColumnConstraints(
                                130, 130, 130, Priority.NEVER, HPos.CENTER, false)); // Date

        grid.addRow(
                0,
                FXGL.getUIFactoryService().newText("Rank", Color.NAVAJOWHITE, FontType.MONO, 18.0),
                FXGL.getUIFactoryService()
                        .newText("Player", Color.NAVAJOWHITE, FontType.MONO, 18.0),
                FXGL.getUIFactoryService().newText("Score", Color.NAVAJOWHITE, FontType.MONO, 18.0),
                FXGL.getUIFactoryService().newText("Level", Color.NAVAJOWHITE, FontType.MONO, 18.0),
                FXGL.getUIFactoryService().newText("Date", Color.NAVAJOWHITE, FontType.MONO, 18.0));

        int rank = 1;
        if (endlessScores.isEmpty()) {
            var noDataText
                    = FXGL.getUIFactoryService()
                            .newText("No data available.", Color.GRAY, FontType.UI, 16.0);
            grid.add(noDataText, 0, 1);
            GridPane.setColumnSpan(noDataText, 5);
        } else {
            for (var score : endlessScores) {
                var dateStr
                        = score.timestamp()
                                .atZone(java.time.ZoneId.systemDefault())
                                .format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));

                grid.addRow(
                        rank,
                        FXGL.getUIFactoryService()
                                .newText("#" + rank, Color.YELLOW, FontType.MONO, 16.0),
                        FXGL.getUIFactoryService()
                                .newText(score.name(), Color.CYAN, FontType.MONO, 16.0),
                        FXGL.getUIFactoryService()
                                .newText(
                                        String.valueOf(score.score()),
                                        Color.LIGHTGREEN,
                                        FontType.MONO,
                                        16.0),
                        FXGL.getUIFactoryService()
                                .newText(
                                        String.valueOf(score.level()),
                                        Color.ORANGE,
                                        FontType.MONO,
                                        16.0),
                        FXGL.getUIFactoryService()
                                .newText(dateStr, Color.LIGHTGRAY, FontType.MONO, 16.0));
                rank++;
            }
        }

        var scrollPane = new FXGLScrollPane(grid);
        scrollPane.setPrefHeight(getAppHeight() / 2.0);
        scrollPane.setPrefWidth(480);
        scrollPane.setStyle("-fx-background: black;");

        contentBox.getChildren().addAll(title, scrollPane);

        return new MenuContent(contentBox);
    }

    private void showProfileDialog() {
        ChoiceBox<String> profilesBox
                = FXGL.getUIFactoryService().newChoiceBox(FXCollections.observableArrayList());

        var btnNew
                = FXGL.getUIFactoryService()
                        .newButton(FXGL.localizedStringProperty("multiplayer.new"));
        var btnSelect
                = FXGL.getUIFactoryService()
                        .newButton(FXGL.localizedStringProperty("multiplayer.select"));
        btnSelect.disableProperty().bind(profilesBox.valueProperty().isNull());
        var btnDelete
                = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.delete"));
        btnDelete.disableProperty().bind(profilesBox.valueProperty().isNull());

        btnNew.setOnAction(
                e
                -> FXGL.getDialogService()
                        .showInputBox(
                                FXGL.localize("profile.new"),
                                InputPredicates.ALPHANUM,
                                s -> {
                                    // TODO: implement profile creation tasks if needed
                                }));

        btnSelect.setOnAction(
                e -> {
                    var name = profilesBox.getValue();
                    FXGL.getSettings().getProfileName().set(name);
                    // TODO: load profile details
                });

        // The rest of profile loading is commented out in original Kotlin
    }

    private static class MenuBox extends VBox {

        public MenuBox(MenuButton... items) {
            super();
            for (var item : items) {
                add(item);
            }
        }

        public double getLayoutHeight() {
            return 10 * getChildren().size();
        }

        public void add(MenuButton item) {
            item.setParent(this);
            getChildren().addAll(item);
        }
    }

    public static class MenuContent extends VBox {

        public int maxW = 0;
        private Runnable onOpen;
        private Runnable onClose;

        public MenuContent(Node... items) {
            super();

            if (items != null && items.length > 0) {
                maxW = (int) items[0].getLayoutBounds().getWidth();

                for (var n : items) {
                    var w = (int) n.getLayoutBounds().getWidth();
                    if (w > maxW) {
                        maxW = w;
                    }
                }

                for (var item : items) {
                    getChildren().addAll(item);
                }
            }

            sceneProperty()
                    .addListener(
                            (v, oldS, newS) -> {
                                if (newS != null) {
                                    onOpen();
                                } else {
                                    onClose();
                                }
                            });
        }

        public void setOnOpen(Runnable onOpenAction) {
            this.onOpen = onOpenAction;
        }

        public void setOnClose(Runnable onCloseAction) {
            this.onClose = onCloseAction;
        }

        private void onOpen() {
            if (onOpen != null) {
                onOpen.run();
            }
        }

        private void onClose() {
            if (onClose != null) {
                onClose.run();
            }
        }
    }

    private class MenuButton extends Pane {

        public final Button btn;
        private MenuBox parent;
        private MenuContent cachedContent;
        private boolean isAnimating = false;

        public MenuButton(String stringKey) {
            btn = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty(stringKey));
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setStyle("-fx-background-color: transparent");

            btn.setPrefWidth(280);

            final var p = new Polygon(0.0, 0.0, 270.0, 0.0, 300.0, 35.0, 0.0, 35.0);
            p.setMouseTransparent(true);

            var g
                    = new LinearGradient(
                            0.0,
                            1.0,
                            1.0,
                            0.2,
                            true,
                            CycleMethod.NO_CYCLE,
                            new Stop(0.6, Color.color(1.0, 0.8, 0.0, 0.34)),
                            new Stop(0.85, Color.color(1.0, 0.8, 0.0, 0.74)),
                            new Stop(1.0, Color.WHITE));

            p.fillProperty()
                    .bind(
                            Bindings.when(btn.pressedProperty())
                                    .then((Paint) Color.color(1.0, 0.8, 0.0, 0.75))
                                    .otherwise(g));

            p.setStroke(Color.color(0.1, 0.1, 0.1, 0.15));

            if (!FXGL.getSettings().isNative()) {
                p.setEffect(new GaussianBlur());
            }

            p.visibleProperty().bind(btn.hoverProperty());

            getChildren().addAll(btn, p);

            btn.focusedProperty()
                    .addListener(
                            (v, e, isFocused) -> {
                                if (isFocused) {
                                    var isOK
                                    = animations.stream().noneMatch(Animation::isAnimating)
                                    && !isAnimating;
                                    if (isOK) {
                                        isAnimating = true;

                                        FXGL.animationBuilder()
                                                .onFinished(() -> isAnimating = false)
                                                .bobbleDown(this)
                                                .buildAndPlay(Menu.this);
                                    }
                                }
                            });
        }

        public void setOnAction(EventHandler<ActionEvent> e) {
            btn.setOnAction(e);
        }

        public void setParent(MenuBox menu) {
            parent = menu;
        }

        public void setMenuContent(Supplier<MenuContent> contentSupplier) {
            setMenuContent(contentSupplier, true);
        }

        public void setMenuContent(Supplier<MenuContent> contentSupplier, boolean isCached) {
            btn.addEventHandler(
                    ActionEvent.ACTION,
                    e -> {
                        if (cachedContent == null || !isCached) {
                            cachedContent = contentSupplier.get();
                        }

                        switchMenuContentTo(cachedContent);
                    });
        }

        public void setChild(MenuBox menu) {
            var back = new MenuButton("menu.back");
            menu.getChildren().addFirst(back);

            back.addEventHandler(ActionEvent.ACTION, e -> switchMenuTo(this.parent));

            btn.addEventHandler(ActionEvent.ACTION, e -> switchMenuTo(menu));
        }
    }

    private class PressAnyKeyState extends SubScene {

        private UserAction actionContext;

        private boolean isActive = false;

        public PressAnyKeyState() {
            getInput()
                    .addEventFilter(
                            KeyEvent.KEY_PRESSED,
                            e -> {
                                if (Input.isIllegal(e.getCode())) {
                                    return;
                                }

                                var rebound
                                = FXGL.getInput()
                                        .rebind(
                                                actionContext,
                                                e.getCode(),
                                                InputModifier.from(e));

                                if (rebound) {
                                    FXGL.getSceneService().popSubScene();
                                    isActive = false;
                                }
                            });

            getInput()
                    .addEventFilter(
                            MouseEvent.MOUSE_PRESSED,
                            e -> {
                                var rebound
                                = FXGL.getInput()
                                        .rebind(
                                                actionContext,
                                                e.getButton(),
                                                InputModifier.from(e));

                                if (rebound) {
                                    FXGL.getSceneService().popSubScene();
                                    isActive = false;
                                }
                            });

            var rect = new Rectangle(250.0, 100.0);
            rect.setStroke(Color.color(0.85, 0.9, 0.9, 0.95));
            rect.setStrokeWidth(10.0);
            rect.setArcWidth(15.0);
            rect.setArcHeight(15.0);

            var text = FXGL.getUIFactoryService().newText("", 24.0);
            text.textProperty().bind(FXGL.localizedStringProperty("menu.pressAnyKey"));

            var pane = new StackPane(rect, text);
            pane.setTranslateX(getAppWidth() / 2.0 - 125);
            pane.setTranslateY(getAppHeight() / 2.0 - 50);

            getContentRoot().getChildren().add(pane);
        }
    }
}
