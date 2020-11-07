package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic: Iterative OpMode Shooter Bot", group="TeleOp")
public class BasicOpMode_Iterative_SHOOTERBOT extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8;
    double fL_power, fR_power, bL_power, bR_power;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {
        motor1 = this.hardwareMap.dcMotor.get("frontLeft");
        motor2 = this.hardwareMap.dcMotor.get("frontRight");
        motor3 = this.hardwareMap.dcMotor.get("backLeft");
        motor4 = this.hardwareMap.dcMotor.get("backRight");
        motor5 = this.hardwareMap.dcMotor.get("intake");
        motor6 = this.hardwareMap.dcMotor.get("hopper");
        motor7 = this.hardwareMap.dcMotor.get("leftShooter");
        motor8 = this.hardwareMap.dcMotor.get("rightShooter");
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
        fL_power = (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)) - Math.PI / 4)) - gamepad1.right_stick_x / 2;
        fR_power = (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)) - Math.PI / 4)) + gamepad1.right_stick_x / 2;
        bL_power = (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * (Math.sin(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)) - Math.PI / 4)) - gamepad1.right_stick_x / 2;
        bR_power = (Math.hypot(gamepad1.left_stick_x, gamepad1.left_stick_y) * (Math.cos(Math.atan2(gamepad1.left_stick_y, gamepad1.left_stick_x)) - Math.PI / 4)) + gamepad1.right_stick_x / 2;

        motor1.setPower(-fL_power);
        motor2.setPower(-fR_power);
        motor3.setPower(-bL_power);
        motor4.setPower(-bR_power);

        if(gamepad1.a) {
            motor5.setPower(1.0);
        } else if(gamepad1.b) {
            motor6.setPower(1.0);
        } else if(gamepad1.y) {
            motor7.setPower(1.0);
            motor8.setPower(-1.0);
        } else {
            motor5.setPower(0.0);
            motor6.setPower(0.0);
            motor7.setPower(0.0);
            motor8.setPower(0.0);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
