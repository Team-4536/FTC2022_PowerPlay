/* Copyright (c) 2017 FIRST. All rights reserved.
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

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.functions.DriveFNS;
import org.firstinspires.ftc.teamcode.functions.NavFNS;
import org.firstinspires.ftc.teamcode.functions.TelemetryFNS;
import org.firstinspires.ftc.teamcode.util.Hardware;
import org.firstinspires.ftc.teamcode.util.PIDData;
import org.firstinspires.ftc.teamcode.util.TelemetryData;
import org.firstinspires.ftc.teamcode.util.V2f;
import org.firstinspires.ftc.teamcode.util.V3d;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="FTC 2022 TeleOp", group="Linear Opmode")
public class TeleOp extends LinearOpMode {

    @Override
    public void runOpMode() {

        //region// Multi-Frame variables ____________________________
        this.telemetry.setCaptionValueSeparator("");
        Hardware hardware = new Hardware(this.hardwareMap);
        double prevTime = 0;

        //DRIVE
        PIDData driveTurningPID = new PIDData();
        driveTurningPID.Kd = -0.2f;
        driveTurningPID.Ki = 0.0f;
        driveTurningPID.Kp = 0.018f;

        //NAV
        V3d PosAccumulator = new V3d();
        V3d VelAccumulator = new V3d();

        //UPDATE INFO
        int updateCount = 0;
        int displayUpdateCount = 0;
        double dtAcc = 0;
        //endregion _________________________________________________



        //wait for start
        telemetry.addData("PRG state", "Initialized");
        telemetry.update();
        this.waitForStart();

        while (opModeIsActive()) {

            //region// Frame specific variables _________________________________________

            double dt = hardware.time.seconds() - prevTime;
            prevTime = hardware.time.seconds();


            V2f l = new V2f(this.gamepad1.left_stick_x, this.gamepad1.left_stick_y);
            V2f r = new V2f(this.gamepad1.right_stick_x, this.gamepad1.right_stick_y);

            TelemetryData telemetryData = new TelemetryData("State");

            //endregion ________________________________________________________________




            dtAcc += dt;
            updateCount++;
            if(dtAcc > 1){
                dtAcc = 0;
                displayUpdateCount = updateCount;
                updateCount = 0;
            }
            telemetryData.addChild("Updates/Sec", displayUpdateCount);



            NavFNS.updateNav(hardware, telemetryData, dt, VelAccumulator, PosAccumulator);


            //set bot drive motors for direction,
            // update PID for turning,
            // send debug telemetry
            DriveFNS.updateDrive(hardware, telemetryData, (float)dt, driveTurningPID, l,
                    new V2f(r.x, r.y)
            );




            //add hardware telemetry
            telemetryData.addChild(TelemetryFNS.hardware("Hardware", hardware));



            //update telemetry
            this.telemetry.addData("", telemetryData.getString());
            telemetry.update();
        }
    }


}
