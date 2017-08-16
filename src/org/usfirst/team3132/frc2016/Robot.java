
package org.usfirst.team3132.frc2016;

import org.usfirst.team3132.frc2016.autonomous.AutoChooserHelper;
import org.usfirst.team3132.frc2016.autonomous.AutonomousRoutine;
import org.usfirst.team3132.frc2016.autonomous.AutonomousRunner;
import org.usfirst.team3132.frc2016.autonomous.EmptyAutonomous;
import org.usfirst.team3132.frc2016.autonomous.ReversingShootingCenterBack;
import org.usfirst.team3132.frc2016.autonomous.StationaryShootingCenterBack;
import org.usfirst.team3132.frc2016.behaviors.Shoot3Shots;
import org.usfirst.team3132.frc2016.behaviors.ShootDemoShot;
import org.usfirst.team3132.frc2016.subsystems.Climber;
import org.usfirst.team3132.frc2016.subsystems.Drivebase;
import org.usfirst.team3132.frc2016.subsystems.Shooter;
import org.usfirst.team3132.frc2016.test.ClimberTest;
import org.usfirst.team3132.frc2016.test.DriveOutputTest;
import org.usfirst.team3132.frc2016.test.FlywheelTest;
import org.usfirst.team3132.frc2016.test.JoystickScalingTest;
import org.usfirst.team3132.frc2016.test.KickerTest;
import org.usfirst.team3132.frc2016.test.TestChooserHelper;
import org.usfirst.team3132.frc2016.test.TongueTest;
import org.usfirst.team3132.lib.LogitechGamepadF310;
import org.usfirst.team3132.lib.MultiLooper;
import org.usfirst.team3132.lib.Test;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;

public class Robot extends IterativeRobot {

	// robot parts
	GlobalSingleton globalSingleton = GlobalSingleton.getInstance();
  	Drivebase drivebase = Drivebase.getInstance();
	Shooter shooter = Shooter.getInstance();
	Climber climber = Climber.getInstance(); 
	MultiLooper multiLooper = new MultiLooper("subsystems",1.0/100.0);
	
	// human interfacing
	DriverStation ds = DriverStation.getInstance();
	LogitechGamepadF310 driverGamepad = new LogitechGamepadF310(0);
	LogitechGamepadF310 operatorGamepad = new LogitechGamepadF310(1);

	
	// Automated Routine Control
	Thread automated;
	
	// Autonomous control
	AutoChooserHelper autoChooser = new AutoChooserHelper();
	AutonomousRunner autoRunner = new AutonomousRunner();
	AutonomousRoutine autoRoutine;
	String routine;
	
	// test
	TestChooserHelper testChooser = new TestChooserHelper();
	Test test;
    
	
	
	public void robotInit() {
		multiLooper.addLoopable(drivebase);
		multiLooper.addLoopable(shooter); 
		autoChooser.create();
		testChooser.create();
		//SmartDashboard.putString("Test Mode", "null");
		
	}
	
	public void autonomousInit() {
		multiLooper.start();
		routine = autoChooser.getSelection();
		
		
		switch(routine){
			case Constants.autoStationary:
				autoRoutine = new StationaryShootingCenterBack();
				break;
			case Constants.autoReversing:
				autoRoutine = new ReversingShootingCenterBack();
				break;
			default:
				autoRoutine = new EmptyAutonomous();
				break;
		}
		
		autoRunner.setAutoRoutine(autoRoutine);
		autoRunner.start();
	}

    public void autonomousPeriodic() {
    	
    }
    

    public void teleopInit() {
    	autoRunner.kill();
    	
    	multiLooper.start();
      	shooter.setTongueToStorage();
    }
    
    public void teleopPeriodic() {
        // drive controls
    	if(!globalSingleton.autoDrive){
	    	drivebase.driveWheel(driverGamepad.getLeftYScaled(), 
	    			driverGamepad.getRightXScaled(), 
	    			driverGamepad.getLeftStickClick()|| driverGamepad.getRightStickClick() || driverGamepad.getLeftXScaled() == 0);
	    	if(driverGamepad.getTriggerLeftBtn()){
	    		drivebase.shiftLowGear();
	    	}
	    	if(driverGamepad.getLeftBumper()){
	    		drivebase.shiftHighGear();
	    	}
    	}
    	
    	// climber controls
    	if(driverGamepad.getYellowButton() || operatorGamepad.getYellowButton()){
    		climber.extend();
    	}
    	if(driverGamepad.getGreenButton() || operatorGamepad.getGreenButton()){
    		climber.retract();
    	}
    	
    	// shoot controls
    	if(!globalSingleton.shooterAutomated){
    		if(operatorGamepad.getTriggerRightBtn()){
    			automated = new Thread(new ShootDemoShot());
    			automated.start();
    		} else if(operatorGamepad.getTriggerLeftBtn()){
    			automated = new Thread(new Shoot3Shots());
    			automated.start();
    		}
    	}
    	
    }
    
    public void disabledInit() {
    	// TODO: test if this triggers
    	climber.retract();
    	
    	autoRunner.kill();
    	multiLooper.stop();
    	System.out.println("disabled init overloaded :)");
    }
    
    public void disabledPeriodic() {
    	
    }
    
    
    
    
    public void testInit() {
    	multiLooper.start();
    	
    	String selection = testChooser.getSelection();
    	
    	switch(selection){
    		case Constants.testDrivebase:
    			test = drivebase;
    			break;
    		case Constants.testFlywheel:
    			test = new FlywheelTest();
    			break;
    		case Constants.testTongue:
    			test = new TongueTest();
    			break;
    		case Constants.testClimber:
    			test = new ClimberTest();
    			break;
    		case Constants.testKicker:
    			test = new KickerTest();
    			break;
    		case Constants.testJoyScaling:
    			test = new JoystickScalingTest();
    			break;
    		case Constants.testDriveMath:
    			test = new DriveOutputTest();
    			break;
    	}
    	
    	test.testInit();
		
    }
    
    public void testPeriodic() {
    	
    	if(test != null){
    		test.testPeriodic();
    	} else {
    		System.out.println("null test case");
    	}
    }
    
    
}
