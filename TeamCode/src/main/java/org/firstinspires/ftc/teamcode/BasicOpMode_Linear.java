package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorA;
import com.qualcomm.robotcore.util.ElapsedTime;

import javax.sound.sampled.Line;

@Autonomous(name="Basic OpMode Linear")
public class BasicOpMode_Linear extends LinearOpMode
{
    // Declare OpMode members.
    private DcMotorA leftDrive;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void runOpMode() {
        leftDrive = new DcMotorA();
//        waitForStart();

        for(int i = 0; i < 4; i++) {
            leftDrive.setLinVel(1.0, 0);

            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + 1000) {
                //wait for time to be over
            }

            leftDrive.setLinVel(0, 0);
            leftDrive.setTurn(1.0);

            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + 1200) {
                //wait for time to be over
            }

            leftDrive.setLinVel(0, 0);
            leftDrive.setTurn(0);
        }

        requestOpModeStop();
    }
}
