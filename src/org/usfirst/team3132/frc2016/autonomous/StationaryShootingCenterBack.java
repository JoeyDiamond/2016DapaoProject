package org.usfirst.team3132.frc2016.autonomous;

import org.usfirst.team3132.frc2016.behaviors.Shoot3Shots;
import org.usfirst.team3132.lib.util.SystemUtil;

public class StationaryShootingCenterBack extends AutonomousRoutine{

	Thread thread;
	
	@Override
	public void init() {
		System.out.println("stationary shooting auto init finished");
	}

	@Override
	public void run() {
		System.out.println("stationary shooting auto started");

		thread = new Thread(new Shoot3Shots());
		thread.start();
		while(ds.isAutonomous() && thread.isAlive()){
			SystemUtil.slowLoopDelay();
		}
		
		System.out.println("stationary shooting auto ended");
		
	}

}
