package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class blueAuto extends LinearOpMode {
    private double targetShooterVel = 1120.0;


    private DcMotor intakeMotor;
    private DcMotor transferMotor;
    private DcMotorEx shooterMotor;
    private double shooterVel;

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private  ElapsedTime timer = new ElapsedTime();
    private static final double moveForwardTime = 1.6;
    private boolean autoIsOn = false;

    public void runOpMode() {
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");
        transferMotor = hardwareMap.get(DcMotor.class, "transferMotor");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooterMotor");

        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeft");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeft");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRight");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRight");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        timer.reset();
        if (timer.seconds()>0.8);
            intakeMotor.setPower(.0);
            transferMotor.setPower(.0);
            shooterMotor.setVelocity(1180);


        while (opModeIsActive() && (timer.seconds() > 5.0 )){
            intakeMotor.setPower(1.0);
            transferMotor.setPower(1.0);
            shooterMotor.setVelocity(1180);
        }
        while (opModeIsActive() && (timer.seconds() > 5.5 )){
            intakeMotor.setPower(0.0);
            transferMotor.setPower(0.0);
            shooterMotor.setVelocity(1180);
        }
        while (opModeIsActive() && (timer.seconds() > 5.8 )){
            intakeMotor.setPower(1.0);
            transferMotor.setPower(1.0);
            shooterMotor.setVelocity(1180);
        }
        while (opModeIsActive() && (timer.seconds() > 7.0 )){
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }
        //turning left a bit
        while (opModeIsActive() && (timer.seconds() < 7.0 )){
            frontLeftMotor.setPower(-0.5);
            frontRightMotor.setPower(0.5);
            backLeftMotor.setPower(-0.5);
            backRightMotor.setPower(0.5);
        }
        while (opModeIsActive() && (timer.seconds() > 7.4 )){
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }
        while (opModeIsActive() && (timer.seconds() < 8.6 )){
            frontLeftMotor.setPower(0.5);
            frontRightMotor.setPower(-0.5);
            backLeftMotor.setPower(0.5);
            backRightMotor.setPower(-0.5);
        }
        while (opModeIsActive() && (timer.seconds() < 7.0 )){
            frontLeftMotor.setPower(-0.5);
            frontRightMotor.setPower(0.5);
            backLeftMotor.setPower(-0.5);
            backRightMotor.setPower(0.5);
        }
        while (opModeIsActive() && (timer.seconds() > 9.1 )){
            frontLeftMotor.setPower(1.0);
            frontRightMotor.setPower(1.0);
            backLeftMotor.setPower(1.0);
            backRightMotor.setPower(1.0);
        }
    }}

//helloo???
