package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class delayedFullAuto extends LinearOpMode {
    private double targetShooterVel = 1120.0;

    private DcMotor intakeMotor;
    private DcMotor transferMotor;
    private DcMotorEx shooterMotor;
    private double shooterVel;

    private DcMotor frontLeftMotor;
    private DcMotor backLeftMotor;
    private DcMotor frontRightMotor;
    private DcMotor backRightMotor;
    private ElapsedTime timer = new ElapsedTime();

    private boolean autoIsOn = false;
    double x = -1.0  *1.1; //YEAH WHAT WE DNOT NEED THIS

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
        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        waitForStart();
        timer.reset();
        while (opModeIsActive() && (timer.seconds() < 5.0 )) {
            intakeMotor.setPower(0.0);
            transferMotor.setPower(0.0);
        }
        while (opModeIsActive() && (timer.seconds() < 10.0 )) {
            intakeMotor.setPower(0.0);
            transferMotor.setPower(0.0);
            shooterMotor.setVelocity(1180);
        }

            while (opModeIsActive() && (timer.seconds() < 10.5 )){ //MIGHT HAVE TO CHANGE THIS 5.5 VALUE DOWN
                intakeMotor.setPower(1.0); //check how long it takes before 2nd round is launched
                transferMotor.setPower(1.0);
                shooterMotor.setVelocity(1180);
            }
            while (opModeIsActive() && (timer.seconds() < 11.5 )){
                //leaving blank as a wait,
            }
            while (opModeIsActive() && (timer.seconds() < 15.0 )){
                intakeMotor.setPower(0.0);
                transferMotor.setPower(0.0);
                shooterMotor.setVelocity(1180);
            }
            while (opModeIsActive() && (timer.seconds() < 17.5 )){
                intakeMotor.setPower(1.0); //check how long it takes before 2nd round is launched
                transferMotor.setPower(1.0);
                shooterMotor.setVelocity(1180);
            }
            while (opModeIsActive() && (timer.seconds() < 18.6 )){ //moving forward time either adjust time or power
                frontLeftMotor.setPower(1.0);
                frontRightMotor.setPower(1.0);
                backLeftMotor.setPower(1.0);
                backRightMotor.setPower(1.0);
            }
            while (opModeIsActive() && (timer.seconds() < 20.6 )){
                frontLeftMotor.setPower(-1.0);
                frontRightMotor.setPower(1.0);
                backLeftMotor.setPower(1.0);
                backRightMotor.setPower(-1.0);
                //reverse FL and BR for left strafing
            }
            while (opModeIsActive() && (timer.seconds() < 23.2 )){ //moving forward time either adjust time or power to get into the park spot
                frontLeftMotor.setPower(1.0);
                frontRightMotor.setPower(1.0);
                backLeftMotor.setPower(1.0);
                backRightMotor.setPower(1.0);
            }
            while (opModeIsActive() && (timer.seconds() < 25.0 )){
                frontLeftMotor.setPower(-1.0);
                frontRightMotor.setPower(1.0);
                backLeftMotor.setPower(1.0);
                backRightMotor.setPower(-1.0);
                //reverse FL and BR for left strafing
            }
            while (opModeIsActive() && (timer.seconds() < 30 )){
                frontLeftMotor.setPower(0.0);
                frontRightMotor.setPower(0.0);
                backLeftMotor.setPower(0.0);
                backRightMotor.setPower(0.0);
            } //should I do a while loop or just say stop it without the while loop
            //turning left a bit

            telemetry.addData("Auto done :0) hehehae", "timer");
        }}
