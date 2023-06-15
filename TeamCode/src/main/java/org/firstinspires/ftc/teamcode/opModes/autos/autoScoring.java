package org.firstinspires.ftc.teamcode.opModes.autos;


import static org.firstinspires.ftc.robotcore.external.navigation.AngleUnit.DEGREES;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XYZ;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesOrder.XZY;
import static org.firstinspires.ftc.robotcore.external.navigation.AxesReference.EXTRINSIC;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.matrices.OpenGLMatrix;
import org.firstinspires.ftc.robotcore.external.matrices.VectorF;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackableDefaultListener;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.functions.TelemetryFunctions;
import org.firstinspires.ftc.teamcode.util.Constants;
import org.firstinspires.ftc.teamcode.util.Data.DriveData;
import org.firstinspires.ftc.teamcode.util.Data.NavData;
import org.firstinspires.ftc.teamcode.util.Data.NavImageDetectionPipeline;
import org.firstinspires.ftc.teamcode.util.Data.PIDData;
import org.firstinspires.ftc.teamcode.util.Data.TelemetryData;
import org.firstinspires.ftc.teamcode.util.Stage;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.firstinspires.ftc.teamcode.util.XRobot;

import java.util.ArrayList;

@Autonomous(name="XAutoScoring")
public class autoScoring extends LinearOpMode {


    @Override public void runOpMode() {

        XRobot.init(this.hardwareMap, this.telemetry, false);
        NavImageDetectionPipeline imagePip = new NavImageDetectionPipeline(this.hardwareMap);
        TelemetryPacket packet = new TelemetryPacket();


        XRobot.autoData.stages = List.of(
                new Stage.MoveTimed(new V2f(0, 0.3f), 5),
                new Stage.CenterOnImage(0, new V2f(0, 1000), imagePip)
        );


        waitForStart();
        while (!isStopRequested()) {

            XRobot.updateSystems(this.telemetry);
            imagePip.update();
            packet.fieldOverlay()
                    .setFill("blue")
                    .fillRect(-20, -20, 40, 40);
            XRobot.autoData.run();

            // ??????????????????????????????????????
            /*
            *           +Y       BL
            *
            *       O <- target
            * -X                 +X
            *
            *
            *  RL       -Y
            * */

        }


        imagePip.deactivate();
    }





}
