package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@com.qualcomm.robotcore.eventloop.opmode.TeleOp(name="Demo Teleop", group="TeleOp")
public class DemoTeleOp extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8;
    double fL_power, fR_power, bL_power, bR_power;

    double launcherPower = 0.8;

    boolean wantsToIncreaseShooterPower = false;
    boolean wantsToDecreaseShooterPower = false;


    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {
        motor1 = this.hardwareMap.dcMotor.get("frontLeft");
        motor2 = this.hardwareMap.dcMotor.get("frontRight");
        motor3 = this.hardwareMap.dcMotor.get("backLeft");
        motor4 = this.hardwareMap.dcMotor.get("backRight");
        motor5 = this.hardwareMap.dcMotor.get("ringCollection");
        motor6 = this.hardwareMap.dcMotor.get("ringLoader");
        motor7 = this.hardwareMap.dcMotor.get("ringShooter");
        motor8 = this.hardwareMap.dcMotor.get("wobbleActuator");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        runtime.reset();
    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        fL_power = Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y) * Math.cos(Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4) - gamepad1.right_stick_x / 2;
        fR_power = Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y) * Math.sin(Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4) + gamepad1.right_stick_x / 2;
        bL_power = Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y) * Math.sin(Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4) - gamepad1.right_stick_x / 2;
        bR_power = Math.hypot(gamepad1.left_stick_x, -gamepad1.left_stick_y) * Math.cos(Math.atan2(gamepad1.left_stick_y, -gamepad1.left_stick_x) - Math.PI / 4) + gamepad1.right_stick_x / 2;

        motor1.setPower(-fL_power);
        motor2.setPower(-fR_power);
        motor3.setPower(-bL_power);
        motor4.setPower(-bR_power);

        if(gamepad1.a) {
            motor5.setPower(1.0);
        } else {
            motor5.setPower(0.0);
        }

        if(gamepad1.left_trigger > 0) {
            motor6.setPower(1.0);
        } else {
            motor6.setPower(0.0);
        }

        if(gamepad1.dpad_up && launcherPower < 1.0) {
            wantsToIncreaseShooterPower = true;
        } else if(gamepad1.dpad_down && launcherPower > 0.0) {
            wantsToDecreaseShooterPower = true;
        }

        if(wantsToIncreaseShooterPower && !gamepad1.dpad_up) {
            launcherPower += 0.01;
            wantsToIncreaseShooterPower = false;
        } else if(wantsToDecreaseShooterPower && !gamepad1.dpad_down) {
            launcherPower -= 0.01;
            wantsToDecreaseShooterPower = false;
        }


        if(gamepad1.left_bumper) {
            motor8.setPower(0.1);
        } else if(gamepad1.right_bumper) {
            motor8.setPower(1.0);
        } else {
            motor8.setPower(0.0);
        }

        if(gamepad1.right_trigger > 0) {
            motor7.setPower(launcherPower);
        } else {
            motor7.setPower(0.0);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
