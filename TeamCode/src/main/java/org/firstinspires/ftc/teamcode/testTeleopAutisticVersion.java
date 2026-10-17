package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

// Code for testing the transfer, shooter, and automated gate servo cycle.
@TeleOp(name = "Test TELEOP Gate Cycle", group = "Linear OpMode")
public class testTeleopAutisticVersion extends LinearOpMode {

    // Hardware Declarations
    private DcMotor transferMotor;
    private DcMotor flyWheels;
    private Servo gateServo;
    private Servo flagPole;

    // Servo & Motor Constants
    private static final double TRANSFER_SPEED = 0.85;
    private static final double GATE_CLOSED_POS = 0.20; // Adjust for your closed position
    private static final double GATE_OPEN_POS = 0.80;   // Adjust for your open position

    // Timing Constants (in seconds)
    private static final double CLOSED_DURATION = 3.0; // Stay closed for 3.0 seconds
    private static final double OPEN_DURATION = 2.5;   // Stay open for 2.5 seconds

    // State Machine Enum for Gate Cycle
    public enum GateState {
        CLOSED,
        OPEN
    }

    private GateState gateState = GateState.CLOSED;
    private final ElapsedTime gateTimer = new ElapsedTime();

    @Override
    public void runOpMode() {

        // 1. Hardware Mapping
        transferMotor = hardwareMap.get(DcMotor.class, "transfer_motor");
        flyWheels = hardwareMap.get(DcMotor.class, "flywheels_motor");
        gateServo = hardwareMap.get(Servo.class, "gate_servo");
        flagPole = hardwareMap.get(Servo.class, "flag_pole_servo");

        // 2. Motor Directions & Braking Configuration
        transferMotor.setDirection(DcMotor.Direction.FORWARD);
        transferMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        flyWheels.setDirection(DcMotor.Direction.FORWARD);
        flyWheels.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addData("Status", "Initialized! Press PLAY to start cycle.");
        telemetry.update();

        waitForStart();

        // Reset timer when match starts
        gateTimer.reset();

        while (opModeIsActive()) {

            // --- GATE & TRANSFER STATE MACHINE ---
            switch (gateState) {

                case CLOSED:
                    // 1. Set servo to closed position & stop transfer
                    gateServo.setPosition(GATE_CLOSED_POS);
                    transferMotor.setPower(0.0);

                    // 2. Wait for 3.0 seconds, then switch to OPEN
                    if (gateTimer.seconds() >= CLOSED_DURATION) {
                        gateState = GateState.OPEN;
                        gateTimer.reset(); // Reset timer for open state duration
                    }
                    break;

                case OPEN:
                    // 1. Set servo to open position & run transfer motor
                    gateServo.setPosition(GATE_OPEN_POS);
                    transferMotor.setPower(TRANSFER_SPEED);

                    // 2. Wait for 2.5 seconds, then switch back to CLOSED
                    if (gateTimer.seconds() >= OPEN_DURATION) {
                        gateState = GateState.CLOSED;
                        gateTimer.reset(); // Reset timer for closed state duration
                    }
                    break;
            }

            // --- SHOOTER / FLYWHEELS CONTROL ---
            // Example: Control shooter using triggers or buttons
            if (gamepad1.right_trigger > 0.1) {
                flyWheels.setPower(gamepad1.right_trigger); // Variable shooter speed
            } else {
                flyWheels.setPower(0.0);
            }

            // --- TELEMETRY ---
            telemetry.addData("Gate State", gateState);
            telemetry.addData("Gate State Time", "%.1f / %.1f sec", 
                    gateTimer.seconds(), 
                    (gateState == GateState.CLOSED) ? CLOSED_DURATION : OPEN_DURATION);
            telemetry.addData("Transfer Power", "%.2f", transferMotor.getPower());
            telemetry.addData("Flywheels Power", "%.2f", flyWheels.getPower());
            telemetry.update();
        }
    }
}
