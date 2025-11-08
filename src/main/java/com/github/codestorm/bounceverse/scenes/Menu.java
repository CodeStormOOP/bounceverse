/*
 * FXGL - JavaFX Game Library. The MIT License (MIT).
 * Copyright (c) AlmasB (almaslvl@gmail.com).
 * See LICENSE for details.
 *
 * Modified by Mai Thành (@thnhmai06), 2025.
 */

package com.github.codestorm.bounceverse.scenes;

import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.animation.Interpolators;
import com.almasb.fxgl.app.ApplicationMode;
import com.almasb.fxgl.app.MenuItem;
import com.almasb.fxgl.app.scene.FXGLDefaultMenu;
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
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.animation.FadeTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.*;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
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

/**
 *
 *
 * <h1>{@link Menu}</h1>
 *
 * Menu ở Màn hình chính và trong trò chơi. Được viết lại dựa trên của {@link FXGLDefaultMenu} để
 * phù hợp với trò chơi.
 *
 * @see FXGLDefaultMenu
 */
@SuppressWarnings("ALL")
public class Menu extends FXGLMenu {
    private static final Logger log = Logger.get(Menu.class);

    private final ParticleSystem particleSystem = new ParticleSystem();

    private final SimpleObjectProperty<Color> titleColor = new SimpleObjectProperty<>(Color.WHITE);
    private final Pane menuRoot = new Pane();
    private final Pane menuContentRoot = new Pane();
    private final MenuContent EMPTY = new MenuContent();
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

        double menuX = 50.0;
        double menuY = getAppHeight() / 2.0 - menu.getLayoutHeight() / 2.0;

        menuRoot.setTranslateX(menuX);
        menuRoot.setTranslateY(menuY);

        menuContentRoot.setTranslateX(getAppWidth() - 500.0);
        menuContentRoot.setTranslateY(menuY);

        initParticles();

        menuRoot.getChildren().add(menu);
        menuContentRoot.getChildren().add(EMPTY);

        getContentRoot()
                .getChildren()
                .addAll(
                        createBackground((double) getAppWidth(), (double) getAppHeight()),
                        createTitleView(FXGL.getSettings().getTitle()),
                        createVersionView(makeVersionString()),
                        particleSystem.getPane(),
                        menuRoot,
                        menuContentRoot);
    }

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
                (n) -> new Point2D(FXGL.random() * 2.5, -FXGL.random() * FXGL.random(80, 120)));
        emitter.setExpireFunction((n) -> Duration.seconds(FXGL.random(4, 7)));
        emitter.setScaleFunction((n) -> new Point2D(0.15, 0.10));
        emitter.setSpawnPointFunction(
                (n) -> new Point2D(FXGL.random(0.0, getAppWidth() - 200.0), 120.0));

        particleSystem.addParticleEmitter(emitter, 0.0, FXGL.getAppHeight());
    }

    @Override
    public void onCreate() {
        animations.clear();

        MenuBox menuBox = (MenuBox) menuRoot.getChildren().getFirst();

        for (int i = 0; i < menuBox.getChildren().size(); i++) {
            Node node = menuBox.getChildren().get(i);
            node.setTranslateX(-250.0);

            Animation<?> animation =
                    FXGL.animationBuilder()
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
        switchMenuContentTo(EMPTY);
    }

    @Override
    public void onUpdate(double tpf) {
        if (getType() == MenuType.MAIN_MENU
                && FXGL.getSettings().isUserProfileEnabled()
                && "DEFAULT".equals(FXGL.getSettings().getProfileName().get())) {
            showProfileDialog();
        }

        animations.forEach((a) -> a.onUpdate(tpf));

        double frequency = 1.7;

        t += tpf * frequency;

        particleSystem.onUpdate(tpf);

        Color color = Color.color(1.0, 1.0, 1.0, FXGLMath.noise1D(t));
        titleColor.set(color);
    }

    private Node createBackground(double width, double height) {
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.rgb(10, 1, 1, getType() == MenuType.GAME_MENU ? 0.5 : 1.0));
        return bg;
    }

    private Node createTitleView(String title) {
        Text text = FXGL.getUIFactoryService().newText(title.substring(0, 1), 50.0);
        text.setFill(null);
        text.strokeProperty().bind(titleColor);
        text.setStrokeWidth(1.5);

        Text text2 = FXGL.getUIFactoryService().newText(title.substring(1), 50.0);
        text2.setFill(null);
        text2.setStroke(titleColor.get());
        text2.setStrokeWidth(1.5);

        double textWidth = text.getLayoutBounds().getWidth() + text2.getLayoutBounds().getWidth();

        Rectangle border = new Rectangle(textWidth + 30, 65.0, null);
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
                    if (i % 2 == 0) return new Point2D(FXGL.random(-10.0, 0.0), 0.0);
                    else return new Point2D(FXGL.random(0.0, 10.0), 0.0);
                });
        emitter.setExpireFunction((i) -> Duration.seconds(FXGL.random(4.0, 6.0)));
        emitter.setScaleFunction((i) -> new Point2D(-0.03, -0.03));
        emitter.setSpawnPointFunction((i) -> new Point2D(0, 0));
        emitter.setAccelerationFunction(
                () -> new Point2D(FXGL.random(-1.0, 1.0), FXGL.random(0.0, 0.0)));

        HBox box = new HBox(text, text2);
        box.setAlignment(Pos.CENTER);

        StackPane titleRoot = new StackPane(border, box);
        titleRoot.setTranslateX(getAppWidth() / 2.0 - (textWidth + 30) / 2.0);
        titleRoot.setTranslateY(50.0);

        if (!FXGL.getSettings().isNative())
            particleSystem.addParticleEmitter(
                    emitter,
                    getAppWidth() / 2.0 - 30,
                    titleRoot.getTranslateY() + border.getHeight() - 16);

        return titleRoot;
    }

    private Node createVersionView(String version) {
        Text view = FXGL.getUIFactoryService().newText(version);
        view.setTranslateY(getAppHeight() - 2.0);
        return view;
    }

    private MenuBox createMenuBodyMainMenu() {
        log.debug("createMenuBodyMainMenu()");

        MenuBox box = new MenuBox();

        Set<MenuItem> enabledItems = FXGL.getSettings().getEnabledMenuItems();

        MenuButton itemNewGame = new MenuButton("menu.newGame");
        itemNewGame.setOnAction(e -> fireNewGame());
        box.add(itemNewGame);

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        if (enabledItems.contains(MenuItem.EXTRA)) {
            MenuButton itemExtra = new MenuButton("menu.extra");
            itemExtra.setChild(createExtraMenu());
            box.add(itemExtra);
        }

        MenuButton itemExit = new MenuButton("menu.exit");
        itemExit.setOnAction(e -> fireExit());
        box.add(itemExit);

        return box;
    }

    private MenuBox createMenuBodyGameMenu() {
        log.debug("createMenuBodyGameMenu()");

        MenuBox box = new MenuBox();

        Set<MenuItem> enabledItems = FXGL.getSettings().getEnabledMenuItems();

        MenuButton itemResume = new MenuButton("menu.resume");
        itemResume.setOnAction(e -> fireResume());
        box.add(itemResume);

        if (enabledItems.contains(MenuItem.SAVE_LOAD)) {
            MenuButton itemSave = new MenuButton("menu.save");
            itemSave.setOnAction(e -> fireSave());

            MenuButton itemLoad = new MenuButton("menu.load");
            itemLoad.setMenuContent(() -> createContentLoad(), false);

            box.add(itemSave);
            box.add(itemLoad);
        }

        MenuButton itemOptions = new MenuButton("menu.options");
        itemOptions.setChild(createOptionsMenu());
        box.add(itemOptions);

        if (enabledItems.contains(MenuItem.EXTRA)) {
            MenuButton itemExtra = new MenuButton("menu.extra");
            itemExtra.setChild(createExtraMenu());
            box.add(itemExtra);
        }

        if (FXGL.getSettings().isMainMenuEnabled()) {
            MenuButton itemExit = new MenuButton("menu.mainMenu");
            itemExit.setOnAction(e -> fireExitToMainMenu());
            box.add(itemExit);
        } else {
            MenuButton itemExit = new MenuButton("menu.exit");
            itemExit.setOnAction(e -> fireExit());
            box.add(itemExit);
        }

        return box;
    }

    private MenuBox createOptionsMenu() {
        log.debug("createOptionsMenu()");

        MenuButton itemGameplay = new MenuButton("menu.gameplay");
        itemGameplay.setMenuContent(() -> createContentGameplay());

        MenuButton itemControls = new MenuButton("menu.controls");
        itemControls.setMenuContent(() -> createContentControls());

        MenuButton itemVideo = new MenuButton("menu.video");
        itemVideo.setMenuContent(() -> createContentVideo());
        MenuButton itemAudio = new MenuButton("menu.audio");
        itemAudio.setMenuContent(() -> createContentAudio());

        MenuButton btnRestore = new MenuButton("menu.restore");
        btnRestore.setOnAction(
                evt -> {
                    FXGL.getDialogService()
                            .showConfirmationBox(
                                    FXGL.localize("menu.settingsRestore"),
                                    yes -> {
                                        if (yes) {
                                            switchMenuContentTo(EMPTY);
                                            restoreDefaultSettings();
                                        }
                                    });
                });

        return new MenuBox(itemGameplay, itemControls, itemVideo, itemAudio, btnRestore);
    }

    private MenuBox createExtraMenu() {
        log.debug("createExtraMenu()");

        MenuButton itemAchievements = new MenuButton("menu.trophies");
        itemAchievements.setMenuContent(() -> createContentAchievements());

        MenuButton itemCredits = new MenuButton("menu.credits");
        itemCredits.setMenuContent(() -> createContentCredits());

        return new MenuBox(itemAchievements, itemCredits);
    }

    private void switchMenuTo(Node menuNode) {
        Node oldMenu = menuRoot.getChildren().get(0);

        FadeTransition ft = new FadeTransition(Duration.seconds(0.33), oldMenu);
        ft.setToValue(0.0);
        ft.setOnFinished(
                e -> {
                    menuNode.setOpacity(0.0);
                    menuRoot.getChildren().set(0, menuNode);
                    oldMenu.setOpacity(1.0);

                    FadeTransition ft2 = new FadeTransition(Duration.seconds(0.33), menuNode);
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

        final double FONT_SIZE = 16.0;

        list.setCellFactory(
                param ->
                        new ListCell<SaveFile>() {
                            @Override
                            protected void updateItem(SaveFile item, boolean empty) {
                                super.updateItem(item, empty);

                                if (empty || item == null) {
                                    setText(null);
                                    setGraphic(null);
                                } else {
                                    String nameDate =
                                            String.format(
                                                    "%-25.25s %s",
                                                    item.getName(),
                                                    item.getDateTime()
                                                            .format(
                                                                    DateTimeFormatter.ofPattern(
                                                                            "dd-MM-yyyy HH-mm")));

                                    Text text =
                                            FXGL.getUIFactoryService()
                                                    .newText(
                                                            nameDate,
                                                            Color.WHITE,
                                                            FontType.MONO,
                                                            FONT_SIZE);

                                    setGraphic(text);
                                }
                            }
                        });

        var task =
                getSaveLoadService()
                        .readSaveFilesTask("./", FXGL.getSettings().getSaveFileExt())
                        .onSuccess(files -> list.getItems().addAll(files));

        FXGL.getTaskService().runAsyncFXWithDialog(task, FXGL.localize("menu.load"));

        list.prefHeightProperty().bind(Bindings.size(list.getItems()).multiply(FONT_SIZE).add(16));

        Button btnLoad =
                FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.load"));
        btnLoad.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

        btnLoad.setOnAction(
                e -> {
                    SaveFile saveFile = list.getSelectionModel().getSelectedItem();
                    fireLoad(saveFile);
                });

        Button btnDelete =
                FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.delete"));
        btnDelete.disableProperty().bind(list.getSelectionModel().selectedItemProperty().isNull());

        btnDelete.setOnAction(
                e -> {
                    SaveFile saveFile = list.getSelectionModel().getSelectedItem();
                    fireDelete(saveFile);
                });

        HBox hbox = new HBox(50.0, btnLoad, btnDelete);
        hbox.setAlignment(Pos.CENTER);

        return new MenuContent(list, hbox);
    }

    protected MenuContent createContentGameplay() {
        log.debug("createContentGameplay()");

        return new MenuContent();
    }

    protected MenuContent createContentControls() {
        log.debug("createContentControls()");

        GridPane grid = new GridPane();
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

        FXGLScrollPane scroll = new FXGLScrollPane(grid);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.ALWAYS);
        scroll.setMaxHeight(getAppHeight() / 2.5);

        HBox hbox = new HBox(scroll);
        hbox.setAlignment(Pos.CENTER);

        return new MenuContent(hbox);
    }

    private void addNewInputBinding(UserAction action, Trigger trigger, GridPane grid) {
        Text actionName = FXGL.getUIFactoryService().newText(action.getName(), Color.WHITE, 18.0);

        TriggerView triggerView = new TriggerView(trigger);
        triggerView.triggerProperty().bind(FXGL.getInput().triggerProperty(action));

        triggerView.setOnMouseClicked(
                evt -> {
                    if (pressAnyKeyState.isActive) return;

                    pressAnyKeyState.isActive = true;
                    pressAnyKeyState.actionContext = action;
                    FXGL.getSceneService().pushSubScene(pressAnyKeyState);
                });

        HBox hBox = new HBox();
        hBox.setPrefWidth(100.0);
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().add(triggerView);

        int controlsRow = (int) grid.getUserData();
        grid.addRow(controlsRow++, actionName, hBox);
        grid.setUserData(controlsRow);
    }

    protected MenuContent createContentVideo() {
        log.debug("createContentVideo()");

        ChoiceBox<Language> languageBox =
                FXGL.getUIFactoryService()
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

        VBox vbox = new VBox();

        if (FXGL.getSettings().isFullScreenAllowed()) {
            CheckBox cbFullScreen = FXGL.getUIFactoryService().newCheckBox();
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

        Slider sliderMusic = FXGL.getUIFactoryService().newSlider();
        sliderMusic.setMin(0.0);
        sliderMusic.setMax(1.0);
        sliderMusic
                .valueProperty()
                .bindBidirectional(FXGL.getSettings().globalMusicVolumeProperty());

        Text textMusic =
                FXGL.getUIFactoryService()
                        .newText(FXGL.localizedStringProperty("menu.music.volume").concat(": "));
        Text percentMusic = FXGL.getUIFactoryService().newText("");
        percentMusic
                .textProperty()
                .bind(sliderMusic.valueProperty().multiply(100).asString("%.0f"));

        Slider sliderSound = FXGL.getUIFactoryService().newSlider();
        sliderSound.setMin(0.0);
        sliderSound.setMax(1.0);
        sliderSound
                .valueProperty()
                .bindBidirectional(FXGL.getSettings().globalSoundVolumeProperty());

        Text textSound =
                FXGL.getUIFactoryService()
                        .newText(FXGL.localizedStringProperty("menu.sound.volume").concat(": "));
        Text percentSound = FXGL.getUIFactoryService().newText("");
        percentSound
                .textProperty()
                .bind(sliderSound.valueProperty().multiply(100).asString("%.0f"));

        GridPane gridPane = new GridPane();
        gridPane.setHgap(15.0);
        gridPane.addRow(0, textMusic, sliderMusic, percentMusic);
        gridPane.addRow(1, textSound, sliderSound, percentSound);

        return new MenuContent(gridPane);
    }

    protected MenuContent createContentCredits() {
        log.debug("createContentCredits()");

        FXGLScrollPane pane = new FXGLScrollPane();
        pane.setPrefWidth(500.0);
        pane.setPrefHeight(getAppHeight() / 2.0);
        pane.setStyle("-fx-background:black;");

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER_LEFT);
        vbox.setPrefWidth(pane.getPrefWidth() - 15);

        ArrayList<String> credits = new ArrayList<>(FXGL.getSettings().getCredits());
        credits.add("");
        credits.add("Powered by FXGL " + FXGL.getVersion());
        credits.add("Author: Almas Baimagambetov");
        credits.add("https://github.com/AlmasB/FXGL");
        credits.add("");

        for (String credit : credits) {
            if (credit.length() > 45) {
                log.warning("Credit name length > 45: " + credit);
            }

            vbox.getChildren().add(FXGL.getUIFactoryService().newText(credit));
        }

        pane.setContent(vbox);

        return new MenuContent(pane);
    }

    protected MenuContent createContentAchievements() {
        log.debug("createContentAchievements()");

        MenuContent content = new MenuContent();

        FXGL.getAchievementService()
                .getAchievementsCopy()
                .forEach(
                        a -> {
                            CheckBox checkBox = new CheckBox();
                            checkBox.setDisable(true);
                            checkBox.selectedProperty().bind(a.achievedProperty());

                            Text text = FXGL.getUIFactoryService().newText(a.getName());
                            Tooltip tooltip = new Tooltip(a.getDescription());
                            tooltip.setShowDelay(Duration.seconds(0.1));

                            Tooltip.install(text, tooltip);

                            HBox box = new HBox(25.0, text, checkBox);
                            box.setAlignment(Pos.CENTER_RIGHT);

                            content.getChildren().add(box);
                        });

        return content;
    }

    private void showProfileDialog() {
        ChoiceBox<String> profilesBox =
                FXGL.getUIFactoryService().newChoiceBox(FXCollections.observableArrayList());

        Button btnNew =
                FXGL.getUIFactoryService()
                        .newButton(FXGL.localizedStringProperty("multiplayer.new"));
        Button btnSelect =
                FXGL.getUIFactoryService()
                        .newButton(FXGL.localizedStringProperty("multiplayer.select"));
        btnSelect.disableProperty().bind(profilesBox.valueProperty().isNull());
        Button btnDelete =
                FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty("menu.delete"));
        btnDelete.disableProperty().bind(profilesBox.valueProperty().isNull());

        btnNew.setOnAction(
                evt -> {
                    FXGL.getDialogService()
                            .showInputBox(
                                    FXGL.localize("profile.new"),
                                    InputPredicates.ALPHANUM,
                                    (Consumer<String>)
                                            name -> {
                                                // TODO: implement profile creation tasks if needed
                                            });
                });

        btnSelect.setOnAction(
                evt -> {
                    String name = profilesBox.getValue();
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

                for (Node n : items) {
                    int w = (int) n.getLayoutBounds().getWidth();
                    if (w > maxW) maxW = w;
                }

                for (Node item : items) {
                    getChildren().addAll(item);
                }
            }

            sceneProperty()
                    .addListener(
                            (obs, oldS, newS) -> {
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
            if (onOpen != null) onOpen.run();
        }

        private void onClose() {
            if (onClose != null) onClose.run();
        }
    }

    private class MenuButton extends Pane {
        public final Button btn;
        private final Polygon p = new Polygon(0.0, 0.0, 220.0, 0.0, 250.0, 35.0, 0.0, 35.0);
        private MenuBox parent;
        private MenuContent cachedContent;
        private boolean isAnimating = false;

        public MenuButton(String stringKey) {
            btn = FXGL.getUIFactoryService().newButton(FXGL.localizedStringProperty(stringKey));
            btn.setAlignment(Pos.CENTER_LEFT);
            btn.setStyle("-fx-background-color: transparent");

            p.setMouseTransparent(true);

            LinearGradient g =
                    new LinearGradient(
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
                            (obs, oldV, isFocused) -> {
                                if (isFocused) {
                                    boolean isOK =
                                            animations.stream().noneMatch(Animation::isAnimating)
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
                        if (cachedContent == null || !isCached)
                            cachedContent = contentSupplier.get();

                        switchMenuContentTo(cachedContent);
                    });
        }

        public void setChild(MenuBox menu) {
            MenuButton back = new MenuButton("menu.back");
            menu.getChildren().add(0, back);

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
                                if (Input.isIllegal(e.getCode())) return;

                                boolean rebound =
                                        FXGL.getInput()
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
                                boolean rebound =
                                        FXGL.getInput()
                                                .rebind(
                                                        actionContext,
                                                        e.getButton(),
                                                        InputModifier.from(e));

                                if (rebound) {
                                    FXGL.getSceneService().popSubScene();
                                    isActive = false;
                                }
                            });

            Rectangle rect = new Rectangle(250.0, 100.0);
            rect.setStroke(Color.color(0.85, 0.9, 0.9, 0.95));
            rect.setStrokeWidth(10.0);
            rect.setArcWidth(15.0);
            rect.setArcHeight(15.0);

            Text text = FXGL.getUIFactoryService().newText("", 24.0);
            text.textProperty().bind(FXGL.localizedStringProperty("menu.pressAnyKey"));

            StackPane pane = new StackPane(rect, text);
            pane.setTranslateX(getAppWidth() / 2.0 - 125);
            pane.setTranslateY(getAppHeight() / 2.0 - 50);

            getContentRoot().getChildren().add(pane);
        }
    }
}
