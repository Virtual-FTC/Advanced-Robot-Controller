package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Basic: Iterative OpMode Scoop Bot", group="TeleOp")
public class BasicOpMode_Iterative_SCOOPBOT extends OpMode
{
    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor motor1, motor2, motor3, motor4, motor5, motor6, motor7, motor8, motor9;
    double leftPower, rightPower;

    /*
     * Code to run ONCE when the driver hits INIT
     */

    @Override
    public void init() {
        motor1 = this.hardwareMap.dcMotor.get("motor1");
        motor2 = this.hardwareMap.dcMotor.get("motor2");
        motor3 = this.hardwareMap.dcMotor.get("motor3");
        motor4 = this.hardwareMap.dcMotor.get("motor4");
        motor5 = this.hardwareMap.dcMotor.get("motor5");
        motor6 = this.hardwareMap.dcMotor.get("motor6");
        motor7 = this.hardwareMap.dcMotor.get("motor7");
        motor8 = this.hardwareMap.dcMotor.get("motor8");
        motor9 = this.hardwareMap.dcMotor.get("motor9");
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
        leftPower = gamepad1.left_stick_y - gamepad1.right_stick_x / 2; //MAY NEED TO REVERSE DIRECTION of STICK or Operation from "-" to "+"
        rightPower = gamepad1.left_stick_y + gamepad1.right_stick_x / 2; //MAY NEED TO REVERSE DIRECTION of STICK or Operation from "-" to "+"

        motor1.setPower(leftPower);
        motor2.setPower(rightPower);
        motor3.setPower(leftPower);
        motor4.setPower(rightPower);
        motor5.setPower(leftPower);
        motor6.setPower(rightPower);

        if(gamepad1.a) {
            motor7.setPower(1.0);
        } else if(gamepad1.b) {
            motor8.setPower(1.0);
        } else if(gamepad1.y) {
            motor9.setPower(1.0);
        } else {
            motor7.setPower(0.0);
            motor8.setPower(0.0);
            motor9.setPower(0.0);
        }
    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }

}
