package org.usfirst.team3132.frc2016.behaviors;

import org.usfirst.team3132.lib.util.SystemUtil;

public class ShootDemoShot extends Action{

	double prevTongueAngle = 0.0;
	boolean movedTongue = false;
	
	@Override
	public void run() {
		globalSingleton.shooterAutomated = true;
		// turn on flywheel
		shooter.setFlywheelTargetRPM(1000);
		
		// move tongue out of way
		if(shooter.getTongueCurrDeg() > 45){
			movedTongue = true;
			prevTongueAngle = shooter.getTongueCurrDeg();
			shooter.setTongueDeg(30);
		}
		
		// wait for things to get to speed/position
		while(!shooter.isReadyToShoot()){
			SystemUtil.fastLoopDelay();
		}
		
		// fire
		shooter.kickerExtend();
		SystemUtil.delay(200);
		shooter.kickerRetract();
		SystemUtil.delay(200);
		shooter.setFlywheelTargetRPM(0.0);
		
		// put tongue back if moved initially
		if(movedTongue){
			shooter.setTongueDeg(prevTongueAngle);
		}
		
		globalSingleton.shooterAutomated = false;
	}

}
