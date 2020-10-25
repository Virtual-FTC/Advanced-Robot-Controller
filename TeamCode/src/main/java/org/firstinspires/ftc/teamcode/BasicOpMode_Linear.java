package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name="Basic OpMode Linear")
public class BasicOpMode_Linear extends LinearOpMode
{
    // Declare OpMode members.
    private DcMotor motor1, motor2, motor3, motor4;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void runOpMode() {

        motor1 = this.hardwareMap.dcMotor.get("motor1");
        motor2 = this.hardwareMap.dcMotor.get("motor2");
        motor3 = this.hardwareMap.dcMotor.get("motor3");
        motor4 = this.hardwareMap.dcMotor.get("motor4");
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
