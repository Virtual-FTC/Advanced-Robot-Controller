package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Basic OpMode Linear")
public class BasicOpMode_Linear extends LinearOpMode {
    // Declare OpMode members.
    private DcMotor motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void runOpMode() {

        motor1 = this.hardwareMap.dcMotor.get("frontLeft");
        motor2 = this.hardwareMap.dcMotor.get("frontRight");
        motor3 = this.hardwareMap.dcMotor.get("backLeft");
        motor4 = this.hardwareMap.dcMotor.get("backRight");
        motor5 = this.hardwareMap.dcMotor.get("intake");
        motor6 = this.hardwareMap.dcMotor.get("hopper");
        motor7 = this.hardwareMap.dcMotor.get("leftShooter");
        motor8 = this.hardwareMap.dcMotor.get("rightShooter");

//        waitForStart();
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //Square movement example program
//        motor1.setPower(-0.7);
//        motor2.setPower(0.7);
//        motor3.setPower(-0.7);
//        motor4.setPower(0.7);
        moveForwardAndTurn();
        moveForwardAndTurn();
        moveForwardAndTurn();
        moveForwardAndTurn();


        requestOpModeStop();
    }

    private void moveForwardAndTurn() {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (motor1.getCurrentPosition() < 1000 && motor2.getCurrentPosition() < 1000 && motor3.getCurrentPosition() < 1000 && motor4.getCurrentPosition() < 1000) {
            motor1.setPower(1.0);
            motor2.setPower(1.0);
            motor3.setPower(1.0);
            motor4.setPower(1.0);
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);

        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        while (motor1.getCurrentPosition() > -750 && motor2.getCurrentPosition() < 750 && motor3.getCurrentPosition() > -750 && motor4.getCurrentPosition() < 750) {
            motor1.setPower(-1.0);
            motor2.setPower(1.0);
            motor3.setPower(-1.0);
            motor4.setPower(1.0);
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);
    }
}
