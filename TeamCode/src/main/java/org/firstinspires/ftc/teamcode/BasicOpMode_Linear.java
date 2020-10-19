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
    private DcMotorA motor1, motor2, motor3, motor4;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void runOpMode() {

        motor1 = new DcMotorA("motor1");//this.hardwareMap.dcMotor.get("motor1");
        motor2 = new DcMotorA("motor2");//this.hardwareMap.dcMotor.get("motor1");
        motor3 = new DcMotorA("motor3");//this.hardwareMap.dcMotor.get("motor1");
        motor4 = new DcMotorA("motor4");//this.hardwareMap.dcMotor.get("motor1");
//        waitForStart();

        for(int i = 0; i < 4; i++) {
            long startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + 1000) {
                //wait for time to be over
            }

            motor1.setPower(1.0);
            motor2.setPower(1.0);
            motor3.setPower(1.0);
            motor4.setPower(1.0);

            startTime = System.currentTimeMillis();
            while (System.currentTimeMillis() < startTime + 1200) {
                //wait for time to be over
            }

            motor1.setPower(1.0);
            motor2.setPower(-1.0);
            motor3.setPower(1.0);
            motor4.setPower(-1.0);
        }

        requestOpModeStop();
    }
}
