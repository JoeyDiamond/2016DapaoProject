package org.usfirst.team3132.frc2016.behaviors;

import org.usfirst.team3132.lib.util.SystemUtil;

import edu.wpi.first.wpilibj.Timer;

public class Shoot3Shots extends Action{

	double prevTongueAngle = 0.0;
	boolean movedTongue = false;
	
	Timer t = new Timer();
	
	@Override
	public void run() {
		t.reset();
		t.start();
		globalSingleton.shooterAutomated = true;
		globalSingleton.drivebaseAllowedToDrive = false;
		// turn on flywheel
		shooter.setFlywheelTargetRPM(2000);
		
		// move tongue out of way
		if(shooter.getTongueCurrDeg() > 45){
			movedTongue = true;
			prevTongueAngle = shooter.getTongueCurrDeg();
			shooter.setTongueDeg(30);
		}
		
		// fire 3 shots in rapid mode
		for(int i=0;i<3;i++){
			// wait for things to get to speed/position
			while(!shooter.isReadyToShoot()){
				SystemUtil.fastLoopDelay();
			}
			
			System.out.println("firing shot: " + i + " at " + t.get());
			
			// fire
			shooter.kickerExtend();
			SystemUtil.delay(200);
			shooter.kickerRetract();
			SystemUtil.delay(100);
		}
		
		// delay for last frisbee to exit shooter
		SystemUtil.delay(200);
		
		// alow drive controls again
		globalSingleton.drivebaseAllowedToDrive = true;
		
		// turn off flywheel
		shooter.setFlywheelTargetRPM(0.0);
		
		// print out interesting info
		System.out.println("total time to shoot: " + t.get() + " seconds");
		
		// put tongue back if moved initially
		if(movedTongue){
			shooter.setTongueDeg(prevTongueAngle);
		}

		globalSingleton.shooterAutomated = false;
	}

}
