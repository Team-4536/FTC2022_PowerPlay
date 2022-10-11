package org.firstinspires.ftc.teamcode.util;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class ObjectDetection {
    private static final String TFOD_MODEL_ASSET = "PowerPlay.tflite";


    private static final String[] LABELS = {
            "1 Bolt",
            "2 Bulb",
            "3 Panel"
    };


    private static final String VUFORIA_KEY = "ARtx29j/////AAABmc8Mmozqr0GsgPsH/NOLUkBPcj0u8eyHkeiAyftu6QAtlUKWRkZ5UPmI6mCAXfVgmJHq7dPpxL1kd5rCK4YO8ABWIbqITG6W6Kp8LqKWJnRWOVIBoOxtZsSkfvOu39s2NKVKbmNshfLUTkNS8Xs6NLOJS2mAHITqOPgR+68DhqEFQIvT1HRBplwsEeuWFVeiizCgmp3sBHZMWdNiCN0KS1AZLbF/+352OGpIkhFGPuMYSb7VdjLVUPRc3Pl7qoOISjVpIweyRMIrc/jkj3bEBULFbKjPyBa+sAwXnxiBxoFDVB7Fo2S5NsU6pW1w67wqYyHM8R1/JLEQJqWTFf+hrnLnPN+aew4/+C3q1XP14fBg";


    private VuforiaLocalizer vuforia;


    private TFObjectDetector tfod;


    public ObjectDetection(TelemetryData telemetry){
        initVuforia();
        initTfod();


        if (tfod != null) {
            tfod.activate();

            tfod.setZoom(1.0, 16.0/9.0);
        }





    }





    private void initVuforia() {
        /*
         * Configure Vuforia by creating a Parameter object, and passing it to the Vuforia engine.
         */
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        // parameters.cameraDirection = CameraDirection.BACK;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }


    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

        // Use loadModelFromAsset() if the TF Model is built in as an asset by Android Studio
        // Use loadModelFromFile() if you have downloaded a custom team model to the Robot Controller's FLASH.
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
        // tfod.loadModelFromFile(TFOD_MODEL_FILE, LABELS);
    }
}
