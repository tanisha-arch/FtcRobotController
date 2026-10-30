package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robot.RobotState;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.openftc.easyopencv.PipelineRecordingParameters;

import java.util.Base64;

public class testTeleop2 extends LinearOpMode {
    private DcMotorEx frontLeft = null;
    private DcMotorEx backLeft = null;
    private DcMotorEx frontRight = null;
    private DcMotorEx backRight = null;

    // --- OTHER MECHANISM HARDWARE ---
    private DcMotorEx slurpItIntakeMotor = null;
    private DcMotorEx transferMotor = null;
    private DcMotorEx flyWheels = null;
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

    public enum RobotState {
        IDLE,
        DEFENSE,
        INTAKESTORE,
        SHOOTING,

    }

    RobotState currentState = RobotState.IDLE;

    private testTeleop.GateState gateState = testTeleop.GateState.CLOSED;
    private final ElapsedTime gateTimer = new ElapsedTime();

    // Timers & Speeds for Buttons B & X
    private final ElapsedTime runBackTimer = new ElapsedTime();
    private static final double runBackRunTime = 1.50; // 1.5 seconds reverse, 1.5 seconds forward
    private boolean isRunBackTimerRunning = false;

    private final ElapsedTime fasterSpeedTimer = new ElapsedTime();
    private boolean isFasterSpeedTimerRunning = false;
    private boolean isINTAKESTOREStateRunning = false;
    private boolean isSHOOTERStateRunning = false;
    private boolean isDEFENSEStateRunning = false;

    @Override
    public void runOpMode() {

        telemetry.addData("intake teleop brochacho", "plz wait cuh");
        telemetry.update();

        // 1. Hardware Mapping - Mecanum Drivetrain
        frontLeft = hardwareMap.get(DcMotorEx.class, "frontLeft");
        backLeft = hardwareMap.get(DcMotorEx.class, "backLeft");
        frontRight = hardwareMap.get(DcMotorEx.class, "frontRight");
        backRight = hardwareMap.get(DcMotorEx.class, "backRight");

        // Reverse left drive motors so forward on joystick drives robot forward
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);

        frontLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // 2. Hardware Mapping - Mechanisms
        slurpItIntakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotorEx.class, "tranferMotor");
        flyWheels = hardwareMap.get(DcMotorEx.class, "flyWheels");
        gateServo = hardwareMap.get(Servo.class, "gateServo");
        flagPole = hardwareMap.get(Servo.class, "flagPole");

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

            // --- MECANUM DRIVE CONTROL (GAMEPAD 1 JOYSTICKS) ---
            double y = -gamepad1.left_stick_y;  // Forward/Backward (inverted on gamepad)
            double x = gamepad1.left_stick_x * 1.1; // Strafe Left/Right (1.1 multiplier compensates for strafe friction)
            double rx = gamepad1.right_stick_x; // Turn Left/Right

            // Calculate Mecanum wheel power values
            double frontLeftPower = y + x + rx;
            double backLeftPower = y - x + rx;
            double frontRightPower = y - x - rx;
            double backRightPower = y + x - rx;

            // Normalize powers so no motor value exceeds 1.0
            double max = Math.max(Math.abs(frontLeftPower), Math.max(Math.abs(backLeftPower),
                    Math.max(Math.abs(frontRightPower), Math.abs(backRightPower))));

            if (max > 1.0) {
                frontLeftPower /= max;
                backLeftPower /= max;
                frontRightPower /= max;
                backRightPower /= max;
            }

            // Output power to drive motors
            frontLeft.setPower(frontLeftPower);
            backLeft.setPower(backLeftPower);
            frontRight.setPower(frontRightPower);
            backRight.setPower(backRightPower);

            switch (currentState){
                case IDLE:
                    if (gamepad1.a){
                        //reset encoders for shooter here
                        //reset odometry pods here
                        frontLeft.setPower(0.0);
                        frontRight.setPower(0.0);
                        backLeft.setPower(0.0);
                        backRight.setPower(0.0);
                        currentState = RobotState.DEFENSE;
                        break;
                    }

                case DEFENSE:
                    if (gamepad1.x){
                        //stop odopods? or can it not pick up the location from the new spot once the case switches again

                    }
            }


        }}}
