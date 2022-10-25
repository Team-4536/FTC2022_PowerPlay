/* Copyright (c) 2019 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.opModes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import java.util.List;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.functions.DriveFunctions;
import org.firstinspires.ftc.teamcode.functions.NavFunctions;
import org.firstinspires.ftc.teamcode.functions.PIDFunctions;
import org.firstinspires.ftc.teamcode.util.Data.DynamicData;
import org.firstinspires.ftc.teamcode.util.Data.ObjectDetectionData;
import org.firstinspires.ftc.teamcode.util.PID.PIDSettings;
import org.firstinspires.ftc.teamcode.util.Data.StaticData;
import org.firstinspires.ftc.teamcode.util.V2f;

/**
 * This 2022-2023 OpMode illustrates the basics of using the TensorFlow Object Detection API to
 * determine which image is being presented to the robot.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list.
 *
 * IMPORTANT: In order to use this OpMode, you need to obtain your own Vuforia license key as
 * is explained below.
 */
@Autonomous(name = "Autonomous Team made Object Detection", group = "Concept")

/*
      all lables:
      "1 Bolt",
      "2 Bulb",
      "3 Panel"
 */
public class ObjectDetectionTeamMadeObjects extends LinearOpMode {

    @Override
    public void runOpMode(){

        StaticData s = new StaticData(
                this.hardwareMap,
                new PIDSettings(0.018f, 0.0f, -0.5f),
                new boolean[] { false, true, false, true } );

        DynamicData d = new DynamicData();
        ObjectDetectionData detector = new ObjectDetectionData(this.hardwareMap);


        telemetry.addData("PRG state", "Initialized");
        telemetry.update();
        this.waitForStart();


        while (opModeIsActive()) {
            NavFunctions.updateHeading(s,d);
            NavFunctions.updateDt(s, d);
            float x = PIDFunctions.updatePIDAngular(s.drivePIDSettings, d.drivePID, d.heading, (float) d.dt);

            List<Recognition> updatedRecognitions = detector.tfod.getUpdatedRecognitions();
            if(updatedRecognitions != null){

                for (Recognition recognition : updatedRecognitions) {
                    /*if(recognition.getLabel().equals("1 Bolt")){
                        telemetry.addData("Object Detected", "Bolt");
                        DriveFunctions.setPower(s, new V2f(-1,0), x);
                    }
                    if(recognition.getLabel().equals("2 Bulb")){
                        telemetry.addData("Object Detected", "Bulb");
                        DriveFunctions.setPower(s, new V2f(0,-1), x);
                    }
                    if(recognition.getLabel().equals("3 Panel")){
                        telemetry.addData("Object Detected", "Panel");
                        DriveFunctions.setPower(s, new V2f(1,0), x);
                    }*/
                    telemetry.addData("Object Detected", recognition.getLabel());
                }


            }
            else{
                DriveFunctions.setPower(s, new V2f(0,0), x);
            }
            telemetry.update();


        }
    }
}
