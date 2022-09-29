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

import org.firstinspires.ftc.teamcode.functions.DriveFS;
import org.firstinspires.ftc.teamcode.functions.TelemetryFS;
import org.firstinspires.ftc.teamcode.util.v2f;
import org.firstinspires.ftc.teamcode.util.BotState;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="FTC 2022 TeleOp", group="Linear Opmode")
public class TeleOp extends LinearOpMode {

    BotState botState = null;

    @Override
    public void runOpMode() {

        this.botState = new BotState(this.hardwareMap);
        float prevTime = 0;


        this.telemetry.setCaptionValueSeparator(" ");
        telemetry.addData("PRG state", "Initialized");
        telemetry.update();



        while (opModeIsActive()) {

            //test this first plz, not sure what unit the output from time.time() is
            float dt = (float)this.botState.time.seconds() - prevTime;
            prevTime = (float)this.botState.time.seconds();



            float LX = this.gamepad1.left_stick_x;
            float LY = this.gamepad1.left_stick_y;
            float RX = this.gamepad1.right_stick_x;
            float RY = this.gamepad1.right_stick_y;



            DriveFS.updateDrive(this.botState, dt, new v2f(LX, LY), new v2f(RX, RY));
            //DriveFS.setPower(this.botState, new v2f(LX, LY), RX);


            //TELEMETRY ===============================================================
            this.telemetry.addData("PRG state ", "Running");
            this.telemetry.addData("",
                    TelemetryFS.botState("Bot State", this.botState).getString());
            telemetry.update();
            //=========================================================================
        }
    }
}
