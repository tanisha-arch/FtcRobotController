package org.firstinspires.ftc.teamcode.auto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name="Shoot4AndDriveForward", group="BioBuzz")
public class auto extends LinearOpMode {

    // --- DRIVETRAIN MOTORS ---
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;

    // --- SHOOTER & TRANSFER MECHANISMS ---
    private DcMotor flyWheels = null;
    private DcMotor transferMotor = null;
    private Servo gateServo = null;

    // Servo positions
    private static final double GATE_CLOSED = 0.00;
    private static final double GATE_OPEN = 1.00;

    private final ElapsedTime runtime = new ElapsedTime();

    @Override
    public void runOpMode() {

        // 1. HARDWARE MAPPING
        leftDrive     = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive    = hardwareMap.get(DcMotor.class, "right_drive");
        flyWheels     = hardwareMap.get(DcMotor.class, "flyWheels");
        transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        gateServo     = hardwareMap.get(Servo.class, "gateServo");

        // 2. MOTOR DIRECTIONS & BRAKING
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        leftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        rightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        flyWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        // Explicitly set drive motors to 0 power initially so the robot stays still
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);
        gateServo.setPosition(GATE_CLOSED);

        telemetry.addData("Status", "Initialized! Press PLAY to start.");
        telemetry.update();

        // 3. WAIT FOR START (Robot stays completely still)
        waitForStart();
        runtime.reset();

        if (!opModeIsActive()) return;

        // =========================================================================
        // STEP 1: SHOOT 4 BALLS FROM STARTING POSITION (ROBOT DOES NOT MOVE)
        // =========================================================================
        
        // Ensure drive motors remain OFF while shooting
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        // A. Rev up flywheels
        telemetry.addData("Auto Status", "Revving Flywheels...");
        telemetry.update();
        flyWheels.setPower(0.85); // Turn on flywheel
        sleep(1000);              // Wait 1 second to reach full speed

        // B. Shoot 4 balls by cycling gate and transfer motor 4 times
        for (int i = 1; i <= 4; i++) {
            if (!opModeIsActive()) break;

            telemetry.addData("Auto Status", "Shooting Ball %d of 4", i);
            telemetry.update();

            // Open gate & run transfer to feed 1 ball
            gateServo.setPosition(GATE_OPEN);
            transferMotor.setPower(0.85);
            sleep(1200); // Wait 1.2 sec for ball to shoot

            // Close gate & stop transfer briefly so flywheels regain speed
            gateServo.setPosition(GATE_CLOSED);
            transferMotor.setPower(0.0);
            sleep(500);  // 0.5 sec pause between shots
        }

        // C. Turn off shooter mechanisms
        flyWheels.setPower(0.0);
        transferMotor.setPower(0.0);
        gateServo.setPosition(GATE_CLOSED);

        // =========================================================================
        // STEP 2: DRIVE STRAIGHT FORWARD FOR 1 SECOND
        // =========================================================================
        telemetry.addData("Auto Status", "Driving Straight Forward for 1 second...");
        telemetry.update();

        // Set equal power (50%) to both wheels to move straight forward
        leftDrive.setPower(0.5);
        rightDrive.setPower(0.5);

        runtime.reset();
        while (opModeIsActive() && runtime.seconds() < 1.0) {
            telemetry.addData("Path", "Driving Straight: %.1f / 1.0 sec", runtime.seconds());
            telemetry.update();
        }

        // STEP 3: STOP ALL MOTORS AT THE END
        leftDrive.setPower(0.0);
        rightDrive.setPower(0.0);

        telemetry.addData("Auto Status", "Complete!");
        telemetry.update();
        sleep(1000);
    }
}


