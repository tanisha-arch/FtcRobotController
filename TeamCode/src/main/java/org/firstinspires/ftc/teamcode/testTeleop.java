package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "Test TELEOP", group = "Linear OpMode")
public class testTeleop extends LinearOpMode {

    // Hardware Declarations
    private DcMotor slurpItIntakeMotor = null;
    private DcMotor transferMotor = null;
    private DcMotor flyWheels = null;
    private Servo gateServo = null;
    private Servo flagPole = null;

    // Intake Power & Timer Settings
    private static final double INTAKE_POWER = 0.85;
    private static final double INTAKE_RUN_TIME_SECONDS = 20.0;
    private final ElapsedTime intakeTimer = new ElapsedTime();
    private boolean isIntakeRunning = false;

    // Gate & Transfer Timing & Servo Positions
    private static final double gardenClosed = 0.00;
    private static final double gardenOpen = 1.00;
    private static final double CLOSED_DURATION = 3.0; // Stay closed for 3.0 seconds
    private static final double OPEN_DURATION = 3.0;   // Stay open for 3.0 seconds
    private static final double TRANSFER_SPEED = 0.85;

    public enum GateState {
        CLOSED,
        OPEN
    }

    private GateState gateState = GateState.CLOSED;
    private final ElapsedTime gateTimer = new ElapsedTime();

    // Timers & Speeds for Buttons B & X
    private final ElapsedTime runBackTimer = new ElapsedTime();
    private static final double runBackRunTime = 1.50;
    private boolean isRunBackTimerRunning = false;

    private final ElapsedTime fasterSpeedTimer = new ElapsedTime();
    private boolean isFasterSpeedTimerRunning = false;

    @Override
    public void runOpMode() {

        telemetry.addData("intake teleop brochacho", "plz wait cuh");
        telemetry.update();

        // 1. Hardware Mapping
        slurpItIntakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        flyWheels = hardwareMap.get(DcMotor.class, "flyWheels");
        gateServo = hardwareMap.get(Servo.class, "gateServo");
        flagPole = hardwareMap.get(Servo.class, "flagPole");

        // 2. Motor Directions & Braking Settings
        slurpItIntakeMotor.setDirection(DcMotor.Direction.FORWARD);
        slurpItIntakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        transferMotor.setDirection(DcMotor.Direction.FORWARD);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flyWheels.setDirection(DcMotor.Direction.FORWARD);
        flyWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("LETSGO EVERYTHINGS READY HIT DA PLAY", "vroom vroom OU SHI!");
        telemetry.update();

        // Wait for driver to press PLAY
        waitForStart();
        gateTimer.reset();

        // TeleOp loop - runs continuously while match is active
        while (opModeIsActive()) {

            // --- INTAKE MOTOR CONTROL (BUTTON A) ---
            if (gamepad1.a) {
                intakeTimer.reset(); // Reset timer when button A is pressed
                isIntakeRunning = true;
            }

            // Stop intake once the target time limit has passed
            if (isIntakeRunning && intakeTimer.seconds() >= INTAKE_RUN_TIME_SECONDS) {
                isIntakeRunning = false;
            }

            // Set motor power based on intake state
            if (isIntakeRunning) {
                slurpItIntakeMotor.setPower(INTAKE_POWER);
            } else if (!isRunBackTimerRunning && !isFasterSpeedTimerRunning) {
                slurpItIntakeMotor.setPower(0.0);
            }

            // --- RUN BACK CONTROL (BUTTON B) ---
            if (gamepad1.b && !isRunBackTimerRunning) {
                runBackTimer.reset();
                isRunBackTimerRunning = true;
            }

            if (isRunBackTimerRunning) {
                if (runBackTimer.seconds() < runBackRunTime) {
                    slurpItIntakeMotor.setPower(-0.25);
                    flyWheels.setPower(0.48);
                } else {
                    slurpItIntakeMotor.setPower(0.6);
                    flyWheels.setPower(0.48);
                }
            }

            // --- FASTER SPEED CONTROL (BUTTON X) ---
            if (gamepad1.x && !isFasterSpeedTimerRunning) {
                fasterSpeedTimer.reset();
                isFasterSpeedTimerRunning = true;
            }

            if (isFasterSpeedTimerRunning) {
                if (fasterSpeedTimer.seconds() < runBackRunTime) {
                    slurpItIntakeMotor.setPower(-0.25);
                    flyWheels.setPower(0.55);
                } else {
                    slurpItIntakeMotor.setPower(0.6);
                    flyWheels.setPower(0.55);
                }
            }

            // Turn off flywheels when neither timer sequence is running
            if (!isRunBackTimerRunning && !isFasterSpeedTimerRunning) {
                flyWheels.setPower(0.0);
            }

            // --- GATE STATE MACHINE ---
            switch (gateState) {

                case CLOSED:
                    // Set servo to closed position & stop transfer motor
                    gateServo.setPosition(gardenClosed);
                    transferMotor.setPower(0.0);

                    // Wait for 3.0 seconds, then switch to OPEN
                    if (gateTimer.seconds() >= CLOSED_DURATION) {
                        gateState = GateState.OPEN;
                        gateTimer.reset();
                    }
                    break;

                case OPEN:
                    // Set servo to open position & run transfer motor
                    gateServo.setPosition(gardenOpen);
                    transferMotor.setPower(TRANSFER_SPEED);

                    // Wait for 3.0 seconds, then switch back to CLOSED
                    if (gateTimer.seconds() >= OPEN_DURATION) {
                        gateState = GateState.CLOSED;
                        gateTimer.reset();
                    }
                    break;
            }

            // --- TELEMETRY ---
            telemetry.addData("Intake Active", isIntakeRunning);
            if (isIntakeRunning) {
                telemetry.addData("Intake Time Remaining", "%.1f sec", INTAKE_RUN_TIME_SECONDS - intakeTimer.seconds());
            }
            telemetry.addData("Gate State", gateState);
            telemetry.addData("State Time Remaining", "%.1f sec",
                    (gateState == GateState.CLOSED ? CLOSED_DURATION : OPEN_DURATION) - gateTimer.seconds());
            telemetry.update();
        }
    }
}
