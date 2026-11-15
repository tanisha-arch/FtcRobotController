package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class bestAutoEver extends LinearOpMode {
    private double targetShooterVel = 1180.0;
    private double minShooterVel = 1160.0;
    private DcMotor intakeMotor;
    private DcMotor transferMotor;
    private DcMotorEx shooterMotor;
    private Servo flowerServo;
    private double shooterVel;
    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private DcMotor lordFarquadsBumHoleMotor;
    private ElapsedTime timer = new ElapsedTime();
    private boolean autoIsOn = false;
    double x = -1.0 * 1.1; //YEAH WHAT WE DNOT NEED THIS

    // PIDF Constants for the Shooter
    public static double P = 4.2;
    public static double I = 0.23;
    public static double D = 0.05;
    public static double F = 6.8;

    RevHubOrientationOnRobot.LogoFacingDirection logoDirection = RevHubOrientationOnRobot.LogoFacingDirection.LEFT;

    public void runOpMode() throws InterruptedException {
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooterMotor");
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");
        lordFarquadsBumHoleMotor = hardwareMap.get(DcMotor.class, "counterMotor");
        flowerServo = hardwareMap.get(Servo.class,"flowerStick");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // Apply custom PIDF tuning to shooterMotor
        shooterMotor.setVelocityPIDFCoefficients(P, I, D, F);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        timer.reset();

        while (opModeIsActive() && (timer.seconds() < 6.0)) {
            intakeMotor.setPower(0.5);
            transferMotor.setPower(0.6);
            shooterMotor.setVelocity(targetShooterVel);
            lordFarquadsBumHoleMotor.setPower(-1.0);
            if (shooterMotor.getVelocity() <= targetShooterVel && shooterMotor.getVelocity()> minShooterVel) {
                transferMotor.setPower(1.0);
            }
            while (opModeIsActive() && (timer.seconds() < 6.55)) {
                frontLeftMotor.setPower(0.35);
                frontRightMotor.setPower(0.35);
                backLeftMotor.setPower(0.35);
                backRightMotor.setPower(0.35);
            }

            while (opModeIsActive() && (timer.seconds() < 7.1)){
                frontLeftMotor.setPower(-0.35);
                frontRightMotor.setPower(0.35);
                backLeftMotor.setPower(0.35);
                backRightMotor.setPower(-0.35); //left strafing
            }
            while (opModeIsActive() && (timer.seconds() < 7.4))
            frontLeftMotor.setPower(-0.15);
            frontRightMotor.setPower(0.25);
            backLeftMotor.setPower(-0.15);
            backRightMotor.setPower(0.25); //turning it so it faces the flower
        }
            while (opModeIsActive() && (timer.seconds() < 9)){
                flowerServo.setPosition(1.0); //lowering arm
                frontLeftMotor.setPower(0.35);
                frontRightMotor.setPower(0.35);
                backLeftMotor.setPower(0.35);
                backRightMotor.setPower(0.35);
                intakeMotor.setPower(1.0);
                transferMotor.setPower(0.6);
            }
            while (opModeIsActive() && (timer.seconds() < 9.25)){
                flowerServo.setPosition(0.0);
                frontLeftMotor.setPower(-0.35);
                frontRightMotor.setPower(-0.35);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(-0.35); //back a little bit off the flower
            }
        while (opModeIsActive() && (timer.seconds() < 9.55)){
            frontLeftMotor.setPower(-0.35);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(-0.35);
            backRightMotor.setPower(0); //turn backwards
        }
        while (opModeIsActive() && (timer.seconds() < 10.5)){
            frontLeftMotor.setPower(-0.35);
            frontRightMotor.setPower(-0.35);
            backLeftMotor.setPower(-0.35);
            backRightMotor.setPower(-0.35); //move backwards for 1.2 s
        }
        while (opModeIsActive() && (timer.seconds() < 11)){
            frontLeftMotor.setPower(-0.35);
            frontRightMotor.setPower(0.35);
            backLeftMotor.setPower(0.35);
            backRightMotor.setPower(-0.35); //strafe left
        }
        while (opModeIsActive() && (timer.seconds() < 12)){
            frontLeftMotor.setPower(-0.35);
            frontRightMotor.setPower(-0.35);
            backLeftMotor.setPower(-0.35);
            backRightMotor.setPower(-0.35); //back into wal for next shooting
        }

        while (opModeIsActive() && (timer.seconds() < 15)){
        intakeMotor.setPower(0.5);
        transferMotor.setPower(0.6);
        shooterMotor.setVelocity(targetShooterVel);
        lordFarquadsBumHoleMotor.setPower(-1.0);
        if (shooterMotor.getVelocity() <= targetShooterVel && shooterMotor.getVelocity()> minShooterVel) {
            transferMotor.setPower(1.0);
        }
        while (opModeIsActive() && (timer.seconds() < 15.34)){
            frontLeftMotor.setPower(0.35);
            frontRightMotor.setPower(0.35);
            backLeftMotor.setPower(0.35);
            backRightMotor.setPower(0.35);
        }
        while (opModeIsActive() && (timer.seconds() < 16.7)){
            frontLeftMotor.setPower(0.35);
            frontRightMotor.setPower(0);
            backLeftMotor.setPower(0.35);
            backRightMotor.setPower(0); //turning 180
            }
        while (opModeIsActive() && (timer.seconds() < 16.9)){
            frontLeftMotor.setPower(-0.35);
            frontRightMotor.setPower(0.35);
            backLeftMotor.setPower(0.35);
            backRightMotor.setPower(-0.35); //strafing left
        }
        while (opModeIsActive() && (timer.seconds() < 18)){
                frontLeftMotor.setPower(0.35);
                frontRightMotor.setPower(0.35);
                backLeftMotor.setPower(0.35);
                backRightMotor.setPower(0.35); //drive into flower
                flowerServo.setPosition(1.0);
                intakeMotor.setPower(1.0);
                transferMotor.setPower(0.6);
;            }
        while (opModeIsActive() && (timer.seconds() < 18.25)){
                frontLeftMotor.setPower(-0.35);
                frontRightMotor.setPower(-0.35);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(-0.35); //back a little bit off the flower
            }
        while (opModeIsActive() && (timer.seconds() < 18.5)){
                flowerServo.setPosition(0.0);
                frontLeftMotor.setPower(0.35);
                frontRightMotor.setPower(-0.35);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(0.35); //strafe right
            }
        while (opModeIsActive() && (timer.seconds() < 19.2)){
                frontLeftMotor.setPower(-0.35);
                frontRightMotor.setPower(0);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(0); //turning 180
            }
        while (opModeIsActive() && (timer.seconds() < 19.78)){
                frontLeftMotor.setPower(-0.35);
                frontRightMotor.setPower(-0.35);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(-0.35); //back into wall for shooting again
            }
        while (opModeIsActive() && (timer.seconds() < 25)){
                intakeMotor.setPower(0.5);
                transferMotor.setPower(0.6);
                shooterMotor.setVelocity(targetShooterVel);
                lordFarquadsBumHoleMotor.setPower(-1.0);
                if (shooterMotor.getVelocity() <= targetShooterVel && shooterMotor.getVelocity()> minShooterVel) {
                    transferMotor.setPower(1.0);
                }
        while (opModeIsActive() && (timer.seconds() < 26)){
            frontLeftMotor.setPower(0.35);
            frontRightMotor.setPower(0.35);
            backLeftMotor.setPower(0.35);
            backRightMotor.setPower(0.35); //turning 180
    }
        while (opModeIsActive() && (timer.seconds() < 27.7)){
                frontLeftMotor.setPower(0.35);
                frontRightMotor.setPower(-0.35);
                backLeftMotor.setPower(-0.35);
                backRightMotor.setPower(0.35); //turning 180
            }
        while (opModeIsActive() && (timer.seconds() < 30)){
            frontLeftMotor.setPower(0.0);
            frontRightMotor.setPower(0.0);
            backLeftMotor.setPower(0.0);
            backRightMotor.setPower(0.0);
            intakeMotor.setPower(0.0);
            transferMotor.setPower(0.0);
            shooterMotor.setVelocity(0);
            flowerServo.setPosition(0.0);
            lordFarquadsBumHoleMotor.setPower(0.0);
        }

}}}}
