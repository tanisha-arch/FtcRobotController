package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Test TELEOP", group = "Linear OpMode")
public class testTeleop extends LinearOpMode {

    private DcMotor slurpItIntakeMotor = null;

    private static final double INTAKE_POWER = 0.85;   // Power to pull game elements in
    private static final double INTAKE_RUN_TIME_SECONDS = 20.0; // Duration to run intake (in seconds)
    private final ElapsedTime intakeTimer = new ElapsedTime();
    private boolean isIntakeRunning = false;

    @Override
    public void runOpMode() {
        telemetry.addData("intake teleop brochacho", "plz wait cuh");
        telemetry.update();

        slurpItIntakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        slurpItIntakeMotor.setDirection(DcMotor.Direction.FORWARD);
        slurpItIntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        telemetry.addData("LETSGO EVERYTHINGS READY HIT DA PLAY", "vroom vroom OU SHI!");
        telemetry.update();

        // Wait for driver to press PLAY
        waitForStart();

        // TeleOp loop - runs continuously while match is active
        while (opModeIsActive()) {

            if (gamepad1.a) {
                intakeTimer.reset(); // Reset timer to 0 seconds and triggers it when a gets pressed
                isIntakeRunning = true;
            }

            // Stop intake once the target time limit has passed
            if (isIntakeRunning && intakeTimer.seconds() >= INTAKE_RUN_TIME_SECONDS) {
                isIntakeRunning = false;
            }

            // Set motor power based on intake state
            if (isIntakeRunning) {
                slurpItIntakeMotor.setPower(INTAKE_POWER);
            } else {
                slurpItIntakeMotor.setPower(0.0);
            }

// motor power and time remaining
            telemetry.addData("Intake Active", isIntakeRunning);
            if (isIntakeRunning) {
                telemetry.addData("Time Remaining", "%.1f sec", INTAKE_RUN_TIME_SECONDS - intakeTimer.seconds());
            }
            telemetry.addData("Motor Power", "%.2f", slurpItIntakeMotor.getPower());
            telemetry.update();
        }
    }
}
