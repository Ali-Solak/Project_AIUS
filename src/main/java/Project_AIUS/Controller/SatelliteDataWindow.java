package  Project_AIUS.Controller;

import  Project_AIUS.Model.SatelliteData;
import  Project_AIUS.Service.InputOutput;
import  Project_AIUS.Service.InternetSpeedData;
import  Project_AIUS.Service.WifiSignalAdder;
import  Project_AIUS.View.ViewFactory;
import com.jfoenix.controls.JFXButton;
import eu.hansolo.tilesfx.Demo;
import eu.hansolo.tilesfx.Tile;
import eu.hansolo.tilesfx.TileBuilder;
import eu.hansolo.tilesfx.chart.ChartData;
import eu.hansolo.tilesfx.chart.RadarChartMode;
import eu.hansolo.tilesfx.chart.TilesFXSeries;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.XYChart;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.text.TextAlignment;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class SatelliteDataWindow extends BaseController implements Initializable {

    @FXML private Tile gaugeTile;
    @FXML private Tile smoothChartTile;
    @FXML private Tile radarChart;
    @FXML private JFXButton home;
    @FXML private JFXButton browser;
    @FXML private JFXButton message;
    @FXML private JFXButton satellite;
    @FXML private JFXButton settings;
    @FXML private ImageView image;
    @FXML private AnchorPane mainWindow;
    @FXML private Tile imageTile;
    @FXML private ImageView imageView;

    private ChartData chartData1;
    private ChartData chartData2;
    private ChartData chartData3;
    private ChartData chartData4;
    private ChartData chartData5;
    private ChartData chartData6;
    private ScheduledExecutorService scheduledExecutorService;

    private SatelliteData satelliteData;
    private InputOutput inputOutput;
    private XYChart.Series<String, Number> series1;
    private InternetSpeedData internetSpeedData;
    private WifiSignalAdder wifiSignalAdder;

    public SatelliteDataWindow(ViewFactory viewFactory, String fxmlName) {
        super(viewFactory, fxmlName);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        internetSpeedData = new InternetSpeedData();
        imageView.setLayoutX(1250);
        wifiSignalAdder = new WifiSignalAdder();
        wifiSignalAdder.ajustData(imageView);

        home.setOnAction(Event -> openHome());
        browser.setOnAction(Event -> openBrowser());
        message.setOnAction(Event -> openMessage());

        satelliteData = new SatelliteData();
        inputOutput = new InputOutput();
        series1 = new XYChart.Series();


        setupCharts();
        ajustData();
    }

    /**
     * Uses ScheduledExecuter to periodically check data and update Charts.
     */
    public void ajustData(){

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            long data = internetSpeedData.readInternetSpeed(internetSpeedData.getNet());

            Platform.runLater(() -> {

                Date now = new Date();

                if (series1.getData().size() > 20) {
                    series1.getData().remove(0);
                }

                gaugeTile.setValue(data);

                series1.setName("Kb");
                series1.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), data));
            });
        }, 0, 3, TimeUnit.SECONDS);
    }

    public void setupCharts(){

        chartData1 = new ChartData("1", 0, Tile.MAGENTA);
        chartData2 = new ChartData("2", 0, Tile.BLUE);
        chartData3 = new ChartData("3", 0, Tile.RED);
        chartData4 = new ChartData("4", 0, Tile.YELLOW_ORANGE);
        chartData5 = new ChartData("5", 0, Tile.BLUE);
        chartData6 = new ChartData("6", 0, Tile.BLUE);


        gaugeTile = TileBuilder.create()
                .skinType(Tile.SkinType.GAUGE)
                .minValue(0)
                .maxValue(2000)
                .prefSize(350, 210)
                .barColor(Color.rgb(168,104,160))
                .backgroundColor(Color.rgb(20,17,36))
                .borderColor(Color.rgb(168,104,160))
                .thresholdColor(Color.rgb(168,104,160))

                .borderWidth(1)
                .title("Current Signal Power")
                .unit("kb")
                .threshold(1000)
                .build();

        gaugeTile.setLayoutX(530);
        gaugeTile.setLayoutY(120);


        smoothChartTile = TileBuilder.create().skinType(Tile.SkinType.SMOOTHED_CHART)
                .prefSize(800, 400)
                .minValue(1)
                .maxValue(1000)
                .title("Signal")
                .unit("Mb")
                .chartType(Tile.ChartType.AREA)
                .smoothing(true)
                .tooltipTimeout(1000)
                .tilesFxSeries(new TilesFXSeries<>(series1,
                        Tile.MAGENTA,
                        new LinearGradient(0, 0, 0, 1,
                                true, CycleMethod.NO_CYCLE,
                                new Stop(0, Tile.MAGENTA),
                                new Stop(1, Color.TRANSPARENT))))

                .backgroundColor(Color.rgb(20,17,36))
                .borderColor(Color.rgb(168,104,160))
                .thresholdColor(Color.rgb(168,104,160))
                .knobColor(Color.rgb(168,104,160))
                .barColor(Color.rgb(168,104,160))
                .needleColor(Color.rgb(168,104,160))

                .animated(true)
                .build();

        smoothChartTile.setLayoutX(300);
        smoothChartTile.setLayoutY(360);

        /*
        radarChart = TileBuilder.create().skinType(Tile.SkinType.RADAR_CHART)
                .prefSize(800, 400)
                .minValue(1)
                .maxValue(5)
                .title("Signal Sector")
                .unit("Signal")
                .radarChartMode(RadarChartMode.SECTOR)
                .gradientStops(new Stop(0.00000, Color.TRANSPARENT),
                        new Stop(0.00001, Color.web("#3552a0")),
                        new Stop(0.09090, Color.web("#456acf")),
                        new Stop(0.27272, Color.web("#45a1cf")),
                        new Stop(0.36363, Color.web("#30c8c9")),
                        new Stop(0.45454, Color.web("#30c9af")),
                        new Stop(0.50909, Color.web("#56d483")),
                        new Stop(0.72727, Color.web("#9adb49")),
                        new Stop(0.81818, Color.web("#efd750")),
                        new Stop(0.90909, Color.web("#ef9850")),
                        new Stop(1.00000, Color.web("#ef6050")))

                .backgroundColor(Color.rgb(20,17,36))
                .borderColor(Color.rgb(168,104,160))
                .thresholdColor(Color.rgb(168,104,160))
                .knobColor(Color.rgb(168,104,160))
                .barColor(Color.rgb(168,104,160))
                .needleColor(Color.rgb(168,104,160))
                .chartData(chartData1, chartData2, chartData3, chartData4,
                        chartData5, chartData6)
                .tooltipText("")
                .animated(true)
                .build();

        radarChart.setLayoutX(300);
        radarChart.setLayoutY(360);

         */

        mainWindow.getChildren().addAll(gaugeTile,smoothChartTile);
    }

    public void closeThread(ScheduledExecutorService executorService){
        executorService.shutdown();
        try {
            executorService.awaitTermination(60, TimeUnit.SECONDS);
        } catch (Exception e) {
        }

    }

    public ScheduledExecutorService getScheduledExecutorService() {
        return scheduledExecutorService;
    }

    public void openHome(){
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        closeThread(getScheduledExecutorService());
        viewFactory.openMainWindow();
    }
    public void openBrowser(){
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        closeThread(getScheduledExecutorService());
        viewFactory.openBrowserWindow();
    }

    public void openMessage() {
        wifiSignalAdder.closeThread(wifiSignalAdder.getScheduledExecutorService());
        closeThread(getScheduledExecutorService());
        viewFactory.openBlackboardWindow();
    }
}
