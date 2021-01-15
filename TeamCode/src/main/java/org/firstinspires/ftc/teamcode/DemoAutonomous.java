package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

@Autonomous(name = "Demo Autonomous")
public class DemoAutonomous extends LinearOpMode {
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

        /** Move Forward With Wobble Goal and Push Into Zone **/

        moveWithEncoders(5500);

        strafeWithEncoders(1000);

        moveWithEncoders(1250);

        strafeWithEncoders(-1900);

        moveWithEncoders(-4000);

        strafeWithEncoders(1100);

        launchRings();

        strafeWithEncoders(150);

        launchRings();

        strafeWithEncoders(150);

        launchRings();

        moveWithEncoders(200);

        strafeWithEncoders(-1500);

        motor5.setPower(1.0);
        moveWithEncodersAndPower(-600, -0.3);
        moveWithEncodersAndPower(-600, -0.3);
        moveWithEncodersAndPower(-600, -0.3);
        motor5.setPower(0.0);

        moveWithEncoders(1300);

        launchRings();
        launchRings();
        launchRings();


        moveWithEncoders(-3000);
        moveWithEncodersAndPower(25, 0.3);

        strafeWithEncoders(-800);
        moveWithEncoders(5500);

        strafeWithEncoders(1000);

        moveWithEncoders(1250);

        moveWithEncodersAndPower(-15, -0.3);


        strafeWithEncoders(-900);

        strafeWithEncoders(600);

        moveWithEncoders(-3750);

        launchRings();
        launchRings();
        launchRings();

        moveWithEncoders(1000);

        requestOpModeStop();
    }

    private void moveWithEncoders(double encPosition) {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(encPosition > 0) {
            while (motor1.getCurrentPosition() < encPosition && motor2.getCurrentPosition() < encPosition && motor3.getCurrentPosition() < encPosition && motor4.getCurrentPosition() < encPosition) {
                motor1.setPower(1.0);
                motor2.setPower(1.0);
                motor3.setPower(1.0);
                motor4.setPower(1.0);
            }
        } else {
            while (motor1.getCurrentPosition() > encPosition && motor2.getCurrentPosition() > encPosition && motor3.getCurrentPosition() > encPosition && motor4.getCurrentPosition() > encPosition) {
                motor1.setPower(-1.0);
                motor2.setPower(-1.0);
                motor3.setPower(-1.0);
                motor4.setPower(-1.0);
            }
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);
    }

    private void moveWithEncodersAndPower(double encPosition, double power) {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(encPosition > 0) {
            while (motor1.getCurrentPosition() < encPosition && motor2.getCurrentPosition() < encPosition && motor3.getCurrentPosition() < encPosition && motor4.getCurrentPosition() < encPosition) {
                motor1.setPower(power);
                motor2.setPower(power);
                motor3.setPower(power);
                motor4.setPower(power);
            }
        } else {
            while (motor1.getCurrentPosition() > encPosition && motor2.getCurrentPosition() > encPosition && motor3.getCurrentPosition() > encPosition && motor4.getCurrentPosition() > encPosition) {
                motor1.setPower(power);
                motor2.setPower(power);
                motor3.setPower(power);
                motor4.setPower(power);
            }
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);
    }

    private void strafeWithEncoders(double encPosition) {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(encPosition > 0) {
            while (motor1.getCurrentPosition() < encPosition && motor2.getCurrentPosition() > -encPosition && motor3.getCurrentPosition() < encPosition && motor4.getCurrentPosition() > -encPosition) {
                motor1.setPower(1.0);
                motor2.setPower(-1.0);
                motor3.setPower(-1.0);
                motor4.setPower(1.0);
            }
        } else {
            while (motor1.getCurrentPosition() > encPosition && motor2.getCurrentPosition() < -encPosition && motor3.getCurrentPosition() > encPosition && motor4.getCurrentPosition() < -encPosition) {
                motor1.setPower(-1.0);
                motor2.setPower(1.0);
                motor3.setPower(1.0);
                motor4.setPower(-1.0);
            }
        }
        motor1.setPower(0.0);
        motor2.setPower(0.0);
        motor3.setPower(0.0);
        motor4.setPower(0.0);
    }

    private void turnWithEncoders(double encPosition) {
        motor1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor3.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor4.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        if(encPosition > 0) {
            while (motor1.getCurrentPosition() > -encPosition && motor2.getCurrentPosition() < encPosition && motor3.getCurrentPosition() > -encPosition && motor4.getCurrentPosition() < encPosition) {
                motor1.setPower(-1.0);
                motor2.setPower(1.0);
                motor3.setPower(-1.0);
                motor4.setPower(1.0);
            }
            motor1.setPower(0.0);
            motor2.setPower(0.0);
            motor3.setPower(0.0);
            motor4.setPower(0.0);
        } else if(encPosition < 0) {
            while (motor1.getCurrentPosition() < -encPosition && motor2.getCurrentPosition() > encPosition && motor3.getCurrentPosition() < -encPosition && motor4.getCurrentPosition() > encPosition) {
                motor1.setPower(1.0);
                motor2.setPower(-1.0);
                motor3.setPower(1.0);
                motor4.setPower(-1.0);
            }
            motor1.setPower(0.0);
            motor2.setPower(0.0);
            motor3.setPower(0.0);
            motor4.setPower(0.0);
        }
    }

    private void launchRings() {
        motor7.setPower(1.0);
        motor8.setPower(1.0);
        pauseForMillis(500);
        motor6.setPower(1.0);
        pauseForMillis(500);
        motor7.setPower(0.0);
        motor8.setPower(0.0);
        motor6.setPower(0.0);
    }

    private void pauseForMillis(long millis) {
        long startTime = System.currentTimeMillis();
        while(System.currentTimeMillis() < startTime + millis) {
            //wait for time to finish
        }
    }
}
